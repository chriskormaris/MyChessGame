package com.chriskormaris.mychessgame.gui.frame;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

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
public class DragAndDropFrame extends ChessFrame implements MouseListener, MouseMotionListener {

	// The size of each square on the chessPanel is almost: 125 x 120
	int squareWidth;
	int squareHeight;

	JLayeredPane layeredPane;

	JLabel pieceLabel;

	int xAdjustment;
	int yAdjustment;

	boolean mouseIsPressed;

	boolean chessPanelEnabled;

	public DragAndDropFrame() {
		this(GuiConstants.TITLE);
	}

	public DragAndDropFrame(String title) {
		super(title);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) screenSize.getHeight() - 120;
		width = height + 60;

		guiPanel = new JPanel();
		turnTextPane = new JTextPane();

		gameParameters = new GameParameters();
		gameParameters.setGuiType(GuiType.DRAG_AND_DROP);
		gameParameters.setNumOfRows(Constants.DEFAULT_NUM_OF_ROWS);
		newGameParameters = new GameParameters(gameParameters);

		nextHalfMoveFenPositions = new Stack<>();

		chessBoard = new ChessBoard();

		initializeCapturedPieces();

		undoCapturedPieces = new Stack<>();
		redoCapturedPieces = new Stack<>();

		tools = new JToolBar();

		startingPosition = "";
		endingPosition = "";

		hintPositions = new HashSet<>();
		chessPanelEnabled = true;

		initializeGUI();

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			super.setIconImage(icon);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		super.setResizable(false);

		int cornerPosition = getSquareIndex(chessBoard.getNumOfRows(), chessBoard.getNumOfColumns());
		JPanel piecePanel = (JPanel) chessPanel.getComponent(cornerPosition);

		squareHeight = (int) piecePanel.getLocation().getY() / chessBoard.getNumOfRows();
		squareWidth = (int) piecePanel.getLocation().getX() / chessBoard.getNumOfColumns();

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
			SettingsFrame settings = new SettingsFrame(this, newGameParameters);
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

