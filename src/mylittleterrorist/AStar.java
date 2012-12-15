package mylittleterrorist;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AStar {
    public static interface ITile {
        public boolean isFloor();
    }
    
    public static interface IMap {
        public ITile get(int x, int y);
        public int getWidth();
        public int getHeight();
    }
    
    protected static class PointComparator implements Comparator<Point> {
        public int compare(Point arg0, Point arg1) {
            if (arg0.x == arg1.x) return arg0.y - arg1.y;
            return arg0.x - arg1.x;
        }
    }
    protected static final PointComparator POINTCMP = new PointComparator();
    
    protected class DistanceComparator implements Comparator<Point> {
        public int compare(Point o1, Point o2) {
            double s1 = fScore.get(o1),
                   s2 = fScore.get(o2);
            if (s1 < s2) return -1;
            if (s1 == s2) return 0;
            return 1;
        }
    }
    protected final DistanceComparator DISTCMP = new DistanceComparator();
    
    protected Map<Point, Double> gScore = new TreeMap<Point, Double>(POINTCMP),
                                 fScore = new TreeMap<Point, Double>(POINTCMP);
    protected Map<Point, Point> cameFrom = new TreeMap<Point, Point>(POINTCMP);
    protected Set<Point> closedSet = new TreeSet<Point>(POINTCMP);
    protected Queue<Point> openSet = new PriorityQueue<Point>(110, DISTCMP);
    protected IMap map;
    protected Point from, to;
    protected AStar(IMap map, Point from, Point to) {
        this.map = map;
        this.from = from;
        this.to = to;
    }
    
    protected double heuristicDistance(Point pos) {
        // Manhattan distance heuristic
        return Math.abs(to.x - pos.x) + Math.abs(to.y - pos.y);
    }
    
    protected void updateFScore(Point pos) {
        fScore.put(pos, gScore.get(pos) + heuristicDistance(pos));
    }
    
    protected List<Point> getNeighbors(Point pos) {
        List<Point> result = new ArrayList<Point>();
        if (pos.x > 0) result.add(new Point(pos.x-1, pos.y));
        if (pos.y > 0) result.add(new Point(pos.x, pos.y-1));
        if (pos.x < map.getWidth()-1)
            result.add(new Point(pos.x+1, pos.y));
        if (pos.y < map.getHeight()-1)
            result.add(new Point(pos.x, pos.y+1));
        return result;
    }
    
    protected List<Point> solve() {
        /* See this page for the algorithm:
         * http://en.wikipedia.org/wiki/A*_search_algorithm
         */
        openSet.add(from);
        gScore.put(from, 0.0);
        updateFScore(from);
        
        while (!openSet.isEmpty()) {
            Point current = openSet.remove();
            
            if (current.equals(to))
                return reconstructPath();
            
            closedSet.add(current);
            
            for (Point neighbor: getNeighbors(current)) {
                
                if (closedSet.contains(neighbor))
                    continue;
                if (!map.get(neighbor.x, neighbor.y).isFloor())
                    continue;
                
                double dist = current.distance(neighbor);
                double g = gScore.get(current) + dist;
                
                if (!openSet.contains(neighbor) || g < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, g);
                    updateFScore(neighbor);
                    openSet.add(neighbor);
                }
            }
        }
        return null;
    }
    
    protected Point nextStep() {
        List<Point> path = solve();
        if (path == null || path.size() == 0) return null;
        return path.get(0);
    }
    
    protected List<Point> reconstructPath() {
        List<Point> result = new ArrayList<Point>();
        Point current = to;
        while (!current.equals(from)) {
            result.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(result);
        return result;
    }

    public static Point nextStep(IMap map, Point from, Point to) {
        return new AStar(map, from, to).nextStep();
    }

}
