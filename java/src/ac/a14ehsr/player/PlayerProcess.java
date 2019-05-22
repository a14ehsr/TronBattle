package ac.a14ehsr.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import ac.a14ehsr.exception.TimeoutException;
import ac.a14ehsr.exception.TimeoverException;
import ac.a14ehsr.platform.setting.Setting;

public class PlayerProcess {
    private Process process;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedReader;

    private String name;
    private int code;

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

    public static PlayerProcess of(Runtime runtime, String runCommand) throws IOException {
        PlayerProcess playerProcess = new PlayerProcess();
        playerProcess.process = runtime.exec(runCommand);
        playerProcess.outputStream = playerProcess.process.getOutputStream();
        playerProcess.inputStream = playerProcess.process.getInputStream();
        playerProcess.bufferedReader = new BufferedReader(new InputStreamReader(playerProcess.inputStream));

        new ErrorReader(playerProcess.process.getErrorStream()).start();

        if (!playerProcess.process.isAlive())
            throw new IOException("次のサブプロセスを起動できませんでした．:" + playerProcess.process);
        return playerProcess;
    }

    /**
     * playerにnumを送る
     * @param num
     * @throws IOException
     */
    public void send(int num) throws IOException {
        send(String.valueOf(num));
    }

    /**
     * playerにmesを送る
     * 末尾に改行があると不具合を起こす可能性がある．末尾改行のreplaceを組み込むのも手かもしれない
     * @param mes
     * @throws IOException
     */
    public void send(String mes) throws IOException {
        outputStream.write((mes + "\n").getBytes()); // 初期座標を渡す
        outputStream.flush();
    }

    public String receiveMes(long timeout, long timelimit) throws IOException, TimeoutException, TimeoverException {
        if (!process.isAlive())
            throw new IOException("値取得時に次のプレイヤーのサブプロセスが停止しました :" + name);
        Thread thread = new ReceiveThread();
        thread.start();
        long start = System.nanoTime();
        try {
            thread.join(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long calculateTime = System.nanoTime() - start;
        // TODO: tyimoutしたプレイヤーをkillしてむりやりすすめる
        if (receiveString == null)
            throw new TimeoutException("一定時間以内に次のプレイヤーから値を取得できませんでした :" + name);

        if(calculateTime/1.0e6 > timelimit) {
            throw new TimeoverException("制限時間内に次のプレイヤーから値を取得できませんでした :" + name);
        }

        return receiveString;
    }

    public int receiveNum(long timeout, long timelimit) throws IOException, TimeoutException, TimeoverException, NumberFormatException {
        String tmp = receiveMes(timeout, timelimit);
        return Integer.parseInt(tmp);
    }

    /**
     * 数値の取得用Thread
     */
    class ReceiveThread extends Thread {
        public void run() {
            try {
                receiveString = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                // outputException = e;
            }
        }
    }


    /**
     * サブプロセスを終了
     */
    private void destroy() {
        if (process == null) return;
        try {
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}