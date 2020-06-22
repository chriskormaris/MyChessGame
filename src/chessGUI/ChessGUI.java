package chessGUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import chess.ChessBoard;
import chess.Move;
import minimaxAi.MiniMaxAi;
import utilities.Constants;
import utilities.FenUtilities;
import utilities.GameParameters;
import utilities.InvalidFenFormatException;
import utilities.ResourceLoader;
import utilities.Utilities;


public class ChessGUI {
	
	private final static String TITLE = "My Chess Game";
	
	private final static int NUM_OF_COLUMNS = Constants.NUM_OF_COLUMNS;
	
	private final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	private final static int WIDTH = (int) SCREEN_SIZE.getHeight() - 80;
	private final static int HEIGHT = (int) SCREEN_SIZE.getHeight() - 80;
	
	public static JFrame frame = new JFrame(TITLE);
	public static JPanel gui = new JPanel(new BorderLayout(3, 3));
	
	public static JPanel chessBoardPanel;
	
	public static String firstTurnMessage = "Move number: 1. White plays first.";
	public static JLabel labelMessage = new JLabel(firstTurnMessage);
	
	// The position (0, 0) is the upper left tile of the chessBoardSquares.
	public static JButton[][] chessBoardSquares;
	
	private static JMenuBar menuBar;
	private static JMenu fileMenu;
	private static JMenuItem newGameItem;
	private static JMenuItem undoItem;
	private static JMenuItem redoItem;
	private static JMenuItem exportToGifItem;
	private static JMenuItem settingsItem;
	private static JMenuItem importFenPositionItem;
	private static JMenuItem aboutItem;
	private static JMenuItem exitItem;
	
	// The position (0, 0) of the "chessBoard.getGameBoard()" is the lower left button 
	// of the JButton array "chessBoardSquares".
	// The position (chessBoard.getNumOfRows()-1, 0) of the "chessBoard.getGameBoard()" is the upper left button 
	// of the JButton array "chessBoardSquares".
	public static ChessBoard chessBoard = new ChessBoard();
	
	public static String startingPosition = "";
	public static String endingPosition = "";
	
	// These stacks of "ChessBoard" objects are used to handle the "undo" and "redo" functionality.
	public static Stack<ChessBoard> previousChessBoards = new Stack<ChessBoard>();
	public static Stack<ChessBoard> redoChessBoards = new Stack<ChessBoard>();

	public static boolean startingButtonIsClicked = false;
	public static Set<String> hintPositions = new TreeSet<String>();
	
	public static boolean buttonsEnabled = true;
		
	// This variable is used for the implementation of Human Vs MiniMax AI.
	public static MiniMaxAi ai;
	
	// This variable is used for the implementation of MiniMax AI Vs MiniMax AI.
	public static boolean isGameOver;
	
	// This variable is true if the main is running from "ChessGui2" class.
	public static boolean isChessGui2;
	
	
	public ChessGUI(String title) {

		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);
		
		configureGuiStyle();

		initializeGui();

		if (GameParameters.gameMode == Constants.HUMAN_VS_MINIMAX_AI) {
			ai = new MiniMaxAi(GameParameters.maxDepth1, Constants.BLACK);
		}
		
		frame.add(getGui());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		frame.pack();

		frame.setSize(new Dimension(WIDTH, HEIGHT));
		
		// ensures the minimum size is enforced.
		frame.setMinimumSize(frame.getSize());
		
		frame.setLocation((int) (SCREEN_SIZE.getWidth() - frame.getWidth()) / 2, 
						  (int) (SCREEN_SIZE.getHeight() - frame.getHeight()) / 2);
				
		frame.setResizable(false);
		// frame.setFocusable(true);
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newGameItem = new JMenuItem("New Game");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		exportToGifItem = new JMenuItem("Export to .gif");
		settingsItem = new JMenuItem("Preferences");
		importFenPositionItem = new JMenuItem("Import FEN Position");
		aboutItem = new JMenuItem("About");
		exitItem = new JMenuItem("Exit");
		
		undoItem.setEnabled(false);
		redoItem.setEnabled(false);

		newGameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		});
		
		undoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoLastMove();
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redoLastMove();
			}
		});
		
		exportToGifItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportToGif();
			}
		});
		
		settingsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow settings = new SettingsWindow();
				settings.setVisible(true);
			}
		});
		
		importFenPositionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fenPosition = JOptionPane.showInputDialog(
						"Please insert the \"FEN\" position in the text field below:                      ",
						"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
				
				if (fenPosition != null) {
					// GameParameters.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;
					startNewGame();
					placePiecesToChessBoard(fenPosition);
				}
				
			}
		});
		
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			JLabel label = new JLabel("<html><center>A traditional chess game implementation using Minimax AI.<br>"
					+ "with Alpha-Beta Pruning.<br>"
					+ "© Created by: Christos Kormaris, Athens 2020<br>"
					+ "Version " + Constants.VERSION + "</center></html>");
			JOptionPane.showMessageDialog(frame, label, "About", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		fileMenu.add(newGameItem);
		fileMenu.add(undoItem);
		fileMenu.add(redoItem);
		fileMenu.add(exportToGifItem);
		fileMenu.add(settingsItem);
		fileMenu.add(importFenPositionItem);
		fileMenu.add(aboutItem);
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
		
		frame.setJMenuBar(menuBar);
		
		frame.setVisible(true);
	}
	
	
	public static void configureGuiStyle() {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (GameParameters.guiStyle == Constants.CROSS_PLATFORM_STYLE) {
				// Option 1
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} else if (GameParameters.guiStyle == Constants.NIMBUS_STYLE) {
				// Option 2
			    for (LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			}
		} catch (Exception e1) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e2) {
				e2.printStackTrace();	
			}
		}
	}
	
	
	private static void setTurnMessage() {
		String turnMessage = "";
		if (chessBoard.getHalfmoveNumber() == 1) {
			turnMessage = firstTurnMessage;
		} else {
			turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". ";
			turnMessage += (chessBoard.isWhitePlays()) ? "White plays." : "Black plays.";
		}
		if (chessBoard.isWhitePlays() && chessBoard.isWhiteKingInCheck())
			turnMessage += " White king is in check!";
		else if (!chessBoard.isWhitePlays() && chessBoard.isBlackKingInCheck())
			turnMessage += " Black king is in check!";
		labelMessage.setText(turnMessage);
	}


	private static void undoLastMove() {
		if (!previousChessBoards.isEmpty()) {
			System.out.println("Undo is pressed!");
			
			startingButtonIsClicked = false;
			hideHintPositions(hintPositions);
			
			int startingPositionRow = Utilities.getRowFromPosition(startingPosition);
			int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
			JButton startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
			Color startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);
			changeTileColor(startingButton, startingButtonColor);
			
			redoChessBoards.push(new ChessBoard(chessBoard));
						
			if (previousChessBoards.isEmpty()) {
				undoItem.setEnabled(false);
			}
			
			chessBoard = previousChessBoards.pop();
			
			// This is true if any terminal state has occured.
			// Terminal states are "draw", "stalemate draw" and "checkmate".
			if (!buttonsEnabled) {
				enableChessBoardButtons();
			}
			
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					placePieceToPosition(Utilities.getPositionByRowCol(i, j), chessBoard.getGameBoard()[i][j]);		
				}
			}
			
			System.out.println();
			System.out.println(chessBoard);
			
			setTurnMessage();
			
			if (redoItem != null)
				redoItem.setEnabled(true);
		}
	}
	
	
	private void redoLastMove() {
		if (!redoChessBoards.isEmpty()) {
			System.out.println("Redo is pressed!");
			
			startingButtonIsClicked = false;
			hideHintPositions(hintPositions);
			
			int startingPositionRow = Utilities.getRowFromPosition(startingPosition);
			int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
			JButton startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
			Color startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);
			changeTileColor(startingButton, startingButtonColor);
			
			previousChessBoards.push(new ChessBoard(chessBoard));
			
			chessBoard = redoChessBoards.pop();

			if (redoChessBoards.isEmpty()) {
				redoItem.setEnabled(false);
			}
			
			// NOTE: We are not able to perform a redo,
			// if we are in a terminal state, because the game has ended.
			
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					placePieceToPosition(Utilities.getPositionByRowCol(i, j), chessBoard.getGameBoard()[i][j]);		
				}
			}
			
			System.out.println();
			System.out.println(chessBoard);
			
			setTurnMessage();
			
			if (undoItem != null)
				undoItem.setEnabled(true);
			
			checkForGameOver();
		}
	}


	public static void exportToGif() {
		String gifName = JOptionPane.showInputDialog("Please type the exported \".gif\" file name:", "chess_board.gif");
		
		BufferedImage bi = new BufferedImage(gui.getSize().width, gui.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		gui.paint(g);
		g.dispose();
		try {
			ImageIO.write(bi, "gif", new File(gifName));
			System.out.println("Exported .gif file!");
		} catch (Exception e) {
			
		}
	}
	
	
	public final void initializeGui() {
		
		// Set up the main GUI.
		gui.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.PAGE_START);
		tools.add(labelMessage);
		
		initializeChessBoardPanel();
		initializeChessBoardSquares();
		
	}
	
	
	public static void initializeChessBoardPanel() {		
		if (chessBoardPanel != null)
			gui.remove(chessBoardPanel);
		chessBoardPanel = new JPanel(new GridLayout(GameParameters.numOfRows+2, NUM_OF_COLUMNS+2));
		chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
		gui.add(chessBoardPanel);
	}

	public static void initializeChessBoardSquares() {

		chessBoardSquares = new JButton[GameParameters.numOfRows][NUM_OF_COLUMNS];
		
		// create the chess board squares
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i=0; i<GameParameters.numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);
				
				// Our chess pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon..
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				
				Color color = getColorByRowCol(i, j);
				button.setBackground(color);
				button.setOpaque(true);
				// button.setBorderPainted(false);
				
				int row = GameParameters.numOfRows - 1 - i;
				int column = j;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						chessButtonClick(row, column, button);
					}
				});
				
				chessBoardSquares[i][j] = button;
			}
		}

		// fill the chess board panel
		chessBoardPanel.add(new JLabel(""));
		
		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65 
		for (int j=0; j<NUM_OF_COLUMNS; j++) {
			chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
		}
		
		chessBoardPanel.add(new JLabel(""));
		// fill the black non-pawn piece row
		for (int i=0; i<GameParameters.numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS+1; j++) {
				switch (j) {
					case 0:
						chessBoardPanel.add(new JLabel("" + (GameParameters.numOfRows - i), SwingConstants.CENTER));
						chessBoardPanel.add(chessBoardSquares[i][j]);
						break;
					case NUM_OF_COLUMNS:
						chessBoardPanel.add(new JLabel("" + (GameParameters.numOfRows - i), SwingConstants.CENTER));
						break;
					default:
						chessBoardPanel.add(chessBoardSquares[i][j]);
				}
			}
		}
		
		// fill the bottom row
		chessBoardPanel.add(new JLabel(""));
		for (int j=0; j<NUM_OF_COLUMNS; j++) {
			chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
		}
	}


	public static void startNewGame() {
		System.out.println("Starting new game!");
		
		configureGuiStyle();
		restoreDefaultValues();

		/* If running "ChessGUI.java", use this! */
		chessBoardPanel.removeAll();
		if (!isChessGui2)
			initializeChessBoardPanel();
		initializeChessBoardSquares();
		chessBoardPanel.revalidate();
		chessBoardPanel.repaint();
		
		/* If running "ChessGui2.java", use this! */
		// if (!buttonsEnabled)
		//	enableChessBoardButtons();
		
		restoreDefaultValues();
		// makeChessBoardSquaresEmpty();  // Unneeded.
		
		placePiecesToChessBoard();
		
		System.out.println();
		System.out.println(chessBoard);
		
		if (GameParameters.gameMode == Constants.MINIMAX_AI_VS_MINIMAX_AI) {
			playAiVsAi();
		}
		
	}

	
	// Restores all the default values.
	private static void restoreDefaultValues() {
		chessBoard = new ChessBoard();
		
		startingPosition = "";
		endingPosition = "";

		previousChessBoards.clear();
		redoChessBoards.clear();
		
		startingButtonIsClicked = false;
		
		hintPositions = new TreeSet<String>();
		
		if (undoItem != null)
			undoItem.setEnabled(false);
		if (redoItem != null)
			redoItem.setEnabled(false);
		
		if (GameParameters.gameMode == Constants.HUMAN_VS_MINIMAX_AI) {
			ai = new MiniMaxAi(GameParameters.maxDepth1, Constants.BLACK);
		}
		isGameOver = false;

		String turnMessage = firstTurnMessage;
		labelMessage.setText(turnMessage);
	}
	
	
	// This method is only called from inside a chess button listener.
	public static void chessButtonClick(int row, int column, JButton button) {
		// System.out.println("row: " + row + ", column: " + column);
		
		hideHintPositions(hintPositions);
		
		String position = Utilities.getPositionByRowCol(row, column);
		// System.out.println("position: " + position);
		int piece = chessBoard.getGameBoard()[row][column];
		// System.out.println("piece: " + piece);
		
		int startingPositionRow = 0;
		int startingPositionColumn = 0;
		int startingPiece = Constants.EMPTY;
		if (!startingPosition.equals("")) {
			startingPositionRow = Utilities.getRowFromPosition(startingPosition);
			startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
			startingPiece = chessBoard.getGameBoard()[startingPositionRow][startingPositionColumn];
		}
		
		if (!startingButtonIsClicked &&
			(piece > 0 && chessBoard.isWhitePlays() || piece < 0 && !chessBoard.isWhitePlays())) {
			
			startingPosition = position;
			// System.out.println("startingPosition: " + startingPosition);
			
//			System.out.println("chessBoard: ");
//			System.out.println(chessBoard);
			
			if (piece != Constants.EMPTY) {
				
				// Get the hint positions.
				if (chessBoard.isWhitePlays() && !chessBoard.isWhiteKingInCheck() 
					|| !chessBoard.isWhitePlays() && !chessBoard.isBlackKingInCheck()) {
					hintPositions = chessBoard.getNextPositions(position);
					
					// System.out.println("chessBoard: ");
					// System.out.println(chessBoard);
				}
				// If the White or Black King is in check, then get one of the following valid moves.
				else if (chessBoard.isWhitePlays() && chessBoard.isWhiteKingInCheck() || !chessBoard.isWhitePlays() 
						&& chessBoard.isBlackKingInCheck()) {
					hintPositions = new TreeSet<String>();
					
					if (chessBoard.isWhitePlays() 
							&& chessBoard.getWhiteKingInCheckValidPieceMoves().containsKey(startingPosition)) {
						hintPositions = chessBoard.getWhiteKingInCheckValidPieceMoves().get(startingPosition);
					}
					else if (!chessBoard.isWhitePlays() 
							&& chessBoard.getBlackKingInCheckValidPieceMoves().containsKey(startingPosition)) {
						hintPositions = chessBoard.getBlackKingInCheckValidPieceMoves().get(startingPosition);
					}
					
				}
				
				changeTileColor(button, Color.CYAN);
				
				// Display the hint positions.
				if (hintPositions != null && hintPositions.size() != 0) {
					// System.out.println("hintPositions: " + hintPositions);
					for (String hintPosition: hintPositions) {
						// System.out.println("hintPosition: " + hintPosition);
						
						int hintPositionRow = Utilities.getRowFromPosition(hintPosition);
						int hintPositionColumn = Utilities.getColumnFromPosition(hintPosition);
						JButton hintPositionButton = 
								chessBoardSquares[chessBoard.getNumOfRows() - 1 - hintPositionRow][hintPositionColumn];
						// System.out.println("startingPiece: " + startingPiece);
						// System.out.println("hint position: " + hintPosition);
						
						int hintPositionPiece = chessBoard.getGameBoard()[hintPositionRow][hintPositionColumn];
						
						if (piece * hintPositionPiece < 0
							|| chessBoard.getEnPassantPosition().equals(hintPosition))
							changeTileColor(hintPositionButton, Color.RED);
						else if (piece == Constants.WHITE_PAWN && hintPositionRow == chessBoard.getNumOfRows() - 1
								|| piece == Constants.BLACK_PAWN && hintPositionRow == 0)
							changeTileColor(hintPositionButton, Color.GREEN);
						else if (hintPositionPiece == 0)
							changeTileColor(hintPositionButton, Color.BLUE);
						
					}
				}
				
				// System.out.println("chessBoard: ");
				// System.out.println(chessBoard);
								
				startingButtonIsClicked = true;
			}
			
		} else if (startingButtonIsClicked &&
			(startingPiece > 0 && chessBoard.isWhitePlays() || startingPiece < 0 && !chessBoard.isWhitePlays())) {
			
			startingButtonIsClicked = false;
			
			endingPosition = position;
			// System.out.println("endingPosition: " + endingPosition);
			
			if (startingPosition.equals(endingPosition)
				|| !hintPositions.contains(endingPosition)) {
				
				JButton startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
				Color startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() -1 - startingPositionRow, startingPositionColumn);
				// System.out.println("startingButtonColor: " + startingButtonColor);
				changeTileColor(startingButton, startingButtonColor);
				
				startingButtonIsClicked = false;
				return;
			} 
			
			else if (!startingPosition.equals(endingPosition)) {
				if (hintPositions.contains(endingPosition)) {
					
					previousChessBoards.push(new ChessBoard(chessBoard));
					redoChessBoards.clear();
										
					// System.out.println("startingPositionGameBoard: ");
					// ChessBoard.printChessBoard(startingPositionChessBoard.getGameBoard());
					
					// chessBoard.movePieceFromAPositionToAnother(startingPosition, endingPosition, true);
					
					Move move = new Move(startingPosition, endingPosition);
					chessBoard.makeMove(move, Constants.WHITE, true);
					
					hideHintPositions(hintPositions);
				}
				
				JButton startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
				Color startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);
				// System.out.println("startingButtonColor: " + startingButtonColor);
				changeTileColor(startingButton, startingButtonColor);
				
			}
			
			if (checkForGameOver()) return;

			if (GameParameters.enableSounds)
				Utilities.playSound("piece_move.wav");	
			
			// Remove the check from the king of the player who made the last move.
			// The thing that the player managed to make a move,
			// means that his king has escaped from the check.
			if (GameParameters.gameMode == Constants.HUMAN_VS_HUMAN) {
				if (chessBoard.isWhitePlays())
					chessBoard.setWhiteKingInCheck(false);
				else
					chessBoard.setBlackKingInCheck(false);
			}
			
			if (hintPositions.contains(endingPosition)) {
				System.out.println();
				System.out.println(chessBoard);
				
				// System.out.println("en passant position: " + chessBoard.getEnPassantPosition());
				
				if (undoItem != null)
					undoItem.setEnabled(true);
				if (redoItem != null)
					redoItem.setEnabled(false);
				
				// change chessBoard.turn
				chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
				chessBoard.setWhitePlays((chessBoard.isWhitePlays()) ? false : true);
				if (GameParameters.gameMode == Constants.HUMAN_VS_HUMAN) {
					String turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". ";
					turnMessage += (chessBoard.isWhitePlays()) ? "White plays." : "Black plays.";
					if (chessBoard.isWhitePlays() && chessBoard.isWhiteKingInCheck())
						turnMessage += " White king is in check!";
					else if (!chessBoard.isWhitePlays() && chessBoard.isBlackKingInCheck())
						turnMessage += " Black king is in check!";
					labelMessage.setText(turnMessage);
				}
				
				/* Random AI implementation here. */
				// The AI controls the Black pieces.
				if (GameParameters.gameMode == Constants.HUMAN_VS_RANDOM_AI && !chessBoard.isWhitePlays()) {
					// System.out.println("INSIDE RANDOM AI");
					randomAiMove();
				} 
				/* MiniMax AI implementation here. */
				else if (GameParameters.gameMode == Constants.HUMAN_VS_MINIMAX_AI && !chessBoard.isWhitePlays()) {
					// System.out.println("INSIDE MINIMAX AI");
					minimaxAiMove(ai);
				}
				
			}
		}
	}
	
	
	private static boolean checkForGameOver() {
		
		/* Check for White checkmate. */
		if (chessBoard.isWhitePlays()) {
			chessBoard.checkForWhiteCheckmate(true);
			if (chessBoard.isWhiteCheckmate()) {
				chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
				String turnMessage = "Move number: " 
						+ (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) 
						+ ". Checkmate. White wins!";
				labelMessage.setText(turnMessage);
				
				if (GameParameters.enableSounds)
					Utilities.playSound("checkmate.wav");

				int dialogResult = JOptionPane.showConfirmDialog(gui, 
						"White wins! Start a new game?", "Checkmate", JOptionPane.YES_NO_OPTION);
				// System.out.println("dialogResult:" + dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (undoItem != null)
						undoItem.setEnabled(false);
					startNewGame();
				} else {
					if (undoItem != null)
						undoItem.setEnabled(true);
					disableChessBoardSquares();
				}
				
				return true;
			}
		}
		
		/* Check for Black checkmate. */
		else {
			chessBoard.checkForBlackCheckmate(true);
			if (chessBoard.isBlackCheckmate()) {
				chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
				String turnMessage = "Move number: " 
						+ (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) 
						+ ". Checkmate. Black wins!";
				labelMessage.setText(turnMessage);
				
				if (GameParameters.enableSounds)
					Utilities.playSound("checkmate.wav");

				int dialogResult = JOptionPane.showConfirmDialog(gui, 
						"Black wins! Start a new game?", "Checkmate", JOptionPane.YES_NO_OPTION);
				// System.out.println("dialogResult:" + dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (undoItem != null)
						undoItem.setEnabled(false);
					startNewGame();
				} else {
					if (undoItem != null)
						undoItem.setEnabled(true);
					disableChessBoardSquares();
				}
				return true;
			}
		}
		
		/* Draw implementation. */
		chessBoard.checkFor2KingsLeftDraw();
		if (chessBoard.isTwoKingsLeftDraw()) {
			int dialogResult = JOptionPane.showConfirmDialog(gui, 
					"It is a draw! Start a new game?", "Draw", JOptionPane.YES_NO_OPTION);
			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (undoItem != null)
					undoItem.setEnabled(false);
				startNewGame();
			} else {
				if (undoItem != null)
					undoItem.setEnabled(true);
				disableChessBoardSquares();
			}
			return true;
		}
		
		/* Stalemate draw implementation. */
		// Check for White stalemate.
		if (!chessBoard.isWhitePlays() && !chessBoard.isWhiteKingInCheck()) {
			// System.out.println("Checking for white stalemate!");
			chessBoard.checkForWhiteStalemateDraw();
			if (chessBoard.isWhiteStalemateDraw()) {
				int dialogResult = JOptionPane.showConfirmDialog(gui, 
						"Stalemate! No legal moves for White exist. Start a new game?", 
						"Draw", JOptionPane.YES_NO_OPTION);
				// System.out.println("dialogResult:" + dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (undoItem != null)
						undoItem.setEnabled(false);
					startNewGame();
				} else {
					if (undoItem != null)
						undoItem.setEnabled(true);
					disableChessBoardSquares();
				}
				return true;
			}
		}
		
		// Check for Black stalemate.
		else if (chessBoard.isWhitePlays() && !chessBoard.isBlackKingInCheck()) {
			// System.out.println("Checking for black stalemate!");
			chessBoard.checkForBlackStalemateDraw();
			if (chessBoard.isBlackStalemateDraw()) {
				int dialogResult = JOptionPane.showConfirmDialog(gui, 
						"Stalemate! No legal moves for Black exist. Start a new game?", 
						"Draw", JOptionPane.YES_NO_OPTION);
				// System.out.println("dialogResult:" + dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (undoItem != null)
						undoItem.setEnabled(false);
					startNewGame();
				} else {
					if (undoItem != null)
						undoItem.setEnabled(true);
					disableChessBoardSquares();
				}
				return true;
			}
		}
		
		
		// 50 fullmoves without a piece capture Draw implementation.
		if (chessBoard.getHalfmoveClock() >= Constants.NO_CAPTURE_DRAW_HALFMOVES_LIMIT) {
			int dialogResult = JOptionPane.showConfirmDialog(gui, 
					"50 fullmoves have passed without a piece capture! Do you want to claim a draw? ",
					"Draw", JOptionPane.YES_NO_OPTION);
			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (undoItem != null)
					undoItem.setEnabled(false);
				startNewGame();
			}
			return true;
		}
		
		return false;
	}
	
	
	private static void randomAiMove() {

		String randomAiStartingPosition = "";
		String randomAiEndingPosition = "";
		
		// This map is used for the Random AI implementation.
		Map<String, Set<String>> randomStartingEndingPositions = new TreeMap<String, Set<String>>();
		
		/* STEP 1. Random starting position. */
		if (!chessBoard.isBlackKingInCheck()) {
			for (int ii=0; ii<chessBoard.getNumOfRows(); ii++) {
				for (int jj=0; jj<NUM_OF_COLUMNS; jj++) {
					if (chessBoard.getGameBoard()[ii][jj] < 0) {
						String randomStartingPosition = Utilities.getPositionByRowCol(ii, jj);
						Set<String> randomEndingPositions = chessBoard.getNextPositions(randomStartingPosition);
						
						if (randomEndingPositions.size() > 0) {
							randomStartingEndingPositions.put(randomStartingPosition, randomEndingPositions);
							// System.out.println("randomStartingPosition: " + randomStartingPosition + " -> " + randomEndingPositions);
						}
					}
				}
			}

			Random r = new Random();
			List<String> keys = new ArrayList<String>(randomStartingEndingPositions.keySet());
			if (randomStartingEndingPositions.size() > 0) {
				int randomStartingPositionIndex = r.nextInt(randomStartingEndingPositions.size());
				// System.out.println("randomStartingPositionIndex: " + randomStartingPositionIndex);
				randomAiStartingPosition = keys.get(randomStartingPositionIndex);
			}
			/* Stalemate implementation for Black Random AI. */
			// I think we do need it, because checked it from the human move.
			else {
				int dialogResult = JOptionPane.showConfirmDialog(gui, 
						"Stalemate! No legal moves for Black exist. Start a new game?", 
						"Draw", JOptionPane.YES_NO_OPTION);
				// System.out.println("dialogResult:" + dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					if (undoItem != null)
						undoItem.setEnabled(false);
					startNewGame();
				} else {
					if (undoItem != null)
						undoItem.setEnabled(true);
					disableChessBoardSquares();
				}
				return;
			}
			
		} 
		// If the Black King is in check, then get one of the following valid moves.
		else {
			// System.out.println("chessBoard.blackKingInCheckValidPieceMoves: " + chessBoard.blackKingInCheckValidPieceMoves);
			Random r = new Random();
			List<String> keys = new ArrayList<String>(chessBoard.getBlackKingInCheckValidPieceMoves().keySet());
			int randomStartingPositionIndex = r.nextInt(chessBoard.getBlackKingInCheckValidPieceMoves().size());
			
			randomAiStartingPosition = keys.get(randomStartingPositionIndex);
		}
		// System.out.println("random starting position: " + randomAiStartingPosition);
		
		
		/* STEP 2. Random ending position. */
		Set<String> possibleEndingPositions = new TreeSet<String>();
		if (!chessBoard.isBlackKingInCheck()) {
			possibleEndingPositions = randomStartingEndingPositions.get(randomAiStartingPosition);
		} else {
			possibleEndingPositions = chessBoard.getBlackKingInCheckValidPieceMoves().get(randomAiStartingPosition);
		}
		Random r = new Random();
		int randomEndingPositionIndex = r.nextInt(possibleEndingPositions.size());
		// System.out.println("randomEndingPositionIndex: " + randomEndingPositionIndex);
		int ii = 0;
		for (String possibleEndingPosition: possibleEndingPositions) {
		    if (ii == randomEndingPositionIndex) {
		    	randomAiEndingPosition = possibleEndingPosition; break;
		    }
		    ii++;
		}
		// System.out.println("random ending position: " + randomAiEndingPosition);
		
		// chessBoard.movePieceFromAPositionToAnother(randomAiStartingPosition, randomAiEndingPosition, true);
		// hideHintPositions(possibleEndingPositions);
		
		Move move = new Move(randomAiStartingPosition, randomAiEndingPosition, chessBoard.evaluate());
		chessBoard.makeMove(move, Constants.BLACK, true);
		
		if (isGameOver) return;
		
		// Remove the check from the king of the player who made the last move.
		// The thing that the player managed to make a move,
		// means that his king has escaped from the check.
		if (chessBoard.isWhitePlays())
			chessBoard.setWhiteKingInCheck(false);
		else
			chessBoard.setBlackKingInCheck(false);
		
		startingButtonIsClicked = false;
		
		chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
		chessBoard.setWhitePlays(true);
		String turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". White plays.";
		if (chessBoard.isWhiteKingInCheck())
			turnMessage += " White king is in check!";
		labelMessage.setText(turnMessage);
		
		System.out.println();
		System.out.println(chessBoard);
	}
	
	
	// Gets called after the human player makes a move. It makes a Minimax AI move.
	private static void minimaxAiMove(MiniMaxAi ai) {
		// Move aiMove = ai.miniMax(chessBoard);
		Move aiMove = ai.miniMaxAlphaBeta(chessBoard);
		System.out.println("aiMove: " + aiMove);
		
		String minimaxAiStartingPosition = aiMove.getPositions().get(0);
		String minimaxAiEndingPosition = aiMove.getPositions().get(1);
		
		/* Stalemate implementation for Black Minimax AI. */
		// I think we do need it, because checked it from the human move.
		if (minimaxAiStartingPosition.equals("") || minimaxAiEndingPosition.equals("")) {
			String message = "";
			if (ai.getAiPlayer() == Constants.BLACK) {
				message = "Stalemate! No legal moves for Black exist. Start a new game?";
			} else if (ai.getAiPlayer() == Constants.WHITE) {
				message = "Stalemate! No legal moves for White exist. Start a new game?";
			}
			int dialogResult = JOptionPane.showConfirmDialog(gui, message, "Draw", JOptionPane.YES_NO_OPTION);
			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (undoItem != null)
					undoItem.setEnabled(false);
				startNewGame();
			} else {
				if (undoItem != null)
					undoItem.setEnabled(true);
				disableChessBoardSquares();
			}
			return;
		}
		
		// chessBoard.movePieceFromAPositionToAnother(minimaxAiStartingPosition, minimaxAiEndingPosition, true);

		chessBoard.makeMove(aiMove, ai.getAiPlayer(), true);
		// System.out.println("board value after aiMove -> " + chessBoard.evaluate());

		isGameOver = checkForGameOver(); 
		if (isGameOver) return;
		
		// Remove the check from the king of the player who made the last move.
		// The thing that the player managed to make a move,
		// means that his king has escaped from the check.
		if (chessBoard.isWhitePlays())
			chessBoard.setWhiteKingInCheck(false);
		else
			chessBoard.setBlackKingInCheck(false);
		
		startingButtonIsClicked = false;
		
		chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
		
		chessBoard.setWhitePlays(chessBoard.isWhitePlays() ? false : true);
		String turnMessage;
		if (chessBoard.isWhitePlays()) {
			turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". White plays.";
			if (chessBoard.isWhiteKingInCheck())
				turnMessage += " White king is in check!";
		} else {
			turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". Black plays.";
			if (chessBoard.isBlackKingInCheck())
				turnMessage += " Black king is in check!";
		}
		labelMessage.setText(turnMessage);
		
		System.out.println();
		System.out.println(chessBoard);
	}
	
	
	private static void playAiVsAi() {
		MiniMaxAi ai1 = new MiniMaxAi(GameParameters.maxDepth1, Constants.WHITE);
		MiniMaxAi ai2 = new MiniMaxAi(GameParameters.maxDepth2, Constants.BLACK);
		
		while (!isGameOver) {
			previousChessBoards.push(new ChessBoard(chessBoard));
			minimaxAiMove(ai1);
			
			try {
				frame.paint(frame.getGraphics());
				frame.revalidate();
				frame.repaint();
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			isGameOver = checkForGameOver(); 
			if (isGameOver) return;
			
			previousChessBoards.push(new ChessBoard(chessBoard));
			minimaxAiMove(ai2);
			
			try {
				frame.paint(frame.getGraphics());
				frame.revalidate();
				frame.repaint();
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public static void hideHintPositions(Set<String> positionsToHide) {
		if (positionsToHide != null && positionsToHide.size() != 0) {
			for (String hintPosition: positionsToHide) {
				// System.out.println("hide hint position: " + hintPosition);
				int row = Utilities.getRowFromPosition(hintPosition);
				int column = Utilities.getColumnFromPosition(hintPosition);
				// System.out.println("hide hint row: " + row + ", hide hint column: " + column);
				JButton button = chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column];
				Color buttonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - row, column);
				changeTileColor(button, buttonColor);
			}
		}
	}


	private static void changeTileColor(JButton button, Color color) {
		button.setBackground(color);
	}
	
	
	public static Color getColorByRowCol(int row, int column) {
		Color color = null;
		if ((column % 2 == 1 && row % 2 == 1)
				//) {
			|| (column % 2 == 0 && row % 2 == 0)) {
			color = Color.WHITE;
		} else {
			color = GameParameters.blackTileColor;
		}
		return color;
	}
	
	
	private static ImageIcon preparePieceIcon(String imagepath) {
		ImageIcon pieceIcon = new ImageIcon(ResourceLoader.load(imagepath));
		Image image = pieceIcon.getImage(); // transform it 
		 // scale it the smooth way
		Image newimg = image.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);  
		pieceIcon = new ImageIcon(newimg);  // transform it back
		return pieceIcon; 
	}
	
	
	// It inserts the given piece to the given position on the board
	// (both the data structure and the GUI)
	public static void placePieceToPosition(String position, int piece) {
		String imagePath = "";
		switch (piece) {
			case Constants.WHITE_PAWN:
				imagePath = Constants.WHITE_PAWN_IMG_PATH;
				break;
			case Constants.WHITE_ROOK:
				imagePath = Constants.WHITE_ROOK_IMG_PATH;
				break;
			case Constants.WHITE_KNIGHT:
				imagePath = Constants.WHITE_KNIGHT_IMG_PATH;
				break;
			case Constants.WHITE_BISHOP:
				imagePath = Constants.WHITE_BISHOP_IMG_PATH;
				break;
			case Constants.WHITE_QUEEN:
				imagePath = Constants.WHITE_QUEEN_IMG_PATH;
				break;
			case Constants.WHITE_KING:
				imagePath = Constants.WHITE_KING_IMG_PATH;
				break;
			
			case Constants.BLACK_PAWN:
				imagePath = Constants.BLACK_PAWN_IMG_PATH;
				break;
			case Constants.BLACK_ROOK:
				imagePath = Constants.BLACK_ROOK_IMG_PATH;
				break;
			case Constants.BLACK_KNIGHT:
				imagePath = Constants.BLACK_KNIGHT_IMG_PATH;
				break;
			case Constants.BLACK_BISHOP:
				imagePath = Constants.BLACK_BISHOP_IMG_PATH;
				break;
			case Constants.BLACK_QUEEN:
				imagePath = Constants.BLACK_QUEEN_IMG_PATH;
				break;
			case Constants.BLACK_KING:
				imagePath = Constants.BLACK_KING_IMG_PATH;
				break;
		}
		
		ImageIcon pieceImage = ChessGUI.preparePieceIcon(imagePath);
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// System.out.println("chessBoardSquares.length: " + chessBoardSquares.length);
		// System.out.println("chessBoardSquares[0].length: " + chessBoardSquares[0].length);
		chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(pieceImage);
		
		chessBoard.getGameBoard()[row][column] = piece;
	}
	
	
	// It removes the given piece from the board (both the data structure and the GUI).
	public static void removePieceFromPosition(String position) {
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// Our chess pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon..
		ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
		chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(icon);
		
		chessBoard.getGameBoard()[row][column] = Constants.EMPTY;
	}
	
	
	public static void placePiecesToChessBoard() {
		
		for (int j=0; j<Constants.NUM_OF_COLUMNS; j++) {
			// System.out.println("test: " + ((char) (65 + j) + "2"));
			placePieceToPosition((char) (65 + j) + "2", Constants.WHITE_PAWN);
		}
		
		String leftWhiteRookPosition = "A1";
		placePieceToPosition(leftWhiteRookPosition, Constants.WHITE_ROOK);
		
		String leftWhiteKnightPosition = "B1";
		placePieceToPosition(leftWhiteKnightPosition, Constants.WHITE_KNIGHT);
		
		String leftWhiteBishopPosition = "C1";
		placePieceToPosition(leftWhiteBishopPosition, Constants.WHITE_BISHOP);
		
		String whiteQueenPosition = "D1";
		placePieceToPosition(whiteQueenPosition, Constants.WHITE_QUEEN);
		
		String whiteKingPosition = "E1";
		placePieceToPosition(whiteKingPosition, Constants.WHITE_KING);
		chessBoard.setWhiteKingPosition(whiteKingPosition);
		
		String rightWhiteBishopPosition = "F1";
		placePieceToPosition(rightWhiteBishopPosition, Constants.WHITE_BISHOP);
		
		String rightWhiteKnightPosition = "G1";
		placePieceToPosition(rightWhiteKnightPosition, Constants.WHITE_KNIGHT);

		String rightWhiteRookPosition = "H1";
		placePieceToPosition(rightWhiteRookPosition, Constants.WHITE_ROOK);
		
		for (int j=0; j<Constants.NUM_OF_COLUMNS; j++) {
			placePieceToPosition((char) (65 + j) + (chessBoard.getNumOfRows() - 1 + ""), Constants.BLACK_PAWN);
		}
		
		String leftBlackRookPosition = "A" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackRookPosition, Constants.BLACK_ROOK);
		
		String leftBlackKnightPosition = "B" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackKnightPosition, Constants.BLACK_KNIGHT);
		
		String leftBlackBishopPosition = "C" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackBishopPosition, Constants.BLACK_BISHOP);
		
		String blackQueenPosition = "D" + chessBoard.getNumOfRows();
		placePieceToPosition(blackQueenPosition, Constants.BLACK_QUEEN);
		
		String blackKingPosition = "E" + chessBoard.getNumOfRows();
		placePieceToPosition(blackKingPosition, Constants.BLACK_KING);
		chessBoard.setBlackKingPosition(blackKingPosition);
		
		String rightBlackBishopPosition = "F" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackBishopPosition, Constants.BLACK_BISHOP);
		
		String rightBlackKnightPosition = "G" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackKnightPosition, Constants.BLACK_KNIGHT);

		String rightBlackRookPosition = "H" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackRookPosition, Constants.BLACK_ROOK);
		
		chessBoard.setThreats();
		
		setTurnMessage();
	}
	
	
	public static void placePiecesToChessBoard(String fenPosition) {
		
		/* This is the default Chess starting game state. */
		// String fenPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
		// String fenPosition = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
		// String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
		// String fenPosition = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		
		/* This is used for debugging on the "stalemate", "draw" and "checkmate",
		 * "pawn promotion" and other conditions. */
		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "3q1Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		
		/* This is used for debugging on the "castling" implementation. */
		// String fenPosition = "4k3/8/8/8/8/8/8/R3K2R w KQ - 0 1";
		// String fenPosition = "3qk3/8/8/8/8/8/8/R2QK2R w KQ - 0 1";
		// String fenPosition = "r2qk2r/8/8/8/8/8/8/R2QK2R w KQkq - 0 1";
		
		try {
			chessBoard = FenUtilities.getChessBoardFromFenPosition(fenPosition);
		} catch (InvalidFenFormatException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<Constants.NUM_OF_COLUMNS; j++) {
				String piecePosition = Utilities.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
		
		setTurnMessage();
	}
	
	
	public static void makeChessBoardSquaresEmpty() {
		for (int i=0; i<GameParameters.numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(true);
				
				// Our chess pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon..
				ImageIcon icon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
				chessBoardSquares[i][j].setIcon(icon);
				
				Color color = getColorByRowCol(i, j);
				chessBoardSquares[i][j].setBackground(color);
				chessBoardSquares[i][j].setOpaque(true);
				// chessBoardSquares[i][j].setBorderPainted(false);
				
				chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1 - i][j] = Constants.EMPTY;
				chessBoard.setThreats();
			}
		}
	}
	
	
	static void enableChessBoardButtons() {
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				JButton button = chessBoardSquares[i][j];
				button.setEnabled(true);

				int row = chessBoard.getNumOfRows() - 1 - i;
				int column = j;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						chessButtonClick(row, column, button);
					}
				});
			}
		}
		buttonsEnabled = true;
	}
	
	
	static void disableChessBoardSquares() {
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(false);
				
				chessBoardSquares[i][j].removeActionListener(chessBoardSquares[i][j].getActionListeners()[0]);
			}
		}
		buttonsEnabled = false;
	}
	
	
	public final JComponent getChessBoardPanel() {
		return chessBoardPanel;
	}
	
	
	public final JComponent getGui() {
		return gui;
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(TITLE);
		ChessGUI.placePiecesToChessBoard();
		
		System.out.println(chessBoard);
	}

}
