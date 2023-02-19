package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.ai.AI;
import com.chriskormaris.mychessgame.api.ai.MinimaxAI;
import com.chriskormaris.mychessgame.api.ai.MinimaxAlphaBetaPruningAI;
import com.chriskormaris.mychessgame.api.ai.RandomChoiceAI;
import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.enumeration.GameResult;
import com.chriskormaris.mychessgame.api.evaluation.Evaluation;
import com.chriskormaris.mychessgame.api.evaluation.PeSTOEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.ShannonEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.SimplifiedEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.WukongEvaluation;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptySquare;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import static javax.swing.JOptionPane.QUESTION_MESSAGE;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GUI {

	private static int width;
	private static int height;

	private static JPanel gui;
	private static JTextPane turnTextPane;

	// These stacks of "String" objects are used to handle the "undo" and "redo" functionality.
	private static Stack<String> undoFenPositions;
	private static Stack<String> redoFenPositions;

	// The length of this array is 30 elements.
	// The first 15 elements represent White captured pieces and are capital chars.
	// The last 15 elements represent Black captured pieces and are lowercase chars.
	// The elements could also be '-', which is a placeholder for future captured pieces.
	private static char[] capturedPieces;

	// These stacks of "char" arrays are used to handle the "undo" and "redo" functionality.
	private static Stack<char[]> undoCapturedPieces;
	private static Stack<char[]> redoCapturedPieces;

	public static GameParameters gameParameters;
	public static GameParameters newGameParameters;

	public static JFrame frame;

	// The position (0, 0) of the "chessBoard.getGameBoard()" is the upper left button
	// of the JButton array "chessBoardButtons".
	// The position (gameParameters.getNumOfRows()-1, 0) of the "chessBoard.getGameBoard()" is the lower left button
	// of the JButton array "chessBoardButtons".
	public static ChessBoard chessBoard;

	// This variable is used for the implementation of "Human Vs AI".
	public static AI ai;

	// These stacks of "String" objects are used to check for a threefold repetition of a chess board position.
	public static Stack<String> undoHalfMoveFenPositions;
	public static Stack<String> redoHalfMoveFenPositions;

	private static JToolBar tools;
	private static JPanel chessBoardPanel;
	private static JPanel capturedPiecesPanel;

	// The position (0, 0) of the chessBoardButtons,
	// corresponds to the position (NUM_OF_COLUMNS - 1, 0) of the ChessBoard's gameBoard.
	private static JButton[][] chessBoardButtons;

	// 30 captured pieces at maximum, plus 1 label for displaying the score = 31 labels size.
	private static JLabel[] capturedPiecesImages;

	private static String startingPosition;
	private static String endingPosition;

	private static boolean startingButtonIsClicked;

	private static Set<String> hintPositions;

	private static boolean buttonsEnabled;

	// This variable is used for the implementation of "AI Vs AI".
	private static boolean isGameOver;

	private static String savedFenPosition;

	private static JMenuItem undoItem;
	private static JMenuItem redoItem;
	private static JMenuItem exportFenPositionItem;
	private static JMenuItem saveCheckpointItem;
	private static JMenuItem loadCheckpointItem;

	public static void create(String title) {
		width = GuiConstants.DEFAULT_WIDTH;
		height = GuiConstants.DEFAULT_HEIGHT;

		gui = new JPanel();
		turnTextPane = new JTextPane();

		undoFenPositions = new Stack<>();
		redoFenPositions = new Stack<>();

		initializeCapturedPieces();

		undoCapturedPieces = new Stack<>();
		redoCapturedPieces = new Stack<>();

		gameParameters = new GameParameters();
		newGameParameters = new GameParameters(gameParameters);

		chessBoard = new ChessBoard();

		undoHalfMoveFenPositions = new Stack<>();
		redoHalfMoveFenPositions = new Stack<>();

		tools = new JToolBar();

		startingPosition = "";
		endingPosition = "";

		hintPositions = new HashSet<>();
		buttonsEnabled = true;

		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);

		configureGuiStyle();
		initializeGUI();
		redrawChessBoardButtons();
		initializeAI();

		frame = new JFrame(title);
		frame.add(gui);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);

		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		frame.pack();

		frame.setSize(new Dimension(width, height));

		// ensures the minimum size is enforced.
		frame.setMinimumSize(frame.getSize());

		frame.setLocation((int) (GuiConstants.SCREEN_SIZE.getWidth() - frame.getWidth()) / 2, 5);

		frame.setResizable(false);

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			frame.setIconImage(icon);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGameItem = new JMenuItem("New Game");
		undoItem = new JMenuItem("Undo    Ctrl+Z");
		redoItem = new JMenuItem("Redo    Ctrl+Y");
		JMenuItem exportToGifItem = new JMenuItem("Export to .gif");
		JMenuItem settingsItem = new JMenuItem("Settings");
		JMenuItem importStartingFenPositionItem = new JMenuItem("Import starting FEN position");
		exportFenPositionItem = new JMenuItem("Export FEN position to file");
		saveCheckpointItem = new JMenuItem("Save Checkpoint");
		loadCheckpointItem = new JMenuItem("Load Checkpoint");
		JMenuItem exitItem = new JMenuItem("Exit");

		JMenu helpMenu = new JMenu("Help");
		JMenuItem howToPlayItem = new JMenuItem("How to Play");
		JMenuItem aboutItem = new JMenuItem("About");

		undoItem.setEnabled(false);
		redoItem.setEnabled(false);

		loadCheckpointItem.setEnabled(false);

		newGameItem.addActionListener(e -> startNewGame());

		undoItem.addActionListener(e -> {
			undo();
			exportFenPositionItem.setEnabled(true);
			saveCheckpointItem.setEnabled(true);
		});

		redoItem.addActionListener(e -> redo());

		exportToGifItem.addActionListener(e -> exportToGif());

		settingsItem.addActionListener(e -> {
			SettingsWindow settings = new SettingsWindow(frame);
			settings.setVisible(true);
		});

		importStartingFenPositionItem.addActionListener(e -> {
			String fenPosition = (String) JOptionPane.showInputDialog(
					frame,
					"Please insert the starting \"FEN\" position in the text field below:"
							+ "                      ",
					"Import starting FEN position",
					QUESTION_MESSAGE,
					null,
					null,
					Constants.DEFAULT_STARTING_FEN_POSITION
			);

			if (fenPosition != null) {
				// gameParameters.getNumOfRows() = Constants.DEFAULT_NUM_OF_ROWS;
				startNewGame(fenPosition);
			}
		});

		exportFenPositionItem.addActionListener(e -> {
			String exportedFenPositionFilename = (String) JOptionPane.showInputDialog(
					frame,
					"Please type the name of the export file:",
					"Export FEN position",
					QUESTION_MESSAGE,
					null,
					null,
					"exported_FEN_position.txt"
			);

			if (exportedFenPositionFilename != null) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(exportedFenPositionFilename))) {
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

		howToPlayItem.addActionListener(
				e -> JOptionPane.showMessageDialog(
						frame,
						GuiConstants.RULES,
						"How to Play",
						JOptionPane.INFORMATION_MESSAGE
				)
		);

		aboutItem.addActionListener(e -> {
			JLabel label = new JLabel(
					"<html>A traditional chess game implementation using Minimax AI,<br>"
							+ "with Alpha-Beta Pruning.<br>© Created by: Christos Kormaris, Athens 2020<br>"
							+ "Version " + GuiConstants.VERSION + "</html>"
			);

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
			ImageIcon imageIcon = new ImageIcon(dImg);

			JOptionPane.showMessageDialog(frame, label, "About", JOptionPane.PLAIN_MESSAGE, imageIcon);
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
		frame.addKeyListener(undoRedoKeyListener);
	}

	private static final KeyListener undoRedoKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			// System.out.println("keyTyped = " + KeyEvent.getKeyText(e.getKeyCode()));
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// System.out.println("keyPressed = " + KeyEvent.getKeyText(e.getKeyCode()));
			if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_Z) {
				undo();
			} else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_Y) {
				redo();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// System.out.println("keyReleased = " + KeyEvent.getKeyText(e.getKeyCode()));
		}
	};

	private static void configureGuiStyle() {
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
		} catch (Exception ex1) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}
	}


	private static void initializeGUI() {
		// Set up the main GUI.
		// gui.setBorder(new EmptyBorder(0,0,0,0));
		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));

		initializeTurnTextPaneBar();

		initializeChessBoardPanel();
		initializeChessBoardButtons();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();
	}


	private static void setTurnMessage() {
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


	private static void setScoreMessage() {
		if (chessBoard.getScore() > 0) {
			capturedPiecesImages[15].setText("White: +" + chessBoard.getScore());
		} else if (chessBoard.getScore() < 0) {
			capturedPiecesImages[15].setText("Black: +" + (-chessBoard.getScore()));
		} else {
			capturedPiecesImages[15].setText(ZERO_SCORE_TEXT);
		}
	}


	private static void undo() {
		if (!undoFenPositions.isEmpty()) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);

				int startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
				int startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);

				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardButtons
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardButtons[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				GuiUtils.changeSquareColor(startingButton, startingButtonColor);
			}

			redoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

			redoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

			String fenPosition = undoFenPositions.pop();
			chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);

			redoHalfMoveFenPositions.push(undoHalfMoveFenPositions.pop());

			capturedPieces = undoCapturedPieces.pop();

			updateCapturedPiecesPanel();

			setTurnMessage();

			// This is true if any terminal state has occurred.
			// The terminal states are: "draw", "stalemate draw" & "checkmate"
			if (!buttonsEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				enableChessBoardButtons();
			}

			placePiecesToChessBoard(fenPosition);

			System.out.println();
			System.out.println(chessBoard);

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
	private static void redo() {
		if (!redoFenPositions.isEmpty()) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);

				int startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
				int startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);

				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardButtons
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardButtons[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				GuiUtils.changeSquareColor(startingButton, startingButtonColor);
			}

			undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

			undoHalfMoveFenPositions.push(redoHalfMoveFenPositions.pop());

			undoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

			String fenPosition = redoFenPositions.pop();
			chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);

			capturedPieces = redoCapturedPieces.pop();

			updateCapturedPiecesPanel();

			setTurnMessage();

			for (int i = 0; i < 31; i++) {
				capturedPiecesPanel.add(capturedPiecesImages[i]);
			}

			if (!buttonsEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				enableChessBoardButtons();
			}

			if (redoFenPositions.isEmpty()) {
				redoItem.setEnabled(false);
			}

			placePiecesToChessBoard(fenPosition);

			System.out.println();
			System.out.println(chessBoard);

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}

			checkForGameOver();
		}
	}


	private static void updateCapturedPiecesPanel() {
		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();
		chessBoard.setScore(0);
		chessBoard.setWhiteCapturedPiecesCounter(0);
		chessBoard.setBlackCapturedPiecesCounter(0);
		for (int i = 0; i < 30; i++) {
			char pieceChar = capturedPieces[i];
			if (pieceChar != '-') {
				ChessPiece chessPiece = Utilities.getChessPiece(pieceChar);
				addCapturedPieceImage(chessPiece);
				chessBoard.updateScore(chessPiece);
			}
		}
		setScoreMessage();
	}


	private static void exportToGif() {
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
			} catch (Exception ex) {
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
		turnTextPane.setFocusable(false);
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

	private static void initializeChessBoardPanel() {
		if (chessBoardPanel != null) {
			gui.remove(chessBoardPanel);
		}
		chessBoardPanel = new JPanel(new GridLayout(gameParameters.getNumOfRows() + 2, NUM_OF_COLUMNS + 2));
		chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
		chessBoardPanel.setPreferredSize(new Dimension(width, height - 100));
		gui.add(chessBoardPanel, BorderLayout.CENTER);
	}

	private static void initializeCapturedPiecesPanel() {
		if (capturedPiecesPanel != null) {
			gui.remove(capturedPiecesPanel);
		}
		capturedPiecesPanel = new JPanel();
		gui.add(capturedPiecesPanel, BorderLayout.SOUTH);
	}

	private static void initializeChessBoardButtons() {
		chessBoardButtons = new JButton[gameParameters.getNumOfRows()][NUM_OF_COLUMNS];

		// Create the chess board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);

				// Our chess board pieces are 64x64 px in size, so we'll
				// "fill this in" using a transparent icon...
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
								GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
								BufferedImage.TYPE_INT_ARGB
						)
				);
				button.setIcon(imageIcon);

				Color color = getColorByRowCol(i, j);

				button.setBackground(color);

				// button.setBorderPainted(false);
				button.setFocusPainted(false);
				button.setFocusable(false);
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

				chessBoardButtons[i][j] = button;
			}
		}

		// fill the chess board panel
		chessBoardPanel.add(new JLabel(""));

		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
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
							chessBoardPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
						} else {
							chessBoardPanel.add(
									new JLabel(String.valueOf(gameParameters.getNumOfRows() - i), SwingConstants.CENTER)
							);
						}
						break;
					default:
						chessBoardPanel.add(chessBoardButtons[i][j]);
				}
				if (j == 0) {
					chessBoardPanel.add(chessBoardButtons[i][j]);
				}
			}
		}

		// fill the bottom row
		chessBoardPanel.add(new JLabel(""));
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}
	}

	private static void initializeCapturedPiecesImages() {
		capturedPiecesImages = new JLabel[31];

		// Create the captured chess board pieces icons.
		for (int i = 0; i < 31; i++) {
			capturedPiecesImages[i] = new JLabel();

			if (i == 15) {
				capturedPiecesImages[i].setText(ZERO_SCORE_TEXT);
			} else {
				// We'll "fill this in" using a transparent icon...
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CAPTURED_PIECE_PIXEL_SIZE,
								GuiConstants.CAPTURED_PIECE_PIXEL_SIZE,
								BufferedImage.TYPE_INT_ARGB
						)
				);
				capturedPiecesImages[i].setIcon(imageIcon);
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

		initializeChessBoardButtons();
		initializeCapturedPiecesImages();

		// if (!buttonsEnabled) {
		// 	enableChessBoardButtons();
		// }

		restoreDefaultValues();

		if (fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			redrawChessBoardButtons();
		} else {
			placePiecesToChessBoard(fenPosition);
		}

		initializeAI();

		chessBoard.setThreats();

		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

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
	private static void restoreDefaultValues() {
		chessBoard = new ChessBoard(gameParameters.getNumOfRows());

		startingPosition = "";
		endingPosition = "";

		undoFenPositions.clear();
		redoFenPositions.clear();

		initializeCapturedPieces();

		undoCapturedPieces.clear();
		redoCapturedPieces.clear();

		undoHalfMoveFenPositions.clear();
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

		isGameOver = false;

		setTurnMessage();
	}

	private static void initializeCapturedPieces() {
		capturedPieces = new char[30];
		for (int i = 0; i < 30; i++) {
			capturedPieces[i] = '-';
		}
	}

	private static void initializeAI() {
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
			if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
				Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					if (gameParameters.getAi1MaxDepth() <= 2) {
						ai = new MinimaxAI(
								gameParameters.getAi1MaxDepth(),
								Constants.BLACK,
								evaluation1
						);
					} else {
						ai = new MinimaxAlphaBetaPruningAI(
								gameParameters.getAi1MaxDepth(),
								Constants.BLACK,
								evaluation1
						);
					}
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					if (gameParameters.getAi2MaxDepth() <= 2) {
						ai = new MinimaxAI(
								gameParameters.getAi2MaxDepth(),
								Constants.WHITE,
								evaluation1
						);
					} else {
						ai = new MinimaxAlphaBetaPruningAI(
								gameParameters.getAi2MaxDepth(),
								Constants.WHITE,
								evaluation1
						);
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
	}

	private static Evaluation createEvaluation(EvaluationFunction evaluationFunction) {
		if (evaluationFunction == EvaluationFunction.SIMPLIFIED) {
			return new SimplifiedEvaluation();
		} else if (evaluationFunction == EvaluationFunction.PESTO) {
			return new PeSTOEvaluation();
		} else if (evaluationFunction == EvaluationFunction.WUKONG) {
			return new WukongEvaluation();
		} else {
			return new ShannonEvaluation();
		}
	}

	// This method is only called from inside a chess board button listener.
	private static void chessButtonClick(int row, int column, JButton button) {
		// System.out.println("row: " + row + ", column: " + column);

		hideHintPositions(hintPositions);

		String position = chessBoard.getPositionByRowCol(row, column);
		// System.out.println("position: " + position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];
		// System.out.println("chessPiece: " + chessPiece);

		int startingPositionRow = 0;
		int startingPositionColumn = 0;
		ChessPiece startingPiece = null;
		if (!startingPosition.equals("")) {
			startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
			startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);
			startingPiece = chessBoard.getGameBoard()[startingPositionRow][startingPositionColumn];
		}

		if (!startingButtonIsClicked
				&& (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			startingPosition = position;
			// System.out.println("startingPosition: " + startingPosition);

			// System.out.println("chessBoard: ");
			// System.out.println(chessBoard);

			if (!(chessPiece instanceof EmptySquare)) {

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

				GuiUtils.changeSquareColor(button, Color.CYAN);

				// Display the hint positions.
				if (hintPositions != null && hintPositions.size() != 0) {
					// System.out.println("hintPositions: " + hintPositions);
					for (String hintPosition : hintPositions) {
						// System.out.println("hintPosition: " + hintPosition);

						int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
						int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

						JButton hintPositionButton;

						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
								&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							hintPositionButton = chessBoardButtons
									[gameParameters.getNumOfRows() - 1 - hintPositionRow]
									[NUM_OF_COLUMNS - 1 - hintPositionColumn];
						} else {
							hintPositionButton = chessBoardButtons[hintPositionRow][hintPositionColumn];
						}

						// System.out.println("startingPiece: " + startingPiece);
						// System.out.println("hint position: " + hintPosition);

						ChessPiece hintPositionPiece = chessBoard.getGameBoard()[hintPositionRow][hintPositionColumn];

						if (chessPiece.getAllegiance() != hintPositionPiece.getAllegiance()
								&& hintPositionPiece.getAllegiance() != Allegiance.EMPTY
								|| chessBoard.getEnPassantPosition().equals(hintPosition)
								&& chessPiece instanceof Pawn) {
							GuiUtils.changeSquareColor(hintPositionButton, Color.RED);
						} else if (chessPiece instanceof Pawn &&
								(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == 0
										|| chessPiece.getAllegiance() == Allegiance.BLACK
										&& hintPositionRow == gameParameters.getNumOfRows() - 1)) {
							GuiUtils.changeSquareColor(hintPositionButton, Color.GREEN);
						} else if (hintPositionPiece instanceof EmptySquare) {
							GuiUtils.changeSquareColor(hintPositionButton, Color.BLUE);
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
					startingButton = chessBoardButtons
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardButtons[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				// System.out.println("startingButtonColor: " + startingButtonColor);
				GuiUtils.changeSquareColor(startingButton, startingButtonColor);

				startingButtonIsClicked = false;
				return;
			} else {
				undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

				undoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

				redoFenPositions.clear();
				redoHalfMoveFenPositions.clear();
				redoCapturedPieces.clear();

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
					startingButton = chessBoardButtons
							[gameParameters.getNumOfRows() - 1 - startingPositionRow]
							[NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - startingPositionRow,
							NUM_OF_COLUMNS - 1 - startingPositionColumn
					);
				} else {
					startingButton = chessBoardButtons[startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);
				}

				// System.out.println("startingButtonColor: " + startingButtonColor);
				GuiUtils.changeSquareColor(startingButton, startingButtonColor);
			}

			if (checkForGameOver()) return;

			if (gameParameters.isEnableSounds()) {
				SoundUtils.playMoveSound();
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
		int rowStart = chessBoard.getRowFromPosition(positionStart);
		int columnStart = chessBoard.getColumnFromPosition(positionStart);
		ChessPiece startingPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		String positionEnd = move.getPositions().get(1);
		int rowEnd = chessBoard.getRowFromPosition(positionEnd);
		int columnEnd = chessBoard.getColumnFromPosition(positionEnd);
		ChessPiece endSquare = chessBoard.getGameBoard()[rowEnd][columnEnd];

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
							GUI.gui,
							"Promote White Pawn to:",
							"White Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				} else if (startingPiece.getAllegiance() == Allegiance.BLACK) {
					value = (String) JOptionPane.showInputDialog(
							GUI.gui,
							"Promote Black Pawn to:",
							"Black Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				}
				// System.out.println("value: " + value);

				ChessPiece queen = new Queen(startingPiece.getAllegiance(), true);
				ChessPiece rook = new Rook(startingPiece.getAllegiance(), true);
				ChessPiece bishop = new Bishop(startingPiece.getAllegiance(), true);
				ChessPiece knight = new Knight(startingPiece.getAllegiance(), true);

				if (value == null || value.equals("Queen")) {
					chessBoard.getPiecesToPlace().put(positionEnd, queen);
				} else if (value.equals("Rook")) {
					chessBoard.getPiecesToPlace().put(positionEnd, rook);
				} else if (value.equals("Bishop")) {
					chessBoard.getPiecesToPlace().put(positionEnd, bishop);
				} else if (value.equals("Knight")) {
					chessBoard.getPiecesToPlace().put(positionEnd, knight);
				}

			}
		}

		// If a chessPiece capture has occurred.
		if (startingPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)) {
			updateCapturedPieces(endSquare);
			addCapturedPieceImage(endSquare);
		}
		// True if an en passant captured piece exists.
		else if (chessBoard.getCapturedEnPassantPiece() != null) {
			updateCapturedPieces(endSquare);
			addCapturedPieceImage(chessBoard.getCapturedEnPassantPiece());
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
		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
	}

	private static void updateCapturedPieces(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			int index = Math.min(chessBoard.getWhiteCapturedPiecesCounter(), 14);
			capturedPieces[index] = chessPiece.getPieceChar();
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			int index = Math.min(chessBoard.getBlackCapturedPiecesCounter(), 14);
			index = 30 - index - 1;
			capturedPieces[index] = chessPiece.getPieceChar();
		}
	}

	private static void addCapturedPieceImage(ChessPiece endSquare) {
		String imagePath = "";

		if (endSquare.isPromoted()) {
			if (endSquare.getAllegiance() == Allegiance.WHITE) {
				imagePath = GuiConstants.WHITE_PAWN_IMG_PATH;
			} else if (endSquare.getAllegiance() == Allegiance.BLACK) {
				imagePath = GuiConstants.BLACK_PAWN_IMG_PATH;
			}
		} else {
			imagePath = GuiUtils.getImagePath(endSquare);
		}

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CAPTURED_PIECE_PIXEL_SIZE);

		if (endSquare.getAllegiance() == Allegiance.WHITE) {
			int index = Math.min(chessBoard.getWhiteCapturedPiecesCounter(), 14);
			capturedPiecesImages[index].setIcon(pieceImage);
		} else if (endSquare.getAllegiance() == Allegiance.BLACK) {
			int index = Math.min(chessBoard.getBlackCapturedPiecesCounter(), 14);
			index = 31 - index - 1;
			capturedPiecesImages[index].setIcon(pieceImage);
		}

		chessBoard.incrementCapturedPiecesCounter(endSquare);

		setScoreMessage();
	}

	public static boolean checkForGameOver() {

		/* Check for White checkmate. */
		if (chessBoard.whitePlays()) {
			chessBoard.checkForWhiteCheckmate(true);
			if (chessBoard.getGameResult() == GameResult.WHITE_CHECKMATE) {

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! White wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playCheckmateSound();
				}

				int dialogResult = JOptionPane.showConfirmDialog(
						gui,
						"White wins! Start a new game?",
						"Checkmate",
						JOptionPane.YES_NO_OPTION
				);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		/* Check for Black checkmate. */
		else {
			chessBoard.checkForBlackCheckmate(true);
			if (chessBoard.getGameResult() == GameResult.BLACK_CHECKMATE) {

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! Black wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playCheckmateSound();
				}

				int dialogResult = JOptionPane.showConfirmDialog(
						gui,
						"Black wins! Start a new game?",
						"Checkmate",
						JOptionPane.YES_NO_OPTION
				);

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
			if (chessBoard.getGameResult() == GameResult.WHITE_STALEMATE_DRAW) {

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for White exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(
						gui,
						"Stalemate! No legal moves for White exist. Start a new game?",
						"Draw", JOptionPane.YES_NO_OPTION
				);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		// Check for Black stalemate.
		else if (chessBoard.whitePlays() && !chessBoard.isBlackKingInCheck()) {
			// System.out.println("Checking for black stalemate!");
			chessBoard.checkForBlackStalemateDraw();
			if (chessBoard.getGameResult() == GameResult.BLACK_STALEMATE_DRAW) {

				String turnMessage = "Move number: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for Black exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(
						gui,
						"Stalemate! No legal moves for Black exist. Start a new game?",
						"Draw", JOptionPane.YES_NO_OPTION
				);

				// System.out.println("dialogResult:" + dialogResult);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}


		/* Insufficient checkmate material draw implementation. */
		chessBoard.checkForInsufficientMaterialDraw();
		if (chessBoard.getGameResult() == GameResult.INSUFFICIENT_MATERIAL_DRAW) {

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


		// 50 full-moves without a chessPiece capture Draw implementation.
		if (chessBoard.checkForNoPieceCaptureDraw()) {
			int dialogResult = -1;

			if (!chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| !chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI) {
				dialogResult = JOptionPane.showConfirmDialog(
						gui,
						Constants.NO_CAPTURE_DRAW_MOVES_LIMIT +
								" full-moves have passed without a piece capture! Do you want to declare a draw?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);
			}

			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				chessBoard.setGameResult(GameResult.NO_CAPTURE_DRAW);
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
							|| !chessBoard.blackPlays()
							&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK)
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
				chessBoard.setGameResult(GameResult.THREEFOLD_REPETITION_DRAW);
				showDeclareDrawDialog();
				return true;
			}
		}

		return false;
	}

	// We are comparing FEN positions, but without checking the half-move clock and the full-move number.
	private static boolean checkForThreefoldRepetitionDraw() {

		if (!undoHalfMoveFenPositions.isEmpty()) {
			int N = undoHalfMoveFenPositions.size();
			String lastHalfMoveFenPosition = undoHalfMoveFenPositions.get(N - 1);
			lastHalfMoveFenPosition = FenUtils.skipCounters(lastHalfMoveFenPosition);
			int numOfRepeats = 0;
			for (int i = N - 2; i >= 0; i--) {
				// Skip the last iteration, if the number of repeats found is less ore equal to 1.
				// Also, skip the second to last iteration, if the number of repeats found is 0.
				if (!(numOfRepeats <= 1 && i == 0 || numOfRepeats == 0 && i == 1)) {
					// System.out.println("i: " + i);
					String otherHalfMoveFenPosition = undoHalfMoveFenPositions.get(i);
					otherHalfMoveFenPosition = FenUtils.skipCounters(otherHalfMoveFenPosition);
					if (lastHalfMoveFenPosition.equals(otherHalfMoveFenPosition)) {
						// System.out.println("i: " + i + ");
						// System.out.println(lastHalfMoveFenPosition);
						// System.out.println(otherHalfMoveFenPosition);
						// System.out.println("numOfRepeats: " + numOfRepeats);
						numOfRepeats++;
						if (numOfRepeats == 3) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private static void showDeclareDrawDialog() {
		String turnMessage = "Move number: "
				+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
				+ ". It is a draw.";
		turnTextPane.setText(turnMessage);

		int dialogResult = JOptionPane.showConfirmDialog(
				gui,
				"It is a draw! Start a new game?",
				"Draw",
				JOptionPane.YES_NO_OPTION
		);

		startNewGameOrNot(dialogResult);
	}

	private static void startNewGameOrNot(int dialogResult) {
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


	// Gets called after the human player makes a move. It makes a Minimax AI move.
	public static void aiMove(AI ai) {
		Move aiMove = ai.getNextMove(chessBoard);
		System.out.println("aiMove: " + aiMove);
		// System.out.println("lastCapturedPieceValue: " + chessBoard.getLastCapturedPieceValue());

		makeDisplayMove(aiMove, true);
		// System.out.println("board value after aiMove -> " + chessBoard.evaluate());

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
		if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
			Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
			if (gameParameters.getAi1MaxDepth() <= 2) {
				ai1 = new MinimaxAI(
						gameParameters.getAi1MaxDepth(),
						Constants.WHITE,
						evaluation1
				);
			} else {
				ai1 = new MinimaxAlphaBetaPruningAI(
						gameParameters.getAi1MaxDepth(),
						Constants.WHITE,
						evaluation1
				);
			}
		} else {
			ai1 = new RandomChoiceAI(Constants.WHITE);
		}

		AI ai2;
		if (gameParameters.getAi2Type() == AiType.MINIMAX_AI) {
			Evaluation evaluation2 = createEvaluation(gameParameters.getEvaluationFunction2());
			ai2 = new MinimaxAlphaBetaPruningAI(
					gameParameters.getAi2MaxDepth(),
					Constants.BLACK,
					evaluation2
			);
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
				} catch (InterruptedException ex) {
					ex.printStackTrace();
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
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (undoItem != null) {
			undoItem.setEnabled(true);
		}
	}

	private static void aiVsAiMove(AI ai) {
		undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		undoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

		aiMove(ai);

		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		setTurnMessage();
		setScoreMessage();

		frame.revalidate();
		frame.paint(frame.getGraphics());
	}

	private static void hideHintPositions(Set<String> positionsToHide) {
		if (positionsToHide != null && positionsToHide.size() != 0) {
			for (String hintPosition : positionsToHide) {
				// System.out.println("hide hint position: " + hintPosition);
				int row = chessBoard.getRowFromPosition(hintPosition);
				int column = chessBoard.getColumnFromPosition(hintPosition);
				// System.out.println("hide hint row: " + row + ", hide hint column: " + column);

				JButton button;
				Color buttonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					button = chessBoardButtons[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column];
					buttonColor = getColorByRowCol(
							gameParameters.getNumOfRows() - 1 - row,
							NUM_OF_COLUMNS - 1 - column
					);
				} else {
					button = chessBoardButtons[row][column];
					buttonColor = getColorByRowCol(row, column);
				}

				GuiUtils.changeSquareColor(button, buttonColor);
			}
		}
	}

	private static Color getColorByRowCol(int row, int column) {
		Color color;
		if ((column % 2 == 1 && row % 2 == 1)
				//) {
				|| (column % 2 == 0 && row % 2 == 0)) {
			color = gameParameters.getWhiteSquareColor();
		} else {
			color = gameParameters.getBlackSquareColor();
		}
		return color;
	}

	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI)
	public static void placePieceToPosition(String position, ChessPiece chessPiece) {
		String imagePath = GuiUtils.getImagePath(chessPiece);

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_SQUARE_PIXEL_SIZE);

		int column = chessBoard.getColumnFromPosition(position);
		int row = chessBoard.getRowFromPosition(position);

		chessBoard.getGameBoard()[row][column] = chessPiece;

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardButtons[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column].setIcon(pieceImage);
		} else {
			chessBoardButtons[row][column].setIcon(pieceImage);
		}
	}

	// It removes the given chessPiece from the board (both the data structure and the GUI).
	private static void removePieceFromPosition(String position) {
		// int column = (int) Character.toUpperCase(position.charAt(0)) - (int) 'A';
		// int row = N - Character.getNumericValue(position.charAt(1));

		int column = chessBoard.getColumnFromPosition(position);
		int row = chessBoard.getRowFromPosition(position);

		// Our chess board pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon.
		ImageIcon imageIcon = new ImageIcon(new BufferedImage(GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
				GuiConstants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardButtons[gameParameters.getNumOfRows() - 1 - row][NUM_OF_COLUMNS - 1 - column].setIcon(imageIcon);
		} else {
			chessBoardButtons[row][column].setIcon(imageIcon);
		}
	}

	private static void redrawChessBoardButtons() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(position, chessPiece);
			}
		}
	}

	public static void placePiecesToStartingPositions() {
		chessBoard.placePiecesToStartingPositions();
		redrawChessBoardButtons();

		chessBoard.setThreats();

		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

		setTurnMessage();
	}

	public static void placePiecesToChessBoard(String fenPosition) {
		chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);

		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < Constants.NUM_OF_COLUMNS; j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();

		setTurnMessage();

		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
	}

	public static void makeChessBoardSquaresEmpty() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				// chessBoardButtons[i][j].setEnabled(true);

				// Our chess board pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon.
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
								GuiConstants.CHESS_SQUARE_PIXEL_SIZE,
								BufferedImage.TYPE_INT_ARGB
						)
				);
				chessBoardButtons[i][j].setIcon(imageIcon);

				Color color = getColorByRowCol(i, j);
				chessBoardButtons[i][j].setBackground(color);
				chessBoardButtons[i][j].setOpaque(true);
				// chessBoardButtons[i][j].setBorderPainted(false);

				chessBoard.getGameBoard()[i][j] = new EmptySquare();
			}
		}
		chessBoard.setThreats();
	}

	private static void enableChessBoardButtons() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				JButton button = chessBoardButtons[i][j];
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

	private static void disableChessBoardSquares() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				// chessBoardButtons[i][j].setEnabled(false);
				ActionListener[] actionListeners = chessBoardButtons[i][j].getActionListeners();
				if (actionListeners.length > 0) {
					chessBoardButtons[i][j].removeActionListener(actionListeners[0]);
				}
			}
		}
		buttonsEnabled = false;
	}

	public static void main(String[] args) {
		create(TITLE);

		System.out.println(chessBoard);
	}

}
