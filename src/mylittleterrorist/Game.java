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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mylittleterrorist.Worker.Style;

public class Game {

    public static final int TILE_WIDTH = 52, TILE_HEIGHT = 52;

    protected List<InputEvent> bufferedEvents;

    protected GameMap map;
    protected List<Worker> workerData;
    protected int selectedWorker = 0;
    protected int money;
    
    protected Spritesheet spritesheet;
    
    protected Applet applet;
    protected JPanel currentWindow;
    protected Action currentWindowCloseAction;
    
    protected int frame;
    
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
        
        try {
            spritesheet = new Spritesheet("/sprites/spritesheet.png",
                TILE_WIDTH, TILE_HEIGHT);
        } catch (IOException e) {
            System.err.println("Unable to load spritesheet:");
            e.printStackTrace();
            System.exit(1);
        }
        
        map = new GameMap();
        
        bufferedEvents = new ArrayList<InputEvent>(200);
        
        workerData = new ArrayList<Worker>(20);
        addWorker(Worker.Style.MALE);
        addWorker(Worker.Style.MALE);
        addWorker(Worker.Style.FEMALE);
        
        frame = 0;
    }
    
    public Worker[] getWorkers() {
        return workerData.toArray(new Worker[0]);
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
    
    public void addWorker(Style style) {
        Worker worker = new Worker(workerData.size()+1, style);
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
            boolean clickedOne = false;
            for (Worker worker: workerData) {
                if (worker.getPos().x != e.x || worker.getPos().y != e.y)
                    continue;
                if (selectedWorker != worker.id) {
                    selectedWorker = worker.id;
                } else {
                    selectedWorker = 0;
                }
                clickedOne = true;
            }
            if (!clickedOne) {
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
            
            worker.setJob(null);
            int y = e.y, x = e.x;
            switch (t.getKind()) {
            // TODO if the target tile contains a usable block, make worker use
            // it
            case CRAFTING_BENCH:
            case INVENTORY:
            case SPONSOR:
                y += 1;
                break;
            case DOOR:
                y -= 1;
                break;
            }
            worker.setTargetPos(new Point(x, y));
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
    
    protected void renderWorker(Graphics2D g, Worker worker) {
        
        int tween = worker.getTweenFrames();
        if (tween != 0) {
            worker.decTweenFrames();
            int x = (worker.getPrevPos().x-worker.getPos().x) *
                    tween * TILE_WIDTH / Worker.MAX_TWEEN_FRAMES,
                y = (worker.getPrevPos().y-worker.getPos().y) *
                    tween * TILE_HEIGHT / Worker.MAX_TWEEN_FRAMES;
            g.translate(x, y);
        }
        
        worker.render(g, spritesheet);

        // Draw a green box around selected workers
        if (selectedWorker == worker.id) {
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, TILE_WIDTH-1, TILE_HEIGHT-1);
        }
        
    }
    
    protected void renderTile(Graphics2D g, Tile tile) {
        tile.render(g, spritesheet, frame);
    }

    public synchronized void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getPixelSize().width, getPixelSize().height);
        
        // Layer 1: Everything but WORKERs
        for (int x = 0; x < map.getWidth(); ++x)
        for (int y = 0; y < map.getHeight(); ++y) {
            Tile tile = map.get(x,y);
            
            AffineTransform origXfm = g.getTransform();
            g.translate(x * TILE_WIDTH, y * TILE_HEIGHT);
            renderTile(g, tile);
            
            g.setTransform(origXfm);
        }
        
        // Layer 2: WORKERs
        for (Worker worker: workerData) {
            if (!worker.isOnScreen()) continue;
            AffineTransform origXfm = g.getTransform();
            g.translate(
                    worker.getPos().x * TILE_WIDTH,
                    worker.getPos().y * TILE_HEIGHT);
            renderWorker(g, worker);
            g.setTransform(origXfm);
        }
        
        // Layer 3: DOORs (cover the workers)
        for (int x = 0; x < map.getWidth(); ++x)
        for (int y = 0; y < map.getHeight(); ++y) {
            Tile tile = map.get(x,y);
            
            if (tile.getKind() != Tile.Kind.DOOR) continue;
            
            AffineTransform origXfm = g.getTransform();
            g.translate(x * TILE_WIDTH, y * TILE_HEIGHT);
            renderTile(g, tile);
            
            g.setTransform(origXfm);
        }
        
        if (selectedWorker != 0) {
            Worker worker = workerData.get(selectedWorker-1);
            Point target = worker.getTargetPos();
            if (target != null) {
                g.setColor(Color.YELLOW);
                g.drawRect(target.x * TILE_WIDTH, target.y * TILE_HEIGHT,
                        TILE_WIDTH, TILE_HEIGHT);
            }
            if (worker.getJob() != null) {
                Point eqPos = worker.getJob().equipmentPosition();
                g.setColor(Color.RED);
                g.drawRect(eqPos.x * TILE_WIDTH, eqPos.y * TILE_HEIGHT,
                        TILE_WIDTH, TILE_HEIGHT);
            }
        }
        
        ++frame;
    }
    
    public synchronized void inputEvent(InputEvent ie) {
        bufferedEvents.add(ie);
    }
    
    public synchronized void inputEvent(int x, int y, MouseEvent e) {
        inputEvent(new InputEvent(x,y,e));
    }

    public int getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(int i) {
        selectedWorker = i;
    }

    public int getMoney() {
        return money;
    }
    
}
