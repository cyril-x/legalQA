package com.demo.service;

import com.demo.content.Constant;
import com.demo.dao.QAEntity;
import com.demo.dao.SimilarResultDao;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public   SimilarResultDao similarResultDao;

    @Autowired
    public DocVector docvector;


    public  ArrayList<QAEntity> question;




        public JSONArray getAnswer(String queue) throws Exception {
        if (question==null){
            question =  similarResultDao.getQAEntity();}
            JSONArray jsonArray = new JSONArray();

            int[] num=new int[3];
            float[] result=new float[3];
            float temp=0;
            int tempnum=0;
           // System.out.println(docVectorModel.similarity("公司拖欠工资，如何要回？", "怎么要回被拖欠的工资"));
            for (int i=0;i<question.size();i++){
                if(i%1000==0){
                    System.out.println(i+"ddd");
                }
                tempnum = i;
               temp=docvector.similarity(queue,question.get(i).getQuestionContent());
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
//            //System.out.println(docVectorModel.similarity("山西副省长贪污腐败开庭", "股票基金增长"));
            for (int i=0;i<3;i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("num",num[i]);
                jsonObject.put("questionContent",question.get(num[i]).getQuestionContent());
                jsonObject.put("response",question.get(num[i]).getBestResponse());
                jsonObject.put("result",result[i]);
                jsonArray.add(jsonObject);
            }

            return jsonArray;
        }





}
