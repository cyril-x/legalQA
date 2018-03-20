package com.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.object.SDPEntity;
import com.demo.service.DspSimilarService;
import com.demo.service.LTPService;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        LTPService ltpService = new LTPService();
       /* String mm = ltpService.getResult("sdp","plain","今天我来到这里，可以说是非常开心了");
        String[] mp = mm.split("\n");*/
        List<List<SDPEntity>> sdplist = ltpService.getSdpListResult("今天我来到这里，可以说是非常开心了");
        System.out.println(sdplist);

    }
}
