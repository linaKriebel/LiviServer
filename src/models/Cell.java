package models;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    public Field position;

    public Boolean visited;
    public Boolean dummy;
    public Boolean northWall;
    public Boolean southWall;
    public Boolean westWall;
    public Boolean eastWall;

    public Cell northNeighbour; //0
    public Cell southNeighbour; //1
    public Cell westNeighbour;  //2
    public Cell eastNeighbour;  //3

    public Cell(Field position) {
        this.position = position;

        visited = false;
        dummy = false;
        northWall = true;
        southWall = true;
        westWall = true;
        eastWall = true;
    }

    public Cell() {
        position = null;

        visited = false;
        dummy = false;
        northWall = true;
        southWall = true;
        westWall = true;
        eastWall = true;
    }

    public List<Cell> GetNeighbours() {
        List<Cell> neighbours = new ArrayList<>();
        neighbours.add(northNeighbour);
        neighbours.add(southNeighbour);
        neighbours.add(westNeighbour);
        neighbours.add(eastNeighbour);

        return neighbours;
    }

    public Cell GetNeighbour(Neighbour neighbour) {
        switch (neighbour) {
            case NORTH:
                return northNeighbour;
            case SOUTH:
                return southNeighbour;
            case WEST:
                return westNeighbour;
            case EAST:
                return eastNeighbour;
            default:
                return null;
        }
    }
}