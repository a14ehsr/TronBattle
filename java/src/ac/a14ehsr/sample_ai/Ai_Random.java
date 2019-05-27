package ac.a14ehsr.sample_ai;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Ai_Random {
    private static final int CONTINUE  = 0;
    private static final int FINISH  = 1;

    private static final int DEATH = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    private int numberOfPlayers;
    private int numberOfGames;
    private int timelimit;
    private int width;
    private int height;
    private int playerCode; // 0始まりの識別番号
    private int[][] nowPosition;
    private Scanner sc;
    static final String playerName = "P_Random";

    public static void main(String[] args) {
        (new Ai_Random()).run();
    }

        /**
     * ゲーム実行メソッド
     */
    public void run() {
        sc = new Scanner(System.in);
        initialize();

        // ゲーム数ループ
        for (int i = 0; i < numberOfGames; i++) {
            boolean continueFlag = true;
            while(continueFlag){
                for (int p = 0; p < numberOfPlayers; p++) {
                    int x0 = sc.nextInt();
                    int y0 = sc.nextInt();
                    int x1 = sc.nextInt();
                    int y1 = sc.nextInt();
                    System.err.println(p + " | " +x0 + " " + y0 + " " + x1 + " " + y1);
                }

                int direction = LEFT;
                // 戦略を実行
                //direction = (int)(Math.random()*4) + 1;
                System.out.println(direction);

                if(sc.nextInt() == FINISH) {
                    continueFlag = false;
                }
            }
        }
    }
    /*
    public void move(int player, int direction, int[][] boad) {
        if(direction == DEATH) {
            for(int y = 1; y <= height; y++) {
                for(int x = 1; x <= width; x++) {
                    if(boad[y][x] == player) {
                        boad[y][x] = -1;
                    }
                }
            }
            nowPosition[player][0] = -1;
            nowPosition[player][1] = -1;
            return;
        }
        int x = nowPosition[player][0];
        int y = nowPosition[player][1];
        switch(direction) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        boad[y][x] = player;
        nowPosition[player][0] = x;
        nowPosition[player][1] = y;
    }
    */

    /**
     * 初期化
     */
    private void initialize() {
        numberOfPlayers = sc.nextInt();
        numberOfGames = sc.nextInt();
        timelimit = sc.nextInt();
        playerCode = sc.nextInt();

        System.out.println(playerName);

        width = sc.nextInt();
        height = sc.nextInt();
    }

}