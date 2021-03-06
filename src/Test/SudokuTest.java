package Test;

import Model.Sudoku;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {
    int[][] validBoard;
    int[][] invalidBoard;
    int[][] invalidSizeBoard;
    Sudoku randomSudoku;
    Sudoku arraySudoku;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        validBoard = new int[][] {
                {1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 5, 0, 0, 0, 0},
                {0, 0, 5, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 3, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 3, 0, 0},
                {0, 0, 0, 0, 0, 5, 0, 0, 0}
        };

        invalidSizeBoard = new int[10][5];

        invalidBoard = new int[][] {
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 2, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},

        };
        randomSudoku = Sudoku.randomBoard();
        try {
            arraySudoku = Sudoku.fromArray(validBoard);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getBoard() {
        int[][] board = randomSudoku.getBoard();
        assertEquals(Sudoku.getSize(), randomSudoku.getBoard()[0].length);
        assertEquals(Sudoku.getSize(), randomSudoku.getBoard().length);
    }

    @org.junit.jupiter.api.Test
    void isCellEmpty() {
        assertFalse(arraySudoku.isCellEmpty(0, 0));
        assertTrue(arraySudoku.isCellEmpty(1, 1));
    }

    @org.junit.jupiter.api.Test
    void setEmpty() {
        assertFalse(arraySudoku.isCellEmpty(0, 0));
        arraySudoku.setEmpty(0,0);
        assertTrue(arraySudoku.isCellEmpty(0, 0));
    }

    @org.junit.jupiter.api.Test
    void insert() {
        try {
            Sudoku newSudoku = Sudoku.fromArray(validBoard);
            assertTrue(newSudoku.isCellEmpty(2, 5)); //cell is empty
            assertFalse(newSudoku.insert(2, 5, 3)); //invalid placement attempt
            assertTrue(newSudoku.isCellEmpty(2, 5)); //cell remains empty
            assertTrue(newSudoku.insert(2, 5, 4)); //valid placement attempt
            assertFalse(newSudoku.isCellEmpty(2, 5)); //cell no longer empty
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void isInsertionAllowed() {
        assertFalse(arraySudoku.isInsertionAllowed(4, 3, 5));
        assertTrue(arraySudoku.isInsertionAllowed(4, 3, 6));
    }

    @org.junit.jupiter.api.Test
    void fromArray() {
        Sudoku testSudoku = null;
        try {
            testSudoku = Sudoku.fromArray(invalidSizeBoard);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            assertNull(testSudoku);
        }

        try {
            testSudoku = Sudoku.fromArray(invalidBoard);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            assertNull(testSudoku);
        }

        try {
            testSudoku = Sudoku.fromArray(validBoard);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            assertNotNull(testSudoku);
        }
    }

    @org.junit.jupiter.api.Test
    void printBoard() {
        try {
            Sudoku printSudoku = Sudoku.fromArray(validBoard);
            printSudoku.printBoard();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @org.junit.jupiter.api.Test
    void randomBoard() {
        boolean noExceptions = true;
        for(int i = 0; i < 100; i++) {
            Sudoku random = Sudoku.randomBoard(); //make random board
            try {
                //generate new sudoku using random board to check if it generates valid sudokus
                Sudoku fromRandom = Sudoku.fromArray(random.getBoard());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                noExceptions = false;
                break;
            }
        }
        assertTrue(noExceptions);
    }

    @org.junit.jupiter.api.Test
    void copy() {
        Sudoku original = Sudoku.randomBoard();
        Sudoku copy = original.copy();
        assertArrayEquals(original.getBoard(),copy.getBoard());
        assertNotEquals(copy,original);
    }
}