package mylittleterrorist;

import javax.swing.JPanel;

public class ResourceSelectWindow implements IGameWindow {

    protected ResourceJob job;
    
    public ResourceSelectWindow(ResourceJob job) {
        this.job = job;
    }
    
    public String getTitle() {
        return "Select resources to gather";
    }

    public void create(Game game, JPanel panel) {
        
    }

}
