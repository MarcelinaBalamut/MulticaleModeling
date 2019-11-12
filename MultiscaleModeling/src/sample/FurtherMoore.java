package sample;

public class FurtherMoore extends Neighbourhood {

    public FurtherMoore(Cell c) {
        this.neighbours(c);
    }

    @Override
    protected void neighbours(Cell cell) {
        Grid grid = Nucleons.getGrid();

        int cx = cell.getX();
        int cy = cell.getY();

        super.addCell(grid.getCell(cx - 1, cy - 1) );
        super.addCell(grid.getCell(cx + 1, cy - 1) );
        super.addCell(grid.getCell(cx - 1, cy + 1) );
        super.addCell(grid.getCell(cx + 1, cy + 1) );

    }
}
