package com.demo.service;

import com.demo.object.VecCell;
import com.demo.util.VecCal;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class SdpSimilarService {

    public List<List<String>> reList= new ArrayList<List<String>>();

    public ArrayList<String> que_coreI = new ArrayList<String>();
    public ArrayList<String> que_coreII = new ArrayList<String>();
    /*
    * 记录将句子分解后的，每个语段的语法树
    * */
    public Map<String,String> sentenceI = new HashMap<String, String>();
    public Map<String,String> sentenceII = new HashMap<String,String>();

    @Autowired
    public LTPService ltpService;

    @Autowired
    public WordVectorModel wordVectorModel;

    private VecCal vecCal = new VecCal();

    public SdpSimilarService(){
        reList.add(new ArrayList<String>());
        reList.add(new ArrayList<String>());
    }

    public float getSdpSimilarity(String queryI,String queryII){
        float result=0;
        String answerI;
        if (sentenceI.containsKey(queryI))
         answerI = sentenceI.get(queryI);
        else
         answerI =  ltpService.getResult("sdp","plain",queryI);

        String answerII;
        if (sentenceII.containsKey(queryII))
            answerII=sentenceII.get(queryII);
        else
            answerII=ltpService.getResult("sdp","plain",queryII);
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
    /*
    * 输入 语义分析结果的数组的String，返回Root
    *
    * */
    private String getRoot(String answer){
        String[] seq=getSdpSeq(answer);
        String m="can't find root";
        for (int i=0;i<seq.length;i++){
            String[] temp = getSdpTerm(seq[i]);
            if (temp[2].equals("Root")){
                return temp[0];
            }
        }
        return m;
    }

    /*
    *
    * 第一次de计算方法，只找与 root关系相同的序列进行比对
    * */

    public float cal(String answer_queI,String answer_queII){
        reList.get(0).clear();
        reList.get(1).clear();
        int count=0;
       String rootI =  getRoot(answer_queI);
       String rootII = getRoot(answer_queII);
       if (rootI.equals("can't find root")||rootII.equals("can't find root")){
           return 0;
       }
       String[] s1 = getSdpSeq(answer_queI);
       String[] s2 = getSdpSeq(answer_queII);
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

    /*
    * 输入语义分析后数组的String，得到与Root相关联的String[]
    *
    * */
    public String[] getSArrray(String answer){
        String root = getRoot(answer);
        if(root.equals("can't find root")){
            System.out.println(answer+"can't find root");
            return null;
        }
        String[] s1 = getSdpSeq(answer);
        return  getTermFromSeq(s1,root);
    }


    /*
    * 输入为语义分析后数组的String
    * 计算 两个关键矩阵的相似度
    *
    * */
    public float calDspVec(String queryI,String queryII){

        String[] s1Vec = getSArrray(queryI);

        String[] s2Vec = getSArrray(queryII);
        if(s1Vec==null||s2Vec==null){
            return 0;
        }
        for (String s:s1Vec){
            if (!que_coreI.contains(s)){
            que_coreI.add(s);}
        }
        for (String s:s2Vec){
            if (!que_coreII.contains(s)){
            que_coreII.add(s);}
        }

        return calStringArray(s1Vec,s2Vec);
    }
    /*
    * 计算两个字符串数组的相似度（通常一个字符串数组里是一个短句的）
    * */
    public float calStringArray(String[] s1 ,String[] s2){
        float re = 0;
        int count=0;
        int size1 = s1.length;
        int size2 = s2.length;
        ArrayList<VecCell> list = new ArrayList<VecCell>();
        for (int i=0;i<s1.length;i++){
            for (int j=0;j<s2.length;j++){
                VecCell temp = new VecCell();
                float mm = wordVectorModel.similarity(s1[i],s2[j]);
                if (mm<0&&s1[i].equals(s2[j])){
                    mm = 1;
                }else if (mm<0){
                    mm =(float) 0.01;
                }
                temp.setKey(mm);
                temp.setX(i);
                temp.setY(j);
                list.add(temp);
            }
        }
        Collections.sort(list);

        while (!list.isEmpty()){

            re+= vecCal.getMaxClearXY(list);
            count++;

        }

        return re/count ;
    }

    /*
    * 计算长句的相似度，
    * */
    public float calLongSeqList(List<String[]> l1,List<String[]> l2){
        int size1 = l1.size();
        int size2 = l2.size();
        float re =0;
        int count=0;
        ArrayList<VecCell> list = new ArrayList<VecCell>();
        for (int i=0;i<size1;i++){
            for (int j=0;j<size2;j++){
                VecCell vecCell = new VecCell();
                float temp = calStringArray(l1.get(i),l2.get(j));
                if (Float.isNaN(temp)||Float.isInfinite(temp)){
                    vecCell.setKey(0);
                }else {
                    vecCell.setKey(temp);}
                vecCell.setX(i);
                vecCell.setY(j);
                list.add(vecCell);
            }
        }
        Collections.sort(list);

        while (!list.isEmpty()){

            re+= vecCal.getMaxClearXY(list);
            count++;

        }
        float alpha = size1<size2?(float) (size1+size2)/(2*size2):(float) (size1+size2)/(2*size1);
        return re/count * alpha ;

    }
/*
*
* 从数据取sdp_re 并解析 返回每个短句关键部分组成的list
* */
    public List<String[]> getSdp_List(String sdp_re) {
        List<String[]> re = new ArrayList<String[]>();
        if (sdp_re.contains("##")){
        String[] fir_floor=sdp_re.split("##");
        for (String s:fir_floor){
            if (s.contains("@"))
            re.add(s.split("@"));
        }
        }
        return re;
    }

    /*
        * 根据语义列表 A_1 B_2 Root 和Root，寻找与root有关的成分
        * */
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
        re.add(root.split("_")[0]);

        return  (String[]) re.toArray(new String[re.size()]);
    }
    /*
    * 输入正常的两句话返回相似度
    * */

    public float clauseRe(String s1,String s2){
        que_coreI.clear();
        que_coreII.clear();
       // sentenceII.clear();
        sentenceI.clear();
        float re=0;
        int count=0;
        String[] s1clause = s1.split("\\,|\\，|\\.|\\。|\\;|\\；|\\?|\\？");
        String[] s2clause = s2.split("\\,|\\，|\\.|\\。|\\;|\\；|\\?|\\？");
        ArrayList<VecCell> list = new ArrayList<VecCell>();
        int size1 = s1clause.length;
        int size2 = s2clause.length;
        for (int i=0;i<size1;i++){
            for (int j=0;j<size2;j++){
                VecCell vecCell = new VecCell();
                float temp = getSdpSimilarity(s1clause[i],s2clause[j]);
                if (Float.isNaN(temp)||Float.isInfinite(temp)){
                    vecCell.setKey(0);
                }else {
                vecCell.setKey(temp);}
                vecCell.setX(i);
                vecCell.setY(j);
                list.add(vecCell);
            }
        }
        Collections.sort(list);

        while (!list.isEmpty()){

            re+= vecCal.getMaxClearXY(list);
            count++;

        }
        float alpha = size1<size2?(float) (size1+size2)/(2*size2):(float) (size1+size2)/(2*size1);
        return re/count * alpha ;
    }



}
