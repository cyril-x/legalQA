package com.demo.object;

/**
 * @author d-xsj
 * mail coder_xushijia@126.com
 * date 21/03/2018
 * Description:
 */
public class VecCell implements Comparable<VecCell> {
    private float key;
    private int x;
    private int y;

    public float getKey() {
        return key;
    }

    public void setKey(float key) {
        this.key = key;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int compareTo(VecCell o) {
        return o.key>key?1 : -1;
    }
}
