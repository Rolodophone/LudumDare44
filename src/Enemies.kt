import processing.core.PConstants.TAU
import processing.core.PImage
import processing.sound.SoundFile

class Enemies {
    abstract class Enemy {
        var x: Float = p.random(0f, p.width.toFloat())
        var y: Float = -200f
        abstract val w: Float
        abstract val h: Float
        abstract var yhit: Float
        abstract val hhit: Float

        var dead = false

        abstract fun update()
        abstract fun die()
    }


    class Asteroid : Enemy() {
        private val type = p.random(1f, 5f).toInt()
        private val img: PImage = p.loadImage("asteroid$type.png")
        override var w = if (type % 2 == 1) 60f else 90f
        override var h = if (type % 2 == 1) 60f else 90f
        var rotation = p.random(0f, TAU)
        val spin = p.random(-0.2f, 0.2f) //radians
        val xSpeed = p.random(-1.7f, 1.7f)
        val ySpeed = p.random(2f, 4.5f)
        override var yhit = y
        override val hhit = h
        val dmg = 1

        val asteroidHit: SoundFile = SoundFile(p, "asteroidHit.wav")

        override fun update(){
            x += xSpeed
            y += ySpeed
            yhit = y
            rotation = (rotation + spin) % TAU

            whileRotated(x, y, rotation) {
                p.image(img, 0f, 0f)
            }

            if (collisions.hasCollided(this.x, this.yhit, this.w, this.hhit, player.x, player.y, player.w, player.h)) {
                player.hit(dmg)
                this.dead = true
                asteroidHit.play()
            }
        }

        override fun die() {
            asteroidHit.play()
        }
    }


    class Ufo : Enemy() {
        class Bullet(val x: Float, var y: Float) {
            val img: PImage = p.loadImage("ufoBullet.png")
            val speed = 8f
            val w = 3f
            val h = 40f
            var dead = false
            val dmg = 1

            fun update() {
                y += speed
                p.image(img, x, y)

                if (collisions.hasCollided(this.x, this.y, this.w, this.h, player.x, player.y, player.w, player.h)) {
                    player.hit(dmg)
                    this.dead = true
                }
            }
        }

        var bullets = mutableListOf<Bullet>()
        val img: PImage = p.loadImage("ufo.png")
        override val w = 90f
        override val h = 24f
        override var yhit = y
        override val hhit = h
        val speed = 2f
        val dmg = 1

        val ufoDie: SoundFile = SoundFile(p, "ufoDie.wav")
        val ufoShoot: SoundFile = SoundFile(p, "ufoShoot.wav")

        override fun update() {
            //move towards player
            if (player.x > this.x) x += speed
            else x -= speed

            y += speed
            yhit = y

            //shoot every 100 frames
            if (p.frameCount % 100 == 0) {
                bullets.add(Bullet(x, y))
                ufoShoot.stop()
                ufoShoot.play()
            }

            //update bullets
            for (bullet in bullets) bullet.update()
            bullets.removeIf { bullet -> bullet.dead || bullet.y > p.height + 200 }

            if (collisions.hasCollided(this.x, this.yhit, this.w, this.hhit, player.x, player.y, player.w, player.h)) {
                player.hit(dmg)
                this.dead = true
                ufoDie.play()
            }

            p.image(img, x, y)
        }

        override fun die() {
            this.dead = true
            ufoDie.play()
        }
    }


    class EnemyShip : Enemy() {

        class Bullet(val x: Float, var y: Float) {
            val img: PImage = p.loadImage("enemyShipBullet.png")
            val speed = 8f
            var dead = false
            val w = 10f
            val h = 40f
            val dmg = 3

            fun update() {
                y += speed
                p.image(img, x, y)

                if (collisions.hasCollided(this.x, this.y, this.w, this.h, player.x, player.y, player.w, player.h)) {
                    player.hit(dmg)
                    this.dead = true
                }
            }
        }

        val img: PImage = p.loadImage("enemyShip.png")
        override val w = 124f
        override val h = 108f
        val speed = 4f
        var noiseX = 0f
        var bullets = mutableListOf<Bullet>()
        override var yhit = y - 6
        override val hhit = 15f
        val dmg = 3

        val enemyDie: SoundFile = SoundFile(p, "enemyDie.wav")
        val enemyShoot: SoundFile = SoundFile(p, "enemyShoot.wav")

