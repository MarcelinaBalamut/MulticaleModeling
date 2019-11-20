package sample;




import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Nucleons {

    private static Grid grid;
    private static int numberOfGrains;
    private static String neighbourhoodType;
    private static int numberOfInclusions;
    private static Map<Integer, Color> grainsColors = new HashMap<>();
    private static int numberOfStructures;


    private Nucleons() {}

    public static void setGrid(Grid g) {
        grid = g;
    }
    public static Grid getGrid() {
        return grid;
    }

    public static int getNumberOfGrains() {
        return numberOfGrains;
    }

    public static void setNumberOfGrains(int numberOfGrains) {
        Nucleons.numberOfGrains = numberOfGrains;
    }

    public static String getNeighbourhoodType() {
        return neighbourhoodType;
    }

    public static void setNeighbourhood(String neighbourhoodType) {
        Nucleons.neighbourhoodType = neighbourhoodType;
    }

    public static Map<Integer, Color> getGrainsColors() {
        return grainsColors;
    }

    public static Color getColorForGrain(int i) {
        return grainsColors.getOrDefault(i, Color.WHITE);
    }

    public static void setGrainsColors(Map<Integer, Color> grainsColors) {
        Nucleons.grainsColors = grainsColors;
    }

    public static void cleanGrid() {

        grid = null;
        numberOfGrains = 0;
        neighbourhoodType = null;
        grainsColors.keySet().removeIf(key -> !(key.equals(0)) && !(key.equals(1)));
        numberOfInclusions = 0;
        numberOfStructures = 0;
    }
    public static int getNumberOfInclusions() {
        return numberOfInclusions;
    }

    public static void setNumberOfInclusions(int numberOfInclusions) {
        Nucleons.numberOfInclusions = numberOfInclusions;
    }

    public static boolean checkIfAnyEmptySpaces() {
        for (Cell c: Nucleons.getGrid().getGrid()) {
            if (c.getState() == 0)
                return true;
        }
        return false;
    }

    public static int getNumberOfStructures() {
        return numberOfStructures;
    }

    public static void setNumberOfStructures(int numberOfStructures) {
        Nucleons.numberOfStructures = numberOfStructures;
    }
}

