package mylittleterrorist;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import mylittleterrorist.Worker.Style;

public class Game {

    public static final int TILE_WIDTH = 52, TILE_HEIGHT = 52;

    protected List<InputEvent> bufferedEvents;

    protected GameMap map;
    protected List<Worker> workerData;
    protected int selectedWorker = 0;
    protected int money, renown;
    
    protected Spritesheet spritesheet;
    
    protected MainApplet applet;
    protected JPanel currentWindow;
    
    protected int frame;
    
    public Game(MainApplet a) {
        applet = a;

        try {
            spritesheet = Spritesheet.get("/sprites/spritesheet.png",
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
    
    public void showWindow(Worker worker, IGameWindow w) {
        if (worker != null)
            selectedWorker = worker.id;
        
        if (currentWindow != null) currentWindow.setVisible(false);
        
        final JPanel panel = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(5,5,5,5));
        header.setBackground(Color.DARK_GRAY);
        JLabel title = new JLabel(w.getTitle());
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        
        JButton closeBtn = new JButton("X");
        closeBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                applet.showMapPanel();
            }
            
        });
        header.add(closeBtn, BorderLayout.EAST);
        
        panel.add(header, BorderLayout.NORTH);
        
        
        JPanel innerPanel = new JPanel();
        panel.add(innerPanel, BorderLayout.CENTER);
        
        w.create(innerPanel);
        
        currentWindow = panel;
        applet.showPanel(panel);
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
            switch (ie.kind) {
            case CLICK:
                handleClick(ie);
                break;
            case JOB_CANCEL:
                getSelectedWorker().setJob(null);
                getSelectedWorker().setTargetPos(null);
                break;
            case SELECT_WORKER:
                selectedWorker = ie.arg;
                break;
            default:
                throw new RuntimeException("Unhandle event kind");
            }
        }
        bufferedEvents.clear();
    }
    
    protected void handleClick(InputEvent e) {
        Tile t = map.get(e.x, e.y);
        
        if (e.arg == 1) {
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
        
        if (e.arg == 3 && selectedWorker != 0) {
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

    public int getSelectedWorkerIndex() {
        return selectedWorker;
    }

    public Worker getSelectedWorker() {
        if (selectedWorker == 0) return null;
        return workerData.get(selectedWorker-1);
    }

    public int getMoney() {
        return money;
    }

    public int getRenown() {
        return renown;
    }
    
}
