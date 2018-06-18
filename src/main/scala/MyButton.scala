import scalafx.Includes._

import javafx.scene.input.MouseButton
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent

case class MyButton(pos_x: Int, pos_y: Int, width: Int, height: Int, imgPath: String = "") extends ImageView(new Image(imgPath)) {
  x = pos_x
  y = pos_y
  fitWidth = width
  fitHeight = height
  var i = 1
  onMouseClicked = (m: MouseEvent) => {
    println("CLCK" + i)
    println(m.getX)
    println(m.getY)
    i += 1
    /*if(m.getX >= x.asInstanceOf[Int] && m.getX <= x.asInstanceOf[Int] + width && m.getY >= y.asInstanceOf[Int] && m.getY <= y.asInstanceOf[Int] + height){
      println("BUTTON!")
    }*/

/*

    println("BUTTON")
    if(m.getButton == MouseButton.PRIMARY && m.getX >10 && m.getX < 30){
      println("PIERWSZY")
    } else if(m.getButton == MouseButton.SECONDARY){
      println("DRUGI")
    } else if(m.getButton == MouseButton.MIDDLE){
      println("Srodek")
    }*/
  }
}