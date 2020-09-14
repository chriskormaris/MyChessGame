package chess_gui2;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

/*  www.java2s.com*/
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess_gui.ChessGUI;
import enumerations.GameMode;
import minimax_ai.MiniMaxAi;
import utilities.Constants;
import utilities.GameParameters;


public class ChessGui2 extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5684370471793136759L;

	private final static String title = "My Chess Game GUI 2";

	private static int numOfRows = GameParameters.numOfRows;
	private final static int numOfColumns = Constants.NUM_OF_COLUMNS;

	private final static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private final static int WIDTH = (int) screenSize.getHeight() - 150;
	private final static int HEIGHT = (int) screenSize.getHeight() - 150;
	
	// static int SIZE = 75;

	public static JLabel labelMessage = new JLabel("Turn 1, White plays first.");
	
	
	public ChessGui2() {
		super(new GridLayout(numOfRows+2, numOfColumns+2));
		// GameParameters.gameMode = Constants.HumanVsRandomAi;
		
		if (GameParameters.gameMode == GameMode.HUMAN_VS_AI) {
			ChessGUI.ai = new MiniMaxAi(GameParameters.maxDepth1, Constants.BLACK);
		}
		
		ChessGUI.configureGuiStyle();
		
		// this.setPreferredSize(new Dimension(N * SIZE, N * SIZE));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		// this.initializeChessBoardPanel();
		
		ChessGUI.isChessGui2 = true;
		ChessGUI.chessBoardPanel = this;
		ChessGUI.gui.add(ChessGUI.chessBoardPanel);
		ChessGUI.initializeChessBoardSquareButtons();
		
		ChessGUI.initializeCapturedPiecesPanel();
		ChessGUI.initializeCapturedPiecesImages();
	}
	
	
	/*
	private void initializeChessBoardPanel() {

		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				// chessBoardSquares[i][j] = new ChessButton(i*N + j);
				ChessGUI.chessBoardSquares[i][j] = new ChessButton(i, j);
				JButton button = ChessGUI.chessBoardSquares[i][j];
				
				int row = 7 - i;
				int column = j;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ChessGUI.chessButtonClick(row, column, button);
					}
				});
				
				// this.add(chessBoardSquares[i][j]);
			}
		}
		
		// fill the chess board panel
		add(new JLabel(""));
		
		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65 
		for (int i=0; i<N; i++) {
			add(new JLabel(String.valueOf((char) (65+i)), SwingConstants.CENTER));
		}
		
		add(new JLabel(""));
		
		// fill the black non-pawn piece row
		for (int i=0; i<N; i++) {
			for (int j=0; j<N+1; j++) {
				switch (j) {
					case 0:
						add(new JLabel("" + (N - i), SwingConstants.CENTER));
						add(ChessGUI.chessBoardSquares[i][j]);
						break;
					case N:
						add(new JLabel("" + (N - i), SwingConstants.CENTER));
						break;
					default:
						add(ChessGUI.chessBoardSquares[i][j]);
				}
			}
		}
		
		// fill the bottom row
		add(new JLabel(""));
		for (int i=0; i<N; i++) {
			add(new JLabel(String.valueOf((char) (65+i)), SwingConstants.CENTER));
		}
		
	}
	*/
	
	
	public static void main(String[] args) {

		ChessGui2 chessGui2 = new ChessGui2();
		
		JFrame f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(chessGui2);
		f.pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((int) (screenSize.getWidth() - f.getWidth()) / 2, 
					  (int) (screenSize.getHeight() - f.getHeight()) / 2);
		
		f.setResizable(false);
		f.setVisible(true);
		
		ChessGUI.placePiecesToChessBoard();
		System.out.println(ChessGUI.chessBoard);
	}
	
}