        override fun update() {
            attack()
            yhit = y - 6

            for (bullet in bullets) bullet.update()
            bullets.removeIf { bullet -> bullet.dead || bullet.y > p.height + 200 }

            if (collisions.hasCollided(this.x, this.yhit, this.w, this.hhit, player.x, player.y, player.w, player.h)) {
                player.hit(dmg)
                this.dead = true
                enemyDie.play()
            }

            p.image(img, x, y)
        }

        private fun attack() {
            for (bullet in player.bullets) {

                // if the bullet is close
                if (bullet.y - 100 < this.y && (bullet.x + 100 > this.x || bullet.x - 100 < this.x)) {
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
            if (p.frameCount % map(p.noise(noiseX), 0f, 1f, 20f, 80f).toInt() == 0) {
                noiseX += 0.03f
                bullets.add(Bullet(x, y))
                enemyShoot.stop()
                enemyShoot.play()
            }

            return
        }

        override fun die() {
            this.dead = true
            enemyDie.play()
        }
    }


    class Boss : Enemy() {

        class Bullet(val x: Float, var y: Float) {
            val img: PImage = p.loadImage("bossBullet.png")
            val speed = 12f
            var dead = false
            val w = 48f
            val h = 114f
            val dmg = 10

            fun update() {
                y += speed
                p.image(img, x, y)

                if (collisions.hasCollided(this.x, this.y, this.w, this.h, player.x, player.y, player.w, player.h)) {
                    player.hit(dmg)
                    this.dead = true
                }
            }
        }

        val img: PImage = p.loadImage("boss.png")
        override val w = 461f
        override val h = 523f
        val speed = 3f
        var noiseX = 0f
        var bullets = mutableListOf<Bullet>()
        override var yhit = y
        override val hhit = h
        var hp = 50

        val bossDie: SoundFile = SoundFile(p, "bossDie.wav")
        val bossShoot: SoundFile = SoundFile(p, "bossShoot.wav")
        val bossHit: SoundFile = SoundFile(p, "bossHit.wav")

        override fun update() {
            attack()
            yhit = y

            for (bullet in bullets) bullet.update()
            bullets.removeIf { bullet -> bullet.dead || bullet.y > p.height + 200 }

            if (collisions.hasCollided(this.x, this.yhit, this.w, this.hhit, player.x, player.y, player.w, player.h)) {
                gui.state = "game over"
            }

            p.image(img, x, y)
        }

        private fun attack() {
            if (player.x > this.x) x += speed
            else x -= speed

            if (player.y > this.y + 250f) y += speed
            else if (player.y < this.y + 200f) y -= speed


            //shoot sometimes
            if (p.frameCount % map(p.noise(noiseX), 0f, 1f, 20f, 80f).toInt() == 0) {
                noiseX += 0.03f
                bullets.add(Bullet(x, y))
                bossShoot.stop()
                bossShoot.play()
            }
        }

        override fun die() {
            hp--

            if (hp <= 0) {
                this.dead = true
                bossDie.play()
            } else {
                bossHit.play()
            }
        }
    }


    var enemyList = mutableListOf<Enemy>()
    var spawnInterval = 1000 // milliseconds
    var timeOfLastSpawn = p.millis()

    fun update(){

        //update enemies
        for (enemy in enemyList) enemy.update()

        //remove dead enemies from list
        enemyList.removeIf { enemy -> enemy.dead }

        //remove offscreen enemies
        enemyList.removeIf { enemy -> enemy.y > p.height + 200 }

        val now = p.millis()

        //only spawn enemies every second minimum
        if (now - timeOfLastSpawn > spawnInterval) {

            when {
//                p.random(gui.totalLvl * 5f).toInt() == 0 -> { //5
//                    enemyList.add(Asteroid())
//                    timeOfLastSpawn = now
//                }
//                p.random(gui.totalLvl * 10f).toInt() == 0 -> { //20
//                    enemyList.add(Ufo())
//                    timeOfLastSpawn = now
//                }
//                p.random(gui.totalLvl * 20f).toInt() == 0 -> { //40
//                    enemyList.add(EnemyShip())
//                    timeOfLastSpawn = now
//                }
                p.random(gui.totalLvl * 40f).toInt() == 0 -> { //400
                    enemyList.add(Boss())
                    timeOfLastSpawn = now
                }
            }
        }
    }
}
