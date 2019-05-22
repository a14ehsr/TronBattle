package ac.a14ehsr.platform;

import java.io.IOException;
import java.util.Arrays;

import ac.a14ehsr.exception.TimeoutException;
import ac.a14ehsr.player.Player;

public class TronBattle extends Game {
    public static final int DEATH = -1;
    public static final int WIN = -2;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;

    private static final int NOT_ACHIEVED = -1;
    private static final int WALL = -2;

    private int width;
    private int height;
    private int[][] board; // 1 <= x <= width, 1 <= y <= height
    private int[][] nowPosition; // position of players. nowPosition[player] = {x,y}
    private int aliveCount;

    public TronBattle(int numberOfPlayers, int numberOfGames, long timelimit, Player[] players, int width, int height) {
        super(numberOfGames,numberOfPlayers, timelimit, players);
        board = makeBoard();
        this.width = width;
        this.height = height;
    }

    /**
     * 盤を作成する
     * 四辺は壁とする
     */
    public int[][] makeBoard() {
        int[][] board = new int[height + 2][width + 2];
        for(int[] a : board) {
            Arrays.fill(a, NOT_ACHIEVED);
        }
        for(int x = 0; x <= width+1; x++) {
            board[0][x] = WALL;
            board[height + 1][x] = WALL;
        }
        for(int y = 0; y <= height+1; y++) {
            board[y][0] = WALL;
            board[y][width + 1] = WALL;
        }
        return board;
    }

    @Override
    void initialize() throws IOException {
        board = makeBoard();
        nowPosition = new int[numberOfPlayers][2];
        for(int p = 0; p < numberOfPlayers; p++) {
            int[] tmp = new int[]{-1,-1};
            boolean check;
            do {
                check = true;
                tmp = new int[]{(int)(Math.random() * width) + 1, (int)(Math.random() * height) + 1};
                for(int k = 0; k < p; k++) {
                    if(tmp[0] == nowPosition[k][0] && tmp[1] == nowPosition[k][1]) {
                        check = false;
                        break;
                    }
                }
            } while(!check);
            nowPosition[p] = tmp;
        }
        // 初期座標送信
        for(Player player : players) {
            for(int p = 0; p < numberOfPlayers; p++) {
                player.sendMes(modeFirst + nowPosition[p][0] + "-" + nowPosition[p][1]);
            }
            player.setGamePoint(numberOfPlayers);
        }
        aliveCount = numberOfPlayers;
    }

    @Override
    boolean isContinue() {
        return !Arrays.stream(players).anyMatch(player -> player.getStatus() == WIN);
    }
    
    @Override
    boolean checkContinue() {
        if(Arrays.stream(players).filter(p -> p.getStatus() != DEATH).count() == 1) {
            for(Player player : players) {
                if(player.getStatus() != DEATH) {
                    player.setStatus(WIN);
                    player.setGamePoint(1);
                    break;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    void play() {
        for(int p = 0; p < numberOfPlayers && isContinue(); p++) {
            Player player = players[p];
            int direction = put(player);
            String mode = !checkContinue() ? modeEnd : modePlay;

            for(int k = 0; k < numberOfPlayers; k++) {
                try{
                    players[k].sendMes(mode + direction);
                } catch(IOException e) {
                    System.out.println("送信時エラー");
                    e.printStackTrace();
                    kill(players[k]);
                }
            }
        }
    }

    int put(Player player) {
        int direction = DEATH;
        try {
            direction = player.receiveNum(timelimit+1000, timelimit);
        }catch(Exception e) {
            kill(player);
            return DEATH;
        }
        if(player.getStatus() == DEATH) {
            return DEATH;
        }
        int playerCode = player.getCode();
        int x = nowPosition[playerCode][0];
        int y = nowPosition[playerCode][1];
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
                System.out.println("存在しない移動パターンの値が入力されました　 | プレイヤー:" + player.getName());
                kill(player);
                return DEATH;       
        }

        if(y < 0 || y > board.length || x < 0 || x > board[y].length || board[y][x] == WALL) {
            System.out.println("ボードの範囲外に移動しようとしました | プレイヤー:" + player.getName());
            kill(player);
            return DEATH;
        }
        if(board[y][x] != NOT_ACHIEVED) {
            System.out.println("獲得済みのマスに移動しようとしました | プレイヤー:" + player.getName());
            kill(player);
            return DEATH;
        }
        board[y][x] = player.getCode();
        nowPosition[player.getCode()] = new int[]{x,y};
        return direction;
        
    }


    /**
     * playerに行動不能等の状態を付与
     * プレイヤーの現在座標を{-1,-1}に変更し,
     * プレイヤーが獲得した盤面を解放する
     * @param player
     */
    void kill(Player player) {
        player.setStatus(DEATH);
        player.setGamePoint(aliveCount--);
        nowPosition[player.getCode()] = new int[]{-1,-1};
        deletePlayer(player);
    }

    @Override
    void calcGameResult() {
        for(Player player : players) {
            player.setSumPoint(player.getSumPoint() + (numberOfPlayers - player.getGamePoint()));
        }
    }

    /**
     * playerに勝利の状態を付与
     * @param player
     */
    void win(Player player) {
        player.setStatus(WIN);
    }

    @Override
    void show() {
        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("---");
        }
        System.out.println("|");

        for(int y = height; y > 0; y--) {
            System.out.print("|");
            for(int x = 1; x <= width; x++) {
                System.out.printf("%2d ",board[y][x]);
            }
            System.out.println("|");
        }

        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("---");
        }
        System.out.println("|");
    }

    @Override
    void showGameResult() {
        for(Player player : players) {
            System.out.printf("%2d ",player.getGamePoint());
        }
        System.out.println();
    }

    /**
     * playerが獲得した盤面を解放する
     * @param player
     */
    public void deletePlayer(Player player) {
        int code = player.getCode();
        for(int y = 1; y <= height; y++) {
            for(int x = 1; x <= width; x++) {
                if(board[y][x] == code) {
                    board[y][x] = -1;
                }
            }
        }
    }

}