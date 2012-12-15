package mylittleterrorist;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Game {

    public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32;

    protected GameMap map;
    
    public Game() {
        map = new GameMap();
    }

    public Dimension getMapSize() {
        return map.getSize();
    }

    public Dimension getPixelSize() {
        Dimension mapSize = map.getSize();
        return new Dimension(mapSize.width * TILE_WIDTH, mapSize.height
                * TILE_HEIGHT);
    }

    public synchronized void tick() {
        // TODO
    }
    
    protected void renderTile(Graphics2D g, Tile tile) {
        switch (tile.getKind()) {
        case FLOOR:
            // Nothing for now... TODO
            break;
        case WALL:
            g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
            g.drawString("W", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case DOOR:
            g.drawString("D", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case CRAFTING_BENCH:
            g.drawRect(0, 4, TILE_WIDTH-1, TILE_HEIGHT-5);
            g.drawString("C", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case INVENTORY:
            g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
            g.drawString("I", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case WORKER:
            g.drawString("T", TILE_WIDTH/2, TILE_HEIGHT/2);
            g.drawOval(1, 1, TILE_WIDTH-2, TILE_HEIGHT-2);
            break;
        case SPONSOR:
            g.drawString("S", TILE_WIDTH/2, TILE_HEIGHT/2);
            g.drawOval(1, 1, TILE_WIDTH-2, TILE_HEIGHT-2);
            break;
        case MERCHANT:
            g.drawString("M", TILE_WIDTH/2, TILE_HEIGHT/2);
            g.drawOval(1, 1, TILE_WIDTH-2, TILE_HEIGHT-2);
            break;
        default:
            throw new RuntimeException("Unhandled tile kind");
        }
    }

    public synchronized void render(Graphics2D g) {
        // TODO use sprites
        for (int x = 0; x < map.getWidth(); ++x)
        for (int y = 0; y < map.getHeight(); ++y) {
            AffineTransform origXfm = g.getTransform();
            Tile tile = map.get(x,y);
            
            g.translate(x * TILE_WIDTH, y * TILE_HEIGHT);
            renderTile(g, tile);
            
            g.setTransform(origXfm);
        }
    }

}
