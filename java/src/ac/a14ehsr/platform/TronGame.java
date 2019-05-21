package ac.a14ehsr.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ac.a14ehsr.platform.setting.Setting;
import ac.a14ehsr.platform.visualizer.Visualizer;


public class TronGame {
    Process[] processes;
    InputStream[] inputStreams;
    OutputStream[] outputStreams;
    BufferedReader[] bufferedReaders;
    Setting setting;
    int numberOfGames;
    int numberOfSelectNodes;
    String[] outputStr;

    public TronGame(String[] args) {
        // 各種設定と実行コマンド関連の処理
        setting = new Setting();
        setting.start(args);
    }

    /**
     * サブプロセスの起動
     * 
     * @param cmd 実行コマンド(0:攻撃，1:防御)
     * @throws IOException
     */
    private void startSubProcess(String[] cmd) throws IOException {
        int numberOfPlayers = setting.getNumberOfPlayers();
        Runtime rt = Runtime.getRuntime();
        processes = new Process[numberOfPlayers];
        inputStreams = new InputStream[numberOfPlayers];
        outputStreams = new OutputStream[numberOfPlayers];
        bufferedReaders = new BufferedReader[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            processes[i] = rt.exec(cmd[i]);
            outputStreams[i] = processes[i].getOutputStream();
            inputStreams[i] = processes[i].getInputStream();
            bufferedReaders[i] = new BufferedReader(new InputStreamReader(inputStreams[i]));
            new ErrorReader(processes[i].getErrorStream()).start();
            if (!processes[i].isAlive())
                throw new IOException("次のサブプロセスを起動できませんでした．:" + processes[i]);
        }
    }

    private void getOutput(int index) throws IOException {
        outputStr[index] = bufferedReaders[index].readLine();
    }

