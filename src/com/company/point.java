package com.company;

/**
 * Created by charlesanderson on 9/21/16.
 */
public class point {
    public int x, y;
    public char c;
    public boolean visited;
    public double distance;

    public point(int x, int y, char c) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.visited = false;
        this.distance = 9999999;
    }
}