package mylittleterrorist;

import java.awt.Point;

public class SuicideJob implements IWorkerJob {

    protected static final int MAX_PROGRESS = 300;
    
    protected int x, y;
    protected boolean activated;
    protected Sponsor sponsor;
    
    public SuicideJob(int x, int y, Sponsor sponsor) {
        this.x = x;
        this.y = y;
        this.sponsor = sponsor;
        this.activated = false;
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
        if (game.executeOrder(sponsor)) {
            worker.kill();
        }
        worker.setJob(null);
    }

    public double getProgress() {
        return 0;
    }

    public void tick(Game game, Worker worker) {
        // Nothing...
    }

    public String getDescription() {
        return "Suicide Mission";
    }

    public boolean isActivated() {
        return activated;
    }

}
