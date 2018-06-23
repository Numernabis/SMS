package logic

abstract class Cell(state: Int)
case class Blank(state: Int) extends Cell(state)
case class Bomb(state: Int) extends Cell(state)
case class Hint(state: Int, val num: Int) extends Cell(state)