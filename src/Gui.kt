import processing.core.PImage
import processing.sound.SoundFile

class Gui {

    class Health {
        val img: PImage = p.loadImage("player.png")

        fun update() {
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

    class Button(val image: String, val onClick: () -> Unit) {
        val img: PImage = p.loadImage(image)

        fun update(x: Float, y: Float) {
            val l = x - img.width / 2
            val r = x + img.width / 2
            val t = y - img.height / 2
            val b = y + img.height / 2

            p.image(img, x, y)

            if (p.mousePressed && p.mouseX > l && p.mouseX < r && p.mouseY > t && p.mouseY < b) onClick()
        }
    }

    var state = "shop"

    val gameLoop: SoundFile = SoundFile(p, "crystals.wav")

    val health = Health()
    val title = Title()
    val playBtn = Button("playButton.png") { state = "game"; gameLoop.loop() }
    val shopBtn = Button("shopButton.png") { state = "shop" }
    val quitBtn = Button("quitButton.png") { p.exit() }
    val infoBtn = Button("infoButton.png") { state = "info" }
    val backBtn = Button("backButton.png") { state = "menu"; gameLoop.stop() }
    //val leftBtn = Button("leftButton.png")
    val margin = 15f

    fun update() {
        when (state) {
            "menu" -> {
                title.update()
                playBtn.update(p.width / 4f, p.height - 300f)
                shopBtn.update(p.width * 3f / 4f, p.height - 300f)
                quitBtn.update(p.width / 4f, p.height - 110f)
                infoBtn.update(p.width * 3f / 4f, p.height - 110f)
            }

            "game" -> {
                health.update()
                player.update()
                background.update()
                enemies.update()
            }

            "shop" -> {
                p.textSize(90f)
                p.text("SHOP", 55f, margin + 75f)

                p.textSize(60f)
                p.text("Items", 40f, margin * 2 + 100f)
                p.text("Upgrades", 40f, p.height - margin * 6)

                health.update()
                backBtn.update(p.width - 120f, 70f)
            }

            "info" -> {

            }
        }
    }
}