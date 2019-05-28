import java.util.Arrays;
import java.util.Scanner;

public class P_SampleJava {
    private static final int CONTINUE  = 0;
    private static final int FINISH  = 1;

    private static final int DEATH = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    private static final int NOT_ACHIEVED = -1;

    private int numberOfPlayers;
    private int numberOfGames;
    private int timelimit; // millisecond
    private int width;
    private int height;
    private int playerCode; // your player code. 0 <= playerCode < numberOfPlayers
    static final String playerName = "Java_Sample"; // Do not include spaces in your name.
    
    /**
     * currentPostion[p] = {p.x, p.y}
     * Payer p's current location is (p.x, p.y).
     */
    private int[][] currentPosition;

    /**
     * board[y][x] = status
     * Status is either NOT_ACHIEVED or player's code.
     * Be careful position status of location (x,y) saved board[y][x]
     * Array size is (height+2) * (width+2) bad Board size is height * width.
     * 1 <= x <= width, 1 <= y <= height.
     */
    private int[][] board;
    
    private Scanner sc;

    public static void main(String[] args) {
        (new P_SampleJava()).run();
    }

    /**
     * Game loop.
     */
    public void run() {
        sc = new Scanner(System.in);
        initialize();

        for (int i = 0; i < numberOfGames; i++) {
            boolean continueFlag = true;

            // initialize board and players position
            currentPosition = new int[numberOfPlayers][2];
            board = new int[height + 2][width + 2];
            for(int[] a : board) {
                Arrays.fill(a, NOT_ACHIEVED);
            }

            while(continueFlag){
                // receive all player positions.
                for (int p = 0; p < numberOfPlayers; p++) {
                    int x0 = sc.nextInt();
                    int y0 = sc.nextInt();
                    int x1 = sc.nextInt();
                    int y1 = sc.nextInt();
                    move(p, x0, y0, x1, y1);
                }

                // send your strategy.
                int direction = put();
                System.out.println(direction);

                // receive continue flag.
                if(sc.nextInt() == FINISH) {
                    continueFlag = false;
                }
            }
        }
    }

    /**
     * Your strategy
     * @return Direction {UP, DOWN, RIGHT, LEFT} or other parameter meaning you are dead.
     */
    public int put() {
        int x = currentPosition[playerCode][0];
        int y = currentPosition[playerCode][1];
        if(x == -1 && y == -1) {
            return DEATH;
        }
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

    /**
     * Move player to point (moveX, moveY) and set first location.
     * @param player
     * @param initX
     * @param initY
     * @param moveX
     * @param moveY
     */
    public void move(int player, int initX, int initY, int moveX, int moveY) {
        if(moveX == -1) {
            for(int y = 1; y <= height; y++) {
                for(int x = 1; x <= width; x++) {
                    if(board[y][x] == player) {
                        board[y][x] = NOT_ACHIEVED;
                    }
                }
            }
            currentPosition[player][0] = -1;
            currentPosition[player][1] = -1;
            return;
        }
        board[initY][initX] = player;
        board[moveY][moveX] = player;
        currentPosition[player][0] = moveX;
        currentPosition[player][1] = moveY;
    }


    /**
     * input parameters and send your name.
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