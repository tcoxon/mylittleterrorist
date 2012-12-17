package mylittleterrorist;

import java.awt.event.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class HelpWindow implements IGameWindow {

    public String getTitle() {
        return "Help & Controls";
    }

    public void create(final Game game, JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel congrats = new JLabel("<html><br/>Controls<br/></html>");
        congrats.setFont(congrats.getFont().deriveFont(24.0f));
        congrats.setHorizontalAlignment(SwingConstants.CENTER);
        congrats.setAlignmentX(0.5f);
        panel.add(congrats);
        
        try {
            ImagePanel img = new ImagePanel(
                    ImageIO.read(HelpWindow.class.getResource("/images/controls.png")));
            img.setAlignmentX(0.5f);
            panel.add(img);
        } catch (IOException e) {
            e.printStackTrace();
            JLabel error = new JLabel("Sorry, there was an error reading the controls image.");
            error.setAlignmentX(0.5f);
            panel.add(error);
        }
        
        JButton okBtn = new JButton("OK");
        okBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                game.closeWindow();
            }
            
        });
        okBtn.setAlignmentX(0.5f);

        panel.add(okBtn);
    }

}
