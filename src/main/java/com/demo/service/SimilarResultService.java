package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import com.demo.dao.SimilarResultDao;
import com.demo.object.NumScore;
import com.hankcs.hanlp.mining.word2vec.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 05/03/2018
 * Description:
 */

@Service
public class SimilarResultService {

    @Autowired
    public SimilarResultDao similarResultDao;

    @Autowired
    public DocVector docvector;

    @Autowired
    public DmService dmService;

    @Autowired
    public LTPService ltpService;

    @Autowired
    SdpService sdpService;

    @Autowired
    SdpSimilarService sdpSimilarService;

        public JSONArray getAnswer(String query,Boolean dmif,int method)  {
            ArrayList<Integer> queNum;

            int qDm = dmService.getDm(query);
            if (dmif) {
               queNum  = similarResultDao.getNumFromDm(qDm);

            }else {
                 queNum = similarResultDao.getNumList();

            }

            JSONArray jsonArray = new JSONArray();
            int size = queNum.size();
            ArrayList<NumScore> relist;
            if (method==1){
                relist = getWord2VecNumScore(queNum,query);

            }else if(method ==2){
                relist= getSdpNumScore(queNum,query);
            }else {
                relist = getWord2VecNumScore(queNum,query);
            }


            Collections.sort(relist);

            int maxsize;
            if (size>=3){
                maxsize =3;
            }else {
                maxsize = size;
            }
            if (size>0){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("re",maxsize+1);
                jsonArray.add(jsonObject);
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("re",0);
                jsonArray.add(jsonObject);
                return jsonArray;
            }

            for (int i=0;i<maxsize;i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("num",relist.get(i).getNum());
                jsonObject.put("dm",similarResultDao.getDm(relist.get(i).getNum()));
                jsonObject.put("questionTitle",similarResultDao.getTitle(relist.get(i).getNum()));
                jsonObject.put("questionContent",similarResultDao.getContent(relist.get(i).getNum()));
                jsonObject.put("response",similarResultDao.getAnswer(relist.get(i).getNum()));
                jsonObject.put("result",relist.get(i).getScore());
                jsonArray.add(jsonObject);
            }

            return jsonArray;
        }




    public ArrayList<NumScore> getWord2VecNumScore( ArrayList<Integer> list, String query){
        ArrayList<NumScore> re = new ArrayList<NumScore>();
        Vector target = docvector.query(query);
        int size = list.size();
        for (int i=0;i<size;i++){
            String vector = similarResultDao.getVecFromNum(list.get(i));
            if (vector.equals("")){
                System.out.println(list.get(i));
                continue;}
           float temp=docvector.vecstrToVec(vector).cosineForUnitVector(target);
            NumScore ns = new NumScore(list.get(i));
            ns.setScore(temp);
            synchronized (re){
            if (!re.contains(ns))
                re.add(ns);
            }
        }
        return re;
    }

    public ArrayList<NumScore> getSdpNumScore(ArrayList<Integer> list, String query){
        List<String[]> queHanledquery = sdpService.handleQuery(query);
        ArrayList<NumScore> re = new ArrayList<NumScore>();

        int size = list.size();
        for (int i=0;i<size;i++){
            String contemp= similarResultDao.getSdp_reFromNum(list.get(i));
            List<String[]> data = sdpSimilarService.getSdp_List(contemp);
            if (contemp!=null&!contemp.equals("")){
                NumScore numScore = new NumScore(list.get(i));
             numScore.setScore(sdpSimilarService.calLongSeqList(data,queHanledquery));
             re.add(numScore);
                System.out.println(list.get(i)+"is over");
            }

        }


        return  re;
    }

    public float getWord2VecSimilarity(String s1,String s2){
        float m ;
        Vector v1 = docvector.query(s1);
        Vector v2 = docvector.query(s2);

        if (v1==null||v2==null){
            m=0;
        }else {
            return v1.cosineForUnitVector(v2);
        }
        return m;
    }

    public JSONObject getCompareRe(String s1, String s2){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sdp",sdpSimilarService.clauseRe(s1,s2));
        jsonObject.put("vec",getWord2VecSimilarity(s1,s2));
        ArrayList<String> queryI = sdpSimilarService.que_coreI;
        ArrayList<String> queryII = sdpSimilarService.que_coreII;
        jsonObject.put("queryIAll",sdpSimilarService.getSdpAnalys(s1).split("\n"));
        jsonObject.put("queryIIAll",sdpSimilarService.getSdpAnalys(s2).split("\n"));
        jsonObject.put("queryI",queryI);
        jsonObject.put("queryII",queryII);


        return  jsonObject;
    }


}
