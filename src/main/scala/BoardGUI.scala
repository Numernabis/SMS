import javafx.scene.input.MouseButton
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle


object BoardGUI extends JFXApp {
/*
  val button = new Button {
    onAction = (event: ActionEvent) =>  {
      println("Pressed")
      val e = event
      println(e)
    }
    graphic = new ImageView {image = new Image(this, "2.png")}
    if(MouseEvent.BUTTON3 == 1){
      println("Right")
    }
  }*/
/*
  val myLayout = new HBox{
    children = List(button)
  }*/

  val tileWidth = 40
  val tileHeight = 40

  val tilesX = 10
  val tilesY = 15

  var board = Array.ofDim[ImageView](tilesY,tilesX)
  for(j <- 0 to tilesX - 1; i <- 0 to tilesY - 1){
    val im = new ImageView(new Image("tile.png"))
    im.x = j * tileWidth
    im.y = i * tileHeight
    im.fitHeight = tileHeight
    im.fitWidth = tileWidth
    board(i)(j) = im
  }

  var boardFlat = board.flatten


  val myScene = new Scene(tileWidth * tilesX, tileHeight * tilesY){


    val rect = Rectangle(0,0,30,30)
    rect.fill = Color.Blue




    content = boardFlat
    onMouseMoved = (m: MouseEvent) => {
      rect.x = m.getX - 0.5 * rect.width()
      rect.y = m.getY - 0.5 * rect.height()
    }

    onMouseClicked = (m: MouseEvent) => {
      val nr_x = (m.getX/tileWidth).asInstanceOf[Int]
      val nr_y = (m.getY/tileHeight).asInstanceOf[Int]

      if(m.getButton == MouseButton.PRIMARY && m.getX >10 && m.getX < 30){
        println("PIERWSZY")
      } else if(m.getButton == MouseButton.SECONDARY){
        println("DRUGI")
      } else if(m.getButton == MouseButton.MIDDLE){
        println("Srodek")
      }

      println(nr_x, nr_y)
      putImg(nr_x, nr_y, "tile_pressed.png")
    }

    def putImg(x: Int, y: Int, name: String): Unit ={
      val im = new ImageView(new Image(name))
      im.x = x * tileWidth
      im.y = y * tileHeight
      im.fitHeight = tileHeight
      im.fitWidth = tileWidth
      board(y)(x) = im
      var boardFlat = board.flatten
      content = boardFlat
    }


  }

  stage = new JFXApp.PrimaryStage{
    scene = myScene
    title = "TADA"
  }



}
