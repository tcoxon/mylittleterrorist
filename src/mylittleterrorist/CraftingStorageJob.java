package mylittleterrorist;

import java.awt.Point;
import java.util.Set;

public class CraftingStorageJob extends InventoryStorageJob {

    public CraftingStorageJob(int x, int y, Item item) {
        super(x, y, item);
    }

    @Override
    protected void success(Game game, Worker worker) {
        if (worker.getHolding() != null) return;

        Set<Item> ingredients = CraftingJob.getCraftingIngredients(game);

        CraftingRecipe recipe = null;
        for (CraftingRecipe r: CraftingRecipe.values()) {
            Set<Item> recIngred = r.getIngredientSet();
            if (ingredients.equals(recIngred)) {
                recipe = r;
                break;
            }
        }
        if (recipe == null) {
            worker.setJob(null);
            return;
        }
        
        worker.setJob(new CraftingJob(equipmentPosition().x,
                equipmentPosition().y, recipe));
    }

    @Override
    public Point requiredPosition() {
        return new Point(equipmentPosition().x, equipmentPosition().y-1);
    }

    @Override
    protected InventorySlot getSlot(Game game, int i) {
        return game.getCrafting()[i];
    }

    @Override
    public String getDescription() {
        return "Placing "+item.name+" on crafting bench";
    }

}
