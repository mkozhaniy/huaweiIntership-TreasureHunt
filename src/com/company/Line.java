package com.company;

public class Line {
    public final double x1;
    public final double y1;
    public final double x2;
    public final double y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public static double product(Line p1, Line p2) {
        return (p1.x2 - p1.x1) *
                (p2.y2- p2.y1) -
                (p1.y2 - p1.y1) *
                        (p2.x2 - p2.x1);
    }

    public static boolean intersect(Line p1, Line p2) {

        double prod1 = product(p2, new Line(p1.x1, p1.y1, p2.x1, p2.y1));
        double prod2 = product(p2, new Line(p1.x2, p1.y2, p2.x1, p2.y1));

        if (prod1 * prod2 > 0)
            return false;

        prod1 = product(p1, new Line(p2.x1, p2.y1, p1.x1, p1.y1));
        prod2 = product(p1, new Line(p2.x2, p2.y2, p1.x1, p1.y1));

        return !(prod1 * prod2 > 0);
    }

}
