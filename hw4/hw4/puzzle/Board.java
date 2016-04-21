package hw4.puzzle;

public class Board {

    private final int[][] tile;
    private static int[][] goalTile;

    /** Constructor. */
    public Board(int[][] tiles) {
        tile = new int[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            tile[i] = new int[tiles[i].length];
            for (int j = 0; j < tiles[i].length; j++) {
                tile[i][j] = tiles[i][j];
            }
        }

        if (goalTile == null) {
            goalTile = new int[tiles.length][];
            for (int i = 0; i < tiles.length; i++) {
                goalTile[i] = new int[tiles[i].length];
                for (int j = 0; j < tiles[i].length; j++) {
                    goalTile[i][j] = i * tiles[i].length + j + 1;
                }
            }
            goalTile[size() - 1][size() - 1] = 0;
        }
    }

    /** Returns value of the tile where we want to find by index. */
    public int tileAt(int i, int j) {
        if (i < 0 || j < 0 || i > size() * size() || j > size() * size()) {
            throw new IndexOutOfBoundsException("Oh, no!");
        }
        return tile[i][j];
    }

    /** Returns length of tile(array). */
    public int size() {
        return tile.length;
    }

    /** Returns an integer that represents hamming priority. */
    public int hamming() {
        int toReturn = 0;
        for (int i = 0; i < goalTile.length; i++) {
            for (int j = 0; j < goalTile[i].length; j++) {
                if (tile[i][j] != goalTile[i][j] && tile[i][j] != 0) {
                    toReturn += 1;
                }
            }
        }
        return toReturn;
    }

    /** Returns an integer that represents manhattan priority. */
    public int manhattan() {
        int toReturn = 0;
        int tilei, tilej;
        for (int i = 0; i < goalTile.length; i++) {
            for (int j = 0; j < goalTile[i].length; j++) {
                if (tile[i][j] != goalTile[i][j] && tile[i][j] != 0) {
                    tilei = tile[i][j] / size();
                    tilej = tile[i][j] - tilei * size() - 1;
                    toReturn += Math.abs(i - tilei) + Math.abs(j - tilej);
                }
            }
        }
        return toReturn;
    }

    public boolean isGoal() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tile[i][j] != goalTile[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object y) {
        Board tempy = (Board) y;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tile[i][j] != tempy.tile[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
