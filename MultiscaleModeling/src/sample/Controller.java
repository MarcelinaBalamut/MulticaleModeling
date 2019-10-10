package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Controller {


    @FXML
    public ComboBox neighborhood;
    public ComboBox random;
    public ComboBox boundary;
    public TextField radius;
    public TextField pointsNumber;
    public Button stop;
    public Button ok;
    public TextField width;
    public TextField height;
    Nb nb = new Nb();
    @FXML
    Canvas canvas = new Canvas();



int ileweszło = 0;
    @FXML
    Cell [][]cell;

    @FXML
    Button rand;
    @FXML
    TextField embryCount = new TextField();
    @FXML
    TextField length = new TextField();
    Timeline tl;

    private int SIZEwidth;
    private int SIZEheight;
    private int EMBRYCOUNT;
    private int neighborhoodType;
    private int randomType;
    private int boundaryType;
    Map<Color, Integer> m;
   // private  int pointsCount;
    private  int radiusSize;
    private boolean countWhite = false ;
    GraphicsContext graphicsContext ;
    private boolean stopClick =  false;
    List <Cell> randomedCells = new ArrayList();
    int points;
    List<Point> list;
    Runner runner;
    boolean    conuntofEmpty;
    public  void initialize(){

        conuntofEmpty = true;
        canvas.setWidth(750);
        canvas.setHeight(750);
        neighborhood.getItems().addAll(
               // "Moore",
                "Von Neumann"
//                "Heksagonal left",
//                "Heksagonal right",
//                "Random Heksagonal",
//                "Random Pentagonal"
               );

//        random.getItems().addAll(
//                "Random",
//                "Evenly random",
//                "In ray",
//                "Mouse click"
//
//        );
        boundary.getItems().addAll(
                "Periodical"
               // "Absorbing"
        );

    }

    public void setSize(){


        SIZEwidth = Integer.parseInt(width.getText());
        SIZEheight  = Integer.parseInt(height.getText());

        points= SIZEheight*SIZEwidth;
        if(SIZEwidth>1000 || SIZEheight>1000){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Powierzchnia jest zbyt duża");
            alert.setContentText("Maksymalna wartość to: 1000x1000");
            alert.showAndWait();
        }
          else {
            cell = new Cell[SIZEheight][SIZEwidth];
            for (int i = 0; i < SIZEheight; i++) {
                for (int j = 0; j < SIZEwidth; j++) {

                    cell[i][j] = new Cell();
                }
            }
        }
    }



    public void getNeeds(){

        String nbh=(String)neighborhood.getValue();
//        String rd=(String)random.getValue();
        String bd=(String)boundary.getValue();

        switch(nbh){
            case "Moore":
                neighborhoodType=1;
                break;
            case "Von Neumann":
                neighborhoodType=2;
                break;
            case "Heksagonal left":
                neighborhoodType=3;
                break;
            case "Heksagonal right":
                neighborhoodType=4;
                break;
            case "Random Heksagonal":
                neighborhoodType=5;
                break;case "spr":
                neighborhoodType=6;
                break;
        }

//        switch(rd){
//            case "Random":
//                randomType=1;
//                break;
//            case "Evenly random":
//                randomType=2;
//                break;
//            case "In ray":
//                randomType=3;
//                break;
//            case "Mouse click":
//                randomType=4;
//                break;
//        }

        switch(bd){
            case "Periodical":
                boundaryType=1;
                randomType=1;
                break;
            case "Absorbing":
                boundaryType=2;
                break;
        }


    }

    public void rand(){

        getNeeds();



        if(randomType==1){
            EMBRYCOUNT = Integer.parseInt(embryCount.getText());
            randoooom();

        }
        else if(randomType ==2){
            EMBRYCOUNT = Integer.parseInt(embryCount.getText());
            evenlyRandom();

        }
        else if (randomType==3){
            EMBRYCOUNT = Integer.parseInt(embryCount.getText());
           circleRandom();

        }
        else if (randomType==4){
            mouseClick();
        }

        print(canvas.getGraphicsContext2D());
    }

    public  void randoooom(){

        int x;
        int y;
        Random random = new Random();
        for(int i =0; i<EMBRYCOUNT; i++){
            x = random.nextInt(SIZEheight);
            y = random.nextInt(SIZEwidth);
            if(!cell[x][y].isState()){
                cell[x][y].setState(true);
                Color color =Color.color( random.nextDouble(), random.nextDouble(), random.nextDouble());
                System.out.println(" selected color " + color);
                cell[x][y].setColor(color);
                randomedCells.add(cell[x][y]);
                System.out.println(cell[x][y].getColor().toString());
            }
            else {
                i--;
            }


        }
    }
    public  void  circleRandom() {
        radiusSize = Integer.parseInt(radius.getText());

        boolean add;
        Random generator=new Random();

        for(int i = 0; i < EMBRYCOUNT; i++) {

            int x = 0,y = 0;
            try {
                 x = generator.nextInt(SIZEheight - 2 * radiusSize) + radiusSize;
                 y = generator.nextInt(SIZEwidth - 2 * radiusSize) + radiusSize;
            } catch (IllegalArgumentException e) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("WARNING");
                alert.setHeaderText("Zbyt duży promień");
                alert.setContentText("Zbyt duży promień na wylosowanie ziaren");
                alert.showAndWait();
            } finally {


                if (!cell[x][y].isState()) {


                    add = true;

                    for (int j = x - radiusSize; j < x + radiusSize; j++) {
                        for (int k = y - radiusSize; k < y + radiusSize; k++) {
                            if (cell[j][k].isState()) {
                                add = false;

                                break;
                            }
                            if (!add)
                                break;
                        }
                    }
                    if (!add) {
                        i--;

                    } else {
                        cell[x][y].setState(true);
                        cell[x][y].setColor(Color.color(Math.random(), Math.random(), Math.random()));
                    }
                }
            }
        }

//       radiusSize = Integer.parseInt(radius.getText());
//        int x;
//        int y;
//        Random random = new Random();
//        int r =0;
//        boolean ok  = false;
//       while(r < EMBRYCOUNT) {
//           x = random.nextInt(SIZE);
//           y = random.nextInt(SIZE);
//
//         if(!ifInCircle(x,y)) {
//              cell[x][y].setState(true);
//              cell[x][y].setColor(Color.color( Math.random(), Math.random(), Math.random()));
//             randomedCells.add(cell[x][y]);
//              r++;
//          }
//           for (int i = 0; i < SIZE; i++) {
//               for (int j = 0; j < SIZE; j++) {
//
//
//                 if(cell[i][j].isState()){
//                     int distance = (int) Math.sqrt(Math.pow((i - x), 2) + Math.pow((j - y), 2));
//                     if (distance > radiusSize) {
//                         ok = true;
//                     }
//                 }
//                 else
//                     ok = true;
//
//
//               }
//
//           }
//           if(ok){
//               cell[x][y].setState(true);
//               cell[x][y].setColor(Color.color( Math.random(), Math.random(), Math.random()));
//               randomedCells.add(cell[x][y]);
//               r++;
//           }
//       }

    }
    public boolean ifInCircle(int x, int y){

        for (int i = 0; i < SIZEheight; i++) {
            for (int j = 0; j < SIZEwidth; j++) {
                if (cell[i][j].isState()){
                    int distance = (int) Math.sqrt(Math.pow((i - x), 2) + Math.pow((j - y), 2));
                    if(distance  < 2){
                        if (cell[i][j].isState()){
                            return  true;
                        }
                    }
                    }
            }

            }
            return  false;
    }

    public  void evenlyRandom(){

////
//        System.out.println("SIZE " + SIZEheight);
//        Random random = new Random();
//        double nx = Math.sqrt( (SIZEwidth/SIZEheight) * EMBRYCOUNT + ((SIZEwidth - SIZEheight ) * (SIZEwidth - SIZEheight)) / (4*SIZEheight * SIZEheight)) - (SIZEwidth - SIZEheight) / (2*SIZEheight);
//        double ny = SIZEheight/SIZEwidth*nx + 1 -SIZEheight/SIZEwidth;
//
//        double dx =SIZEwidth/nx;
//        double dy =SIZEheight/ny;
//
//        while (EMBRYCOUNT > 0)
//        {
//            for (int i = 0; (i< SIZEheight && EMBRYCOUNT > 0); i+=dx )
//            {
//                for (int j = 0; (j< SIZEwidth && EMBRYCOUNT > 0); j+=dy ){
//                    cell[i][j].setState(true);
//                    cell[i][j].setColor(Color.color( Math.random(), Math.random(), Math.random()));
//                    randomedCells.add(cell[i][j]);
//                    EMBRYCOUNT--;
//                }
//            }
   //     }



        int sqrt=(int) Math.sqrt(EMBRYCOUNT);
        int borderx = SIZEheight/sqrt-1;
        int bordery = SIZEwidth/sqrt-1;
        int counter=0;

        for(int i=0;i<=sqrt;i++)
        {
            for(int j=0;j<=sqrt && counter < EMBRYCOUNT;j++)
            {
                if(!cell[i*borderx+1][j*bordery+1].isState()){
                cell[i*borderx+1][j*bordery+1].setState(true);
                cell[i*borderx+1][j*bordery+1].setColor(Color.color( Math.random(), Math.random(), Math.random()));

                counter++;
            }}
        }

    }

    @FXML
    public  void mouseClicked(double x, double y){


        double width = canvas.getGraphicsContext2D().getCanvas().getWidth() / SIZEwidth;
        double height = canvas.getGraphicsContext2D().getCanvas().getHeight() / SIZEheight;
        x = (int) ((x / width) *width);
        y = (int) ((y / height) *height);

        int canvasX = (int) (x/height);
        int canvasY = (int) (y/width);

        if(canvasX > SIZEheight -1 )
            canvasX = SIZEwidth -1;
        if (canvasX <0)
            canvasX = 0;

        if(canvasY > SIZEheight -1 )
            canvasY = SIZEheight -1;
        if (canvasY <0)
            canvasY = 0;

        int Xend = canvasX;
        int Yend = canvasY;

        System.out.println( "x,  y " + Xend + " " + Yend +  " " + cell[Xend][Yend].isState() + " " +cell[Xend][Yend].getColor());
        Platform.runLater( () ->{
            if(!cell[Xend][Yend].isState()){
                cell[Xend][Yend].setState(true);
                cell[Xend][Yend].setColor(Color.color( Math.random(), Math.random(), Math.random()));
                System.out.println( "x2,  y2 " + Xend + " " + Yend + " " + cell[Xend][Yend].isState() + " " +cell[Xend][Yend].getColor());

                randomedCells.add(cell[Xend][Yend]);


               // canvas.getGraphicsContext2D().setFill(cell[Xend][Yend].getColor());
               // canvas.getGraphicsContext2D().fillRect(width * Xend + 1, width * Yend + 1, width - 1, width - 1);
                print(canvas.getGraphicsContext2D());

                canvas.setOnMouseDragged(null);
                canvas.setOnMouseClicked(null);
            }

       });
    }


    public void mouseClick() {
        canvas.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                mouseClicked(x, y);
              //  canvas.setOnMouseClicked(null);
               canvas.setOnMouseDragged(null);

            }



        });
       // canvas.setOnMouseDragged(null);
    }

