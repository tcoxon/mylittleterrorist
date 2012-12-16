package mylittleterrorist;

import javax.swing.JPanel;

public interface IGameWindow {

    public String getTitle();
    public void create(Game game, JPanel panel);
    
}
