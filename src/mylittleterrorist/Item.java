package mylittleterrorist;

import java.awt.*;

public enum Item {

    COAL("C", 5),
    SULPHUR("S", 5),
    FERTILIZER("F", 5),
    BOMB("B", 20);
    
    protected final String graphic;
    public final int cost;
    
    private Item(String graphic, int cost) {
        this.graphic = graphic;
        this.cost = cost;
    }
    
    public void render(Graphics2D g) {
        // TODO when we have graphics...
        g.setColor(Color.WHITE);
        g.drawString(graphic, Game.TILE_WIDTH/2, Game.TILE_HEIGHT/2);
    }
    
}
