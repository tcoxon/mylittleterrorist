package mylittleterrorist;

import java.awt.*;

public class InventoryStorageJob implements IWorkerJob {

    protected int x, y;
    protected Item item;
    protected boolean activated;
    protected IWorkerJob after;
    
    public InventoryStorageJob(int x, int y, Item item) {
        this(x, y, item, null);
    }
    
    public InventoryStorageJob(int x, int y, Item item, IWorkerJob after) {
        this.x = x;
        this.y = y;
        this.item = item;
        this.after = after;
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
            worker.setHolding(null);
            slot.increase();
            worker.setJob(after);
        } else if (slot.item == null) {
            worker.setHolding(null);
            slot.set(item, 1);
            worker.setJob(after);
        } else {
            worker.setJob(null);
        }
    }

    public String getDescription() {
        return "Placing "+item.name+" into storage";
    }

    public boolean isActivated() {
        return activated;
    }

}
