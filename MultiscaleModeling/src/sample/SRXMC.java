package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SRXMC {
    public static final String CONSTANT = "Constant";
    public static final String INCREASING = "Increasing";
    public static final String BEGINNING = "At the beginning";
    public static final String ANYWHERE = "Anywhere";
    public static final String ON_BOUNDARIES = "On boundaries";

    private float coefficient;
    private int steps;
    private List<Cell> grains;
    private Random random;
    private Nucleation nucleation;

    public SRXMC(String type, String location, int numberOfGrains, float coeff, int st) {
        grains = new ArrayList<>(Nucleons.getGrid().getGrid());
        coefficient = coeff;
        this.steps = st;
        if (location.equals(ON_BOUNDARIES)) {
            List<Cell> borders = findBorderCells();
            nucleation = new Nucleation(type, borders, numberOfGrains);
        }
        else {
            nucleation = new Nucleation(type, Nucleons.getGrid().getGrid(), numberOfGrains);

        }
    }

    public void growGrains(String neighbourhood) {
        growGrainsMonteCarlo(neighbourhood);
        for (int i = 0; i < steps; i++) {
            if (nucleation.shouldPerformNucleation()) {
                List<Cell> nucleons = nucleation.nucleate();
                grains.removeAll(nucleons);
            }
            MCstep();
        }
    }

    protected void MCstep() {
        random = new Random();
        List<Cell> grainsPool = new ArrayList<>(this.grains);
        for (int i = 0; i < grainsPool.size(); i++) {
            int randomIndex = random.nextInt(grainsPool.size());
            checkNeighbourhood(grainsPool.get(randomIndex));
            grainsPool.remove(randomIndex);
        }
    }


    private boolean checkNeighbourhood(Cell c) {
        List<Cell> neighbours = c.getNeighbourhood().getNeighbours();

        if (neighbours.stream().anyMatch(Cell::isRecrystallized)) {
            Cell recrystalizedNeighbour;
            while (true) {
                recrystalizedNeighbour = neighbours.get(random.nextInt(neighbours.size()));
                if (recrystalizedNeighbour.isRecrystallized())
                    break;
            }

            int deltaBefore = 0;
            int deltaAfter = 0;
            int stateCandidate = recrystalizedNeighbour.getState();
            for (Cell n : neighbours) {
                if (n.getState() != c.getState()) {
                    deltaBefore++;
                }
                if (n.getState() != stateCandidate) {
                    deltaAfter++;
                }
            }
            float energyBefore = coefficient * deltaBefore + c.getEnergy();
            float energyAfter = coefficient * deltaAfter;
            if (energyAfter <= energyBefore) {
                c.setState(stateCandidate);
                c.setEnergy(energyAfter);
                c.setRecrystallized(true);
                grains.remove(c);
            }
            return true;
        }
        return false;
    }

    private static List<Cell> findBorderCells() {
        List<Cell> borderCells = new ArrayList<>();
        for (Cell cell : Nucleons.getGrid().getGrid()) {
            if (cell.getState() == 1 || cell.getState() == 0)
                continue;
            Neighbourhood neighbourhood = new Moore(cell);
            for (Cell n : neighbourhood.getNeighbours() ) {
                if ( n.getState() != cell.getState()) {
                    borderCells.add(n);
                }
            }
        }
        return borderCells;
    }

    public static void distributeEnergy(float inside, float border, int threshold) {
        List<Cell> cells = Nucleons.getGrid().getGrid();
        Random random = new Random();
        float insideDown = inside * (100 - threshold) / 100;
        float insideUp = inside * (100 + threshold) / 100;

        if (inside == border) {
            for (Cell c : cells) {
                c.setEnergy(insideDown + random.nextFloat() * (insideUp - insideDown));
            }
        }
        else {
            List<Cell> borders = findBorderCells();
            float borderDown = border * (100 - threshold) / 100;
            float borderUp = border * (100 + threshold) / 100;
            for (Cell c : cells) {
                if (borders.contains(c)) {
                    c.setEnergy(borderDown + random.nextFloat() * (borderUp - borderDown));
                }
                else {
                    c.setEnergy(insideDown + random.nextFloat() * (insideUp - insideDown));
                }
            }
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
    }
}
