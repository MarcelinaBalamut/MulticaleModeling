package sample;

import java.util.ArrayList;
import java.util.List;

public class HexagonalRight  {

    public  static List<Point> getNeighborhood(int x, int y, int sizeHeight, int sizeWidth , int number) {

            List<Point> points  = new ArrayList();

            points.add(new Point(x, y+1));
            points.add(new Point(x+1, y));
            points.add(new Point(x, y-1));
            points.add(new Point(x-1, y));
            points.add(new Point(x+1, y-1));
            points.add(new Point(x-1, y+1));


        BoundaryCondition.checkBoundary(points,number,  sizeHeight,  sizeWidth);


            return points;


    }
}
