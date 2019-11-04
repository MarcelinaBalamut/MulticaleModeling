package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    private int numberOfNucleon;
    //private Set<Integer> rdCells = new HashSet<>();
    private Set<Integer> randomCells = new HashSet<>();
    public  String SEPARATOR = ";";
    int TOP_PADDING = 30;
    int SCALE = 2;
    boolean clearGrid = true;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearGrid = true;
        gc = canvas.getGraphicsContext2D();
        numberOfNucleon = Integer.parseInt(embryCount.getText());

        boundary.getItems().addAll(
                "Periodical"
                // "Absorbing"
        );

        neighborhood.getItems().addAll(
                // "Moore",
                "VonNeumann"
//                "Heksagonal left",
//                "Heksagonal right",
//                "Random Heksagonal",
//                "Random Pentagonal"
        );
        inclusionTypeId.getItems().addAll(
                "Circle",
                "Square"
        );
    }
    public void evenlyRandom(MouseEvent mouseEvent) {
    }

    public void startButton(ActionEvent actionEvent) {
        if (Nucleons.checkIfAnyEmptySpaces()) {
            Growth growth = new Growth();
            growth.growGrains();
            print();
        }
    }

    public void setSize(ActionEvent actionEvent) {
    }

    public void finalRand(ActionEvent actionEvent) {

        if(Nucleons.getGrid() == null){
            gc.clearRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            prepareGrid();
        }

       randomCells.clear();
        numberOfNucleon = Integer.parseInt(embryCount.getText());



        validateGrid();

        //cleanNucleation();
        Nucleons.setNumberOfGrains(Nucleons.getNumberOfGrains() + numberOfNucleon);
        colorGrains();
        randNucleons();
        setState( numberOfNucleon);
        print();

    }


    public void stop(ActionEvent actionEvent) {
    }

    public void clear(ActionEvent actionEvent) {

        cleanGrowth();
        cleanNucleation();
        Nucleons.cleanGrid();
        gc.clearRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        print();


    }

    private void randNucleons() {

//        for (Cell c : Nucleons.getGrid().getGrid()) {
//            c.setState(0);
//        }

        if (Nucleons.checkIfAnyEmptySpaces()) {
            Random generator = new Random();

            for (int i = 0; i < Nucleons.getNumberOfGrains(); i++) {
                int random = generator.nextInt(Nucleons.getGrid().getGrid().size());
                if (this.randomCells.contains(random)) {
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

        for (int i = 2; i <= Nucleons.getNumberOfGrains() +1 ; i++) {

            Color color = Color.color(Math.random(), Math.random(), Math.random());

            if (color.equals(Color.WHITE) || grains.containsValue(color)) {  --i;  continue;  }

            grains.put(i, color);


        }
        Nucleons.setGrainsColors(grains);
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
            for (Cell c : Nucleons.getGrid().getGrid())
            {
                if(c.getState() == 0) {
                    gc.setFill(Color.WHITE);
                }
                else if (c.getState() == 1) {
                    gc.setFill(Color.BLACK);

                }
                else {
                    gc.setFill(Nucleons.getGrainsColors().get(c.getState()));
                }
                gc.fillRect(c.getX() * 2, 10 + c.getY()*2, 2, 2);


            }
        }
    }


    private void prepareGrid(){
        int widthSize = Integer.parseInt(width.getText());
        int heightSize = Integer.parseInt(height.getText());
        if (widthSize < 300 || heightSize < 300) {
            throw new IllegalArgumentException("Grid should be grater than 300x300");
        }
        boolean isPeriodic = checkIfPeriodic();
        Nucleons.cleanGrid();
        Nucleons.setGrid(new Grid(widthSize, heightSize, isPeriodic));

    }

    private boolean checkIfPeriodic() {
        boolean isPeriodic =  false;

        if(boundary.getValue() == "Periodical"){
            isPeriodic =true;
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
    private File createFile(){

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
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            Nucleons.cleanGrid();

            String currentLine = br.readLine();
            String[] line = currentLine.split(";");

            Nucleons.setNumberOfGrains(Integer.parseInt(line[3]));
            Nucleons.setNumberOfInclusions(Integer.parseInt(line[4]));
            Nucleons.setNeighbourhood(line[5]);

            Grid grid = new Grid(Integer.parseInt(line[0]),Integer.parseInt(line[1]),Boolean.parseBoolean(line[2]));

            br.readLine();

            while ((currentLine = br.readLine()) != null) {
                line = currentLine.split(";");
                Cell cell = new Cell(Integer.parseInt(line[0]),Integer.parseInt(line[1]));
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
                    Color color = intToRgb((image.getRGB(x, y)));
                    Cell cell = new Cell(x, y);
                    if (color.equals(Color.WHITE))
                    {
                        cell.setState(0);
                    }
                    else if(color.equals((Color.BLACK))) {
                        Nucleons.setNumberOfInclusions(1);
                        cell.setState(1);
                    }
                    else if (grainsColors.containsKey(color))
                    {
                        cell.setState(grainsColors.get(color));
                    }
                    else
                        {
                        numberOfGrains++;
                        grainsColors.put(color, numberOfGrains);
                        cell.setState(numberOfGrains);
                    }
                    grid.setCell(cell.getX(), cell.getY(), cell);
                }
            }

            Nucleons.setGrid(grid);
            Nucleons.setNumberOfGrains(numberOfGrains-1);

            Map<Integer, Color> colorsInversed = grainsColors.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
            Nucleons.setGrainsColors(colorsInversed);
            print();
        }
        catch (IOException e) {
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

        try(PrintWriter printWriter = new PrintWriter(file)) {
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

        try{
            ImageIO.write(image, "bmp", file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    private void validateGrid() {
        if (numberOfNucleon < 2)
            throw new IllegalArgumentException("Set number of grains grater than 2");
        if (numberOfNucleon > Nucleons.getGrid().getGrid().size())
            throw new IllegalArgumentException("Set number of grains less than size");
    }

    //    public  int RGBtoINT(Color c) {
//        int rgb = c.getRed();
//        rgb = (rgb << 8) + c.getGreen();
//        rgb = (rgb << 8) + c.getBlue();
//        return rgb;
//    }
//
    private Color intToRgb(int rgb) {
        int b =  rgb & 255;
        int g = (rgb >> 8) & 255;
        int r =   (rgb >> 16) & 255;

        Color color = (Color.rgb(b,g,r));
        return color;
    }

    private int convertColor(float red, float green, float blue){
        int  r = Math.round(255 * red);
        int  b = Math.round(255 * blue);
        int  g = Math.round(255 * green);

         r = (r << 16) & 0x00ff0000;
        g = (g<<8) & 0x0000ff00;
        b = b & 0x000000ff;
        return 0xff000000 | r | g | b;

    }

    public void addInclusions(ActionEvent actionEvent) {
        int inclusionNumber = Integer.parseInt(inclusionId.getText());
        Nucleons.setNumberOfInclusions(inclusionNumber);
        if(Nucleons.getGrid() == null){
            prepareGrid();
            addInclusions(actionEvent);
        }

        int inclusionsSize = Integer.parseInt(inclusionSizeId.getText());
        int inclusionsNumber = Integer.parseInt(inclusionId.getText());

        String inclusionType =  inclusionTypeId.getValue().toString();


                    if(Nucleons.getNumberOfInclusions() != 0) {
                        int inclusionSize = Integer.parseInt(inclusionSizeId.getText());

                        Inclusion inclusion = new Inclusion();

                        if (Nucleons.getNumberOfGrains() == 0)
                            inclusion.addInclusion(inclusionNumber, inclusionSize, inclusionType);
                        else
                            inclusion.addBoundaryInclusion(inclusionNumber, inclusionSize, inclusionType);
                    }
                    else {
                        System.out.println("dziene zero");
                    }


                print();
    }
}
