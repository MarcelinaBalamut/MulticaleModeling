package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Nucleation {
    private boolean continueAgain;
    private List<Cell> locationCells;
    private int numberOfGrains;
    private int index;
    private String type;

    private Random generator;

    public Nucleation(String type, List<Cell> locationsPool, int numberOfGrains) {
        this.locationCells = new ArrayList<>(locationsPool);
        this.type = type;
        this.numberOfGrains = numberOfGrains;
        this.generator = new Random();
        index = Nucleons.getNumberOfGrains() + 2;
        if (!locationsPool.isEmpty())
            continueAgain = true;
    }

    public boolean shouldPerformNucleation() {
        if (!continueAgain)
            return false;

        locationCells.removeIf(Cell::isRecrystallized);

        if (locationCells.isEmpty())
            continueAgain = false;

        return continueAgain;
    }

    public List<Cell> nucleate() {
        List<Cell> recrystallizedCells = new ArrayList<>();
        if (type.equals(SRXMC.BEGINNING)) {
            recrystallizedCells.addAll(performNucleation());
            continueAgain = false;
        }
        else if (type.equals(SRXMC.CONSTANT)) {
            recrystallizedCells.addAll(performNucleation());
        }
        else if (type.equals(SRXMC.INCREASING)) {
            recrystallizedCells.addAll(performNucleation());
            numberOfGrains *= 2;
        }
        return recrystallizedCells;
    }

    private List<Cell> performNucleation() {
        List<Cell> recrystallizedCells = new ArrayList<>();
        int i;
        for (i = 1; i <= numberOfGrains; i++) {
            boolean toBeContinued = true;
            while (true) {
                if (locationCells.isEmpty()) {
                    i--;
                    continueAgain = false;
                    toBeContinued = false;
                    break;
                }
                Cell cell = locationCells.get(generator.nextInt(locationCells.size()));
                if (cell.isRecrystallized()) {
                    locationCells.remove(cell);
                }
                else {
                    cell.setRecrystallized(true);
                    cell.setEnergy(0f);
                    cell.setState(index + i - 1);
                    recrystallizedCells.add(cell);
                    locationCells.remove(cell);
                    break;
                }
            }
            if (!toBeContinued)
                break;
        }
        index += i;
        Nucleons.setNumberOfGrains(Nucleons.getNumberOfGrains() + i);
        chooseGrainsColorsSRXMC(i);
        return recrystallizedCells;
    }

    private void colorGrains(int numberOfNucleon) {
        Map<Integer, Color> colors = Nucleons.getGrainsColors();
        int colorSize = colors.size();
        for (int i = colorSize; i < colorSize + numberOfNucleon; i++){

            Color color = Color.color(Math.random(), Math.random(), Math.random());
            if (colors.equals(Color.WHITE) || colors.equals(Color.BLACK) || colors.equals(Color.MAGENTA) || colors.containsValue(colors)) {
                --i;
                continue;
            }

            colors.put(i, color);

        }
    }

    public static void chooseGrainsColorsSRXMC(int numberOfNucleatedGrains) {
        Map<Integer, Color> colors = Nucleons.getGrainsColors();
        int start = colors.size();
        Random generator = new Random();
        for (int i = start; i < start + numberOfNucleatedGrains; i++) {
            float min = (float)330/360;
            float max = (float)390/360;
            float hue = generator.nextFloat() * (max - min) + min;
            float s = generator.nextFloat() * 0.5f + 0.5f;
            float b = generator.nextFloat() * 0.3f + 0.3f;

            Color color = Color.hsb(hue, s, b);

            if (color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.MAGENTA) ||
                    colors.containsValue(color)) {
                --i;
                continue;
            }
            colors.put(i, color);
        }
    }
}
