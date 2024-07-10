package snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.cells.Cell;
import snakes_n_ladders_sim.simulation.entities.cells.concrete_cell.*;
import snakes_n_ladders_sim.simulation.mediator.Mediator;

public class RandomBoardBuilder implements BoardBuildStrategy {
    private Mediator mediator;

    private List<Cell> specialCells = new ArrayList<>();
    private float fillingRatio;

    private List<Integer> emptyCellPositionList;

    private RandomBoardBuilder(boolean isPriceOn, boolean isParkingOn, boolean isCardsOn, float fillingRatio, Mediator mediator) {
        if(isPriceOn) {
            specialCells.add(new PriceCell(mediator, false)); // Reroll
            specialCells.add(new PriceCell(mediator, true)); // Spring
        }
        if(isParkingOn) {
            specialCells.add(new ParkingCell(mediator, 1)); // Bench
            specialCells.add(new ParkingCell(mediator, 3)); // Inn
        }
        if(isCardsOn) {
            specialCells.add(new DrawCardCell(mediator)); // Draw Card
        }

        emptyCellPositionList = new ArrayList<>();

        this.fillingRatio = fillingRatio;
    }

    public RandomBoardBuilder(boolean isPriceOn, boolean isParkingOn, boolean isCardsOn, Mediator mediator) {
        this(isPriceOn, isParkingOn, isCardsOn, (float)(0.15+(isCardsOn ? 1 : 0)*0.05+(isParkingOn ? 1 : 0)*0.05+(isPriceOn ? 1 : 0)*0.05), mediator);
    }

    @Override
    public Cell[][] buildBoard(int rows, int columns) {
        int nSpecialCells = Math.round(rows*columns*fillingRatio);
        Random rng = new Random(System.currentTimeMillis());

        Cell[][] board = new Cell[rows][columns];
        initializeEmptyCellPositionList(rows, columns);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // For each cell
                // If nSpecialCells > 0 and is not the last cell, fillingRatio% chance to be a special cell over a basic cell
                if(rng.nextFloat() <= fillingRatio && nSpecialCells > 0 && !((i+1==rows) && (j+1==columns))) {
                    // 50% chance to be a special cell over Snake or Ladder
                    // If the special cell list is empty or null, it will be always a Snake or Ladder
                    if(rng.nextFloat() <= 0.5 || specialCells.isEmpty() || specialCells == null) {
                        addCell(i, j, board, setSnakeOrLadder(board, i, rows, columns, rng, mediator));
                    }else {
                        addCell(i, j, board, specialCells.get(rng.nextInt(specialCells.size())));
                    }
                    nSpecialCells--;
                }else {
                    // If the cell wan not already filled, fill it with a basic cell
                    if (board[i][j] == null) {
                        addCell(i, j, board, new BasicCell(mediator));
                    }
                }
            }
        }

        return board;
    }

    private void initializeEmptyCellPositionList(int rows, int columns) {
        for (int i = 0; i < rows*columns; i++) {
            emptyCellPositionList.add(i+1);
        }
    }

    private void addCell(int i, int j, Cell[][] board, Cell cell) {
        board[i][j] = cell;
        emptyCellPositionList.remove(Integer.valueOf((i+1)*(j+1)));
    }

    private Cell setSnakeOrLadder(Cell[][] board,int i, int rows, int columns, Random rng, Mediator mediator) {
        if(i < 1) {
            // If is the first row, it's a Ladder
            int tpPosition = getRandomEmptyCellPositionLadder((i+1)*columns+1, rng);
            addCell(tpPosition-1 % columns, tpPosition-1 / rows, board, new BasicCell(mediator));
            return new SnakeOrLadderCell(mediator, tpPosition);
        }else if(i > rows-2) {
            // If is the last row, it's a Snake
            int tpPosition = getRandomEmptyCellPositionSnake(i*columns, rng);
            addCell(tpPosition-1 % columns, tpPosition-1 / rows, board, new BasicCell(mediator));
            return new SnakeOrLadderCell(mediator, tpPosition);
        }

        // 50% chance to be a Snake over a Ladder
        if(rng.nextFloat() <= 0.5) {
            // Snake
            int tpPosition = getRandomEmptyCellPositionSnake(i*columns, rng);
            addCell(tpPosition-1 % columns, tpPosition-1 / rows, board, new BasicCell(mediator));
            return new SnakeOrLadderCell(mediator, tpPosition);
        }
        // Ladder
        int tpPosition = getRandomEmptyCellPositionLadder((i+1)*columns+1, rng);
        addCell(tpPosition-1 % columns, tpPosition-1 / rows, board, new BasicCell(mediator));
        return new SnakeOrLadderCell(mediator, tpPosition);
    }

    // Get a random empty cell position after a Ladder minPos (first cell on the next row)
    private int getRandomEmptyCellPositionLadder(int minPos, Random rng) {
        int split = -1;
        // Find the first empty cell position after minPos
        for (int i = 0; i < emptyCellPositionList.size(); i++) {
            if(emptyCellPositionList.get(i) >= minPos) {
                split = i;
                break;
            }
        }

        if(split == -1) {
            throw new RuntimeException("Board construction error, impossible to find a random empty cell position for ladder");
        }

        // Get a random empty cell position after minPos
        List<Integer> tempList = new ArrayList<>(emptyCellPositionList.subList(split, emptyCellPositionList.size()));
        return tempList.get(rng.nextInt(tempList.size()));
    }

    // Get a random empty cell position before a Snake minPos (last cell on the previous row)
    private int getRandomEmptyCellPositionSnake(int minPos, Random rng) {
        int split = -1;
        // Find the first empty cell position before minPos
        for (int i = emptyCellPositionList.size(); i > 0; i--) {
            if(emptyCellPositionList.get(i) <= minPos) {
                split = i;
                break;
            }
        }

        if(split == -1) {
            throw new RuntimeException("Board construction error, impossible to find a random empty cell position for ladder");
        }

        // Get a random empty cell position before minPos
        List<Integer> tempList = new ArrayList<>(emptyCellPositionList.subList(0, split));
        return tempList.get(rng.nextInt(tempList.size()));
    }
}