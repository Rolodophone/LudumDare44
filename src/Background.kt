import processing.core.PImage

class Background () {
    val numStars = 100
    val starImgs = listOf(
        p.loadImage("star1.png"),
        p.loadImage("star2.png"),
        p.loadImage("star3.png"),
        p.loadImage("star4.png"),
        p.loadImage("star5.png"),
        p.loadImage("star6.png"),
        p.loadImage("star7.png"),
        p.loadImage("star8.png"),
        p.loadImage("star9.png"),
        p.loadImage("star10.png")
    )
    var stars = mutableListOf<Star>()

    class Star(private val img: PImage, private val x: Float, var y: Float, private val ySpeed: Float) {
        fun update() {
            y += ySpeed
            p.image(img, x, y)

        }
    }

    init {
        for (i in 0..numStars) {
            stars.add(Star(
                starImgs.random(),
                p.random(p.width.toFloat()),
                p.random(p.height.toFloat()),
                p.random(1f)
            ))
        }
    }

    fun update () {

        for (star in stars) {
            star.update()
        }

        stars.removeAll{star -> star.y > p.height}

        while (stars.count() < numStars) {
            stars.add(
                Star(
                    starImgs.random(),
                    p.random(p.width.toFloat()),
                    -5f,
                    p.random(1f)
                )
            )
        }
    }
}