package ac.a14ehsr.platform;

import java.util.Arrays;

public class Boad {
    private int height;
    private int width;
    private int[][] gained; // 1 <= x <= width, 1 <= y <= height

    public Boad(int width, int height) {
        this.width = width;
        this.height = height;
        gained = new int[width + 2][height + 2];
        for(int[] a : gained) {
            Arrays.fill(a, -1);
        }
    }

    /**
     * 盤面に対する獲得処理．獲得できないのであればfalseを返す
     * @param x 獲得するざ座標
     * @param y 獲得するざ座標
     * @param player 獲得するプレイヤー
     * @return
     */
    public boolean gain(int x, int y, int player, String[] names) {
        if(x < 1 || x > width || y < 1 || y > height) {
            System.out.println("ボードの範囲外に移動しようとしました | プレイヤー:" + names[player]);
            return false;
        }
        if(gained[x][y] != -1) {
            System.out.println("獲得済みのマスに移動しようとしました | プレイヤー:" + names[player]);
            return false;
        }
        gained[x][y] = player;
        return true;
    }

    public void show() {
        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("--");
        }
        System.out.println("|");
        for(int y = 1; y <= height; y++) {
            System.out.print("|");
            for(int x = 1; x <= width; x++) {
                System.out.print(gained[x][y] + " ");
            }
            System.out.println("|");
        }
        System.out.print("|");
        for(int x = 1; x <= width; x++) {
            System.out.print("--");
        }
        System.out.println("|");
    }
}