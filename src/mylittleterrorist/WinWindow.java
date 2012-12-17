package mylittleterrorist;

import javax.swing.*;

public class WinWindow implements IGameWindow {

    public String getTitle() {
        return "Game Over";
    }

    public void create(Game game, JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel congrats = new JLabel("<html><br/>Congratulations: You WIN!<br/></html>");
        congrats.setFont(congrats.getFont().deriveFont(24.0f));
        congrats.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(congrats);
        
        JLabel text = new JLabel(
                "<html><br/><br/>"+
                "You maxed out your renown.<br/><br/>"+
                "Everyone in the world has heard of your terrorist cell "+
                "and what you want...<br/><br/>"+
                "... Money and fame, that is...</html>");
        
        text.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(text);
    }

}
