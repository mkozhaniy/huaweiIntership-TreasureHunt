package com.company;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static boolean equals(Point p1, Point p2){
        return (p1.x == p2.x && p1.y == p2.y);
    }
}
