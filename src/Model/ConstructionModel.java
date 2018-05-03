package Model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Model for creating new Fields
 */
public class ConstructionModel extends java.util.Observable {
    private char[][] gammeBoard;
    int rows;
    int col;

    public ConstructionModel(int rows, int col) {
        this.rows = rows;
        this.col = col;
        gammeBoard = new char[col][rows];
        for (int i = 0; i < col; i++) {
            getGammeBoard()[i][0] = '#';
            getGammeBoard()[i][rows - 1] = '#';
        }
        for (int i = 0; i < rows; i++) {
            getGammeBoard()[0][i] = '#';
            getGammeBoard()[col - 1][i] = '#';
        }
    }

    public void setCell(int x, int y, FloorElement floorElement) {
        if (x != 0 && x != col - 1 && y != 0 && y != rows - 1) {
            switch (floorElement) {
                case GOAL: {
                    getGammeBoard()[x][y] = '.';
                    break;
                }
                case WALL: {
                    getGammeBoard()[x][y] = '#';
                    break;
                }
                case CRATE: {
                    if (getGammeBoard()[x][y] == '.') {
                        getGammeBoard()[x][y] = '*';
                    } else {
                        getGammeBoard()[x][y] = '$';
                    }
                    break;
                }
                case PLAYER: {
                    if (getGammeBoard()[x][y] == '.') {
                        getGammeBoard()[x][y] = '+';
                    } else {
                        getGammeBoard()[x][y] = '@';
                    }
                }
            }


        }

    }

    public char[][] getGammeBoard() {
        return gammeBoard;
    }

    /**
      * Saves the new gamefiled as a txt file
      * @param name name of teh txt file (without .txt)
     */
    public void finish(String name) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(name + ".txt");
        } catch (FileNotFoundException e) {
        }
        String output = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                output += gammeBoard[j][i];
            }
            out.println(output);
            output = "";
        }
    }
}