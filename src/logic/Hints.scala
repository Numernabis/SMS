package logic

class Hints(row: Int, col: Int) {
    def initializeHints(board: List[List[Cell]], x: Int, y: Int): List[List[Cell]] = {
        List.tabulate(x, y)((i, j) => changeToHint(board, i, j))
    }

    def changeToHint(board: List[List[Cell]], x: Int, y: Int): Cell = {
        val cell = board(x)(y)
        cell match {
            case Bomb(b) => Bomb(b)
            case _ => calculateHint(board, x, y)
        }
    }

    def calculateHint(board: List[List[Cell]], x: Int, y: Int): Cell = {
        def countBombs(cell: Cell): Int = {
            cell match {
                case Bomb(_) => 1
                case _ => 0
            }
        }
        var adjacentCells: List[Cell] = Nil
        val adj = List(-1, 0, 1)
        for (i <- adj; j <- adj) {
            adjacentCells = getCell(board, x + i, y + j) ++ adjacentCells
        }
        val hintNum = (adjacentCells.map(countBombs)).foldLeft(0)(_ + _)
        hintNum match {
            case 0 => Blank(false)
            case _ => Hint(false, hintNum)
        }
    }

    def getCell(board: List[List[Cell]], x: Int, y: Int): List[Cell] = {
        if (contains(x, y)) List(board(x)(y))
        else Nil
    }

    def contains(x: Int, y: Int): Boolean = {
        x >= 0 && x < row && y >= 0 && y < col
    }
}