package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import com.alibaba.fastjson.JSONPObject;
import com.demo.dao.SimilarResultDao;
import com.hankcs.hanlp.mining.word2vec.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public DspSimilarService dspsimilarService;

        public JSONArray getAnswer(String query,Boolean dmif)  {
            ArrayList<Integer> queNum;

            int qDm = dmService.getDm(query);
            if (dmif) {
               queNum  = similarResultDao.getNumFromDm(qDm);

            }else {
                 queNum = similarResultDao.getNumList();

            }

            JSONArray jsonArray = new JSONArray();
            int size = queNum.size();

            int[] num=new int[3];
            float[] result=new float[3];

            float temp=0;
            int tempnum=0;
            Vector target = docvector.query(query);


            for (int i=0;i<size;i++){
                tempnum=i;
                String vector = similarResultDao.getVecFromNum(queNum.get(i));
                if (vector.equals("")){
                    System.out.println(queNum.get(i));
                    continue;}
                temp=docvector.vecstrToVec(vector).cosineForUnitVector(target);
                if (temp>result[2]){
                    num[2] = tempnum;
                    result[2] = temp;
                    if (temp>result[1]){
                        result[2] = result[1];
                        result[1] = temp;
                        num[2] = num[1];
                        num[1] = tempnum;
                        if(temp>result[0]){
                            result[1]=result[0];
                            result[0] = temp;
                            num[1] = num[0];
                            num[0] = tempnum;
                        }
                    }

                }
            }
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
