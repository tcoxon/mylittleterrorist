package mylittleterrorist;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {
    
    public static final int MAX_TWEEN_FRAMES = 10;
    
    public final int id;

    protected String name = "Terry";
    protected Object job = null; // TODO
    protected Point pos = null, prevPos = null, targetPos = null;
    protected int tweenFrames = 0;
    
    public Worker(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name + " #" + Integer.toString(id);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(Point targetPos) {
        this.targetPos = targetPos;
    }

    public Object getJob() {
        return job;
    }

    public void setJob(Object job) {
        this.job = job;
    }

    public boolean isOnScreen() {
        return pos != null;
    }

    protected void updateOffScreen(Game game) {
        GameMap map = game.getMap();
        
        if (getJob() == null && !isOnScreen()) {
            
            Point entrance = map.getWorkerEntrance();
            if (map.get(entrance.x, entrance.y-1).getKind() ==
                    Tile.Kind.FLOOR) {
                map.set(entrance.x, entrance.y-1,
                        new Tile(Tile.Kind.WORKER, id));
                prevPos = new Point(entrance.x, entrance.y);
                pos = new Point(entrance.x, entrance.y-1);
                tweenFrames = MAX_TWEEN_FRAMES;
            }
            
            // TODO: send worker to inventory if carrying something
            // ATM just sends worker to a random tile
            List<Point> floorTiles = new ArrayList<Point>(
                    map.getWidth()*map.getHeight());
            for (int x = 0; x < map.getWidth(); ++x)
            for (int y = 0; y < map.getHeight(); ++y) {
                if (map.get(x,y).isFloor())
                    floorTiles.add(new Point(x,y));
            }
            if (floorTiles.size() != 0) {
                targetPos = floorTiles.get(
                        new Random().nextInt(floorTiles.size()));
            }
        }
    }
    
    protected void moveTo(GameMap map, int x, int y) {
        Tile workerTile = map.get(pos.x, pos.y);
        map.set(x, y, workerTile);
        map.set(pos.x, pos.y, new Tile(Tile.Kind.FLOOR, 0));
        prevPos = pos;
        pos = new Point(x,y);
        tweenFrames = MAX_TWEEN_FRAMES;
    }

    public Point getPrevPos() {
        return prevPos;
    }

    public int getTweenFrames() {
        return tweenFrames;
    }

    public void decTweenFrames() {
        --tweenFrames;
    }

    public Point getPos() {
        return pos;
    }

    protected void updateOnScreen(Game game) {
        GameMap map = game.getMap();
        
        // Don't do anything else while performing animated movement between
        // positions
        if (tweenFrames != 0) return;
        
        if (targetPos != null) {
            Point next = AStar.nextStep(map, pos, targetPos);
            if (next != null) {
                moveTo(map, next.x, next.y);
            }
            if (pos.equals(targetPos))
                targetPos = null;
        }
    }
    
    public void update(Game game) {
        if (isOnScreen())
            updateOnScreen(game);
        else
            updateOffScreen(game);
    }

}
