package com.chriskormaris.mychessgame.gui.buttons;


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
import com.chriskormaris.mychessgame.gui.drag_and_drop.DragAndDropGui;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.settings.SettingsWindow;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;
import com.chriskormaris.mychessgame.gui.util.GuiUtils;
import com.chriskormaris.mychessgame.gui.util.ResourceLoader;
import com.chriskormaris.mychessgame.gui.util.SoundUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalButtonUI;
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

import static javax.swing.JOptionPane.QUESTION_MESSAGE;


public class ButtonsGui extends JFrame {


	public GameParameters gameParameters;
	public GameParameters newGameParameters;

	int width;
	int height;

	JPanel guiPanel;
	JTextPane turnTextPane;

	// This stack of "String" objects is used to handle the "undo" and "redo" functionality.
	Stack<String> nextHalfMoveFenPositions;

	// The length of this array is 30 elements.
	// The first 15 elements represent White captured pieces and are capital chars.
	// The last 15 elements represent Black captured pieces and are lowercase chars.
	// The elements could also be '-', which is a placeholder for future captured pieces.
	char[] capturedPieces;

	// These stacks of "char" arrays are used to handle the "undo" and "redo" functionality.
	Stack<char[]> undoCapturedPieces;
	Stack<char[]> redoCapturedPieces;

	// The position (0, 0) of the "chessBoard.getGameBoard()" is the upper left button
	// of the JButton array "chessButtons".
	// The position (chessBoard.getNumOfRows() - 1, 0) of the "chessBoard.getGameBoard()" is the lower left button
	// of the JButton array "chessButtons".
	public ChessBoard chessBoard;

	// This variable is used for the implementation of "Human vs AI".
	public AI ai;

	JToolBar tools;
	JPanel chessPanel;
	JPanel capturedPiecesPanel;

	// The position (0, 0) of the chessButtons,
	// corresponds to the position (chessBoard.getNumOfColumns() - 1, 0) of the ChessBoard's gameBoard.
	JButton[][] chessButtons;

	// 30 captured pieces at maximum + 1 label for displaying the score = 31 labels size
	JLabel[] capturedPiecesImages;

	int score;

	int whiteCapturedPiecesCounter;
	int blackCapturedPiecesCounter;

	String startingPosition;
	String endingPosition;

	boolean startingButtonIsClicked;

	Set<String> hintPositions;

	boolean buttonsEnabled;

	// This variable is used for the implementation of "AI vs AI".
	boolean isGameOver;

	String savedFenPosition;

	JMenuItem undoItem;
	JMenuItem redoItem;
	JMenuItem exportFenPositionItem;
	JMenuItem saveCheckpointItem;
	JMenuItem loadCheckpointItem;

