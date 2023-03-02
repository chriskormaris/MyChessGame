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
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;
import com.chriskormaris.mychessgame.gui.util.GuiUtils;
import com.chriskormaris.mychessgame.gui.util.ResourceLoader;
import com.chriskormaris.mychessgame.gui.util.SoundUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static com.chriskormaris.mychessgame.api.util.Constants.DEFAULT_NUM_OF_ROWS;
import static com.chriskormaris.mychessgame.api.util.Constants.NUM_OF_COLUMNS;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.FIRST_TURN_TEXT;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.TITLE;
import static com.chriskormaris.mychessgame.gui.util.GuiConstants.ZERO_SCORE_TEXT;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Example showing the use of a JLayeredPane to implement dragging an object
 * across a JPanel containing other objects.
 * <p>
 * Basic idea: Create a JLayeredPane as a container, then put the JPanel containing
 * the application's components or whatever in the JLayeredPane.DEFAULT_LAYER layer of that layered pane.
 * The code is going to drag a JComponent object by calling JComponent.setPosition(x, y)
 * on the component. When a mouse is clicked on the panel to start the dragging, put the
 * component on the drag layer of the layered pane; as it is dragged, continue to call
 * setPosition to move it. When the mouse is released, use the x.y position of the release
 * to decide what to do with it next.
 */
public class GUI extends JFrame implements MouseListener, MouseMotionListener {


	GameParameters gameParameters;
	GameParameters newGameParameters;

	int width;
	int height;

	// The size of each square on the chessPanel is almost: 125 x 120
	int squareWidth;
	int squareHeight;

	JTextPane turnTextPane;

	// These stacks of "String" objects are used to handle the "undo" and "redo" functionality.
	Stack<String> undoFenPositions;
	Stack<String> redoFenPositions;

	// The length of this array is 30 elements.
	// The first 15 elements represent White captured pieces and are capital chars.
	// The last 15 elements represent Black captured pieces and are lowercase chars.
	// The elements could also be '-', which is a placeholder for future captured pieces.
	char[] capturedPieces;

	// These stacks of "char" arrays are used to handle the "undo" and "redo" functionality.
	Stack<char[]> undoCapturedPieces;
	Stack<char[]> redoCapturedPieces;

	// The position (0, 0) of the "chessBoard.getGameBoard()" is the upper left button
	// of the JButton array "chessBoardButtons".
	// The position (gameParameters.getNumOfRows()-1, 0) of the "chessBoard.getGameBoard()" is the lower left button
	// of the JButton array "chessBoardButtons".
	ChessBoard chessBoard;

	// This variable is used for the implementation of "Human Vs AI".
	AI ai;

	// These stacks of "String" objects are used to check for a threefold repetition of a chess board position.
	Stack<String> undoHalfMoveFenPositions;
	Stack<String> redoHalfMoveFenPositions;

	JPanel gui;

	JToolBar tools;
	JPanel capturedPiecesPanel;

	// 30 captured pieces at maximum, plus 1 label for displaying the score = 31 labels size.
	JLabel[] capturedPiecesImages;

	JLayeredPane layeredPane;
	JPanel chessPanel;

	JLabel pieceLabel;

	int xAdjustment;
	int yAdjustment;

	String startingPosition;
	String endingPosition;

	boolean mouseIsPressed;

	Set<String> hintPositions;

	boolean chessPanelEnabled;
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

