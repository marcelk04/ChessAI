package tree;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chess.board.ChessBoard;

public class TreeGUI {
	private Map<String, Node<ChessEvent>> roots;
	private JFrame frame;
	private JList<String> rootsList;
	private JScrollPane sp;
	private JComboBox<String> rootsCB;
	private JTextArea boardDisplay;
	private JButton viewData;
	@SuppressWarnings("unused")
	private JList<String> parentsList;

	public TreeGUI() {
		roots = new HashMap<String, Node<ChessEvent>>();

		frame = new JFrame("Tree");
		frame.setSize(600, 400);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);

		Container cp = frame.getContentPane();
		cp.setLayout(null);

		rootsList = new JList<String>();
		sp = new JScrollPane(rootsList);
		sp.setBounds(10, 10, 250, 340);
		cp.add(sp);

		rootsCB = new JComboBox<String>();
		rootsCB.setBounds(270, 10, 180, 20);
		rootsCB.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					updateListData();
				}
			}
		});
		cp.add(rootsCB);

		boardDisplay = new JTextArea();
		boardDisplay.setBounds(270, 50, 90, 130);
		boardDisplay.setBackground(new Color(128, 128, 128));
		boardDisplay.setEditable(false);
		cp.add(boardDisplay);

		viewData = new JButton("View Data");
		viewData.setBounds(460, 10, 100, 20);
		viewData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateBoardDisplay();
			}
		});
		cp.add(viewData);

		frame.setVisible(true);
	}

	public void addTree(Node<ChessEvent> root) {
		String key = "Move " + (roots.size() + 1);
		roots.put(key, root);
		rootsCB.addItem(key);
	}

	private void updateListData() {
		String key = (String) rootsCB.getSelectedItem();
		List<Node<ChessEvent>> listData = getListData(roots.get(key));
		String[] listDataArray = new String[listData.size()];

		for (int i = 0; i < listDataArray.length; i++)
			listDataArray[i] = listData.get(i).toString();

		rootsList.setListData(listDataArray);
	}

	/**
	 * Gets the selected node in the List and updates the TextArea with the current
	 * board.
	 */
	private void updateBoardDisplay() {
		String key = (String) rootsCB.getSelectedItem();
		if (key != null) {
			List<Node<ChessEvent>> listData = getListData(roots.get(key));
			Node<ChessEvent> selectedNode = listData.get(rootsList.getSelectedIndex());
			ChessBoard selectedBoard = selectedNode.getData().getBoard();
			boardDisplay.setText(getBoardDisplayData(selectedBoard));
		}
	}

	/**
	 * Converts a tree into a List.
	 * 
	 * @param root the root of the tree.
	 * @return a List of the nodes.
	 */
	private List<Node<ChessEvent>> getListData(Node<ChessEvent> root) {
		List<Node<ChessEvent>> data = new ArrayList<Node<ChessEvent>>();
		addChildrenData(data, root);
		return data;
	}

	/**
	 * Adds the data of a Node and their children to the List.
	 * 
	 * @param data the list to which the nodes will be added.
	 * @param node a node.
	 */
	private void addChildrenData(List<Node<ChessEvent>> data, Node<ChessEvent> node) {
		data.add(node);
		for (Node<ChessEvent> child : node.getChildren()) {
			addChildrenData(data, child);
		}
	}

	/**
	 * Converts a ChessBoard to a String for the TextArea to display.
	 * 
	 * @param board the board to be converted.
	 * @return the board data as a String.
	 */
	private String getBoardDisplayData(ChessBoard board) {
		String data = "";
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (board.getPiece(x, y) != null) {
					data += board.getPiece(x, y).getName().charAt(0) + " ";
				} else {
					if ((x + y + 1) % 2 == 0) {
						data += "# ";
					} else {
						data += "O ";
					}
				}
			}

			data += "\n";
		}

		return data;
	}
}