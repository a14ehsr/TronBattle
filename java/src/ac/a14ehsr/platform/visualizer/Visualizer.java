package ac.a14ehsr.platform.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Visualizer {
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel namePanel;

    private JPanel[][] panels;

    private int height;
    private int width;

    private static final Color[] playerColor = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    private static final Color notAchieve = Color.LIGHT_GRAY;

    public Visualizer(int width, int height) {
        this.width = width;
        this.height = height;
        frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(width * 40,height * 40);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(height + 2,width + 2));
        
        panels = new JPanel[height + 2][width + 2];
        LineBorder border = new LineBorder(Color.BLACK);

        for(int y = 0; y < panels.length; y++) {
            for(int x = 0; x < panels[y].length; x++) {
                panels[y][x] = new JPanel();
                panels[y][x].setBackground(notAchieve);
                panels[y][x].setBorder(border);
                mainPanel.add(panels[y][x]);
            }
        }
        for(int x = 0; x < panels[0].length; x++) {
            panels[0][x].setBackground(Color.GRAY);
            panels[height+1][x].setBackground(Color.GRAY);
        }

        for(int y = 0; y < panels.length; y++) {
            panels[y][0].setBackground(Color.GRAY);
            panels[y][width + 1].setBackground(Color.GRAY);
        }

        frame.getContentPane().add(mainPanel,BorderLayout.CENTER);

        namePanel = new JPanel();
        frame.getContentPane().add(namePanel,BorderLayout.NORTH);

    }

    public void setName(String[] names) {
        for(int p = 0; p < names.length; p++) {
            JLabel label = new JLabel(names[p]);
            label.setForeground(playerColor[p]);
            namePanel.add(label);
        }
    }

    public void setColor(int player, int x, int y) {
        panels[y][x].setBackground(playerColor[player]);
        panels[y][x].repaint();
        frame. validate();
    }

    public void relese(int player, int x, int y) {
        panels[y][x].setBackground(notAchieve);
        panels[y][x].repaint();
        frame. validate();
    }

    public void dispose() {
        frame.dispose();
    }

    public void reset() {
        for(int y = 1; y <= height; y++) {
            for(int x = 1; x <= width; x++) {
                panels[y][x].setBackground(notAchieve);
            }
        }       
    }
}