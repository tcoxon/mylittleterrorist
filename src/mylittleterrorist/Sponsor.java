package mylittleterrorist;

import java.util.Date;

public class Sponsor {

    protected final String name, subject;
    protected final int value, renown;
    protected final long by;
    protected InventorySlot[] required, rewards;
    protected boolean suicide;
    
    // 'duration' is in seconds
    public Sponsor(String name, String subject,
            InventorySlot[] required, InventorySlot[] rewards, boolean suicide,
            int value, int renown, int duration) {
        this.name = name;
        this.subject = subject;
        this.required = required;
        this.rewards = rewards;
        this.suicide = suicide;
        this.value = value;
        this.renown = renown;
        this.by = new Date().getTime() + duration*1000;
    }
    
    public String getSubject() {
        return subject;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
    
    public int getRenown() {
        return renown;
    }
    
    public long getMSLeft() {
        return by - new Date().getTime();
    }
    
    public String getTimeLeft() {
        long seconds = (getMSLeft())/1000;
        if (seconds <= 0) return "0:00";
        long minutes = seconds/60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    
    public String toString() {
        return name + " - $" + value + " - " + subject;
    }
    
    public String getHTMLBody() {
        String html = "<html>" +
                "<br/>";
        if (required.length > 0 || suicide) {
            html += "<b>Requires:</b><ul>";
            for (InventorySlot s: required) {
                html += "<li>"+s+"</li>";
            }
            if (suicide)
                html += "<li>1x Suicide</li>";
            html += "</ul>";
        }
        if (rewards.length > 0 || value != 0) {
            html += "<b>Rewards:</b><ul>";
            for (InventorySlot s: rewards) {
                html += "<li>"+s+"</li>";
            }
            if (value != 0)
                html += "<li>$"+value+"</li>";
            html += "</ul>";
        }
        return html;
    }

}
