package mylittleterrorist;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    protected BufferedImage img;
    
    public ImagePanel(BufferedImage img) {
        this.img = img;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(img.getWidth(), img.getHeight());
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        g.drawImage(img, 0, 0, this);
    }

}
