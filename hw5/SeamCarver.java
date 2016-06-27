import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

/**
 * Created by Ingyo on 2016. 4. 23..
 */
public class SeamCarver {
    private Picture picture;
    private Color[][] pixels;

    /** Constructor */
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        this.pixels = new Color[picture.width()][picture.height()];
        for (int i = 0; i < pixels.length; i += 1) {
            for (int j = 0; j < pixels[i].length; j += 1) {
                pixels[i][j] = picture.get(i, j);
            }
        }
    }

    /** current picture */
    public Picture picture() {
        return new Picture(this.picture);
    }

    /** width of current picture */
    public int width() {
        return this.picture.width();
    }

    /** height of current picture */
    public int height() {
        return this.picture.height();
    }

    /** energy of pixel at column x and row y */
    public double energy(int x, int y) {
        if (x >= width() || x < 0 || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }

        double drx, dgx, dbx;
        double dry, dgy, dby;

        /** Exception handler (if width == 1 or height == 1) */
        if (width() == 1 && height() == 1) {
            return 0.0;
        } else if (width() == 1) {
            if (y == 0) {
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][height() - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][height() - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][height() - 1].getBlue());
            } else if (y == height() - 1) {
                dry = Math.abs(pixels[x][y - 1].getRed() - pixels[x][0].getRed());
                dgy = Math.abs(pixels[x][y - 1].getGreen() - pixels[x][0].getGreen());
                dby = Math.abs(pixels[x][y - 1].getBlue() - pixels[x][0].getBlue());
            } else {
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][y - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][y - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][y - 1].getBlue());
            }
            return Math.pow(dry, 2) + Math.pow(dgy, 2) + Math.pow(dby, 2);
        } else if (height() == 1) {
            if (x == 0) {
                drx = Math.abs(pixels[x + 1][y].getRed() - pixels[width() - 1][y].getRed());
                dgx = Math.abs(pixels[x + 1][y].getGreen() - pixels[width() - 1][y].getGreen());
                dbx = Math.abs(pixels[x + 1][y].getBlue() - pixels[width() - 1][y].getBlue());
            } else if (x == width() - 1) {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[0][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[0][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[0][y].getBlue());
            } else {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[x + 1][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[x + 1][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[x + 1][y].getBlue());
            }
            return Math.pow(drx, 2) + Math.pow(dgx, 2) + Math.pow(dbx, 2);
        }

        /** Normal situation. */
        double[] temp = new double[6];
        calculateGradient(x, y, temp);

        drx = temp[0];
        dgx = temp[1];
        dbx = temp[2];
        dry = temp[3];
        dgy = temp[4];
        dby = temp[5];

        return Math.pow(drx, 2) + Math.pow(dgx, 2) + Math.pow(dbx, 2)
                + Math.pow(dry, 2) + Math.pow(dgy, 2) + Math.pow(dby, 2);
    }

    private void calculateGradient(int x, int y, double[] toReturn) {
        double drx, dgx, dbx, dry, dgy, dby;
        if (x == 0) {
            if (y == 0) {
                drx = Math.abs(pixels[x + 1][y].getRed() - pixels[width() - 1][y].getRed());
                dgx = Math.abs(pixels[x + 1][y].getGreen() - pixels[width() - 1][y].getGreen());
                dbx = Math.abs(pixels[x + 1][y].getBlue() - pixels[width() - 1][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][height() - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][height() - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][height() - 1].getBlue());
            } else if (y == height() - 1) {
                drx = Math.abs(pixels[x + 1][y].getRed() - pixels[width() - 1][y].getRed());
                dgx = Math.abs(pixels[x + 1][y].getGreen() - pixels[width() - 1][y].getGreen());
                dbx = Math.abs(pixels[x + 1][y].getBlue() - pixels[width() - 1][y].getBlue());
                dry = Math.abs(pixels[x][y - 1].getRed() - pixels[x][0].getRed());
                dgy = Math.abs(pixels[x][y - 1].getGreen() - pixels[x][0].getGreen());
                dby = Math.abs(pixels[x][y - 1].getBlue() - pixels[x][0].getBlue());
            } else {
                drx = Math.abs(pixels[x + 1][y].getRed() - pixels[width() - 1][y].getRed());
                dgx = Math.abs(pixels[x + 1][y].getGreen() - pixels[width() - 1][y].getGreen());
                dbx = Math.abs(pixels[x + 1][y].getBlue() - pixels[width() - 1][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][y - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][y - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][y - 1].getBlue());
            }
        } else if (x == width() - 1) {
            if (y == 0) {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[0][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[0][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[0][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][height() - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][height() - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][height() - 1].getBlue());
            } else if (y == height() - 1) {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[0][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[0][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[0][y].getBlue());
                dry = Math.abs(pixels[x][y - 1].getRed() - pixels[x][0].getRed());
                dgy = Math.abs(pixels[x][y - 1].getGreen() - pixels[x][0].getGreen());
                dby = Math.abs(pixels[x][y - 1].getBlue() - pixels[x][0].getBlue());
            } else {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[0][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[0][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[0][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][y - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][y - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][y - 1].getBlue());
            }
        } else {
            if (y == 0) {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[x + 1][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[x + 1][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[x + 1][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][height() - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][height() - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][height() - 1].getBlue());
            } else if (y == height() - 1) {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[x + 1][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[x + 1][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[x + 1][y].getBlue());
                dry = Math.abs(pixels[x][y - 1].getRed() - pixels[x][0].getRed());
                dgy = Math.abs(pixels[x][y - 1].getGreen() - pixels[x][0].getGreen());
                dby = Math.abs(pixels[x][y - 1].getBlue() - pixels[x][0].getBlue());
            } else {
                drx = Math.abs(pixels[x - 1][y].getRed() - pixels[x + 1][y].getRed());
                dgx = Math.abs(pixels[x - 1][y].getGreen() - pixels[x + 1][y].getGreen());
                dbx = Math.abs(pixels[x - 1][y].getBlue() - pixels[x + 1][y].getBlue());
                dry = Math.abs(pixels[x][y + 1].getRed() - pixels[x][y - 1].getRed());
                dgy = Math.abs(pixels[x][y + 1].getGreen() - pixels[x][y - 1].getGreen());
                dby = Math.abs(pixels[x][y + 1].getBlue() - pixels[x][y - 1].getBlue());
            }
        }
        toReturn[0] = drx;
        toReturn[1] = dgx;
        toReturn[2] = dbx;
        toReturn[3] = dry;
        toReturn[4] = dgy;
        toReturn[5] = dby;
    }

    /** sequence of indices for horizontal seam */
    public int[] findHorizontalSeam() {
        double[][] accumulatedEnergy = new double[width()][height()];
        int[][] followedPath = new int [width()][height()];

        for (int i = 0; i < height(); i += 1) {
            accumulatedEnergy[0][i] = energy(0, i);
        }

        /** Exception Handler */
        if (width() == 1) {
            int[] indicesIfWidthIs1 = new int[1];
            double value = accumulatedEnergy[0][0];
            indicesIfWidthIs1[0] = 0;
            for (int i = 1; i < height(); i += 1) {
                if (accumulatedEnergy[0][i] < value) {
                    value = accumulatedEnergy[0][i];
                    indicesIfWidthIs1[0] = i;
                }
            }
            return indicesIfWidthIs1;
        } else if (height() == 1) {
            int[] indicesIfHeightIs1 = new int[width()];
            return indicesIfHeightIs1;
        }

        for (int i = 1; i < width(); i += 1) {
            for (int j = 0; j < height(); j += 1) {
                accumulatedEnergy[i][j] = energy(i, j);
                if (j == 0) {
                    accumulatedEnergy[i][j] += Math.min(accumulatedEnergy[i - 1][j],
                            accumulatedEnergy[i - 1][j + 1]);
                    if (accumulatedEnergy[i - 1][j] <= accumulatedEnergy[i - 1][j + 1]) {
                        followedPath[i][j] = j;
                    } else {
                        followedPath[i][j] = j + 1;
                    }
                } else if (j == height() - 1) {
                    accumulatedEnergy[i][j] += Math.min(accumulatedEnergy[i - 1][j],
                            accumulatedEnergy[i - 1][j - 1]);
                    if (accumulatedEnergy[i - 1][j] <= accumulatedEnergy[i - 1][j - 1]) {
                        followedPath[i][j] = j;
                    } else {
                        followedPath[i][j] = j - 1;
                    }
                } else {
                    accumulatedEnergy[i][j] += min(accumulatedEnergy[i - 1][j - 1],
                            accumulatedEnergy[i - 1][j], accumulatedEnergy[i - 1][j + 1]);
                    if (accumulatedEnergy[i - 1][j - 1] <= accumulatedEnergy[i - 1][j]
                            && accumulatedEnergy[i - 1][j - 1] <= accumulatedEnergy[i - 1][j + 1]) {
                        followedPath[i][j] = j - 1;
                    } else if (accumulatedEnergy[i - 1][j] < accumulatedEnergy[i - 1][j - 1]
                            && accumulatedEnergy[i - 1][j] <= accumulatedEnergy[i - 1][j + 1]) {
                        followedPath[i][j] = j;
                    } else {
                        followedPath[i][j] = j + 1;
                    }
                }
            }
        }

        double smallest = Double.MAX_VALUE;
        int smallestIndex = -1;
        int[] indices = new int[width()];

        for (int i = 0; i < height(); i += 1) {
            if (accumulatedEnergy[width() - 1][i] < smallest) {
                smallest = accumulatedEnergy[width() - 1][i];
                smallestIndex = i;
            }
        }
        indices[width() - 1] = smallestIndex;

        for (int i = width() - 1; i >= 1; i -= 1) {
            smallestIndex = followedPath[i][smallestIndex];
            indices[i - 1] = smallestIndex;
        }

        return indices;
    }

    /** sequence of indices for vertical seam */
    public int[] findVerticalSeam() {
        double[][] accumulatedEnergy = new double[width()][height()];
        int[][] followedPath = new int [width()][height()];

        for (int i = 0; i < width(); i += 1) {
            accumulatedEnergy[i][0] = energy(i, 0);
        }

        /** Exception Handler */
        if (height() == 1) {
            int[] indicesIfHeightIs1 = new int[1];
            double value = accumulatedEnergy[0][0];
            indicesIfHeightIs1[0] = 0;
            for (int i = 1; i < width(); i += 1) {
                if (accumulatedEnergy[i][0] < value) {
                    value = accumulatedEnergy[i][0];
                    indicesIfHeightIs1[0] = i;
                }
            }
            return indicesIfHeightIs1;
        } else if (width() == 1) {
            int[] indicesIfWidthIs1 = new int[height()];
            return indicesIfWidthIs1;
        }

        for (int i = 1; i < height(); i += 1) {
            for (int j = 0; j < width(); j += 1) {
                accumulatedEnergy[j][i] = energy(j, i);
                if (j == 0) {
                    accumulatedEnergy[j][i] += Math.min(accumulatedEnergy[j][i - 1],
                            accumulatedEnergy[j + 1][i - 1]);
                    if (accumulatedEnergy[j][i - 1] <= accumulatedEnergy[j + 1][i - 1]) {
                        followedPath[j][i] = j;
                    } else {
                        followedPath[j][i] = j + 1;
                    }
                } else if (j == width() - 1) {
                    accumulatedEnergy[j][i] += Math.min(accumulatedEnergy[j][i - 1],
                            accumulatedEnergy[j - 1][i - 1]);
                    if (accumulatedEnergy[j][i - 1] <= accumulatedEnergy[j - 1][i - 1]) {
                        followedPath[j][i] = j;
                    } else {
                        followedPath[j][i] = j - 1;
                    }
                } else {
                    accumulatedEnergy[j][i] += min(accumulatedEnergy[j - 1][i - 1],
                            accumulatedEnergy[j][i - 1], accumulatedEnergy[j + 1][i - 1]);
                    if (accumulatedEnergy[j - 1][i - 1] <= accumulatedEnergy[j][i - 1]
                            && accumulatedEnergy[j - 1][i - 1] <= accumulatedEnergy[j + 1][i - 1]) {
                        followedPath[j][i] = j - 1;
                    } else if (accumulatedEnergy[j][i - 1] < accumulatedEnergy[j - 1][i - 1]
                            && accumulatedEnergy[j][i - 1] <= accumulatedEnergy[j + 1][i - 1]) {
                        followedPath[j][i] = j;
                    } else {
                        followedPath[j][i] = j + 1;
                    }
                }
            }
        }

        double smallest = Double.MAX_VALUE;
        int smallestIndex = -1;
        int[] indices = new int[height()];

        for (int i = 0; i < width(); i += 1) {
            if (accumulatedEnergy[i][height() - 1] < smallest) {
                smallest = accumulatedEnergy[i][height() - 1];
                smallestIndex = i;
            }
        }
        indices[height() - 1] = smallestIndex;

        for (int i = height() - 1; i >= 1; i -= 1) {
            smallestIndex = followedPath[smallestIndex][i];
            indices[i - 1] = smallestIndex;
        }

        return indices;
    }

    /** Calculate the minimum value among 3 values */
    private double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    /** remove horizontal seam from picture */
    public void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    /** remove vertical seam from picture */
    public void removeVerticalSeam(int[] seam) {
        SeamRemover.removeVerticalSeam(picture, seam);
    }

//    public static void main(String[] args) {
//        Color c = new Color(123,24,1);
//        System.out.println(c);
//    }
}
