package mylittleterrorist;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

public class MainApplet extends Applet {
    private static final long serialVersionUID = 1L;

    // Milliseconds between each update
    public static final long UPDATE_PERIOD = 32;

    protected Timer timer;
    protected MapPanel mapPanel;
    
    protected Game game;
    
    @Override
    public void destroy() {
        super.destroy();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return mapPanel.getPreferredSize();
    }

    @Override
    public void init() {
        super.init();

        game = new Game(this);
        mapPanel = new MapPanel(game);
        setLayout(new BorderLayout());
        add(mapPanel, BorderLayout.CENTER);
        
        resize(getPreferredSize());
        
    }

    protected void updateGame() {
        game.tick();
        mapPanel.repaint();
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
