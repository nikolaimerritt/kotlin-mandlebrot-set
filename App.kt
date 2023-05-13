import javax.swing.JFrame

class App {

	public fun main() {
		val frame = JFrame("Mandlebrot set")
		frame.setSize(1920, 1080)
		frame.isVisible = true
		frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

		val panel = Panel()
		frame.add(panel)
		frame.addKeyListener(Input(panel))
		panel.startAnimation()
	}

}

