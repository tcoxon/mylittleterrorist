package mylittleterrorist;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.*;

import mylittleterrorist.Worker.Style;

public class Game {
    
    public static final int MAX_SPONSORS = 30;

    public static final int TILE_WIDTH = 52, TILE_HEIGHT = 52;

    protected List<InputEvent> bufferedEvents;

    protected GameMap map;
    protected List<Worker> workerData;
    protected int selectedWorker = 0;
    protected int money, renown;
    protected InventorySlot[] inventory, crafting;
    protected List<Sponsor> sponsors;
    
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
        
        frame = 0;
        
        money = 1000;
        renown = 1000;
        
        workerData = new ArrayList<Worker>(20);
        addWorker(Worker.Style.MALE);
        addWorker(Worker.Style.MALE);
        addWorker(Worker.Style.FEMALE);
        
        sponsors = new ArrayList<Sponsor>();
        addSponsor();
        
        setupInventory();
    }
    
    protected void addSponsor() {
        Sponsor sponsor = Plot.randomSponsor(renown);
        if (sponsor != null)
            sponsors.add(sponsor);
    }
    
    protected void setupInventory() {
        int invCount = 0, craftCount = 0;
        for (int x = 0; x < map.getWidth(); ++x)
        for (int y = 0; y < map.getHeight(); ++y) {
            Tile t = map.get(x,y);
            if (t.getKind() == Tile.Kind.INVENTORY) {
                t.setExtraData(invCount++);
            } else if (t.getKind() == Tile.Kind.CRAFTING_BENCH) {
                t.setExtraData(craftCount++);
            }
        }
        
        inventory = new InventorySlot[invCount];
        for (int i = 0; i < invCount; ++i) {
            inventory[i] = new InventorySlot();
        }
        
        crafting = new InventorySlot[craftCount];
        for (int i = 0; i < craftCount; ++i) {
            crafting[i] = new InventorySlot();
        }
        
        inventory[0].set(Item.COAL, 2);
        inventory[1].set(Item.SULPHUR, 2);
        inventory[2].set(Item.FERTILIZER, 2);
        inventory[3].set(Item.BOMB, 1);
    }
    
    public Worker[] getWorkers() {
        return workerData.toArray(new Worker[0]);
    }
    
    public void closeWindow() {
        applet.showMapPanel();
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
        
        w.create(this, innerPanel);
        
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
                if (!worker.isOnScreen() || worker.getPos().x != e.x ||
                        worker.getPos().y != e.y)
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
            case INVENTORY:
                setWorkerJobInventory(worker, e.x, e.y);
                return;
            case DOOR:
                worker.setJob(new ResourceJob(e.x, e.y));
                return;
            case CRAFTING_BENCH:
                setWorkerJobCrafting(worker, e.x, e.y);
                return;
            case SPONSOR:
                worker.setJob(new SponsorJob(e.x, e.y));
                return;
            }

            worker.setTargetPos(new Point(e.x, e.y));
        }
    }
    
    protected void setWorkerJobInventory(Worker worker, int x, int y) {
        InventorySlot slot = inventory[map.get(x,y).getExtraData()];
        if (worker.getHolding() == null && slot.getItem() != null) {
            worker.setJob(new InventoryRetrieveJob(x, y, slot.getItem()));
        } else if (worker.getHolding() != null && (slot.getItem() == null ||
                slot.getItem() == worker.getHolding())) {
            worker.setJob(new InventoryStorageJob(x, y, worker.getHolding()));
        }
    }

    protected void setWorkerJobCrafting(Worker worker, int x, int y) {
        InventorySlot slot = crafting[map.get(x,y).getExtraData()];
        if (worker.getHolding() == null && slot.getItem() != null) {
            worker.setJob(new CraftingRetrieveJob(x, y, slot.getItem()));
        } else if (worker.getHolding() != null && (slot.getItem() == null ||
                slot.getItem() == worker.getHolding())) {
            worker.setJob(new CraftingStorageJob(x, y, worker.getHolding()));
        }
    }
    
    public GameMap getMap() {
        return map;
    }

    public synchronized void tick() {
        handleEvents();
        updateWorkers();
        
        for (Sponsor sponsor: new ArrayList<Sponsor>(sponsors)) {
            if (sponsor.getMSLeft() <= 0)
                sponsors.remove(sponsor);
        }
        
        if (sponsors.size() < MAX_SPONSORS &&
                new Random().nextInt((int)(1500/Math.log(renown))) == 0) {
            addSponsor();
        }
    }
    
    protected void updateWorkers() {
        for (Worker worker: new ArrayList<Worker>(workerData)) {
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
        tile.render(g, this, spritesheet, frame);
        if (tile.getKind() == Tile.Kind.INVENTORY) {
            InventorySlot slot = inventory[tile.getExtraData()];
            slot.render(g);
        }
        if (tile.getKind() == Tile.Kind.CRAFTING_BENCH) {
            InventorySlot slot = crafting[tile.getExtraData()];
            slot.render(g);
        }
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

    public InventorySlot[] getInventory() {
        return inventory;
    }
    
    public int getMatchingInventorySlotIndex(Item item) {
        // First try to put it into a slot with the same kind of item
        for (int i = 0; i < inventory.length; ++i) {
            InventorySlot slot = inventory[i];
            if (slot.getItem() == item) {
                return i;
            }
        }
        
        // Otherwise, try to put it into an empty slot
        for (int i = 0; i < inventory.length; ++i) {
            InventorySlot slot = inventory[i];
            if (slot.getItem() == null) {
                return i;
            }
        }
        
        return -1;
    }

    public String buy(Item item) {
        if (money >= item.cost) {
            // First try to put it into a slot with the same kind of item
            for (InventorySlot slot: inventory) {
                if (slot.getItem() == item) {
                    slot.increase();
                    money -= item.cost;
                    return null;
                }
            }
            
            // Otherwise, try to put it into an empty slot
            for (InventorySlot slot: inventory) {
                if (slot.getItem() == null) {
                    slot.set(item, 1);
                    money -= item.cost;
                    return null;
                }
            }
            
            return "Not enough free shelf space";
        }
        return "Not enough money";
    }

    public int getWorkerCount() {
        return workerData.size();
    }
    
    public int getMaxWorkers() {
        return 3 + renown/50;
    }

    public InventorySlot[] getCrafting() {
        return crafting;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }
    
    public boolean hasSponsors() {
        return sponsors.size() > 0;
    }
    
}