//    public static void start(){
//
//
//
//        List<Point> points =vonNeuman.getNeighborhood();
//
//        for(int y = 0; y < size; y++){
//            for(int x = 0; x < size; x++) {
//                String c;
//                TextField tf = new TextField();
//                c = "-fx-background-color: " + cells[x][y].getColor().toString();
//                tf.setStyle(c);
//                GridPane.setRowIndex(tf,y);
//                GridPane.setColumnIndex(tf,x);
//                root.getChildren().add(tf);
//            }
//
//        }
//
//    }

    @FXML
    public void startButton() throws InterruptedException {

        stopClick = false;

        runner =  new Runner(this);
        runner.setStop(false);
        Thread thread =  new Thread(runner);
        thread.setDaemon(true);
        thread.start();


//
//        if(neighborhoodType ==1)
//            nb = new Moore();
//        if(neighborhoodType ==2)
//            nb = new vonNeuman();
//        if(neighborhoodType ==3)
//            nb = new HexagonalLeft();
//        if(neighborhoodType ==4)
//            nb = new HexagonalRight();
//            Thread t = new Thread(this);
//            t.start();



//        for(int i = 0 ; i<100; i++) {
//            Task<Void> task = new Task<Void>() {
//                // Implement required call() method
//                @Override
//                protected Void call() throws Exception {
//                    // Add delay code from initial attempt
//                    try {
//                        //Thread.sleep(100);
//                    } catch (Exception e) {
//                    }
//
//                    Platform.runLater(() ->{
//                                nextGeneration();
//                        print(canvas.getGraphicsContext2D());
//                            }
//
//
//                    );
//
//
//
//                    // We're not interested in the return value, so return null
//                    return null;
//                }
//            };
//// Run task in new thread
//            new Thread(task).start();
//
//        }



//        Thread thread = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Runnable updater = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        nextGeneration();
//                        print(canvas.getGraphicsContext2D());
//                    }
//                };
//
//                while (true) {
//                    try {
//                        Thread.sleep(700);
//                    } catch (InterruptedException ex) {
//                    }
//
//                    // UI update is run on the Application thread
//                    Platform.runLater(updater);
//                }
//            }
//
//        });
        // don't let thread prevent JVM shutdown
//        thread.setDaemon(true);
//        thread.start();
//
        // oryginalne
//        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), e -> {
//            nextGeneration();
//            print(canvas.getGraphicsContext2D());
//
//        });
//        tl = new Timeline(kf);
//        tl.setCycleCount(500);
//
//            tl.play();


        // koniec

//for(int i = 0 ; i<100; i++){
//
//    Platform.runLater(new Runnable() {
//        public void run() {
//
//            nextGeneration();
//            print(canvas.getGraphicsContext2D());
//        }
//    });
//
//
//}


// tooo dziala w miare ok
//
//        points = 1;
//        stopClick = false;
//        Task task = new Task<Void>() {
//            @Override public Void call() {
//
//                 final int max = 300;
//                while ((!stopClick) ) {
//                  //  if(countWhite==false){
//                        nextGeneration();
//
//                        print(canvas.getGraphicsContext2D());
//                    points = SIZEheight*SIZEwidth;
//                  //  }
//
//                }
//
//
//                return null;
//            }
//        };
//
//       Thread t= new Thread(task);
//               t.start();
//



//
//        var t = Thread(Runnable {
//            print(canvas.getGraphicsContext2D());
//        }).start()
//        Thread(Runnable {
//            run {
//                val timeSpent = measureNanoTime {
//                    sort(data)
//                }
//                time.text = "Time spent: ${timeSpent/1_000_000.0}ms"
//                doRun = false
//            }
//        }).start()
//
//        if  (stopClick || countWhite )
//            t.stop();
//
//
//        Task<Long> task = new Task<Long>() {
//            @Override protected Long call() throws Exception {
//                long a=0;
//                long b=1;
//                for (long i = 0; i < Long.MAX_VALUE; i++){
//                    final long v = a;
//                    Platform.runLater(new Runnable() {
//                                          @Override public void run() {
//                                           //   updateValue(v);
//                                          }
//                                      }
//                            a += b;
//                    b = a - b;
//                }
//                return a;
//            }
//        };
//
//
//        stopClick = false;
//        Thread t;
//        t = new Thread(() -> {
//
//            while (!stopClick) {
//
//
//
//
//                    nextGeneration();
//                //visTask();
//                //  print(canvas.getGraphicsContext2D());
//
//
//                try {
//                    Thread.sleep(20);
//
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//
//                }
//
//
//            }
//
//        });
//
//        t.start();
//
//        if(stopClick)
//            t.stop();


    }

