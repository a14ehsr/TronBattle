package ac.a14ehsr.platform;

import java.io.IOException;
import java.util.Arrays;

import ac.a14ehsr.platform.visualizer.Visualizer;
import ac.a14ehsr.player.Player;

public abstract class Game {
    protected int numberOfPlayers;
    protected int numberOfGames;
    protected long timelimit;
    protected Player[] players;
    protected Visualizer visualizer;

    protected static final String modeInit = "I";
    protected static final String modeFirst = "F";
    protected static final String modePlay = "P";
    protected static final String modeEnd = "E";


    public Game(int numberOfPlayers, int numberOfGames, long timelimit, Player[] players){
        this.numberOfGames = numberOfGames;
        this.numberOfPlayers = numberOfPlayers;
        this.timelimit = timelimit;
        this.players = players;
    }

    /**
     * @param visualizer the visualizer to set
     */
    public void setVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
    }

    /**
     * ゲームを開始する時の一番最初の処理
     * ゲームごとに変わるプレイヤーに渡すべき情報があればここに出力を加える
     */
    abstract void initialize() throws IOException;

    /**
     * ゲーム状況を出力する
     */
    abstract void show();

    /**
     * ゲーム終了時の状況を出力
     */
    abstract void showGameResult();
    /**
     * ターン処理
     */
    abstract void play();

    /**
     * 順位付けを行い，それに合わせたポイントを付与する
     */
    void gameRank() {

    }

    /**
     * ゲーム終了時のplayerのgamePointから算出したポイントをsumPointに加算
     */
    abstract void calcGameResult();

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
     * @param numberOfGames the numberOfGames to set
     */
    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    /**
     * @param numberOfPlayers the numberOfPlayers to set
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    
    /**
     * playerのsumPointの高い順に順位を付与して
     * Resultオブジェクトを返す
     */
    Result result() {
        return new Result(players);
    }

    void sendGameInfo() throws IOException {
        for(Player player : players) {
            // TODO: 渡す情報のクラスを修正，もしくは　新しく作る
            player.sendMes(modeInit + numberOfPlayers);
            player.sendMes(modeInit + numberOfGames);
            player.sendMes(modeInit + timelimit);
        }
    }

    void showPlayers() {
        System.out.println("players  : " + String.join(" vs ",Arrays.stream(players).map(Player::getName).toArray(String[]::new)));
    }

    /**
     * ゲームが終了しているならfalse
     */ 
    abstract boolean isContinue();

    /**
     * 残り一人になっているならその人にWINを付与してfalseを返す
     * @return
     */
    abstract boolean checkContinue();
}