package ac.a14ehsr.platform;

import java.util.Arrays;

import ac.a14ehsr.player.Player;

public abstract class Game {
    public static final int WIN = -256;
    public static final int DEATH = -128;
    private int numberOfPlayers;
    private int numberOfGames;
    private long timelimit;

    Player[] players;

    /**
     * ゲームを開始する時の一番最初の処理
     */
    abstract void initialize();

    /**
     * ゲーム状況を出力する
     */
    abstract void show();

    /**
     * ターン処理
     */
    abstract void play();

    /**
     * playerが手を打つ
     * @param player
     */
    abstract void put(Player player);

    /**
     * playerに行動不能等の状態を付与
     * @param player
     */
    void kill(Player player) {
        player.setStatus(DEATH);
    }

    /**
     * playerに勝利の状態を付与
     * @param player
     */
    void win(Player player) {
        player.setStatus(WIN);
    }

    /**
     * 順位付けを行い，それに合わせたポイントを付与する
     */
    void gameRank() {

    }

    /**
     * @return the numberOfGames
     */
    public int getNumberOfGames() {
        return numberOfGames;
    }

    /**
     * @return the numberOfPlayers
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    
    /**
     * playerのsumPointの高い順に順位を付与して
     * Resultオブジェクトを返す
     */
    Result result() {
        return new Result();
    }

    void sendGameInfo() {
        for(Player player : players) {
            // TODO: 渡す情報のクラスを修正，もしくは　新しく作る
            player.sendGameInfo(setting);
        }
    }

    void showPlayers() {
        System.out.println("players  : " + String.join(" vs ",Arrays.stream(players).map(Player::getName).toArray(String[]::new)));
    }
}