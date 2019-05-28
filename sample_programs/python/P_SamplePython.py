import sys,random

CONTINUE = 0
FINISH = 1
DEATH = 0
UP = 1
RIGHT = 2
DOWN = 3
LEFT = 4
NOT_ACHIEVED = -1

"""
Your strategy
@return Direction {UP, DOWN, RIGHT, LEFT} or other parameter meaning you are dead.
"""
def put():
    global currentPosition
    x = currentPosition[playerCode][0]
    y = currentPosition[playerCode][1]
    if x == -1 and y == -1 :
        return DEATH;
    if y < height and board[y+1][x] == NOT_ACHIEVED:
        return UP
    elif x < width and board[y][x+1] == NOT_ACHIEVED:
        return RIGHT
    elif y > 1 and board[y-1][x] == NOT_ACHIEVED:
        return DOWN
    elif x > 1 and board[y][x-1] == NOT_ACHIEVED:
        return LEFT
    return DEATH

"""
Move player to point (moveX, moveY) and set first location.
@param player
@param initX
@param initY
@param moveX
@param moveY
"""
def move(player, initX, initY, moveX, moveY):
    global currentPosition
    global board
    if moveX == -1:
        for y in range(1, height+1):
            for x in range(1, width + 1):
                if board[y][x] == player:
                    board[y][x] = NOT_ACHIEVED

        currentPosition[player][0] = -1
        currentPosition[player][1] = -1
        return

    board[initY][initX] = player
    board[moveY][moveX] = player
    currentPosition[player][0] = moveX
    currentPosition[player][1] = moveY


# input parameters and send your name.
playerName = "Python_Sample" # Do not include spaces in your name.
numberOfPlayers = int(sys.stdin.readline())
numberOfGames = int(sys.stdin.readline())
timelimit = int(sys.stdin.readline()) # millisecond
playerCode = int(sys.stdin.readline()) # your player code. 0 <= playerCode < numberOfPlayers

print(playerName, flush=True)

width = int(sys.stdin.readline())
height = int(sys.stdin.readline())
 
for i in range(numberOfGames):
    continueFlag = True

    # initialize board and players position
    """
    currentPostion[p] = {p.x, p.y}
    Payer p's current location is (p.x, p.y).
    """
    currentPosition = [[0,0] for p in range(numberOfPlayers)]

    """
    board[y][x] = status
    Status is either NOT_ACHIEVED or player's code.
    Be careful position status of location (x,y) saved board[y][x]
    Array size is (height+2) * (width+2) bad Board size is height * width.
    1 <= x <= width, 1 <= y <= height.
    """
    board = [[NOT_ACHIEVED for x in range(width + 2)] for y in range(height + 2)]

    while continueFlag:
        # receive all player positions.
        for p in range(numberOfPlayers):
            line = sys.stdin.readline()
            nums = line.split(" ")
            x0 = int(nums[0])
            y0 = int(nums[1])
            x1 = int(nums[2])
            y1 = int(nums[3])
            move(p, x0, y0, x1, y1)

        # send your strategy.
        direction = put();
        print(direction, flush=True)
        # receive continue flag.
        if int(sys.stdin.readline()) == FINISH:
            continueFlag = False;

