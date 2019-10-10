package sample;

import java.util.List;

public class BoundaryCondition {


    public static void checkBoundary(List <Point> points, int number,  int sizeHeight, int sizeWidth){



        for (Point p: points ) {

            if( number ==1){

                if (p.getX() == -1)
                    p.setX(sizeWidth - 1);
                    // p.setX(0);
                else if (p.getX() == sizeWidth)
                    p.setX(0);


                if (p.getY()== -1)
                    //  p.setY(0);
                    p.setY(sizeHeight - 1);
                else if (p.getY() == sizeHeight)
                    p.setY(0);

            }
            else if(number == 2){

                if (p.getX() == -1)
                    p.setX(0);
                else if (p.getX() == sizeWidth)
                    p.setX(sizeWidth-1);


                if (p.getY()== -1)
                    p.setY(0);
                else if (p.getY() == sizeHeight)
                    p.setY(sizeHeight-1);
            }

        }



    }

}
