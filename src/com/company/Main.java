package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

/*
 * @author Максим Кожуховский
 * mail: m.kozhukhovskii@g.nsu.ru
 * */

/*Идея: Так как все многоугольники выпуклые, то предполагается,
* что можно выпустить линию из границы плоскости в сокровище и эта линия
* пересекает все сломанные стены. Делее делим плоскость на треугольные
* сегменты такие, что сокровище и две ближайшие точки на границе
* плоскости(точки из которых исходят линии), далее проверяем
* количество линий пересекших этот сегмент хотя бы в одной линии,
* пересечения в одной линии достаточно, так как две точки на границе
* ближайшее, следовательно любая линия из границы в сокровище,
* в этом сегменте, пересечет линию ,пересекшую одну из треугольника .*/
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(Path.of("tests.txt"), StandardCharsets.UTF_8);
        PrintWriter out = new PrintWriter("out.txt", StandardCharsets.UTF_8);

        int n;
        Point p1;
        Point p2;
        Point treasure;
        ArrayList<Point> pointsX0 = new ArrayList<>();
        ArrayList<Point> pointsY0 = new ArrayList<>();
        ArrayList<Point> pointsX100 = new ArrayList<>();
        ArrayList<Point> pointsY100 = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        while (in.hasNextInt()) {
            n = in.nextInt();

            if (n == 0) {
                in.next();
                in.next();
                out.write("Number of doors = 1\n");
                continue;
            }

            Line[] lines = new Line[n];
            for (int i = 0; i < n; ++i) {
                p1 = getPoint(in, pointsX0, pointsY0, pointsX100, pointsY100);
                p2 = getPoint(in, pointsX0, pointsY0, pointsX100, pointsY100);
                lines[i] = new Line(p1, p2);
            }
            treasure = new Point(Double.parseDouble(in.next().replaceAll(",", ".")),
                    Double.parseDouble(in.next().replaceAll(",", ".")));
            points.add(new Point(0, 0));
            if (!pointsX0.isEmpty()) {
                Collections.sort(pointsX0, Comparator.comparing(Point::getY));
                points.addAll(pointsX0);
            }
            points.add(new Point(0, 100));
            if (!pointsY100.isEmpty()) {
                Collections.sort(pointsY100, Comparator.comparing(Point::getX));
                points.addAll(pointsY100);
            }
            points.add(new Point(100, 100));
            if (!pointsX100.isEmpty()) {
                Collections.sort(pointsX100, Comparator.comparing(Point::getY).reversed());
                points.addAll(pointsX100);
            }
            points.add(new Point(100, 0));
            if (!pointsY0.isEmpty()) {
                Collections.sort(pointsY0, Comparator.comparing(Point::getX).reversed());
                points.addAll(pointsY0);
            }

            int mins = 1000;
            int crossSt;
            int crossNd;
            Point stP = pointsX0.get(0);
            crossSt = numCrosses(new Line(stP, treasure), lines);
            for (var ndP : points) {
                if (Objects.equals(stP, ndP)) continue;
                else if (Point.equals(stP, ndP)) continue;
                crossNd = numCrosses(new Line(ndP, treasure), lines);
                if (crossNd > crossSt) {
                    if (crossNd < mins) mins = crossNd;
                } else {
                    if (crossSt < mins) mins = crossSt;
                }
                stP = ndP;
                crossSt = crossNd;
            }
            pointsX0.clear();
            pointsX100.clear();
            pointsY0.clear();
            pointsY100.clear();
            points.clear();
            out.write("Number of doors = " + (mins + 1));
            out.write("\n");
        }
        out.flush();
    }

    private static Point getPoint(Scanner in, ArrayList<Point> pointsX0,
                                  ArrayList<Point> pointsY0, ArrayList<Point> pointsX100,
                                  ArrayList<Point> pointsY100) {
        Point p1;
        p1 = new Point(in.nextInt(), in.nextInt());
        if (p1.x == 0 || p1.x == 100) {
            if (p1.x == 0) pointsX0.add(p1);
            else pointsX100.add(p1);
        } else {
            if (p1.y == 0) pointsY0.add(p1);
            else pointsY100.add(p1);
        }
        return p1;
    }

    public static double product(Line p1, Line p2) {
        return (p1.getEnd().x - p1.getStart().x) *
                (p2.getEnd().y - p2.getStart().y) -
                (p1.getEnd().y - p1.getStart().y) *
                        (p2.getEnd().x - p2.getStart().x);
    }

    public static boolean intersect(Line p1, Line p2) {
        double eps = 0.000001;

        double prod1 = product(p2, new Line(p1.getStart(), p2.getStart()));
        double prod2 = product(p2, new Line(p1.getEnd(), p2.getStart()));

        if (prod1 * prod2 > 0 || Math.abs(prod1) < eps || Math.abs(prod2) < eps) return false;

        prod1 = product(p1, new Line(p2.getStart(), p1.getStart()));
        prod2 = product(p1, new Line(p2.getEnd(), p1.getStart()));

        return !(prod1 * prod2 > 0 || Math.abs(prod1) < eps || Math.abs(prod2) < eps);
    }

    public static int numCrosses(Line line, Line[] lines) {
        int counter = 0;
        for (var l : lines) {
            if (intersect(line, l)) counter += 1;
        }
        return counter;
    }
}

