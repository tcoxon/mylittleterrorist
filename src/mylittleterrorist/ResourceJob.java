package mylittleterrorist;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ResourceJob implements IWorkerJob {

    protected static final int MAX_PROGRESS = 300;
    
    protected int x, y;
    protected Set<Resource> kinds;
    protected int progress;
    protected boolean activated;
    
    public ResourceJob(ResourceJob other) {
        this(other.x, other.y, other.kinds);
    }
    
    public ResourceJob(int x, int y) {
        this(x, y, EnumSet.noneOf(Resource.class));
    }
    
    public ResourceJob(int x, int y, Set<Resource> kinds) {
        this.x = x;
        this.y = y;
        this.kinds = kinds;
        this.progress = 0;
        this.activated = false;
    }
    
    public Set<Resource> getKinds() {
        return kinds;
    }

    public Point equipmentPosition() {
        return new Point(x,y);
    }

    public Point requiredPosition() {
        return new Point(x,y-1);
    }

    public void activate(Game game, Worker worker) {
        worker.moveOffScreen(game.getMap());
        activated = true;
        if (kinds.size() == 0)
            game.showWindow(worker, new ResourceSelectWindow(this));
    }

    public double getProgress() {
        return progress / (0.0 + MAX_PROGRESS);
    }

    public void tick(Game game, Worker worker) {
        if (worker.getHolding() != null) {
            int slotIdx = game.getMatchingInventorySlotIndex(worker.getHolding());
            if (slotIdx != -1) {
                worker.setJob(new InventoryStorageJob(
                        slotIdx+1, 0, worker.getHolding(),
                        new ResourceJob(this)));
            } else {
                worker.setJob(null);
            }
            return;
        } else if (kinds.size() == 0) {
            worker.setJob(null);
            return;
        }
        
        ++progress;
        
        if (progress >= MAX_PROGRESS) {
            progress = MAX_PROGRESS;
            Random r = new Random();
            List<Resource> kindList = new ArrayList<Resource>(kinds);
            Resource kind = kindList.get(r.nextInt(kinds.size()));
            if (r.nextDouble() <= kind.successProbability(game)) {
                if (kind == Resource.RECRUIT) {
                    if (game.getWorkerCount() <= game.getMaxWorkers()) {
                        // TODO different styles of recruits
                        game.addWorker(Worker.Style.MALE);
                        progress = 0;
                    } else {
                        worker.setJob(null);
                    }
                } else {
                    worker.setHolding(kind.item);
                }
            } else {
                progress = 0;
            }
        }
    }

    public String getDescription() {
        if (kinds.size() == 0)
            return "Gathering resources";
        String result = "Gathering ";
        int i = 0;
        for (Resource res: kinds) {
            if (i == kinds.size()-1 && i != 0) {
                result += " and ";
            } else if (i != 0) {
                result += ", ";
            }
            result += res.name;
            ++i;
        }
        return result;
    }

    public boolean isActivated() {
        return activated;
    }

}