//    public void run() {
//        while (!stopClick) {
//            nextGeneration();
//            print(canvas.getGraphicsContext2D());
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    void print(GraphicsContext gc) {
        double width = canvas.getGraphicsContext2D().getCanvas().getWidth() / SIZEwidth;
        double height = canvas.getGraphicsContext2D().getCanvas().getHeight() / SIZEheight;

        for (int i = 0; i < SIZEheight; i++) {
            for (int j=0; j<SIZEwidth; j++){
                if (cell[i][j].isState()){


                    gc.setFill(cell[i][j].getColor());
             //    gc.fillRect(height * i + 1, width * j + 1, height - 1, width - 1);
                  // gc.fillRect(height * i , width * j , height , width );

                   // System.out.println("width " + width);
              //  gc.fillRect(i, j, 1, 1);
               gc.fillRect(i*height, j*width, height, width);
                   // gc.setFill(cell[i][j].getColor());
                    //System.out.println(cell[i][j].getColor());
                }

            }
        }
    }
    Map.Entry<Color, Integer> maxEntry;



    public void nextGeneration() {
        Cell[][] cells = new Cell[SIZEheight][SIZEwidth];
        for (int i = 0; i < SIZEheight; i++) {
            for (int j = 0; j < SIZEwidth; j++) {
                cells[i][j] =  new Cell();
                cells[i][j].setState(cell[i][j].isState());
                cells[i][j].setColor(cell[i][j].getColor());

            }
        }

        for (int i = 0; i < SIZEheight; i++) {
            for (int j = 0; j < SIZEwidth; j++) {

//                boolean check = false;
//                for (Cell c :randomedCells ) {
//                    if(cells[i][j].equals(c) && cells[i][j].ifNb  ) {
//                        check = true; break;
//                    }
//                }
                if (!cells[i][j].isState() ) {

                    if (boundaryType == 1) {
                        list = Nb.getNeighborhood(i, j, SIZEwidth,SIZEheight, 1, neighborhoodType);
                    } else if (boundaryType == 2) {
                        list = Nb.getNeighborhood(i, j, SIZEwidth,SIZEheight, 2, neighborhoodType);
                    }


//                    if(neighborhoodType ==5){
//
//                      //  randHeksaType();
//                        int x =rand.nextInt(2);
//                        if(x==1)
//                            nb = new HexagonalRight();
//                        else
//                            nb = new HexagonalLeft();
//                    }

//                   if(neighborhoodType ==6){
//
//                       int x =rand.nextInt(5);
//                       if(x==0)
//                         nb = new PentagonaLRight();
//                       else if(x==1)
//                           nb = new PentagonalLeft();
//                        if(x==2)
//                            nb = new PentagonalUp();
//                        if(x==3)
//                            nb = new PentagonalDown();
//                  }




//                         for (Point p: list ) {
//                         if(!cells[p.getX()][p.getY()].isState()){
//                             cell[p.getX()][p.getY()].setState(true);
//                             System.out.println(cells[i][j].getColor().toString());
//                             cell[p.getX()][p.getY()].setColor(cells[i][j].getColor());
//                             System.out.println(cells[p.getX()][p.getY()].getColor().toString());
//                         }
//
//                         }

                     m = new HashMap();
                    Color c;
                    int count = 1;
                    if (!cells[i][j].isState()) {
                        for (Point p : list) {
                            if (cells[p.getX()][p.getY()].isState() && !cells[p.getX()][p.getY()].getColor().equals(Color.WHITE)) {
                                if (m.containsKey(cells[p.getX()][p.getY()].getColor())){
                                    m.put(cells[p.getX()][p.getY()].getColor(), count + 1);
                                    cells[p.getX()][p.getY()].setIfNb(true);
                                }

                                else {
                                    m.put(cells[p.getX()][p.getY()].getColor(), count);
                                    cells[p.getX()][p.getY()].setIfNb(true);
                                }
                            }

                        }
                    }

                    if (m.size() > 0) {

                       maxEntry =null;

                        for (Map.Entry<Color, Integer> entry : m.entrySet()) {
                            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                                maxEntry = entry;
                            }
                        }
                        c = maxEntry.getKey();
                        cell[i][j].setColor(c);
                        cell[i][j].setState(true);

                    }
                }


            }
        }

