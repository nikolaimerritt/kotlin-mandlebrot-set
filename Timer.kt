import java.awt.event.ActionEvent
import java.awt.event.ActionListener

public class Timer(val draw: Panel) : ActionListener {
    public override fun actionPerformed(event: ActionEvent) {
        draw.repaint()
    }
}

