package sample;

import javafx.scene.paint.Color;

import java.util.*;

public class GrainsSelector {
    private List<Cell> listOfSelected;
    private Map<Integer, Color> listOfRemoved;


    public GrainsSelector() {
       this.listOfSelected = new ArrayList<>();
        listOfRemoved = new HashMap<>();
    }

    public void selectGrain(int x, int y) {

        Grid grid = Nucleons.getGrid();


            int state = grid.getCell(x, y).getState();

            for (Cell cell : grid.getGrid()) {
                if (cell.getState() == state) {
                    listOfSelected.add(cell);
                }
            }
            changeColorForSelectedGrain(state);

    }

    public void unselect(int x, int y) {
        Grid grid = Nucleons.getGrid();

            int state = grid.getCell(x, y).getState();
            listOfSelected.removeIf(cell -> cell.getState() == state);
            changeColorForUnselectedGrains(state);

    }

    public boolean checkIfSelected(int x, int y)  {
        Grid grid = Nucleons.getGrid();
        Cell cell = grid.getCell(x, y);
        return listOfSelected.contains(cell);
    }

    public void unselectAll() {
        listOfRemoved.forEach( (i, c) -> Nucleons.getGrainsColors().put(i, listOfRemoved.get(i)));
        listOfSelected.clear();
        listOfRemoved.clear();
    }

    private void changeColorForSelectedGrain(int state) {
        listOfRemoved.put(state, Nucleons.getColorForGrain(state));
        Nucleons.getGrainsColors().put(state, Color.MAGENTA);
    }

    private void changeColorForUnselectedGrains(int state) {
        Nucleons.getGrainsColors().put(state, listOfRemoved.get(state));
        listOfRemoved.remove(state);
    }

    public List<Cell> getListOfSelected() {
        return listOfSelected;
    }

    public ArrayList<Integer> getSelectedGrainsId() {
        Set<Integer> selected = new HashSet<>();
        for (Cell c : listOfSelected)
            selected.add(c.getState());
        return new ArrayList<>(selected);
    }

}
