import java.util.List;

/**
 * Created by Ingyo on 2016. 4. 30..
 */

/** Board to play Boggle game. */
public class Board {
    private char[][] board;
    private int xLength;
    private int yLength;

    public Board(List<String> list) {
        this.yLength = list.size();
        this.xLength = list.get(0).length();
        this.board = new char[list.size()][];

        /** Iterate through the list which will construct the board. */
        for (int i = 0; i < this.board.length; i += 1) {
            this.board[i] = new char[list.get(i).length()];
            for (int j = 0; j < list.get(i).length(); j += 1) {
                this.board[i][j] = list.get(i).charAt(j);
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public int getxLength() {
        return xLength;
    }

    public int getyLength() {
        return yLength;
    }

}
