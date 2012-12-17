package mylittleterrorist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SponsorInboxWindow implements IGameWindow {

    public String getTitle() {
        return "Inbox";
    }
    
    public void setMsgPanel(List<Sponsor> sponsors, JList inbox,
            JPanel msgPanel) {
        
        for (Component c: msgPanel.getComponents()) {
            msgPanel.remove(c);
        }
        
        if (inbox.getSelectedIndex() == -1) return;
        
        final Sponsor sponsor = sponsors.get(inbox.getSelectedIndex());
        
        msgPanel.setLayout(new BorderLayout());
        
        JLabel headers = new JLabel(
                "<html><b>From:</b> " + sponsor.getName() + "<br/>" +
                "<b>To:</b> Terry Wrist<br/>"+
                "<b>Subject:</b> TODO<br/>"+
                "<br/>"+
                "</html>");
        JLabel body = new JLabel(
                "<html>$"+Integer.toString(sponsor.getValue())+" TODO</html>");
        
        msgPanel.add(headers, BorderLayout.NORTH);
        msgPanel.add(body, BorderLayout.CENTER);
        
        JPanel footer = new JPanel(new BorderLayout());
        final JButton fillBtn = new JButton("Fill Order");
        final JLabel timeLeft = new JLabel(sponsor.getTimeLeft()) {
            private static final long serialVersionUID = 1L;
            private Timer timer;
            private TimerTask task;
            private JLabel self = this;
            {
                timer = new Timer(true);
                task = new TimerTask() {
                    @Override
                    public void run() {
                        self.setText(sponsor.getTimeLeft());
                        if (sponsor.getMSLeft() <= 0)
                            fillBtn.setEnabled(false);
                    }
                };
                timer.schedule(task, new Date(), 500);
            }
        };
        footer.add(timeLeft, BorderLayout.WEST);
        footer.add(fillBtn, BorderLayout.EAST);
        
        msgPanel.add(footer, BorderLayout.SOUTH);
    }

    public void create(Game game, JPanel panel) {
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(5,5));
        
        final Vector<Sponsor> sponsors = new Vector<Sponsor>(game.getSponsors());
        
        final JList inbox = new JList(sponsors);
        inbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane inboxScroll = new JScrollPane(inbox);
        inbox.setPreferredSize(new Dimension(300,300));
        panel.add(inboxScroll, BorderLayout.WEST);
        
        final JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalTextPosition(JLabel.CENTER);
        panel.add(errorLabel, BorderLayout.SOUTH);
        
        final JPanel msgPanel = new JPanel();
        msgPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        msgPanel.setBackground(Color.WHITE);
        JScrollPane msgScroll = new JScrollPane(msgPanel);
        panel.add(msgScroll, BorderLayout.CENTER);
        
        inbox.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg0) {
                setMsgPanel(sponsors, inbox, msgPanel);
            }
        });
        
    }

}
