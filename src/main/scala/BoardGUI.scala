import logic._
import javafx.scene.input.MouseButton
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}

object BoardGUI extends JFXApp {

    val imgNormal = "images/tile.png"
    val imgPressed = "images/tile_pressed.png"
    val imgBomb = "images/bomb.png"
    val imgFlag = "images/flag.png"
    val imgHint = new Array[String](9)
    for (i <- 1 until 9) {
        imgHint(i) = s"images/$i.png"
    }

    val tileWidth = 40
    val tileHeight = 40

    val tilesX = 15 //TODO: jako zmienne, w zależności od poziomu
    val tilesY = 10 //TODO: jw.
    val bombNr = 9  //TODO: jw.

    var logicBoard = new Game(tilesX, tilesY, bombNr)

    var imgBoard = Array.ofDim[ImageView](tilesY, tilesX)
    for (j <- 0 until tilesX; i <- 0 until tilesY) {
        val im = new ImageView(new Image(imgNormal))
        im.x = j * tileWidth
        im.y = i * tileHeight
        im.fitHeight = tileHeight
        im.fitWidth = tileWidth
        imgBoard(i)(j) = im
    }
    var boardFlat = imgBoard.flatten

    val myScene: Scene = new Scene(tileWidth * tilesX, tileHeight * tilesY) {

        content = boardFlat

        onMouseClicked = (m: MouseEvent) => {
            val nr_x = (m.getX / tileWidth).asInstanceOf[Int]
            val nr_y = (m.getY / tileHeight).asInstanceOf[Int]
            //println(nr_x, nr_y)

            m.getButton match {
                case MouseButton.PRIMARY =>
                    logicBoard = logicBoard.openCell(nr_x, nr_y)
                    refresh()
                case MouseButton.SECONDARY =>
                    //TODO: logicBoard.putFlag(nr_x, nr_y)
                    putImg(nr_x, nr_y, imgFlag)
                    flattenAndReplace()
                case MouseButton.MIDDLE =>
                    //czy potrzebujemy środkowego przycisku mychy?
                    //putImg(nr_x, nr_y, imgBomb)
                    //flattenAndReplace()
                case _ =>
                    println("Unknown pattern with Mouse Button")
            }

        }

        def flattenAndReplace(): Unit = {
            boardFlat = imgBoard.flatten
            content = boardFlat
        }

        def putImg(x: Int, y: Int, name: String): Unit = {
            val im = new ImageView(new Image(name))
            im.x = x * tileWidth
            im.y = y * tileHeight
            im.fitHeight = tileHeight
            im.fitWidth = tileWidth
            imgBoard(y)(x) = im
        }

        def refresh(): Unit = {

            for (j <- 0 until tilesX; i <- 0 until tilesY) {
                logicBoard.getCell(j, i) match {
                    case Hint(false, _)=>
                    case Bomb(false)   =>
                    case Blank(false)  =>

                    case Bomb(true)    => putImg(j, i, imgBomb)
                        //TODO: koniec gry
                    case Blank(true)   => putImg(j, i, imgPressed)
                    case Hint(true, x) => putImg(j, i, imgHint(x))
                    case _ =>
                }
            }

            //TODO: sprawdzenie czy wygrano

            flattenAndReplace()
        }
    }

    stage = new JFXApp.PrimaryStage {
        scene = myScene
        title = "Mine Sweeper"
    }
}
