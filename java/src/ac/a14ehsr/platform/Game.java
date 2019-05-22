package ac.a14ehsr.platform;

public abstract class Game {
    public static final int WIN = -111;
    public static final int DEATH = -222;
    int numberOfPlayer;

    /**
     * 全てのゲームを終えた時点でのポイント
     */
    int[] sumPoint;

    /**
     * 各ゲームレベルでのポイント
     */
    int[] gamePoint;

    /**
     * 各ゲームレベルでのプレイヤーの状態
     */
    int[] status;


    /**
     * ゲームを開始する時の一番最初の処理
     */
    abstract void initialize();

    /**
     * ゲーム状況をリセットする処理
     */
    abstract void clear();

    abstract void show();

    /**
     * playerが手を打つ
     * @param player
     */
    abstract void put(int player);

    /**
     * playerに行動不能等の
     * @param player
     */
    abstract void kill(int player);

    abstract void win(int player);

    /**
     * 順位付けを行い，それに合わせたポイントを付与する
     */
    abstract void rank();

}