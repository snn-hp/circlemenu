package com.modle;

import java.io.Serializable;

/**
 * Created by gfh on 2017/7/7.
 */

public class Coordinate implements Serializable {
    private float x;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    private float y;
}