	KeyListener undoRedoKeyListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_Z) {
				undo();
			} else if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_Y) {
				redo();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	};

	public ButtonsGui() {
		this(GuiConstants.TITLE);
	}

	public ButtonsGui(String title) {
		super(title);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) screenSize.getHeight() - 40;
		width = height + 40;

		guiPanel = new JPanel();
		turnTextPane = new JTextPane();

		nextHalfMoveFenPositions = new Stack<>();

		initializeCapturedPieces();

		undoCapturedPieces = new Stack<>();
		redoCapturedPieces = new Stack<>();

		gameParameters = new GameParameters();
		gameParameters.setGuiType(GuiType.BUTTONS);
		newGameParameters = new GameParameters(gameParameters);

		chessBoard = new ChessBoard();

		tools = new JToolBar();

		startingPosition = "";
		endingPosition = "";

		hintPositions = new HashSet<>();
		buttonsEnabled = true;

		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);

		initializeGUI();
		redrawChessButtons();
		initializeAI();

		super.add(guiPanel);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.setLocationByPlatform(true);

		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		super.pack();

		super.setSize(new Dimension(width, height));

		// ensures the minimum size is enforced.
		super.setMinimumSize(super.getSize());

		super.setLocation((int) (screenSize.getWidth() - super.getWidth()) / 2, 5);

		super.setResizable(false);

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			super.setIconImage(icon);
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
			SettingsWindow settings = new SettingsWindow(this, newGameParameters);
			settings.setVisible(true);
		});

		importStartingFenPositionItem.addActionListener(e -> {
			String fenPosition = (String) JOptionPane.showInputDialog(
					this,
					"Please insert the starting \"FEN\" position in the text field below:"
							+ "                      ",
					"Import starting FEN position",
					QUESTION_MESSAGE,
					null,
					null,
					Constants.DEFAULT_STARTING_FEN_POSITION
			);

			if (fenPosition != null) {
				startNewGame(fenPosition);
			}
		});

		exportFenPositionItem.addActionListener(e -> {
			String exportedFenPositionFilename = (String) JOptionPane.showInputDialog(
					this,
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
						this,
						GuiConstants.RULES,
						"How to Play",
						JOptionPane.INFORMATION_MESSAGE
				)
		);

		aboutItem.addActionListener(e -> {
			JLabel label = new JLabel(
					"<html>A traditional Chess game implementation using Minimax AI,<br>"
							+ "with Alpha-Beta Pruning.<br>"
							+ "&copy; Created by: Christos Kormaris, Athens 2020<br>"
							+ "Version " + GuiConstants.VERSION + "</html>"
			);

			try {
				BufferedImage img = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));

				Image dImg = img.getScaledInstance(
						GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
						GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
						Image.SCALE_SMOOTH
				);
				ImageIcon imageIcon = new ImageIcon(dImg);

				JOptionPane.showMessageDialog(this, label, "About", JOptionPane.PLAIN_MESSAGE, imageIcon);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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

		super.setJMenuBar(menuBar);

		super.setVisible(true);
		super.addKeyListener(undoRedoKeyListener);
	}

	private void configureGuiStyle() {
		try {
			if (gameParameters.getGuiStyle() == GuiStyle.CROSS_PLATFORM) {
				// Option 1
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} else if (gameParameters.getGuiStyle() == GuiStyle.SYSTEM) {
				// Option 2
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else if (gameParameters.getGuiStyle() == GuiStyle.NIMBUS) {
				// Option 3
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


	private void initializeGUI() {
		configureGuiStyle();

		// Set up the main GUI.
		// gui.setBorder(new EmptyBorder(0, 0, 0, 0));
		guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));

		initializeTurnTextPaneBar();
		setTurnMessage();

		initializeChessPanel();
		initializeChessButtons();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();

		setScoreMessage();
	}


	private void setTurnMessage() {
		if (chessBoard.getHalfMoveNumber() == 1) {
			turnTextPane.setText(GuiConstants.FIRST_TURN_TEXT);
		} else {
			String turnMessage = "Turn: " + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". ";
			turnMessage += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";

			if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck()) {
				turnMessage += " White King is in check!";
			} else if (chessBoard.blackPlays() && chessBoard.isBlackKingInCheck()) {
				turnMessage += " Black King is in check!";
			}

			turnTextPane.setText(turnMessage);
		}
	}


	private void setScoreMessage() {
		if (score > 0) {
			capturedPiecesImages[15].setText("White: +" + score);
		} else if (score < 0) {
			capturedPiecesImages[15].setText("Black: +" + (-score));
		} else {
			capturedPiecesImages[15].setText(GuiConstants.ZERO_SCORE_TEXT);
		}
	}


	private void undo() {
		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
				&& !chessBoard.getPreviousHalfMoveFenPositions().isEmpty()
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& chessBoard.getPreviousHalfMoveFenPositions().size() >= 2) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				if (!buttonsEnabled) {
					enableChessButtons();
				}

				startingButtonIsClicked = false;
				hideHintPositions();
			}

			nextHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
			redoCapturedPieces.push(capturedPieces.clone());
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && !(chessBoard.blackPlays() && isGameOver)) {
				nextHalfMoveFenPositions.push(chessBoard.getPreviousHalfMoveFenPositions().pop());
				redoCapturedPieces.push(undoCapturedPieces.pop());
			}

			String fenPosition = chessBoard.getPreviousHalfMoveFenPositions().pop();
			Stack<String> previousHalfMoveFenPositions = chessBoard.getPreviousHalfMoveFenPositions();
			placePiecesToChessBoard(fenPosition);
			chessBoard.setPreviousHalfMoveFenPositions(previousHalfMoveFenPositions);
			capturedPieces = undoCapturedPieces.pop();

			resetCapturedPiecesPanel();

			resetScore();
			setScoreMessage();

			setTurnMessage();

			System.out.println();
			System.out.println(chessBoard);

			if (chessBoard.getPreviousHalfMoveFenPositions().isEmpty()) {
				undoItem.setEnabled(false);
			}

			if (redoItem != null) {
				redoItem.setEnabled(true);
			}

			if (exportFenPositionItem != null) {
				exportFenPositionItem.setEnabled(true);
			}

			if (saveCheckpointItem != null) {
				saveCheckpointItem.setEnabled(true);
			}

			isGameOver = false;
		}
	}


	// NOTE: We are not able to perform a redo,
	// if we are in a terminal state, because the game has ended.
	private void redo() {
		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI && !nextHalfMoveFenPositions.isEmpty()
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && nextHalfMoveFenPositions.size() >= 2) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				startingButtonIsClicked = false;
				hideHintPositions();
			}

			chessBoard.getPreviousHalfMoveFenPositions().push(FenUtils.getFenPositionFromChessBoard(chessBoard));
			undoCapturedPieces.push(capturedPieces.clone());
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
				chessBoard.getPreviousHalfMoveFenPositions().push(nextHalfMoveFenPositions.pop());
				undoCapturedPieces.push(redoCapturedPieces.pop());
			}
			Stack<String> previousHalfMoveFenPositions = chessBoard.getPreviousHalfMoveFenPositions();

			placePiecesToChessBoard(nextHalfMoveFenPositions.pop());
			chessBoard.setPreviousHalfMoveFenPositions(previousHalfMoveFenPositions);
			capturedPieces = redoCapturedPieces.pop();

			resetCapturedPiecesPanel();

			resetScore();
			setScoreMessage();

			setTurnMessage();

			for (int i = 0; i < 31; i++) {
				capturedPiecesPanel.add(capturedPiecesImages[i]);
			}

			if (nextHalfMoveFenPositions.isEmpty()) {
				redoItem.setEnabled(false);
			}

			System.out.println();
			System.out.println(chessBoard);

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}

			checkForGameOver();
		}
	}


	private void resetCapturedPiecesPanel() {
		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();
		whiteCapturedPiecesCounter = 0;
		blackCapturedPiecesCounter = 0;
		for (int i = 0; i < 30; i++) {
			char pieceChar = capturedPieces[i];
			if (pieceChar != '-') {
				ChessPiece chessPiece = Utilities.getChessPiece(pieceChar);
				addCapturedPieceImage(chessPiece);
			}
		}
	}

	private void resetScore() {
		score = 0;
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				String position = chessBoard.getPositionByRowCol(i, j);
				ChessPiece chessPiece = chessBoard.getChessPieceFromPosition(position);
				score += Utilities.getScoreValue(chessPiece);
			}
		}
	}

	private void exportToGif() {
		String gifName = JOptionPane.showInputDialog(
				this,
				"Please type the exported \".gif\" file name:",
				"chess_board.gif"
		);

		if (gifName != null) {
			BufferedImage bufferedImage = new BufferedImage(
					guiPanel.getSize().width,
					guiPanel.getSize().height,
					BufferedImage.TYPE_INT_ARGB
			);
			Graphics graphics = bufferedImage.createGraphics();
			guiPanel.paint(graphics);
			graphics.dispose();
			try {
				ImageIO.write(bufferedImage, "gif", new File(gifName));
				System.out.println("Exported .gif file!");
			} catch (Exception ex) {
				System.err.println("Error exporting .gif file!");
				System.err.flush();
			}
		}
	}

	private void initializeTurnTextPaneBar() {
		if (tools != null) {
			guiPanel.remove(tools);
		}

		tools = new JToolBar();
		tools.setFloatable(false);

		turnTextPane.setEditable(false);
		turnTextPane.setFocusable(false);
		centerTextPaneAndMakeBold();

		tools.add(turnTextPane);

		guiPanel.add(tools, BorderLayout.NORTH);
	}

	private void centerTextPaneAndMakeBold() {
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

	private void initializeChessPanel() {
		if (chessPanel != null) {
			guiPanel.remove(chessPanel);
		}
		chessPanel = new JPanel(new GridLayout(chessBoard.getNumOfRows() + 2, chessBoard.getNumOfColumns() + 2));
		chessPanel.setBorder(new LineBorder(Color.BLACK));
		chessPanel.setPreferredSize(new Dimension(width, height - 100));
		guiPanel.add(chessPanel, BorderLayout.CENTER);
	}

	private void initializeCapturedPiecesPanel() {
		if (capturedPiecesPanel != null) {
			guiPanel.remove(capturedPiecesPanel);
		}
		capturedPiecesPanel = new JPanel();
		guiPanel.add(capturedPiecesPanel, BorderLayout.SOUTH);
	}

	private void initializeChessButtons() {
		chessButtons = new JButton[chessBoard.getNumOfRows()][chessBoard.getNumOfColumns()];

		// Create the chess board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);

				// Our chess board pieces are 64x64 px in size, so we'll
				// "fill this in" using a transparent icon...
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
								GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
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

				if (gameParameters.getGuiStyle() == GuiStyle.SYSTEM) {
					button.setUI(new MetalButtonUI());
				}

				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = chessBoard.getNumOfRows() - 1 - i;
					column = chessBoard.getNumOfColumns() - 1 - j;
				} else {
					row = i;
					column = j;
				}

				button.addActionListener(e -> chessButtonClick(row, column, button));

				chessButtons[i][j] = button;
			}
		}

		// fill the chess panel
		chessPanel.add(new JLabel(""));

		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = chessBoard.getNumOfColumns() - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}

		chessPanel.add(new JLabel(""));
		// fill the black non-pawn chessPiece row
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns() + 1; j++) {
				if (j == 0 || j == chessBoard.getNumOfColumns()) {
					int rankNumber = chessBoard.getNumOfRows() - i;
					if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
							&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
						rankNumber = i + 1;
					}
					chessPanel.add(new JLabel(String.valueOf(rankNumber), SwingConstants.CENTER));
				} else {
					chessPanel.add(chessButtons[i][j]);
				}
				if (j == 0) {
					chessPanel.add(chessButtons[i][j]);
				}
			}
		}

		// fill the bottom row
		chessPanel.add(new JLabel(""));
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = chessBoard.getNumOfColumns() - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}
	}

	private void initializeCapturedPiecesImages() {
		capturedPiecesImages = new JLabel[31];

		// Create the captured chess board pieces icons.
		for (int i = 0; i < 31; i++) {
			capturedPiecesImages[i] = new JLabel();

			if (i == 15) {
				capturedPiecesImages[i].setText(GuiConstants.ZERO_SCORE_TEXT);
			} else {
				// We'll "fill this in" using a transparent icon...
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CAPTURED_CHESS_PIECE_PIXEL_SIZE,
								GuiConstants.CAPTURED_CHESS_PIECE_PIXEL_SIZE,
								BufferedImage.TYPE_INT_ARGB
						)
				);
				capturedPiecesImages[i].setIcon(imageIcon);
			}

			capturedPiecesPanel.add(capturedPiecesImages[i]);
		}
	}

	public void startNewGame() {
		if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
			startNewGame(Constants.DEFAULT_STARTING_FEN_POSITION);
		} if (newGameParameters.getGuiType() == GuiType.DRAG_AND_DROP) {
			super.dispose();
			DragAndDropGui dragAndDropGui = new DragAndDropGui();
			dragAndDropGui.newGameParameters = newGameParameters;
			dragAndDropGui.startNewGame();
		}
	}

	public void startNewGame(String fenPosition) {
		System.out.println("Starting new game!");

		if (newGameParameters.getNumOfRows() != Constants.DEFAULT_NUM_OF_ROWS) {
			newGameParameters.setEvaluationFunction1(EvaluationFunction.SHANNON);
			if (newGameParameters.getGameMode() == GameMode.AI_VS_AI) {
				newGameParameters.setEvaluationFunction2(EvaluationFunction.SHANNON);
			}
		}
		gameParameters = new GameParameters(newGameParameters);

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}

		chessPanel.removeAll();

		restoreDefaultValues(fenPosition);

		initializeGUI();

		redrawChessButtons();
		super.revalidate();

		initializeAI();

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
	private void restoreDefaultValues(String fenPosition) {
		if (fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			chessBoard = new ChessBoard(gameParameters.getNumOfRows());
		} else {
			chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition, gameParameters.getNumOfRows());
		}

		startingPosition = "";
		endingPosition = "";

		nextHalfMoveFenPositions.clear();

		initializeCapturedPieces();

		undoCapturedPieces.clear();
		redoCapturedPieces.clear();

		if (fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			score = 0;
		} else {
			resetScore();
		}

		whiteCapturedPiecesCounter = 0;
		blackCapturedPiecesCounter = 0;

		startingButtonIsClicked = false;

		hintPositions = new HashSet<>();

		buttonsEnabled = true;

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
	}

	private void initializeCapturedPieces() {
		capturedPieces = new char[30];
		for (int i = 0; i < 30; i++) {
			capturedPieces[i] = '-';
		}
	}

	private void initializeAI() {
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
			if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
				Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					if (gameParameters.getAi1MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.BLACK, evaluation1);
					} else {
						ai = new MinimaxAlphaBetaPruningAI(
								gameParameters.getAi1MaxDepth(),
								Constants.BLACK,
								evaluation1
						);
					}
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					if (gameParameters.getAi2MaxDepth() <= 2) {
						ai = new MinimaxAI(gameParameters.getAi2MaxDepth(), Constants.WHITE, evaluation1);
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

	private Evaluation createEvaluation(EvaluationFunction evaluationFunction) {
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
	private void chessButtonClick(int row, int column, JButton button) {
		hideHintPositions();

		String position = chessBoard.getPositionByRowCol(row, column);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		int startingPositionRow;
		int startingPositionColumn;
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

			if (!(chessPiece instanceof EmptySquare)) {
				hintPositions = chessBoard.getNextPositions(position);
				GuiUtils.changeSquareColor(button, Color.CYAN);

				// Display the hint positions.
				for (String hintPosition : hintPositions) {
					int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
					int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

					int hintPositionButtonRow = hintPositionRow;
					int hintPositionButtonColumn = hintPositionColumn;
					if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
							&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
						hintPositionButtonRow = chessBoard.getNumOfRows() - 1 - hintPositionRow;
						hintPositionButtonColumn = chessBoard.getNumOfColumns() - 1 - hintPositionColumn;
					}
					JButton hintPositionButton = chessButtons[hintPositionButtonRow][hintPositionButtonColumn];
					ChessPiece hintPositionPiece = chessBoard.getGameBoard()[hintPositionRow][hintPositionColumn];

					if (hintPositionPiece.getAllegiance() != Allegiance.NONE
							|| chessBoard.getEnPassantPosition().equals(hintPosition)
							&& chessPiece instanceof Pawn) {
						GuiUtils.changeSquareColor(hintPositionButton, Color.RED);
					} else if (chessPiece instanceof Pawn &&
							(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == 0
									|| chessPiece.getAllegiance() == Allegiance.BLACK
									&& hintPositionRow == chessBoard.getNumOfRows() - 1)) {
						GuiUtils.changeSquareColor(hintPositionButton, Color.GREEN);
					} else if (hintPositionPiece instanceof EmptySquare) {
						GuiUtils.changeSquareColor(hintPositionButton, Color.BLUE);
					}
				}

				startingButtonIsClicked = true;
			}

		} else if (startingButtonIsClicked && startingPiece != null
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| startingPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {

			startingButtonIsClicked = false;
			endingPosition = position;

			if (hintPositions.contains(endingPosition)) {
				Move move = new Move(startingPosition, endingPosition);
				makeDisplayMove(move, false);

				if (checkForGameOver()) return;

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playMoveSound();
				}

				hintPositions.clear();

				if (undoItem != null) {
					undoItem.setEnabled(true);
				}
				if (redoItem != null) {
					redoItem.setEnabled(false);
				}

				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
					aiMove(ai);
				}
			}
		}
	}

	private void makeDisplayMove(Move move, boolean isAiMove) {
		String positionStart = move.getPositionStart();
		int rowStart = chessBoard.getRowFromPosition(positionStart);
		int columnStart = chessBoard.getColumnFromPosition(positionStart);
		ChessPiece startingPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		String positionEnd = move.getPositionEnd();
		int rowEnd = chessBoard.getRowFromPosition(positionEnd);
		int columnEnd = chessBoard.getColumnFromPosition(positionEnd);
		ChessPiece endSquare = chessBoard.getGameBoard()[rowEnd][columnEnd];

		undoCapturedPieces.push(capturedPieces.clone());

		nextHalfMoveFenPositions.clear();
		redoCapturedPieces.clear();

		chessBoard.makeMove(move, true);

		// Pawn promotion implementation.
		// If AI plays, automatically choose the best promotion piece, based on the best outcome.
		if (startingPiece instanceof Pawn
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && rowEnd == 0
				|| startingPiece.getAllegiance() == Allegiance.BLACK && rowEnd == chessBoard.getNumOfRows() - 1)) {
			ChessPiece promotedPiece = new Queen(startingPiece.getAllegiance(), true);
			if (isAiMove) {
				chessBoard.automaticPawnPromotion(startingPiece, positionEnd, true);

				promotedPiece = chessBoard.getGameBoard()[rowEnd][columnEnd];
				if (promotedPiece.getAllegiance() == Allegiance.WHITE) {
					JOptionPane.showMessageDialog(
							this,
							"Promoting White Pawn to " + promotedPiece + "!",
							"White Pawn Promotion",
							JOptionPane.INFORMATION_MESSAGE
					);
				} else if (promotedPiece.getAllegiance() == Allegiance.BLACK) {
					JOptionPane.showMessageDialog(
							this,
							"Promoting Black Pawn to " + promotedPiece + "!",
							"Black Pawn Promotion",
							JOptionPane.INFORMATION_MESSAGE
					);
				}
			}
			// If human player plays, select which promotion piece you want and display it on the GUI.
			else {
				removePieceFromPosition(positionStart);
				placePieceToPosition(positionEnd, startingPiece);

				String[] promotionPieces = {"Queen", "Rook", "Bishop", "Knight"};
				String initialSelection = "Queen";

				String value = null;
				if (startingPiece.getAllegiance() == Allegiance.WHITE) {
					value = (String) JOptionPane.showInputDialog(
							this,
							"Promote White Pawn to:",
							"White Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				} else if (startingPiece.getAllegiance() == Allegiance.BLACK) {
					value = (String) JOptionPane.showInputDialog(
							this,
							"Promote Black Pawn to:",
							"Black Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				}

				if (value == null || value.equals("Queen")) {
					promotedPiece = new Queen(startingPiece.getAllegiance(), true);
				} else if (value.equals("Rook")) {
					promotedPiece = new Rook(startingPiece.getAllegiance(), true);
				} else if (value.equals("Bishop")) {
					promotedPiece = new Bishop(startingPiece.getAllegiance(), true);
				} else if (value.equals("Knight")) {
					promotedPiece = new Knight(startingPiece.getAllegiance(), true);
				}
				chessBoard.getPiecesToPlace().put(positionEnd, promotedPiece);
			}

			score += Utilities.getScoreValue(promotedPiece);
			if (startingPiece.getAllegiance() == Allegiance.WHITE) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (startingPiece.getAllegiance() == Allegiance.BLACK) {
				score += Constants.PAWN_SCORE_VALUE;
			}
		}

		// If a chessPiece capture has occurred.
		if (startingPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)) {
			score -= Utilities.getScoreValue(endSquare);

			updateCapturedPieces(endSquare);
			addCapturedPieceImage(endSquare);
		}
		// True if an en passant captured piece exists.
		else if (!(chessBoard.getCapturedEnPassantPiece() instanceof EmptySquare)) {
			score -= Utilities.getScoreValue(chessBoard.getCapturedEnPassantPiece());

			updateCapturedPieces(chessBoard.getCapturedEnPassantPiece());
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

		setTurnMessage();
		setScoreMessage();

		System.out.println();
		System.out.println(chessBoard);
	}

	private void updateCapturedPieces(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			int index = Math.min(whiteCapturedPiecesCounter, 14);
			capturedPieces[index] = chessPiece.getPieceChar();
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			int index = Math.min(blackCapturedPiecesCounter, 14);
			index = 30 - index - 1;
			capturedPieces[index] = chessPiece.getPieceChar();
		}
	}

	private void addCapturedPieceImage(ChessPiece endSquare) {
		String imagePath = GuiUtils.getImagePath(endSquare);

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CAPTURED_CHESS_PIECE_PIXEL_SIZE);

		if (endSquare.getAllegiance() == Allegiance.WHITE) {
			int index = Math.min(whiteCapturedPiecesCounter, 14);
			capturedPiecesImages[index].setIcon(pieceImage);
		} else if (endSquare.getAllegiance() == Allegiance.BLACK) {
			int index = Math.min(blackCapturedPiecesCounter, 14);
			index = 31 - index - 1;
			capturedPiecesImages[index].setIcon(pieceImage);
		}

		incrementCapturedPiecesCounter(endSquare.getAllegiance());

		setScoreMessage();
	}

	private void incrementCapturedPiecesCounter(Allegiance allegiance) {
		if (allegiance == Allegiance.WHITE) {
			whiteCapturedPiecesCounter++;
		} else if (allegiance == Allegiance.BLACK) {
			blackCapturedPiecesCounter++;
		}
	}

	public boolean checkForGameOver() {
		/* Check for White checkmate. */
		if (chessBoard.blackPlays()) {
			chessBoard.checkForWhiteCheckmate();
			if (chessBoard.getGameResult() == GameResult.WHITE_CHECKMATE) {
				String turnMessage = "Turn: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! White wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playCheckmateSound();
				}

				int dialogResult = JOptionPane.showConfirmDialog(
						this,
						"White wins! Start a new game?",
						"Checkmate",
						JOptionPane.YES_NO_OPTION
				);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		/* Check for Black checkmate. */
		else {
			chessBoard.checkForBlackCheckmate();
			if (chessBoard.getGameResult() == GameResult.BLACK_CHECKMATE) {
				String turnMessage = "Turn: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! Black wins!";
				turnTextPane.setText(turnMessage);

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playCheckmateSound();
				}

				int dialogResult = JOptionPane.showConfirmDialog(
						this,
						"Black wins! Start a new game?",
						"Checkmate",
						JOptionPane.YES_NO_OPTION
				);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		/* Stalemate draw implementation. */

		// Check for White stalemate.
		if (chessBoard.whitePlays()) {
			chessBoard.checkForWhiteStalemateDraw();
			if (chessBoard.getGameResult() == GameResult.WHITE_STALEMATE_DRAW) {
				String turnMessage = "Turn: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for White exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(
						this,
						"Stalemate! No legal moves for White exist. Start a new game?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		// Check for Black stalemate.
		else {
			chessBoard.checkForBlackStalemateDraw();
			if (chessBoard.getGameResult() == GameResult.BLACK_STALEMATE_DRAW) {
				String turnMessage = "Turn: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". Stalemate! No legal moves for Black exist.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(
						this,
						"Stalemate! No legal moves for Black exist. Start a new game?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);

				startNewGameOrNot(dialogResult);

				return true;
			}
		}

		/* Insufficient checkmate material draw implementation. */
		if (chessBoard.checkForInsufficientMatingMaterialDraw()) {
			String turnMessage = "Turn: "
					+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
					+ ". It is a draw.";
			turnTextPane.setText(turnMessage);

			int dialogResult = JOptionPane.showConfirmDialog(
					this,
					"It is a draw due to insufficient mating material! Start a new game?",
					"Draw",
					JOptionPane.YES_NO_OPTION
			);

			startNewGameOrNot(dialogResult);

			return true;
		}

		// 75 full-moves without a Chess piece capture Draw implementation.
		if (chessBoard.checkForUnconditionalNoCaptureDraw()) {
			String turnMessage = "Turn: "
					+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
					+ ". It is a draw.";
			turnTextPane.setText(turnMessage);

			int dialogResult = JOptionPane.showConfirmDialog(
					this,
					"It is a draw! 75 full-moves have passed without a piece capture! Start a new game?",
					"Draw",
					JOptionPane.YES_NO_OPTION
			);

			startNewGameOrNot(dialogResult);

			return true;
		}

		// 50 full-moves without a Chess piece capture Draw implementation.
		if (chessBoard.checkForConditionalNoCaptureDraw()) {
			int dialogResult = -1;

			// In the HUMAN_VS_AI mode, show the draw dialog, only if the AI has just made a move.
			if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
					|| (chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK)) {
				dialogResult = JOptionPane.showConfirmDialog(
						this,
						"50 full-moves have passed without a piece capture! Do you want to declare a draw?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);
			}

			if (dialogResult == JOptionPane.YES_OPTION) {
				chessBoard.setGameResult(GameResult.NO_CAPTURE_DRAW);
				showDeclareDrawDialog();
				return true;
			}
		}

		// Three-fold repetition draw rule implementation.
		// This situation occurs when we end up with the same chess board position 3 different times
		// at any time in the game, not necessarily successively.
		if (chessBoard.checkForThreefoldRepetitionDraw()) {
			if (chessBoard.getGameResult() == GameResult.FIVEFOLD_REPETITION_DRAW) {
				String turnMessage = "Turn: "
						+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
						+ ". It is a draw.";
				turnTextPane.setText(turnMessage);

				int dialogResult = JOptionPane.showConfirmDialog(
						this,
						"It is a draw! Fivefold repetition of the same Chess board position has occurred! " +
								"Start a new game?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);

				startNewGameOrNot(dialogResult);

				return true;
			}

			int dialogResult = -1;

			// In the HUMAN_VS_AI mode, show the draw dialog, only if the AI has just made a move.
			if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
					|| (chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK)) {
				dialogResult = JOptionPane.showConfirmDialog(
						this,
						"Threefold repetition of the same Chess board position has occurred! "
								+ "Do you want to declare a draw?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);
			}

			if (JOptionPane.YES_OPTION == dialogResult) {
				chessBoard.setGameResult(GameResult.THREEFOLD_REPETITION_DRAW);
				showDeclareDrawDialog();
				return true;
			}
		}

		return false;
	}

	private void showDeclareDrawDialog() {
		String turnMessage = "Turn: "
				+ (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
				+ ". It is a draw.";
		turnTextPane.setText(turnMessage);

		int dialogResult = JOptionPane.showConfirmDialog(
				this,
				"It is a draw! Start a new game?",
				"Draw",
				JOptionPane.YES_NO_OPTION
		);

		startNewGameOrNot(dialogResult);
	}

	private void startNewGameOrNot(int dialogResult) {
		isGameOver = true;

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


	public void aiMove(AI ai) {
		Move aiMove = ai.getNextMove(chessBoard);
		System.out.println("aiMove: " + aiMove);

		makeDisplayMove(aiMove, true);

		checkForGameOver();
	}

	public void playAiVsAi() {
		AI ai1;
		if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
			Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
			ai1 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
		} else {
			ai1 = new RandomChoiceAI(Constants.WHITE);
		}

		AI ai2;
		if (gameParameters.getAi2Type() == AiType.MINIMAX_AI) {
			Evaluation evaluation2 = createEvaluation(gameParameters.getEvaluationFunction2());
			ai2 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi2MaxDepth(), Constants.BLACK, evaluation2);
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

	private void aiVsAiMove(AI ai) {
		aiMove(ai);

		super.revalidate();
		super.paint(super.getGraphics());
	}

	private void hideHintPositions() {
		if (startingPosition != null && !startingPosition.equals("")) {
			int startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
			int startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				startingPositionRow = chessBoard.getNumOfRows() - 1 - startingPositionRow;
				startingPositionColumn = chessBoard.getNumOfColumns() - 1 - startingPositionColumn;
			}
			JButton startingButton = chessButtons[startingPositionRow][startingPositionColumn];
			Color startingButtonColor = getColorByRowCol(startingPositionRow, startingPositionColumn);

			GuiUtils.changeSquareColor(startingButton, startingButtonColor);
		}
		for (String hintPosition : hintPositions) {
			int row = chessBoard.getRowFromPosition(hintPosition);
			int column = chessBoard.getColumnFromPosition(hintPosition);

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				row = chessBoard.getNumOfRows() - 1 - row;
				column = chessBoard.getNumOfColumns() - 1 - column;
			}
			JButton button = chessButtons[row][column];
			Color buttonColor = getColorByRowCol(row, column);

			GuiUtils.changeSquareColor(button, buttonColor);
		}
	}

	private Color getColorByRowCol(int row, int column) {
		Color color;
		if ((column % 2 == 1 && row % 2 == 1) || (column % 2 == 0 && row % 2 == 0)) {
			color = gameParameters.getWhiteSquareColor();
		} else {
			color = gameParameters.getBlackSquareColor();
		}
		return color;
	}

	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI).
	public void placePieceToPosition(String position, ChessPiece chessPiece) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		chessBoard.getGameBoard()[row][column] = chessPiece;

		String imagePath = GuiUtils.getImagePath(chessPiece);
		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}
		chessButtons[row][column].setIcon(pieceImage);
	}

	// It removes the given chessPiece from the board (both the data structure and the GUI).
	private void removePieceFromPosition(String position) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}

		// Our chess board pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon.
		ImageIcon imageIcon = new ImageIcon(new BufferedImage(
				GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
				GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
				BufferedImage.TYPE_INT_ARGB
		));
		chessButtons[row][column].setIcon(imageIcon);
	}

	private void redrawChessButtons() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(position, chessPiece);
			}
		}
	}

	public void placePiecesToChessBoard(String fenPosition) {
		chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition, gameParameters.getNumOfRows());

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
	}

	public void makeChessBoardSquaresEmpty() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				// Our chess board pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon.
				ImageIcon imageIcon = new ImageIcon(
						new BufferedImage(
								GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
								GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
								BufferedImage.TYPE_INT_ARGB
						)
				);
				chessButtons[i][j].setIcon(imageIcon);

				Color color = getColorByRowCol(i, j);
				chessButtons[i][j].setBackground(color);
				chessButtons[i][j].setOpaque(true);

				chessBoard.getGameBoard()[i][j] = new EmptySquare();
			}
		}
		chessBoard.setThreats();
	}

	private void enableChessButtons() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				JButton button = chessButtons[i][j];
				button.setEnabled(true);

				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = chessBoard.getNumOfRows() - 1 - i;
					column = chessBoard.getNumOfColumns() - 1 - j;
				} else {
					row = i;
					column = j;
				}

				button.addActionListener(e -> chessButtonClick(row, column, button));
			}
		}
		buttonsEnabled = true;
	}

	private void disableChessBoardSquares() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ActionListener[] actionListeners = chessButtons[i][j].getActionListeners();
				if (actionListeners.length > 0) {
					chessButtons[i][j].removeActionListener(actionListeners[0]);
				}
			}
		}
		buttonsEnabled = false;
	}

	public static void main(String[] args) {
		ButtonsGui buttonsGui = new ButtonsGui();
		buttonsGui.startNewGame();
	}

}
