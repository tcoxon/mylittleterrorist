package mylittleterrorist;

import java.awt.Point;

public class MerchantJob implements IWorkerJob {

    protected Point pos;
    
    public MerchantJob(int x, int y) {
        this.pos = new Point(x+1, y);
    }
    
    public Point requiredPosition() {
        return pos;
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

}
