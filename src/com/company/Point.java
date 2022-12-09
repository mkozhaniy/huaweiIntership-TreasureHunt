package com.company;

public class Point {
    public final double x;
    public final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static boolean equals(Point p1, Point p2){
        return (p1.x == p2.x && p1.y == p2.y);
    }
}
