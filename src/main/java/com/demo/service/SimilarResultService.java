package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import com.demo.dao.SimilarResultDao;
import com.demo.object.NumScore;
import com.hankcs.hanlp.mining.word2vec.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


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
    public SdpSimilarService dspsimilarService;

        public JSONArray getAnswer(String query,Boolean dmif,Boolean sdpif)  {
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
            if (sdpif){
                relist = getSdpNumScore(queNum,query);
            }else {
                relist= getWord2VecNumScore(queNum,query);
            }
            int[] num=new int[3];
            float[] result=new float[3];

            float temp=0;
            int tempnum=0;
            Vector target = docvector.query(query);

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
                jsonObject.put("num",queNum.get(num[i]));
                jsonObject.put("dm",similarResultDao.getDm(queNum.get(num[i])));
                jsonObject.put("questionTitle",similarResultDao.getTitle(queNum.get(num[i])));
                jsonObject.put("questionContent",similarResultDao.getContent(queNum.get(num[i])));
                jsonObject.put("response",similarResultDao.getAnswer(queNum.get(num[i])));
                jsonObject.put("result",result[i]);
                jsonArray.add(jsonObject);
            }

            return jsonArray;
        }


    public float getSdpSimilarity(String s1,String s2){
            return dspsimilarService.getSdpSimilarity(s1,s2);
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
        ArrayList<NumScore> re = new ArrayList<NumScore>();
        int size = list.size();
        for (int i=0;i<size;i++){
            String quetemp= similarResultDao.getContent(list.get(i))+similarResultDao.getTitle(list.get(i));
            //TODO 未完待续。。。先把结果插入到数据库的。。。。。
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
        jsonObject.put("sdp",dspsimilarService.clauseRe(s1,s2));
        jsonObject.put("vec",getWord2VecSimilarity(s1,s2));
        ArrayList<String> queryI = dspsimilarService.que_coreI;
        ArrayList<String> queryII = dspsimilarService.que_coreII;
        jsonObject.put("queryIAll",dspsimilarService.getSdpAnalys(s1).split("\n"));
        jsonObject.put("queryIIAll",dspsimilarService.getSdpAnalys(s2).split("\n"));
        jsonObject.put("queryI",queryI);
        jsonObject.put("queryII",queryII);


        return  jsonObject;
    }


}
