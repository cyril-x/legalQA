package com.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.dao.QAEntity;
import com.demo.dao.SimilarResultDao;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 07/03/2018
 * Description:
 */
@Service
public class DmService {
    @Autowired
    private  SimilarResultDao similarResultDao;

    private static String url = "http://172.16.124.16:8398/anyouClassify";

    public  int getDm(int num,String content){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        parameters.add(new BasicNameValuePair("result_type","flat"));
        parameters.add(new BasicNameValuePair("hits","3"));
        parameters.add(new BasicNameValuePair("src",content));

        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity formEntity;
            formEntity = new UrlEncodedFormEntity(parameters,"utf-8");
            httpPost.setEntity(formEntity);

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                String result = EntityUtils.toString(response.getEntity(),"utf-8");

                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.get("minshi")==null){
                    System.out.println(jsonObject);
                    System.out.println(num);
                }
                String minshijson = jsonObject.get("minshi").toString();
                String xingshijson = jsonObject.get("xingshi").toString();

                JSONObject  minshi = JSONObject.parseObject(minshijson);
                JSONObject xingshi = JSONObject.parseObject(xingshijson);

                if(minshi.get("labels")!=null&&xingshi.get("labels")!=null){
                    JSONArray minshiarray = JSONObject.parseArray(minshi.get("labels").toString());

                    JSONArray xingshiarray = JSONObject.parseArray(xingshi.get("labels").toString());
                    if (Float.parseFloat(minshiarray.getJSONObject(0).get("prob").toString())>Float.parseFloat(xingshiarray.getJSONObject(0).get("prob").toString()))
                        return Integer.parseInt(minshiarray.getJSONObject(0).get("DM").toString());
                    else return Integer.parseInt(xingshiarray.getJSONObject(0).get("DM").toString());
                }
                else if(minshi.get("labels")!=null&&xingshi.get("labels")==null){
                    JSONArray minshiarray = JSONObject.parseArray(minshi.get("labels").toString());
                    int minshidm = Integer.parseInt(minshiarray.getJSONObject(0).get("DM").toString());
                    System.out.println(minshiarray.getJSONObject(0).get("prob"));
                    return minshidm;
                }else if(minshi.get("labels")==null&&xingshi.get("labels")!=null){
                    JSONArray xingshiarray = JSONObject.parseArray(xingshi.get("labels").toString());
                    int xingshidm = Integer.parseInt(xingshiarray.getJSONObject(0).get("DM").toString());
                    System.out.println(xingshiarray.getJSONObject(0).get("prob"));
                    return xingshidm;
                }else {
                    return 0;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public void setDm() {
        int[] reInt = similarResultDao.getNum();
        int size = reInt.length;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));
        for (int i=0;i<size;i++){
            QAEntity qaEntity=similarResultDao.getQAEntity(reInt[i]);
            String content = qaEntity.getQuestionContent().trim();
            if (content.equals("")){
                content = qaEntity.getQuestionTitle();
                if(content.equals("")){
                    System.out.println(qaEntity.getNum());
                    System.out.println("youyigekong");
                }
            }

            similarResultDao.setDm(qaEntity.getNum(),getDm(qaEntity.getNum(),content));

        }
        System.out.println(simpleDateFormat.format(new Date()));
    }

    public int getDm(String query){
       return getDm(0,query);
    }
}