    /**
     * 対戦する
     * 
     * @throws IOException
     * @throws AgainstTheRulesException
     * @throws NumberFormatException
     */
    private Result run() throws IOException, NumberFormatException, TimeoutException {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int numberOfPlayers = setting.getNumberOfPlayers();
        int numberOfGames = setting.getNumberOfGames();
        int outputLevel = setting.getOutputLevel();
        boolean isVisible = setting.isVisible();
        long timeout = setting.getTimeout();
        String[] names = new String[numberOfPlayers];

        int patternSize = 1;
        for (int i = 1; i <= numberOfPlayers; i++) {
            patternSize *= i;
        }

        // send information of game
        for (int p = 0; p < numberOfPlayers; p++) {
            outputStreams[p].write((numberOfPlayers + "\n").getBytes());
            outputStreams[p].write((numberOfGames + "\n").getBytes());
            outputStreams[p].write((setting.getWidth() + "\n").getBytes());
            outputStreams[p].write((setting.getHeight() + "\n").getBytes());
            outputStreams[p].write((p + "\n").getBytes()); // player code
            outputStreams[p].flush();
            names[p] = bufferedReaders[p].readLine();
        }

        if (outputLevel > 0) {
            System.out.println("players  : " + String.join(" vs ",names));
        }

        outputStr = new String[numberOfPlayers];

        // 各プレイヤーの勝利数
        int[] playerPoints = new int[numberOfPlayers];

        Boad boad = new Boad(setting.getHeight(),setting.getWidth(), numberOfPlayers, setting.getTimelimit());
        // numberOfGames回対戦
        for (int i = 0; i < numberOfGames; i++) {
            // 盤面の初期化とデータ渡し
            int[][] nowPosition = boad.initilize();

            List<Integer> alivePlayers = new ArrayList<>();
            for(int p = 0; p < numberOfPlayers; p++) {
                for(int k = 0; k < numberOfPlayers; k++) {
                    outputStreams[p].write((nowPosition[k][0] + "\n").getBytes()); // 初期座標を渡す
                    outputStreams[p].write((nowPosition[k][1] + "\n").getBytes()); // 初期座標を渡す
                }
                outputStreams[p].flush();
                alivePlayers.add(p);
            }

            Visualizer visualizer = null;
            if (isVisible) {
                visualizer = new Visualizer(setting.getWidth(), setting.getHeight());
            }

            int turn = 0;
            int[] numberOfTurn = new int[numberOfPlayers]; // 各playerが何ターン生きたか
            List<Integer> deadList = new ArrayList<>();
            while(numberOfPlayers == 1 || alivePlayers.size() > 1) {
                for(int p = 0; p < numberOfPlayers; p++) {
                    if (!processes[p].isAlive())
                        throw new IOException("次のプレイヤーのサブプロセスが停止しました :" + names[p]);
                    Thread thread = new GetResponseThread(p);
                    thread.start();
                    long start = System.nanoTime();
                    try {
                        thread.join(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long calculateTime = System.nanoTime() - start;
                    // TODO: tyimoutしたプレイヤーをkillしてむりやりすすめる
                    if (outputStr[p] == null)
                        throw new TimeoutException("一定時間以内に次のプレイヤーから値を取得できませんでした :" + names[p]);

                    int num;
                    try {
                        num = Integer.parseInt(outputStr[p]);
                        outputStr[p] = null;
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException(
                            // TODO: プレイヤーをkillしてむりやりすすめる
                            "次のプレイヤーから整数以外の値を取得しました :" + names[p] + " :" + outputStr[p]);
                    }
                    boolean accept = false;
                    if(num != Boad.DEATH) {
                        accept = boad.move(p, num, calculateTime, names[p]);
                        if(!accept) {
                            numberOfTurn[p] = turn;
                            num = Boad.DEATH;
                            alivePlayers.remove(Integer.valueOf(p));
                            deadList.add(p);
    
                        }
                    }

                    for( int k = 0; k < numberOfPlayers; k++){
                        outputStreams[k].write((num + "\n").getBytes()); // 移動情報をoutput
                        outputStreams[k].flush();
                    }
                    
                    if (isVisible) {
                        // TODO: 処理
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if(outputLevel >= 3) {
                        boad.show();
                    }

                    if((numberOfPlayers > 1 && alivePlayers.size() == 1) || (numberOfPlayers == 1 && alivePlayers.size() == 0)) {
                        break;
                    }

                }

                if(numberOfPlayers == 1 && alivePlayers.size() == 0){
                    break;
                }
                turn++;
            }

            // 勝ち点の計算
            int[] point = calcPoint(deadList,alivePlayers, turn);
            if (outputLevel >= 2) {
                System.out.printf("%第2dゲームの利得: ",i);
                for (int k = 0; k < numberOfPlayers; k++) {
                    System.out.print(names[k] + " ");
                }
                System.out.print(" | ");

                for (int k = 0; k < numberOfPlayers; k++) {
                    System.out.printf("%3d ",numberOfTurn[k]);
                }

                System.out.print(" | 点数: ");
                for (int k = 0; k < numberOfPlayers; k++) {
                    System.out.printf("%2d ",point[k]);
                }
                System.out.println();

            }
            for (int t = 0; t < numberOfPlayers; t++) {
                playerPoints[t] += point[t];
            }
            if (isVisible) {
                visualizer.dispose();
            }
        }
        if (outputLevel > 0) {
            System.out.print("勝ち点合計:");
            for (int num : playerPoints) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        return new Result(names, playerPoints);
    }

    private int[] calcPoint(List<Integer> deadList, List<Integer> aliveList, int turn) {
        if(setting.getNumberOfPlayers() > 1) {
            int[] score = new int[setting.getNumberOfPlayers()];
            int point = 0;
            for(int p : deadList) {
                score[p] = point++;
            }
            score[aliveList.get(0)] = point;
            return score;
        } else {
            return new int[]{turn};
        }
    }

    /**
     * タイムアウト発生時に投げる例外クラス
     */
    class TimeoutException extends Exception {
        /**
         * コンストラクタ
         * 
         * @param mes メッセージ
         */
        TimeoutException(String mes) {
            super(mes);
        }
    }

    /**
     * 数値の取得用Thread
     */
    class GetResponseThread extends Thread {
        private int index;

        GetResponseThread(int index) {
            this.index = index;
        }

        public void run() {
            try {
                getOutput(index);
            } catch (IOException e) {
                e.printStackTrace();
                // outputException = e;
            }
        }
    }

        /**
     * リザルト用のEntity
     */
    class Result {
        String[] names;
        int[] playerPoints;
        int[] playerID;
        int[] rank;
        boolean isNoContest;

        Result(String[] names, int[] playerPoints) {
            this.names = names;
            this.playerPoints = playerPoints;
            rank = new int[names.length];
            setRank();
            isNoContest = false;
        }

        Result(int[] id) {
            playerID = new int[id.length];
            for (int i = 0; i < id.length; i++) {
                playerID[i] = id[i];
            }
            isNoContest = true;
        }

        void setPlayerID(int[] id) {
            playerID = new int[id.length];
            for (int i = 0; i < id.length; i++) {
                playerID[i] = id[i];
            }
        }

        /**
         * ランク情報をセットする
         * 任意のプレイヤー人数に対応済み
         */
        void setRank() {
            // プレイヤーIDと特典をペアにして特典順にソート
            List<NumberPair> dict = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                dict.add(new NumberPair(i, playerPoints[i]));
            }
            dict.sort((a, b) -> b.num - a.num);

            int beforeNum = dict.get(0).num;
            int index = 0;
            NumberPair numpair = dict.get(0);
            // 初期化時点で0なので以下の処理は暗黙のうちに行われている.
            //rank[numpair.key] = 0;

            // 特典順に見て，同じ値の時は同じ順位をつけていく．
            for (int i = 1; i < setting.getNumberOfPlayers(); i++) {
                numpair = dict.get(i);
                if (beforeNum != numpair.num) {
                    beforeNum = numpair.num;
                    index = i;
                }
                rank[numpair.key] = index;
            }
        }
    }
}


/**
 * エラー出力のReader
 */
class ErrorReader extends Thread {
    InputStream error;

    public ErrorReader(InputStream is) {
        error = is;
    }

    public void run() {
        try {
            byte[] ch = new byte[50000];
            int read;
            while ((read = error.read(ch)) > 0) {
                String s = new String(ch, 0, read);
                System.out.print(s);
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class NumberPair {
    int key;
    int num;

    NumberPair(int key, int num) {
        this.key = key;
        this.num = num;
    }
}