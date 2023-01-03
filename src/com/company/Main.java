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
        double[] treasure;

        ArrayList<Point> pointsX0 = new ArrayList<>();
        ArrayList<Point> pointsY0 = new ArrayList<>();
        ArrayList<Point> pointsX100 = new ArrayList<>();
        ArrayList<Point> pointsY100 = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        Comparator<Point> comparatorPointX = Comparator.comparingInt(Point::getX);
        Comparator<Point> comparatorPointY = Comparator.comparingInt(Point::getY);
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
                lines[i] = new Line(p1.x, p1.y, p2.x, p2.y);
            }
            treasure = new double[]{Double.parseDouble(in.next()
                    .replaceAll(",", ".")), Double.parseDouble(in.next()
                    .replaceAll(",", "."))};
            points.add(new Point(0, 0));
            if (!pointsX0.isEmpty()) {
                pointsX0.sort(comparatorPointX);
                points.addAll(pointsX0);
            }
            points.add(new Point(0, 100));
            if (!pointsY100.isEmpty()) {
                pointsY100.sort(comparatorPointY);
                points.addAll(pointsY100);
            }
            points.add(new Point(100, 100));
            if (!pointsX100.isEmpty()) {
                comparatorPointX.reversed();
                pointsX100.sort(comparatorPointX);
                points.addAll(pointsX100);
            }
            points.add(new Point(100, 0));
            if (!pointsY0.isEmpty()) {
                comparatorPointY.reversed();
                pointsY0.sort(comparatorPointY);
                points.addAll(pointsY0);
            }

            int mins = 1000;
            int crossSt;
            int crossNd;
            Point stP = pointsX0.get(0);
            crossSt = numCrosses(new Line(stP.x, stP.y, treasure[0], treasure[1]), lines);
            for (var ndP : points) {
                if (Objects.equals(stP, ndP)) continue;
                else if (Point.equals(stP, ndP)) continue;
                crossNd = numCrosses(new Line(ndP.x, ndP.y, treasure[0], treasure[1]), lines);
                if (crossNd > crossSt) {
                    if (crossNd < mins) mins = crossNd;
                } else {
                    if (crossSt < mins) mins = crossSt;
                }
                stP = ndP;
                crossSt = crossNd;
            }
            out.write("Number of doors = " + (mins));
            out.write("\n");
            pointsX0.clear();
            pointsX100.clear();
            pointsY0.clear();
            pointsY100.clear();
            points.clear();
            comparatorPointX.reversed();
            comparatorPointY.reversed();
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


    public static int numCrosses(Line line, Line[] lines) {
        int counter = 0;
        for (var l : lines) {
            if (Line.intersect(line, l)) counter += 1;
        }
        return counter;
    }
}

