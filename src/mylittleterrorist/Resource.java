package mylittleterrorist;

public enum Resource {

    COAL("Coal", Item.COAL),
    SULPHUR("Sulphur", Item.SULPHUR),
    FERTILIZER("Fertilizer", Item.FERTILIZER),
    
    RECRUIT("Recruits", null) {

        @Override
        public double successProbability(Game game) {
            return 0.1;
        }
        
    }
    
    ;
    
    public final String name;
    public final Item item;
    
    public double successProbability(Game game) {
        return 1.0;
    }
    
    private Resource(String name, Item item) {
        this.name = name;
        this.item = item;
    }
    
}
