package sample;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Inclusion {


    int counter  =1;
    public void addInclusion(int inclusionNumber, int size, String inclusionType) {

        Nucleons.setNumberOfInclusions(inclusionNumber);
        Set<Integer> randomCells = new HashSet<>();

        Random generator = new Random();

        for (int i = 0; i < inclusionNumber; i++) {
            if (Nucleons.checkIfAnyEmptySpaces()) {
                int random = generator.nextInt(Nucleons.getGrid().getGrid().size());
                if (randomCells.contains(random)) {
                    i--;
                } else
                    randomCells.add(random);

                if (inclusionType == "Circle") {
                    addCircle(random, size);
                } else {
                    addSquare(random, size);
                }
            }
            }
            for (Cell c : Nucleons.getGrid().getGrid()) {
                if (c.getNextState() == 1)
                    c.setState(1);
            }

    }

    public void addInclusionOnBoundaries(int inclusionNumber, int size, String inclusionType) {

        Nucleons.setNumberOfInclusions(inclusionNumber);

        Set<Integer> set = new HashSet<>();
        Random generator = new Random();

        for (int i = 0; i < inclusionNumber; i++) {
            boolean search = true;
            int random;

            while (search) {
                random = generator.nextInt(Nucleons.getGrid().getGrid().size());
                Cell cell = Nucleons.getGrid().getCellByPosition(random);

                for (int j = cell.getX() - 1; j < cell.getX() + 1; j++) {
                    for (int k = cell.getY() - 1; k < cell.getY() + 1; k++) {
                        try {
                            int state = Nucleons.getGrid().getCell(j, k).getState();
                            if (state != 1 && state != cell.getState()) {

                                if(set.contains(random)){
                                    i--;
                                }
                                else{
                                    set.add(random);
                                    search = false;
                                    if(inclusionType == "Circle")
                                    addCircle(random, size);
                                    else
                                        addSquare(random, size);
                                    break;
                                }
                            }
                        }
                        catch (NullPointerException e) {
                            continue;
                        }

                    }
                    if (!search)
                        break;
                }
            }
        }
        for (Cell c: Nucleons.getGrid().getGrid()) {
            if (c.getNextState() == 1)
                c.setState(1);
        }
    }


     void addSquare(int grain, int size) {

        Grid grid = Nucleons.getGrid();
        Cell grainRoot = grid.getCellByPosition(grain);

        int x = grainRoot.getX();
        int y = grainRoot.getY();
        int fromMiddle = size/2;

        if (size % 2 == 0)
        {
            fromMiddle--;
        }

        for (int i = x - fromMiddle; i <= x + size/2; i++) {
            for (int j = y - size/2; j <= y + fromMiddle; j++) {
                Nucleons.getGrid().getCell(i, j).setNextState(1);
            }

        }

    }
    protected void addCircle(int gr, int size) {


        Grid grid = Nucleons.getGrid();
        Cell grain = grid.getCellByPosition(gr);
        int x = grain.getX();
        int y = grain.getY();

        setCircle(x, y, size, counter);
        fillCircle(x, y, size, counter);
        counter ++;


    }

    void fillCircle(int posX, int posY, int r, int counter) {
        Nucleons.getGrid().getCell(posX, posY).setNextState(1);

        Cell cc =  Nucleons.getGrid().getCell(posX, posY);
        for (int i = posY - r + 1; i < posY + r; i++) {
            for (int j = posX; j > (posX - r) ; j--) {
                if (i == posY && j == posX)
                    continue;
                try {
                    Cell c = Nucleons.getGrid().getCell(j, i);

                    if (c.getNextState() == 1 && c.getId() ==counter && c.getId()!=cc.getId()) {
                       break ;
                    }
                    else
                    c.setState(1);
                    c.setNextState(1);

                }
                catch (NullPointerException e) {
                    break;
                }
            }
            for (int j = posX; j < (posX + r) ; j++) {
                if (i == posY && j == posX)
                    continue;
                try {
                    Cell c = Nucleons.getGrid().getCell(j, i);
                    if (c.getNextState() == 1 && c.getId() == counter ) {
                        break;
                    }else
                    c.setState(1);
                    c.setNextState(1);


                }
                catch (NullPointerException e) {
                    continue;

                }
            }

        }
    }


    void setCircle(int posX, int posY, int r, int counter)
    {
        int x = 0, y = r;
        int d = 3 - 2 * r;
        setCircleState(posX, posY, x, y, counter);
        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else
                d = d + 4 * x + 6;
            setCircleState(posX, posY, x, y, counter);
        }
    }

    void setCircleState(int posX, int posY, int x, int y, int counter)
    {
        setPxelState(posX + x, posY + y, 1);
        setPxelState(posX - x, posY + y, 1);
        setPxelState(posX + x, posY - y, 1);
        setPxelState(posX - x, posY - y, 1);
        setPxelState(posX + y, posY + x, 1);
        setPxelState(posX - y, posY + x, 1);
        setPxelState(posX + y, posY - x, 1);
        setPxelState(posX - y, posY - x, 1);

        setPxelId(posX + x, posY + y, counter);
        setPxelId(posX - x, posY + y, counter);
        setPxelId(posX + x, posY - y, counter);
        setPxelId(posX - x, posY - y, counter);
        setPxelId(posX + y, posY + x, counter);
        setPxelId(posX - y, posY + x, counter);
        setPxelId(posX + y, posY - x, counter);
        setPxelId(posX - y, posY - x, counter);

    }

    private void setPxelState(int x, int y, int state){

            Nucleons.getGrid().getCell(x, y).setNextState(state);
    }
    private void setPxelId(int x, int y, int couner){

        Nucleons.getGrid().getCell(x, y).setId(couner);
    }


}
