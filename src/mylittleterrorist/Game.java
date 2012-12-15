package mylittleterrorist;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game {

    public static final int TILE_WIDTH = 52, TILE_HEIGHT = 52;

    protected List<InputEvent> bufferedEvents;
    protected GameMap map;
    
    protected List<Worker> workerData;
    protected int selectedWorker = 0;
    
    protected Applet applet;
    protected JPanel currentWindow;
    protected Action currentWindowCloseAction;
    
    public Game(Applet a) {
        applet = a;
        currentWindowCloseAction = new Action() {

            public void actionPerformed(ActionEvent e) {
                applet.remove(currentWindow);
                currentWindow = null;
            }

            public void addPropertyChangeListener(
                    PropertyChangeListener listener) { }
            public Object getValue(String key) { return null; }
            public boolean isEnabled() {
                return true;
            }
            public void putValue(String key, Object value) { }
            public void removePropertyChangeListener(
                    PropertyChangeListener listener) { }
            public void setEnabled(boolean b) { }
        };
        
        map = new GameMap();
        
        bufferedEvents = new ArrayList<InputEvent>(200);
        
        workerData = new ArrayList<Worker>(20);
        addWorker();
        addWorker();
        addWorker();
    }
    
    public void showWindow(IGameWindow w) {
        if (currentWindow != null) applet.remove(currentWindow);
        
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setMinimumSize(w.getSize());
        
        JLabel label = new JLabel(w.getTitle());
        panel.add(label, BorderLayout.NORTH);
        
        JPanel inner = new JPanel();
        w.create(inner);
        panel.add(inner, BorderLayout.CENTER);
        
        JPanel footer = new JPanel(new BorderLayout());
        JButton close = new JButton("Close");
        close.setAction(currentWindowCloseAction);
        footer.add(close, BorderLayout.EAST);
        panel.add(footer, BorderLayout.SOUTH);
        
        panel.setBounds(
                (applet.getWidth()-w.getSize().width)/2,
                (applet.getHeight()-w.getSize().height)/2,
                w.getSize().width,
                w.getSize().height);
        
        currentWindow = panel;
        applet.add(panel);
    }
    
    public void addWorker() {
        Worker worker = new Worker(workerData.size()+1);
        workerData.add(worker);
    }

    public Dimension getMapSize() {
        return map.getSize();
    }

    public Dimension getPixelSize() {
        Dimension mapSize = map.getSize();
        return new Dimension(mapSize.width * TILE_WIDTH, mapSize.height
                * TILE_HEIGHT);
    }

    protected synchronized void handleEvents() {
        for (InputEvent ie: bufferedEvents) {
            handleEvent(ie);
        }
        bufferedEvents.clear();
    }
    
    protected void handleEvent(InputEvent e) {
        Tile t = map.get(e.x, e.y);
        
        if (e.mouseButton == 1) {
            // (Un)select workers to command
            if (t.getKind() == Tile.Kind.WORKER) {
                if (selectedWorker != t.getExtraData()) {
                    selectedWorker = t.getExtraData();
                } else {
                    selectedWorker = 0;
                }
            } else {
                selectedWorker = 0;
            }
        }
        
        if (e.mouseButton == 3 && selectedWorker != 0) {
            Worker worker = workerData.get(selectedWorker-1);
            switch (t.getKind()) {
            case MERCHANT:
                worker.setJob(new MerchantJob(e.x, e.y));
                return;
            }
            
            int y = e.y, x = e.x;
            switch (t.getKind()) {
            case CRAFTING_BENCH:
            case INVENTORY:
            case SPONSOR:
                y += 1;
                break;
            case DOOR:
                y -= 1;
                break;
            }
            // TODO if the target tile contains a usable block, make worker use
            // it
            workerData.get(selectedWorker-1).setTargetPos(new Point(x, y));
        }
    }
    
    public GameMap getMap() {
        return map;
    }

    public synchronized void tick() {
        handleEvents();
        updateWorkers();
    }
    
    protected void updateWorkers() {
        for (Worker worker: workerData) {
            worker.update(this);
        }
    }
    
    protected void drawWorker(Graphics2D g, Tile tile) {
        Worker worker = workerData.get(tile.getExtraData()-1);
        
        int tween = worker.getTweenFrames();
        if (tween != 0) {
            worker.decTweenFrames();
            int x = (worker.getPrevPos().x-worker.getPos().x) *
                    tween * TILE_WIDTH / Worker.MAX_TWEEN_FRAMES,
                y = (worker.getPrevPos().y-worker.getPos().y) *
                    tween * TILE_HEIGHT / Worker.MAX_TWEEN_FRAMES;
            g.translate(x, y);
        }
        
        g.drawString("T", TILE_WIDTH/2, TILE_HEIGHT/2);
        g.drawOval(1, 1, TILE_WIDTH-3, TILE_HEIGHT-3);
    }
    
    protected void renderTile(Graphics2D g, Tile tile) {
        g.setColor(Color.BLACK);
        switch (tile.getKind()) {
        case FLOOR:
            // Nothing for now... TODO
            break;
        case WALL:
            g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
            g.drawString("W", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case DOOR:
            g.drawString("D", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case CRAFTING_BENCH:
            g.drawRect(0, 4, TILE_WIDTH-1, TILE_HEIGHT-9);
            g.drawString("C", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case INVENTORY:
            g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
            g.drawString("I", TILE_WIDTH/2, TILE_HEIGHT/2);
            break;
        case WORKER:
            drawWorker(g, tile);
            break;
        case SPONSOR:
            g.drawString("S", TILE_WIDTH/2, TILE_HEIGHT/2);
            g.drawOval(1, 1, TILE_WIDTH-3, TILE_HEIGHT-3);
            break;
        case MERCHANT:
            g.drawString("M", TILE_WIDTH/2, TILE_HEIGHT/2);
            g.drawOval(1, 1, TILE_WIDTH-3, TILE_HEIGHT-3);
            break;
        default:
            throw new RuntimeException("Unhandled tile kind");
        }
    }

    public synchronized void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getPixelSize().width, getPixelSize().height);
        
        // TODO use sprites
        for (int x = 0; x < map.getWidth(); ++x)
        for (int y = 0; y < map.getHeight(); ++y) {
            AffineTransform origXfm = g.getTransform();
            Tile tile = map.get(x,y);
            
            g.translate(x * TILE_WIDTH, y * TILE_HEIGHT);
            renderTile(g, tile);
            
            if (tile.getKind() == Tile.Kind.WORKER &&
                    selectedWorker == tile.getExtraData()) {
                g.setColor(Color.GREEN);
                g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
            }
            
            if (selectedWorker != 0) {
                Worker worker = workerData.get(selectedWorker-1);
                Point target = worker.getTargetPos();
                if (target != null && target.x == x && target.y == y) {
                    g.setColor(Color.YELLOW);
                    g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
                }
            }
            
            g.setTransform(origXfm);
        }
    }
    
    public synchronized void inputEvent(InputEvent ie) {
        bufferedEvents.add(ie);
    }
    
    public synchronized void inputEvent(int x, int y, MouseEvent e) {
        inputEvent(new InputEvent(x,y,e));
    }

}
