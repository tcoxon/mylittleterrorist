package mylittleterrorist;

import java.awt.*;

public class InventoryRetrieveJob implements IWorkerJob {

    protected int x, y;
    protected Item item;
    protected boolean activated;
    
    public InventoryRetrieveJob(int x, int y, Item item) {
        this.x = x;
        this.y = y;
        this.item = item;
    }

    public Point equipmentPosition() {
        return new Point(x,y);
    }

    public Point requiredPosition() {
        return new Point(x, y+1);
    }

    public void activate(Game game, Worker worker) {
        activated = true;
    }

    public double getProgress() {
        return 0;
    }

    public void tick(Game game, Worker worker) {
        GameMap map = game.getMap();
        Tile t = map.get(x,y);
        InventorySlot slot = game.getInventory()[t.getExtraData()];
        if (slot.item == item) {
            worker.setHolding(item);
            slot.decrease();
        }
        worker.setJob(null);
    }

    public String getDescription() {
        return "Retrieving "+item.name+" from storage";
    }

    public boolean isActivated() {
        return activated;
    }

}
