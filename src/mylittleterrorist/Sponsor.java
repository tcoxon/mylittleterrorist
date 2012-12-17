package mylittleterrorist;

import java.util.Date;

public class Sponsor {

    protected final String name, subject;
    protected final int value;
    protected final long by;
    
    // 'duration' is in seconds
    public Sponsor(String name, String subject, int value, int duration) {
        this.name = name;
        this.subject = subject;
        this.value = value;
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
        return "<html>$" + value + " - TODO</html>";
    }

}
