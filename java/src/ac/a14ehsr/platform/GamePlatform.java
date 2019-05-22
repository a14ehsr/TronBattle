package ac.a14ehsr.platform;

import java.io.IOException;
import java.util.Arrays;

public class GamePlatform {
    Player[] players;

    /**
     * サブプロセスの起動
     * @param numberOfPlayer
     * @param commands 実行コマンド
     * @throws IOException
     */
    public void startSubProcess(int numberOfPlayer, String[] commands) throws IOException {
        Runtime rt = Runtime.getRuntime();
        players = new Player[numberOfPlayer];
        for(int p = 0; p < numberOfPlayer; p++) {
            players[p] = Player.of(rt,commands[p],p);
        }

        // サブプロセス起動が長い可能性を考慮しておく
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Result battle(int numberOfPlayer, int numberOfGame, int outputLevel, boolean isVisible, long timeout, long timelimit) {
        for(Player player : players) {
            player.sendGameInfo(setting);
        }
        if (outputLevel > 0) {
            System.out.println("players  : " + String.join(" vs ",Arrays.stream(players).map(Player::getName).toArray(String[]::new)));
        }
        

    }


}