import logic._
import javafx.scene.input.MouseButton
import scalafx.Includes._
import scalafx.scene.input.MouseEvent
import scalafx.application.JFXApp
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.control.{Button, RadioButton, ToggleGroup}
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.HBox
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text

object BoardGUI extends JFXApp {

  val imgNormal = "images/tile.png"
  val imgPressed = "images/tile_pressed.png"
  val imgBomb = "images/bomb.png"
  val imgFlag = "images/flag.png"
  val imgHint = new Array[String](9)
  for (i <- 1 until 9) {
    imgHint(i) = s"images/$i.png"
  }
  var gameRunning = false

  val tileWidth = 40
  val tileHeight = 40

  var tilesX = 10
  var tilesY = 10
  var bombNr = 5
  var logicBoard: Game = new Game(tilesX, tilesY, bombNr)
  var gameScene: Scene = makeScene(tilesX, tilesY)

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


  val startText = new HBox {
    autosize()
    layoutX = 0
    layoutY = 0

    padding = Insets(20)
    children = Seq(
      new Text {
        text = "Welcome to MineSweeper!"
        style = "-fx-font-size: 35pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(BlueViolet, DarkRed))
      }
    )
  }

  val levelText = new HBox {
    autosize()
    layoutX = 170
    layoutY = 100

    padding = Insets(20)
    children = Seq(
      new Text {
        text = "Choose level"
        style = "-fx-font-size: 25pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(BlueViolet, Black))
      }
    )
  }

  val authors = new HBox {
    autosize()
    layoutX = 450
    layoutY = 310

    padding = Insets(20)
    children = Seq(
      new Text {
        text = "Made by: \nLudwik Ciechański \nWojciech Wańczyk"
        style = "-fx-font-family:  Helvetica"
        fill = new LinearGradient(
          stops = Stops(Black, Black))
      }
    )
  }


  val level1: RadioButton = new RadioButton("Easy") {
    layoutX = 250
    layoutY = 200
  }
  val level2: RadioButton = new RadioButton("Medium") {
    layoutX = 250
    layoutY = 230
  }

  val level3: RadioButton = new RadioButton("Hard") {
    layoutX = 250
    layoutY = 260
  }
  val toggle = new ToggleGroup()
  toggle.toggles = List(level1, level2, level3)


  val startButton: Button = new Button("    Start!    ") {
    layoutX = 250
    layoutY = 300

    onMouseClicked = (m: MouseEvent) => {
      if (!(toggle.getSelectedToggle == null)) {

        if (level1.selected()) {
          tilesX = 10
          tilesY = 10
          bombNr = 5
        } else if (level2.selected()) {
          tilesX = 15
          tilesY = 15
          bombNr = 15
        } else if (level3.selected()) {
          tilesX = 20
          tilesY = 15
          bombNr = 40
        }

        gameRunning = true
        gameScene = makeScene(tilesX, tilesY)
        logicBoard = new Game(tilesX, tilesY, bombNr)
        imgBoard = Array.ofDim[ImageView](tilesY, tilesX)
        refresh()
        stage.scene = gameScene
      }
    }
  }


  val imgStart1: ImageView = new ImageView(new Image("images/flag_white.png")){
    x = 20
    y = 170
    fitHeight = 200
    fitWidth = 150
  }

  val imgStart2: ImageView = new ImageView(new Image("images/bomb_white.png")){
    x = 390
    y = 130
    fitHeight = 150
    fitWidth = 180
    rotate = 15
  }


  val welcomeScreen = List(startButton, level1, level2, level3, startText, authors,
    levelText, imgStart1, imgStart2)

  val welcomeScene: Scene = new Scene(600, 400) {
    content = welcomeScreen
  }

  // make scene prepared to game with defined size and mouse behaviour
  def makeScene(x: Int, y: Int): Scene = {
    new Scene(x * tileWidth, y * tileHeight) {
      onMouseClicked = (m: MouseEvent) => {

        if (gameRunning) {
          val nr_x = (m.getX / tileWidth).asInstanceOf[Int]
          val nr_y = (m.getY / tileHeight).asInstanceOf[Int]
          //println(nr_x, nr_y)

          m.getButton match {
            case MouseButton.PRIMARY =>
              logicBoard = logicBoard.openCell(nr_x, nr_y)
            case MouseButton.SECONDARY =>
              logicBoard = logicBoard.putFlag(nr_x, nr_y)
            case _ =>
              println("Unknown pattern with Mouse Button")
          }
          refresh()
        } else {
          logicBoard = new Game(tilesX, tilesY, bombNr)
          stage.scene = welcomeScene
        }
      }
    }
  }

  val gameOver: HBox = new HBox {
    padding = Insets(20)
    children = Seq(
      new Text {
        text = "Game Over!"
        style = "-fx-font-size: 48pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(Red, DarkRed))
        effect = new DropShadow {
          color = Black
          radius = 25
          spread = 0.25
        }
      }
    )
  }

  def putGameOver(): Unit = {
    gameOver.layoutX = tilesX * tileWidth / 2 - 190
    gameOver.layoutY = tilesY * tileHeight / 2 - 80
    gameScene.content += gameOver
  }


  val winner: HBox = new HBox {
    padding = Insets(20)
    children = Seq(
      new Text {
        text = "Winner!"
        style = "-fx-font-size: 48pt"
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(Yellow, Red))
        effect = new DropShadow {
          color = Black
          radius = 25
          spread = 0.25
        }
      }
    )
  }

  def putWinner(): Unit = {
    winner.layoutX = tilesX * tileWidth / 2 - 130
    winner.layoutY = tilesY * tileHeight / 2 - 80
    gameScene.content += winner
  }


  def flattenAndReplace(): Unit = {
    boardFlat = imgBoard.flatten
    gameScene.content = boardFlat
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
        case Blank(0) | Bomb(0) | Hint(0, _) => {
          putImg(j, i, imgNormal)
        }
        case Blank(1) => putImg(j, i, imgPressed)
        case Hint(1, x) => putImg(j, i, imgHint(x))
        case Bomb(1) => {
          putImg(j, i, imgBomb)
          gameRunning = false
        }
        case Blank(2) | Bomb(2) | Hint(2, _) => {
          putImg(j, i, imgFlag)
        }
        case _ =>
      }
    }

    flattenAndReplace()

    if (!gameRunning) {
      for (j <- 0 until tilesX; i <- 0 until tilesY) {
        logicBoard.getCell(j, i) match {
          case Bomb(_) => {
            putImg(j, i, imgBomb)
          }
          case _ =>
        }
      }
      flattenAndReplace()
      putGameOver()
    } else if (logicBoard.hasOnlyBombs()) {
      putWinner()
      gameRunning = false
    }
  }

  stage = new JFXApp.PrimaryStage {
    scene = welcomeScene
    title = "Mine Sweeper"
  }
}
