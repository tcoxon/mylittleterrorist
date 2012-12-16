package mylittleterrorist;

import java.awt.Graphics2D;

public class Tile implements AStar.ITile {

    public static enum Kind {
        FLOOR, WALL, DOOR, CRAFTING_BENCH, INVENTORY,
        WORKER, SPONSOR, MERCHANT
    }

    protected Kind kind;
    protected int extraData;

    public Tile(Kind kind, int extraData) {
        this.kind = kind;
        this.extraData = extraData;
    }
    
    public boolean isFloor() {
        switch (kind) {
        case FLOOR:
            return true;
        default:
            return false;
        }
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public int getExtraData() {
        return extraData;
    }

    public void setExtraData(int extraData) {
        this.extraData = extraData;
    }

    public void render(Graphics2D g, Spritesheet spritesheet, int frame) {
        switch (kind) {
        case WORKER:
            // Just draw floor here. The actual worker is rendered by the
            // Worker class.
        case FLOOR:
            g.drawImage(spritesheet.get(0, 3), null, 0, 0);
            break;
        case WALL:
            g.drawImage(spritesheet.get(4, 3), null, 0, 0);
            break;
        case DOOR:
            g.drawImage(spritesheet.get(0, 4), null, 0, 0);
            break;
        case CRAFTING_BENCH:
            g.drawImage(spritesheet.get(3, 4), null, 0, 0);
            break;
        case INVENTORY:
            // TODO draw the item it holds on top
            g.drawImage(spritesheet.get(2, 4), null, 0, 0);
            break;
        case SPONSOR:
            // TODO display shadowy figure in screen
            g.drawImage(spritesheet.get(0, 3), null, 0, 0); // FLOOR
            g.drawImage(spritesheet.get((frame>>2)%4, 2), null, 0, 0);
            break;
        case MERCHANT:
            g.drawImage(spritesheet.get(0, 3), null, 0, 0); // FLOOR
            g.drawImage(spritesheet.get(4, 1), null, 0, 0);
            break;
        default:
            throw new RuntimeException("Unhandled tile kind");
        }
    }
    
}
