package com.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.object.SDPEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
@Service
public class LTPService {
    String url="https://api.ltp-cloud.com/analysis/";
    String api_key="G1t7R2i3z56G9Z5G7oKFfoHTvXEClXWPFYWRAFWd";

    public JSONArray getResultJson(String pattern,String query){
        JSONArray jsonArray = null ;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        parameters.add(new BasicNameValuePair("api_key",api_key));
        parameters.add(new BasicNameValuePair("text",query));
        parameters.add(new BasicNameValuePair("pattern",pattern));
        parameters.add(new BasicNameValuePair("format","json"));

        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity formEntity;
            formEntity = new UrlEncodedFormEntity(parameters,"utf-8");
            httpPost.setEntity(formEntity);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                String result = EntityUtils.toString(response.getEntity(),"utf-8");
                jsonArray = JSONArray.parseArray(result);
                return jsonArray;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    return jsonArray;
    }
    public String getResult(String pattern,String format,String query){
        String reString = null ;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();

        parameters.add(new BasicNameValuePair("api_key",api_key));
        parameters.add(new BasicNameValuePair("text",query));
        parameters.add(new BasicNameValuePair("pattern",pattern));
        parameters.add(new BasicNameValuePair("format",format));

        CloseableHttpResponse response = null;
        try {
            UrlEncodedFormEntity formEntity;
            formEntity = new UrlEncodedFormEntity(parameters,"utf-8");
            httpPost.setEntity(formEntity);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode()==200){
                reString = EntityUtils.toString(response.getEntity(),"utf-8");

                return reString;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        return reString;
    }

    public List<List<SDPEntity>> getSdpListResult(String query){
        List<List<SDPEntity>> sdplist = new ArrayList<List<SDPEntity>>();
        JSONArray jsonArray = getResultJson("sdp",query);

        JSONArray jsonArray1 = jsonArray.getJSONArray(0);
        int size1 = jsonArray1.size();
        for (int i=0;i<size1;i++){
            JSONArray temp=jsonArray1.getJSONArray(i);
            sdplist.add(new ArrayList<SDPEntity>());
            int size2 = temp.size();
            for (int j=0;j<size2;j++){
                JSONObject jsonObject = JSONObject.parseObject(temp.get(j).toString());
                SDPEntity sdpEntity = new SDPEntity();
                sdpEntity.semrelate = jsonObject.getString("semrelate");
                sdpEntity.semparent = Integer.parseInt(jsonObject.getString("semparent"));
                sdpEntity.pos = jsonObject.getString("pos");
                sdpEntity.id = Integer.parseInt(jsonObject.getString("id"));
                sdpEntity.cont = jsonObject.getString("cont");
                sdplist.get(i).add(sdpEntity);
            }

        }
        return  sdplist;

    }


}
