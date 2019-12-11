package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarlo {
    private Random random;
    private float coefficient;
    private int index;
    private int steps;
    List<Cell> grains;

    public MonteCarlo(int numberOfGrains, float coeff, int steps) {
        random = new Random();
        grains = new ArrayList<>();
        coefficient = coeff;
        this.steps = steps;
        index = Nucleons.getNumberOfGrains() - numberOfGrains + 2;
        for (Cell c : Nucleons.getGrid().getGrid()) {
            if(c.getState() == 0) {
                c.setState(random.nextInt(Nucleons.getNumberOfGrains() + 2 - index) + index);
                grains.add(c);
            }
        }
    }

    public void growGrains(String neighbourhood) {
        growGrainsMonteCarlo(neighbourhood);
        for (int i = 0; i < steps; i++) {
            MCIteration();
        }
    }

    public void growGrainsMonteCarlo(String nb) {
        List<Cell> grid = Nucleons.getGrid().getGrid();

        for (Cell c: grid)
        {
            if(nb =="VonNeumann")
                c.setNeighbourhood(new VonNeumann(c));
            else
                c.setNeighbourhood(new Moore(c));
        }
        boolean isEmpty = true;
        while (isEmpty)
        {
            isEmpty = false;
            List<Cell> temp = new ArrayList<>();

            for (Cell c : grid)
            {
                if (c.getState() == 0) {
                    isEmpty = true;

                    checkNeighbourhood(c);
                    temp.add(c);
                }
            }
            for (Cell c : temp)
            {
                c.setState(c.getNextState());
            }
        }
    }

    protected void MCIteration() {
        List<Cell> grains = new ArrayList<>(this.grains);
        int size = grains.size();
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(grains.size());
            checkNeighbourhood(grains.get(index));
            grains.remove(index);
        }
    }

    protected boolean checkNeighbourhood(Cell c) {
        List<Cell> neighbours = c.getNeighbourhood().getNeighbours();
        int delta = 0;
        for (Cell n : neighbours) {
            if (n.getState() != c.getState()) {
                delta++;
            }
        }
        int possibleState = random.nextInt(Nucleons.getNumberOfGrains() + 2 - index) + index;
        int randomDelta = 0;
        for (Cell n : neighbours) {
            if (n.getState() != possibleState) {
                randomDelta++;
            }
        }
        if (randomDelta <= delta) {
            c.setState(possibleState);
        }
        return true;
    }
}
