package com.demo.dao;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 06/03/2018
 * Description:
 */
public class QAEntity {
    private int num;
    private String askTime;
    private String questionTitle;
    private String questionContent;
    private String responseTime;
    private String bestResponse;
    private String vector;
    private int dm;
    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }


    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getBestResponse() {
        return bestResponse;
    }

    public void setBestResponse(String bestResponse) {
        this.bestResponse = bestResponse;
    }
}
