# TronBattle

Copyright © 2019年 K.Hirano All rights reserved. 

作成日2019/05/28

最終更新日2019/05/28



## 1. 環境の準備

ここでは，環境の準備をしながら対戦の実行方法の一例を紹介します．

### 1.1 実行環境構築

- Java1.8以降のインストール
- Python3系のインストール（Anacondaを推奨）
- Docker または，busyboxのダウンロード（Windowsの場合）

#### 補足

windows環境においてのみ必要としている操作は，Linuxライクに使えるターミナルを利用したいからです．

そのためMac, Linuxで実行する場合には不要です．

他にCygwinや統合ターミナルを持つVSCodeなどもありますが，Docker, busyboxの順に推奨します．

#### busyboxのダウンロード

以下の記事を参考にしながら，busybox64.exeをダウンロードしてください．

https://qiita.com/tetsuy/items/22cba0bc2048967b270a



### Dockerのダウンロード

略



### 1.2 clone

ターミナルで以下を実行

`git clone https://github.com/a14ehsr/VoronoiGame.git`

もしくはdownload zipからダウンロードして適当なディレクトリに解凍してください．  

### 1.3 カレントディレクトリを移動

busyboxの場合，コマンドプロンプトを起動し，busybox.exeを以下のようにして起動してください．

`busybox64.exe sh -l`

VoronoiGameのディレクトリまで移動してください．

ホームディレクトリ直下に配置した場合にはターミナルで以下を実行すれば良いです．

`cd VoronoiGame`



### 1.4 サンプルを動かしてみる

#### サンプルAIのコピー

`sample_programs`に各言語のサンプルが用意されているので，

プログラムをプールするディレクトリの`ai_programs` にコピーします．

以下を実行すれば良いです．

```
cp sample_programs/c/P_SampleC.c ai_programs/
cp sample_programs/cpp/P_SampleCPP.cpp ai_programs/
cp sample_programs/java/P_SampleJava.java ai_programs/
cp sample_programs/python/P_SamplePython.py ai_programs/
```



#### プラットフォームのコンパイル

以下を実行する．

```
sh shell/compile.sh
```

環境によってはshが使えない場合があるようです．その場合には`./shell/compile.sh`としてください．

#### AIのプログラムをコンパイルする

以下を実行することで，`ai_programs`内の`P_`から始まるプログラムを自動でコンパイルし，

実行コマンドリストのテキストファイルを生成します．

```a
sh shell/auto_compile.sh
```

##### python3コマンドが無いというエラーが出た場合

Windows環境でAnacondaをインストールした場合に，`python3`コマンドが生成されていない場合が確認されています．その場合には，`resoruce/config/python/run_command.txt`の`python3`を`python`に書き換えてください．（ただし，`python`コマンドによってPython3が使えることは先に確認してください．）



#### 対戦を実行する

##### 2人対戦

以下を実行することで，先に用意した対戦プログラムとプラットフォームが用意している単純なAIを合わせた

全ての組み合わせの対戦を実行します．

```
sh shell/auto_run.sh 2
```

以下の様な出力が出れば，問題ないです．

```
設定終了

<~略~>

players  : Java_Sample vs Sample_Random
GAME RESULT: 
           NAME POINT RANK
    Java_Sample    24    0
  Sample_Random    16    1
players  : Python_Sample vs Sample_Random
GAME RESULT: 
           NAME POINT RANK
  Python_Sample    22    0
  Sample_Random    18    1
RESULT
                       (    0)(    1)(    2)(    3)(    4) | r1 r2 VOID (times)
  0:          C_Sample   null      1      1      1      1  |  4  0    0
  1:        Cpp_Sample      1   null      1      2      1  |  3  1    0
  2:       Java_Sample      2      2   null      1      1  |  2  2    0
  3:     Python_Sample      1      1      1   null      1  |  4  0    0
  4:     Sample_Random      2      2      2      2   null  |  0  4    0
```

###### RESULTの見方

プレイヤー0がプレイヤー1と戦った時に，何位だったかを表し，

 r1(1位)を0回，r2(2位)を3回取り，VOID(無効試合)が何回と読みます．

対戦が存在しないセルにはnullが入ります．



##### 3人(多人数)対戦

上の例では，2人対戦を行ないましたが，以下の実行で3人対戦もできます．

実行できるか試してみてください．

```
sh shell/auto_run.sh 3
```

また，最大で6人まで対戦できます．人数の設定は上記の3を任意の数にすればできます．



## 2. AIプログラムの準備

1.3でコピーしたプログラムを改変して，それぞれのAIを作ります．

ファイル名を`P_` から始まる適当な名前に変更することを推奨します．

### 2.1 AIの作り方．

前準備として，playerNameを書き換えてください．(例: playerNmae = "Yamada_Taro") 
サンプルの関数`put`を修正して，設計したAIを実装します．

#### __コーディングにあたっての注意事項__

デバッグなどの出力を行いたい場合には，**必ず** 標準エラー出力を使用してください．

標準エラー関数は以下のようなものがそれぞれの言語にあります（他にもある場合があります）．

