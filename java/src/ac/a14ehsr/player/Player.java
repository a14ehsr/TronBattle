package ac.a14ehsr.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import ac.a14ehsr.exception.TimeoutException;
import ac.a14ehsr.exception.TimeoverException;
import ac.a14ehsr.platform.setting.Setting;

public class Player {
    private PlayerProcess process;
    private String name;
    private int code;
    private int gamePoint;
    private int sumPoint;
    private int rank;

    /**
     * 文字列受け取り用の一時変数
     * 受け取ったらnullなどでリセットする
     */
    private String receiveString;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public Player(Runtime runtime, String runCommand, int code) throws IOException {
        process = PlayerProcess.of(runtime, runCommand);
        this.code = code;
    }

    public void sendGameInfo(Setting setting) throws IOException, TimeoutException, TimeoverException {
        process.send(setting.toString());
        name = process.receiveMes(100, 1000);
    }

}