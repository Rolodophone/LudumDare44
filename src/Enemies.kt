import processing.core.PConstants.TAU
import processing.core.PImage

class Enemies {
    abstract class Enemy {
        var x: Float = p.random(0f, p.width.toFloat())
        var y: Float = -200f
        abstract val w: Float
        abstract val h: Float

        abstract fun update()
    }

    class Asteroid : Enemy() {
        private val type = p.random(1f, 5f).toInt()
        private val img: PImage = p.loadImage("asteroid$type.png")
        override var w = if (type % 2 == 1) 60f else 90f
        override var h = if (type % 2 == 1) 60f else 90f
        var rotation = p.random(0f, TAU)
        val spin = p.random(0f, 0.2f) //radians
        val xSpeed = p.random(-1.7f, 1.7f)
        val ySpeed = p.random(2f, 4.5f)

        override fun update(){
            x += xSpeed
            y += ySpeed
            rotation = (rotation + spin) % TAU

            whileRotated(x, y, rotation) {
                p.image(img, 0f, 0f)
            }
        }
    }

    class Ufo : Enemy() {
        class Bullet(val x: Float, var y: Float) {
            val img: PImage = p.loadImage("ufoBullet.png")
            val speed = 8f

            fun update() {
                y += speed
                p.image(img, x, y)
            }
        }

        var bullets = mutableListOf<Bullet>()
        val img: PImage = p.loadImage("ufo.png")
        override val w = 90f
        override val h = 24f
        val speed = 3f

        override fun update() {
            //move towards player
            if (player.x > this.x) x += speed
            else x -= speed

            y += speed

            //shoot every 40 frames
            if (p.frameCount % 40 == 0) bullets.add(Bullet(x, y))

            //update bullets
            for (bullet in bullets) bullet.update()

            p.image(img, x, y)
        }
    }


    class EnemyShip : Enemy() {

        class Bullet(val x: Float, var y: Float) {
            val img: PImage = p.loadImage("enemyShipBullet.png")
            val speed = 8f

            fun update() {
                y += speed
                p.image(img, x, y)
            }
        }

        val img: PImage = p.loadImage("enemyShip.png")
        override val w = 124f
        override val h = 108f
        val speed = 6f
        var noiseX = 0f
        var bullets = mutableListOf<Bullet>()

        override fun update() {
            attack()
            for (bullet in bullets) bullet.update()
            p.image(img, x, y)
        }

        private fun attack() {
            for (bullet in player.bullets) {

                // if the bullet is close
                if (bullet.y - 300 < this.y && (bullet.x + 300 > this.x || bullet.x - 300 < this.x)) {
                    if (bullet.x > this.x) x -= speed
                    else x += speed

                    y -= speed

                    return
                }
            }

            // if no bullets need to be dodged:
            if (player.x > this.x) x += speed
            else x -= speed
            y += speed

            //shoot sometimes
            if (p.frameCount % map(p.noise(noiseX), 0f, 1f, 8f, 80f).toInt() == 0) {
                noiseX += 0.01f
                bullets.add(Bullet(x, y))
            }

            return
        }
    }


    var enemyList = mutableListOf<Enemy>()
    var spawnInterval = 1000 // milliseconds
    var timeOfLastSpawn = p.millis()

    fun update(){
        for (enemy in enemyList) enemy.update()

        val now = p.millis()

        //only spawn enemies every second miniumum
        if (now - timeOfLastSpawn > spawnInterval) {

            when {
                p.random(20f).toInt() == 0 -> {
                    enemyList.add(Asteroid())
                    timeOfLastSpawn = now
                }
                p.random(140f).toInt() == 0 -> {
                    enemyList.add(Ufo())
                    timeOfLastSpawn = now
                }
                p.random(250f).toInt() == 0 -> {
                    enemyList.add(EnemyShip())
                    timeOfLastSpawn = now
                }
            }
        }
    }
}

private fun map(value: Float, istart: Float, istop: Float, ostart: Float, ostop: Float) =
    ostart + (ostop - ostart) * ((value - istart) / (istop - istart))
