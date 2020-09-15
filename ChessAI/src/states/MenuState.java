package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JColorChooser;

import main.Game;
import ui.ClickListener;
import ui.UIManager;
import ui.UISelectionBox;
import ui.UITextButton;

public class MenuState extends State {
	private UIManager uiManager;

	public MenuState(Game game) {
		super(game);

		uiManager = new UIManager();

		uiManager.addObject(new UITextButton(game.getDisplay().getWidth() / 2, game.getDisplay().getHeight() / 2,
				"Change Background Color", Color.black, new Font("Bahnschrift", Font.PLAIN, 15), new ClickListener() {

					@Override
					public void onClick() {
						Color c = JColorChooser.showDialog(game.getDisplay().getFrame(), "Change Background Color",
								game.getGameState().getBoard().getBackgroundColor());
						if (c != null)
							game.getGameState().getBoard().setBackgroundColor(c);
					}
				}));
		UISelectionBox selectionBox = new UISelectionBox(game.getDisplay().getWidth() / 2 - 100, 200, 200, 20,
				new String[] { "Play against AI", "Play against Player" }, Color.black,
				new Font("Bahnschrift", Font.PLAIN, 15), null);
		selectionBox.setClickListener(new ClickListener() {
			@Override
			public void onClick() {
				game.getGameState().getBoard().setPlayingAgainstAI(selectionBox.getSelectedIndex() == 0 ? true : false);
			}
		});
		uiManager.addObject(selectionBox);
		uiManager.addObject(new UITextButton(game.getDisplay().getWidth() / 2, game.getDisplay().getHeight() - 200,
				"Reset Board", Color.black, new Font("Bahnschrift", Font.PLAIN, 15), new ClickListener() {

					@Override
					public void onClick() {
						game.getGameState().getBoard().reset();
						State.setState(game.getGameState());
					}
				}));
	}

	@Override
	public void tick() {
		uiManager.tick();
	}

	@Override
	public void render(Graphics g) {
		uiManager.render(g);
	}

	public UIManager getUIManager() {
		return uiManager;
	}
}