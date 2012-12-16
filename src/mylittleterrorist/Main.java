package mylittleterrorist;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;

    protected MainApplet applet;
    
    public Main() {
        super("My Little Terrorist");
        setLayout(new BorderLayout());
        
        applet = new MainApplet();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                applet.stop();
                applet.destroy();
            }
        });
        
        add(applet, BorderLayout.CENTER);
        applet.init();
        applet.start();
        
        pack();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        main.setVisible(true);
    }

}
