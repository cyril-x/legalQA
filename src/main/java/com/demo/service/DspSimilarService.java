package com.demo.service;

import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.beans.factory.annotation.Autowired;

public class DspSimilarService {

    @Autowired
    public LTPService ltpService;

    @Autowired
    public WordVectorModel wordVectorModel;

    public float getSimilarity(String queryI,String queryII){
        float result=0;
        String answerI = ltpService.getResult("sdp","plain",queryI);
        String answerII = ltpService.getResult("sdp","plain",queryII);
        result=cal(answerI,answerII);
        return result;
    }

    public String[] getSdpSeq(String answer){
        String[] re = answer.split("\n");
        return re;
    }

    public String[] getSdpTerm(String seq){
        String[] term = seq.split(" ");
        return term;
    }

    public String getRoot(String query){
        String[] seq=getSdpSeq(query);
        String m="can't find root";
        for (int i=0;i<seq.length;i++){
            String[] temp = getSdpTerm(seq[i]);
            if (temp[2].equals("Root")){
                return temp[0];
            }
        }
        return m;
    }

    public float cal(String queryI,String queryII){
        int count=0;
       String rootI =  getRoot(queryI);
       String rootII = getRoot(queryII);
       if (rootI.equals("can't find root")||rootII.equals("can't find root")){
           return 0;
       }
       String[] s1 = getSdpSeq(queryI);
       String[] s2 = getSdpSeq(queryII);
        float re = 0;
       for (String t1: s1){
           for (String t2:s2){
               if (!t1.equals("")&&!t2.equals("")){
               String[] terms1 = getSdpTerm(t1);
               String[] terms2 = getSdpTerm(t2);
               if (terms1[1].equals(rootI)&&terms2[1].equals(rootII)){
                   if (terms1[2].equals(terms2[2])) {
                       re += wordVectorModel.similarity(terms1[0].split("_")[0], terms2[0].split("_")[0]);
                        count++;
                   }}}
           }
       }
       return re /count;

    }



}
