package mylittleterrorist;

import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

public class MerchantWindow implements IGameWindow {

    public String getTitle() {
        return "Arms Dealer";
    }

    public void create(JPanel panel) {
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(5,5));
        
        try {
            ImagePanel face = new ImagePanel(
                    Spritesheet.get("/sprites/faces.png", 128, 128).get(0,0));
            panel.add(face, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        panel.add(mainPanel, BorderLayout.CENTER);
        
        JLabel dialogue = new JLabel("\"See anything you like?\"");
        mainPanel.add(dialogue, BorderLayout.NORTH);
    }

}
