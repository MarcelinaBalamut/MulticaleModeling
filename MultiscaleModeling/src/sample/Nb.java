package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Nb {

    public static List<Point> getNeighborhood(int x, int y, int sizeHeight, int sizeWidth , int number, int neighborhoodType) {
        List<Point> points  = new ArrayList();
        Random rand = new Random();
        int m;
        if(neighborhoodType==1){
            points = Moore.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
        }

            if(neighborhoodType==2){

            points = vonNeuman.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
        }

        if(neighborhoodType==3){

            points = HexagonalLeft.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
        }

        if(neighborhoodType==4){

            points = HexagonalRight.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
        }


        if(neighborhoodType==5){

             m =rand.nextInt(2);
            if(m==1)

               points= HexagonalRight.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
            else
               points =HexagonalLeft.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
        }

        if(neighborhoodType==6){

            m =rand.nextInt(5);
                       if(m==0)
                           points =PentagonalDown.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
                       else if(m==1)
                           points =PentagonalUp.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
                        if(m==2)
                            points =PentagonalLeft.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);
                        if(m==3)
                            points =PentagonaLRight.getNeighborhood( x,  y,  sizeHeight,  sizeWidth ,  number);

        }


        return points;
    }

}
