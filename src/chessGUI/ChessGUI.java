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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
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
import enums.Allegiance;
import minimaxAi.MiniMaxAi;
import pieces.Bishop;
import pieces.ChessPiece;
import pieces.EmptyTile;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;
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
	
	private final static int CHESS_SQUARE_PIXEL_SIZE = 48;

	private final static int HEIGHT = (int) SCREEN_SIZE.getHeight() - 60;
	private final static int WIDTH = HEIGHT + 40;
	
	public static JFrame frame = new JFrame(TITLE);
	public static JPanel gui = new JPanel();
	
	public static JPanel chessBoardPanel;
	public static JPanel capturedPiecesPanel;
	
	public static String firstTurnMessage = "Move number: 1. White plays first.";
	public static JLabel labelMessage = new JLabel(firstTurnMessage);
	
	// The position (0, 0) of the chessBoardSquares,
	// corresponds to the position (7, 0) of the ChessBoard's gameBoard. 
	public static JButton[][] chessBoardSquares;
	
	// 30 captured pieces at maximum, 
	// plus 1 label for displaying the score = 31 labels size.
	public static JLabel[] capturedPiecesImages;
	
	private static JMenuBar menuBar;
	private static JMenu fileMenu;
	private static JMenuItem newGameItem;
	private static JMenuItem undoItem;
	private static JMenuItem redoItem;
	private static JMenuItem exportToGifItem;
	private static JMenuItem settingsItem;
	private static JMenuItem importFenPositionItem;
	private static JMenuItem exportFenPositionItem;
	private static JMenuItem saveCheckpointItem;
	private static JMenuItem loadCheckpointItem;
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
	
	// These stacks of "ChessBoard" objects are used to handle the "undo" and "redo" functionality.
	public static Stack<JLabel[]> previousCapturedPiecesImages = new Stack<JLabel[]>();
	public static Stack<JLabel[]> redoCapturedPiecesImages = new Stack<JLabel[]>();
		
	public static boolean startingButtonIsClicked = false;
	public static Set<String> hintPositions = new TreeSet<String>();
	
	public static boolean buttonsEnabled = true;
	
	// This variable is used for the implementation of Human Vs MiniMax AI.
	public static MiniMaxAi ai;
	
	// This variable is used for the implementation of MiniMax AI Vs MiniMax AI.
	public static boolean isGameOver;
	
	// This variable is true if the main is running from "ChessGui2" class.
	public static boolean isChessGui2;
	
	public static String savedFenPosition;
	
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
		
		frame.setLocation((int) (SCREEN_SIZE.getWidth() - frame.getWidth()) / 2, 5);
		
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
		exportFenPositionItem = new JMenuItem("Export FEN Position to file");
		saveCheckpointItem = new JMenuItem("Save Checkpoint");
		loadCheckpointItem = new JMenuItem("Load Checkpoint");
		aboutItem = new JMenuItem("About");
		exitItem = new JMenuItem("Exit");
		
		undoItem.setEnabled(false);
		redoItem.setEnabled(false);
		
		loadCheckpointItem.setEnabled(false);
		
		newGameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		});
		
		undoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoLastMove();
				exportFenPositionItem.setEnabled(true);
				saveCheckpointItem.setEnabled(true);
			}
		});
		
		redoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redoNextMove();
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
		
		exportFenPositionItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String exportedFenFilename = JOptionPane.showInputDialog(
						"Please type the name of the export file:",
						"exported_FEN_position.txt");
				
				String fenPosition = FenUtilities.getFenPositionFromChessBoard(chessBoard);
				
				if (fenPosition != null) {
					BufferedWriter bw = null;
					try {
						bw = new BufferedWriter(new FileWriter(exportedFenFilename));
						bw.write(fenPosition + "\n");
					} catch (IOException ex) {
						ex.printStackTrace();
					} finally {
						try {
							bw.flush();
							bw.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				}
				
			}
		});
		
		saveCheckpointItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!chessBoard.isTerminalState()) {
					savedFenPosition = FenUtilities.getFenPositionFromChessBoard(chessBoard);
					loadCheckpointItem.setEnabled(true);
				}
			}
		});
		
		loadCheckpointItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (savedFenPosition != null) {
					startNewGame();
					placePiecesToChessBoard(savedFenPosition);
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
		fileMenu.add(exportFenPositionItem);
		fileMenu.add(saveCheckpointItem);
		fileMenu.add(loadCheckpointItem);
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
			turnMessage += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";
		}
		if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck())
			turnMessage += " White king is in check!";
		else if (!chessBoard.whitePlays() && chessBoard.isBlackKingInCheck())
			turnMessage += " Black king is in check!";
		labelMessage.setText(turnMessage);
	}
	
	
	public static void setScoreMessage() {
		if (chessBoard.getScore() == 0) {
			ChessGUI.capturedPiecesImages[15].setText("Score: 0");
		} else if (chessBoard.getScore() > 0) {
			ChessGUI.capturedPiecesImages[15].setText("White: +" + chessBoard.getScore());
		} else if (chessBoard.getScore() < 0) {
			ChessGUI.capturedPiecesImages[15].setText("Black: +" + (-chessBoard.getScore()));
		}
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
			
			// Push to the redoCapturedPiecesImages Stack.
			JLabel[] newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			redoCapturedPiecesImages.push(newCapturedPiecesImages);
			
			if (previousChessBoards.isEmpty()) {
				undoItem.setEnabled(false);
			}
			
			chessBoard = previousChessBoards.pop();
			
			// Display the previous captured chess pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = previousCapturedPiecesImages.pop();
			for (int i=0; i<31; i++) {
			    capturedPiecesPanel.add(capturedPiecesImages[i]);
			}
			
			// This is true if any terminal state has occured.
			// The terminal states are: "draw", "stalemate draw" & "checkmate"
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
			setScoreMessage();
			
			if (redoItem != null)
				redoItem.setEnabled(true);
		}
	}
	
	
	// NOTE: We are not able to perform a redo,
	// if we are in a terminal state, because the game has ended.
	private static void redoNextMove() {
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
			
			// Push to the previousCapturedPiecesImages Stack.
			JLabel[] newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			previousCapturedPiecesImages.push(newCapturedPiecesImages);
			
			chessBoard = redoChessBoards.pop();

			// Display the redo captured chess pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = redoCapturedPiecesImages.pop();
			for (int i=0; i<31; i++) {
			    capturedPiecesPanel.add(capturedPiecesImages[i]);
			}
			
			if (redoChessBoards.isEmpty()) {
				redoItem.setEnabled(false);
			}
			
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					placePieceToPosition(Utilities.getPositionByRowCol(i, j), chessBoard.getGameBoard()[i][j]);		
				}
			}
			
			System.out.println();
			System.out.println(chessBoard);
			
			setTurnMessage();
			setScoreMessage();
			
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
		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));
		
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		gui.add(tools, BorderLayout.NORTH);
		tools.add(labelMessage);
		
		initializeChessBoardPanel();
		initializeChessBoardSquareButtons();
				
		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();
	}
	
	
	public static void initializeChessBoardPanel() {
		if (chessBoardPanel != null)
			gui.remove(chessBoardPanel);
		chessBoardPanel = new JPanel(new GridLayout(GameParameters.numOfRows+2, NUM_OF_COLUMNS+2));
		chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
		chessBoardPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 100));
		gui.add(chessBoardPanel, BorderLayout.CENTER);
	}
	
	
	public static void initializeCapturedPiecesPanel() {		
		if (capturedPiecesPanel != null)
			gui.remove(capturedPiecesPanel);
		capturedPiecesPanel = new JPanel();
		gui.add(capturedPiecesPanel, BorderLayout.SOUTH);
	}
	
	
	public static void initializeChessBoardSquareButtons() {

		chessBoardSquares = new JButton[GameParameters.numOfRows][NUM_OF_COLUMNS];
		
		// Create the chess board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i=0; i<GameParameters.numOfRows; i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);
				
				// Our chess pieces are 64x64 px in size, so we'll
				// "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(CHESS_SQUARE_PIXEL_SIZE, CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
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
		// fill the black non-pawn chessPiece row
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

	public static void initializeCapturedPiecesImages() {

		capturedPiecesImages = new JLabel[31];
		
		// Create the captured chess pieces icons.
		for (int i=0; i<31; i++) {
			capturedPiecesImages[i] = new JLabel();
			
			if (i == 15) {
				ChessGUI.capturedPiecesImages[i].setText("Score: 0");
			} else {
				// We'll "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(Constants.CAPTURED_PIECE_PIXEL_SIZE, Constants.CAPTURED_PIECE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				capturedPiecesImages[i].setIcon(icon);
				
				// This is for TESTING.
//				ImageIcon pieceImage = ChessGUI.preparePieceIcon(Constants.WHITE_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
//				ChessGUI.capturedPiecesImages[i].setIcon(pieceImage);
			}

		    capturedPiecesPanel.add(capturedPiecesImages[i]);
		}
		
	}
	
	public static void startNewGame() {
		System.out.println("Starting new game!");
		
		configureGuiStyle();
		// restoreDefaultValues();

		/* If running "ChessGUI.java", use this! */
		chessBoardPanel.removeAll();
		if (!isChessGui2) {
			initializeChessBoardPanel();
			initializeCapturedPiecesPanel();
		}

		initializeChessBoardSquareButtons();
		initializeCapturedPiecesImages();
		
		//* If running "ChessGui2.java", you must use this! */
		chessBoardPanel.revalidate();
		chessBoardPanel.repaint();
		
		// if (!buttonsEnabled)
		//	enableChessBoardButtons();
		
		restoreDefaultValues();
		
		// The call of the following method is important.
		// Consider the case where the number of rows has been changed.
		makeChessBoardSquaresEmpty();
		
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
		previousCapturedPiecesImages.clear();
		redoChessBoards.clear();
		redoCapturedPiecesImages.clear();
		
		startingButtonIsClicked = false;
		
		hintPositions = new TreeSet<String>();
		
		if (undoItem != null)
			undoItem.setEnabled(false);
		if (redoItem != null)
			redoItem.setEnabled(false);
		if (exportFenPositionItem != null)
			exportFenPositionItem.setEnabled(true);
		if (saveCheckpointItem != null)
			saveCheckpointItem.setEnabled(true);
		
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
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];
		// System.out.println("chessPiece: " + chessPiece);
		
		int startingPositionRow = 0;
		int startingPositionColumn = 0;
		ChessPiece startingPiece = null;
		if (!startingPosition.equals("")) {
			startingPositionRow = Utilities.getRowFromPosition(startingPosition);
			startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
			startingPiece = chessBoard.getGameBoard()[startingPositionRow][startingPositionColumn];
		}
		
		if (!startingButtonIsClicked &&
			(chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| chessPiece.getAllegiance() == Allegiance.BLACK && !chessBoard.whitePlays())) {
			
			startingPosition = position;
			// System.out.println("startingPosition: " + startingPosition);
			
//			System.out.println("chessBoard: ");
//			System.out.println(chessBoard);
			
			if (!(chessPiece instanceof EmptyTile)) {
				
				// Get the hint positions.
				if (chessBoard.whitePlays() && !chessBoard.isWhiteKingInCheck() 
					|| !chessBoard.whitePlays() && !chessBoard.isBlackKingInCheck()) {
					hintPositions = chessBoard.getNextPositions(position);
					
					// System.out.println("chessBoard: ");
					// System.out.println(chessBoard);
				}
				// If the White or Black King is in check, then get one of the following valid moves.
				else if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck() || !chessBoard.whitePlays() 
						&& chessBoard.isBlackKingInCheck()) {
					hintPositions = new TreeSet<String>();
					
					if (chessBoard.whitePlays() 
							&& chessBoard.getWhiteKingInCheckValidPieceMoves().containsKey(startingPosition)) {
						hintPositions = chessBoard.getWhiteKingInCheckValidPieceMoves().get(startingPosition);
					}
					else if (!chessBoard.whitePlays() 
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
						
						ChessPiece hintPositionPiece = chessBoard.getGameBoard()[hintPositionRow][hintPositionColumn];
						
						if (chessPiece.getAllegiance() != hintPositionPiece.getAllegiance()
							&& hintPositionPiece.getAllegiance() != Allegiance.EMPTY
							|| chessBoard.getEnPassantPosition().equals(hintPosition) && chessPiece instanceof Pawn) {
							changeTileColor(hintPositionButton, Color.RED);
						} else if (chessPiece instanceof Pawn &&
									(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == chessBoard.getNumOfRows() - 1
									|| chessPiece.getAllegiance() == Allegiance.BLACK && hintPositionRow == 0)) {
							changeTileColor(hintPositionButton, Color.GREEN);
						} else if (hintPositionPiece instanceof EmptyTile) {
							changeTileColor(hintPositionButton, Color.BLUE);
						}
						
					}
				}
				
				// System.out.println("chessBoard: ");
				// System.out.println(chessBoard);
								
				startingButtonIsClicked = true;
			}
			
		} else if (startingButtonIsClicked &&
				(startingPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| startingPiece.getAllegiance() == Allegiance.BLACK && !chessBoard.whitePlays())) {
			
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
					
					// Push to the previousCapturedPiecesImages Stack.
					JLabel[] newCapturedPiecesImages = new JLabel[31];
					for (int i=0; i<=30; i++) {
						newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
					}
					previousCapturedPiecesImages.push(newCapturedPiecesImages);
					
					redoChessBoards.clear();
					redoCapturedPiecesImages.clear();
										
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
				if (chessBoard.whitePlays())
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
				chessBoard.setPlayer((chessBoard.whitePlays()) ? false : true);
				if (GameParameters.gameMode == Constants.HUMAN_VS_HUMAN) {
					String turnMessage = "Move number: " + (int) Math.ceil((float) chessBoard.getHalfmoveNumber() / 2) + ". ";
					turnMessage += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";
					if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck())
						turnMessage += " White king is in check!";
					else if (!chessBoard.whitePlays() && chessBoard.isBlackKingInCheck())
						turnMessage += " Black king is in check!";
					labelMessage.setText(turnMessage);
				}
				
				/* Random AI implementation here. */
				// The AI controls the Black pieces.
				if (GameParameters.gameMode == Constants.HUMAN_VS_RANDOM_AI && !chessBoard.whitePlays()) {
					// System.out.println("INSIDE RANDOM AI");
					randomAiMove();
				}
				/* MiniMax AI implementation here. */
				else if (GameParameters.gameMode == Constants.HUMAN_VS_MINIMAX_AI && !chessBoard.whitePlays()) {
					// System.out.println("INSIDE MINIMAX AI");
					minimaxAiMove(ai);
				}
				
			}
		}
	}
	
	
	static boolean checkForGameOver() {
		
		/* Check for White checkmate. */
		if (chessBoard.whitePlays()) {
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
					if (exportFenPositionItem != null)
						exportFenPositionItem.setEnabled(false);
					if (saveCheckpointItem != null)
						saveCheckpointItem.setEnabled(false);
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
					if (exportFenPositionItem != null)
						exportFenPositionItem.setEnabled(false);
					if (saveCheckpointItem != null)
						saveCheckpointItem.setEnabled(false);
					disableChessBoardSquares();
				}
				
				return true;
			}
		}
		
		/* Draw implementation. */
		chessBoard.checkForTwoKingsLeftDraw();
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
				if (exportFenPositionItem != null)
					exportFenPositionItem.setEnabled(false);
				if (saveCheckpointItem != null)
					saveCheckpointItem.setEnabled(false);
				disableChessBoardSquares();
			}
			
			return true;
		}
		
		/* Stalemate draw implementation. */
		// Check for White stalemate.
		if (!chessBoard.whitePlays() && !chessBoard.isWhiteKingInCheck()) {
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
					if (exportFenPositionItem != null)
						exportFenPositionItem.setEnabled(false);
					if (saveCheckpointItem != null)
						saveCheckpointItem.setEnabled(false);
					disableChessBoardSquares();
				}
				
				return true;
			}
		}
		
		// Check for Black stalemate.
		else if (chessBoard.whitePlays() && !chessBoard.isBlackKingInCheck()) {
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
					if (exportFenPositionItem != null)
						exportFenPositionItem.setEnabled(false);
					if (saveCheckpointItem != null)
						saveCheckpointItem.setEnabled(false);
					disableChessBoardSquares();
				}
				
				return true;
			}
		}
		
		
		// 50 fullmoves without a chessPiece capture Draw implementation.
		if (chessBoard.getHalfmoveClock() >= Constants.NO_CAPTURE_DRAW_HALFMOVES_LIMIT) {
			int dialogResult = JOptionPane.showConfirmDialog(gui, 
					(int) Math.ceil(Constants.NO_CAPTURE_DRAW_HALFMOVES_LIMIT / (double) 2) + 
					" fullmoves have passed without a chessPiece capture! Do you want to claim a draw? ",
					"Draw", JOptionPane.YES_NO_OPTION);
			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				if (undoItem != null)
					undoItem.setEnabled(false);
				startNewGame();
				
				return true;
			}
			
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
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					if (chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.BLACK) {
						String randomStartingPosition = Utilities.getPositionByRowCol(i, j);
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
		int i = 0;
		for (String possibleEndingPosition: possibleEndingPositions) {
		    if (i == randomEndingPositionIndex) {
		    	randomAiEndingPosition = possibleEndingPosition; break;
		    }
		    i++;
		}
		// System.out.println("random ending position: " + randomAiEndingPosition);
		
		// chessBoard.movePieceFromAPositionToAnother(randomAiStartingPosition, randomAiEndingPosition, true);
		// hideHintPositions(possibleEndingPositions);
		
		Move move = new Move(randomAiStartingPosition, randomAiEndingPosition, chessBoard.evaluate());
		chessBoard.makeMove(move, Constants.BLACK, true);
		
		isGameOver = checkForGameOver(); 
		if (isGameOver) return;
		
		// Remove the check from the king of the player who made the last move.
		// The thing that the player managed to make a move,
		// means that his king has escaped from the check.
		if (chessBoard.whitePlays())
			chessBoard.setWhiteKingInCheck(false);
		else
			chessBoard.setBlackKingInCheck(false);
		
		startingButtonIsClicked = false;
		
		chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
		chessBoard.setPlayer(true);
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
		
		chessBoard.makeMove(aiMove, ai.getAiPlayer(), true);
		// System.out.println("board value after aiMove -> " + chessBoard.evaluate());
		
		isGameOver = checkForGameOver(); 
		if (isGameOver) return;
		
		// Remove the check from the king of the player who made the last move.
		// The thing that the player managed to make a move,
		// means that his king has escaped from the check.
		if (chessBoard.whitePlays())
			chessBoard.setWhiteKingInCheck(false);
		else
			chessBoard.setBlackKingInCheck(false);
		
		startingButtonIsClicked = false;
		
		chessBoard.setHalfmoveNumber(chessBoard.getHalfmoveNumber() + 1);
		
		chessBoard.setPlayer(chessBoard.whitePlays() ? false : true);
		String turnMessage;
		if (chessBoard.whitePlays()) {
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
			
			// Push to the previousCapturedPiecesImages Stack.
			JLabel[] newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			previousCapturedPiecesImages.push(newCapturedPiecesImages);
			
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
			
			// Push to the previousCapturedPiecesImages Stack.
			newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			previousCapturedPiecesImages.push(newCapturedPiecesImages);
			
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
	
	
	public static ImageIcon preparePieceIcon(String imagepath, int size) {
		ImageIcon pieceIcon = new ImageIcon(ResourceLoader.load(imagepath));
		Image image = pieceIcon.getImage(); // transform it 
		 // scale it the smooth way
		Image newimg = image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);  
		pieceIcon = new ImageIcon(newimg);  // transform it back
		return pieceIcon; 
	}
	
	
	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI)
	public static void placePieceToPosition(String position, ChessPiece chessPiece) {
		String imagePath = "";
		
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			if (chessPiece instanceof Pawn) {
				imagePath = Constants.WHITE_PAWN_IMG_PATH;
			} else if (chessPiece instanceof Rook) {
				imagePath = Constants.WHITE_ROOK_IMG_PATH;
			} else if (chessPiece instanceof Knight) {
				imagePath = Constants.WHITE_KNIGHT_IMG_PATH;
			} else if (chessPiece instanceof Bishop) {
				imagePath = Constants.WHITE_BISHOP_IMG_PATH;
			} else if (chessPiece instanceof Queen) {
				imagePath = Constants.WHITE_QUEEN_IMG_PATH;
			} else if (chessPiece instanceof King) {
				imagePath = Constants.WHITE_KING_IMG_PATH;
			}
		}
		
		else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			if (chessPiece instanceof Pawn) {
				imagePath = Constants.BLACK_PAWN_IMG_PATH;
			} else if (chessPiece instanceof Rook) {
				imagePath = Constants.BLACK_ROOK_IMG_PATH;
			} else if (chessPiece instanceof Knight) {
				imagePath = Constants.BLACK_KNIGHT_IMG_PATH;
			} else if (chessPiece instanceof Bishop) {
				imagePath = Constants.BLACK_BISHOP_IMG_PATH;
			} else if (chessPiece instanceof Queen) {
				imagePath = Constants.BLACK_QUEEN_IMG_PATH;
			} else if (chessPiece instanceof King) {
				imagePath = Constants.BLACK_KING_IMG_PATH;
			}
		}
		
		ImageIcon pieceImage = ChessGUI.preparePieceIcon(imagePath, CHESS_SQUARE_PIXEL_SIZE);
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// System.out.println("chessBoardSquares.length: " + chessBoardSquares.length);
		// System.out.println("chessBoardSquares[0].length: " + chessBoardSquares[0].length);
		chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(pieceImage);
		
		chessBoard.getGameBoard()[row][column] = chessPiece;
	}
	
	
	// It removes the given chessPiece from the board (both the data structure and the GUI).
	public static void removePieceFromPosition(String position) {
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// Our chess pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon..
		ImageIcon icon = new ImageIcon(new BufferedImage(CHESS_SQUARE_PIXEL_SIZE, CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
		chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(icon);
		
		chessBoard.getGameBoard()[row][column] = new EmptyTile();
	}
	
	
	public static void placePiecesToChessBoard() {
		
		for (int j=0; j<Constants.NUM_OF_COLUMNS; j++) {
			// System.out.println("test: " + ((char) (65 + j) + "2"));
			placePieceToPosition((char) (65 + j) + "2", new Pawn(Allegiance.WHITE));
		}
		
		String leftWhiteRookPosition = "A1";
		placePieceToPosition(leftWhiteRookPosition, new Rook(Allegiance.WHITE));
		
		String leftWhiteKnightPosition = "B1";
		placePieceToPosition(leftWhiteKnightPosition, new Knight(Allegiance.WHITE));
		
		String leftWhiteBishopPosition = "C1";
		placePieceToPosition(leftWhiteBishopPosition, new Bishop(Allegiance.WHITE));
		
		String whiteQueenPosition = "D1";
		placePieceToPosition(whiteQueenPosition, new Queen(Allegiance.WHITE));
		
		String whiteKingPosition = "E1";
		placePieceToPosition(whiteKingPosition, new King(Allegiance.WHITE));
		chessBoard.setWhiteKingPosition(whiteKingPosition);
		
		String rightWhiteBishopPosition = "F1";
		placePieceToPosition(rightWhiteBishopPosition, new Bishop(Allegiance.WHITE));
		
		String rightWhiteKnightPosition = "G1";
		placePieceToPosition(rightWhiteKnightPosition, new Knight(Allegiance.WHITE));

		String rightWhiteRookPosition = "H1";
		placePieceToPosition(rightWhiteRookPosition, new Rook(Allegiance.WHITE));
		
		for (int j=0; j<Constants.NUM_OF_COLUMNS; j++) {
			placePieceToPosition((char) (65 + j) + (chessBoard.getNumOfRows() - 1 + ""), 
					new Pawn(Allegiance.BLACK));
		}
		
		String leftBlackRookPosition = "A" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackRookPosition, new Rook(Allegiance.BLACK));
		
		String leftBlackKnightPosition = "B" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackKnightPosition, new Knight(Allegiance.BLACK));
		
		String leftBlackBishopPosition = "C" + chessBoard.getNumOfRows();
		placePieceToPosition(leftBlackBishopPosition, new Bishop(Allegiance.BLACK));
		
		String blackQueenPosition = "D" + chessBoard.getNumOfRows();
		placePieceToPosition(blackQueenPosition, new Queen(Allegiance.BLACK));
		
		String blackKingPosition = "E" + chessBoard.getNumOfRows();
		placePieceToPosition(blackKingPosition, new King(Allegiance.BLACK));
		chessBoard.setBlackKingPosition(blackKingPosition);
		
		String rightBlackBishopPosition = "F" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackBishopPosition, new Bishop(Allegiance.BLACK));
		
		String rightBlackKnightPosition = "G" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackKnightPosition, new Knight(Allegiance.BLACK));

		String rightBlackRookPosition = "H" + chessBoard.getNumOfRows();
		placePieceToPosition(rightBlackRookPosition, new Rook(Allegiance.BLACK));
		
		chessBoard.setThreats();
		
		setTurnMessage();
	}
	
	
	public static void placePiecesToChessBoard(String fenPosition) {
		
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
				ImageIcon icon = new ImageIcon(new BufferedImage(CHESS_SQUARE_PIXEL_SIZE, CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				chessBoardSquares[i][j].setIcon(icon);
				
				Color color = getColorByRowCol(i, j);
				chessBoardSquares[i][j].setBackground(color);
				chessBoardSquares[i][j].setOpaque(true);
				// chessBoardSquares[i][j].setBorderPainted(false);
				
				chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1 - i][j] = new EmptyTile();
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
