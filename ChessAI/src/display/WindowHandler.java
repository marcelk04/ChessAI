package display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowHandler extends WindowAdapter {
	private Display display;

	public WindowHandler(Display display) {
		this.display = display;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		display.stop();
	}
}