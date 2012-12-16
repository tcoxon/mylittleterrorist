package mylittleterrorist;

import java.awt.Point;
import java.util.Comparator;

public class Util {

    public static class PointComparator implements Comparator<Point> {
        public int compare(Point arg0, Point arg1) {
            if (arg0.x == arg1.x) return arg0.y - arg1.y;
            return arg0.x - arg1.x;
        }
    }
    public static final PointComparator POINTCMP = new PointComparator();
    
}
