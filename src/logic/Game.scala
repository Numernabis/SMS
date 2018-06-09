package logic

class Game(row: Int, col: Int, quantity: Int, var board: List[List[Cell]] = Nil) {
    val bombs = new Bombs(row, col)
    val hints = new Hints(row, col)

    board = if (board != Nil) board else initializeBoard()

    private def initializeBoard(): List[List[Cell]] = {
        val board1 = List.tabulate(row)(_ => List.tabulate(col)(_ => new Blank(false)))
        val board2 = bombs.initializeBombs(board1, quantity)
        hints.initializeHints(board2, row, col)
    }

    def openCell(tuple: (Int, Int)): Game = {
        val x = tuple._1
        val y = tuple._2
        val cell = board(x)(y)
        cell match {
            case Blank(false) => openAdjacentCells(x, y)
            case Bomb(false) => {
                val newboard = board.updated(x, board(x).updated(y, Bomb(true)))
                new Game(row, col, quantity, newboard)
            }
            case Hint(false, num) => {
                val newboard = board.updated(x, board(x).updated(y, Hint(true, num)))
                new Game(row, col, quantity, newboard)
            }
            case _ => new Game(row, col, quantity, board)
        }
    }

    private def openAdjacentCells(x: Int, y: Int): Game = {
        val newboard = board.updated(x, board(x).updated(y, Blank(true)))
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
            case Blank(false) => true
            case Hint(false, _) => true
            case _ => false
        })
    }

    def hasActiveBomb(): Boolean = {
        board.flatten.exists(cell => cell match {
            case Bomb(true) => true
            case _ => false
        })
    }
}