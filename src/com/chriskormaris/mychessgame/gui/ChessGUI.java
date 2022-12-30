package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.ai.AI;
import com.chriskormaris.mychessgame.api.ai.MinimaxAI;
import com.chriskormaris.mychessgame.api.ai.MinimaxAlphaBetaPruningAI;
import com.chriskormaris.mychessgame.api.ai.RandomChoiceAI;
import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.enumeration.GameResult;
import com.chriskormaris.mychessgame.api.exception.InvalidFenFormatException;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptyTile;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.api.util.FenUtils;
import com.chriskormaris.mychessgame.api.util.Utilities;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;
import com.chriskormaris.mychessgame.gui.util.GuiUtils;
import com.chriskormaris.mychessgame.gui.util.ResourceLoader;
import com.chriskormaris.mychessgame.gui.util.SoundUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static com.chriskormaris.mychessgame.api.util.Constants.NUM_OF_COLUMNS;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.FIRST_TURN_TEXT;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.TITLE;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.ZERO_SCORE_TEXT;
import static com.chriskormaris.mychessgame.gui.util.SoundUtils.CHECKMATE_SOUND;
import static com.chriskormaris.mychessgame.gui.util.SoundUtils.PIECE_MOVE_SOUND;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;


public class ChessGUI {

	private static final int HEIGHT = GuiConstants.DEFAULT_HEIGHT;
	private static final int WIDTH = GuiConstants.DEFAULT_WIDTH;

	private static final JPanel gui = new JPanel();
	private static final JTextPane turnTextPane = new JTextPane();

	// These stacks of FEN Position String objects are used to handle the "undo" and "redo" functionality.
	private static final Stack<String> undoFenPositions = new Stack<>();
	private static final Stack<String> redoFenPositions = new Stack<>();

	// These stacks of "JLabel" arrays are used to handle the "undo" and "redo" functionality.
	private static final Stack<JLabel[]> undoCapturedPiecesImages = new Stack<>();
	private static final Stack<JLabel[]> redoCapturedPiecesImages = new Stack<>();

	public static GameParameters gameParameters = new GameParameters();
	public static GameParameters newGameParameters = new GameParameters(gameParameters);

	public static JFrame frame;

	// The position (0, 0) of the "chessBoard.getGameBoard()" is the upper left button
	// of the JButton array "chessBoardSquares".
	// The position (gameParameters.getNumOfRows()-1, 0) of the "chessBoard.getGameBoard()" is the lower left button
	// of the JButton array "chessBoardSquares".
	public static ChessBoard chessBoard = new ChessBoard();

	// This variable is used for the implementation of "Human Vs AI".
	public static AI ai;

	// These stack of FEN Position String objects are used
	// to check for a threefold repetition of a chess board position.
	public static Stack<String> halfMoveFenPositions = new Stack<>();
	public static Stack<String> redoHalfMoveFenPositions = new Stack<>();

	private static JToolBar tools = new JToolBar();
	private static JPanel chessBoardPanel;
	private static JPanel capturedPiecesPanel;

	// The position (0, 0) of the chessBoardSquares,
	// corresponds to the position (NUM_OF_COLUMNS - 1, 0) of the ChessBoard's gameBoard.
	private static JButton[][] chessBoardSquares;

	// 30 captured pieces at maximum,
	// plus 1 label for displaying the score = 31 labels size.
	private static JLabel[] capturedPiecesImages;

	private static String startingPosition = "";
	private static String endingPosition = "";

	private static boolean startingButtonIsClicked = false;

	private static Set<String> hintPositions = new HashSet<>();

	private static boolean buttonsEnabled = true;

	// This variable is used for the implementation of "AI Vs AI".
	private static boolean isGameOver;

	private static String savedFenPosition;

	private static GameResult gameResult;

	private static JMenuBar menuBar;
	private static JMenu fileMenu;
	private static JMenuItem newGameItem;
	private static JMenuItem undoItem;
	private static JMenuItem redoItem;
	private static JMenuItem exportToGifItem;
	private static JMenuItem settingsItem;
	private static JMenuItem importStartingFenPositionItem;
	private static JMenuItem exportFenPositionItem;
	private static JMenuItem saveCheckpointItem;
	private static JMenuItem loadCheckpointItem;
	private static JMenuItem exitItem;
	private static JMenu helpMenu;
	private static JMenuItem howToPlayItem;
	private static JMenuItem aboutItem;

	/*
	private static int whiteMinimaxAiMoveElapsedSecs;
	private static int blackMinimaxAiMoveElapsedSecs;

    private static double whiteMinimaxAiMoveAverageSecs;
    private static double blackMinimaxAiMoveAverageSecs;
	*/

	public ChessGUI(String title) {
		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);

		configureGuiStyle();

