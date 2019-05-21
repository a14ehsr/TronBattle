package ac.a14ehsr.sample_ai;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Ai_Random {
    private static final int DEATH = -1;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    private int numberOfPlayers;
    private int numberOfGames;
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
            int[][] boad = new int[height + 2][width + 2];
            for(int[] a : boad) {
                Arrays.fill(a, -1);
            }
            nowPosition = new int[numberOfPlayers][2];
            List<Integer> alivePlayers = new ArrayList<>();
            int aliveCount = numberOfPlayers;
            boolean[] isAlive = new boolean[numberOfPlayers];
            Arrays.fill(isAlive, true);
            for (int p = 0; p < numberOfPlayers; p++) {
                nowPosition[p][0] = sc.nextInt();
                nowPosition[p][1] = sc.nextInt();
                alivePlayers.add(p);
            }
            
            while(numberOfPlayers == 1 || aliveCount > 1){
                for (int p = 0; p < numberOfPlayers; p++) {
                    if (p == playerCode) {
                        if(!isAlive[p]) {
                            System.out.println(DEATH);
                        }else{
                            int direction = 0;
                            // 戦略を実行
                            System.out.println(direction);
                        }
                    }

                    int direction = sc.nextInt();
                    if(direction != DEATH) {
                        move(p, direction, boad);
                    }else if(isAlive[p]){
                        aliveCount--;
                        move(p, direction, boad);
                    }
                }
            }
        }
    }

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

    /**
     * 初期化
     */
    private void initialize() {
        numberOfPlayers = sc.nextInt();
        numberOfGames = sc.nextInt();
        width = sc.nextInt();
        height = sc.nextInt();
        playerCode = sc.nextInt();
        System.out.println(playerName);
    }

}