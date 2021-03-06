package mylittleterrorist;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class MerchantWindow implements IGameWindow {

    public static final int SEX_SLAVE_COST = 200;
    
    public String getTitle() {
        return "Arms Dealer";
    }

    public void create(final Game game, JPanel panel) {
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setLayout(new BorderLayout(5,5));
        
        try {
            ImagePanel face = new ImagePanel(
                    Spritesheet.get("/sprites/faces.png", 128, 128).get(1,0));
            panel.add(face, BorderLayout.WEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        JPanel mainPanel = new JPanel(new BorderLayout(5,5));
        panel.add(mainPanel, BorderLayout.CENTER);
        
        JLabel dialogue = new JLabel("\"See anything you like? Double-click to buy it.\"");
        mainPanel.add(dialogue, BorderLayout.NORTH);
        
        final Map<Item, Integer> ownedCount =
                new EnumMap<Item, Integer>(Item.class);
        
        for (Item item: Item.values()) ownedCount.put(item, 0);
        for (InventorySlot slot: game.getInventory()) {
            if (slot.getItem() != null)
                ownedCount.put(slot.getItem(),
                        ownedCount.get(slot.getItem()) + slot.getCount());
        }
        
        final String[] columns = new String[]{"Item", "Cost", "Owned"};
        final Object[][] table = new Object[Item.values().length+1][3];
        for (int i = 0; i < Item.values().length; ++i) {
            Item item = Item.values()[i];
            table[i][0] = item.name;
            table[i][1] = "$" + Integer.toString(item.cost);
            table[i][2] = ownedCount.get(item);
        }
        table[Item.values().length] = new Object[]{
                "Sex Slave", "$"+SEX_SLAVE_COST, game.getSexSlaveCount()
        };
        
        final JTable jtable = new JTable(table, columns);
        JScrollPane jtableScroll = new JScrollPane(jtable);
        mainPanel.add(jtableScroll, BorderLayout.CENTER);
        
        final JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalTextPosition(JLabel.CENTER);
        mainPanel.add(errorLabel, BorderLayout.SOUTH);
        
        // Disable editing of the table contents:
        jtable.setModel(new AbstractTableModel() {
            private static final long serialVersionUID = 1L;

            public int getColumnCount() {
                return 3;
            }

            public int getRowCount() {
                return Item.values().length+1;
            }

            public Object getValueAt(int row, int col) {
                return table[row][col];
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }
            
        });
        
        jtable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() == 2) {
                    int row = jtable.getSelectedRow();
                    if (row < Item.values().length) {
                        Item item = Item.values()[row];
                        String error = game.buy(item);
                        if (error == null) {
                            ownedCount.put(item, ownedCount.get(item)+1);
                            table[row][2] = Integer.toString(ownedCount.get(item));
                            jtable.repaint();
                            errorLabel.setText("");
                        } else {
                            errorLabel.setText(error);
                        }
                    } else {
                        String error = game.buySexSlave(SEX_SLAVE_COST);
                        if (error == null) {
                            errorLabel.setText("");
                            table[row][2] = game.getSexSlaveCount();
                            jtable.repaint();
                        } else {
                            errorLabel.setText(error);
                        }
                    }
                }
            }
            
        });
    }

}
