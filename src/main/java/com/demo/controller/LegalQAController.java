package com.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.demo.service.DmService;
import com.demo.service.SeqVector;
import com.demo.service.SimilarResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.management.monitor.MonitorSettingException;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 05/03/2018
 * Description:
 */
@Controller
public class LegalQAController {
    @Autowired
    private SimilarResultService similarResultService;
    @Autowired
    private DmService dmService;
    @Autowired
    private SeqVector seqVector;

    @ResponseBody
    @RequestMapping(value = "/queandan.do",method = RequestMethod.GET,produces = {"application/json;charset=utf-8"})
    public JSONArray QAController(String input,boolean dm) throws Exception {
       // String queue = httpServletRequest.getParameter("input");

        //URLDecoder.decode(input,"UTF-8");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));
        System.out.println(dm);
        JSONArray re =  similarResultService.getAnswer(input,dm);
        System.out.println(simpleDateFormat.format(new Date()));
        return re;
    }

    @RequestMapping(value = "/setdm.do")
    public void getDmController(){
        dmService.setDm();
    }

    @RequestMapping(value = "/getVector.do")
    public void getVectorController() throws Exception {seqVector.insertVector(); }

    @ResponseBody
    @RequestMapping(value = "/simCompare.do",method = RequestMethod.GET,produces = {"application/json;charset=utf-8"})
    public JSONObject Compare(String s1, String s2){
        System.out.println(s1);
        s1=s1.replace("\n|\t| ","");
        s2=s2.replace("\n|\t| ","");
        return similarResultService.getCompareRe(s1,s2);
    }

    @RequestMapping(value = "/compare")
    public ModelAndView toCompare(){
        ModelAndView mv = new ModelAndView("compare");
        return mv;
    }
}