	public GUI() {
		super(TITLE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) screenSize.getHeight() - 120;
		width = height + 60;

		gui = new JPanel();
		turnTextPane = new JTextPane();

		gameParameters = new GameParameters();
		gameParameters.setGuiType(GuiType.DRAG_AND_DROP);
		newGameParameters = new GameParameters(gameParameters);

		undoFenPositions = new Stack<>();
		redoFenPositions = new Stack<>();

		chessBoard = new ChessBoard();

		initializeCapturedPieces();

		undoCapturedPieces = new Stack<>();
		redoCapturedPieces = new Stack<>();

		undoHalfMoveFenPositions = new Stack<>();
		redoHalfMoveFenPositions = new Stack<>();

		tools = new JToolBar();

		startingPosition = "";
		endingPosition = "";

		hintPositions = new HashSet<>();
		chessPanelEnabled = true;

		initializeGUI();

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			setIconImage(icon);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		int cornerPosition = getSquareIndex(8, 8);
		// System.out.println("chessPanel.getComponentCount(): " + chessPanel.getComponentCount());
		JPanel piecePanel = (JPanel) chessPanel.getComponent(cornerPosition);
		// System.out.println("piecePanel.getLocation(): " + piecePanel.getLocation());

		squareHeight = (int) piecePanel.getLocation().getY() / DEFAULT_NUM_OF_ROWS;
		squareWidth = (int) piecePanel.getLocation().getX() / NUM_OF_COLUMNS;
		// System.out.println("squareHeight: " + squareHeight);
		// System.out.println("squareWidth: " + squareWidth);

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
			SettingsWindow settings = new SettingsWindow(this, gameParameters, newGameParameters);
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
					GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
					GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE,
					Image.SCALE_SMOOTH
			);
			ImageIcon imageIcon = new ImageIcon(dImg);

			JOptionPane.showMessageDialog(this, label, "About", JOptionPane.PLAIN_MESSAGE, imageIcon);
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

		setJMenuBar(menuBar);

