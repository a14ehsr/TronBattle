package ac.a14ehsr.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import ac.a14ehsr.exception.TimeoutException;
import ac.a14ehsr.exception.TimeoverException;
import ac.a14ehsr.platform.setting.Options;

public class Player {
    private PlayerProcess process;
    private String name;
    private int code;
    private int gamePoint;
    private int sumPoint;
    private int status;
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

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the gamePoint
     */
    public int getGamePoint() {
        return gamePoint;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return the sumPoint
     */
    public int getSumPoint() {
        return sumPoint;
    }

    /**
     * @param gamePoint the gamePoint to set
     */
    public void setGamePoint(int gamePoint) {
        this.gamePoint = gamePoint;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @param sumPoint the sumPoint to set
     */
    public void setSumPoint(int sumPoint) {
        this.sumPoint = sumPoint;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    public Player(Runtime runtime, String runCommand, int code) throws IOException {
        process = PlayerProcess.of(runtime, runCommand);
        this.code = code;
    }

    public void sendMes(String mes) throws IOException {
        process.send(mes);
    }

    public void sendNum(int num) throws IOException {
        process.send(num);
    }

    public String receiveMes(long timeout, long timelimit) throws IOException, TimeoutException, TimeoverException {
        return process.receiveMes(timeout, timelimit);
    }

    public int receiveNum(long timeout, long timelimit) throws IOException, TimeoutException, TimeoverException {
        return process.receiveNum(timeout, timelimit);
    }

    public void destroy() {
        process.destroy();
    }


}