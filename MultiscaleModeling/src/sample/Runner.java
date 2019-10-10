package sample;

public class Runner implements Runnable {
    private Controller controller;
    private boolean stop = false;

    public Runner(Controller controller){
        this.controller = controller;
        }

    @Override
    public void run() {
        while(!controller.conuntofEmpty){
            controller.nextGeneration();
        }
        controller.print(controller.canvas.getGraphicsContext2D());
    }
    public void setStop(boolean stop){
        this.stop= stop;
    }
}
