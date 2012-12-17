package mylittleterrorist;

import javax.swing.*;

public class GameOverWindow implements IGameWindow {

    public String getTitle() {
        return "Game Over";
    }

    public void create(Game game, JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel congrats = new JLabel("<html><br/>Game Over<br/></html>");
        congrats.setFont(congrats.getFont().deriveFont(24.0f));
        congrats.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(congrats);
        
        JLabel text = new JLabel(
                "<html><br/><br/>"+
                "Your notoriety reached rock bottom.<br/><br/>"+
                "Nobody knows who you are and nobody gives a fuck.</html>");
        
        text.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(text);
    }

}
