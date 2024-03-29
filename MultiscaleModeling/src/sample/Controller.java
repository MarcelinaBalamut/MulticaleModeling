package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Controller implements Initializable {
    public TextField width;
    public TextField height;
    public Canvas canvas;
    public TextField embryCount;
    public ComboBox boundary;
    public ComboBox neighborhood;
    public Button clear;
    public Button inclusionsAddButtonId;
    public TextField inclusionSizeId;
    public TextField inclusionId;
    public ComboBox inclusionTypeId;
    public GraphicsContext gc;
    public TextField shapePercentage;
    public RadioButton shapeControlId;
    public ComboBox structureTypeId;
    public Button chooseId;
    public ComboBox borderSizeId;
    public RadioButton monteCarloId;
    public TextField stepsId;
    public TextField coefficientId;
    public ComboBox energyDistributionId;
    public TextField energyInsideFieldId;
    public TextField energyThresholdId;
    public TextField energyEdgesId;
    public ComboBox nucleationLocationId;
    public TextField nucleationAmountId;
    public ComboBox nucleationTypeId;
    public TextField MCSRXStepsId;
    public Button energyDistributionButton;
    public Button growthSRXButton;

    private int numberOfNucleon;
    private Set<Integer> randomCells = new HashSet<>();
    private List<Cell> borderCells;
    int padding = 0;
    boolean clearGrid = true;
    private GrainsSelector grainsSelector;
    boolean isClecable =  false;
    public final int SCALE = 1;

    private final float MIN_HUE = 135f/360;
    private final float MAX_HUE = 225f/360;

    private float min;
    private float max;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearGrid = true;
        gc = canvas.getGraphicsContext2D();
        numberOfNucleon = Integer.parseInt(embryCount.getText());


        boundary.getItems().addAll(
                "Periodical"
        );

        neighborhood.getItems().addAll(

                "VonNeumann",
                "Moore"
        );
        inclusionTypeId.getItems().addAll(
                "Circle",
                "Square"
        );
        structureTypeId.getItems().addAll(
                "Substructure",
                 "Dual-Phase",
                "Disable",
                "None"
        );
        borderSizeId.getItems().addAll(
                "0",
                "1",
                "2",
                "3",
                "4"
        );
        energyDistributionId.getItems().addAll(
                "Homogenous",
                "Heterogenous"

        );
        nucleationTypeId.getItems().addAll(
                "Constant",
                "Increasing",
                "At the beginning"

        );
        nucleationLocationId.getItems().addAll(
                "Anywhere",
                "On boundaries"

        );

    }

    public void evenlyRandom(MouseEvent mouseEvent) {
    }

    public void startButton(ActionEvent actionEvent) {


        String nb = neighborhood.getValue().toString();

        if (Nucleons.checkIfAnyEmptySpaces()) {

            if (!shapeControlId.isSelected()|| neighborhood.getValue().toString()=="VonNeumann")
            {
                Growth growth = new Growth();
                growth.growGrains(nb, shapeControlId.isSelected());
                print();
            } else {

                int probability = Integer.parseInt(shapePercentage.getText());
                if (probability <= 0 || probability > 100)
                    throw new NullPointerException();
                Nucleons.setNeighbourhood(neighborhood.getValue().toString());
                Growth g = new Growth();
                g.shapeControlGrowth();
                g.growGrainsMoore(probability);
                print();

            }

        }
    }

    public void setSize(ActionEvent actionEvent) {
    }

    public void finalRand(ActionEvent actionEvent) {

        numberOfNucleon = Integer.parseInt(embryCount.getText());
        if(!monteCarloId.isSelected()) {


            if (Nucleons.getGrid() == null) {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                prepareGrid();
            }

            grainsSelector = new GrainsSelector();
            randomCells.clear();

            validateGrid();
            Nucleons.setNumberOfGrains(Nucleons.getNumberOfGrains() + numberOfNucleon);
            if (Nucleons.getGrainsColors() == null || Nucleons.getGrainsColors().size() == 0)
                colorGrains();
            else
                colorGrains(numberOfNucleon);
            randNucleons(numberOfNucleon);
            setState(numberOfNucleon);
            print();
        }
        else{



                if (Nucleons.getGrid() == null) {
                    gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                    prepareGrid();
                }
            grainsSelector = new GrainsSelector();
                int numberOfGrains = Integer.parseInt(embryCount.getText());
                if (numberOfGrains < 2)
                    throw new IllegalArgumentException("Type more grains");
                if (numberOfGrains > Nucleons.getGrid().getGrid().size())
                    throw new IllegalArgumentException("Type less grains!");

                int steps = Integer.parseInt(stepsId.getText());
                if (steps < 1)
                    throw new IllegalArgumentException("Type more steps");

                float coeff = Float.parseFloat(coefficientId.getText());
                if (coeff < 0.1)
                    throw new IllegalArgumentException("Type greater coefficient");
                else if(coeff > 1.0)
                    throw new IllegalArgumentException("The maximum value of J is 1");

                Nucleons.setNumberOfGrains(Nucleons.getNumberOfGrains() + numberOfGrains);
                if (Nucleons.getGrainsColors() == null || Nucleons.getGrainsColors().size() == 0)
                   colorGrains();
                else
                    colorGrains(numberOfGrains);

                MonteCarlo growth = new MonteCarlo(numberOfGrains, coeff, steps);
                growth.growGrains("Moore");

                print();





        }
    }

    public void stop(ActionEvent actionEvent) {
    }

    public void clear(ActionEvent actionEvent) {

        cleanGrowth();
        cleanNucleation();
        Nucleons.cleanGrid();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        print();

    }

    private void randNucleons(int nGrains) {


        if (Nucleons.checkIfAnyEmptySpaces()) {
            Random generator = new Random();
            for (int i = 0; i < nGrains; i++) {
                int random = generator.nextInt(Nucleons.getGrid().getGrid().size());
                if (this.randomCells.contains(random) || Nucleons.getGrid().getCellByPosition(random).getState()>0) {
                    i--;
                } else {
                    this.randomCells.add(random);
                }
            }
        }
    }


    private void setState(int grainsNumber) {
        Iterator<Integer> iterator = randomCells.iterator();
        int id = 2 + (Nucleons.getNumberOfGrains() - grainsNumber);
        while (iterator.hasNext()) {
            int i = iterator.next();
            Nucleons.getGrid().getCellByPosition(i).setState(id);
            id++;
        }
    }


    private void colorGrains() {
        HashMap<Integer, Color> grains = new HashMap<>();
        grains.put(0, Color.WHITE);
        grains.put(1, Color.BLACK);

        for (int i = 2; i <= Nucleons.getNumberOfGrains() + 1; i++) {
            double r = Math.random();
            double gg =  Math.random();
            double b =  Math.random();

            Color color = Color.color(r, gg,b);

            Color rr = Color.hsb(r,gg,b);
//            if (color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.MAGENTA) || grains.containsValue(color)) {
//                --i;
//                continue;
//            }
            if (color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.MAGENTA) ||
                    rr.getRed() >= (float)330/360 || rr.getRed() <= (float)30/360 || grains.containsValue(color)) {
                --i;
                continue;
            }

            grains.put(i, color);

        }
        Nucleons.setGrainsColors(grains);
    }
    private void colorGrains(int numberOfNucleon) {
        Map<Integer, Color> colors = Nucleons.getGrainsColors();
        int colorSize = colors.size();
        for (int i = colorSize; i < colorSize + numberOfNucleon; i++){

            double r = Math.random();
            double gg =  Math.random();
            double b =  Math.random();

            Color color = Color.color(r, gg,b);

            Color rr = Color.hsb(r,gg,b);
//            if (color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.MAGENTA) || colorsMap.containsValue(color)) {
//                --i;
//                continue;
//            }
            if (color.equals(Color.WHITE) || color.equals(Color.BLACK) || color.equals(Color.MAGENTA) ||
                    rr.getRed() >= (float)330/360 || rr.getRed()  <= (float)30/360 || colors.containsValue(color)) {
                --i;
                continue;
            }
            colors.put(i, color);
        }
    }
    private static void cleanNucleation() {
        Nucleons.getGrainsColors().keySet().removeIf(key -> !(key.equals(0)) && !(key.equals(1)));
        Nucleons.setNumberOfGrains(0);
        Nucleons.setNeighbourhood(null);
        Nucleons.getGrid().getGrid().forEach(cell -> {
            cell.setState(0);
            cell.setNextState(0);
            cell.setNeighbourhood(null);

        });
    }
    private void print() {
        if (Nucleons.getGrid() != null) {
            for (Cell c : Nucleons.getGrid().getGrid()) {
                if (c.getState() == 0) {
                    gc.setFill(Color.WHITE);
                } else if (c.getState() == 1) {
                    gc.setFill(Color.BLACK);
                }
                else
                    gc.setFill(Nucleons.getGrainsColors().get(c.getState()));
                gc.fillRect(c.getX() * 2, padding + c.getY() * 2, 2, 2);

            }
        }
    }
    
    private void prepareGrid() {
        int widthSize = Integer.parseInt(width.getText());
        int heightSize = Integer.parseInt(height.getText());
        canvas.setWidth(widthSize*2);
        canvas.setHeight(heightSize*2);
        if (widthSize < 300 || heightSize < 300) {
            throw new IllegalArgumentException("Grid should be grater than 300x300");
        }
        boolean isPeriodic = checkIfPeriodic();
        Nucleons.cleanGrid();
        Nucleons.setGrid(new Grid(widthSize, heightSize, isPeriodic));

    }

    private boolean checkIfPeriodic() {
        boolean isPeriodic = false;

        if (boundary.getValue() == "Periodical") {
            isPeriodic = true;
        }
        return isPeriodic;
    }

    private static void cleanGrowth() {

        Nucleons.setNeighbourhood(null);
        for (Cell c : Nucleons.getGrid().getGrid()) {
            c.setState(0);
            c.setNeighbourhood(null);
        }
    }

    private File createFile() {

        Stage stage = new Stage();
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );

        File file = chooser.showOpenDialog(stage);
        return file;
    }

    public void importTxtButton(ActionEvent actionEvent) {
        File file = createFile();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            Nucleons.cleanGrid();

            String currentLine = br.readLine();
            String[] line = currentLine.split(";");

            Nucleons.setNumberOfGrains(Integer.parseInt(line[3]));
            Nucleons.setNumberOfInclusions(Integer.parseInt(line[4]));
            Nucleons.setNeighbourhood(line[5]);

            Grid grid = new Grid(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Boolean.parseBoolean(line[2]));

            br.readLine();

            while ((currentLine = br.readLine()) != null) {
                line = currentLine.split(";");
                Cell cell = new Cell(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                cell.setState(Integer.parseInt(line[2]));
                grid.setCell(Integer.parseInt(line[0]), Integer.parseInt(line[1]), cell);
            }

            Nucleons.setGrid(grid);
            colorGrains();
            print();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importBmpButton(ActionEvent actionEvent) {
        File file = createFile();

        try {
            BufferedImage image = ImageIO.read(file);
            Grid grid = new Grid(image.getWidth(), image.getHeight(), false);

            Nucleons.cleanGrid();

            HashMap<Color, Integer> grainsColors = new HashMap<>();
            grainsColors.put(Color.WHITE, 0);
            grainsColors.put(Color.BLACK, 1);

            int numberOfGrains = 1;

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = parseIntToRgb((image.getRGB(x, y)));
                    Cell cell = new Cell(x, y);
                    if (color.equals(Color.WHITE)) {
                        cell.setState(0);
                    } else if (color.equals((Color.BLACK))) {
                        Nucleons.setNumberOfInclusions(1);
                        cell.setState(1);
                    } else if (grainsColors.containsKey(color)) {
                        cell.setState(grainsColors.get(color));
                    } else {
                        numberOfGrains++;
                        grainsColors.put(color, numberOfGrains);
                        cell.setState(numberOfGrains);
                    }
                    grid.setCell(cell.getX(), cell.getY(), cell);
                }
            }

            Nucleons.setGrid(grid);
            Nucleons.setNumberOfGrains(numberOfGrains - 1);

            Map<Integer, Color> colorsInversed = grainsColors.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            Nucleons.setGrainsColors(colorsInversed);
            print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportTxtButton(ActionEvent actionEvent) {
        File file = createFile();
        Grid grid = Nucleons.getGrid();

        StringBuilder gridText = new StringBuilder();

        String neighbourhoodType = Nucleons.getNeighbourhoodType();

        gridText.append(grid.getWidth()).append(";").append(grid.getHeight()).append(";").append(grid.isPeriodic()).append(";")
                .append(Nucleons.getNumberOfGrains()).append(";").append(Nucleons.getNumberOfInclusions()).append(";")
                .append(neighbourhoodType).append(System.lineSeparator()).append("X").append(";").append("Y").append(";")
                .append("ID").append(";").append(System.lineSeparator());

        grid.getGrid().forEach(cell ->
                gridText.append(cell.getX()).append(";").append(cell.getY()).append(";")
                        .append(cell.getState()).append(";").append(System.lineSeparator())
        );

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(gridText.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportBmpButton(ActionEvent actionEvent) {
        File file = createFile();
        Grid grid = Nucleons.getGrid();

        int width = grid.getWidth();
        int height = grid.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        grid.getGrid().forEach(c -> {
            int x = c.getX();
            int y = c.getY();

            float blue = (float) Nucleons.getColorForGrain(c.getState()).getBlue();
            float green = (float) Nucleons.getColorForGrain(c.getState()).getGreen();
            float red = (float) Nucleons.getColorForGrain(c.getState()).getRed();
            int color = convertColor(red, green, blue);

            image.setRGB(x, y, color);

        });

        try {
            ImageIO.write(image, "bmp", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void validateGrid() {
        if (numberOfNucleon < 2)
            throw new IllegalArgumentException("Set number of grains grater than 2");
        if (numberOfNucleon > Nucleons.getGrid().getGrid().size())
            throw new IllegalArgumentException("Set number of grains less than size");
    }

    private Color parseIntToRgb(int rgb) {
        int b = rgb & 255;
        int g = (rgb >> 8) & 255;
        int r = (rgb >> 16) & 255;

        Color color = (Color.rgb(b, g, r));
        return color;
    }

    private int convertColor(float red, float green, float blue) {
        int r = Math.round(255 * red);
        int b = Math.round(255 * blue);
        int g = Math.round(255 * green);

        r = (r << 16) & 0x00ff0000;
        g = (g << 8) & 0x0000ff00;
        b = b & 0x000000ff;
        return 0xff000000 | r | g | b;

    }

    public void addInclusions(ActionEvent actionEvent) {
        int inclusionNumber = Integer.parseInt(inclusionId.getText());
        String inclusionType = inclusionTypeId.getValue().toString();
        Nucleons.setNumberOfInclusions(inclusionNumber);
        if (Nucleons.getGrid() == null) {
            prepareGrid();
            addInclusions(actionEvent);
        }
        if (Nucleons.getNumberOfInclusions() != 0) {
            int inclusionSize = Integer.parseInt(inclusionSizeId.getText());

            Inclusion inclusion = new Inclusion();

            if (Nucleons.getNumberOfGrains() == 0)
                inclusion.addInclusion(inclusionNumber, inclusionSize, inclusionType);
            else
                inclusion.addInclusionOnBoundaries(inclusionNumber, inclusionSize, inclusionType);
        }
        print();
    }


    public void createStructure(ArrayList<Integer> listOfSelectedGrains, boolean isSubstructure) {
        if (isSubstructure)
            createSubstructure(listOfSelectedGrains);
        else
            createDualPhase(listOfSelectedGrains);
    }

    private void createSubstructure(ArrayList<Integer> listOfSelectedGrains) {
        deleteRestOfGrains(listOfSelectedGrains);
        Nucleons.setNumberOfGrains(listOfSelectedGrains.size());
    }

    private void createDualPhase(ArrayList<Integer> listOfSelectedGrains) {
        deleteRestOfGrains(listOfSelectedGrains);
        Nucleons.setNumberOfGrains(1);
        
    }

    private void deleteRestOfGrains(ArrayList<Integer> listOfSelectedGrains) {
        Nucleons.getGrid().getGrid().forEach(cell -> {
            if (cell.getState() != 1 && !listOfSelectedGrains.contains(cell.getState())) {
                cell.setState(0);
                cell.setNextState(0);
                cell.setNeighbourhood(null);
            }
        });
    }

    private void growthForSubstructure() {
        Map<Integer, Integer> colors = new HashMap<>();
        Map<Integer, Color> grainColors = new HashMap<>();
        grainColors.put(0, Color.WHITE);
        grainColors.put(1, Color.BLACK);

        int id = 2;
        for (Cell cell : Nucleons.getGrid().getGrid()) {
            if (cell.getState() != 0 && cell.getState() != 1) {
                if (!colors.containsKey(cell.getState())) {
                    grainColors.put(id, Nucleons.getColorForGrain(cell.getState()));
                    colors.put(cell.getState(), id);
                    id++;
                }
                cell.setState(colors.get(cell.getState()));
            }
        }
        Nucleons.setNumberOfStructures(id - 2);
        Nucleons.setGrainsColors(grainColors);
    }

    private void growthForDualPhase() {
        boolean firstTemp = true;
        Map<Integer, Color> grainColors = new HashMap<>();
        grainColors.put(0, Color.WHITE);
       grainColors.put(1, Color.BLACK);

        int id = 2;
        for (Cell cell : Nucleons.getGrid().getGrid()) {
            if (cell.getState() != 0 && cell.getState() != 1) {
                if (firstTemp) {
                    grainColors.put(id, Nucleons.getColorForGrain(cell.getState()));
                    firstTemp = false;
                }
                cell.setState(id);
            }
        }
        Nucleons.setNumberOfStructures(1);
        Nucleons.setGrainsColors(grainColors);
    }


    public void deleteGrain(ActionEvent actionEvent) {

    }

    public void mouseClick(MouseEvent mouseEvent) {

        String structure = structureTypeId.getValue().toString();
        if (structure!="Disable") {

                    int x = (int) mouseEvent.getX();
                    int y = (int) (mouseEvent.getY() - padding);

int widthInput =  Integer.parseInt(width.getText());
int heightInput =  Integer.parseInt(height.getText());
            double width = canvas.getGraphicsContext2D().getCanvas().getWidth() / widthInput;
            double height = canvas.getGraphicsContext2D().getCanvas().getHeight() / heightInput;
            x = (int) ((x / width) *width);
            y = (int) ((y / height) *height);

            int canvasX = (int) (x/height);
            int canvasY = (int) (y/width);

            if(canvasX > 300 -1 )
                canvasX = 300 -1;
            if (canvasX <0)
                canvasX = 0;

            if(canvasY > 300 -1 )
                canvasY = 300 -1;
            if (canvasY <0)
                canvasY = 0;

            int Xend = canvasX;
            int Yend = canvasY;

            x = Xend;
            y = Yend;
                    try {
                        if (!grainsSelector.checkIfSelected(x, y)) {
                            grainsSelector.select(x, y);
                        } else {
                            grainsSelector.unselect(x, y);
                        }
                       print();
                    } catch (NullPointerException exc) {
                    }

        } else {
            if(grainsSelector != null) {
                grainsSelector.unselectAll();
                grainsSelector = null;
            }
            print();
        }

    }

    public void chooseGrains(ActionEvent actionEvent) {
    }

    public void choose(ActionEvent actionEvent) {

        String structure = structureTypeId.getValue().toString();

         if(grainsSelector.getSelectedCells().isEmpty()) {
                    throw new NullPointerException();
                }
                if( structure =="Substructure") {

                    createStructure(grainsSelector.getListOfSelectedGrains(), true);
                    grainsSelector.removeColorsGrains();
                    grainsSelector.unselectAll();
                    growthForSubstructure();
                    isClecable = true;
                    grainsSelector = null;
                    print();
                }
                else if( structure =="Dual-Phase"){

                    createStructure(grainsSelector.getListOfSelectedGrains(), false);
                    grainsSelector.removeColorsGrains();
                    grainsSelector.unselectAll();
                    growthForDualPhase();

                    isClecable = true;
                    grainsSelector = null;
                    print();
                }

                else if( structure =="Disable"){
                    isClecable = false;

                }

    }

    public void selectAllGrains(ActionEvent actionEvent) {

        if (grainsSelector != null) {


            if (!grainsSelector.checkIfAllSelected()) {
                grainsSelector.selectAllGrains();
            } else {
                grainsSelector.unselectAll();
            }
            print();
        }

    }

    public void drawBorders(ActionEvent actionEvent) {

            if(!grainsSelector.getSelectedCells().isEmpty())
            {
            String borderSize = borderSizeId.getValue().toString();

            borderCells = new ArrayList<>();
            getBorders(grainsSelector.getSelectedCells(), Integer.parseInt(borderSize));
            grainsSelector.unselectAll();
            shapeControlId.setSelected(false);
            grainsSelector = null;
            print();
            }
    }

    public void getBorders(List<Cell> selected, int size) {
        for (Cell cell : selected) {
            if (cell.getState() == 1 || cell.getState() == 0)
                continue;
            Neighbourhood neighbourhood = new Moore(cell);
            for (Cell n : neighbourhood.getNeighbours() ) {
                if ( n.getState() != cell.getState()) {
                    borderCells.add(n);
                    n.setState(1);
                }
            }
        }
        if (size > 0)
            enlargeBoundaries(size);
    }

    public void cleareGrid(ActionEvent actionEvent) {
        int counter = 0;
        List<Cell> grid = Nucleons.getGrid().getGrid();
        for (Cell cell : grid)
        {
            cell.setNextState(0);
            if (cell.getState() != 1)
            {
                cell.setState(0);
            }
            if (cell.getState() == 1)
            {
                counter++;
            }
        }
        print();
        countPercentage(counter, grid);
    }


   public void countPercentage(int counter, List<Cell> grid){
       double boundaryPercentage = ((double) counter) / grid.size() * 100;
       System.out.println("Boundary cells percentage: " + boundaryPercentage + "%");
    }

    private List<Cell> largeInOne(List<Cell> borders, List<Cell> previous, int size) {
        if (size > 0) {
            Set<Cell> newNeighbourhood = new HashSet<>();

            previous.forEach( i ->
                    newNeighbourhood.addAll(new Moore(i).getNeighbours().stream().filter(
                            neighbourCell -> !borders.contains(neighbourCell)).collect(Collectors.toList())));

            newNeighbourhood.forEach(c -> c.setState(1));

            borders.addAll(newNeighbourhood);
            return largeInOne(borders, new ArrayList<>(newNeighbourhood), --size);
        }
        else {
            return borders;
        }

    }

    private void enlargeBoundaries(int size) {
        List<Cell> borders = new ArrayList<>();

        borders.addAll(Nucleons.getGrid().getGrid().stream().filter(cell -> cell.getState() == 1).collect(Collectors.toList()));

        for (Cell cell : borderCells) {
            List<Cell> root = new ArrayList<>();
            root.add(cell);
            largeInOne(borders, root, size);
        }
    }
    public List<Cell> getBorderCells() {
        return borderCells;
    }

    public void printEnergy() {
        min = 0f;
        max = 16f;
        paintEnergy();
    }


    protected void paintEnergy() {


        if (Nucleons.getGrid() != null) {
            for (Cell c : Nucleons.getGrid().getGrid()) {
                if (c.getState() == 0) {
                    gc.setFill(Color.WHITE);
                } else if (c.getState() == 1) {
                    gc.setFill(Color.BLACK);
                }
                else{
                    gc.setFill(calculateColourForEnergy(c.getEnergy()));

                }
                gc.fillRect(c.getX() * 2, padding + c.getY() * 2, 2, 2);

            }
        }

    }

    private Color calculateColourForEnergy(float energy) {
        float value = 1 - (energy - min) / (max - min);
//        float color = value* MAX_HUE + (1-value)* MIN_HUE;
        return (Color.color(value, 0.5, 0.8f));
    }

    public void growthSRX(ActionEvent actionEvent) {
            int numberOfNucleons = Integer.parseInt(nucleationAmountId.getText());
            String type = (String) nucleationTypeId.getValue();
            String location = (String) nucleationLocationId.getValue();
            int steps = Integer.parseInt(MCSRXStepsId.getText());
            float coefficient = Float.parseFloat(coefficientId.getText());

            SRXMC srxmc = new SRXMC(type, location, numberOfNucleons, coefficient, steps);
            srxmc.growGrains("Moore");

            print();
    }

    public void energyDistribution(ActionEvent actionEvent) {

            float insideEnergy = Float.parseFloat(energyInsideFieldId.getText());
            float borderEnergy = insideEnergy;
            if (energyDistributionId.getValue() == "Heterogenous") {
                borderEnergy = Float.parseFloat(energyEdgesId.getText());
            }
            int threshold = Integer.parseInt(energyThresholdId.getText());
            SRXMC.distributeEnergy(insideEnergy, borderEnergy, threshold);
            printEnergy();



    }
}
