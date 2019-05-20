package ac.a14ehsr.platform;

import java.util.Arrays;

public class Boad {
    private int height;
    private int width;
    private int[][] gained; // 1 <= x <= width, 1 <= y <= height
    private int[][] nowPosition; // position of players. nowPosition[player] = {x,y}

    private static final int DEATH = 0;
    private static final int UP = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;
    private static final int LEFT = 4;

    public Boad(int width, int height, int numberOfPlayers) {
        this.width = width;
        this.height = height;
        gained = new int[height + 2][width + 2];
        nowPosition = new int[numberOfPlayers][2];
    }

    /**
     * 盤面を初期化し，プレイヤーの初期座標をランダムに決定してその座標を返す
     * @return プレイヤーの座標
     */
    public int[][] initilize() {
        clear();
        for(int p = 0; p < numberOfPlayers; p++) {
            int x,y;
            boolean check;
            do {
                check = true;
                x = Math.random() * width + 1;
                y = Math.random() * height + 1;
                for(int k = 0; k < p; k++) {
                    if(x == nowPosition[k][0] && y == nowPosition[k][1]) {
                        check = false;
                        break;
                    }
                }
            } while(!check);
            nowPosition[p][0] = x;
            nowPosition[p][1] = y;
        }

        return nowPosition;

    }

    /**
     * playerをdirectionに移動させる．移動できないのであればfalseを返す
     * @param player
     * @param direction
     * @param names
     * @return
     */
    public boolean move(int player, int direction, long calculateTime, String name) {
        if(calculateTime > timelimit) {
            System.err.println("制限時間を超過しました  | プレイヤー:" + name);
            kill(player);
            return false;
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
            default:
                System.out.println("存在しない移動パターンの値が入力されました　 | プレイヤー:" + name);
                kill(player);
                return false;       
        }
        if(x < 1 || x > width || y < 1 || y > height) {
            System.out.println("ボードの範囲外に移動しようとしました | プレイヤー:" + name);
            kill(player);
            return false;
        }
        if(gained[y][x] != -1) {
            System.out.println("獲得済みのマスに移動しようとしました | プレイヤー:" + name);
            kill(player);
            return false;
        }
        gained[y][x] = player;
        nowPosition[player][0] = x;
        nowPosition[player][1] = y;
        return true;
    }

    /**
     * 盤面を表示する
     */
    public void show() {
        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("--");
        }
        System.out.println("|");
        for(int y = height; y > 0; y--) {
            System.out.print("|");
            for(int x = 1; x <= width; x++) {
                System.out.print(gained[y][x] + " ");
            }
            System.out.println("|");
        }
        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("--");
        }
        System.out.println("|");
    }


    /**
     * playerが獲得した盤面を解放する
     * @param player
     */
    public void deletePlayer(int player) {
        for(int y = 1; y <= height; y++) {
            for(int x = 1; x <= width; x++) {
                if(gained[y][x] == player) {
                    gained[y][x] = -1;
                }
            }
        }
    }

    /**
     * 盤面を誰にも獲得されていない状態にする
     */
    public void clear() {
        for(int[] a : gained) {
            Arrays.fill(a, -1);
        }
    }

    /**
     * プレイヤーの現在座標を{-1,-1}に変更し,
     * プレイヤーが獲得した盤面を解放する
     * @param player
     */
    public void kill(int player) {
        nowPosition[player] = new int[]{-1,-1};
        deletePlayer(player);
    }

    /**
     * playerが生きているかを返す
     * @param player
     * @return
     */
    public boolean isAlive(int player) {
        if(nowPosition[player][0] == -1 && nowPosition[player][1] == -1) {
            return false;
        }
        return true;
    }
}