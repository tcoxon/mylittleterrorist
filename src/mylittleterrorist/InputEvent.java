package mylittleterrorist;

import java.awt.event.MouseEvent;

public class InputEvent {

    public final int x, y, mouseButton;
    
    public InputEvent(int x, int y, MouseEvent e) {
        this.x = x;
        this.y = y;
        mouseButton = e.getButton();
    }
    
}