		initializeGui();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
			if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					if (gameParameters.getAi1MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.BLACK,
								gameParameters.getEvaluationFunction1());
					} else {
						ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.BLACK,
								gameParameters.getEvaluationFunction1());
					}
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					if (gameParameters.getAi2MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi2MaxDepth(), Constants.WHITE,
								gameParameters.getEvaluationFunction2());
					} else {
						ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi2MaxDepth(), Constants.WHITE,
								gameParameters.getEvaluationFunction2());
					}
				}
			} else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					ai = new RandomChoiceAI(Constants.BLACK);
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					ai = new RandomChoiceAI(Constants.WHITE);
				}
			}
		}

		frame = new JFrame(title);
		frame.add(gui);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);

		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		frame.pack();

		frame.setSize(new Dimension(WIDTH, HEIGHT));

		// ensures the minimum size is enforced.
		frame.setMinimumSize(frame.getSize());

		frame.setLocation((int) (GuiConstants.SCREEN_SIZE.getWidth() - frame.getWidth()) / 2, 5);

		frame.setResizable(false);
		// frame.setFocusable(true);

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			frame.setIconImage(icon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newGameItem = new JMenuItem("New Game");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		exportToGifItem = new JMenuItem("Export to .gif");
		settingsItem = new JMenuItem("Settings");
		importStartingFenPositionItem = new JMenuItem("Import starting FEN position");
		exportFenPositionItem = new JMenuItem("Export FEN position to file");
		saveCheckpointItem = new JMenuItem("Save Checkpoint");
		loadCheckpointItem = new JMenuItem("Load Checkpoint");
		exitItem = new JMenuItem("Exit");

		helpMenu = new JMenu("Help");
		howToPlayItem = new JMenuItem("How to Play");
		aboutItem = new JMenuItem("About");

		undoItem.setEnabled(false);
		redoItem.setEnabled(false);

		loadCheckpointItem.setEnabled(false);

		newGameItem.addActionListener(e -> startNewGame());

		undoItem.addActionListener(e -> {
			undoLastMove();
			exportFenPositionItem.setEnabled(true);
			saveCheckpointItem.setEnabled(true);
		});

		redoItem.addActionListener(e -> redoNextMove());

		exportToGifItem.addActionListener(e -> exportToGif());

		settingsItem.addActionListener(e -> {
			SettingsWindow settings = new SettingsWindow(frame);
			settings.setVisible(true);
		});

		importStartingFenPositionItem.addActionListener(e -> {
			String fenPosition = (String) JOptionPane.showInputDialog(frame,
					"Please insert the starting \"FEN\" position in the text field below:" +
							"                      ",
					"Import starting FEN position",
					QUESTION_MESSAGE, null, null,
					Constants.DEFAULT_STARTING_FEN_POSITION);

			if (fenPosition != null) {
				// gameParameters.getNumOfRows() = Constants.DEFAULT_NUM_OF_ROWS;
				startNewGame(fenPosition);
			}
		});

		exportFenPositionItem.addActionListener(e -> {
			String exportedFenFilename = (String) JOptionPane.showInputDialog(
					frame,
					"Please type the name of the export file:",
					"Export FEN position",
					QUESTION_MESSAGE,
					null,
					null,
					"exported_FEN_position.txt"
			);

			if (exportedFenFilename != null) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportedFenFilename))) {
					String fenPosition = FenUtils.getFenPositionFromChessBoard(chessBoard);
					bw.write(fenPosition + "\n");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});

		saveCheckpointItem.addActionListener(e -> {
			if (!chessBoard.isTerminalState()) {
				savedFenPosition = FenUtils.getFenPositionFromChessBoard(chessBoard);
				loadCheckpointItem.setEnabled(true);
			}
		});

		loadCheckpointItem.addActionListener(e -> {
			if (savedFenPosition != null) {
				startNewGame(savedFenPosition);
			}
		});

		exitItem.addActionListener(e -> System.exit(0));

		howToPlayItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, GuiConstants.RULES, "How to Play",
				JOptionPane.INFORMATION_MESSAGE));

		aboutItem.addActionListener(e -> {
			JLabel label = new JLabel("<html>A traditional chess game implementation using Minimax AI,<br>"
					+ "with Alpha-Beta Pruning.<br>© Created by: Christos Kormaris, Athens 2020<br>"
					+ "Version " + GuiConstants.VERSION + "</html>");

			BufferedImage img = null;
			try {
				img = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			Image dImg = img.getScaledInstance(
					GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
					GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
					Image.SCALE_SMOOTH
			);
			ImageIcon icon1 = new ImageIcon(dImg);

			JOptionPane.showMessageDialog(frame, label, "About", JOptionPane.PLAIN_MESSAGE, icon1);
		});

		fileMenu.add(newGameItem);
		fileMenu.add(undoItem);
		fileMenu.add(redoItem);
		fileMenu.add(exportToGifItem);
		fileMenu.add(settingsItem);
		fileMenu.add(importStartingFenPositionItem);
		fileMenu.add(exportFenPositionItem);
		fileMenu.add(saveCheckpointItem);
		fileMenu.add(loadCheckpointItem);
		fileMenu.add(exitItem);

		helpMenu.add(howToPlayItem);
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		frame.setJMenuBar(menuBar);

		frame.setVisible(true);
	}


	public static void configureGuiStyle() {
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			if (gameParameters.getGuiStyle() == GuiStyle.CROSS_PLATFORM_STYLE) {
				// Option 1
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} else if (gameParameters.getGuiStyle() == GuiStyle.NIMBUS_STYLE) {
				// Option 2
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
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


	public static void setTurnMessage() {
		if (chessBoard.getHalfMoveNumber() == 1) {
			turnTextPane.setText(FIRST_TURN_TEXT);
		} else {
			String turnMessage = "Turn: " + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". ";
			turnMessage += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";

			if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck()) {
				turnMessage += " White king is in check!";
			} else if (chessBoard.blackPlays() && chessBoard.isBlackKingInCheck()) {
				turnMessage += " Black king is in check!";
			}

			turnTextPane.setText(turnMessage);
		}
	}


	/*
	public static void updateMinimaxAiMoveElapsedSecs() {
		setTurnMessage();

		String turnMessage = turnTextPane.getText();

		if (chessBoard.blackPlays() &&
				(gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.AI_VS_AI
				&& gameParameters.getAiType() == AiType.MINIMAX_AI)) {
			blackMinimaxAiMoveElapsedSecs++;
			turnMessage += " Minimax AI has been thinking for: " + blackMinimaxAiMoveElapsedSecs + " secs";
	    	// System.out.println("blackMinimaxAiMoveElapsedSecs: " + blackMinimaxAiMoveElapsedSecs);
		} else if (chessBoard.whitePlays() &&
				(gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.AI_VS_AI
				&& gameParameters.getAiType() == AiType.MINIMAX_AI)) {
	    	whiteMinimaxAiMoveElapsedSecs++;
			turnMessage += " Minimax AI has been thinking for: " + whiteMinimaxAiMoveElapsedSecs + " secs";
	    	// System.out.println("whiteMinimaxAiMoveElapsedSecs: " + whiteMinimaxAiMoveElapsedSecs);
		}

		turnTextPane.setText(turnMessage);
	}
	*/


	public static void setScoreMessage() {
		if (chessBoard.getScore() == 0) {
			capturedPiecesImages[15].setText(ZERO_SCORE_TEXT);
		} else if (chessBoard.getScore() > 0) {
			capturedPiecesImages[15].setText("White: +" + chessBoard.getScore());
		} else if (chessBoard.getScore() < 0) {
			capturedPiecesImages[15].setText("Black: +" + (-chessBoard.getScore()));
		}
	}


	/*
	private static Timer initializeMinimaxAiMoveTimer() {
		Timer timer = new Timer(true);

		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {

		    	updateMinimaxAiMoveElapsedSecs();

				chessBoardPanel.revalidate();
				chessBoardPanel.repaint();
		    }
		}, 0, 1000);

		return timer;
	}
	*/


	private static void undoLastMove() {
		if (!undoFenPositions.isEmpty()) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);

				int startingPositionRow = Utilities.getRowFromPosition(startingPosition, gameParameters.getNumOfRows());
				int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);

				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardSquares[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				GuiUtils.changeTileColor(startingButton, startingButtonColor);
			}

			redoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

			// Push to the redoCapturedPiecesImages Stack.
			JLabel[] tempCapturedPiecesImages = new JLabel[31];
			for (int i = 0; i <= 30; i++) {
				tempCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			redoCapturedPiecesImages.push(tempCapturedPiecesImages);

			chessBoard = FenUtils.getChessBoardFromFenPosition(undoFenPositions.pop());

			String halfMoveFenPosition = halfMoveFenPositions.pop();
			redoHalfMoveFenPositions.push(halfMoveFenPosition);
			// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());

			// Display the "undo" captured chess board pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = undoCapturedPiecesImages.pop();
			for (int i = 0; i < 31; i++) {
				capturedPiecesPanel.add(capturedPiecesImages[i]);
			}

			// This is true if any terminal state has occurred.
			// The terminal states are: "draw", "stalemate draw" & "checkmate"
			if (!buttonsEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				enableChessBoardButtons();
			}

			// Display the "undo" chess board.
			for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
				for (int j = 0; j < NUM_OF_COLUMNS; j++) {
					placePieceToPosition(
							Utilities.getPositionByRowCol(i, j, gameParameters.getNumOfRows()),
							chessBoard.getGameBoard()[i][j]
					);
				}
			}

			System.out.println();
			System.out.println(chessBoard);

			setTurnMessage();
			setScoreMessage();

			if (undoFenPositions.isEmpty()) {
				undoItem.setEnabled(false);
			}

			if (redoItem != null) {
				redoItem.setEnabled(true);
			}
		}
	}


	// NOTE: We are not able to perform a redo,
	// if we are in a terminal state, because the game has ended.
	private static void redoNextMove() {
		if (!redoFenPositions.isEmpty()) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);

				int startingPositionRow = Utilities.getRowFromPosition(startingPosition, gameParameters.getNumOfRows());
				int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);

				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardSquares[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				GuiUtils.changeTileColor(startingButton, startingButtonColor);
			}

			undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

			halfMoveFenPositions.push(redoHalfMoveFenPositions.pop());
			// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());

			// Push to the "undoCapturedPiecesImages" Stack.
			JLabel[] tempCapturedPiecesImages = new JLabel[31];
			for (int i = 0; i <= 30; i++) {
				tempCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			undoCapturedPiecesImages.push(tempCapturedPiecesImages);

			chessBoard = FenUtils.getChessBoardFromFenPosition(redoFenPositions.pop());

			// Display the "redo" captured chess board pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = redoCapturedPiecesImages.pop();
			for (int i = 0; i < 31; i++) {
				capturedPiecesPanel.add(capturedPiecesImages[i]);
			}

			if (!buttonsEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				enableChessBoardButtons();
			}

			if (redoFenPositions.isEmpty()) {
				redoItem.setEnabled(false);
			}

			// Display the "redo" chess board.
			for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
				for (int j = 0; j < NUM_OF_COLUMNS; j++) {
					placePieceToPosition(
							Utilities.getPositionByRowCol(i, j, gameParameters.getNumOfRows()),
							chessBoard.getGameBoard()[i][j]
					);
				}
			}

			System.out.println();
			System.out.println(chessBoard);

			setTurnMessage();
			setScoreMessage();

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}

			checkForGameOver();
		}
	}


	public static void exportToGif() {
		String gifName = JOptionPane.showInputDialog(
				frame,
				"Please type the exported \".gif\" file name:",
				"chess_board.gif"
		);

		if (gifName != null) {
			BufferedImage bi = new BufferedImage(
					gui.getSize().width,
					gui.getSize().height,
					BufferedImage.TYPE_INT_ARGB
			);
			Graphics g = bi.createGraphics();
			gui.paint(g);
			g.dispose();
			try {
				ImageIO.write(bi, "gif", new File(gifName));
				System.out.println("Exported .gif file!");
			} catch (Exception e) {
				System.err.println("Error exporting .gif file!");
				System.err.flush();
			}
		}
	}

	private static void initializeTurnTextPaneBar() {
		if (tools != null) {
			gui.remove(tools);
		}

		tools = new JToolBar();
		tools.setFloatable(false);

		turnTextPane.setEditable(false);
		centerTextPaneAndMakeBold();

		tools.add(turnTextPane);

		gui.add(tools, BorderLayout.NORTH);
	}

	private static void centerTextPaneAndMakeBold() {
		// Center textPane
		StyledDocument style = turnTextPane.getStyledDocument();
		SimpleAttributeSet align = new SimpleAttributeSet();
		StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
		style.setParagraphAttributes(0, style.getLength(), align, false);

		// Make textPane bold
		MutableAttributeSet attrs = turnTextPane.getInputAttributes();
		StyleConstants.setBold(attrs, true);
		turnTextPane.getStyledDocument().setCharacterAttributes(0, style.getLength(), attrs, false);
	}

	public static void initializeChessBoardPanel() {
		if (chessBoardPanel != null) {
			gui.remove(chessBoardPanel);
		}
		chessBoardPanel = new JPanel(new GridLayout(gameParameters.getNumOfRows() + 2, NUM_OF_COLUMNS + 2));
		chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
		chessBoardPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT - 100));
		gui.add(chessBoardPanel, BorderLayout.CENTER);
	}

	public static void initializeCapturedPiecesPanel() {
		if (capturedPiecesPanel != null) {
			gui.remove(capturedPiecesPanel);
		}
		capturedPiecesPanel = new JPanel();
		gui.add(capturedPiecesPanel, BorderLayout.SOUTH);
	}

	public static void initializeChessBoardSquareButtons() {
		chessBoardSquares = new JButton[gameParameters.getNumOfRows()][NUM_OF_COLUMNS];

		// Create the chess board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);

				// Our chess board pieces are 64x64 px in size, so we'll
				// "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
						GuiConstants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);

				Color color = getColorByRowCol(i, j);

				button.setBackground(color);

				// button.setBorderPainted(false);
				button.setFocusPainted(false);
				// button.setRolloverEnabled(true);
				// button.setOpaque(false);

				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = gameParameters.getNumOfRows() - 1 - i;
					column = Constants.NUM_OF_COLUMNS - 1 - j;
				} else {
					row = i;
					column = j;
				}

				button.addActionListener(e -> chessButtonClick(row, column, button));

				chessBoardSquares[i][j] = button;
			}
		}

		// fill the chess board panel
		chessBoardPanel.add(new JLabel(""));

		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65 + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65 + j)), SwingConstants.CENTER));
			}
		}

		chessBoardPanel.add(new JLabel(""));
		// fill the black non-pawn chessPiece row
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS + 1; j++) {
				switch (j) {
					case 0:
					case NUM_OF_COLUMNS:
						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
								&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							chessBoardPanel.add(new JLabel("" + (i + 1), SwingConstants.CENTER));
						} else {
							chessBoardPanel.add(
									new JLabel("" + (gameParameters.getNumOfRows() - i), SwingConstants.CENTER)
							);
						}
						break;
					default:
						chessBoardPanel.add(chessBoardSquares[i][j]);
				}
				if (j == 0) {
					chessBoardPanel.add(chessBoardSquares[i][j]);
				}
			}
		}

		// fill the bottom row
		chessBoardPanel.add(new JLabel(""));
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65 + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65 + j)), SwingConstants.CENTER));
			}
		}
	}

	public static void initializeCapturedPiecesImages() {
		capturedPiecesImages = new JLabel[31];

		// Create the captured chess board pieces icons.
		for (int i = 0; i < 31; i++) {
			capturedPiecesImages[i] = new JLabel();

			if (i == 15) {
				capturedPiecesImages[i].setText(ZERO_SCORE_TEXT);
			} else {
				// We'll "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(GuiConstants.CAPTURED_PIECE_PIXEL_SIZE,
						GuiConstants.CAPTURED_PIECE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				capturedPiecesImages[i].setIcon(icon);

				// This is for TESTING.
				// ImageIcon pieceImage = GuiUtils.preparePieceIcon(
				//	 GuiConstants.WHITE_PAWN_IMG_PATH,
				//	 GuiConstants.CAPTURED_PIECE_PIXEL_SIZE
				// );
				// capturedPiecesImages[i].setIcon(pieceImage);
			}

			capturedPiecesPanel.add(capturedPiecesImages[i]);
		}

	}

	public static void startNewGame() {
		startNewGame(Constants.DEFAULT_STARTING_FEN_POSITION);
	}

	public static void startNewGame(String fenPosition) {
		System.out.println("Starting new game!");

		gameParameters = new GameParameters(newGameParameters);

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}

		configureGuiStyle();

		chessBoardPanel.removeAll();

		initializeChessBoardPanel();
		initializeCapturedPiecesPanel();

		initializeChessBoardSquareButtons();
		initializeCapturedPiecesImages();

		// if (!buttonsEnabled) {
		// 	enableChessBoardButtons();
		// }

		restoreDefaultValues();
		if (!fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			placePiecesToChessBoard(fenPosition);
		}

		chessBoard.setThreats();

		halfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		redrawChessBoard();

		setTurnMessage();

		System.out.println();
		System.out.println(chessBoard);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			aiMove(ai);
		} else if (gameParameters.getGameMode() == GameMode.AI_VS_AI) {
			playAiVsAi();
		}
	}

	// Restores all the default values.
	public static void restoreDefaultValues() {
		chessBoard = new ChessBoard(gameParameters.getNumOfRows());
		/*
		halfMoveGameBoard = new ChessPiece[gameParameters.getNumOfRows()][NUM_OF_COLUMNS];

		for (int i=0; i<gameParameters.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				halfMoveGameBoard[i][j] = new EmptyTile();
			}
		}
 		*/

		startingPosition = "";
		endingPosition = "";

		undoFenPositions.clear();
		undoCapturedPiecesImages.clear();
		redoFenPositions.clear();
		redoCapturedPiecesImages.clear();

		halfMoveFenPositions.clear();
		redoHalfMoveFenPositions.clear();

		startingButtonIsClicked = false;

		hintPositions = new HashSet<>();

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}
		if (exportFenPositionItem != null) {
			exportFenPositionItem.setEnabled(true);
		}
		if (saveCheckpointItem != null) {
			saveCheckpointItem.setEnabled(true);
		}

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
			if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					if (gameParameters.getAi1MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.BLACK,
								gameParameters.getEvaluationFunction1());
					} else {
						ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.BLACK,
								gameParameters.getEvaluationFunction1());
					}
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					if (gameParameters.getAi2MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi2MaxDepth(), Constants.WHITE,
								gameParameters.getEvaluationFunction2());
					} else {
						ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi2MaxDepth(), Constants.WHITE,
								gameParameters.getEvaluationFunction2());
					}
				}
			} else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					ai = new RandomChoiceAI(Constants.BLACK);
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					ai = new RandomChoiceAI(Constants.WHITE);
				}
			}
		}

		isGameOver = false;

		gameResult = GameResult.NONE;

		setTurnMessage();

		// whiteMinimaxAiMoveAverageSecs = 0;
		// blackMinimaxAiMoveAverageSecs = 0;
	}

	// This method is only called from inside a chess board button listener.
	public static void chessButtonClick(int row, int column, JButton button) {
		// System.out.println("row: " + row + ", column: " + column);

		hideHintPositions(hintPositions);

		String position = Utilities.getPositionByRowCol(row, column, gameParameters.getNumOfRows());
		// System.out.println("position: " + position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];
		// System.out.println("chessPiece: " + chessPiece);

		int startingPositionRow = 0;
		int startingPositionColumn = 0;
		ChessPiece startingPiece = null;
		if (!startingPosition.equals("")) {
			startingPositionRow = Utilities.getRowFromPosition(startingPosition, gameParameters.getNumOfRows());
			startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
			startingPiece = chessBoard.getGameBoard()[startingPositionRow][startingPositionColumn];
		}

		if (!startingButtonIsClicked
				&& (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			startingPosition = position;
			// System.out.println("startingPosition: " + startingPosition);

			// System.out.println("chessBoard: ");
			// System.out.println(chessBoard);

			if (!(chessPiece instanceof EmptyTile)) {

				// Get the hint positions.
				if (chessBoard.whitePlays() && !chessBoard.isWhiteKingInCheck()
						|| chessBoard.blackPlays() && !chessBoard.isBlackKingInCheck()) {
					hintPositions = chessBoard.getNextPositions(position);

					// if (chessPiece instanceof King) {
					// 	System.out.println("hint positions: " + hintPositions);
					// }

					// System.out.println("chessBoard: ");
					// System.out.println(chessBoard);
				}
				// If the White or Black King is in check, then get one of the following valid moves.
				else if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck() || chessBoard.blackPlays()
						&& chessBoard.isBlackKingInCheck()) {
					hintPositions = new HashSet<>();

					if (chessBoard.whitePlays()
							&& chessBoard.getWhiteKingInCheckValidPieceMoves().containsKey(startingPosition)) {
						hintPositions = chessBoard.getWhiteKingInCheckValidPieceMoves().get(startingPosition);
					} else if (chessBoard.blackPlays()
							&& chessBoard.getBlackKingInCheckValidPieceMoves().containsKey(startingPosition)) {
						hintPositions = chessBoard.getBlackKingInCheckValidPieceMoves().get(startingPosition);
					}

				}

				GuiUtils.changeTileColor(button, Color.CYAN);

				// Display the hint positions.
				if (hintPositions != null && hintPositions.size() != 0) {
					// System.out.println("hintPositions: " + hintPositions);
					for (String hintPosition : hintPositions) {
						// System.out.println("hintPosition: " + hintPosition);

						int hintPositionRow = Utilities.getRowFromPosition(hintPosition, gameParameters.getNumOfRows());
						int hintPositionColumn = Utilities.getColumnFromPosition(hintPosition);

						JButton hintPositionButton;

						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
								&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							hintPositionButton = chessBoardSquares
									[gameParameters.getNumOfRows() - 1 - hintPositionRow]
									[NUM_OF_COLUMNS - 1 - hintPositionColumn];
						} else {
							hintPositionButton = chessBoardSquares[hintPositionRow][hintPositionColumn];
						}

						// System.out.println("startingPiece: " + startingPiece);
						// System.out.println("hint position: " + hintPosition);

						ChessPiece hintPositionPiece = chessBoard.getGameBoard()[hintPositionRow][hintPositionColumn];

						if (chessPiece.getAllegiance() != hintPositionPiece.getAllegiance()
								&& hintPositionPiece.getAllegiance() != Allegiance.EMPTY
								|| chessBoard.getEnPassantPosition().equals(hintPosition)
								&& chessPiece instanceof Pawn) {
							GuiUtils.changeTileColor(hintPositionButton, Color.RED);
						} else if (chessPiece instanceof Pawn &&
								(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == 0
										|| chessPiece.getAllegiance() == Allegiance.BLACK
										&& hintPositionRow == gameParameters.getNumOfRows() - 1)) {
							GuiUtils.changeTileColor(hintPositionButton, Color.GREEN);
						} else if (hintPositionPiece instanceof EmptyTile) {
							GuiUtils.changeTileColor(hintPositionButton, Color.BLUE);
						}

					}
				}

				// System.out.println("chessBoard: ");
				// System.out.println(chessBoard);

				startingButtonIsClicked = true;
			}

		} else if (startingButtonIsClicked && startingPiece != null
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| startingPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			startingButtonIsClicked = false;

			endingPosition = position;
			// System.out.println("endingPosition: " + endingPosition);

			if (!hintPositions.contains(endingPosition)) {
				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardSquares[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				// System.out.println("startingButtonColor: " + startingButtonColor);
				GuiUtils.changeTileColor(startingButton, startingButtonColor);

				startingButtonIsClicked = false;
				return;
			} else {
				undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

				// Push to the "undoCapturedPiecesImages" Stack.
				JLabel[] tempCapturedPiecesImages = new JLabel[31];
				for (int i = 0; i <= 30; i++) {
					tempCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
				}
				undoCapturedPiecesImages.push(tempCapturedPiecesImages);

				redoFenPositions.clear();
				redoCapturedPiecesImages.clear();

				// System.out.println("startingPositionGameBoard: ");
				// ChessBoard.printChessBoard(startingPositionChessBoard.getGameBoard());

				// chessBoard.movePieceFromAPositionToAnother(startingPosition, endingPosition, true);

				Move move = new Move(startingPosition, endingPosition);
				makeDisplayMove(move, false);
				// System.out.println("evaluation: " + chessBoard.evaluate(gameParameters.getEvaluationFunction1()));

				hideHintPositions(hintPositions);

				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardSquares[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				// System.out.println("startingButtonColor: " + startingButtonColor);
				GuiUtils.changeTileColor(startingButton, startingButtonColor);
			}

			if (checkForGameOver()) return;

			if (gameParameters.isEnableSounds()) {
				SoundUtils.playSound(PIECE_MOVE_SOUND);
			}

			// Remove the check from the king of the player who made the last move.
			// The thing that the player managed to make a move,
			// means that his king has escaped from the check.
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				if (chessBoard.whitePlays()) {
					chessBoard.setWhiteKingInCheck(false);
				} else {
					chessBoard.setBlackKingInCheck(false);
				}
			}

			if (hintPositions.contains(endingPosition)) {
				System.out.println();
				System.out.println(chessBoard);

				// System.out.println("en passant position: " + chessBoard.getEnPassantPosition());

				if (undoItem != null) {
					undoItem.setEnabled(true);
				}
				if (redoItem != null) {
					redoItem.setEnabled(false);
				}

				// Change chessBoard turn.
				chessBoard.setHalfMoveNumber(chessBoard.getHalfMoveNumber() + 1);
				chessBoard.setPlayer(chessBoard.getNextPlayer());
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
					setTurnMessage();
				} else if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
					aiMove(ai);
				}
			}
		}
	}

	private static void makeDisplayMove(Move move, boolean isAiMove) {
		String positionStart = move.getPositions().get(0);
		int rowStart = Utilities.getRowFromPosition(positionStart, gameParameters.getNumOfRows());
		int columnStart = Utilities.getColumnFromPosition(positionStart);
		ChessPiece startingPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		String positionEnd = move.getPositions().get(1);
		int rowEnd = Utilities.getRowFromPosition(positionEnd, gameParameters.getNumOfRows());
		int columnEnd = Utilities.getColumnFromPosition(positionEnd);
		ChessPiece endTile = chessBoard.getGameBoard()[rowEnd][columnEnd];

		chessBoard.makeMove(move, true);

		// Pawn promotion implementation.
		// If AI plays, automatically choose the best promotion piece, based on the best outcome.
		if (startingPiece instanceof Pawn
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && rowEnd == 0
				|| startingPiece.getAllegiance() == Allegiance.BLACK && rowEnd == gameParameters.getNumOfRows() - 1)) {
			if (isAiMove) {
				chessBoard.automaticPawnPromotion(startingPiece, positionEnd, true);

				ChessPiece promotedPiece = chessBoard.getGameBoard()[rowEnd][columnEnd];
				if (promotedPiece.getAllegiance() == Allegiance.WHITE) {
					JOptionPane.showMessageDialog(
							null,
							"Promoting White Pawn to " + promotedPiece + "!",
							"White Pawn Promotion",
							JOptionPane.INFORMATION_MESSAGE
					);
				} else if (promotedPiece.getAllegiance() == Allegiance.BLACK) {
					JOptionPane.showMessageDialog(
							null,
							"Promoting Black Pawn to " + promotedPiece + "!",
							"Black Pawn Promotion",
							JOptionPane.INFORMATION_MESSAGE
					);
				}
			}
			// If human player plays, select which promotion piece you want and display it on the GUI.
			else {
				String[] promotionPieces = {"Queen", "Rook", "Bishop", "Knight"};
				String initialSelection = "Queen";

				String value = null;
				if (startingPiece.getAllegiance() == Allegiance.WHITE) {
					value = (String) JOptionPane.showInputDialog(
							ChessGUI.gui,
							"Promote White Pawn to:",
							"White Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				} else if (startingPiece.getAllegiance() == Allegiance.BLACK) {
					value = (String) JOptionPane.showInputDialog(
							ChessGUI.gui,
							"Promote Black Pawn to:",
							"Black Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				}
				// System.out.println("value: " + value);

				ChessPiece queen = new Queen(startingPiece.getAllegiance());
				ChessPiece rook = new Rook(startingPiece.getAllegiance());
				ChessPiece bishop = new Bishop(startingPiece.getAllegiance());
				ChessPiece knight = new Knight(startingPiece.getAllegiance());

				if (value == null || value.equals("Queen")) {
					chessBoard.getPiecesToPlace().put(positionEnd, queen);
					chessBoard.getPromotedPieces().add(queen);
				} else if (value.equals("Rook")) {
					chessBoard.getPiecesToPlace().put(positionEnd, rook);
					chessBoard.getPromotedPieces().add(rook);
				} else if (value.equals("Bishop")) {
					chessBoard.getPiecesToPlace().put(positionEnd, bishop);
					chessBoard.getPromotedPieces().add(bishop);
				} else if (value.equals("Knight")) {
					chessBoard.getPiecesToPlace().put(positionEnd, knight);
					chessBoard.getPromotedPieces().add(knight);
				}

			}
		}

		// If a chessPiece capture has occurred.
		if (chessBoard.getCapturedPiece() != null) {  // true if an en passant captured piece exists
			addCapturedPieceImage(chessBoard.getCapturedPiece());
		} else if (startingPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)) {
			chessBoard.setCapturedPiece(endTile);
			addCapturedPieceImage(chessBoard.getCapturedPiece());
		}

		for (String position : chessBoard.getPositionsToRemove()) {
			removePieceFromPosition(position);
		}
		for (String position : chessBoard.getPiecesToPlace().keySet()) {
			ChessPiece chessPieceToPlace = chessBoard.getPiecesToPlace().get(position);
			placePieceToPosition(position, chessPieceToPlace);
		}
		chessBoard.getPositionsToRemove().clear();
		chessBoard.getPiecesToPlace().clear();

		chessBoard.setThreats();

		// Store the chess board of the HalfMove that was just made.
		halfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
		// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());
	}

	public static void addCapturedPieceImage(ChessPiece endTile) {
		String imagePath = "";

		if (chessBoard.getPromotedPieces().contains(endTile)) {
			if (endTile.getAllegiance() == Allegiance.WHITE) {
				imagePath = GuiConstants.WHITE_PAWN_IMG_PATH;
			} else if (endTile.getAllegiance() == Allegiance.BLACK) {
				imagePath = GuiConstants.BLACK_PAWN_IMG_PATH;
			}
		} else {
			imagePath = GuiUtils.getImagePath(endTile);
		}

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CAPTURED_PIECE_PIXEL_SIZE);

		if (endTile.getAllegiance() == Allegiance.WHITE) {
			capturedPiecesImages[chessBoard.getWhiteCapturedPiecesCounter()].setIcon(pieceImage);
		} else if (endTile.getAllegiance() == Allegiance.BLACK) {
			int index = (int) Math.ceil((capturedPiecesImages.length) / 2.0)
					+ chessBoard.getBlackCapturedPiecesCounter();
			capturedPiecesImages[index].setIcon(pieceImage);
		}

		chessBoard.incrementCapturedPiecesCounter(endTile);

		setScoreMessage();

		chessBoard.setCapturedPiece(null);
	}

	public static boolean checkForGameOver() {

		/* Check for White checkmate. */
		if (chessBoard.whitePlays()) {
			chessBoard.checkForWhiteCheckmate(true);
			if (chessBoard.isWhiteCheckmate()) {
				gameResult = GameResult.WHITE_CHECKMATE;

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! White wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playSound(CHECKMATE_SOUND);
				}

				int dialogResult = JOptionPane.showConfirmDialog(gui,
						"White wins! Start a new game?", "Checkmate", JOptionPane.YES_NO_OPTION);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		/* Check for Black checkmate. */
		else {
			chessBoard.checkForBlackCheckmate(true);
			if (chessBoard.isBlackCheckmate()) {
				gameResult = GameResult.BLACK_CHECKMATE;

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! Black wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playSound(CHECKMATE_SOUND);
				}

				int dialogResult = JOptionPane.showConfirmDialog(gui,
						"Black wins! Start a new game?", "Checkmate", JOptionPane.YES_NO_OPTION);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}


		/* Stalemate draw implementation. */

		// Check for White stalemate.
		if (chessBoard.blackPlays() && !chessBoard.isWhiteKingInCheck()) {
			// System.out.println("Checking for white stalemate!");
			chessBoard.checkForWhiteStalemateDraw();
			if (chessBoard.isWhiteStalemateDraw()) {
				gameResult = GameResult.WHITE_STALEMATE_DRAW;

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for White exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(gui,
						"Stalemate! No legal moves for White exist. Start a new game?",
						"Draw", JOptionPane.YES_NO_OPTION);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		// Check for Black stalemate.
		else if (chessBoard.whitePlays() && !chessBoard.isBlackKingInCheck()) {
			// System.out.println("Checking for black stalemate!");
			chessBoard.checkForBlackStalemateDraw();
			if (chessBoard.isBlackStalemateDraw()) {
				gameResult = GameResult.BLACK_STALEMATE_DRAW;

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for Black exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(gui,
						"Stalemate! No legal moves for Black exist. Start a new game?",
						"Draw", JOptionPane.YES_NO_OPTION);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}


		/* Insufficient checkmate material draw implementation. */
		chessBoard.checkForInsufficientMaterialDraw();
		if (chessBoard.isInsufficientMaterialDraw()) {
			gameResult = GameResult.INSUFFICIENT_MATERIAL_DRAW;

			String turnMessage = "Move number: "
					+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
					+ ". It is a draw.";
			turnTextPane.setText(turnMessage);

			int dialogResult = JOptionPane.showConfirmDialog(
					gui,
					"It is a draw due to insufficient mating material! Start a new game?",
					"Draw",
					JOptionPane.YES_NO_OPTION
			);

			// System.out.println("dialogResult:" + dialogResult);

			startNewGameOrNot(dialogResult);

			return true;
		}


		// 50 full moves without a chessPiece capture Draw implementation.
		if (chessBoard.checkForNoPieceCaptureDraw()) {
			int dialogResult = -1;

			if (!chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| !chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI) {
				dialogResult = JOptionPane.showConfirmDialog(gui,
						Constants.NO_CAPTURE_DRAW_MOVES_LIMIT +
								" full moves have passed without a piece capture! Do you want to declare a draw?",
						"Draw", JOptionPane.YES_NO_OPTION);
			}

			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				gameResult = GameResult.NO_CAPTURE_DRAW;
				showDeclareDrawDialog();
				return true;
			}

		}


		// Three-fold repetition draw rule implementation.
		// This situation occurs when we end up with the same chess board position 3 different times
		// at any time in the game, not necessarily successively.
		if (checkForThreefoldRepetitionDraw()) {
			int dialogResult = -1;

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI &&
					(!chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| !chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK)
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI) {
				dialogResult = JOptionPane.showConfirmDialog(
						gui,
						"Threefold repetition of the same chess board position has occurred! "
								+ "Do you want to declare a draw?", "Draw", JOptionPane.YES_NO_OPTION
				);
			}

			// System.out.println("dialogResult:" + dialogResult);
			if (JOptionPane.YES_OPTION == dialogResult) {
				gameResult = GameResult.THREEFOLD_REPETITION_DRAW;
				showDeclareDrawDialog();
				return true;
			}

		}

		return false;
	}

	public static boolean checkForThreefoldRepetitionDraw() {

		if (!halfMoveFenPositions.isEmpty()) {
			int N = halfMoveFenPositions.size();
			ChessPiece[][] lastHalfMoveGameBoard = FenUtils.getChessBoardFromFenPosition(halfMoveFenPositions.get(N-1))
														   .getGameBoard();
			int numOfRepeats = 0;
			for (int i = N-2; i >= 0; i--) {
				// Skip the last iteration, if the number of repeats found is less ore equal to 1.
				// Also, skip the second to last iteration, if the number of repeats found is 0.
				if (!(numOfRepeats <= 1 && i == 0 || numOfRepeats == 0 && i == 1)) {
					// System.out.println("i: " + i);
					ChessPiece[][] otherHalfMoveGameBoard = FenUtils.getChessBoardFromFenPosition(
							halfMoveFenPositions.get(i)).getGameBoard();
					if (Utilities.checkEqualGameBoards(lastHalfMoveGameBoard, otherHalfMoveGameBoard)) {
						// System.out.println("i: " + i + ");
						// ChessBoard.printChessBoard(lastHalfMoveGameBoard);
						// ChessBoard.printChessBoard(otherHalfMoveGameBoard);
						numOfRepeats++;
						if (numOfRepeats == 3) {
							break;
						}
					}
				}
			}
			// System.out.println("numOfRepeats: " + numOfRepeats);
			return numOfRepeats == 3;
		}

		return false;
	}

	private static void showDeclareDrawDialog() {
		String turnMessage = "Move number: "
				+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
				+ ". It is a draw.";
		turnTextPane.setText(turnMessage);

		int dialogResult = JOptionPane.showConfirmDialog(gui,
				"It is a draw! Start a new game?",
				"Draw", JOptionPane.YES_NO_OPTION);

		startNewGameOrNot(dialogResult);
	}

	private static void startNewGameOrNot(int dialogResult) {
		// calculateAverageMinimaxAiMoveSecs();

		if (dialogResult == JOptionPane.YES_OPTION) {
			startNewGame();
		} else {
			if (undoItem != null) {
				undoItem.setEnabled(true);
			}
			if (redoItem != null) {
				redoItem.setEnabled(false);
			}
			if (exportFenPositionItem != null) {
				exportFenPositionItem.setEnabled(false);
			}
			if (saveCheckpointItem != null) {
				saveCheckpointItem.setEnabled(false);
			}
			disableChessBoardSquares();
		}
	}


	/*
	private static void calculateAverageMinimaxAiMoveSecs() {
		if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
			|| gameParameters.getGameMode() == GameMode.AI_VS_AI)
				&& gameParameters.getAiType() == AiType.MINIMAX_AI) {

			whiteMinimaxAiMoveAverageSecs = whiteMinimaxAiMoveAverageSecs
					/ Math.ceil((double) chessBoard.getHalfMoveNumber() / 2.0);
			blackMinimaxAiMoveAverageSecs = blackMinimaxAiMoveAverageSecs
					/ Math.floor((double) chessBoard.getHalfMoveNumber() / 2.0);

			if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI)) {
				System.out.println("Black Minimax AI move Average seconds: " + blackMinimaxAiMoveAverageSecs);
			}
			if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI)) {
				System.out.println("White Minimax AI move Average seconds: " + whiteMinimaxAiMoveAverageSecs);
			}
		}
	}
	*/

	// Gets called after the human player makes a move. It makes a Minimax AI move.
	public static void aiMove(AI ai) {
	    /*
		if (chessBoard.whitePlays()) {
			whiteMinimaxAiMoveElapsedSecs = -1;
		} else if (chessBoard.blackPlays()) {
			blackMinimaxAiMoveElapsedSecs = -1;
		}
		Timer timer = initializeMinimaxAiMoveTimer();
		setTurnMessage();
		chessBoardPanel.revalidate();
		chessBoardPanel.repaint();
	    */

		Move aiMove = ai.getNextMove(chessBoard);
		System.out.println("aiMove: " + aiMove);
		// System.out.println("lastCapturedPieceValue: " + chessBoard.getLastCapturedPieceValue());

		makeDisplayMove(aiMove, true);
		// System.out.println("board value after aiMove -> " + chessBoard.evaluate());

        /*
        timer.cancel();
		if (chessBoard.whitePlays())
			whiteMinimaxAiMoveAverageSecs += whiteMinimaxAiMoveElapsedSecs;
		else if (chessBoard.blackPlays())
			blackMinimaxAiMoveAverageSecs += blackMinimaxAiMoveElapsedSecs;
        */

		isGameOver = checkForGameOver();
		if (isGameOver) return;

		// Remove the check from the king of the player who made the last move.
		// The thing that the player managed to make a move,
		// means that his king has escaped from the check.
		if (chessBoard.whitePlays()) {
			chessBoard.setWhiteKingInCheck(false);
		} else if (chessBoard.blackPlays()) {
			chessBoard.setBlackKingInCheck(false);
		}

		chessBoard.setHalfMoveNumber(chessBoard.getHalfMoveNumber() + 1);
		chessBoard.setPlayer(chessBoard.getNextPlayer());

		setTurnMessage();
		setScoreMessage();

		System.out.println();
		System.out.println(chessBoard);
	}

	public static void playAiVsAi() {
		AI ai1;
		AI ai2;
		if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
			if (gameParameters.getAi1MaxDepth() <= 2) {
				ai1 = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.WHITE,
						gameParameters.getEvaluationFunction1());
			} else {
				ai1 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.WHITE,
						gameParameters.getEvaluationFunction1());
			}
		} else {
			ai1 = new RandomChoiceAI(Constants.WHITE);
		}
		if (gameParameters.getAi2Type() == AiType.MINIMAX_AI) {
			ai2 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi2MaxDepth(), Constants.BLACK,
					gameParameters.getEvaluationFunction2());
		} else {
			ai2 = new RandomChoiceAI(Constants.BLACK);
		}

		while (!isGameOver) {
			System.out.println(turnTextPane.getText());
			aiVsAiMove(ai1);

			if (!isGameOver) {
				System.out.println(turnTextPane.getText());

				try {
					if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
						Thread.sleep(Constants.MINIMAX_AI_MOVE_MILLISECONDS);
					} else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
						Thread.sleep(Constants.RANDOM_AI_MOVE_MILLISECONDS);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				aiVsAiMove(ai2);
			}

			if (!isGameOver) {
				try {
					if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
						Thread.sleep(Constants.MINIMAX_AI_MOVE_MILLISECONDS);
					} else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
						Thread.sleep(Constants.RANDOM_AI_MOVE_MILLISECONDS);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		if (undoItem != null) {
			undoItem.setEnabled(true);
		}
	}

	private static void aiVsAiMove(AI ai) {
		undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		// Push to the "undoCapturedPiecesImages" Stack.
		JLabel[] tempCapturedPiecesImages = new JLabel[31];
		for (int i = 0; i <= 30; i++) {
			tempCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
		}
		undoCapturedPiecesImages.push(tempCapturedPiecesImages);

		// System.out.println("white plays: " + chessBoard.whitePlays());
		aiMove(ai);
		halfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		setTurnMessage();
		setScoreMessage();

		frame.revalidate();
		frame.repaint();
		frame.paint(frame.getGraphics());
	}

	public static void hideHintPositions(Set<String> positionsToHide) {
		if (positionsToHide != null && positionsToHide.size() != 0) {
			for (String hintPosition : positionsToHide) {
				// System.out.println("hide hint position: " + hintPosition);
				int row = Utilities.getRowFromPosition(hintPosition, gameParameters.getNumOfRows());
				int column = Utilities.getColumnFromPosition(hintPosition);
				// System.out.println("hide hint row: " + row + ", hide hint column: " + column);

				JButton button;
				Color buttonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					button = chessBoardSquares[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column];
					buttonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - row,
							NUM_OF_COLUMNS - 1 - column
					);
				} else {
					button = chessBoardSquares[row][column];
					buttonColor = getColorByRowCol(row, column);
				}

				GuiUtils.changeTileColor(button, buttonColor);
			}
		}
	}

	public static Color getColorByRowCol(int row, int column) {
		Color color;
		if ((column % 2 == 1 && row % 2 == 1)
				//) {
				|| (column % 2 == 0 && row % 2 == 0)) {
			color = gameParameters.getWhiteTileColor();
		} else {
			color = gameParameters.getBlackTileColor();
		}
		return color;
	}

	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI)
	public static void placePieceToPosition(String position, ChessPiece chessPiece) {
		String imagePath = GuiUtils.getImagePath(chessPiece);

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_SQUARE_PIXEL_SIZE);

		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));

		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position, gameParameters.getNumOfRows());

		// System.out.println("chessBoardSquares.length: " + chessBoardSquares.length);
		// System.out.println("chessBoardSquares[0].length: " + chessBoardSquares[0].length);

		chessBoard.getGameBoard()[row][column] = chessPiece;

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardSquares[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column].setIcon(pieceImage);
		} else {
			chessBoardSquares[row][column].setIcon(pieceImage);
		}
	}

	// It removes the given chessPiece from the board (both the data structure and the GUI).
	public static void removePieceFromPosition(String position) {
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));

		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position, gameParameters.getNumOfRows());

		// Our chess board pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon.
		ImageIcon icon = new ImageIcon(new BufferedImage(GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
				GuiConstants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));

		chessBoard.getGameBoard()[row][column] = new EmptyTile();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardSquares[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column].setIcon(icon);
		} else {
			chessBoardSquares[row][column].setIcon(icon);
		}
	}

	public static void redrawChessBoard() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = Utilities.getPositionByRowCol(i, j, gameParameters.getNumOfRows());
				placePieceToPosition(position, chessPiece);
			}
		}
	}

	public static void placePiecesToStartingPositions() {
		chessBoard.placePiecesToStartingPositions();
		redrawChessBoard();

		chessBoard.setThreats();

		halfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		setTurnMessage();
	}

	public static void placePiecesToChessBoard(String fenPosition) {
		try {
			chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);
		} catch (InvalidFenFormatException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < Constants.NUM_OF_COLUMNS; j++) {
				String piecePosition = Utilities.getPositionByRowCol(i, j, gameParameters.getNumOfRows());
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();

		setTurnMessage();

		halfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
	}

	public static void makeChessBoardSquaresEmpty() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(true);

				// Our chess board pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon.
				ImageIcon icon = new ImageIcon(new BufferedImage(GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
						GuiConstants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				chessBoardSquares[i][j].setIcon(icon);

				Color color = getColorByRowCol(i, j);
				chessBoardSquares[i][j].setBackground(color);
				chessBoardSquares[i][j].setOpaque(true);
				// chessBoardSquares[i][j].setBorderPainted(false);

				chessBoard.getGameBoard()[i][j] = new EmptyTile();
				chessBoard.setThreats();
			}
		}
	}

	public static void enableChessBoardButtons() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				JButton button = chessBoardSquares[i][j];
				button.setEnabled(true);

				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = gameParameters.getNumOfRows() - 1 - i;
					column = NUM_OF_COLUMNS - 1 - j;
				} else {
					row = i;
					column = j;
				}

				button.addActionListener(e -> chessButtonClick(row, column, button));
			}
		}
		buttonsEnabled = true;
	}

	public static void disableChessBoardSquares() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(false);

				chessBoardSquares[i][j].removeActionListener(chessBoardSquares[i][j].getActionListeners()[0]);
			}
		}
		buttonsEnabled = false;
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(TITLE);
		placePiecesToStartingPositions();

		System.out.println(chessBoard);
	}

	public final void initializeGui() {
		// Set up the main GUI.
		// gui.setBorder(new EmptyBorder(0,0,0,0));
		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));

		initializeTurnTextPaneBar();

		initializeChessBoardPanel();
		initializeChessBoardSquareButtons();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();
	}

}
