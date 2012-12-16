package mylittleterrorist;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Worker {
    
    public static enum Style {
        MALE, FEMALE
    }
    
    public static final int MAX_TWEEN_FRAMES = 10;
    
    public final int id;

    protected String name;
    protected IWorkerJob job = null; // TODO
    protected Point pos = null, prevPos = null, targetPos = null;
    protected int tweenFrames = 0;
    protected Style style;
    
    public Worker(int id, Style style) {
        this.id = id;
        this.style = style;
        if (style == Style.FEMALE) name = "Terri";
        else name = "Terry";
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

    public IWorkerJob getJob() {
        return job;
    }

    public void setJob(IWorkerJob job) {
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
                pos = new Point(entrance.x, entrance.y);
                moveTo(map, entrance.x, entrance.y-1);
            }
            
            // TODO: send worker to inventory if carrying something
            // ATM just sends worker to a random tile
            List<Point> floorTiles = new ArrayList<Point>(
                    map.getWidth()*map.getHeight());
            for (int x = 0; x < map.getWidth(); ++x)
            for (int y = 0; y < map.getHeight(); ++y) {
                if (map.get(x,y).isWalkable())
                    floorTiles.add(new Point(x,y));
            }
            if (floorTiles.size() != 0) {
                targetPos = floorTiles.get(
                        new Random().nextInt(floorTiles.size()));
            }
        }
    }
    
    protected void moveTo(GameMap map, int x, int y) {
        map.get(pos.x, pos.y).setOccupied(false);
        map.get(x, y).setOccupied(true);
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
        
        if (job != null) {
            if (!job.requiredPosition().equals(pos)) {
                targetPos = job.requiredPosition();
            } else {
                if (job.getProgress() == 0.0)
                    job.activate(game, this);
                if (job != null) // job.activate can make job null...
                    job.tick(game, this);
            }
        }
        
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

    public void render(Graphics2D g, Spritesheet spritesheet) {
        int row, col;
        switch (style) {
        case MALE:
            row = 1;
            col = 2;
            break;
        case FEMALE:
            row = 1;
            col = 0;
            break;
        default:
            throw new RuntimeException("Unknown worker style");
        }
        // Animate movement:
        if (tweenFrames >= MAX_TWEEN_FRAMES/2)
            ++col;
        g.drawImage(spritesheet.get(col, row), null, 0, 0);
    }

    public String toString() {
        return getName();
    }
}
