import processing.core.PImage
import processing.sound.SoundFile

class Player {

    class Bullet(val img: PImage, val x:Float, var y: Float){
        companion object {
            var speed = 10f
        }

        val w = 3f
        val h = 17f
        var dead = false

        fun update(){
            y -= speed
            p.image(img, x, y)

            for (enemy in enemies.enemyList) {
                if (collisions.hasCollided(this.x, this.y, this.w, this.h, enemy.x, enemy.yhit, enemy.w, enemy.hhit)) {
                    enemy.die()
                    this.dead = true

                    if (enemy !is Enemies.Asteroid) {
                        player.lives += lGain
                    }
                }
            }
        }
    }

    companion object {
        var lGain = 1
    }

    var bullets = mutableListOf<Bullet>()
    var img: PImage = p.loadImage("player.png")
    var x = p.width / 2f
    var y = p.height - 75f
    val w = 55f
    val h = 75f
    var speed = 5f
    var bulletImg: PImage = p.loadImage("bullet1.png")
    var bulletInterval = 500 //milliseconds
    var timeOfLastBullet = p.millis()
    var lives = 3
    var maxLives = 3

    val playerHit: SoundFile = SoundFile(p, "playerHit.wav")
    val shoot: SoundFile = SoundFile(p, "shoot.wav")
    val gameOver: SoundFile = SoundFile(p, "gameOver.wav")

    fun update() {
        this.render()
        for (bullet in bullets) bullet.update()
        bullets.removeIf { bullet -> bullet.dead || bullet.y < -200 }
        if (lives > maxLives) maxLives = lives
    }

    private fun render() {
        p.image(img, x, y)
    }

    fun tryShoot(){
        val currentTime = p.millis()

        if (currentTime - timeOfLastBullet > bulletInterval) {
            bullets.add(Bullet(bulletImg, x, y))
            timeOfLastBullet = currentTime

            shoot.stop()
            shoot.play()
        }
    }

    fun hit(dmg: Int) {
        lives -= dmg
        playerHit.play()

        if (lives <= 0) {
            gui.state = "game over"
            gui.gameLoop.stop()
            gameOver.play()
        }
    }
}