package mylittleterrorist;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface IGameWindow {

    public Dimension getSize();
    public String getTitle();
    public void create(JPanel panel);
    
}
