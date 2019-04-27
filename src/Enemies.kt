import processing.core.PImage

class Enemies {
    interface Enemy {
        var x: Float
        var y: Float
        var w: Float
        var h: Float

        fun update()
    }

    class Asteroid: Enemy{
        private val type = p.random(1f, 4f).toInt()
        private val img: PImage = p.loadImage("asteroid$type.png")
        override var x = p.random(0f, p.width.toFloat())
        override var y = -50f
        override var w = if (type % 2 == 1) 100f else 150f
        override var h = if (type % 2 == 1) 100f else 150f
        val spin = p.random(0f, 1f) //radians
        val xSpeed = p.random(0f, 2f)
        val ySpeed = p.random(2f, 4.5f)

        override fun update(){
            x += xSpeed
            y += ySpeed

            whileRotated (spin) {
                p.image(img, x, y)
            }
        }
    }

    var enemyList = mutableListOf<Enemy>()
    var spawnInterval = 1000 // milliseconds
    var timeOfLastSpawn = p.millis()

    fun update(){
        for (enemy in enemyList) enemy.update()

        if (p.millis() - timeOfLastSpawn > spawnInterval && p.random(10f).toInt() == 0){
            enemyList.add(Asteroid())
        }
    }
}