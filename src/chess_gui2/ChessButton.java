package chess_gui2;

import java.awt.Color;

import javax.swing.JButton;

import chess_gui.ChessGUI;
import utilities.GameParameters;


public class ChessButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2648374001583797093L;
	
	static int N = 8;

	public ChessButton(int i) {
		// super(7 - i / N + ", " + i % N);
		super();
		
		this.setOpaque(true);
		// this.setBorderPainted(false);
		
		if ((i / N + i % N) % 2 == 1) {
			this.setBackground(GameParameters.blackTileColor);
		} else {
			this.setBackground(Color.WHITE);
		}
		
	}
	
	
	public ChessButton(int i, int j) {
		// super(i + ", " + j);
		super();
		
		this.setOpaque(true);
		// this.setBorderPainted(false);
		
		Color color = ChessGUI.getColorByRowCol(i, j);
		this.setBackground(color);
		
	}
  
}