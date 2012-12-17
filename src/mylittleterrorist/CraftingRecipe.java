package mylittleterrorist;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public enum CraftingRecipe {

    BOMB(Item.BOMB, new Item[]{Item.COAL, Item.SULPHUR, Item.FERTILIZER}),
    DIRTY_BOMB(Item.DIRTY_BOMB, new Item[]{Item.BOMB, Item.PLUTONIUM}),
    
    ;
    
    public final Item product;
    public final Item[] ingredients;
    
    private CraftingRecipe(Item product, Item[] ingredients) {
        this.product = product;
        this.ingredients = ingredients;
    }
    
    public Set<Item> getIngredientSet() {
        return EnumSet.copyOf(Arrays.asList(ingredients));
    }
    
}
