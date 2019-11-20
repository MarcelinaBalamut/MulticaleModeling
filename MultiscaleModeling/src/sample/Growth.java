package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public   class Growth {

    private Moore moore;
    private VonNeumann nearestMoore;
    private FurtherMoore furtherMoore;
    private int probability;
    private Random generator;

    void growGrains(String nb, boolean shapeControl) {

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
                if (c.getState() == 0)
                {
                    isEmpty = true;
                    if(shapeControl && nb=="Moore")
                        checkNeighbourhoodMoore(c);
                    else
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

    private void checkNeighbourhood(Cell c) {

        List<Cell> neighbours = c.getNeighbourhood().getNeighbours();
        HashMap<Integer, Integer> states = new HashMap<>();

        for (Cell n : neighbours)
        {
            if (n.getState() > 1 + Nucleons.getNumberOfStructures())
                states.merge(n.getState(), 1, Integer::sum);
        }

        if(!states.isEmpty())
        {
            HashMap.Entry<Integer, Integer> maxEntry = null;
            for (HashMap.Entry<Integer, Integer> entry : states.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue())
                    maxEntry = entry;
            }
            c.setNextState(maxEntry.getKey());
        }
    }



    public void shapeControlGrowth() {
        this.generator = new Random();
    }



    public void growGrainsMoore(int probability) {
        this.probability = probability;
        growGrains("Moore", true);
    }


    public void checkNeighbourhoodMoore(Cell c) {
        shapeControlNeighbourhood(c);
        HashMap<Integer, Integer> neighbourhoodStates  = new HashMap<>();

        for (Cell n : getMoore().getNeighbours())
        {
            if (n.getState() > 1 + Nucleons.getNumberOfStructures())
                neighbourhoodStates.merge(n.getState(), 1, Integer::sum);
        }
        if (!neighbourhoodStates.isEmpty())
        {

            HashMap.Entry<Integer, Integer> maxForMoore = null;
            for (HashMap.Entry<Integer, Integer> entry : neighbourhoodStates.entrySet())
            {
                if (maxForMoore == null || entry.getValue() > maxForMoore.getValue())
                    maxForMoore = entry;
            }
            if (maxForMoore.getValue() >= 5)
            {
                c.setNextState(maxForMoore.getKey());
            }
            else {

                HashMap.Entry<Integer, Integer> maxMoore = null;
                neighbourhoodStates.clear();
                for (Cell n : getNearestMoore().getNeighbours())
                {
                    if (n.getState() > 1 + Nucleons.getNumberOfStructures())
                        neighbourhoodStates.merge(n.getState(), 1, Integer::sum);
                }

                for (HashMap.Entry<Integer, Integer> entry : neighbourhoodStates.entrySet())
                {
                    if (maxMoore == null || entry.getValue() > maxMoore.getValue())
                        maxMoore = entry;
                }
                if (maxMoore != null && maxMoore.getValue() >= 3)
                {
                    c.setNextState(maxMoore.getKey());
                }
                else
                    {

                    neighbourhoodStates.clear();
                    maxMoore = null;
                    for (Cell n : getFurtherMoore().getNeighbours())
                    {

                        if (n.getState() > 1 + Nucleons.getNumberOfStructures())
                            neighbourhoodStates.merge(n.getState(), 1, Integer::sum);
                    }

                    for (HashMap.Entry<Integer, Integer> entry : neighbourhoodStates.entrySet())
                    {
                        if (maxMoore == null || entry.getValue() > maxMoore.getValue())
                            maxMoore = entry;
                    }
                    if (maxMoore != null && maxMoore.getValue() >= 3)
                    {
                        c.setNextState(maxMoore.getKey());
                    }
                    else
                    {

                        int random = generator.nextInt(100) + 1;
                        if (random <= probability) {
                            c.setNextState(maxForMoore.getKey());
                        }
                    }
                }
            }
        }
    }
    public void shapeControlNeighbourhood(Cell cell) {
        moore = new Moore(cell);
        nearestMoore = new VonNeumann(cell);
        furtherMoore = new FurtherMoore(cell);
    }

    public FurtherMoore getFurtherMoore() {
        return furtherMoore;
    }

    public Moore getMoore() {
        return moore;
    }

    public VonNeumann getNearestMoore() {
        return nearestMoore;
    }
}
