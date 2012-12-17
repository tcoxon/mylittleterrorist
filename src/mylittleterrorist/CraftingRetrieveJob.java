package mylittleterrorist;

import java.awt.Point;

public class CraftingRetrieveJob extends InventoryRetrieveJob {

    public CraftingRetrieveJob(int x, int y, Item item) {
        super(x, y, item);
    }

    @Override
    public Point requiredPosition() {
        return new Point(equipmentPosition().x, equipmentPosition().y-1);
    }

    @Override
    public String getDescription() {
        return "Retrieving "+item.name+" from crafting bench";
    }

    @Override
    protected InventorySlot getSlot(Game game, int i) {
        return game.getCrafting()[i];
    }

}
