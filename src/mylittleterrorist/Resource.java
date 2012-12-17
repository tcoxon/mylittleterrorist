package mylittleterrorist;

public enum Resource {

    COAL("Coal", Item.COAL),
    SULPHUR("Sulphur", Item.SULPHUR),
    FERTILIZER("Fertilizer", Item.FERTILIZER),
    
    RECRUIT("Recruits", null) {

        @Override
        public double successProbability(Game game) {
            // The ease of finding recruits depends on your renown
            return Math.log(game.getRenown()) * 0.05;
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
