import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ingyo on 2016. 4. 10..
 */

public class QuadTree {
    /** Starting path of files */
    private static final String IMG_ROOT = "img/";

    /** Standard Longitude, Latitude of Root */
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;

    /** Tile size(Resolution) */
    public static final int TILE_SIZE = 256;

    private Node root = new Node("0", 0, (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) 0) * TILE_SIZE),
            ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT);

    private static double start_lon, start_lat, end_lon, end_lat;
    private static boolean clear = false;

    private class Node {
        Node UL, UR, LL, LR;
        //BufferedImage image;
        String fileName;
        double dpp;
        int depth;
        double ullon, ullat, lrlon, lrlat;

        private Node(File file) {
            String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
            Node temp = root;
            char num;

            try {
                for (int i = 0; i < fileName.length() - 1; i++) {
                    num = fileName.charAt(i);
                    if (num == '1') {
                        temp = temp.UL;
                    } else if (num == '2') {
                        temp = temp.UR;
                    } else if (num == '3') {
                        temp = temp.LL;
                    } else if (num == '4') {
                        temp = temp.LR;
                    }

                }
            } catch (NullPointerException o) {
                System.out.println("Null point exception occured22.");
            }

            try {
                num = fileName.charAt(fileName.length() - 1);
                double[] tempLonLat = calculateLonLat(fileName);
                if (num == '1') {
                    temp.UL = new Node(fileName, fileName.length(),
                            (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) fileName.length()) * TILE_SIZE),
                            tempLonLat[0], tempLonLat[1], tempLonLat[2], tempLonLat[3]);
                } else if (num == '2') {
                    temp.UR = new Node(fileName, fileName.length(),
                            (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) fileName.length()) * TILE_SIZE),
                            tempLonLat[0], tempLonLat[1], tempLonLat[2], tempLonLat[3]);
                } else if (num == '3') {
                    temp.LL = new Node(fileName, fileName.length(),
                            (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) fileName.length()) * TILE_SIZE),
                            tempLonLat[0], tempLonLat[1], tempLonLat[2], tempLonLat[3]);
                } else if (num == '4') {
                    temp.LR = new Node(fileName, fileName.length(),
                            (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) fileName.length()) * TILE_SIZE),
                            tempLonLat[0], tempLonLat[1], tempLonLat[2], tempLonLat[3]);
                }
            } catch (NullPointerException e) {
                System.out.println("fuckme");
            }
        }

        private Node(String fileName, int depth, double dpp, double ullon, double ullat, double lrlon, double lrlat) {
            this.fileName = fileName;
            this.depth = depth;
            this.dpp = dpp;
            this.ullon = ullon;
            this.ullat = ullat;
            this.lrlon = lrlon;
            this.lrlat = lrlat;
        }

        private Node() {

        }

        /** Calculate tile's longitude and latitude with its fileName. */
        private double[] calculateLonLat(String fileName) {
            double[] toReturn = new double[4];
            double x1 = ROOT_ULLON;
            double y1 = ROOT_ULLAT;
            double x2 = ROOT_LRLON;
            double y2 = ROOT_LRLAT;
            double w = x2 - x1;
            double h = y1 - y2;

            for (int i = 0; i < fileName.length(); i++) {
                char num = fileName.charAt(i);

                if (num == '1') {
                    x2 -= w / Math.pow(2.0, (double) (i + 1));
                    y2 += h / Math.pow(2.0, (double) (i + 1));
                } else if (num == '2') {
                    x1 += w / Math.pow(2.0, (double) (i + 1));
                    y2 += h / Math.pow(2.0, (double) (i + 1));
                } else if (num == '3') {
                    x2 -= w / Math.pow(2.0, (double) (i + 1));
                    y1 -= h / Math.pow(2.0, (double) (i + 1));
                } else if (num == '4') {
                    x1 += w / Math.pow(2.0, (double) (i + 1));
                    y1 -= h / Math.pow(2.0, (double) (i + 1));
                }
            }

            toReturn[0] = x1;
            toReturn[1] = y1;
            toReturn[2] = x2;
            toReturn[3] = y2;
            return toReturn;
        }

    }

    /** Constructor of QuadTree and make a whole tree only with constructor. */
    public QuadTree() {
        File img_Dir = new File(IMG_ROOT);
        File[] img_arr = img_Dir.listFiles();
        //makeFullTreeWithNoValue(7, root);

        for (int i = 1; i < img_arr.length - 1; i++) {
            new Node(img_arr[i]);
        }

    }

    private void makeFullTreeWithNoValue(int depth, Node root) {
        if (depth == 0) {
            return ;
        }

        if (root.UL == null) {
            root.UL = new Node();
            makeFullTreeWithNoValue(depth - 1, root.UL);
        }
        if (root.UR == null) {
            root.UR = new Node();
            makeFullTreeWithNoValue(depth - 1, root.UR);
        }
        if (root.LL == null) {
            root.LL = new Node();
            makeFullTreeWithNoValue(depth - 1, root.LL);
        }
        if (root.LR == null) {
            root.LR = new Node();
            makeFullTreeWithNoValue(depth - 1, root.LR);
        }
        return ;
    }

    /** Find depth with given queryDpp. */
    public int findDepth(double queryDpp) {
        Node temp = root;
        while (temp.dpp >= queryDpp && temp.depth != 7) {
            temp = temp.UL;
        }
        return temp.depth;
    }

    /** Find tiles which will be appeared on client's window. */
    public BufferedImage fullImage(int depth, double x1,
                                  double y1, double x2, double y2, double w, double h) {
        if (x1 < ROOT_ULLON) {
            x1 = ROOT_ULLON + 0.00001;
        }
        if (y1 > ROOT_ULLAT) {
            y1 = ROOT_ULLAT - 0.00001;
        }
        if (x2 > ROOT_LRLON) {
            x2 = ROOT_LRLON - 0.00001;
        }
        if (y2 < ROOT_LRLAT) {
            y2 = 0.00001 + ROOT_LRLAT;
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        Node temp = findSpecificNode(depth, x1, y1, root);
        double unitX = temp.lrlon - temp.ullon;
        double unitY = temp.ullat - temp.lrlat;
        double tempX = x1;
        double tempY = y1;
        int width = 0;
        int height = 0;
        Node ttt = findSpecificNode(depth, tempX, tempY, root);
        double dppForRoute = ttt.dpp;

        /** Find some tiles to connect. */
        /** ******************************
         * Fucking border problem!!!!!!! * (while 문에)
         * *******************************/

        while (ttt.ullat > y2) {
            // previous base case: tempY > y2 - unitY
            // current base case: height < h + 256
            tempX = x1;
            width = 0;
            while (ttt.ullon < x2) {
                // previous base case: tempX < x2 + unitX
                // current base case: width < w + 256
                try {
                    images.add(ImageIO.read(new File(IMG_ROOT + ttt.fileName + ".png")));
//                    System.out.println("/" + ttt.ullon + "/" + ttt.ullat + "/" + ttt.lrlon + "/" + ttt.lrlat);
                } catch(IOException ioexception) {
                    ioexception = new IOException("Cannot open the image");
                }
                tempX += unitX;
                width += 256;
                if (tempX > ROOT_LRLON || tempX < ROOT_ULLON) {
                    break;
                }
                ttt = findSpecificNode(depth, tempX, tempY, root);
            }
            tempX = x1;
            tempY -= unitY;
            height += 256;
            if (tempY > ROOT_ULLAT || tempY < ROOT_LRLAT) {
                break;
            }
            ttt = findSpecificNode(depth, tempX, tempY, root);
        }

        /** Make a whole buffered image. */
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();

        int x = 0;
        int y = 0;
        for(BufferedImage image : images){
            g.drawImage(image, x, y, null);
            x += 256;
            if(x >= result.getWidth()){
                x = 0;
                y += image.getHeight();
            }
        }
        drawRoute(result, temp.ullon, temp.ullat, unitX, unitY);

        return result;
    }

    /** ******************************
     * Fucking right side border problem!!!!!!! *
     * *******************************/

    /** Return the positon of rastered image. */
    public double[] findPositionOfRasteredImage(int depth, double x1, double y1, double x2, double y2) {
        if (x1 < ROOT_ULLON) {
            x1 = ROOT_ULLON + 0.00001;
        }
        if (y1 > ROOT_ULLAT) {
            y1 = ROOT_ULLAT - 0.00001;
        }
        if (x2 > ROOT_LRLON) {
            x2 = ROOT_LRLON - 0.00001;
        }
        if (y2 < ROOT_LRLAT) {
            y2 = 0.00001 + ROOT_LRLAT;
        }

        double[] toReturn = new double[4];
        Node temp1 = findSpecificNode(depth, x1, y1, root);
        Node temp2 = findSpecificNode(depth, x2, y2, root);

        toReturn[0] = temp1.ullon;
        toReturn[1] = temp1.ullat;
        toReturn[2] = temp2.lrlon;
        toReturn[3] = temp2.lrlat;
//        System.out.println("\nPosition: " + "/" + toReturn[0] + "/" + toReturn[1] + "/" + toReturn[2] + "/" + toReturn[3]);

        return toReturn;
    }

    public double[] findPositionOfRasteredImage2(int depth, double x1, double y1,
                                                double x2, double y2, double w, double h) {
        double[] toReturn = new double[4];
        Node temp1 = findSpecificNode(depth, x1, y1, root);
        Node temp2;
        double unitX = temp1.lrlon - temp1.ullon;
        double unitY = temp1.ullat - temp1.lrlat;
        double tempX = x1;
        double tempY = y1;
        int width = 0;
        int height = 0;

        /** Find some tiles to connect. */
        while (height < h + 256) {
            // previous base case: tempY > y2 - unitY
            width = 0;
            tempX = x1;
            while (width < w + 256) {
                // previous base case: tempX < x2 + unitX
                tempX += unitX;
                width += 256;
            }
            tempY -= unitY;
            height += 256;
        }

        temp2 = findSpecificNode(depth, tempX - unitX, tempY + unitY, root);

        toReturn[0] = temp1.ullon;
        toReturn[1] = temp1.ullat;
        toReturn[2] = temp2.lrlon;
        toReturn[3] = temp2.lrlat;

        System.out.println("\nPosition: " + "/" + toReturn[0] + "/" + toReturn[1] + "/" + toReturn[2] + "/" + toReturn[3]);

        return toReturn;
    }

    private Node findSpecificNode(int depth, double x, double y, Node root) {
        Node toReturn = root;
        if (depth == 0) {
            return toReturn;
        }

        if (toReturn.ullon + ((toReturn.lrlon - toReturn.ullon) / 2.0) >= x
                && toReturn.lrlat + ((toReturn.ullat - toReturn.lrlat) / 2.0) <= y) {
            toReturn = findSpecificNode(depth - 1, x, y, toReturn.UL);
        } else if (toReturn.ullon + ((toReturn.lrlon - toReturn.ullon) / 2.0) <= x
                && toReturn.lrlat + ((toReturn.ullat - toReturn.lrlat) / 2.0) <= y) {
            toReturn = findSpecificNode(depth - 1, x, y, toReturn.UR);
        } else if (toReturn.ullon + ((toReturn.lrlon - toReturn.ullon) / 2.0) >= x
                && toReturn.lrlat + ((toReturn.ullat - toReturn.lrlat) / 2.0) >= y) {
            toReturn = findSpecificNode(depth - 1, x, y, toReturn.LL);
        } else if (toReturn.ullon + ((toReturn.lrlon - toReturn.ullon) / 2.0) <= x
                && toReturn.lrlat + ((toReturn.ullat - toReturn.lrlat) / 2.0) >= y) {
            toReturn = findSpecificNode(depth - 1, x, y, toReturn.LR);
        }

        return toReturn;
    }

    /** Draw route directly on the map.
     * Step 1. Transfer position of points to quadtree static variable. */
    public void transferRoutePositions(double x1, double y1, double x2, double y2) {
        this.start_lon = x1;
        this.start_lat = y1;
        this.end_lon = x2;
        this.end_lat = y2;

        this.clear = true;
    }

    /** Step 2. Draw a route implicitly(Synchronize with a map).*/
    private void drawRoute(BufferedImage displayedMap, double x0, double y0, double unitX, double unitY) {
        if (clear == true) {
            Graphics gr = displayedMap.getGraphics();
            gr.setColor(MapServer.ROUTE_STROKE_COLOR);
            Stroke s = new BasicStroke(MapServer.ROUTE_STROKE_WIDTH_PX, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            ((Graphics2D) gr).setStroke(s);
            gr.drawLine(Math.round((float) ((start_lon - x0) / (unitX / TILE_SIZE))),
                    Math.round((float) ((y0 - start_lat) / (unitY / TILE_SIZE))),
                    Math.round((float) ((end_lon - x0) / (unitX / TILE_SIZE))),
                    Math.round((float) ((y0 - end_lat) / (unitY / TILE_SIZE))));
//            System.out.println("\nlrlon=" + end_lon + ", ullon=" + start_lon + ", ullat=" + start_lat + ", lrlat=" + end_lat);
//            System.out.println("w1=" + Math.round((float) ((start_lon - x0) / (unitX / TILE_SIZE))) +
//                    ", h1=" + Math.round((float) ((y0 - start_lat) / (unitY / TILE_SIZE))) +
//                    ", w2=" + Math.round((float) ((end_lon - x0) / (unitX / TILE_SIZE))) +
//                    ", h2=" + Math.round((float) ((y0 - end_lat) / (unitY / TILE_SIZE))));
        }
    }

    /** Step 3. Clear a route by only one change. */
    public void clearRoute() {
        this.clear = false;
    }

    public static void main(String[] args) {
        QuadTree qq = new QuadTree();
        String a = "a";
    }
}
