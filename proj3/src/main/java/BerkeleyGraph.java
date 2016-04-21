import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


/**
 * Created by Ingyo on 2016. 4. 15..
 */
public class BerkeleyGraph {
    /** Vertices information and their adj information. */
    private HashMap<Long, Vertex> vertexInfo;
    private HashMap<Long, HashSet<Long>> adj;
    private HashMap<Long, String> cleanedNames;
    private HashMap<Long, Vertex> names;

    /** Vertex class in Graph. */
    public class Vertex {
        Long id;
        double lon;
        double lat;
        String name;

        public Vertex(Long id, double lon, double lat, String name) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.name = name;
        }

        private Vertex(Long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }

        private Vertex() {
        }

        public double getLon() {
            return this.lon;
        }

        public double getLat() {
            return this.lat;
        }

        public Long getID() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    /** Node class for aStar algorithm. */
    private class Node implements Comparable<Node> {
        private Vertex v = null;
        private Node prev = null;
        private double distTo = 0.0;
        private double heuristic = 0.0;
        private double priority = 0.0;

        Node(Vertex v, Node prev, Vertex endV) {
            if (prev == null) {
                this.v = v;
                this.prev = null;
                this.distTo = 0.0;
                this.heuristic = computeDistance(this.v.lon, this.v.lat, endV.lon, endV.lat);
                this.priority = this.distTo + this.heuristic;
            } else {
                this.v = v;
                this.prev = prev;
                this.distTo = prev.distTo + computeDistance(this.v.lon, this.v.lat,
                        prev.v.lon, prev.v.lat);
                this.heuristic = computeDistance(this.v.lon, this.v.lat, endV.lon, endV.lat);
                this.priority = this.distTo + this.heuristic;
            }
        }

        public boolean isGoal(Vertex endV) {
            return this.v.id.equals(endV.id);
        }

        public int compareTo(Node vv) {
            if (this.priority > vv.priority) {
                return 1;
            } else if (this.priority < vv.priority) {
                return -1;
            }
            return 0;
        }
    }

    /** Initiate elements of graph. */
    public BerkeleyGraph() {
        vertexInfo = new HashMap<>();
        adj = new HashMap<>();
        cleanedNames = new HashMap<>();
        names = new HashMap<>();
    }

    public HashMap<Long, String> getCleanedNames() {
        return cleanedNames;
    }

    public HashMap<Long, Vertex> getNames() {
        return names;
    }

    public HashMap<Long, Vertex> getVertexInfo() {
        return vertexInfo;
    }

    /** Add nodes which have names and make cleaned. */
    public void addCleanedNames(Long id, String name) {
        cleanedNames.put(id, GraphDB.cleanString(name));
    }

    /** Add nodes which have names. */
    public void addNames(Long id, double lon, double lat, String name) {
        names.put(id, new Vertex(id, lon, lat, name));
    }

    /** Some methods to get vertices information while parsing. */
    public void addVertex(Long id, double lon, double lat, String name) {
        Vertex temp = new Vertex(id, lon, lat, name);
        vertexInfo.put(id, temp);
    }

    public void addVertex(Long id, double lon, double lat) {
        Vertex temp = new Vertex(id, lon, lat);
        vertexInfo.put(id, temp);
    }

    public void addadjVertices(ArrayList<Long> adjNodes) {
        if (adjNodes.size() == 1) {
            return;
        }

        for (int i = 0; i < adjNodes.size(); i++) {
            if (adj.containsKey(adjNodes.get(i))) {
                HashSet<Long> temp = adj.get(adjNodes.get(i));
                if (i == 0) {
                    temp.add(adjNodes.get(i + 1));
                } else if (i == adjNodes.size() - 1) {
                    temp.add(adjNodes.get(i - 1));
                } else {
                    temp.add(adjNodes.get(i - 1));
                    temp.add(adjNodes.get(i + 1));
                }
            } else {
                HashSet<Long> temp = new HashSet<>();
                if (i == 0) {
                    temp.add(adjNodes.get(i + 1));
                } else if (i == adjNodes.size() - 1) {
                    temp.add(adjNodes.get(i - 1));
                } else {
                    temp.add(adjNodes.get(i - 1));
                    temp.add(adjNodes.get(i + 1));
                }
                adj.put(adjNodes.get(i), temp);
            }
        }
    }

    /** Implement aStar algorithm. */
    public LinkedList<Vertex> aStar(double x1, double y1, double x2, double y2) {
        Vertex startVertex = findVertex(x1, y1);
        Vertex endVertex = findVertex(x2, y2);
        Queue<Node> fringe = new PriorityQueue<>();
        Node initial = new Node(startVertex, null, endVertex);
        fringe.add(initial);
        HashSet<Long> marked = new HashSet<>();
        marked.add(initial.v.getID());

        while (!fringe.peek().isGoal(endVertex)) {
            makeChildren(fringe, endVertex, marked);
        }

        return constructVertexList(fringe);
    }

    /** Make children into fringe element whose priority is lowest. */
    private void makeChildren(Queue<Node> fringe, Vertex endV, HashSet<Long> marked) {
        Node temp = fringe.poll();
        marked.add(temp.v.getID());
        for (Long l : adj.get(temp.v.id)) {
            if (!marked.contains(l)) {
                Node input = new Node(vertexInfo.get(l), temp, endV);
                if (temp.prev == null) {
                    fringe.add(input);
                } else if (!temp.prev.v.id.equals(l)) {
                    fringe.add(input);
                }
            }
        }
    }

    /** Construct Vertex linked list from start vertex to end vertex. */
    private LinkedList<Vertex> constructVertexList(Queue<Node> fringe) {
        Node temp = fringe.peek();
        LinkedList<Vertex> toReturn = new LinkedList<>();
        while (temp.prev != null) {
            toReturn.addFirst(temp.v);
            temp = temp.prev;
        }
        toReturn.addFirst(temp.v);

        return toReturn;
    }

    /** Find vertex with lon, lat. */
    private Vertex findVertex(double x, double y) {
        Vertex toReturn = new Vertex();
        double comparison = Double.MAX_VALUE;
        for (Vertex vertex : vertexInfo.values()) {
            double x2 = vertex.lon;
            double y2 = vertex.lat;
            if (comparison > computeDistance(x, y, x2, y2)) {
                comparison = computeDistance(x, y, x2, y2);
                toReturn = vertex;
            }
        }

        return toReturn;
    }

    /** Compute euclidean distance with lons, lats. */
    private double computeDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0));
    }

    /** Adjust verticesInfo and adj, which means remove all vertices which don't have adjs. */
    public void clean() {
        Set<Long> cl = vertexInfo.keySet();
        Set<Long> use = new HashSet<>();
        for (Long c : cl) {
            use.add(c);
        }
        for (Long c : use) {
            if (!adj.containsKey(c)) {
                vertexInfo.remove(c);
            }
        }
    }
}
