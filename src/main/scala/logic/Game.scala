package logic

class Game(row: Int, col: Int, quantity: Int, var board: List[List[Cell]] = Nil) {
    val bombs = new Bombs(row, col)
    val hints = new Hints(row, col)

    board = if (board != Nil) board else initializeBoard()

    def initializeBoard(): List[List[Cell]] = {
        val board1 = List.tabulate(row)(_ => List.tabulate(col)(_ => new Blank(0)))
        val board2 = bombs.initializeBombs(board1, quantity)
        hints.initializeHints(board2, row, col)
    }

    def putFlag(tuple: (Int, Int)): Game = {
        val x = tuple._1
        val y = tuple._2
        val cell = board(x)(y)
        var newboard = board
        cell match {
            case Blank(0)     => newboard = board.updated(x, board(x).updated(y, Blank(2)))
            case Bomb(0)      => newboard = board.updated(x, board(x).updated(y, Bomb(2)))
            case Hint(0, num) => newboard = board.updated(x, board(x).updated(y, Hint(2, num)))

            case Blank(2)     => newboard = board.updated(x, board(x).updated(y, Blank(0)))
            case Bomb(2)      => newboard = board.updated(x, board(x).updated(y, Bomb(0)))
            case Hint(2, num) => newboard = board.updated(x, board(x).updated(y, Hint(0, num)))
            case _ =>
        }
        new Game(row, col, quantity, newboard)
    }

    def openCell(tuple: (Int, Int)): Game = {
        val x = tuple._1
        val y = tuple._2
        val cell = board(x)(y)
        cell match {
            case Blank(0) => openAdjacentCells(x, y)
            case Bomb(0) => {
                val newboard = board.updated(x, board(x).updated(y, Bomb(1)))
                new Game(row, col, quantity, newboard)
            }
            case Hint(0, num) => {
                val newboard = board.updated(x, board(x).updated(y, Hint(1, num)))
                new Game(row, col, quantity, newboard)
            }

            case _ => new Game(row, col, quantity, board) //flag or already opened
        }
    }

    def openAdjacentCells(x: Int, y: Int): Game = {
        val newboard = board.updated(x, board(x).updated(y, Blank(1)))
        val newGame = new Game(row, col, quantity, newboard)
        var adjacentCells: List[(Int, Int)] = Nil
        val adj = List(-1, 0, 1)
        for (i <- adj; j <- adj) {
            adjacentCells = getPosition(x + i, y + j) ++ adjacentCells
        }
        adjacentCells.foldLeft(newGame)((game, tuple) => game.openCell(tuple))
    }

    def getPosition(x: Int, y: Int): List[(Int, Int)] = {
        if (contains(x, y)) List(Tuple2(x, y))
        else Nil
    }

    def contains(x: Int, y: Int): Boolean = {
        x >= 0 && x < row && y >= 0 && y < col
    }

    def hasOnlyBombs(): Boolean = {
        !board.flatten.exists(cell => cell match {
            case Blank(0) => true
            case Hint(0, _) => true
            case Blank(2) => true
            case Hint(2, _) => true
            case _ => false
        })
    }

    def getCell(x: Int, y: Int): Cell = {
        if (contains(x, y)) board(x)(y)
        else null
    }
}

