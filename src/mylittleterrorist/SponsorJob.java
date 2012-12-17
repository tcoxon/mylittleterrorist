package mylittleterrorist;

import java.awt.Point;

public class SponsorJob implements IWorkerJob {

    protected int x, y;
    protected boolean activated = false;
    
    public SponsorJob(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Point requiredPosition() {
        return new Point(x, y+1);
    }

    public void activate(Game game, Worker worker) {
        if (game.hasSponsors())
            game.showWindow(worker, new SponsorInboxWindow());
        activated = true;
    }
    
    public boolean isActivated() {
        return activated;
    }

    public double getProgress() {
        return 0;
    }

    public void tick(Game game, Worker worker) {
        worker.setJob(null);
    }

    public Point equipmentPosition() {
        return new Point(x,y);
    }

    public String getDescription() {
        return "Contacting sponsors";
    }

}
