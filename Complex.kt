import java.awt.*;
import kotlin.math.exp

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

    public override operator fun equals(other: Any?): Boolean =
        when (other) {
            is Complex -> {
                this.x == other.x && this.y == other.y
            }
            else -> {
                false
            }
        }

    public override fun toString() =
        "(${this.x}, ${this.y})"

    public fun normalise() =
        this * (1.0 / this.abs())

    companion object {
        public val ORIGIN = Complex(0.0, 0.0)
        public val REAL_UNIT = Complex(1.0, 0.0)
        public val IMAG_UNIT = Complex(0.0, 1.0)

        private val ABS_THRESHOLD = 2.0

        public fun centeredAndScaled(imagePoint: Complex, imageWidth: Double, imageHeight: Double, centre: Complex, zoom: Double): Complex {
            val imageCentre = Complex(0.5 * imageWidth, 0.5 * imageHeight)
            val centered = imagePoint - imageCentre

            val imageRadius = imageWidth / 2
            val scaled = centered * (ABS_THRESHOLD / imageRadius)
            return scaled * zoom + centre
        }

        // 0 is divergent. scale is 0 - 1,
        public fun convergenceColour(point: Complex, iterations: Int): Color {
            val background = Color.BLACK

            var series: Complex = point

            for (i in 1 .. iterations) {
                series = (series * series) + point
                val abs = series.abs()
                if (abs > ABS_THRESHOLD) {
                    val hue = exp(i.toDouble() / iterations).toFloat()
                    return Color.getHSBColor(hue.toFloat(), 1f, 1f)
                }
            }

            return background
        }
    }
}

