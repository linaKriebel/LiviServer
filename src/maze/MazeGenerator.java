package maze;

import models.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator
{
    public Cell[][] board;

    private static Random random = new Random();

    private int width;
    private int height;

    public MazeGenerator(int width, int height)
    {
        this.width = width;
        this.height = height;

        board = new Cell[width][height];

        //initialise the board with cells
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                board[x][y] = new Cell(new Field(x,y));
            }
        }

        //set the neighbours of each cell of the board
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                Cell cell = board[x][y];

                if (y != 0) cell.northNeighbour = board[x][y-1];
                if (y != height-1) cell.southNeighbour = board[x][y+1];
                if (x != 0) cell.westNeighbour = board[x - 1][y];
                if (x != width-1) cell.eastNeighbour = board[x + 1][y];
            }
        }
    }

    public Cell[][] carve()
{
    Cell dummy = new Cell();
    dummy.dummy = true;
    dummy.visited = true;

    //always start with 0,0
    int x = 0;
    int y = 0;

    List<Cell> unvistedCells = GetUnvisitedCells();
    Stack<Cell> stack = new Stack<Cell>();

    Cell currentCell = board[x][y];


    //1 While there are unvisited cells
    while (unvistedCells.size() > 0)
    {
        //2 Make the initial cell the current cell and mark it as visited
        currentCell.visited = true;

        //get the neighbours of the current cell
        List<Cell> neighbours = currentCell.GetNeighbours();
        List<Cell> unvisitedNeighbours = new ArrayList<>();

        //add unvisited neighbours to the list
        for (Cell neighbour : neighbours)
        {
            if (neighbour != null && neighbour.visited == false)
                unvisitedNeighbours.add(neighbour);
        }

        //3 If the current cell has any neighbours which have not been visited
        if (unvisitedNeighbours.size() > 0)
        {
            //4 Choose randomly one of the unvisited neighbours
            int randomNeighbourIndex = random.nextInt(unvisitedNeighbours.size());
            Cell chosenCell = unvisitedNeighbours.get(randomNeighbourIndex);

            //5 Push the current cell to the stack
            stack.push(currentCell);

            //find out which neighbour has been chosen
            Neighbour neighbour = GetNeighbour(currentCell, chosenCell);

            //6 Remove the wall between the current cell and the chosen cell
            if(neighbour == Neighbour.NORTH)
            {
                currentCell.northWall = false;
                chosenCell.southWall = false;
            }
            if(neighbour == Neighbour.SOUTH)
            {
                currentCell.southWall = false;
                chosenCell.northWall = false;
            }
            if(neighbour == Neighbour.WEST)
            {
                currentCell.westWall = false;
                chosenCell.eastWall = false;
            }
            if(neighbour == Neighbour.EAST)
            {
                currentCell.eastWall = false;
                chosenCell.westWall = false;
            }

            //7 Make the chosen cell the current cell and mark it as visited
            currentCell = chosenCell;
            currentCell.visited = true;

            unvistedCells = GetUnvisitedCells();
        }
        //8 Else if stack is not empty
        else if (stack.size() > 0)
        {
            //9 Pop a cell from the stack
            //10 Make it the current cell
            currentCell = stack.pop();
        }
    }
    return board;

}

    private List<Cell> GetUnvisitedCells()
    {
        List<Cell> unvistedCells = new ArrayList<>();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                Cell cell = board[i][j];
                if (cell.visited == false)
                {
                    unvistedCells.add(cell);
                }
            }
        }

        return unvistedCells;
    }

    private Neighbour MapNeighbour(int index)
    {
        switch (index)
        {
            case 0:
                return Neighbour.NORTH;
            case 1:
                return Neighbour.SOUTH;
            case 2:
                return Neighbour.WEST;
            case 3:
                return Neighbour.EAST;
            default:
                return Neighbour.NORTH;
        }
    }

    private Neighbour GetNeighbour(Cell currentCell, Cell chosenCell)
    {
        int x = currentCell.position.x;
        int y = currentCell.position.y;

        //find out the neighbour by comparing the positions
        if (chosenCell.position.x == x && chosenCell.position.y == y - 1) return Neighbour.NORTH;
        if (chosenCell.position.x == x && chosenCell.position.y == y + 1) return Neighbour.SOUTH;
        if (chosenCell.position.x == x - 1 && chosenCell.position.y == y) return Neighbour.WEST;
        if (chosenCell.position.x == x + 1 && chosenCell.position.y == y) return Neighbour.EAST;

        return Neighbour.INVALID;
    }

}
