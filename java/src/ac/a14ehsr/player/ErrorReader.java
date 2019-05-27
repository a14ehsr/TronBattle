package ac.a14ehsr.player;

import java.io.InputStream;

/**
 * エラー出力のReader
 */
class ErrorReader extends Thread {
    InputStream error;
    int code;
    public ErrorReader(InputStream is, int code) {
        error = is;
        this.code = code;
    }

    public void run() {
        try {
            byte[] ch = new byte[50000];
            int read;
            while ((read = error.read(ch)) > 0) {
                String s = new String(ch, 0, read);
                System.out.print("player's error code=" + code + " : " + s);
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}