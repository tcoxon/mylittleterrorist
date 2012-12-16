package mylittleterrorist;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class MainApplet extends Applet {
    private static final long serialVersionUID = 1L;

    // Milliseconds between each update
    public static final long UPDATE_PERIOD = 32;

    protected Timer timer;
    protected MapPanel mapPanel;
    protected JPanel currentPanel;
    protected StatusPanel statusPanel;
    
    protected Game game;
    
    public void showPanel(JPanel panel) {
        remove(currentPanel);
        currentPanel = panel;
        add(panel, BorderLayout.CENTER);
        validate();
    }
    
    public void showMapPanel() {
        showPanel(mapPanel);
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                mapPanel.getPreferredSize().width +
                    statusPanel.getPreferredSize().width,
                mapPanel.getPreferredSize().height);
    }

    @Override
    public void init() {
        super.init();

        game = new Game(this);
        
        mapPanel = new MapPanel(game);
        currentPanel = mapPanel;
        setLayout(new BorderLayout());
        add(mapPanel, BorderLayout.CENTER);
        
        statusPanel = new StatusPanel(game, this);
        add(statusPanel, BorderLayout.EAST);
        
        resize(getPreferredSize());
        
    }

    protected void updateGame() {
        if (currentPanel == mapPanel) {
            game.tick();
            mapPanel.repaint();
        }
        statusPanel.update();
    }

    @Override
    public void start() {
        super.start();
        timer = new Timer(true);
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        };
        timer.schedule(updateTask, 0, UPDATE_PERIOD);
    }

    @Override
    public void stop() {
        timer.cancel();
        timer.purge();
        timer = null;
        super.stop();
    }

}
