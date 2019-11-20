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

        super.addCellList( grid.getCell(x, y - 1) );
        super.addCellList( grid.getCell(x - 1, y) );
        super.addCellList( grid.getCell(x + 1, y) );
        super.addCellList( grid.getCell(x, y + 1) );
    }
}
