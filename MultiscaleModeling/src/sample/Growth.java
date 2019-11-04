package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public   class Growth {

    void growGrains() {

        List<Cell> grid = Nucleons.getGrid().getGrid();
        for (Cell c: grid)
        {
            c.setNeighbourhood(new VonNeumann(c));
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
        HashMap<Integer, Integer> nbStates = new HashMap<>();

        for (Cell n : neighbours)
        {
            if (n.getState() != 0 && n.getState() != 1)
                nbStates.merge(n.getState(), 1, Integer::sum);
        }

        if(!nbStates.isEmpty())
        {
            HashMap.Entry<Integer, Integer> maxEntry = null;
            for (HashMap.Entry<Integer, Integer> entry : nbStates.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue())
                    maxEntry = entry;
            }
            c.setNextState(maxEntry.getKey());
            //c.setId(0);
        }
    }
}
