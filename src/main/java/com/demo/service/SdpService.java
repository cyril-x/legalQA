package com.demo.service;

import com.demo.dao.SimilarResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    ArrayList<Integer> numlist;
    int size;
    Iterator it;
    private ExecutorService service;
    private CountDownLatch latch;


    /*
    * 将分离出来的关键词列表插入数据库中
    * */
    public String saveSdpRe(List<String[]> answer){
        StringBuilder stringBuilder = new StringBuilder("");
        int size = answer.size();
        for (int i=0;i<size;i++){
            String[] tar = answer.get(i);
            for (String s:tar){
                if (!s.equals(tar[tar.length-1])){
                stringBuilder.append(s+"@");}else {
                    stringBuilder.append(s);
                }
            }
            stringBuilder.append("##");
        }
        return stringBuilder.toString();
    }

    public List<String[]> handleQuery(String s){
        List<String[]> re = new ArrayList<String[]>();
        String ss1 ;
        Pattern p = Pattern.compile("\\s*|\t|\n|\r");
        Matcher m1 = p.matcher(s);
        ss1 = m1.replaceAll("");
        String[] s1clause = ss1.split("\\,+|\\，+|\\.+|\\。+|\\;+|\\；+|\\?+|\\？+");
        for (int i=0;i<s1clause.length;i++){
            if (!s1clause[i].equals("")&&s1clause[i]!=null) {
                String re1 = ltpService.getResult("sdp", "plain", s1clause[i]);
                if (re1.equals("")||re1==null){
                    continue;
                }
                String[] reArray = sdpSimilarService.getSArrray(re1);
                re.add(reArray);
            }else continue;
        }
        return re;
    }


    public void insertSdpRe(){
            numlist = similarResultDao.getNumList();
            size = numlist.size();
            it = numlist.iterator();
        service = Executors.newFixedThreadPool(4);
        latch = new CountDownLatch(4);
            TeThread thread1 = new TeThread(latch,"1");

            TeThread thread2 = new TeThread(latch,"2");

            TeThread thread3 = new TeThread(latch,"3");

            TeThread thread4 = new TeThread(latch,"4");
            service.execute(thread1);
            service.execute(thread2);
            service.execute(thread3);
            service.execute(thread4);
            service.shutdown();



    }

    class TeThread implements Runnable{

        private CountDownLatch downLatch;
        private String name;

        public TeThread(CountDownLatch downLatch, String name) {
            this.downLatch = downLatch;
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(this.name+"begin");
                while (it.hasNext()) {
                    Integer tepInt;
                    synchronized (it){
                        tepInt = (Integer) it.next();
                        System.out.println(tepInt+"begin");
                        it.notifyAll();
                    }
                    String s = similarResultDao.getContent(tepInt) + "."+similarResultDao.getTitle(tepInt);
                    List<String[]> tep = handleQuery(s);
                    String re = saveSdpRe(tep);
                    similarResultDao.insertSdpRe(re, tepInt);
                    System.out.println(tepInt+"inserted");
                    try {
                        Thread.currentThread().sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                downLatch.countDown();
            System.out.println(this.name+"finish");
        }
    }

    public void checkout(){
        int tepInt =  1473;
        String s = similarResultDao.getContent(tepInt) + similarResultDao.getTitle(tepInt);
        String s2=s.replace("\\n","");
        List<String[]> tep = handleQuery(s2);
        String re = saveSdpRe(tep);
        similarResultDao.insertSdpRe(re, tepInt);
        System.out.println(tepInt+"inserted");
    }
}
