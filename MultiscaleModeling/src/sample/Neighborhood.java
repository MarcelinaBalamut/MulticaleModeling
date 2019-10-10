package sample;

import java.util.ArrayList;
import java.util.List;

public interface Neighborhood {
    public    List<Point> getNeighborhood(int x, int y,  int sizeWidth, int sizeHeight, int number);
}
