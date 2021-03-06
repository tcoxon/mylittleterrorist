package mylittleterrorist;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
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
    protected Item holding;
    protected boolean dead;
    
    public Worker(int id, Style style) {
        this.id = id;
        this.style = style;
        if (style == Style.FEMALE) name = "Terri";
        else name = "Terry";
    }
    
    public boolean isDead() {
        return dead;
    }
    
    public void kill() {
        dead = true;
    }
    
    public String getName() {
        return name + (dead ? " - dead" : "");
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
        this.setTargetPos(null);
    }

    public boolean isOnScreen() {
        return pos != null && !dead;
    }

    protected void updateOffScreen(Game game) {
        GameMap map = game.getMap();
        
        if ((getJob() == null || !getJob().isActivated()) && !isOnScreen()) {
            
            Point entrance = map.getWorkerEntrance();
            if (map.get(entrance.x, entrance.y-1).getKind() ==
                    Tile.Kind.FLOOR) {
                pos = new Point(entrance.x, entrance.y);
                moveTo(map, entrance.x, entrance.y-1);
            }
            
            if (getJob() != null) {
                targetPos = getJob().requiredPosition();
            } else {
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
    }
    
    protected void moveTo(GameMap map, int x, int y) {
        map.get(pos.x, pos.y).setOccupied(false);
        map.get(x, y).setOccupied(true);
        prevPos = pos;
        pos = new Point(x,y);
        tweenFrames = MAX_TWEEN_FRAMES;
    }
    
    protected void moveOffScreen(GameMap map) {
        map.get(pos.x, pos.y).setOccupied(false);
        prevPos = pos;
        pos = null;
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
        if (dead) return;
        
        if (isOnScreen())
            updateOnScreen(game);
        else
            updateOffScreen(game);

        if (job != null && tweenFrames == 0) {
            if (!job.isActivated()) {
                if (!job.requiredPosition().equals(pos)) {
                    targetPos = job.requiredPosition();
                } else {
                    job.activate(game, this);
                }
            } else {
                job.tick(game, this);
            }
        }
        
    }

    public void render(Graphics2D g, Spritesheet spritesheet) {
        if (dead) return;
        
        int row, col;
        // select a sprite for the worker's style
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
        
        // draw main sprite
        g.drawImage(spritesheet.get(col, row), null, 0, 0);
        
        // Draw items being held
        if (holding != null) {
            AffineTransform xfm2 = g.getTransform();
            g.translate(0, 10);
            holding.render(g);
            g.setTransform(xfm2);
        }
        
        // draw job progress bar
        double progress = 0.0;
        if (getJob() != null) progress = getJob().getProgress();
        g.setColor(Color.GREEN);
        g.fillRect(
                (int)(0.2 * Game.TILE_WIDTH),
                (int)(0.6 * Game.TILE_HEIGHT),
                (int)(0.6 * Game.TILE_WIDTH * progress),
                (int)(0.2 * Game.TILE_HEIGHT));
    }

    public String toString() {
        return getName();
    }
    
    public String getJobDescription() {
        if (dead) return "Dead";
        if (job != null) {
            return job.getDescription();
        } else {
            return "Idle";
        }
    }

    public Item getHolding() {
        return holding;
    }

    public void setHolding(Item holding) {
        this.holding = holding;
    }

    public Style getStyle() {
        return style;
    }

}
