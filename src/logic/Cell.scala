abstract class Cell(opened: Boolean)

case class Blank(override val opened: Boolean) extends Cell(opened)
case class Bomb(override val opened: Boolean) extends Cell(opened)
case class Hint(override val opened: Boolean, val num: Int) extends Cell(opened)