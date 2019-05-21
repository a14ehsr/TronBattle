package ac.a14ehsr.platform.visualizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer {
    private JFrame frame;
    private JPanel mainPanel;

    private JPanel[][] panels;

    public Visualizer(int width, int height) {
        frame = new JFrame();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(500,500);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(height + 2,width + 2));

        
        panels = new JPanel[height + 2][width + 2];
        for(int y = 0; y < panels.length; y++) {
            for(int x = 0; x < panels[y].length; x++) {
                panels[y][x] = new JPanel();
                panels[y][x].setBackground(Color.LIGHT_GRAY);
                mainPanel.add(panels[y][x]);
            }
        }

        frame.getContentPane().add(mainPanel,BorderLayout.CENTER);


    }

    public void dispose() {
        frame.dispose();
    }
}