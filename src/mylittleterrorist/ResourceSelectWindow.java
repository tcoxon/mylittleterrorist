package mylittleterrorist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ResourceSelectWindow implements IGameWindow {

    protected ResourceJob job;
    
    public ResourceSelectWindow(ResourceJob job) {
        this.job = job;
    }
    
    public String getTitle() {
        return "Select resources to gather";
    }

    public void create(final Game game, JPanel panel) {
        
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(5,5));
        
        JPanel boxes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        final JCheckBox coalBox = new JCheckBox("Coal"),
                  sulphurBox = new JCheckBox("Sulphur"),
                  fertilizerBox = new JCheckBox("Fertilizer"),
                  recruitBox = new JCheckBox("Recruits");
        boxes.add(coalBox);
        boxes.add(sulphurBox);
        boxes.add(fertilizerBox);
        boxes.add(recruitBox);
        
        panel.add(boxes, BorderLayout.CENTER);
        
        final JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        okBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (recruitBox.isSelected() &&
                        game.getWorkerCount() >= game.getMaxWorkers()) {
                    errorLabel.setText("Cannot recruit any more terrorists at this level of notoriety");
                } else {
                    Set<Resource> kinds = job.getKinds();
                    if (coalBox.isSelected())
                        kinds.add(Resource.COAL);
                    if (sulphurBox.isSelected())
                        kinds.add(Resource.SULPHUR);
                    if (fertilizerBox.isSelected())
                        kinds.add(Resource.FERTILIZER);
                    if (recruitBox.isSelected())
                        kinds.add(Resource.RECRUIT);
                    game.closeWindow();
                }
            }
            
        });
        
        cancelBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                game.closeWindow();
            }
            
        });
    }

}
