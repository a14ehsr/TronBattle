package ac.a14ehsr.platform.visualizer;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import ac.a14ehsr.platform.GamePlatform;
import ac.a14ehsr.platform.TronBattle;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

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
                    gamePanel = new Visualizer(30,20, frame);
                    frame.getContentPane().add(gamePanel);
                    gamePanel.setVisible(true);

                    String[] commands = new String[3];
                    commands[1] = "./ai_programs/TronBattle対戦用プログラム/P_masayo16";
                    commands[2] = "./ai_programs/TronBattle対戦用プログラム/P_mucchin";
                    commands[1] = "-human";
                    commands[0] = "-human";
                    Thread th = new Thread(new GamePlatform(3, commands, gamePanel, frame));
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