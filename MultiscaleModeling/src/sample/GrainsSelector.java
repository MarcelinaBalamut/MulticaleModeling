package sample;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class GrainsSelector {
    private List<Cell> listOfSelected;
    private Map<Integer, Color> removedColors;

    public GrainsSelector() {
       this.listOfSelected = new ArrayList<>();
        removedColors = new HashMap<>();
    }

    public void select(int x, int y) {

        Grid grid = Nucleons.getGrid();

            int state = grid.getCell(x, y).getState();

            for (Cell cell : grid.getGrid()) {
                if (cell.getState() == state)
                {
                    listOfSelected.add(cell);

                }
            }
            changeColorToSelected(state);
    }

    public void unselect(int x, int y) {
        Grid grid = Nucleons.getGrid();

            int state = grid.getCell(x, y).getState();
            listOfSelected.removeIf(cell -> cell.getState() == state);
            changeColorToUnSelected(state);

    }

    public boolean checkIfSelected(int x, int y) {
        Grid grid = Nucleons.getGrid();
        Cell cell = grid.getCell(x, y);
        return listOfSelected.contains(cell);
    }

    private void changeColorToSelected(int state) {
        removedColors.put(state, Nucleons.getColorForGrain(state));
        Nucleons.getGrainsColors().put(state, Color.MAGENTA);
    }

    private void changeColorToUnSelected(int state) {
        Nucleons.getGrainsColors().put(state, removedColors.get(state));
        removedColors.remove(state);
    }

    public List<Cell> getSelectedCells() {
        return listOfSelected;
    }

    public ArrayList<Integer> getListOfSelectedGrains() {
        Set<Integer> set = new HashSet<>();
        for (Cell c : listOfSelected)
            set.add(c.getState());
        return new ArrayList<>(set);
    }

    public void selectAllGrains() {
        List<Cell> gridWithUnselected = Nucleons.getGrid().getGrid().stream()
                .filter(c -> !listOfSelected.contains(c)).collect(Collectors.toList());

        for (Cell cell : gridWithUnselected) {
            if (cell.getState() != 1 && cell.getState() != 0) {
                listOfSelected.add(cell);
                if (!removedColors.containsKey(cell.getState())) {
                    changeColorToSelected(cell.getState());
                }
            }
        }
    }

    public boolean checkIfAllSelected() {
        List<Cell> grid = Nucleons.getGrid().getGrid();
        Optional<Cell> empty = grid.stream().filter(cell -> cell.getState() != 0 &&
                cell.getState() != 1 && !listOfSelected.contains(cell)).findAny();
        return !empty.isPresent();
    }

    public void unselectAll() {
        ArrayList<Integer> listOfSelectedGrains = new ArrayList<>();
        for (Cell c: listOfSelected )
        {
            if (!listOfSelectedGrains.contains(c.getState()))
            listOfSelectedGrains.add(c.getState());
        }
        Map<Integer, Color> colors = Nucleons.getGrainsColors();
        removedColors.forEach(colors::put);
        listOfSelected.clear();
        removedColors.clear();
    }

    public void removeColorsGrains() {
        List<Integer> states = new ArrayList<>();
        states.addAll(removedColors.keySet());
        Nucleons.getGrainsColors().keySet().removeIf(key -> key != 0 && key != 1 && !states.contains(key));

    }
}
