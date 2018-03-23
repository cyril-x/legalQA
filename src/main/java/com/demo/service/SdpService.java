package com.demo.service;

import com.demo.dao.SimilarResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class SdpService {
    @Autowired
    LTPService ltpService;

    @Autowired
    SdpSimilarService sdpSimilarService;

    @Autowired
    SimilarResultDao similarResultDao;

    ArrayList<Integer> numlist = similarResultDao.getNumList();
    int size = numlist.size();
    Iterator it = numlist.iterator();
    /*
    * 将分离出来的关键词列表插入数据库中
    * */
    public String saveSdpRe(List<String[]> answer){
        StringBuilder stringBuilder = new StringBuilder("");
        int size = answer.size();
        for (int i=0;i<size;i++){
            String[] tar = answer.get(i);
            for (String s:tar){
                stringBuilder.append(s+" ");
            }
            stringBuilder.append("##");
        }
        return stringBuilder.toString();
    }

    public List<String[]> handleQuery(String s){
        List<String[]> re = new ArrayList<String[]>();
        String ss1 = "";
        Pattern p = Pattern.compile("\\s*|\t|\n|\r");
        Matcher m1 = p.matcher(s);
        ss1 = m1.replaceAll("");
        String[] s1clause = ss1.split("\\,|\\，|\\.|\\。|\\;|\\；|\\?|\\？");
        for (int i=0;i<s1clause.length;i++){
            if (!s1clause.equals("")) {
                String re1 = ltpService.getResult("sdp", "plain", s1clause[i]);
                String[] reArray = sdpSimilarService.getSArrray(re1);
                re.add(reArray);
            }
        }
        return re;
    }

    public void insertSdpRe(){

        while (it.hasNext()){
            synchronized (it) {
                Integer tepInt = (Integer) it.next();
                String s = similarResultDao.getContent(tepInt) + similarResultDao.getTitle(tepInt);
                List<String[]> tep = handleQuery(s);
                String re = saveSdpRe(tep);
                similarResultDao.insertSdpRe(re, tepInt);
            }
        }


    }
}
