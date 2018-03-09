package com.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.demo.service.DmInsertService;
import com.demo.service.SeqVector;
import com.demo.service.SimilarResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 05/03/2018
 * Description:
 */
@Controller
public class SimilarResultController {
    @Autowired
    private SimilarResultService similarResultService;
    @Autowired
    private DmInsertService dmInsertService;
    @Autowired
    private SeqVector seqVector;

    @ResponseBody
    @RequestMapping(value = "/queandan.do",method = RequestMethod.GET,produces = {"application/json;charset=utf-8"})
    public JSONArray QAController(HttpServletRequest httpServletRequest) throws Exception {
        String queue = httpServletRequest.getParameter("input");
        return similarResultService.getAnswer(queue);
    }

    @RequestMapping(value = "/getdm.do")
    public void getDmController(){
        dmInsertService.dm();
    }

    @RequestMapping(value = "/getVector.do")
    public void getVectorController() throws Exception {seqVector.insertVector(); }
}
