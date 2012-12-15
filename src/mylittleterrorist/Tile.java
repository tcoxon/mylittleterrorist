package mylittleterrorist;

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
    
}
