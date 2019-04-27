import processing.core.PImage

class Player {

    class Bullet(val img: PImage, val x:Float, var y: Float){
        val speed = 10f

        fun update(){
            y -= speed
            p.image(img, x, y)
        }
    }


    var bullets = mutableListOf<Bullet>()
    var img: PImage = p.loadImage("player.png")
    var x = p.width / 2f
    var y = p.height - 75f
    var speed = 5f
    var bulletImg: PImage = p.loadImage("bullet1.png")
    var bulletInterval = 500 //milliseconds
    var timeOfLastBullet = p.millis()
    var lives = 200

    fun update() {
        this.render()
        for (bullet in bullets) bullet.update()
    }

    private fun render() {
        p.image(img, x, y)
    }

    fun tryShoot(){
        val currentTime = p.millis()

        if (currentTime - timeOfLastBullet > bulletInterval) {
            bullets.add(Bullet(bulletImg, x, y))
            timeOfLastBullet = currentTime
        }
    }
}