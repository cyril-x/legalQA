package com.demo.object;

public class NumScore implements Comparable<NumScore> {
    private int num;
    private float score;
    public NumScore(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }



    @Override
    public int compareTo(NumScore o) {
        return o.score>=score?1:-1;
    }
}
