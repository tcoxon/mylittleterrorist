package mylittleterrorist;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StatusPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    protected Game game;
    protected JLabel moneyLabel;
    protected JList<Worker> workerList;
    
    public StatusPanel(Game g) {
        this.game = g;

        BorderLayout layout = new BorderLayout(5,5);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(layout);
        
        moneyLabel = new JLabel("Money: $0");
        moneyLabel.setHorizontalAlignment(JLabel.CENTER);
        add(moneyLabel, BorderLayout.NORTH);
        
        workerList = new JList<Worker>(game.getWorkers());
        workerList.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(workerList, BorderLayout.CENTER);
        
        workerList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                game.setSelectedWorker(arg0.getFirstIndex()+1);
            }
            
        });
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public void update() {
        workerList.setListData(game.getWorkers());
        workerList.setSelectedIndex(game.getSelectedWorker()-1);
        
        moneyLabel.setText("Money: $" + Integer.toString(game.getMoney()));
    }

}