		addKeyListener(undoRedoKeyListener);
	}

	private void initializeGUI() {
		// Set up the main GUI.
		gui.setLayout(new BoxLayout(gui, BoxLayout.Y_AXIS));

		initializeTurnTextPaneBar();
		setTurnMessage();

		initializeChessPanel();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();

		setScoreMessage();
	}

	private void initializeChessPanel() {
		// Use a Layered Pane for this application

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(width, height));
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		// getContentPane().add(layeredPane);

		// debug
		// LayoutManager lm = layeredPane.getLayout();
		// System.out.println("Layered pane layout name is " + (lm == null ? "<null>" : lm.getClass().getName()));

		//  Add a chess board to the Layered Pane on the DEFAULT layer
		chessPanel = new JPanel();
		// chessPanel.setLayout(new GridLayout(DEFAULT_NUM_OF_ROWS, NUM_OF_COLUMNS));
		chessPanel.setLayout(new GridLayout(DEFAULT_NUM_OF_ROWS + 2, NUM_OF_COLUMNS + 2));
		// chessPanel.setBorder(new LineBorder(Color.BLACK));
		chessPanel.setPreferredSize(new Dimension(width, height));
		chessPanel.setBounds(0, 0, width, height);
		chessPanel.setFocusable(false);

		layeredPane.add(chessPanel, JLayeredPane.DEFAULT_LAYER);
		layeredPane.setFocusable(false);


		// fill the chess panel
		chessPanel.add(new JLabel(""));

		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}

		chessPanel.add(new JLabel(""));

		//  Build the Chess Board squares
		// We use a 8x8 grid, and put a JPanel with BorderLayout on each square.
		for (int i = 0; i < DEFAULT_NUM_OF_ROWS; i++) {
			// for (int j = 0; j < NUM_OF_COLUMNS; j++) {
			for (int j = 0; j < NUM_OF_COLUMNS + 1; j++) {
				JPanel square = new JPanel(new BorderLayout());
				square.setBackground((i + j) % 2 == 0
						? gameParameters.getBlackSquareColor()
						: gameParameters.getWhiteSquareColor());
				square.setFocusable(false);

				if (j == 0 || j == NUM_OF_COLUMNS) {
					int rankNumber = gameParameters.getNumOfRows() - i;
					if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
							&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
						rankNumber = i + 1;
					}
					chessPanel.add(new JLabel(String.valueOf(rankNumber), SwingConstants.CENTER));
				} else {
					chessPanel.add(square);
				}
				if (j == 0) {
					chessPanel.add(square);
				}
			}
		}

		// fill the bottom row
		chessPanel.add(new JLabel(""));

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j = NUM_OF_COLUMNS - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}

		gui.add(layeredPane, BorderLayout.CENTER);
		getContentPane().add(gui);
	}

	private void initializeTurnTextPaneBar() {
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

	private void exportToGif() {
		String gifName = JOptionPane.showInputDialog(
				this,
				"Please type the exported \".gif\" file name:",
				"chess_board.gif"
		);

		if (gifName != null) {
			BufferedImage bi = new BufferedImage(
					chessPanel.getSize().width,
					chessPanel.getSize().height,
					BufferedImage.TYPE_INT_ARGB
			);
			Graphics g = bi.createGraphics();
			chessPanel.paint(g);
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

	private void undo() {
		if (!undoFenPositions.isEmpty()) {
			System.out.println("Undo is pressed!");

			chessPanelEnabled = true;

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				mouseIsPressed = false;
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
			if (!chessPanelEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				chessPanelEnabled = true;
			}

			placePiecesToChessBoard(fenPosition);
			redrawChessPanel();

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
	private void redo() {
		if (!redoFenPositions.isEmpty()) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				mouseIsPressed = false;
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

			if (!chessPanelEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				chessPanelEnabled = true;
			}

			if (redoFenPositions.isEmpty()) {
				redoItem.setEnabled(false);
			}

			placePiecesToChessBoard(fenPosition);
			redrawChessPanel();

			System.out.println();
			System.out.println(chessBoard);

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}

			checkForGameOver();
		}
	}

	private void updateCapturedPiecesPanel() {
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

	private void initializeCapturedPiecesPanel() {
		if (capturedPiecesPanel != null) {
			gui.remove(capturedPiecesPanel);
		}
		capturedPiecesPanel = new JPanel();
		gui.add(capturedPiecesPanel, BorderLayout.SOUTH);
	}

	private void initializeCapturedPiecesImages() {
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

	private void updateCapturedPieces(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			int index = Math.min(chessBoard.getWhiteCapturedPiecesCounter(), 14);
			capturedPieces[index] = chessPiece.getPieceChar();
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			int index = Math.min(chessBoard.getBlackCapturedPiecesCounter(), 14);
			index = 30 - index - 1;
			capturedPieces[index] = chessPiece.getPieceChar();
		}
	}

	private void addCapturedPieceImage(ChessPiece endSquare) {
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

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CAPTURED_CHESS_PIECE_PIXEL_SIZE);

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

	private void setTurnMessage() {
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

	private void setScoreMessage() {
		if (chessBoard.getScore() > 0) {
			capturedPiecesImages[15].setText("White: +" + chessBoard.getScore());
		} else if (chessBoard.getScore() < 0) {
			capturedPiecesImages[15].setText("Black: +" + (-chessBoard.getScore()));
		} else {
			capturedPiecesImages[15].setText(ZERO_SCORE_TEXT);
		}
	}

	private void initializeCapturedPieces() {
		capturedPieces = new char[30];
		for (int i = 0; i < 30; i++) {
			capturedPieces[i] = '-';
		}
	}

	public void startNewGame() {
		startNewGame(Constants.DEFAULT_STARTING_FEN_POSITION);
	}

	public void startNewGame(String fenPosition) {
		System.out.println("Starting new game!");

		gameParameters = new GameParameters(newGameParameters);

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}

		gui.removeAll();

		initializeGUI();

		gui.revalidate();
		gui.repaint();
		paint(getGraphics());
		repaint();

		restoreDefaultValues();

		placePiecesToChessBoard(fenPosition);
		redrawChessPanel();
		revalidate();

		initializeAI();

		chessBoard.setThreats();

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

	// Gets called after the human player makes a move. It makes a Minimax AI move.
	public void aiMove(AI ai) {
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

	public void playAiVsAi() {
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
			// System.out.println(turnTextPane.getText());
			aiVsAiMove(ai1);

			if (!isGameOver) {
				// System.out.println(turnTextPane.getText());

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
		undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
		undoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

		aiMove(ai);

		setTurnMessage();
		setScoreMessage();

		this.revalidate();
		this.paint(getGraphics());
	}

	private void initializeAI() {
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

	private void redrawChessPanel() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				removePieceFromPosition(position);
				placePieceToPosition(position, chessPiece);
			}
		}
	}

	// Restores all the default values.
	private void restoreDefaultValues() {
		chessBoard = new ChessBoard(DEFAULT_NUM_OF_ROWS);

		startingPosition = "";
		endingPosition = "";

		undoFenPositions.clear();
		redoFenPositions.clear();

		initializeCapturedPieces();

		undoCapturedPieces.clear();
		redoCapturedPieces.clear();

		undoHalfMoveFenPositions.clear();
		redoHalfMoveFenPositions.clear();

		mouseIsPressed = false;

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

		chessPanelEnabled = true;
		isGameOver = false;

		setTurnMessage();
	}

	public void addChessPiece(ImageIcon chessPiece, int row, int column) {
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = DEFAULT_NUM_OF_ROWS - 1 - row;
		}

		int squareIndex = getSquareIndex(row, column);
		squareIndex += 11; // skip the first row (10 JLabels) + 1 column

		try {
			JPanel piecePanel = (JPanel) chessPanel.getComponent(squareIndex);
			JLabel pieceLabel = new JLabel(chessPiece);
			piecePanel.add(pieceLabel);

			piecePanel.setBackground((row + column) % 2 == 0
					? gameParameters.getBlackSquareColor()
					: gameParameters.getWhiteSquareColor());
		} catch (ClassCastException ignored) {
		}
	}

	public void placePiecesToChessBoard() {
		placePiecesToChessBoard(Constants.DEFAULT_STARTING_FEN_POSITION);
	}

	public void placePiecesToChessBoard(String fenPosition) {
		chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
	}

	public void makeChessBoardSquaresEmpty() {
		for (int i = 0; i < gameParameters.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				String position = chessBoard.getPositionByRowCol(i, j);
				removePieceFromPosition(position);
				chessBoard.getGameBoard()[i][j] = new EmptySquare();
			}
		}
	}

	public void placePieceToPosition(String position, ChessPiece chessPiece) {
		String imagePath = GuiUtils.getImagePath(chessPiece);

		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE);

		int column = chessBoard.getColumnFromPosition(position);
		int row = chessBoard.getRowFromPosition(position);

		addChessPiece(pieceImage, row, column);

		chessBoard.getGameBoard()[row][column] = chessPiece;
	}

	private int getSquareIndex(int row, int column) {
		return row * (DEFAULT_NUM_OF_ROWS + 2) + column;
	}

	private int getSquareRow(MouseEvent e) {
		return e.getY() / squareHeight;
	}

	private int getSquareColumn(MouseEvent e) {
		return e.getX() / squareWidth;
	}

	private String getSquarePosition(MouseEvent e) {
		int row = getSquareRow(e) - 1;
		int column = getSquareColumn(e) - 1;
		return chessBoard.getPositionByRowCol(row, column);
	}

	// It removes the given chessPiece from the board (both the data structure and the JFrame).
	private void removePieceFromPosition(String position) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = DEFAULT_NUM_OF_ROWS - 1 - row;
		}

		Component component = chessPanel.findComponentAt(squareWidth * (column + 1), squareHeight * (row + 1));

		try {
			pieceLabel = (JLabel) component;
			pieceLabel.setVisible(false);
			layeredPane.remove(pieceLabel);
		} catch (ClassCastException ignored) {
		}
	}

	private void makeDisplayMove(Move move, boolean isAiMove) {
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
				|| startingPiece.getAllegiance() == Allegiance.BLACK && rowEnd == 7)) {
			if (isAiMove) {
				chessBoard.automaticPawnPromotion(startingPiece, positionEnd, true);

				ChessPiece promotedPiece = chessBoard.getGameBoard()[rowEnd][columnEnd];
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
				removePieceFromPosition(positionEnd);
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
			updateCapturedPieces(chessBoard.getCapturedEnPassantPiece());
			addCapturedPieceImage(chessBoard.getCapturedEnPassantPiece());
		}

		for (String position : chessBoard.getPositionsToRemove()) {
			removePieceFromPosition(position);
		}
		for (String position : chessBoard.getPiecesToPlace().keySet()) {
			removePieceFromPosition(position);
			ChessPiece chessPieceToPlace = chessBoard.getPiecesToPlace().get(position);
			placePieceToPosition(position, chessPieceToPlace);
		}
		chessBoard.getPositionsToRemove().clear();
		chessBoard.getPiecesToPlace().clear();

		chessBoard.setThreats();

		// Store the chess board of the HalfMove that was just made.
		undoHalfMoveFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));
	}

	public boolean checkForGameOver() {

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
						this,
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
						this,
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
						this,
						"Stalemate! No legal moves for White exist. Start a new game?",
						"Draw",
						JOptionPane.YES_NO_OPTION
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
						this,
						"Stalemate! No legal moves for Black exist. Start a new game?",
						"Draw",
						JOptionPane.YES_NO_OPTION
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
					this,
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
						this,
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
						this,
						"Threefold repetition of the same chess board position has occurred! "
								+ "Do you want to declare a draw?",
						"Draw",
						JOptionPane.YES_NO_OPTION
				);
			}

			// System.out.println("dialogResult: " + dialogResult);
			if (JOptionPane.YES_OPTION == dialogResult) {
				chessBoard.setGameResult(GameResult.THREEFOLD_REPETITION_DRAW);
				showDeclareDrawDialog();
				return true;
			}
		}

		return false;
	}

	// We are comparing FEN positions, but without checking the half-move clock and the full-move number.
	private boolean checkForThreefoldRepetitionDraw() {
		if (!undoHalfMoveFenPositions.isEmpty()) {
			int N = undoHalfMoveFenPositions.size();
			String lastHalfMoveFenPosition = undoHalfMoveFenPositions.get(N - 1);
			lastHalfMoveFenPosition = FenUtils.skipCounters(lastHalfMoveFenPosition);
			int numOfRepeats = 0;
			for (int i = N - 2; i >= 0; i--) {
				// Skip the last iteration, if the number of repeats found is less ore equal to 1.
				// Also, skip the second to last iteration, if the number of repeats found is 0.
				if (!(numOfRepeats <= 1 && i == 0 || numOfRepeats == 0 && i == 1)) {
					String otherHalfMoveFenPosition = undoHalfMoveFenPositions.get(i);
					otherHalfMoveFenPosition = FenUtils.skipCounters(otherHalfMoveFenPosition);
					if (lastHalfMoveFenPosition.equals(otherHalfMoveFenPosition)) {
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

	private void showDeclareDrawDialog() {
		String turnMessage = "Move number: "
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

			chessPanelEnabled = false;
		}
	}

	private void hideHintPositions() {
		int startingRow = chessBoard.getRowFromPosition(startingPosition);
		int startingColumn = chessBoard.getColumnFromPosition(startingPosition);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			startingRow = DEFAULT_NUM_OF_ROWS - 1 - startingRow;
		}

		startingRow += 1;
		startingColumn += 1;

		int startingIndex = getSquareIndex(startingRow, startingColumn);
		Component startingComponent = chessPanel.getComponent(startingIndex);
		startingComponent.setBackground((startingRow + startingColumn) % 2 == 0
				? gameParameters.getBlackSquareColor()
				: gameParameters.getWhiteSquareColor());

		for (String hintPosition : hintPositions) {
			int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
			int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				hintPositionRow = DEFAULT_NUM_OF_ROWS - 1 - hintPositionRow;
			}

			int hintPositionIndex = getSquareIndex(hintPositionRow + 1, hintPositionColumn + 1);
			Component hintPositionComponent = chessPanel.getComponent(hintPositionIndex);
			hintPositionComponent.setBackground((hintPositionRow + hintPositionColumn) % 2 == 0
					? gameParameters.getBlackSquareColor()
					: gameParameters.getWhiteSquareColor());
		}
	}

	// Add the selected chess piece to the dragging layer, so it can be moved.
	@Override
	public void mousePressed(MouseEvent e) {
		if (!chessPanelEnabled) return;

		// get the component where the user pressed; iff that's not a panel,
		// we'll put it on the dragging layer.
		Component component = chessPanel.findComponentAt(e.getX(), e.getY());
		try {
			pieceLabel = (JLabel) component;
		} catch (ClassCastException ex) {
			return;
		}

		// System.out.println("e.getY(): " + e.getY());
		// System.out.println("e.getX(): " + e.getX());

		int row = getSquareRow(e);
		int column = getSquareColumn(e);
		// System.out.println("row: " + row);
		// System.out.println("column: " + column);

		// If the rank and file JLabels are pressed, then return.
		if (row == 0 || row == DEFAULT_NUM_OF_ROWS + 1 || column == 0 || column == NUM_OF_COLUMNS + 1) return;

		int startingRow = row;
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			startingRow = (DEFAULT_NUM_OF_ROWS + 2) - 1 - row;
		}

		mouseIsPressed = true;
		startingPosition = chessBoard.getPositionByRowCol(startingRow - 1, column - 1);
		// System.out.println("starting position: " + startingPosition);

		hintPositions = chessBoard.getNextPositions(startingPosition);

		ChessPiece chessPiece = chessBoard.getChessPieceFromPosition(startingPosition);

		if (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)) {

			int startingIndex = getSquareIndex(row, column);
			Component startingComponent = chessPanel.getComponent(startingIndex);
			startingComponent.setBackground(Color.CYAN);

			for (String hintPosition : hintPositions) {
				int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
				int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					hintPositionRow = DEFAULT_NUM_OF_ROWS - 1 - hintPositionRow;
				}

				int hintPositionIndex = getSquareIndex(hintPositionRow + 1, hintPositionColumn + 1);
				Component hintPositionComponent = chessPanel.getComponent(hintPositionIndex);

				ChessPiece hintPositionPiece = chessBoard.getChessPieceFromPosition(hintPosition);
				if (hintPositionPiece.getAllegiance() != Allegiance.EMPTY
						|| chessBoard.getEnPassantPosition().equals(hintPosition) && chessPiece instanceof Pawn) {
					hintPositionComponent.setBackground(Color.RED);
				} else if (chessPiece instanceof Pawn &&
						(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == 0
								|| chessPiece.getAllegiance() == Allegiance.BLACK
								&& hintPositionRow == gameParameters.getNumOfRows() - 1)) {
					hintPositionComponent.setBackground(Color.GREEN);
				} else if (hintPositionPiece instanceof EmptySquare) {
					hintPositionComponent.setBackground(Color.BLUE);
				}
			}
		}

		// Get the location of the panel containing the image panel, i.e.,
		// the square's panel. we adjust the location to which we move the
		// piece by this amount so the piece doesn't 'snap to' the cursor location.
		// Point parentLocation = component.getParent().getLocation();

		Point componentLocation = component.getLocation();
		xAdjustment = componentLocation.x - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE;
		// xAdjustment = parentLocation.x - e.getX();
		yAdjustment = componentLocation.y - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE;
		// yAdjustment = parentLocation.y - e.getY();
		//chessPiece = (JLabel) c;  // change2 - comment out
		pieceLabel.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);

		// Evidently, this removes it from the default layer also.
		layeredPane.add(pieceLabel, JLayeredPane.DRAG_LAYER);
		layeredPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	//  Move the chess piece around
	@Override
	public void mouseDragged(MouseEvent me) {
		if (pieceLabel == null || startingPosition.equals("")) return;

		removeKeyListener(undoRedoKeyListener);

		//  The drag location should be within the bounds of the chess board

		int x = me.getX() + xAdjustment;
		int xMax = layeredPane.getWidth() - pieceLabel.getWidth();
		x = Math.min(x, xMax);
		x = Math.max(x, 0);

		int y = me.getY() + yAdjustment;
		int yMax = layeredPane.getHeight() - pieceLabel.getHeight();
		y = Math.min(y, yMax);
		y = Math.max(y, 0);

		pieceLabel.setLocation(x, y);   // evidently this works for whatever layer contains the piece.
		// also, the layout manager of its new home is evidently not the same as lower layers.
	}

	//  Drop the chess piece back onto the chess board
	@Override
	public void mouseReleased(MouseEvent e) {
		layeredPane.setCursor(null);

		addKeyListener(undoRedoKeyListener);

		if (pieceLabel == null || startingPosition.equals("")) return;

		mouseIsPressed = false;

		int row = getSquareRow(e);
		int column = getSquareColumn(e);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = (DEFAULT_NUM_OF_ROWS + 2) - 1 - row;
		}

		endingPosition = chessBoard.getPositionByRowCol(row - 1, column - 1);
		// System.out.println("ending position: " + endingPosition);

		//  Make sure the chess piece is no longer painted on the layered pane
		pieceLabel.setVisible(false);
		layeredPane.remove(pieceLabel);

		//  The drop location should be within the bounds of the chess board
		int xMax = layeredPane.getWidth() - pieceLabel.getWidth();
		int x = Math.min(e.getX(), xMax);
		x = Math.max(x, 0);

		int yMax = layeredPane.getHeight() - pieceLabel.getHeight();
		int y = Math.min(e.getY(), yMax);
		y = Math.max(y, 0);

		int rowStart = chessBoard.getRowFromPosition(startingPosition);
		int columnStart = chessBoard.getColumnFromPosition(startingPosition);
		ChessPiece startingChessPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		// System.out.println(hintPositions);

		hideHintPositions();

		if ((startingChessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| startingChessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN))
				&& hintPositions.contains(endingPosition)) {
			if (undoItem != null) {
				undoItem.setEnabled(true);
			}
			if (redoItem != null) {
				redoItem.setEnabled(false);
			}

			Component component = chessPanel.findComponentAt(x, y);
			Container parent;
			if (component instanceof JLabel) {
				parent = component.getParent();  // there's a piece on the square already; remove it from the panel.
				parent.remove(0);
			} else {
				parent = (Container) component;
			}

			undoFenPositions.push(FenUtils.getFenPositionFromChessBoard(chessBoard));

			undoCapturedPieces.push(Utilities.copyCharArray(capturedPieces));

			redoFenPositions.clear();
			redoHalfMoveFenPositions.clear();
			redoCapturedPieces.clear();

			Move move = new Move(startingPosition, endingPosition);
			makeDisplayMove(move, false);

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

			chessBoard.setHalfMoveNumber(chessBoard.getHalfMoveNumber() + 1);
			chessBoard.setPlayer(chessBoard.getNextPlayer());

			setTurnMessage();
			setScoreMessage();

			hintPositions.clear();

			System.out.println();
			System.out.println(chessBoard);

			parent.validate();

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
				makeDisplayMove(ai.getNextMove(chessBoard), true);

				if (checkForGameOver()) return;

				chessBoard.setHalfMoveNumber(chessBoard.getHalfMoveNumber() + 1);
				chessBoard.setPlayer(chessBoard.getNextPlayer());

				setTurnMessage();
				setScoreMessage();

				System.out.println();
				System.out.println(chessBoard);
			}
		} else {
			placePieceToPosition(startingPosition, startingChessPiece);
		}

		startingPosition = "";
		pieceLabel = null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.startNewGame();
	}

}
