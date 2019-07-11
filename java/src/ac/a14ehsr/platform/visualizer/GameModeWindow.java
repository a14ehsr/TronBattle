package ac.a14ehsr.platform.visualizer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.BorderLayout;

import ac.a14ehsr.platform.GamePlatform;

public class GameModeWindow implements KeyListener{
    private JFrame frame;
    private JPanel mainMenuPanel;
    private Visualizer gamePanel;

    public GameModeWindow() {
        //VisualizerPanel visualizer = new VisualizerPanel(30, 20);
        // 入力を待ってからスタート
        /*
        try {
            battle(new TronBattle(numberOfPlayers, players, visualizer), 1, true, 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setName("Tron Battle");
        frame.setSize(800, 600);
        frame.setVisible(true);

        mainMenuPanel = new MainMenuPanel();

        frame.getContentPane().add(mainMenuPanel);

        frame.validate();

        //frame.addKeyListener(this);
    }

    class MainMenuPanel extends JPanel {
        ButtonGroup numOfPlayerButtonGroup;
        JToggleButton[] numOfPlayerButtons;
        ButtonGroup numOfHumanPlayerButtonGroup;
        JToggleButton[] numOfHumanPlayerButtons;

        JPanel numOfPlayerButtonPanel;
        JPanel numOfHumanPlayerButtonPanel;

        JButton startButton;

        MainMenuPanel() {
            setBackground(Color.BLUE);

            setLayout(new GridLayout(1,3));

            Font font = new Font("", Font.BOLD, 30);

            numOfPlayerButtonPanel = new JPanel();
            numOfPlayerButtonPanel.setLayout(new BoxLayout(numOfPlayerButtonPanel, BoxLayout.Y_AXIS));
            numOfPlayerButtonPanel.setLayout(new GridLayout(5,1));

            numOfHumanPlayerButtonPanel = new JPanel();
            numOfHumanPlayerButtonPanel.setLayout(new BoxLayout(numOfHumanPlayerButtonPanel, BoxLayout.Y_AXIS));
            numOfHumanPlayerButtonPanel.setLayout(new GridLayout(5,1));

            add(numOfPlayerButtonPanel);
            add(numOfHumanPlayerButtonPanel);

            numOfPlayerButtonGroup = new ButtonGroup();
            numOfPlayerButtons = new JToggleButton[5];
            JLabel nopLabel = new JLabel("対戦人数");
            nopLabel.setFont(font);
            nopLabel.setHorizontalAlignment(JLabel.CENTER);
            numOfPlayerButtonPanel.add(nopLabel);
            for(int i = 1; i < numOfPlayerButtons.length; i++) {
                numOfPlayerButtons[i] = new JToggleButton(Integer.toString(i+1)+"人");
                numOfPlayerButtonGroup.add(numOfPlayerButtons[i]);
                numOfPlayerButtonPanel.add(numOfPlayerButtons[i]);
                numOfPlayerButtons[i].setActionCommand((i+1)+"");
                numOfPlayerButtons[i].setFont(font);
            }

            numOfHumanPlayerButtonGroup = new ButtonGroup();
            numOfHumanPlayerButtons = new JToggleButton[3];
            JLabel nohLabel = new JLabel("人間プレイヤー数");
            nohLabel.setFont(font);
            nohLabel.setHorizontalAlignment(JLabel.CENTER);
            numOfHumanPlayerButtonPanel.add(nohLabel);
            for(int i = 0; i < numOfHumanPlayerButtons.length; i++) {
                numOfHumanPlayerButtons[i] = new JToggleButton(Integer.toString(i)+"人");
                numOfHumanPlayerButtonGroup.add(numOfHumanPlayerButtons[i]);
                numOfHumanPlayerButtonPanel.add(numOfHumanPlayerButtons[i]);
                numOfHumanPlayerButtons[i].setActionCommand((i)+"");
                numOfHumanPlayerButtons[i].setFont(font);
            }

            numOfPlayerButtons[3].setSelected(true);
            numOfHumanPlayerButtons[1].setSelected(true);

            startButton = new JButton("start");
            add(startButton);

            startButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    gamePanel = new Visualizer(30,20, frame);
                    JButton exitButton =  new JButton("EXIT");
                    exitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            gamePanel.setVisible(false);
                            mainMenuPanel.setVisible(true);
                        }
                    });

                    gamePanel.add(exitButton, BorderLayout.EAST);
                    gamePanel.setGMW(true);
                    frame.getContentPane().add(gamePanel);
                    gamePanel.setVisible(true);

                    int nop = Integer.parseInt(numOfPlayerButtonGroup.getSelection().getActionCommand());
                    int noh = Integer.parseInt(numOfHumanPlayerButtonGroup.getSelection().getActionCommand());
                    String[] commands = new String[nop];
                    String[] defaultCommands = new String[3]; 
                    defaultCommands[0] = "./ai_programs/TronBattle対戦用プログラム/P_mucchin";
                    defaultCommands[1] = "./ai_programs/TronBattle対戦用プログラム/P_masayo16";
                    for(int i = 0; i < noh; i++) {
                        commands[i] = "-human";
                    }
                    for(int i = noh; i<nop; i++) {
                        commands[i] = defaultCommands[i-noh];
                    }
                    Thread th = new Thread(new GamePlatform(nop, commands, gamePanel, frame));
                    th.start();
                    frame.validate();
                    gamePanel.validate();
                    frame.requestFocus();

                    
                }
            });
        }

    }

    public static void main(String[] args) {
        new GameModeWindow();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.err.println(e.getKeyChar());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.err.println(e.getKeyChar());   
    }
    @Override
    public void keyPressed(KeyEvent e) {
        System.err.println("T:"+e.getKeyCode());
    }
}