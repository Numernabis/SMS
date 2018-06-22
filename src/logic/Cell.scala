package logic

abstract class Cell(opened: Boolean)
case class Blank(opened: Boolean) extends Cell(opened)
case class Bomb(opened: Boolean) extends Cell(opened)
case class Hint(opened: Boolean, val num: Int) extends Cell(opened)