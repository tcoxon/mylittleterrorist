package mylittleterrorist;

import java.awt.*;
import java.io.IOException;

public enum Item {

    COAL("Coal", 5, 1),
    SULPHUR("Sulphur", 5, 0),
    FERTILIZER("Fertilizer", 5, 2),
    BOMB("Bomb", 50, 3),
    AK47("AK-47", 50, 4),
    PILOTS_LICENSE("Pilot's License", 500, 5),
    PLUTONIUM("Plutonium", 200, 6),
    DIRTY_BOMB("Dirty Bomb", 700, 7)
    ;
    
    public final String name;
    public final int cost, col;
    
    private Item(String name, int cost, int col) {
        this.name = name;
        this.cost = cost;
        this.col = col;
    }
    
    public void render(Graphics2D g) {
        try {
            g.drawImage(
                    Spritesheet.get("/sprites/spritesheet.png", 52, 52).
                            get(col, 5),
                    null, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
