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
    
    public ResourceJob(int x, int y) {
        this.x = x;
        this.y = y;
        this.kinds = EnumSet.noneOf(Resource.class);
        this.progress = 0;
        this.activated = false;
    }

    public Point equipmentPosition() {
        return new Point(x,y);
    }

    public Point requiredPosition() {
        return new Point(x,y-1);
    }

    public void activate(Game game, Worker worker) {
        worker.setPos(null);
        activated = true;
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
                        slotIdx+1, 0, worker.getHolding()));
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
                    // TODO different styles of recruits
                    game.addWorker(Worker.Style.MALE);
                    progress = 0;
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
            if (i == kinds.size()-1) {
                result += "and ";
            } else if (i != 0) {
                result += ", ";
            }
            result += res.name + " ";
            ++i;
        }
        return result;
    }

    public boolean isActivated() {
        return activated;
    }

}