| 言語   | 関数                   |
| ------ | ---------------------- |
| C      | fprintf(stderr,"str"); |
| C++    | std::cerr >> str       |
| Java   | System.err.print(str)  |
| Python | sys.stderr.write(str)  |

### 2.2 サンプルプログラムの詳細

#### パラメタについて

| 変数名          | 説明                                                         |
| --------------- | ------------------------------------------------------------ |
| numberOfPlayers | 対戦プレイヤー数（何人対戦なのか）                           |
| numberOfGames   | 対戦ゲーム数                                                 |
| timelimit       | 制限時間(ミリ秒)                                             |
| playerCode      | プレイヤーの識別番号（0からnumberOfPlayers-1までのいずれか） |
| width           | グラフのノード数                                             |
| height          | グラフの辺数                                                 |
| currentPosition | 各プレイヤーの最新座標                                       |
| board           | グラフのノードの重み情報の1次元配列（ノードiの重みはweight[i]） |



## 3. サンプルと対戦

完成したプログラムで対戦させてみましょう．

### 3.1 コンパイル

作ったプログラムをコンパイルします．

普通にコンパイルしても良いですし，

プログラムが`ai_programs`にあるのであれば，

1.3で使った自動コンパイルプログラムを使用してください．

###### 再掲

```
sh shell/auto_compile.sh
```



### 3.2 サンプルと戦わせる

サンプルプログラムと対戦させます．以下のコマンドを実行してください. 

```
sh shell/run_sample.sh 実行コマンド
```

実行コマンドには`"./a.out"`や`"java P_Name"` などを入力してください．

コマンドを一つの文字列とみなす必要があるので，

実行コマンドに空白文字が含まれる場合には`"`で囲ってください．

サンプルは以下の内容です．

| 名前      | 戦略 |
| --------- | ---- |
| Ai_Random |      |

また，以下のコマンドでサンプルとの対戦をしつつ，描画もできます．

```
sh shell/run_sample_visible.sh 実行コマンド
```



### 3.3 細かにオプションを指定する

以下の実行コマンドに対し，複数のオプションを入力することで，対戦の設定や出力を変更できる．

```
sh shell/run_normal.sh
```

| オプション  | 説明                                                         | default |
| ----------- | ------------------------------------------------------------ | ------: |
| -p str      | 実行コマンドリストにstrを追加する                            |       - |
| -nop num    | 対戦人数をnumに設定する                                      |       2 |
| -game num   | ゲーム数を設定する                                           |      10 |
| -v          | 描画を行う<br />（ゲーム数やAIの数が多い時は終わらなくなるかもしれないので注意） |       - |
| -olevel num | 出力レベルをnumに設定する                                    |       1 |
| -sample     | trueの場合に対戦プログラムにサンプルAIを追加します．         |   false |
| -auto bool  | resource/command_list/command_list_green.txtの<br />実行コマンドを実行コマンドリストに追加する．<br />trueの場合，サンプルプログラムを一緒に追加する． |       - |

また，出力レベルは以下の通りである．

| レベル | 出力内容                                                     |
| ------ | ------------------------------------------------------------ |
| 1      | 対戦の結果（勝ち点）のみ                                     |
| 2      | レベル1の内容に加えてどのプレイヤーが死んだかと各ゲームごとの利得を出力 |
| 3      | レベル2の内容に加えて各プレイヤーの現在座標を出力            |
| 4      | レベル2の内容に加えて盤面の状況を出力                        |

また，以上のオプションを組み合わせたいくつかのshellscriptを用意している．

| 実行方法                                                     | 内容                                                |
| ------------------------------------------------------------ | --------------------------------------------------- |
| `sh shell/run.sh num "実行コマンド" "実行コマンド" ("実行コマンド")  ` | 対戦人数numで，実行コマンドを対戦させる．           |
| `sh shell/run_visible.sh num "実行コマンド" "実行コマンド" `("実行コマンド") | 対戦人数numで，実行コマンドを対戦させ，描画も行う． |



## 4. 動作確認済み環境

### Java1.8

```
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)
```

### Python

```
Python 3.6.1 :: Anaconda 4.4.0 (x86_64)
```

### PC

```
macOS 10.12.6
```



## 5. 多対多実行について

先に説明した通り，複数のプログラムで自動対戦を行う機能があります．

コンパイル処理はPythonで実装しています．

`ai_programs/`に使用するプログラムを配置してください．

なお，`P_`から始まるプログラムのみを対象にしています．

ディレクトリ内部を再帰的に探索して実行ファイルを取得するので，ディレクトリなどは適当な構成で問題ありません．



## 6. その他細かい設定等について

各言語のコンパイル，実行コマンドの変更やオプション指定ができます．

`resource/config/{各言語}/`の任意のファイルの中身を書き換えてください．



また，ゲーム数などのdefaultの数値は`resource/config/config.txt`に記述されています．

ここを変更することによって変更が可能です．

ただし実行時オプションでの指定も可能なので，そちらを推奨します．



対戦終了時の結果は`resource/result/{numberOfPlayers}PlayersResult.csv`にも記録されます．

必要に応じて参照やコピーを取るなどしてください．

