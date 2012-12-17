package mylittleterrorist;

import java.awt.Graphics2D;

public class Tile implements AStar.ITile {

    public static enum Kind {
        FLOOR, WALL, DOOR, CRAFTING_BENCH, INVENTORY,
        SPONSOR, MERCHANT
    }

    protected Kind kind;
    protected int extraData;
    protected boolean occupied;

    public Tile(Kind kind, int extraData) {
        this.kind = kind;
        this.extraData = extraData;
        this.occupied = false;
    }
    
    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isWalkable() {
        return kind == Kind.FLOOR && !occupied;
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

    public void render(Graphics2D g, Game game, Spritesheet spritesheet,
            int frame) {
        
        switch (kind) {
        case FLOOR:
            g.drawImage(spritesheet.get(extraData, 3), null, 0, 0);
            break;
        case WALL:
            if (extraData >= 4)
                g.drawImage(spritesheet.get(4 + extraData % 4, 4), null, 0, 0);
            else
                g.drawImage(spritesheet.get(4 + extraData % 4, 3), null, 0, 0);
            break;
        case DOOR:
            g.drawImage(spritesheet.get(0, 4), null, 0, 0);
            break;
        case CRAFTING_BENCH:
            g.drawImage(spritesheet.get(2, 4), null, 0, 0);
            break;
        case INVENTORY:
            g.drawImage(spritesheet.get(1, 4), null, 0, 0);
            break;
        case SPONSOR:
            g.drawImage(spritesheet.get(0, 3), null, 0, 0); // FLOOR
            g.drawImage(spritesheet.get((frame>>2)%4
                    + (game.hasSponsors() ? 4 : 0), 2), null, 0, 0);
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
