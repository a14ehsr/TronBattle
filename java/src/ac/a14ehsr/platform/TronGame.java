package ac.a14ehsr.platform;

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
    private Result run() throws IOException, AgainstTheRulesException, NumberFormatException, TimeoutException {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int numberOfPlayers = setting.getNnumberOfPlayers();
        int numberOfGames = setting.getNumberOfGames();
        String[] names = new String[numberOfPlayers];

        int patternSize = 1;
        for (int i = 1; i <= numberOfPlayers; i++) {
            patternSize *= i;
        }

        for (int p = 0; p < numberOfPlayers; p++) {
            outputStreams[p].write((numberOfPlayers + "\n").getBytes());
            outputStreams[p].write((numberOfGames + "\n").getBytes());
            outputStreams[p].write((numberOfSelectNodes + "\n").getBytes());
            outputStreams[p].write((patternSize + "\n").getBytes());
            outputStreams[p].write((p + "\n").getBytes()); // player code
            outputStreams[p].flush();
            names[p] = bufferedReaders[p].readLine();
        }

        if (outputLevel > 0) {
            System.out.print("players  : ");
            for (String name : names)
                System.out.printf(name + " ");
            System.out.println();
        }

        outputStr = new String[numberOfPlayers];
        // 利得のレコード
        int[][][] gainRecord = new int[numberOfGames][patternSize][numberOfPlayers];

        // 各プレイヤーの勝利数
        int[] playerPoints = new int[numberOfPlayers];

        // ゲームレコードの準備(初期値-1)
        int[][][][] gameRecord = new int[numberOfGames][][][];

        // プレイヤーの手番の管理用リスト．線形リストで十分．
        List<int[]> sequenceList = new ArrayList<>();
        sequenceList = Permutation.of(numberOfPlayers);
        // numberOfGames回対戦
        for (int i = 0; i < numberOfGames; i++) {
            graph = new GridGraph(10, 10);
            if (outputLevel >= 3) {
                graph.printWeight();
            }
            gameRecord[i] = new int[sequenceList.size()][graph.getNumberOfNodes()][2];
            for (int[][] sequenceRecord : gameRecord[i]) {
                for (int[] nodeInfo : sequenceRecord) {
                    Arrays.fill(nodeInfo, -1);
                }
            }

            for (int p = 0; p < numberOfPlayers; p++) {
                outputStreams[p].write((graph.toString()).getBytes()); // graph情報
                outputStreams[p].flush();

            }
            for (int s = 0; s < sequenceList.size(); s++) {

                int[] sequence = sequenceList.get(s);
                for (int p = 0; p < numberOfPlayers; p++) {
                    for (int num : sequence) {
                        outputStreams[p].write((num + "\n").getBytes()); // graph情報
                        outputStreams[p].flush();
                    }
                }
                GraphDrawing gui = null;
                if (isVisible) {
                    gui = new GraphDrawing(10, 10, graph.getNodeWeight(), names);
                }

                // 選択するノード数分のループ
                for (int j = 0; j < numberOfSelectNodes; j++) {
                    // 各プレイヤーのループ
                    for (int p : sequence) {
                        // それぞれの数字を取得
                        if (!processes[p].isAlive())
                            throw new IOException("次のプレイヤーのサブプロセスが停止しました :" + names[p]);
                        Thread thread = new GetResponseThread(p);
                        thread.start();
                        long start = System.nanoTime();
                        try {
                            thread.join(timeOut);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        long calculateTime = System.nanoTime() - start;
                        if (outputStr[p] == null)
                            throw new TimeoutException("一定時間以内に次のプレイヤーから値を取得できませんでした :" + names[p]);

                        int num;
                        try {
                            num = Integer.parseInt(outputStr[p]);
                            outputStr[p] = null;
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException(
                                    "次のプレイヤーから整数以外の値を取得しました :" + names[p] + " :" + outputStr[p]);
                        }
                        gain(p, num, gameRecord[i][s], names[p], calculateTime);

                        gameRecord[i][s][num][1] = j;
                        for (int pp : sequence) {
                            if (pp == p)
                                continue;
                            outputStreams[pp].write((num + "\n").getBytes());
                            outputStreams[pp].flush();
                        }
                        if (isVisible) {
                            gui.setColor(num / 10, num % 10, p);
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

                if (outputLevel >= 3) {
                    for (int a = 0; a < 10; a++) {
                        for (int b = 0; b < 10; b++) {
                            System.out.printf("%2d ", gameRecord[i][s][a * 10 + b][0]);
                        }
                        System.out.println();
                    }
                }

                // 勝ち点の計算
                int[] gainNodeInfo = new int[gameRecord[i][s].length];
                for (int t = 0; t < gainNodeInfo.length; t++) {
                    gainNodeInfo[t] = gameRecord[i][s][t][0];
                }
                gainRecord[i][s] = graph.evaluate(gainNodeInfo, numberOfPlayers, numberOfSelectNodes);
                int[] gamePoint = calcPoint(gainRecord[i][s]);
                if (outputLevel >= 2) {
                    System.out.printf("%2dゲーム，順列種%2d番の利得 (", i, s);
                    for (int a = 0; a < numberOfPlayers; a++) {
                        System.out.print("["+sequence[a]+"]"+names[a] + " ");
                    }
                    
                    System.out.print(") = ");
                    for (int num : gainRecord[i][s]) {
                        System.out.printf("%3d ", num);
                    }
                    System.out.print(" | 点数: ");
                    for (int num : gamePoint) {
                        System.out.printf("%3d ", num);
                    }
                    System.out.println();

                }
                for (int t = 0; t < numberOfPlayers; t++) {
                    playerPoints[t] += gamePoint[t];
                }
                if (isVisible) {
                    gui.setColor(graph.getPlaneGain());
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gui.dispose();
                }
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

}