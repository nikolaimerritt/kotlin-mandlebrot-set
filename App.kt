import javax.swing.JFrame

public fun main() {
	val frame = JFrame("manky brod set")
	frame.setSize(1920, 1080)
	frame.isVisible = true
	frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

	val panel = Panel()
	frame.add(panel)
	frame.addKeyListener(Input(panel))
	panel.startAnimation()
}

