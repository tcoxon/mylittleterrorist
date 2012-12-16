package mylittleterrorist;

import java.awt.*;

public class InventoryStorageJob implements IWorkerJob {

    protected int x, y;
    protected Item item;
    protected boolean activated;
    
    public InventoryStorageJob(int x, int y, Item item) {
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
            worker.setHolding(null);
            slot.increase();
        } else if (slot.item == null) {
            worker.setHolding(null);
            slot.set(item, 1);
        }
        worker.setJob(null);
    }

    public String getDescription() {
        return "Placing "+item.name+" into storage";
    }

    public boolean isActivated() {
        return activated;
    }

}
