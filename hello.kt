import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import kotlin.math.pow;

public class Complex(public val x: Double, public val y: Double) {
	public operator fun times(other: Complex): Complex =
		Complex(
			this.x * other.x - this.y * other.y,
			this.x * other.y + this.y * other.x
		)

	public operator fun times(other: Double): Complex =
		times(Complex(other, 0.0))

	public operator fun plus(other: Complex): Complex = 
		Complex(
			this.x + other.x,
			this.y + other.y
		)

	public operator fun minus(other: Complex): Complex = 
		Complex(
			this.x - other.x,
			this.y - other.y
		)

	public fun abs(): Double = 
		this.x * this.x + this.y * this.y



	companion object {
		public val ORIGIN = Complex(0.0, 0.0)

		private val ABS_THRESHOLD = 2.0

		public fun centeredAndScaled(imagePoint: Complex, imageWidth: Double, imageHeight: Double, centre: Complex, zoom: Double): Complex {
			val imageCentre = Complex(0.5 * imageWidth, 0.5 * imageHeight)
			val centered = imagePoint - imageCentre
			
			val imageRadius = imageWidth / 2
			val scaled = centered * (ABS_THRESHOLD / imageRadius)
			return scaled * zoom - centre
		}

		// 0 is divergent. scale is 0 - 1,
		public fun convergenceScale(point: Complex, iterations: Int = 20): Double {
			if (point.abs() > ABS_THRESHOLD)
				return 0.0

			var series: Complex = point
			var maxAbs = point.abs()

			for (i in 1 .. iterations) {
				series = (series * series) + point
				val abs = series.abs()
				if (abs > ABS_THRESHOLD)
					return 0.0

				if (abs > maxAbs)
					maxAbs = abs
			}

			return (maxAbs - point.abs()) / maxAbs
		}
	}
}

public class Draw : JPanel() {
	val WIDTH = 1920;
	val HEIGHT = 1080;
	val BACKGROUND = Color.BLUE
	val FOREGROUND = Color.RED

	val EVENTUAL_CENTRE = Complex(-0.279, 0.0)

	private var frameNum = 0;

	private val animationTimer = Timer(500, TimerHandler(this))
	
    protected override fun paintComponent(graphics: Graphics) {
		super.paintComponent(graphics)

		val iterations = Math.max(frameNum, 30)
		// val centreDistance = 1 - (0.3).pow(frameNum)
		val centre = EVENTUAL_CENTRE
		val zoom = 0.95.pow(frameNum)

		println("zoom = $zoom")
		val image = mandlebrotImage(iterations, centre, zoom)
		if (animationTimer.isRunning()) {
			frameNum += 1
		}
		graphics.drawImage(image, 0, 0, Color.BLACK, null)
    }

	private fun mandlebrotImage(iters: Int, centre: Complex, zoom: Double): Image {
		val image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB) 
		val interestPoints = arrayOf(EVENTUAL_CENTRE, /*Complex.ORIGIN, Complex(1.0, 1.0), Complex(-1.0, 1.0), Complex(1.0, -1.0), Complex(-1.0, -1.0) */)

		for (x in 0 .. WIDTH - 1) {
			for (y in 0 .. HEIGHT - 1) {
				val point = Complex.centeredAndScaled(Complex(x.toDouble(), y.toDouble()), WIDTH.toDouble(), HEIGHT.toDouble(), centre, zoom)
				val convergenceScale = Complex.convergenceScale(point, iters)
				val colour = if (convergenceScale > 0) {
					this.interpolate(BACKGROUND, FOREGROUND, convergenceScale)
				} 
				else {
					BACKGROUND
				}
				image.setRGB(x, y, colour.getRGB())
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

public class TimerHandler(val draw: Draw) : ActionListener {
	public override fun actionPerformed(event: ActionEvent) {
		draw.repaint()
	}
}




public fun main() {
	println("Hello World :)")
	val frame = JFrame("helllooo")
	frame.setSize(1920, 1080)
	frame.setVisible(true)
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

	val animation = Draw()
	frame.add(animation)
	animation.startAnimation()
}

