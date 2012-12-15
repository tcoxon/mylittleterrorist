package mylittleterrorist;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {
    
    public final int id;

    protected String name = "Terry";
    protected Object job = null; // TODO
    protected boolean onScreen = false;
    protected Point targetPos = null;
    
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
        return onScreen;
    }

    public void setOnScreen(boolean onScreen) {
        this.onScreen = onScreen;
    }

    public void updateOffScreen(Game game) {
        GameMap map = game.getMap();
        
        if (getJob() == null && !isOnScreen()) {
            
            Point entrance = map.getWorkerEntrance();
            if (map.get(entrance.x, entrance.y-1).getKind() ==
                    Tile.Kind.FLOOR) {
                map.set(entrance.x, entrance.y-1,
                        new Tile(Tile.Kind.WORKER, id));
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
                onScreen = true;
            }
        }
    }

    public void updateOnScreen(Game game, int x, int y) {
        if (targetPos != null) {
            // TODO A* path-finding
        }
    }

}
