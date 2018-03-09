package com.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.content.Constant;
import com.demo.dao.QAEntity;
import com.demo.dao.SimilarResultDao;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 08/03/2018
 * Description:
 */
@Service
public class SeqVector {
    @Autowired
    SimilarResultDao similarResultDao;

   @Autowired
    DocVector docVector;
    public ArrayList<QAEntity> question;



    public String getSeqVector(int num,String queue) throws Exception {

        StringBuffer seqVector =new StringBuffer();
        Vector vector = docVector.query(queue);
                if (vector==null){
                    System.out.println(num);
                    return "";
                };


        if (vector.elementArray==null){
            System.out.println("elementArray is null");
        }

        for (float f:vector.elementArray) {
            seqVector.append(String.valueOf(f)+"\t");
        }
        System.out.println("<----"+num+"is ok---->");
        return seqVector.toString();

    }


    public void insertVector() throws Exception {
    if (question==null){
        question =  similarResultDao.getQAEntity();}
        int limit = question.size();
        for (int i=0;i<limit;i++){
            String queue = question.get(i).getQuestionContent().trim()+question.get(i).getQuestionTitle().trim();

            String vector = getSeqVector(question.get(i).getNum(),queue);
           // System.out.println(vector);
            similarResultDao.setVector(question.get(i).getNum(),vector);

        }
    }
}
