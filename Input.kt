import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import kotlin.system.exitProcess


internal class Input(private val draw: Panel) : KeyAdapter() {
    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_UP -> this.draw.move(Complex.IMAG_UNIT * -1.0)
            KeyEvent.VK_DOWN -> this.draw.move(Complex.IMAG_UNIT)
            KeyEvent.VK_LEFT -> this.draw.move(Complex.REAL_UNIT * -1.0)
            KeyEvent.VK_RIGHT -> this.draw.move(Complex.REAL_UNIT)
            KeyEvent.VK_SPACE -> this.draw.changeZoom(0.9)
            KeyEvent.VK_CONTROL -> this.draw.changeZoom(1.0 / 0.9)
            KeyEvent.VK_Q -> exitProcess(0)
        }
    }
}