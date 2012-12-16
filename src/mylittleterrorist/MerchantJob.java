package mylittleterrorist;

import java.awt.Point;

public class MerchantJob implements IWorkerJob {

    protected Point eqPos, reqPos;
    protected boolean activated = false;
    
    public MerchantJob(int x, int y) {
        this.eqPos = new Point(x, y);
        this.reqPos = new Point(x+1, y);
    }
    
    public Point requiredPosition() {
        return reqPos;
    }

    public void activate(Game game, Worker worker) {
        game.showWindow(worker, new MerchantWindow());
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
        return eqPos;
    }

    public String getDescription() {
        return "Contacting arms dealer";
    }

}
