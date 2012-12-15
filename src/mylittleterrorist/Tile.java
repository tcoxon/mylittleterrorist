package mylittleterrorist;

public class Tile {

    public static enum Kind {
        FLOOR, WALL, DOOR
    }

    protected Kind kind;
    protected int extraData;

    public Tile(Kind kind, int extraData) {
        this.kind = kind;
        this.extraData = extraData;
    }

    public boolean isWalkable() {
        switch (kind) {
        case FLOOR:
        case DOOR:
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
