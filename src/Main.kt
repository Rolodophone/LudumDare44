import processing.core.PApplet
import processing.core.PConstants

lateinit var p: PApplet
lateinit var player: Player
lateinit var background: Background
lateinit var enemies: Enemies
lateinit var gui: Gui

class Main : PApplet(){
    var keysPressed = mutableMapOf("left" to false, "right" to false, "up" to false, "down" to false, "space" to false)

    override fun settings(){
        fullScreen(2)
    }

    override fun setup(){
        p = this

        imageMode(CENTER)

        player = Player()
        background = Background()
        enemies = Enemies()
        gui = Gui()
    }


    override fun draw(){
        background(0)
        handleKeys()
        gui.update()
    }


    override fun keyPressed(){
        if (key.toInt() == PConstants.CODED) {
            when (keyCode) {
                PConstants.LEFT -> keysPressed["left"] = true
                PConstants.RIGHT -> keysPressed["right"] = true
                PConstants.UP -> keysPressed["up"] = true
                PConstants.DOWN -> keysPressed["down"] = true
            }

        } else {
            when (key) {
                'a' -> keysPressed["left"] = true
                'd' -> keysPressed["right"] = true
                'w' -> keysPressed["up"] = true
                's' -> keysPressed["down"] = true
                ' ' -> keysPressed["space"] = true
            }
        }
    }

    override fun keyReleased(){
        if (key.toInt() == PConstants.CODED) {
            when (keyCode) {
                PConstants.LEFT -> keysPressed["left"] = false
                PConstants.RIGHT -> keysPressed["right"] = false
                PConstants.UP -> keysPressed["up"] = false
                PConstants.DOWN -> keysPressed["down"] = false
            }
        } else {
            when (key) {
                'a' -> keysPressed["left"] = false
                'd' -> keysPressed["right"] = false
                'w' -> keysPressed["up"] = false
                's' -> keysPressed["down"] = false
                ' ' -> keysPressed["space"] = false
            }
        }
    }

    private fun handleKeys(){
        if (keysPressed["left"]!!) player.x -= player.speed
        else if (keysPressed["right"]!!) player.x += player.speed
        if (keysPressed["up"]!!) player.y -= player.speed
        else if (keysPressed["down"]!!) player.y += player.speed
        if (keysPressed["space"]!!) player.tryShoot()
    }
}

fun whileRotated(x: Float, y: Float, radians: Float, block: () -> Unit) {
    p.pushMatrix()
    p.translate(x, y)
    p.rotate(radians)
    block()
    p.popMatrix()
}

fun main(){
    PApplet.main("Main")
}