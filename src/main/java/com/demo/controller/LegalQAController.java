package com.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.demo.service.DmService;
import com.demo.service.SeqVector;
import com.demo.service.SimilarResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

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
        URLDecoder.decode(input,"UTF-8");
        System.out.println(dm);
        return similarResultService.getAnswer(input,dm);
    }

    @RequestMapping(value = "/setdm.do")
    public void getDmController(){
        dmService.setDm();
    }

    @RequestMapping(value = "/getVector.do")
    public void getVectorController() throws Exception {seqVector.insertVector(); }
}
