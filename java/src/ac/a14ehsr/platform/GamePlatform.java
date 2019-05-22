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
            players[p] = new Player(rt,commands[p],p);
        }

        // サブプロセス起動が長い可能性を考慮しておく
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: 引数にgameを入れる
    public Result battle(Game game, int outputLevel, boolean isVisible, long timeout) {
        game.sendGameInfo();
        if (outputLevel > 0) {
            game.showPlayers();
        }

        for(int g = 0; g < game.getNumberOfGames(); g++) {
            game.initialize();
            aGame(game);

            if(outputLevel > 1) {
                game.showGameResult();
            }
        }
        
        return game.result();
    }

    public void aGame(Game game) {
        // ゲームが終了するまで続行
        while(!game.isContinue()) {
            game.play();
        }
    }


}