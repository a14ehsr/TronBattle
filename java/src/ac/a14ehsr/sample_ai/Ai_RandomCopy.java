package ac.a14ehsr.sample_ai;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Ai_RandomCopy {
    private static final int CONTINUE  = 0;
    private static final int FINISH  = 1;

    private static final int DEATH = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    private static final int NOT_ACHIEVED = -1;
    private static final int WALL = -2;

    private int numberOfPlayers;
    private int numberOfGames;
    private int timelimit;
    private int width;
    private int height;
    private int playerCode; // 0始まりの識別番号
    private Scanner sc;
    static final String playerName = "P_RandomCopy";
    
    private int[][] nowPosition;
    private int[][] board;

    public static void main(String[] args) {
        (new Ai_RandomCopy()).run();
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

            nowPosition = new int[numberOfPlayers][2];
            board = new int[height + 2][width + 2];
            for(int[] a : board) {
                Arrays.fill(a, NOT_ACHIEVED);
            }

            while(continueFlag){
                for (int p = 0; p < numberOfPlayers; p++) {
                    int x0 = sc.nextInt();
                    int y0 = sc.nextInt();
                    int x1 = sc.nextInt();
                    int y1 = sc.nextInt();
                    //System.err.println(p + " | " +x0 + " " + y0 + " " + x1 + " " + y1);
                    move(p, x1, y1, board);
                }

                int direction = put();
                // 戦略を実行
                //direction = (int)(Math.random()*4) + 1;
                System.out.println(direction);

                if(sc.nextInt() == FINISH) {
                    continueFlag = false;
                }
            }
        }
    }

    public int put() {
        int x = nowPosition[playerCode][0];
        int y = nowPosition[playerCode][1];
        if(y < height && board[y+1][x] == NOT_ACHIEVED) {
            return UP;
        }else if(x < width && board[y][x+1] == NOT_ACHIEVED) {
            return RIGHT;
        }else if(y > 1 && board[y-1][x] == NOT_ACHIEVED) {
            return DOWN;
        }else if(x > 1 && board[y][x-1] == NOT_ACHIEVED) {
            return LEFT;
        }
        return DEATH;
    }

    public void move(int player, int moveX, int moveY, int[][] board) {
        if(moveX == -1) {
            for(int y = 1; y <= height; y++) {
                for(int x = 1; x <= width; x++) {
                    if(board[y][x] == player) {
                        board[y][x] = NOT_ACHIEVED;
                    }
                }
            }
            nowPosition[player][0] = -1;
            nowPosition[player][1] = -1;
            return;
        }

        board[moveY][moveX] = player;
        nowPosition[player][0] = moveX;
        nowPosition[player][1] = moveY;
    }


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