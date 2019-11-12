package sample;

public class VonNeumann extends  Neighbourhood{

    public VonNeumann(Cell cell) {
        this.neighbours(cell);
    }

    @Override
    protected void neighbours(Cell cell) {
        Grid grid = Nucleons.getGrid();

        int x = cell.getX();
        int y = cell.getY();

        super.addCell( grid.getCell(x, y - 1) );
        super.addCell( grid.getCell(x - 1, y) );
        super.addCell( grid.getCell(x + 1, y) );
        super.addCell( grid.getCell(x, y + 1) );
    }
}
