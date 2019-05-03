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

    class Image(val image: String) {
        val img: PImage = p.loadImage(image)

        fun update() {
            p.image(img, p.width / 2f, 230f)
        }
    }

    class Button(val image: String, val x: Float, val y: Float, var onClick: () -> Unit) {
        val img: PImage = p.loadImage(image)

        fun update() {
            p.image(img, x, y)
        }
    }

    var state = "menu"

    val gameLoop: SoundFile = SoundFile(p, "crystals.wav")
    val mainLoop: SoundFile = SoundFile(p, "ChildrensPlayground.wav")
    val menuLoop: SoundFile = SoundFile(p, "HotelJupiter.wav")
    val error: SoundFile = SoundFile(p, "error.wav")
    val upgrade: SoundFile = SoundFile(p, "upgrade.wav")
    val select: SoundFile = SoundFile(p, "select.wav")

    val health = Health()
    val title = Image("mainmenu.png")
    val gameOver = Image("gameover.png")

    val playBtn = Button("playButton.png", p.width / 4f, p.height - 300f) {
        select.stop(); if (!sfxAreMuted) select.play(); state =
        "game"; if (!musicIsMuted) gameLoop.loop(); mainLoop.stop()
    }
    val shopBtn = Button("shopButton.png", p.width * 3f / 4f, p.height - 300f) {
        select.stop(); if (!sfxAreMuted) select.play(); state =
        "shop"; if (!musicIsMuted) menuLoop.loop(); mainLoop.stop()
    }
    val quitBtn = Button("quitButton.png", p.width / 4f, p.height - 110f) {
        if (!sfxAreMuted) select.play(); p.exit()
    }
    val infoBtn = Button("infoButton.png", p.width * 3f / 4f, p.height - 110f) {
        select.stop(); if (!sfxAreMuted) select.play(); state =
        "info"; if (!musicIsMuted) menuLoop.loop(); mainLoop.stop()
    }

    val backBtn = Button("backButton.png", p.width - 110f, 60f) {
        select.stop(); if (!sfxAreMuted) select.play(); state =
        "menu"; if (!musicIsMuted) mainLoop.loop(); gameLoop.stop(); menuLoop.stop()
    }

    val bSpeed = Button("upgradeButton.png", (p.width / 2) - 70f, 180f) {
        if (player.lives > bSpeedLvl * 5) {
            Player.Bullet.speed += 2
            player.lives -= bSpeedLvl * 5
            bSpeedLvl++
            upgrade.stop()
            if (!sfxAreMuted) upgrade.play()
            totalLvl++
        } else {
            error.stop()
            if (!sfxAreMuted) error.play()
        }
    }
    val mSpeed = Button("upgradeButton.png", p.width - 70f, (p.height + 150f) / 2f + 20f) {
        if (player.lives > mSpeedLvl * 5) {
            player.speed += 2
            player.lives -= mSpeedLvl * 5
            mSpeedLvl++
            upgrade.stop()
            if (!sfxAreMuted) upgrade.play()
            totalLvl++
        } else {
            error.stop()
            if (!sfxAreMuted) error.play()
        }
    }
    val lGain = Button("upgradeButton.png", (p.width / 2) - 70f, (p.height + 150f) / 2f + 20f) {
        if (player.lives > lGainLvl * 5) {
            Player.lGain += 1
            player.lives -= lGainLvl * 5
            lGainLvl++
            upgrade.stop()
            if (!sfxAreMuted) upgrade.play()
            totalLvl++
        } else {
            error.stop()
            if (!sfxAreMuted) error.play()
        }
    }
    val reload = Button("upgradeButton.png", p.width - 70f, 180f) {
        if (player.lives > reloadLvl * 5) {
            player.bulletInterval -= 50
            player.lives -= reloadLvl * 5
            reloadLvl++
            upgrade.stop()
            if (!sfxAreMuted) upgrade.play()
            totalLvl++
        } else {
            error.stop()
            if (!sfxAreMuted) error.play()
        }
    }

    val playAgainBtn = Button("playButton.png", p.width / 4f, p.height - 110f) {
        gameLoop.stop()
        select.stop()
        if (!sfxAreMuted) select.play()
        player = Player()
        background = Background()
        enemies = Enemies()
        gui = Gui()
        state = "menu"
    }
    val quitNowBtn =
        Button(
            "quitButton.png",
            p.width * 3f / 4f,
            p.height - 110f
        ) { select.stop(); if (!sfxAreMuted) select.play(); p.exit() }

    var bSpeedLvl = 1
    var mSpeedLvl = 1
    var lGainLvl = 1
    var reloadLvl = 1
    var totalLvl = 4

    var musicIsMuted = false
    var sfxAreMuted = false

    init {
        if (!musicIsMuted) mainLoop.loop()
    }

    fun update() {
        when (state) {
            "menu" -> {
                title.update()
                playBtn.update()
                shopBtn.update()
                quitBtn.update()
                infoBtn.update()
            }

            "game" -> {
                health.update()
                player.update()
                background.update()
                enemies.update()
            }

            "shop" -> {
                p.textSize(90f)
                p.text("SHOP", 55f, 90f)

                p.textSize(30f)

                p.text("Bullet speed", 55f, 160f)
                p.text("Lvl: $bSpeedLvl", 55f, 190f)
                bSpeed.update()

                p.text("Reload", p.width / 2f, 160f)
                p.text("Lvl: $reloadLvl", p.width / 2f, 190f)
                reload.update()

                p.text("Life gain", 55f, (p.height + 150f) / 2f)
                p.text("Lvl: $lGainLvl", 55f, (p.height + 150f) / 2f + 30f)
                lGain.update()

                p.text("Movement speed", p.width / 2f, (p.height + 150f) / 2f)
                p.text("Lvl: $mSpeedLvl", p.width / 2f, (p.height + 150f) / 2f + 30f)
                mSpeed.update()

                health.update()
                backBtn.update()
            }

            "info" -> {
                p.textSize(90f)
                p.text("INFO", 55f, 90f)
                backBtn.update()

                p.textSize(30f)
                p.text("Code:", 55f, 150f); p.text("me", 170f, 150f)
                p.text("'Art':", 55f, 200f); p.text("me", 170f, 200f)
                p.text("Music:", 55f, 250f); p.text("my good friend Meerko", 170f, 250f)
            }

            "game over" -> {
                gameOver.update()

                p.textSize(60f)
                p.text("Highest number of lives: ${player.maxLives}", 105f, p.height - 300f)

                playAgainBtn.update()
                quitNowBtn.update()
            }
        }
    }
}