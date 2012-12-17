package mylittleterrorist;

import java.awt.Point;
import java.util.EnumSet;
import java.util.Set;

public class CraftingJob implements IWorkerJob {

    protected static final int MAX_PROGRESS = 300;
    
    protected int x, y;
    protected CraftingRecipe recipe;
    protected boolean activated;
    protected int progress;
    
    public CraftingJob(int x, int y, CraftingRecipe recipe) {
        this.x = x;
        this.y = y;
        this.recipe = recipe;
    }
    
    public static Set<Item> getCraftingIngredients(Game game) {
        Set<Item> ingredients = EnumSet.noneOf(Item.class);
        for (InventorySlot craftSlot: game.getCrafting()) {
            if (craftSlot.getItem() != null)
                ingredients.add(craftSlot.getItem());
        }
        return ingredients;
    }

    public Point equipmentPosition() {
        return new Point(x,y);
    }

    public Point requiredPosition() {
        return new Point(x, y-1);
    }

    public void activate(Game game, Worker worker) {
        activated = true;
    }

    public double getProgress() {
        return progress / (0.0 + MAX_PROGRESS);
    }

    public void tick(Game game, Worker worker) {
        ++progress;
        
        if (!getCraftingIngredients(game).equals(recipe.getIngredientSet())) {
            worker.setJob(null);
            return;
        }
        
        if (worker.getHolding() != null) {
            worker.setJob(null);
            return;
        }
        
        if (progress >= MAX_PROGRESS) {
            progress = MAX_PROGRESS;
            
            worker.setHolding(recipe.product);
            for (InventorySlot slot: game.getCrafting()) {
                if (slot.getItem() != null)
                    slot.decrease();
            }
            
            worker.setJob(null);
            
        }
    }

    public String getDescription() {
        return "Crafting a "+recipe.product.name;
    }

    public boolean isActivated() {
        return activated;
    }

}
