package mylittleterrorist;

import java.awt.event.MouseEvent;

public class InputEvent {
    
    enum Kind { CLICK, JOB_CANCEL, SELECT_WORKER }

    public final Kind kind;
    public final int x, y, arg;
    
    public InputEvent(int x, int y, MouseEvent e) {
        this.kind = Kind.CLICK;
        this.x = x;
        this.y = y;
        arg = e.getButton();
    }
    
    public InputEvent(Kind kind) {
        this.kind = kind;
        this.x = 0;
        this.y = 0;
        this.arg = 0;
    }
    
    public InputEvent(Kind kind, int arg) {
        this.kind = kind;
        this.x = 0;
        this.y = 0;
        this.arg = arg;
    }
    
}
