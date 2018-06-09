package logic
import java.util.Random

class Bombs(row: Int, col: Int) {
    def initializeBombs(board: List[List[Cell]], quantity: Int): List[List[Cell]] = {
        val newboard = chooseCell(board)
        if (quantity > 1) initializeBombs(newboard, quantity - 1)
        else newboard
    }

    def placeBomb(board: List[List[Cell]], x: Int, y: Int): List[List[Cell]] = {
        board.updated(x, board(x).updated(y, Bomb(false)))
    }

    def chooseCell(board: List[List[Cell]]): List[List[Cell]] = {
        val x = new Random().nextInt(row)
        val y = new Random().nextInt(col)
        val cell = board(x)(y)
        cell match {
            case Bomb(_) => chooseCell(board)
            case _ => placeBomb(board, x, y)
        }
    }
}