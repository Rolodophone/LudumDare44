import processing.core.PImage

class Gui {

    class Health {
        val img: PImage = p.loadImage("player.png")

        fun update(x: Float, y: Float) {
            p.image(img, 50f, p.height - 60f, 33f, 45f)
            p.fill(255)
            p.textSize(30f)
            p.text("× ${player.lives}", 85f, p.height - 50f)
        }
    }

    class Title {
        val img: PImage = p.loadImage("mainmenu.png")

        fun update() {
            p.image(img, p.width / 2f, 230f)
        }
    }

    class Button(val image: String, val x: Float, val y: Float, val onClick: () -> Unit) {
        val img: PImage = p.loadImage(image)
        val l = x - 160f
        val r = x + 160f
        val t = y - 80f
        val b = y + 80f

        fun update() {
            p.image(img, x, y)

            if (p.mousePressed && p.mouseX > l && p.mouseX < r && p.mouseX > t && p.mouseX < b) {
                onClick()
            }
        }
    }

    var state = "main"

    val health = Health()
    val title = Title()
    val playBtn = Button("playButton.png", p.width / 4f, p.height - 300f) { state = "game" }
    val shopBtn = Button("shopButton.png", p.width * 3f / 4f, p.height - 300f) { state = "shop" }
    val quitBtn = Button("quitButton.png", p.width / 4f, p.height - 110f) { p.exit() }
    val howBtn = Button("howButton.png", p.width * 3f / 4f, p.height - 110f) { state = "how" }

    fun update() {
        when (state) {
            "main" -> {
                title.update()
                playBtn.update()
                shopBtn.update()
                quitBtn.update()
                howBtn.update()
            }

            "game" -> {
                health.update(50f, p.height - 60f)
                player.update()
                background.update()
                enemies.update()
            }

            "shop" -> {

            }

            "how" -> {

            }
        }
    }
}