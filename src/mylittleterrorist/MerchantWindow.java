package mylittleterrorist;

import java.awt.Dimension;

import javax.swing.JPanel;

public class MerchantWindow implements IGameWindow {

    public Dimension getSize() {
        return new Dimension(500, 200);
    }

    public String getTitle() {
        return "\"See anything you like?\" ;)";
    }

    public void create(JPanel panel) {
        // TODO
    }

}
