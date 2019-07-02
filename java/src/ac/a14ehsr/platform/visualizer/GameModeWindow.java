package ac.a14ehsr.platform.visualizer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

public class GameModeWindow {
    private JFrame frame;
    private JPanel mainMenuPanel;
    private JPanel gamePanel;
    private static final Color[] playerColor = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.ORANGE};
    private static final Color notAchieve = Color.LIGHT_GRAY;


    public GameModeWindow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setName("Tron Battle");
        frame.setSize(800, 600);
        frame.setVisible(true);

        mainMenuPanel = new MainMenuPanel();

        frame.getContentPane().add(mainMenuPanel);

        frame.validate();
    }

    class GamePanel extends JPanel {
        private JPanel[][] panels;

        private int height;
        private int width;
    

        GamePanel(int width, int height) {
            this.width = width;
            this.height = height;    
            setLayout(new GridLayout(height + 2,width + 2));
            panels = new JPanel[height + 2][width + 2];
            LineBorder border = new LineBorder(Color.BLACK);
    
            for(int y = 0; y < panels.length; y++) {
                for(int x = 0; x < panels[y].length; x++) {
                    panels[y][x] = new JPanel();
                    panels[y][x].setBackground(notAchieve);
                    panels[y][x].setBorder(border);
                    add(panels[y][x]);
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
    
    
        }
        
    }

    class MainMenuPanel extends JPanel {
        ButtonGroup numOfPlayerButtonGroup;
        JRadioButton[] numOfPlayerButtons;
        ButtonGroup numOfHumanPlayerButtonGroup;
        JRadioButton[] numOfHumanPlayerButtons;

        JPanel numOfPlayerButtonPanel;
        JPanel numOfHumanPlayerButtonPanel;

        JButton startButton;

        MainMenuPanel() {
            setBackground(Color.BLUE);

            setLayout(new GridLayout(1,3));

            numOfPlayerButtonPanel = new JPanel();
            numOfPlayerButtonPanel.setLayout(new BoxLayout(numOfPlayerButtonPanel, BoxLayout.Y_AXIS));

            numOfHumanPlayerButtonPanel = new JPanel();
            numOfHumanPlayerButtonPanel.setLayout(new BoxLayout(numOfHumanPlayerButtonPanel, BoxLayout.Y_AXIS));

            add(numOfPlayerButtonPanel);
            add(numOfHumanPlayerButtonPanel);

            numOfPlayerButtonGroup = new ButtonGroup();
            numOfPlayerButtons = new JRadioButton[5];
            for(int i = 1; i < numOfPlayerButtons.length; i++) {
                numOfPlayerButtons[i] = new JRadioButton(Integer.toString(i+1)+"人");
                numOfPlayerButtonGroup.add(numOfPlayerButtons[i]);
                numOfPlayerButtonPanel.add(numOfPlayerButtons[i]);
            }

            numOfHumanPlayerButtonGroup = new ButtonGroup();
            numOfHumanPlayerButtons = new JRadioButton[5];
            for(int i = 1; i < numOfHumanPlayerButtons.length; i++) {
                numOfHumanPlayerButtons[i] = new JRadioButton(Integer.toString(i+1)+"人");
                numOfHumanPlayerButtonGroup.add(numOfHumanPlayerButtons[i]);
                numOfHumanPlayerButtonPanel.add(numOfHumanPlayerButtons[i]);
            }

            startButton = new JButton("start");
            add(startButton);

            startButton.addActionListener(new ActionListener(){
            
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    gamePanel = new GamePanel(30,20);
                    frame.getContentPane().add(gamePanel);
                    gamePanel.setVisible(true);
                }
            });
        }
    }

    public static void main(String[] args) {
        new GameModeWindow();
    }
}