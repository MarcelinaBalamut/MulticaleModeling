package sample;

public class Moore extends Neighbourhood{

    public Moore(Cell cell) {
        this.neighbours(cell);
    }

    @Override
    protected void neighbours(Cell cell) {

        Grid grid = Nucleons.getGrid();

        int cx = cell.getX();
        int cy = cell.getY();

        super.addCell( grid.getCell(cx - 1, cy - 1) );
        super.addCell( grid.getCell(cx, cy - 1) );
        super.addCell( grid.getCell(cx + 1, cy - 1) );
        super.addCell( grid.getCell(cx - 1, cy) );
        super.addCell( grid.getCell(cx + 1, cy) );
        super.addCell( grid.getCell(cx - 1, cy + 1) );
        super.addCell( grid.getCell(cx, cy + 1) );
        super.addCell( grid.getCell(cx + 1, cy + 1) );
    }
}