//        for (int i = 0; i < SIZE; i++) {
//
//            for (int j = 0; j < SIZE; j++) {
//               cell[i][j].setState(cell[i][j].isState());
//               cell[i][j].setColor(cell[i][j].getColor());
//               if(!cell[i][j].isState())
//                   countWhite ++;
//
//            }
//        }

       // if(!stopClick)
      //  print(canvas.getGraphicsContext2D());

    }

    private void randHeksaType() {


    }


    public  void  clear(){

        for (int i = 0; i < SIZEheight; i++) {

            for (int j = 0; j < SIZEwidth; j++) {
                cell[i][j].setState(false);
                cell[i][j].setColor(Color.WHITE);

            }
        }
        printClear();
    }

    public void printClear(){
//        double width = canvas.getGraphicsContext2D().getCanvas().getWidth() / SIZEwidth;
//        double height = canvas.getGraphicsContext2D().getCanvas().getHeight() / SIZEheight;
//
//        for (int i = 0; i < SIZEheight; i++) {
//            for (int j=0; j<SIZEwidth; j++){
//
//
//                canvas.getGraphicsContext2D().setFill(Color.WHITE);
//                    canvas.getGraphicsContext2D().fillRect(height * i , width * j , height , width );
//                    // gc.fillRect(i, j, 1, 1);
//
//
//            }
//        }
        canvas.getGraphicsContext2D().clearRect(0,0,750, 750);

    }

    public void evenlyRandomm(javafx.scene.input.MouseEvent mouseEvent) {
    }

    public void stop(ActionEvent actionEvent) {

        runner.setStop(true);
        stopClick =true;

    }

    public void finalRand(ActionEvent actionEvent) {

       getNeeds();
        EMBRYCOUNT = Integer.parseInt(embryCount.getText());
        randoooom();
        print(canvas.getGraphicsContext2D());
    }


//
//    @Override
//    public void run() {
//        stopClick=false;
//        while (!stopClick){
//
//        }
//        nextGeneration();
//        print(canvas.getGraphicsContext2D());
//    }
//    public void rePrint(){
//
//        for (int i = 0; i < SIZE; i++) {
//
//            for (int j = 0; j < SIZE; j++) {
//
//                if(cell[i][j].isState()){
////                    cell[i][j].setColor(cell[i][j].getColor());
//                  cell[i][j].setColor(Color.color(0.4,0.1, 1));
//                }
//
//            }
//            }
//    }

}
