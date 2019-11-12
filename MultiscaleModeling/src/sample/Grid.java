package sample;

import java.util.ArrayList;
import java.util.List;

public class Grid {

    private List<Cell> grid;
    private int width;
    private int height;
    private boolean isPeriodic;

    public Grid(int width, int height, boolean isPeriodic) {
        this.width = width;
        this.height = height;
        this.isPeriodic = isPeriodic;

        this.grid = new ArrayList<>();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.grid.add(new Cell(x, y));
            }
        }
    }

    public Cell getCell(int x, int y) {
        if (!this.isPeriodic) {
            if (x >= width || y >= this.height)
                return null;
            if (x < 0 || y < 0)
                return null;
        }
        else {
            if (x >= this.width)
                x -= this.width;
            if (y >= this.height)
                y -= this.height;
            if (x < 0)
                x += this.width;
            if (y < 0)
                y += this.height;
        }
        return this.grid.get(y * this.width + x);
    }






    public void setCell(int x, int y, Cell cell){
        grid.set(y * width + x, cell);
    }

    public Cell getCellByPosition(int i) {
        return this.grid.get(i);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public List<Cell> getGrid() {
        return this.grid;
    }

    public void setGrid(List<Cell> grid) {
        this.grid = grid;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }

    public void setPeriodic(boolean periodic) {
        isPeriodic = periodic;
    }
}
