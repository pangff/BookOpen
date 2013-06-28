package com.witmob.bookopen;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 13-4-11
 * Time: 下午6:05
 * To change this template use File | Settings | File Templates.
 */
public class Vertex {
    public float positionX, positionY, positionZ,w;

    public Vertex(float positionX, float positionY, float positionZ,float w) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.w=w;
    }

    public float[] getPosition() {
        return new float[]{positionX, positionY, positionZ,w};
    }

    public void translate(float dx, float dy) {
        positionX += dx;
        positionY += dy;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", positionZ=" + positionZ +
                '}';
    }
}
