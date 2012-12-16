package mylittleterrorist;

import java.awt.Point;

public interface IWorkerJob {

    public Point equipmentPosition();
    public Point requiredPosition();
    public void activate(Game game, Worker worker);
    public double getProgress();
    public void tick(Game game, Worker worker);
    public String getDescription();
    public boolean isActivated();
    
}
