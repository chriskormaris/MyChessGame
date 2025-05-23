package com.chriskormaris.mychessgame.gui.frame;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.square.Bishop;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import com.chriskormaris.mychessgame.api.square.Knight;
import com.chriskormaris.mychessgame.api.square.Queen;
import com.chriskormaris.mychessgame.api.square.Rook;
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
import java.io.IOException;
import java.util.HashSet;

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
		height = (int) screenSize.getHeight() - 150;
		width = height + 90;

		gameParameters = new GameParameters();
		gameParameters.setGuiType(GuiType.DRAG_AND_DROP);
		gameParameters.setNumOfRows(Constants.DEFAULT_NUM_OF_ROWS);
		newGameParameters = new GameParameters(gameParameters);

		chessPanelEnabled = true;

		initializeGUI();

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			super.setIconImage(icon);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
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
	}

	@Override
	void initializeGUI() {
		configureGuiStyle();

		// Set up the main GUI.
		guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));

		initializeMoveTextPaneBar();
		setMoveText();

		initializeChessPanel();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();

		setScoreAndTimeText();
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
		if (flipBoard) {
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
					if (flipBoard) {
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

		if (flipBoard) {
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
		if (!undoChessBoards.isEmpty()) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				if (!chessPanelEnabled) {
					chessPanelEnabled = true;
				}
				mouseIsPressed = false;
			}

			redoChessBoards.push(new ChessBoard(chessBoard));
			chessBoard = undoChessBoards.pop();
			redrawChessBoard();

			redoCapturedPieces.push(capturedPieces.clone());
			capturedPieces = undoCapturedPieces.pop();
			resetCapturedPiecesPanel();

			resetScore();
			setScoreAndTimeText();

			setMoveText();

			System.out.println();
			System.out.println(chessBoard);

			if (undoChessBoards.isEmpty()) {
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
		if (!redoChessBoards.isEmpty()) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				mouseIsPressed = false;
			}

			undoChessBoards.push(new ChessBoard(chessBoard));
			chessBoard = redoChessBoards.pop();
			redrawChessBoard();

			capturedPieces = redoCapturedPieces.pop();
			undoCapturedPieces.push(capturedPieces.clone());
			resetCapturedPiecesPanel();

			resetScore();
			setScoreAndTimeText();

			if (redoChessBoards.isEmpty()) {
				redoItem.setEnabled(false);
			}

			System.out.println();
			System.out.println(chessBoard);

			if (undoItem != null) {
				undoItem.setEnabled(true);
			}

			if (!checkForGameOver()) {
				setMoveText();
			}
		}
	}

	@Override
	public void startNewGame() {
		if (newGameParameters.getGuiType() == GuiType.DRAG_AND_DROP) {
			startNewGame(null);
		}
		if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
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

		flipBoard = gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanAllegiance() == Allegiance.BLACK;

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

		redrawChessBoard();
		revalidate();

		initializeAI();

		setMoveText();

		System.out.println();
		System.out.println(chessBoard);

		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanAllegiance() == Allegiance.BLACK) {
			aiMove(ai);
		} else if (gameParameters.getGameMode() == GameMode.AI_VS_AI) {
			playAiVsAi();
		}
	}

	@Override
	public void redrawChessBoard() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessSquare chessSquare = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				removePieceFromPosition(position);
				placePieceToPosition(position, chessSquare);
			}
		}
	}

	// Restores all the default values.
	@Override
	void restoreDefaultValues(String fenPosition) {
		if (fenPosition == null) {
			chessBoard = new ChessBoard(gameParameters.getVariant());
		} else {
			chessBoard = FenUtils.getChessBoardFromFenPosition(
					fenPosition,
					gameParameters.getNumOfRows(),
					gameParameters.getVariant()
			);
			chessBoard.setVariant(gameParameters.getVariant());
		}

		startingPosition = "";
		endingPosition = "";

		undoChessBoards.clear();
		redoChessBoards.clear();

		initializeCapturedPieces();

		undoCapturedPieces.clear();
		redoCapturedPieces.clear();

		if (fenPosition == null) {
			score = 0;
		} else {
			resetScore();
		}

		whiteCapturedPiecesCounter = 0;
		blackCapturedPiecesCounter = 0;

		mouseIsPressed = false;

		nextPositions = new HashSet<>();

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

		whiteElapsedSeconds = 0;
		blackElapsedSeconds = 0;
		timeUp = false;
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN && gameParameters.isEnableTimeLimit()) {
			initializeTimers();
		} else {
			if (whiteTimer != null) {
				whiteTimer.stop();
			}
			if (blackTimer != null) {
				blackTimer.stop();
			}
		}

		setMoveText();
	}

	@Override
	void makeDisplayMove(Move move, boolean isAiMove) {
		String positionStart = move.getPositionStart();
		int rowStart = chessBoard.getRowFromPosition(positionStart);
		int columnStart = chessBoard.getColumnFromPosition(positionStart);
		ChessSquare startingPiece = chessBoard.getGameBoard()[rowStart][columnStart];

		String positionEnd = move.getPositionEnd();
		int rowEnd = chessBoard.getRowFromPosition(positionEnd);
		int columnEnd = chessBoard.getColumnFromPosition(positionEnd);
		ChessSquare endSquare = chessBoard.getGameBoard()[rowEnd][columnEnd];

		redoChessBoards.clear();
		redoCapturedPieces.clear();

		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
				|| (gameParameters.getHumanAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| gameParameters.getHumanAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			undoChessBoards.push(new ChessBoard(chessBoard));
			undoCapturedPieces.push(capturedPieces.clone());
		}

		chessBoard.makeMove(move, true);

		// Pawn promotion implementation.
		if (startingPiece.isPawn()
				&& (startingPiece.isWhite() && rowEnd == 0
				|| startingPiece.isBlack() && rowEnd == 7)) {
			ChessSquare promotedPiece = new Queen(startingPiece.getAllegiance(), true);

			// If AI plays, automatically choose the best promotion piece, based on the best outcome.
			if (isAiMove) {
				chessBoard.automaticPawnPromotion(startingPiece, positionEnd, true);

				promotedPiece = chessBoard.getGameBoard()[rowEnd][columnEnd];
				if (promotedPiece.isWhite()) {
					JOptionPane.showMessageDialog(
							this,
							"Promoting White Pawn to " + promotedPiece + "!",
							"White Pawn Promotion",
							JOptionPane.INFORMATION_MESSAGE
					);
				} else if (promotedPiece.isBlack()) {
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
				if (startingPiece.isWhite()) {
					value = (String) JOptionPane.showInputDialog(
							this,
							"Promote White Pawn to:",
							"White Pawn Promotion",
							JOptionPane.QUESTION_MESSAGE,
							null,
							promotionPieces,
							initialSelection
					);
				} else if (startingPiece.isBlack()) {
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
			if (startingPiece.isWhite()) {
				score -= Constants.PAWN_SCORE_VALUE;
			} else if (startingPiece.isBlack()) {
				score += Constants.PAWN_SCORE_VALUE;
			}
		}

		// Increase halfMove.
		chessBoard.setHalfMoveNumber(chessBoard.getHalfMoveNumber() + 1);
		chessBoard.setPlayer(chessBoard.getNextPlayer());

		// If a chessPiece capture has occurred.
		if (startingPiece.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()) {
			score -= Utilities.getScoreValue(endSquare);

			updateCapturedPieces(endSquare);
			addCapturedPieceImage(endSquare);
		}
		// True if an en passant captured piece exists.
		else if (!(chessBoard.getCapturedEnPassantPiece().isEmpty())) {
			score -= Utilities.getScoreValue(chessBoard.getCapturedEnPassantPiece());

			updateCapturedPieces(chessBoard.getCapturedEnPassantPiece());
			addCapturedPieceImage(chessBoard.getCapturedEnPassantPiece());
		}

		for (String position : chessBoard.getPositionsToRemove()) {
			removePieceFromPosition(position);
		}
		for (String position : chessBoard.getPiecesToPlace().keySet()) {
			removePieceFromPosition(position);
			ChessSquare chessSquareToPlace = chessBoard.getPiecesToPlace().get(position);
			placePieceToPosition(position, chessSquareToPlace);
		}
		chessBoard.getPositionsToRemove().clear();
		chessBoard.getPiecesToPlace().clear();

		chessBoard.setThreats();

		setScoreAndTimeText();

		System.out.println();
		System.out.println(chessBoard);
	}

	@Override
	void showNextPositions(ChessSquare chessSquare) {
		for (String nextPosition : nextPositions) {
			int nextPositionRow = chessBoard.getRowFromPosition(nextPosition);
			int nextPositionColumn = chessBoard.getColumnFromPosition(nextPosition);

			if (flipBoard) {
				nextPositionRow = chessBoard.getNumOfRows() - 1 - nextPositionRow;
				nextPositionColumn = chessBoard.getNumOfColumns() - 1 - nextPositionColumn;
			}

			int nextPositionIndex = getSquareIndex(nextPositionRow + 1, nextPositionColumn + 1);
			Component nextPositionComponent = chessPanel.getComponent(nextPositionIndex);
			ChessSquare nextPositionPiece = chessBoard.getChessSquareFromPosition(nextPosition);

			if (nextPositionPiece.isPiece() && chessSquare.getAllegiance() != nextPositionPiece.getAllegiance()
					|| chessBoard.getEnPassantPosition().equals(nextPosition) && chessSquare.isPawn()) {
				nextPositionComponent.setBackground(Color.RED);
			} else if (chessSquare.isPawn() &&
					(!flipBoard
							&& (chessSquare.isWhite()
							&& nextPositionRow == 0
							|| chessSquare.isBlack()
							&& nextPositionRow == chessBoard.getNumOfRows() - 1)
							|| flipBoard
							&& (chessSquare.isWhite()
							&& nextPositionRow == chessBoard.getNumOfRows() - 1
							|| chessSquare.isBlack()
							&& nextPositionRow == 0))
			) {
				nextPositionComponent.setBackground(Color.GREEN);
			} else {
				nextPositionComponent.setBackground(Color.BLUE);
			}
		}
	}

	@Override
	void hideNextPositions() {
		if (startingPosition != null && !startingPosition.isEmpty()) {
			int startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
			int startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);

			if (flipBoard) {
				startingPositionRow = chessBoard.getNumOfRows() - 1 - startingPositionRow;
				startingPositionColumn = chessBoard.getNumOfColumns() - 1 - startingPositionColumn;
			}

			startingPositionRow += 1;
			startingPositionColumn += 1;

			int startingIndex = getSquareIndex(startingPositionRow, startingPositionColumn);
			Component startingComponent = chessPanel.getComponent(startingIndex);
			startingComponent.setBackground(getColorByRowCol(startingPositionRow, startingPositionColumn));
		}
		if (gameParameters.isShowNextMoves()) {
			for (String nextPosition : nextPositions) {
				int nextPositionRow = chessBoard.getRowFromPosition(nextPosition);
				int nextPositionColumn = chessBoard.getColumnFromPosition(nextPosition);

				if (flipBoard) {
					nextPositionRow = chessBoard.getNumOfRows() - 1 - nextPositionRow;
					nextPositionColumn = chessBoard.getNumOfColumns() - 1 - nextPositionColumn;
				}

				int nextPositionIndex = getSquareIndex(nextPositionRow + 1, nextPositionColumn + 1);
				Component nextPositionComponent = chessPanel.getComponent(nextPositionIndex);
				nextPositionComponent.setBackground(getColorByRowCol(nextPositionRow, nextPositionColumn));
			}
		}
	}

	public void addChessPiece(ImageIcon chessPiece, int row, int column) {
		if (flipBoard) {
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
	public void placePieceToPosition(String position, ChessSquare chessSquare) {
		int column = chessBoard.getColumnFromPosition(position);
		int row = chessBoard.getRowFromPosition(position);
		chessBoard.getGameBoard()[row][column] = chessSquare;

		String imagePath = GuiUtils.getImagePath(chessSquare);
		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE);
		addChessPiece(pieceImage, row, column);
	}

	// It removes the given chessPiece from the board (both the data structure and the JFrame).
	@Override
	void removePieceFromPosition(String position) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (flipBoard) {
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
	void placePiecesToChessBoard(ChessBoard chessBoard) {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
	}

	@Override
	void placePiecesToChessBoard(String fenPosition) {
		chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition, gameParameters.getVariant());

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				String piecePosition = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
	}

	@Override
	public void disableChessPanelClicks() {
		chessPanelEnabled = false;
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
		if (flipBoard) {
			startingRow = (chessBoard.getNumOfRows() + 2) - 1 - row;
			startingColumn = (chessBoard.getNumOfColumns() + 2) - 1 - column;
		}

		startingPosition = chessBoard.getPositionByRowCol(startingRow - 1, startingColumn - 1);
		ChessSquare chessSquare = chessBoard.getChessSquareFromPosition(startingPosition);

		if (chessSquare.isEmpty()) return;

		mouseIsPressed = true;

		nextPositions = chessBoard.getNextPositions(startingPosition);

		if (chessSquare.isWhite() && chessBoard.whitePlays()
				&& (gameParameters.getHumanAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| chessSquare.isBlack() && chessBoard.blackPlays()
				&& (gameParameters.getHumanAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)) {

			int startingIndex = getSquareIndex(row, column);
			Component startingComponent = chessPanel.getComponent(startingIndex);
			startingComponent.setBackground(Color.CYAN);

			// Display the next positions.
			if (gameParameters.isShowNextMoves()) {
				showNextPositions(chessSquare);
			}
		}

		// Get the location of the panel containing the image panel, i.e.,
		// the square's panel. we adjust the location to which we move the
		// piece by this amount so the piece doesn't 'snap to' the cursor location.
		// Point parentLocation = component.getParent().getLocation();

		Point componentLocation = component.getLocation();
		xAdjustment = componentLocation.x - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE * 2 / 3;
		yAdjustment = componentLocation.y - GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE * 2 / 3;
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

		if (flipBoard) {
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
		ChessSquare startingChessSquare = chessBoard.getGameBoard()[rowStart][columnStart];

		hideNextPositions();

		if ((startingChessSquare.isWhite() && chessBoard.whitePlays()
				&& (gameParameters.getHumanAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN)
				|| startingChessSquare.isBlack() && chessBoard.blackPlays()
				&& (gameParameters.getHumanAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN))
				&& nextPositions.contains(endingPosition)) {
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

			if (checkForGameOver()) {
				return;
			} else {
				setMoveText();
			}

			if (gameParameters.isEnableSounds()) {
				if (chessBoard.isCapture()) {
					SoundUtils.playCaptureSound();
				} else {
					SoundUtils.playMoveSound();
				}
			}

			nextPositions.clear();

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
			placePieceToPosition(startingPosition, startingChessSquare);
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
