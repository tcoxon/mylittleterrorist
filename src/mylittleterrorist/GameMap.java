package mylittleterrorist;

import java.awt.Dimension;

public class GameMap {

    protected static final int DEFAULT_WIDTH = 12, DEFAULT_HEIGHT = 9;

    protected Tile[][] mapData;

    public GameMap() {
        mapData = new Tile[DEFAULT_WIDTH][DEFAULT_HEIGHT];
        
        fill(Tile.Kind.FLOOR);
        wall(Tile.Kind.WALL);
        
        mapData[2][getHeight()-1] = new Tile(Tile.Kind.DOOR, 0);
        mapData[getWidth()-3][getHeight()-1] = new Tile(Tile.Kind.DOOR, 0);
        
        for (int x = 4; x < getWidth()-4; ++x) {
            mapData[x][1] = new Tile(Tile.Kind.CRAFTING_BENCH, 0);
        }
        
        mapData[getWidth()/2][getHeight()-2] = new Tile(Tile.Kind.MERCHANT, 0);
        
        mapData[1][getHeight()/2] = new Tile(Tile.Kind.SPONSOR, 0);
        
        mapData[getWidth()/2][getHeight()/2] = new Tile(Tile.Kind.WORKER, 0);
        mapData[getWidth()-2][2] = new Tile(Tile.Kind.WORKER, 0);
        mapData[getWidth()-2][3] = new Tile(Tile.Kind.WORKER, 0);
    }
    
    public Tile get(int x, int y) {
        return mapData[x][y];
    }

    protected void fill(Tile.Kind kind) {
        for (int x = 0; x < getWidth(); ++x)
        for (int y = 0; y < getHeight(); ++y) {
            mapData[x][y] = new Tile(kind, 0);
        }
    }
    
    protected void wall(Tile.Kind kind) {
        for (int x = 0; x < getWidth(); ++x) {
            mapData[x][0] = new Tile(kind, 0);
            mapData[x][getHeight()-1] = new Tile(kind, 0);
        }
        for (int y = 0; y < getHeight(); ++y) {
            mapData[0][y] = new Tile(kind, 0);
            mapData[getWidth()-1][y] = new Tile(kind, 0);
        }
    }

    public int getWidth() {
        return mapData.length;
    }

    public int getHeight() {
        return mapData[0].length;
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

}
