package sample;




import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Nucleons {

    private static Grid grid;
    private static int numberOfGrains;
    private static String neighbourhoodType;
    private static Map<Integer, Color> grainsColors = new HashMap<>();

    static {
        grainsColors.put(0, Color.WHITE);
    }

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
        grainsColors.keySet().removeIf(key -> !(key.equals(0)));
    }
}

