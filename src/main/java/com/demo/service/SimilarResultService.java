package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import com.demo.dao.SimilarResultDao;
import com.demo.object.NumScore;
import com.hankcs.hanlp.mining.word2vec.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


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
    SdpService sdpService;

    @Autowired
    SdpSimilarService sdpSimilarService;

    ArrayList<Integer> queNum;
    java.util.Vector<NumScore> relist = new java.util.Vector<>();
    int size;
    Iterator queNumIt;
    List<String[]> queHanledquery;
    String query;
    Vector target;
    ExecutorService service;
    CountDownLatch latch;
    boolean flag;//判断多线程是否跑完，防止提前进行排序
        public JSONArray getAnswer(String query,Boolean dmif,int method) throws InterruptedException {
            relist.clear();
            this.query = query;
            int qDm = dmService.getDm(query);
            if (dmif) {
               queNum  = similarResultDao.getNumFromDm(qDm);

            }else {
                 queNum = similarResultDao.getNumList();

            }

            JSONArray jsonArray = new JSONArray();
            int resize = queNum.size();

            if (method==1){
                getWord2VecNumScore();

            }else if(method ==2){
                getSdpNumScore(query);
            }else {
                getWord2VecNumScore();
            }


            Collections.sort(relist);

            int maxsize;
            if (resize>=3){
                maxsize =3;
            }else {
                maxsize = resize;
            }
            if (resize>0){
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
                jsonObject.put("num",relist.get(i).getNum());
                jsonObject.put("dm",similarResultDao.getDm(relist.get(i).getNum()));
                jsonObject.put("questionTitle",similarResultDao.getTitle(relist.get(i).getNum()));
                jsonObject.put("questionContent",similarResultDao.getContent(relist.get(i).getNum()));
                jsonObject.put("response",similarResultDao.getAnswer(relist.get(i).getNum()));
                jsonObject.put("result",relist.get(i).getScore());
                jsonArray.add(jsonObject);
            }

            return jsonArray;
        }




    public void getWord2VecNumScore() throws InterruptedException {
        target= docvector.query(query);
        queNumIt = queNum.iterator();
        service = Executors.newFixedThreadPool(6);
        latch = new CountDownLatch(6);
        Word2VecThread thread1 = new Word2VecThread(latch,"1");
        Word2VecThread thread2 = new Word2VecThread(latch,"2");
        Word2VecThread thread3 = new Word2VecThread(latch,"3");
        Word2VecThread thread4 = new Word2VecThread(latch,"4");
        Word2VecThread thread5 = new Word2VecThread(latch,"5");
        Word2VecThread thread6 = new Word2VecThread(latch,"6");
        service.execute(thread1);
        service.execute(thread2);
        service.execute(thread3);
        service.execute(thread4);
        service.execute(thread5);
        service.execute(thread6);
        service.shutdown();
        service.awaitTermination(60,TimeUnit.SECONDS);


    }

    public void getSdpNumScore( String query) throws InterruptedException {
        queHanledquery = sdpService.handleQuery(query);
        queNumIt = queNum.iterator();
        service = Executors.newFixedThreadPool(6);
        latch = new CountDownLatch(6);
        SdpSimilarThread thread1 = new SdpSimilarThread(latch,"1");
        SdpSimilarThread thread2 = new SdpSimilarThread(latch,"2");
        SdpSimilarThread thread3 = new SdpSimilarThread(latch,"3");
        SdpSimilarThread thread4 = new SdpSimilarThread(latch,"4");
        SdpSimilarThread thread5 = new SdpSimilarThread(latch,"5");
        SdpSimilarThread thread6 = new SdpSimilarThread(latch,"6");
        service.execute(thread1);
        service.execute(thread2);
        service.execute(thread3);
        service.execute(thread4);
        service.execute(thread5);
        service.execute(thread6);
        service.shutdown();
        service.awaitTermination(60,TimeUnit.SECONDS);



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
        jsonObject.put("sdp",sdpSimilarService.clauseRe(s1,s2));
        jsonObject.put("vec",getWord2VecSimilarity(s1,s2));
        ArrayList<String> queryI = sdpSimilarService.que_coreI;
        ArrayList<String> queryII = sdpSimilarService.que_coreII;
        jsonObject.put("queryIAll",sdpSimilarService.getSdpAnalys(s1).split("\n"));
        jsonObject.put("queryIIAll",sdpSimilarService.getSdpAnalys(s2).split("\n"));
        jsonObject.put("queryI",queryI);
        jsonObject.put("queryII",queryII);


        return  jsonObject;
    }
    class SdpSimilarThread implements Runnable{

        private CountDownLatch downLatch;
        private String name;

        public SdpSimilarThread(CountDownLatch downLatch, String name) {
            this.downLatch = downLatch;
            this.name = name;
        }

        @Override
        public void run() {
        while (queNumIt.hasNext()){
            Integer tepInt;
            synchronized (queNumIt){
                tepInt =(Integer) queNumIt.next();
                queNumIt.notifyAll();
            }
            String contemp= similarResultDao.getSdp_reFromNum(tepInt);
            List<String[]> data = sdpSimilarService.getSdp_List(contemp);
                synchronized (relist){
                    if (contemp!=null&!contemp.equals("")){
                        NumScore numScore = new NumScore(tepInt);
                        numScore.setScore(sdpSimilarService.calLongSeqList(data,queHanledquery));
                relist.add(numScore);
                }
            }
        }
        downLatch.countDown();
        }
    }

    class Word2VecThread implements Runnable{
        private CountDownLatch downLatch;
        private String name;

        public Word2VecThread(CountDownLatch downLatch, String name) {
            this.downLatch = downLatch;
            this.name = name;
        }

        @Override
        public void run() {
            while (queNumIt.hasNext()){
                Integer tepInt;
                synchronized (queNumIt){
                    tepInt =(Integer) queNumIt.next();
                    queNumIt.notifyAll();
                }

                    String vector = similarResultDao.getVecFromNum(tepInt);
                    if (vector.equals("")){
                        System.out.println(tepInt);
                        continue;}
                    float temp=docvector.vecstrToVec(vector).cosineForUnitVector(target);
                    NumScore ns = new NumScore(tepInt);
                    ns.setScore(temp);
                    synchronized (relist) {
                        relist.add(ns);
                    }

            }
            downLatch.countDown();

        }
    }

}