		super.addKeyListener(undoRedoKeyListener);
	}

	@Override
	void initializeGUI() {
		configureGuiStyle();

		// Set up the main GUI.
		guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));

		initializeTurnTextPaneBar();
		setTurnMessage();

		initializeChessPanel();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();

		setScoreMessage();
	}

	@Override
	void initializeChessPanel() {
		// Use a Layered Pane for this application

		layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(width, height));
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		//  Add a chess board to the Layered Pane on the DEFAULT layer
		chessPanel = new JPanel();
		chessPanel.setLayout(new GridLayout(chessBoard.getNumOfRows() + 2, chessBoard.getNumOfColumns() + 2));
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
			for (int j = chessBoard.getNumOfColumns() - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}

		chessPanel.add(new JLabel(""));

		// Build the Chess Board squares
		// We use a 8x8 grid, and put a JPanel with BorderLayout on each square.
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns() + 1; j++) {
				JPanel square = new JPanel(new BorderLayout());
				square.setBackground(getColorByRowCol(i, j));
				square.setFocusable(false);

				if (j == 0 || j == chessBoard.getNumOfColumns()) {
					int rankNumber = chessBoard.getNumOfRows() - i;
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
			for (int j = chessBoard.getNumOfColumns() - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}

		guiPanel.add(layeredPane, BorderLayout.CENTER);
		super.getContentPane().add(guiPanel);
	}

	@Override
	void undo() {
		if (mouseIsPressed) return;
		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
				&& !chessBoard.getPreviousHalfMoveFenPositions().isEmpty()
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& chessBoard.getPreviousHalfMoveFenPositions().size() >= 2) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				if (!chessPanelEnabled) {
					chessPanelEnabled = true;
				}

				mouseIsPressed = false;
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
			redrawChessPanel();
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
	@Override
	void redo() {
		if (mouseIsPressed) return;
		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI && !nextHalfMoveFenPositions.isEmpty()
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && nextHalfMoveFenPositions.size() >= 2) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				mouseIsPressed = false;
			}

			chessBoard.getPreviousHalfMoveFenPositions().push(FenUtils.getFenPositionFromChessBoard(chessBoard));
			undoCapturedPieces.push(capturedPieces.clone());
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
				chessBoard.getPreviousHalfMoveFenPositions().push(nextHalfMoveFenPositions.pop());
				undoCapturedPieces.push(redoCapturedPieces.pop());
			}
			Stack<String> previousHalfMoveFenPositions = chessBoard.getPreviousHalfMoveFenPositions();

			placePiecesToChessBoard(nextHalfMoveFenPositions.pop());
			redrawChessPanel();
			chessBoard.setPreviousHalfMoveFenPositions(previousHalfMoveFenPositions);
			capturedPieces = redoCapturedPieces.pop();

			resetCapturedPiecesPanel();

			resetScore();
			setScoreMessage();

			setTurnMessage();

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

	@Override
	public void startNewGame() {
		if (newGameParameters.getGuiType() == GuiType.DRAG_AND_DROP) {
			startNewGame(Constants.DEFAULT_STARTING_FEN_POSITION);
		} if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
			super.dispose();
			ChessFrame buttonsFrame = new ButtonsFrame();
			buttonsFrame.newGameParameters = newGameParameters;
			buttonsFrame.startNewGame();
		}
	}

	@Override
	public void startNewGame(String fenPosition) {
		System.out.println("Starting new game!");

    	newGameParameters.setNumOfRows(Constants.DEFAULT_NUM_OF_ROWS);
    	gameParameters = new GameParameters(newGameParameters);

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}

		guiPanel.removeAll();

		restoreDefaultValues(fenPosition);

		initializeGUI();

		guiPanel.revalidate();
		guiPanel.repaint();
		super.paint(getGraphics());
		super.repaint();

		redrawChessPanel();
		revalidate();

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

	private void redrawChessPanel() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				removePieceFromPosition(position);
				placePieceToPosition(position, chessPiece);
			}
		}
	}

	// Restores all the default values.
	@Override
	void restoreDefaultValues(String fenPosition) {
		if (fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			chessBoard = new ChessBoard();
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

	@Override
	void makeDisplayMove(Move move, boolean isAiMove) {
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
				|| startingPiece.getAllegiance() == Allegiance.BLACK && rowEnd == 7)) {
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
			removePieceFromPosition(position);
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

	@Override
	void hideHintPositions() {
		if (startingPosition != null && !startingPosition.isEmpty()) {
			int startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
			int startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				startingPositionRow = chessBoard.getNumOfRows() - 1 - startingPositionRow;
				startingPositionColumn = chessBoard.getNumOfColumns() - 1 - startingPositionColumn;
			}

			startingPositionRow += 1;
			startingPositionColumn += 1;

			int startingIndex = getSquareIndex(startingPositionRow, startingPositionColumn);
			Component startingComponent = chessPanel.getComponent(startingIndex);
			startingComponent.setBackground(getColorByRowCol(startingPositionRow, startingPositionColumn));
		}
		for (String hintPosition : hintPositions) {
			int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
			int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
					&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				hintPositionRow = chessBoard.getNumOfRows() - 1 - hintPositionRow;
				hintPositionColumn = chessBoard.getNumOfColumns() - 1 - hintPositionColumn;
			}

			int hintPositionIndex = getSquareIndex(hintPositionRow + 1, hintPositionColumn + 1);
			Component hintPositionComponent = chessPanel.getComponent(hintPositionIndex);
			hintPositionComponent.setBackground(getColorByRowCol(hintPositionRow, hintPositionColumn));
		}
	}

	public void addChessPiece(ImageIcon chessPiece, int row, int column) {
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}

		int squareIndex = getSquareIndex(row, column);
		squareIndex += 11;  // skip the first row (10 JLabels) + 1 column

		try {
			JPanel piecePanel = (JPanel) chessPanel.getComponent(squareIndex);
			JLabel pieceLabel = new JLabel(chessPiece);
			piecePanel.add(pieceLabel);

			piecePanel.setBackground(getColorByRowCol(row, column));
		} catch (ClassCastException ignored) {
		}
	}

	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI).
	@Override
	public void placePieceToPosition(String position, ChessPiece chessPiece) {
		int column = chessBoard.getColumnFromPosition(position);
		int row = chessBoard.getRowFromPosition(position);
		chessBoard.getGameBoard()[row][column] = chessPiece;

		String imagePath = GuiUtils.getImagePath(chessPiece);
		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE);
		addChessPiece(pieceImage, row, column);
	}

	// It removes the given chessPiece from the board (both the data structure and the JFrame).
	@Override
	void removePieceFromPosition(String position) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}

		int threshold = 4;
		Component component = chessPanel.findComponentAt(
				(squareWidth + threshold) * (column + 1),
				(squareHeight + threshold) * (row + 1)
		);

		try {
			pieceLabel = (JLabel) component;
			pieceLabel.setVisible(false);
			layeredPane.remove(pieceLabel);
		} catch (ClassCastException ignored) {
		}
	}

	@Override
	void placePiecesToChessBoard(String fenPosition) {
		chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
	}

	private int getSquareIndex(int row, int column) {
		return row * (chessBoard.getNumOfRows() + 2) + column;
	}

	private int getSquareRow(MouseEvent event) {
		return event.getY() / squareHeight;
	}

	private int getSquareColumn(MouseEvent event) {
		return event.getX() / squareWidth;
	}

	// Add the selected chess piece to the dragging layer, so it can be moved.
	@Override
	public void mousePressed(MouseEvent event) {
		if (mouseIsPressed || !chessPanelEnabled) return;

		// get the component where the user pressed; iff that's not a panel,
		// we'll put it on the dragging layer.
		Component component = chessPanel.findComponentAt(event.getX(), event.getY());
		try {
			pieceLabel = (JLabel) component;
		} catch (ClassCastException ex) {
			return;
		}

		int row = getSquareRow(event);
		int column = getSquareColumn(event);

		// If the rank and file JLabels are pressed, then return.
		if (row == 0 || row == chessBoard.getNumOfRows() + 1
				|| column == 0 || column == chessBoard.getNumOfColumns() + 1) {
			return;
		}

		int startingRow = row;
		int startingColumn = column;
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			startingRow = (chessBoard.getNumOfRows() + 2) - 1 - row;
			startingColumn = (chessBoard.getNumOfColumns() + 2) - 1 - column;
		}

		startingPosition = chessBoard.getPositionByRowCol(startingRow - 1, startingColumn - 1);
		ChessPiece chessPiece = chessBoard.getChessPieceFromPosition(startingPosition);

		if (chessPiece instanceof EmptySquare) return;

		mouseIsPressed = true;

		hintPositions = chessBoard.getNextPositions(startingPosition);

		if (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)) {

			int startingIndex = getSquareIndex(row, column);
			Component startingComponent = chessPanel.getComponent(startingIndex);
			startingComponent.setBackground(Color.CYAN);

			// Display the hint positions.
			for (String hintPosition : hintPositions) {
				int hintPositionRow = chessBoard.getRowFromPosition(hintPosition);
				int hintPositionColumn = chessBoard.getColumnFromPosition(hintPosition);

				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
						&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					hintPositionRow = chessBoard.getNumOfRows() - 1 - hintPositionRow;
					hintPositionColumn = chessBoard.getNumOfColumns() - 1 - hintPositionColumn;
				}

				int hintPositionIndex = getSquareIndex(hintPositionRow + 1, hintPositionColumn + 1);
				Component hintPositionComponent = chessPanel.getComponent(hintPositionIndex);
				ChessPiece hintPositionPiece = chessBoard.getChessPieceFromPosition(hintPosition);

				if (hintPositionPiece.getAllegiance() != Allegiance.NONE
						|| chessBoard.getEnPassantPosition().equals(hintPosition) && chessPiece instanceof Pawn) {
					hintPositionComponent.setBackground(Color.RED);
				} else if (chessPiece instanceof Pawn &&
						(chessPiece.getAllegiance() == Allegiance.WHITE && hintPositionRow == 0
								|| chessPiece.getAllegiance() == Allegiance.BLACK
								&& hintPositionRow == chessBoard.getNumOfRows() - 1)) {
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
		xAdjustment = componentLocation.x - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE * 2/3;
		yAdjustment = componentLocation.y - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE * 2/3;
		pieceLabel.setLocation(event.getX() + xAdjustment, event.getY() + yAdjustment);

		// Evidently, this removes it from the default layer also.
		layeredPane.add(pieceLabel, JLayeredPane.DRAG_LAYER);
		layeredPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	//  Move the chess piece around.
	@Override
	public void mouseDragged(MouseEvent event) {
		removeKeyListener(undoRedoKeyListener);
		if (!mouseIsPressed || pieceLabel == null || startingPosition.isEmpty()) return;

		// The drag location should be within the bounds of the Chess board.

		int x = event.getX() + xAdjustment;
		int xMax = layeredPane.getWidth() - pieceLabel.getWidth();
		x = Math.min(x, xMax);
		x = Math.max(x, 0);

		int y = event.getY() + yAdjustment;
		int yMax = layeredPane.getHeight() - pieceLabel.getHeight();
		y = Math.min(y, yMax);
		y = Math.max(y, 0);

		// Evidently this works for whatever layer contains the piece.
		// Also, the layout manager of its new home is evidently not the same as lower layers.
		pieceLabel.setLocation(x, y);
	}

	//  Drop the chess piece back onto the Chess board.
	@Override
	public void mouseReleased(MouseEvent event) {
		layeredPane.setCursor(null);

		addKeyListener(undoRedoKeyListener);

		if (!mouseIsPressed || pieceLabel == null || startingPosition.isEmpty()) return;

		mouseIsPressed = false;

		int row = getSquareRow(event);
		int column = getSquareColumn(event);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			row = (chessBoard.getNumOfRows() + 2) - 1 - row;
			column = (chessBoard.getNumOfColumns() + 2) - 1 - column;
		}

		endingPosition = chessBoard.getPositionByRowCol(row - 1, column - 1);

		// Make sure the Chess piece is no longer painted on the layered pane.
		pieceLabel.setVisible(false);
		layeredPane.remove(pieceLabel);

		// The drop location should be within the bounds of the Chess board.
		int xMax = layeredPane.getWidth() - pieceLabel.getWidth();
		int x = Math.min(event.getX(), xMax);
		x = Math.max(x, 0);

		int yMax = layeredPane.getHeight() - pieceLabel.getHeight();
		int y = Math.min(event.getY(), yMax);
		y = Math.max(y, 0);

		int rowStart = chessBoard.getRowFromPosition(startingPosition);
		int columnStart = chessBoard.getColumnFromPosition(startingPosition);
		ChessPiece startingChessPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		hideHintPositions();

		if ((startingChessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| startingChessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays()
				&& (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN))
				&& hintPositions.contains(endingPosition)) {
			Component component = chessPanel.findComponentAt(x, y);
			Container parent;
			if (component instanceof JLabel) {
				// If there's a piece on the square already; remove it from the panel.
				parent = component.getParent();
				parent.remove(0);
			} else {
				parent = (Container) component;
			}

			Move move = new Move(startingPosition, endingPosition);
			makeDisplayMove(move, false);

			if (checkForGameOver()) return;

			if (gameParameters.isEnableSounds()) {
				SoundUtils.playMoveSound();
			}

			hintPositions.clear();

			parent.validate();

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}
			if (redoItem != null) {
				redoItem.setEnabled(false);
			}

			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
				aiMove(ai);
			}
		} else {
			placePieceToPosition(startingPosition, startingChessPiece);
		}

		startingPosition = "";
		pieceLabel = null;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	public static void main(String[] args) {
		ChessFrame dragAndDropFrame = new DragAndDropFrame();
		dragAndDropFrame.startNewGame();
	}

}
