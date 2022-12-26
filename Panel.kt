import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.Timer
import kotlin.math.pow

public class Panel : JPanel() {
    val WIDTH = 1920;
    val HEIGHT = 1080;
    val BACKGROUND = Color.BLUE
    val FOREGROUND = Color.RED
    val MAX_ITERS = 100
    val DISTANCE_STEP = 0.1
    val ZOOM_STEP = 0.9
    val BRIGHTNESS = 0.5
    val SATURATION = 0.5

    var centre =  Complex(0.0, 0.0)
    var zoom = 1.0

    private var frameNum = 0;
    private val animationTimer = Timer(300, Timer(this))
    public fun move(direction: Complex) {
        val distance = DISTANCE_STEP * 2.0 * zoom
        centre += direction * (distance / direction.abs())
    }

    public fun changeZoom(zoomMultiplier: Double) {
        this.zoom *= zoomMultiplier
    }
    protected override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)

        val iterations = frameNum.coerceAtMost(MAX_ITERS)
        println("iterations = $iterations \t centring on $centre \t zoom = $zoom")
        val image = mandlebrotImage(iterations, centre, zoom)
        if (animationTimer.isRunning) {
            frameNum += 1
        }
        graphics.drawImage(image, 0, 0, Color.BLACK, null)
    }

    private fun mandlebrotImage(iters: Int, centre: Complex, zoom: Double): Image {
        val image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until WIDTH) {
            for (y in 0 until HEIGHT) {
                val point = Complex.centeredAndScaled(Complex(x.toDouble(), y.toDouble()), WIDTH.toDouble(), HEIGHT.toDouble(), centre, zoom)
                val colour = Complex.convergenceColour(point, iters)
                image.setRGB(x, y, colour.rgb)
            }
        }

        return image
    }

    public fun startAnimation() {
        animationTimer.start()

        if (!animationTimer.isRunning()) {
            animationTimer.restart()
        }
    }

    public fun stopAnimation() {
        animationTimer.stop()
    }

    private fun interpolate(firstColour: Color, secondColour: Color, scale: Double): Color {
        val fstComp = firstColour.getRGBColorComponents(null)
        val sndComp = secondColour.getRGBColorComponents(null)

        val diff = fstComp.indices.map { sndComp[it] - fstComp[it] }
        val magnitude = diff.fold(0.0) { acc, colour -> acc + colour.pow(2) }.pow(0.5)
        val normalisedDiff = diff.map { (scale / magnitude) * it }
        val interpolated = normalisedDiff.indices.map { (fstComp[it] + normalisedDiff[it]).toFloat() }

        return Color(interpolated[0], interpolated[1], interpolated[2])
    }
}
