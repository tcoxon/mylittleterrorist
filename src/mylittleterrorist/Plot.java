package mylittleterrorist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum Plot {

    BOMBING {{
        senders = new String[]{ "Osama", "CIA Guy", "Somalian Pirate",
                "Movie Pirate", "Time Traveller" };
        subjects = new String[]{ "I want you to destroy %s.", "Blow up %s.",
                "%s needs levelling.",
                "It's been a while dude. We should catch up some time. Could "+
                "you do me a favor next time you're in %s?" };
        locations = new String[]{ "King's Cross", "Russell Square",
                "Eiffel Tower", "Parliament", "Santa's Grotto" };
        requiredRenown = 1;
        value = 25;
        duration = 120;
        gainedRenown = 10;
        required = new InventorySlot[]{
                new InventorySlot(Item.BOMB, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    SUICIDE_BOMBING {{
        senders = new String[]{ "Osama", "CIA Guy", "Somalian Pirate",
                "Movie Pirate", "Time Traveller" };
        subjects = new String[]{ "Let's teach the residents of %s a lesson!",
                "Time to go nuts in %s.",
                "72 virgins waiting for you in %s." };
        locations = new String[]{ "King's Cross", "Russell Square",
                "Eiffel Tower", "Parliament", "Santa's Grotto" };
        requiredRenown = 1;
        value = 100;
        duration = 120;
        gainedRenown = 20;
        required = new InventorySlot[]{
                new InventorySlot(Item.BOMB, 1)};
        rewards = new InventorySlot[]{};
        suicide = true;
    }},
    
    KID_BOMBING {{
        senders = new String[]{ "Angry Kid" };
        subjects = new String[]{ "Teach my parents a lesson! Blow up my %s!" };
        locations = new String[]{ "House" };
        value = 25;
        duration = 90;
        requiredRenown = 20;
        gainedRenown = 10;
        required = new InventorySlot[]{
                new InventorySlot(Item.BOMB, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    SCHOOL_SHOOTING {{
        senders = new String[]{ "Angry Kid" };
        subjects = new String[]{ "I want to be famous. Give me an AK so I can shoot up %s." };
        locations = new String[]{ "School" };
        value = 80;
        duration = 60;
        requiredRenown = 30;
        gainedRenown = 20;
        required = new InventorySlot[]{
                new InventorySlot(Item.AK47, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    DIRTY_BOMBING {{
        senders = new String[]{ "Osama", "CIA Guy", "Somalian Pirate",
                "Movie Pirate", "Time Traveller" };
        subjects = new String[]{ "I want you to nuke %s.", "Nuke %s.",
                "Make %s into a radioactive wasteland."};
        locations = new String[]{ "King's Cross", "Russell Square",
                "Eiffel Tower", "Parliament", "Santa's Grotto" };
        requiredRenown = 100;
        value = 300;
        duration = 180;
        gainedRenown = 100;
        required = new InventorySlot[]{
                new InventorySlot(Item.DIRTY_BOMB, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    SPACE_BOMBING {{
        senders = new String[]{ "Spaceman" };
        subjects = new String[]{ "%s is in the way of my mission to Mars.",
                "It's about time %s went away.",
                "Blast %s out of orbit!" };
        locations = new String[]{ "The Moon" };
        value = 1000;
        duration = 300;
        requiredRenown = 200;
        gainedRenown = 1000;
        suicide = true;
        required = new InventorySlot[]{
                new InventorySlot(Item.DIRTY_BOMB, 5),
                new InventorySlot(Item.PILOTS_LICENSE, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    FLYING_LESSONS {{
        senders = new String[]{ "CIA Guy" };
        subjects = new String[]{ "Need anything? Flying lessons on the cheap here." };
        locations = new String[]{ "JFK Airport" };
        value = -20;
        duration = 60;
        requiredRenown = 50;
        gainedRenown = 0;
        required = new InventorySlot[]{};
        rewards = new InventorySlot[]{new InventorySlot(Item.PILOTS_LICENSE, 1)};
    }},
    
    PLANE_HIJACK {{
        senders = new String[]{ "CIA Guy" };
        subjects = new String[]{ "It's a nice time for a flight through %s." };
        locations = new String[]{ "New York", "London", "Dubai" };
        value = 300;
        duration = 120;
        requiredRenown = 50;
        gainedRenown = 60;
        suicide = true;
        required = new InventorySlot[]{new InventorySlot(Item.PILOTS_LICENSE, 1)};
        rewards = new InventorySlot[]{};
    }},
    
    VIAGRA {{
        senders = new String[]{ "Big Bob", "xxx@hotmail.com" };
        subjects = new String[]{ "Need a bigger penis?",
                "Cheap Viagra!",
                "c1al!s SALE"};
        locations = new String[]{ "Hotmail" };
        value = -1;
        duration = 30;
        requiredRenown = 0;
        gainedRenown = 0;
        spam = true;
        required = new InventorySlot[]{};
        rewards = new InventorySlot[]{};
    }},
    
    ;
    
    protected String[] senders, subjects, locations;
    protected int value, duration, requiredRenown, gainedRenown;
    protected boolean suicide, spam;
    protected InventorySlot[] required, rewards;
    
    public String[] getSenders() {
        return senders;
    }
    public String[] getSubjects() {
        return subjects;
    }
    public String[] getLocations() {
        return locations;
    }
    public int getValue() {
        return value;
    }
    public int getDuration() {
        return duration;
    }
    public int getRequiredRenown() {
        return requiredRenown;
    }
    public int getGainedRenown() {
        return gainedRenown;
    }
    
    protected<T> T randomElem(T[] vals) {
        return vals[(int)(Math.random()*vals.length)];
    }
    
    protected Sponsor makeSponsor() {
        Sponsor sponsor = new Sponsor(
                randomElem(senders),
                String.format(randomElem(subjects), randomElem(locations)),
                required, rewards, suicide,
                value, gainedRenown, duration, spam
                );
        return sponsor;
    }
    
    public static Sponsor randomSponsor(int renown) {
        List<Plot> plots = new ArrayList<Plot>(Plot.values().length);
        for (Plot plot: Plot.values()) {
            if (renown >= plot.requiredRenown)
                plots.add(plot);
        }
        if (plots.size() == 0) return null;
        
        Random r = new Random();
        Plot plot = plots.get(r.nextInt(plots.size()));
        return plot.makeSponsor();
    }
    
    public boolean isSuicide() {
        return suicide;
    }
    public boolean isSpam() {
        return spam;
    }
    public InventorySlot[] getRequired() {
        return required;
    }
    public InventorySlot[] getRewards() {
        return rewards;
    }
}
