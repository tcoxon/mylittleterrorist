package mylittleterrorist;

import java.awt.*;

public class InventorySlot {

    protected Item item;
    protected int count;
    
    public Item getItem() {
        return item;
    }
    
    public int getCount() {
        return count;
    }
    
    public void set(Item item, int count) {
        setItem(item);
        setCount(count);
    }
    
    public void setItem(Item item) {
        this.item = item;
        count = 1;
    }
    
    public void setCount(int count) {
        this.count = count;
        if (count == 0) item = null;
    }
    
    public void render(Graphics2D g) {
        if (item != null) {
            item.render(g);
            g.setColor(Color.WHITE);
            Font origFont = g.getFont();
            g.setFont(origFont.deriveFont(15.0f).deriveFont(Font.BOLD));
            g.drawString(count > 99 ? "99+" : Integer.toString(count),
                    4, Game.TILE_HEIGHT-4);
            g.setFont(origFont);
        }
    }
    
}