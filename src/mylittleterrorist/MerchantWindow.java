package mylittleterrorist;

import java.awt.Dimension;

import javax.swing.JPanel;

public class MerchantWindow implements IGameWindow {

    public Dimension getSize() {
        return new Dimension(500, 400);
    }

    public String getTitle() {
        return "Arms Dealer";
    }

    public void create(JPanel panel) {
        // TODO
    }

}
