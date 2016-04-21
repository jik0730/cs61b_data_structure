import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

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

    private Node root = new Node("", 0, 
        (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) 0) * TILE_SIZE),
        ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT);

    private static double startLon, startLat, endLon, endLat;
    private static boolean clear = false;
    private static LinkedList<BerkeleyGraph.Vertex> verticesList;
    private static ArrayList<String> fileNumber;

    private int index = 0;

    private class Node {
        Node UL, UR, LL, LR;
        String fileName;
        double dpp;
        int depth;
        double ullon, ullat, lrlon, lrlat;
        
        private Node(String fileName, int depth, double dpp, 
            double ullon, double ullat, double lrlon, double lrlat) {
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

        private String getName() {
            return this.fileName;
        }

        /** Calculate tile's longitude and latitude with its fileName. */
        private double[] calculateLonLat(String fileNamee) {
            double[] toReturn = new double[4];
            double x1 = ROOT_ULLON;
            double y1 = ROOT_ULLAT;
            double x2 = ROOT_LRLON;
            double y2 = ROOT_LRLAT;
            double w = x2 - x1;
            double h = y1 - y2;

            for (int i = 0; i < fileNamee.length(); i++) {
                char num = fileNamee.charAt(i);

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
        fileNumber = new ArrayList<>();
        makeFileList();
        makeChild(root, 1, ROOT_ULLON, ROOT_ULLAT, ROOT_LRLON, ROOT_LRLAT);
    }

    public void makeFileList() {
        File[] files = new File(IMG_ROOT).listFiles();
        ArrayList<String> fileList = new ArrayList<>();
        for (int i = 1; i < files.length; i++) {
            if (files[i].isFile()) {
                fileList.add(files[i].getName());
            }
        }
        for (int i = 0; i < fileList.size(); i++) {
            fileNumber.add(fileList.get(i).substring(0, fileList.get(i).lastIndexOf('.')));
        }
    }

    public void makeChild(Node x, int height, 
        double ullon, double ullat, double lrlon, double lrlat) {
        index++;
        if (height > 7) {
            return;
        }
        if (x.UL == null && index <= fileNumber.size() - 1) {
            double tempX = lrlon - (ROOT_LRLON - ROOT_ULLON) / Math.pow(2.0, height);
            double tempY = lrlat + (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2.0, height);
            x.UL = new Node(x.getName() + "1", height,
                    (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) height) * TILE_SIZE),
                    ullon, ullat, tempX, tempY);
            makeChild(x.UL, height + 1, ullon, ullat, tempX, tempY);
        }
        if (x.UR == null && index <= fileNumber.size() - 1) {
            double tempY = lrlat + (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2.0, height);
            double tempX = ullon + (ROOT_LRLON - ROOT_ULLON) / Math.pow(2.0, height);
            x.UR = new Node(x.getName() + "2", height,
                    (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) height) * TILE_SIZE),
                    tempX, ullat, lrlon, tempY);
            makeChild(x.UR, height + 1, tempX, ullat, lrlon, tempY);
        }
        if (x.LL == null && index <= fileNumber.size() - 1) {
            double tempY = ullat - (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2.0, height);
            double tempX = lrlon - (ROOT_LRLON - ROOT_ULLON) / Math.pow(2.0, height);
            x.LL = new Node(x.getName() + "3", height,
                    (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) height) * TILE_SIZE),
                    ullon, tempY, tempX, lrlat);
            makeChild(x.LL, height + 1, ullon, tempY, tempX, lrlat);
        }
        if (x.LR == null && index <= fileNumber.size() - 1) {
            double tempX = ullon + (ROOT_LRLON - ROOT_ULLON) / Math.pow(2.0, height);
            double tempY = ullat - (ROOT_ULLAT - ROOT_LRLAT) / Math.pow(2.0, height);
            x.LR = new Node(x.getName() + "4", height,
                    (ROOT_LRLON - ROOT_ULLON) / (Math.pow(2.0, (double) height) * TILE_SIZE),
                    tempX, tempY, lrlon, lrlat);
            makeChild(x.LR, height + 1, tempX, tempY, lrlon, lrlat);
        }
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
        while (ttt.ullat > y2) {
            tempX = x1;
            width = 0;
            while (ttt.ullon < x2) {
                try {
                    images.add(ImageIO.read(new File(IMG_ROOT + ttt.fileName + ".png")));
                } catch (IOException ioexception) {
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
        for (BufferedImage image : images) {
            g.drawImage(image, x, y, null);
            x += 256;
            if (x >= result.getWidth()) {
                x = 0;
                y += image.getHeight();
            }
        }
        drawRoute(result, temp.ullon, temp.ullat, unitX, unitY);

        return result;
    }

    /** Return the positon of rastered image. */
    public double[] findPositionOfRasteredImage(int depth, 
        double x1, double y1, double x2, double y2) {
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

        return toReturn;
    }

    private Node findSpecificNode(int depth, double x, double y, Node rr) {
        Node toReturn = rr;
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
    public void transferRoutePositions(double x1, double y1, double x2, double y2,
                                       LinkedList<BerkeleyGraph.Vertex> v) {
        this.startLon = x1;
        this.startLat = y1;
        this.endLon = x2;
        this.endLat = y2;
        this.verticesList = v;

        this.clear = true;
    }

    /** Step 2. Draw a route implicitly(Synchronize with a map).*/
    private void drawRoute(BufferedImage displayedMap, 
        double x0, double y0, double unitX, double unitY) {
        if (clear) {
            Graphics gr = displayedMap.getGraphics();
            gr.setColor(MapServer.ROUTE_STROKE_COLOR);
            Stroke s = new BasicStroke(MapServer.ROUTE_STROKE_WIDTH_PX, 
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            ((Graphics2D) gr).setStroke(s);
            for (int i = 0; i < verticesList.size() - 1; i++) {
                gr.drawLine((int) ((verticesList.get(i).getLon() - x0)
                        / (unitX / (double) TILE_SIZE)),
                        (int) ((y0 - verticesList.get(i).getLat())
                                / (unitY / (double) TILE_SIZE)),
                        (int) ((verticesList.get(i + 1).getLon() - x0)
                                / (unitX / (double) TILE_SIZE)),
                        (int) ((y0 - verticesList.get(i + 1).getLat())
                                / (unitY / (double) TILE_SIZE)));
            }
        }
    }

    /** Step 3. Clear a route by only one change. */
    public void clearRoute() {
        this.clear = false;
    }
}
