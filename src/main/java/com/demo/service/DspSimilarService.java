package com.demo.service;

import com.demo.object.VecCell;
import com.demo.util.VecCal;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DspSimilarService {

    public List<List<String>> reList= new ArrayList<List<String>>();

    public String answerI;
    public String answerII;

    @Autowired
    public LTPService ltpService;

    @Autowired
    public WordVectorModel wordVectorModel;

    private VecCal vecCal = new VecCal();

    public  DspSimilarService(){
        reList.add(new ArrayList<String>());
        reList.add(new ArrayList<String>());
    }

    public float getSimilarity(String queryI,String queryII){
        float result=0;
        answerI = ltpService.getResult("sdp","plain",queryI);
        answerII = ltpService.getResult("sdp","plain",queryII);
        result=calDspVec(answerI,answerII);
        return result;
    }

    public String getSdpAnalys(String s){
        return ltpService.getResult("sdp","plain",s);
    }


    private String[] getSdpSeq(String answer){
        String[] re = answer.split("\n");
        return re;
    }

    private String[] getSdpTerm(String seq){
        String[] term = seq.split(" ");
        return term;
    }

    private String getRoot(String query){
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
        reList.get(0).clear();
        reList.get(1).clear();
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
                       reList.get(0).add(terms1[0]);
                       reList.get(1).add(terms2[0]);
                       re += wordVectorModel.similarity(terms1[0].split("_")[0], terms2[0].split("_")[0]);
                        count++;
                   }}}
           }
       }

       return re /count;

    }

    public String[] getSArrray(String query){
        String root = getRoot(query);
        if(root.equals("can't find root")){
            return null;
        }
        String[] s1 = getSdpSeq(query);
        return  getTermFromSeq(s1,root);
    }

    public float calDspVec(String queryI,String queryII){
        float re = 0;
        int count=0;
        String[] s1Vec = getSArrray(queryI);

        String[] s2Vec = getSArrray(queryII);
        if(s1Vec==null||s2Vec==null){
            return 0;
        }
        float[][] calVec = new float[s1Vec.length][s2Vec.length];
        ArrayList<VecCell> list = new ArrayList<VecCell>();
        for (int i=0;i<s1Vec.length;i++){
            for (int j=0;j<s2Vec.length;j++){
                calVec[i][j]=wordVectorModel.similarity(s1Vec[i],s2Vec[j]);
                VecCell temp = new VecCell();
                temp.setKey(calVec[i][j]);
                temp.setX(i);
                temp.setY(j);
                list.add(temp);
            }
        }
        Collections.sort(list);
        System.out.println(list);

        while (!list.isEmpty()){

                re+= vecCal.getMaxClearXY(list);
                count++;

        }

        return re/count;
    }

    private String[] getTermFromSeq(String[] answer,String root){

        ArrayList<String> re = new ArrayList<String>();

        for (String an:answer){
            if (!an.equals("")) {
                String[] temp = getSdpTerm(an);
                //System.out.println(an);
                if (temp[1].equals(root)){
                re.add(temp[0].split("_")[0]);
                }
            }
        }

        return  (String[]) re.toArray(new String[re.size()]);
    }



}
