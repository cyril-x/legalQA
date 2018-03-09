package com.demo.service;

import com.demo.constant.Constant;
import com.demo.dao.QAEntity;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Vector;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 09/03/2018
 * Description:
 */
@Service
public class DocVector {

    public DocVectorModel docVectorModel;

    ArrayList<QAEntity> question;

    public DocVector() throws IOException {
        WordVectorModel wordVectorModel = new WordVectorModel(Constant.wiki_vec_url);
        docVectorModel = new DocVectorModel(wordVectorModel);
    }
    public float similarity(String sone,String stwo){
        return docVectorModel.similarity(sone,stwo);
    }

    public Vector query(String ss){
        return docVectorModel.query(ss);
    }
}
