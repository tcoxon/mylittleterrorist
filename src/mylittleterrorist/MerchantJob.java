package mylittleterrorist;

import java.awt.Point;

public class MerchantJob implements IWorkerJob {

    protected Point eqPos, reqPos;
    
    public MerchantJob(int x, int y) {
        this.eqPos = new Point(x, y);
        this.reqPos = new Point(x+1, y);
    }
    
    public Point requiredPosition() {
        return reqPos;
    }

    public void activate(Game game, Worker worker) {
        game.showWindow(new MerchantWindow());
        worker.setJob(null);
    }

    public double getProgress() {
        return 0;
    }

    public void tick(Game game, Worker worker) {
        // Nothing in this case
    }

    public Point equipmentPosition() {
        return eqPos;
    }

    public String getDescription() {
        return "Contacting arms dealer";
    }

}
