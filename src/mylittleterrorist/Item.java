package mylittleterrorist;

import java.awt.*;

public enum Item {

    COAL("Coal", "C", 5),
    SULPHUR("Sulphur", "S", 5),
    FERTILIZER("Fertilizer", "F", 5),
    BOMB("Bomb", "B", 20);
    
    public final String name;
    protected final String graphic;
    public final int cost;
    
    private Item(String name, String graphic, int cost) {
        this.name = name;
        this.graphic = graphic;
        this.cost = cost;
    }
    
    public void render(Graphics2D g) {
        // TODO when we have graphics...
        g.setColor(Color.WHITE);
        g.drawString(graphic, Game.TILE_WIDTH/2, Game.TILE_HEIGHT/2);
    }
    
}
