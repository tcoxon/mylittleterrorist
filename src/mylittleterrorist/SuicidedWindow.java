package mylittleterrorist;

import javax.swing.*;

public class SuicidedWindow implements IGameWindow {

    public String getTitle() {
        return "Game Over";
    }

    public void create(Game game, JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel congrats = new JLabel("<html><br/>You really hate life, don't you?<br/></html>");
        congrats.setFont(congrats.getFont().deriveFont(24.0f));
        congrats.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(congrats);
        
        JLabel text = new JLabel(
                "<html><br/><br/>"+
                "You suicided every single one of your terrorist cell members.<br/><br/>"+
                "Who will execute your orders now?</html>");
        
        text.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(text);
    }

}
