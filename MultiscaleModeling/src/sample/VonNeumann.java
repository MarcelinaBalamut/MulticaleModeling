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

        super.addCellToList( grid.getCell(x, y - 1) );
        super.addCellToList( grid.getCell(x - 1, y) );
        super.addCellToList( grid.getCell(x + 1, y) );
        super.addCellToList( grid.getCell(x, y + 1) );
    }
}
