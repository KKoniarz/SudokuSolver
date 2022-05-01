package Model;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Sudoku {

    static final int SIZE = 9; //Size of the board 9x9 cells
    private int[][] board; //internal state of the sudoku
    private final ReentrantLock lock;

    protected Sudoku() {
        this.board = new int[SIZE][SIZE];
        lock = new ReentrantLock();
    }

    //threadsafe getter for board
    public int[][] getBoard() {
        lock.lock();
        try {
            return board.clone();
        } finally {
            lock.unlock();
        }
    }

    public static int getSize() {
        return SIZE;
    }

    public boolean isCellEmpty(int row, int column) {
        lock.lock();
        try {
            return board[row][column] == 0;
        } finally {
            lock.unlock();
        }
    }

    public void setEmpty(int row, int column) {
        lock.lock();
        try {
            board[row][column] = 0;
        } finally {
            lock.unlock();
        }
    }

    //try to insert a value into a board return if successful
    public boolean insert(int row, int column, int value) {
        lock.lock();
        try {
            if(checkIfValid(row, column, value)) {
                board[row][column] = value;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }

    }

    //Check if a value can be properly inserted into board
    private boolean checkIfValid(int row, int column, int value) {
        return isValidInRow(row, value) &&
                isValidInColumn(column, value) &&
                isValidInSquare(row, column, value);
    }

    //checkIfValid with lock for use outside of insert method
    public boolean isInsertionAllowed(int row, int column, int value) {
        lock.lock();
        try {
            return checkIfValid(row, column, value);
        } finally {
            lock.unlock();
        }
    }

    //Check if a given row doesn't already contain passed value
    private boolean isValidInRow(int row, int value) {
        for(int i = 0; i < SIZE; i++) {
            if(board[row][i] == value) {
                return false;
            }
        }
        return true;
    }

    //Check if a given column doesn't already contain passed value
    private boolean isValidInColumn(int column, int value) {
        for(int i = 0; i < SIZE; i++) {
            if(board[i][column] == value) {
                return false;
            }
        }
        return true;
    }

    //Check if a given square doesn't already contain passed value
    private boolean isValidInSquare(int row, int column, int value) {
        int cornerX = column - column % 3;
        int cornerY = row - row % 3;
        for(int i = cornerY; i < cornerY + 3; i++) {
            for(int j = cornerX; j < cornerX + 3; j++) {
                if(board[i][j] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    //Get instance of Sudoku from passed array
    public static Sudoku fromArray(int[][] array) throws Exception {
        Sudoku sudoku = new Sudoku();

        //check if passed array is of right size if it's now return empty sudoku
        if(array.length != SIZE || array[0].length != SIZE) {
            throw new Exception("Invalid board size! Should be 9x9.");
        }

        //check if sudoku is valid
        for(int row = 0; row < SIZE; row++){
            for(int column = 0; column < SIZE; column++) {
                //if cell is not empty, and it's current value is not valid
                int value = array[row][column];
                if(value != 0 && !sudoku.insert(row,column,value)) {
                    throw new Exception("Sudoku is not valid! Values repeat neither in columns, rows or squares.");
                }
            }
        }
        return sudoku;
    }

    //Get instance of the randomly initialized Sudoku
    public static Sudoku randomBoard() {
        Sudoku sudoku = new Sudoku();
        Random rand = new Random();
        int numberQuantity = rand.nextInt(15) + 5; //between 5 and 20 numbers will be placed randomly
        for(int i = 0; i < numberQuantity; i++) {
            int column = rand.nextInt(9);
            int row = rand.nextInt(9);
            int value = rand.nextInt(9);
            sudoku.insert(row,column,value);
        }
        return sudoku;
    }

    public void printBoard(){
        lock.lock();
        try {
            for(int i = 0; i < SIZE; i++) {
                for(int j = 0; j < SIZE; j++) {
                    if(j != 0 && j % 3 ==0) {
                        System.out.print("| ");
                    }
                    System.out.print(board[i][j]+" ");
                }
                if(i == 2 || i == 5) {
                    System.out.print("\n- - - - - - - - - - - ");
                }
                System.out.print("\n");
            }
        } finally {
            lock.unlock();
        }

    }

    public Sudoku copy() {
        Sudoku copy = new Sudoku();
        copy.board = board;
        return copy;
    }
}