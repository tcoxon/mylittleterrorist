package mylittleterrorist;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StatusPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    protected MainApplet applet;
    protected Game game;
    protected JLabel moneyLabel, renownLabel;
    protected JList workerList;
    protected JPanel detailPanel;
    protected JLabel selectionNameLabel, selectionStatusLabel;
    protected JButton selectionCancelJobButton;
    
    // HACK: used to prevent changing the selection index of the list object
    // during the 'update' method from firing the selection-change event, which
    // causes the game's selected worker to change again
    protected boolean inUpdate = false;
    
    public StatusPanel(Game g, MainApplet a) {
        this.game = g;
        this.applet = a;

        BorderLayout layout = new BorderLayout(5,5);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(layout);
        
        JPanel counterPanel = new JPanel();
        counterPanel.setLayout(new BoxLayout(counterPanel, BoxLayout.Y_AXIS));
        moneyLabel = new JLabel("Money: $0");
        counterPanel.add(moneyLabel);
        renownLabel = new JLabel("Notoriety: 0");
        counterPanel.add(renownLabel);
        add(counterPanel, BorderLayout.NORTH);
        
        workerList = new JList(game.getWorkers());
        workerList.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(workerList, BorderLayout.CENTER);
        
        workerList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!inUpdate)
                    game.inputEvent(new InputEvent(
                            InputEvent.Kind.SELECT_WORKER,
                            workerList.getSelectedIndex()+1));
            }
            
        });
        
        detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout(5,5));
        detailPanel.setBorder(new EmptyBorder(5,5,5,5));
        
        selectionNameLabel = new JLabel("");
        detailPanel.add(selectionNameLabel, BorderLayout.NORTH);
        
        selectionStatusLabel = new JLabel("");
        Font f = selectionStatusLabel.getFont();
        f = new Font(f.getName(), Font.PLAIN, f.getSize());
        selectionStatusLabel.setFont(f);
        detailPanel.add(selectionStatusLabel, BorderLayout.CENTER);
        
        selectionCancelJobButton = new JButton("Cancel Action");
        selectionCancelJobButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                game.inputEvent(new InputEvent(InputEvent.Kind.JOB_CANCEL));
                // Also display the map panel in case the job we want to
                // cancel has caused a window to appear.
                applet.showMapPanel();
            }
        });
        detailPanel.add(selectionCancelJobButton, BorderLayout.SOUTH);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public void update() {
        // See HACK comment at the declaration of inUpdate.
        inUpdate = true;
        
        workerList.setListData(game.getWorkers());
        workerList.setSelectedIndex(game.getSelectedWorkerIndex()-1);
        
        moneyLabel.setText("Money: $" + Integer.toString(game.getMoney()));
        renownLabel.setText("Notoriety: " + Integer.toString(game.getRenown()));
        
        Worker sel = game.getSelectedWorker();
        if (sel != null) {
            add(detailPanel, BorderLayout.SOUTH);
            
            selectionNameLabel.setText(sel.getName());
            selectionStatusLabel.setText(sel.getJobDescription());
            // TODO display name of the object worker is holding
            selectionCancelJobButton.setVisible(sel.getJob() != null);
        } else {
            remove(detailPanel);
        }
        applet.validate();
        
        inUpdate = false;
    }

}
