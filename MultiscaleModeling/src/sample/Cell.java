package sample;

public class Cell {
    private int x;
    private int y;
    private int state;
    private int nextState;
    private Neighbourhood neighbourhood;
    private int id;

    private float energy;
    private boolean recrystallized;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setNeighbourhood(Neighbourhood neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Neighbourhood getNeighbourhood() {
        return neighbourhood;
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public boolean isRecrystallized() {
        return recrystallized;
    }

    public void setRecrystallized(boolean recrystallized) {
        this.recrystallized = recrystallized;
    }
}
