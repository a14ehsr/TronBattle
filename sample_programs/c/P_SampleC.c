#include <stdio.h>
#include <stdlib.h>

#define CONTINUE 0
#define FINISH 1
#define DEATH 0
#define UP 1
#define RIGHT 2
#define DOWN 3
#define LEFT 4
#define NOT_ACHIEVED -1

int numberOfPlayers;
int numberOfGames;
int timelimit; // millisecond
int width;
int height;

int playerCode; // your player code. 0 <= playerCode < numberOfPlayers
char* playerName = "C_Sample"; // Do not include spaces in your name.
 
/**
 * currentPostion[p] = {p.x, p.y}
 * Payer p's current location is (p.x, p.y).
 */
int **currentPosition;

/**
 * board[y][x] = status
 * Status is either NOT_ACHIEVED or player's code.
 * Be careful position status of location (x,y) saved board[y][x]
 * Array size is (height+2) * (width+2) bad Board size is height * width.
 * 1 <= x <= width, 1 <= y <= height.
 */
int **board;

void initialize();
int put();
void move(int player, int initX, int initY, int moveX, int moveY);

int main(void)
{
    initialize();
    for (int i = 0; i < numberOfGames; i++) {
        int continueFlag = CONTINUE;

        // initialize board and players position
        currentPosition = malloc(sizeof(int *) * numberOfPlayers);
		for(int p = 0; p < numberOfPlayers; p++) {
            currentPosition[p] = malloc(sizeof(int ) * 2);
        }

        board = malloc(sizeof(int *) * (height + 2));
        for(int j = 0; j <= height+1; j++) {
            board[j] = malloc(sizeof(int *) * (width + 2));
        }
        for(int y = 1; y <= height; y++) {
            for(int x = 1; x <= width; x++) {
                board[y][x] = NOT_ACHIEVED;
            }
        }

        while(continueFlag == CONTINUE){
            // receive all player positions.
            for (int p = 0; p < numberOfPlayers; p++) {
                int x0,y0,x1,y1;
                scanf("%d",&x0);
                scanf("%d",&y0);
                scanf("%d",&x1);
                scanf("%d",&y1);
                move(p, x0, y0, x1, y1);
            }

            // send your strategy.
            int direction = put();
            // int direction = LEFT
            
            printf("%d\n",direction);
            fflush(stdout);
            // receive continue flag.
            scanf("%d",&continueFlag);
        }
    }
}

/**
 * input parameters and send your name.
 */
void initialize()
{
    scanf("%d",&numberOfPlayers);
    scanf("%d",&numberOfGames);
    scanf("%d",&timelimit);
    scanf("%d",&playerCode);

    printf("%s\n",playerName);
    fflush(stdout);

    scanf("%d",&width);
    scanf("%d",&height);
}

/**
 * Your strategy
 * @return Direction {UP, DOWN, RIGHT, LEFT} or other parameter meaning you are dead.
 */
int put() {
    int x = currentPosition[playerCode][0];
    int y = currentPosition[playerCode][1];
    if(x == -1 && y == -1) {
        return DEATH;
    }
    if(y < height && board[y+1][x] == NOT_ACHIEVED) {
        return UP;
    }else if(x < width && board[y][x+1] == NOT_ACHIEVED) {
        return RIGHT;
    }else if(y > 1 && board[y-1][x] == NOT_ACHIEVED) {
        return DOWN;
    }else if(x > 1 && board[y][x-1] == NOT_ACHIEVED) {
        return LEFT;
    }
    return DEATH;
}

/**
 * Move player to point (moveX, moveY) and set first location.
 * @param player
 * @param initX
 * @param initY
 * @param moveX
 * @param moveY
 */
void move(int player, int initX, int initY, int moveX, int moveY) {
    if(moveX == -1) {
        for(int y = 1; y <= height; y++) {
            for(int x = 1; x <= width; x++) {
                if(board[y][x] == player) {
                    board[y][x] = NOT_ACHIEVED;
                }
            }
        }
        currentPosition[player][0] = -1;
        currentPosition[player][1] = -1;
        return;
    }
    board[initY][initX] = player;
    board[moveY][moveX] = player;
    currentPosition[player][0] = moveX;
    currentPosition[player][1] = moveY;
}
