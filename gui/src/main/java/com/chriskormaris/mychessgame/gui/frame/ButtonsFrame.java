package com.chriskormaris.mychessgame.gui.frame;


import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
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
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;
import com.chriskormaris.mychessgame.gui.util.GuiUtils;
import com.chriskormaris.mychessgame.gui.util.ResourceLoader;
import com.chriskormaris.mychessgame.gui.util.SoundUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;


public class ButtonsFrame extends ChessFrame {

	// The position (0, 0) of the chessButtons,
	// corresponds to the position (chessBoard.getNumOfColumns() - 1, 0) of the ChessBoard's gameBoard.
	JButton[][] chessButtons;

	boolean startingButtonIsClicked;

	boolean buttonsEnabled;

	public ButtonsFrame() {
		this(GuiConstants.TITLE);
	}

	public ButtonsFrame(String title) {
		super(title);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = (int) screenSize.getHeight() - 40;
		width = height + 40;

		gameParameters = new GameParameters();
		gameParameters.setGuiType(GuiType.BUTTONS);
		newGameParameters = new GameParameters(gameParameters);

		buttonsEnabled = true;

		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);

		initializeGUI();
		redrawChessBoard();
		initializeAI();

		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(GuiConstants.ICON_PATH));
			super.setIconImage(icon);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		super.add(guiPanel);
		super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		super.pack();
		super.setSize(new Dimension(width, height));
		super.setMinimumSize(super.getSize());
		super.setLocation((int) (screenSize.getWidth() - super.getWidth()) / 2, 5);
		super.setResizable(false);
	}


	@Override
	void initializeGUI() {
		configureGuiStyle();

		// Set up the main GUI.
		// gui.setBorder(new EmptyBorder(0, 0, 0, 0));
		guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));

		initializeMoveTextPaneBar();
		setMoveMessage();

		initializeChessButtons();
		initializeChessPanel();

		initializeCapturedPiecesPanel();
		initializeCapturedPiecesImages();

		setScoreAndTimeMessage();
	}

	@Override
	void initializeChessPanel() {
		if (chessPanel != null) {
			guiPanel.remove(chessPanel);
		}
		chessPanel = new JPanel(new GridLayout(chessBoard.getNumOfRows() + 2, chessBoard.getNumOfColumns() + 2));
		chessPanel.setBorder(new LineBorder(Color.BLACK));
		chessPanel.setPreferredSize(new Dimension(width, height - 100));
		guiPanel.add(chessPanel, BorderLayout.CENTER);

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
		// fill the black non-pawn chessPiece row
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns() + 1; j++) {
				if (j == 0 || j == chessBoard.getNumOfColumns()) {
					int rankNumber = chessBoard.getNumOfRows() - i;
					if (flipBoard) {
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
		if (flipBoard) {
			for (int j = chessBoard.getNumOfColumns() - 1; j >= 0; j--) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		} else {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				chessPanel.add(new JLabel(String.valueOf((char) ((int) 'A' + j)), SwingConstants.CENTER));
			}
		}
	}

	@Override
	void undo() {
		if (!undoChessBoards.isEmpty()) {
			System.out.println("Undo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				if (!buttonsEnabled) {
					enableChessButtons();
				}
				startingButtonIsClicked = false;
				hideNextPositions();
			}

			redoChessBoards.push(new ChessBoard(chessBoard));
			chessBoard = undoChessBoards.pop();
			placePiecesToChessBoard(chessBoard);

			redoCapturedPieces.push(capturedPieces.clone());
			capturedPieces = undoCapturedPieces.pop();
			resetCapturedPiecesPanel();

			resetScore();
			setScoreAndTimeMessage();

			setMoveMessage();

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
		if (!redoChessBoards.isEmpty()) {
			System.out.println("Redo is pressed!");

			if (gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				startingButtonIsClicked = false;
				hideNextPositions();
			}

			undoChessBoards.push(new ChessBoard(chessBoard));
			chessBoard = redoChessBoards.pop();
			placePiecesToChessBoard(chessBoard);

			capturedPieces = redoCapturedPieces.pop();
			undoCapturedPieces.push(capturedPieces.clone());
			resetCapturedPiecesPanel();

			resetCapturedPiecesPanel();

			resetScore();
			setScoreAndTimeMessage();

			setMoveMessage();

			for (int i = 0; i < 31; i++) {
				capturedPiecesPanel.add(capturedPiecesImages[i]);
			}

			if (redoChessBoards.isEmpty()) {
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

	private void initializeChessButtons() {
		chessButtons = new JButton[chessBoard.getNumOfRows()][chessBoard.getNumOfColumns()];

		// Create the chess board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);

				button.setIcon(null);

				button.setBackground(getColorByRowCol(i, j));

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
				if (flipBoard) {
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
	}

	@Override
	public void startNewGame() {
		if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
			startNewGame(Constants.DEFAULT_STARTING_FEN_POSITION);
		}
		if (newGameParameters.getGuiType() == GuiType.DRAG_AND_DROP) {
			super.dispose();
			ChessFrame dragAndDropFrame = new DragAndDropFrame();
			dragAndDropFrame.newGameParameters = newGameParameters;
			dragAndDropFrame.startNewGame();
		}
	}

	@Override
	public void startNewGame(String fenPosition) {
		System.out.println("Starting new game!");

		if (newGameParameters.getNumOfRows() != Constants.DEFAULT_NUM_OF_ROWS) {
			newGameParameters.setEvaluationFunction1(EvaluationFunction.SHANNON);
			if (newGameParameters.getGameMode() == GameMode.AI_VS_AI) {
				newGameParameters.setEvaluationFunction2(EvaluationFunction.SHANNON);
			}
		}
		gameParameters = new GameParameters(newGameParameters);

		flipBoard = gameParameters.getGameMode() == GameMode.HUMAN_VS_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK;

		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}

		guiPanel.removeAll();

		restoreDefaultValues(fenPosition);

		initializeGUI();

		redrawChessBoard();
		super.revalidate();

		initializeAI();

		setMoveMessage();

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
	@Override
	void restoreDefaultValues(String fenPosition) {
		if (fenPosition.equals(Constants.DEFAULT_STARTING_FEN_POSITION)) {
			chessBoard = new ChessBoard(gameParameters.getNumOfRows());
		} else {
			chessBoard = FenUtils.getChessBoardFromFenPosition(fenPosition, gameParameters.getNumOfRows());
		}

		startingPosition = "";
		endingPosition = "";

		undoChessBoards.clear();
		redoChessBoards.clear();

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

		nextPositions = new HashSet<>();

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

		whitePlayerElapsedSeconds = 0;
		blackPlayerElapsedSeconds = 0;
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN && gameParameters.isEnableTimeLimit()) {
			if (whitePlayerTimer == null || blackPlayerTimer == null) {
				initializeTimers();
			} else {
				whitePlayerTimer.restart();
				blackPlayerTimer.restart();
			}
		} else {
			if (whitePlayerTimer != null) {
				whitePlayerTimer.stop();
			}
			if (blackPlayerTimer != null) {
				blackPlayerTimer.stop();
			}
		}

		timeUp = false;
	}

	// This method is only called from inside a chess board button listener.
	private void chessButtonClick(int row, int column, JButton button) {
		hideNextPositions();

		String position = chessBoard.getPositionByRowCol(row, column);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		int startingPositionRow;
		int startingPositionColumn;
		ChessPiece startingPiece = null;
		if (!startingPosition.isEmpty()) {
			startingPositionRow = chessBoard.getRowFromPosition(startingPosition);
			startingPositionColumn = chessBoard.getColumnFromPosition(startingPosition);
			startingPiece = chessBoard.getGameBoard()[startingPositionRow][startingPositionColumn];
		}

		if (!startingButtonIsClicked
				&& (chessPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| chessPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {

			startingPosition = position;

			if (!(chessPiece instanceof EmptySquare)) {
				nextPositions = chessBoard.getNextPositions(position);
				button.setBackground(Color.CYAN);

				// Display the next positions.
				if (gameParameters.isShowNextMoves()) {
					showNextPositions(chessPiece);
				}

				startingButtonIsClicked = true;
			}

		} else if (startingButtonIsClicked && startingPiece != null
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| startingPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {

			startingButtonIsClicked = false;
			endingPosition = position;

			if (nextPositions.contains(endingPosition)) {
				Move move = new Move(startingPosition, endingPosition);
				makeDisplayMove(move, false);

				if (checkForGameOver()) {
					setMoveMessage();
					return;
				} else {
					setMoveMessage();
				}

				if (gameParameters.isEnableSounds()) {
					SoundUtils.playMoveSound();
				}

				nextPositions.clear();

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

		redoChessBoards.clear();
		redoCapturedPieces.clear();

		if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
				|| (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				|| gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			undoChessBoards.push(new ChessBoard(chessBoard));
			undoCapturedPieces.push(capturedPieces.clone());
		}

		chessBoard.makeMove(move, true);

		// Pawn promotion implementation.
		if (startingPiece instanceof Pawn
				&& (startingPiece.getAllegiance() == Allegiance.WHITE && rowEnd == 0
				|| startingPiece.getAllegiance() == Allegiance.BLACK && rowEnd == chessBoard.getNumOfRows() - 1)) {
			ChessPiece promotedPiece = new Queen(startingPiece.getAllegiance(), true);
			// If AI plays, automatically choose the best promotion piece, based on the best outcome.
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

		// If a ChessPiece capture has occurred.
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

		setScoreAndTimeMessage();

		System.out.println();
		System.out.println(chessBoard);
	}

	@Override
	void showNextPositions(ChessPiece chessPiece) {
		for (String nextPosition : nextPositions) {
			int nextPositionRow = chessBoard.getRowFromPosition(nextPosition);
			int nextPositionColumn = chessBoard.getColumnFromPosition(nextPosition);

			int nextPositionButtonRow = nextPositionRow;
			int nextPositionButtonColumn = nextPositionColumn;
			if (flipBoard) {
				nextPositionButtonRow = chessBoard.getNumOfRows() - 1 - nextPositionRow;
				nextPositionButtonColumn = chessBoard.getNumOfColumns() - 1 - nextPositionColumn;
			}
			JButton nextPositionButton = chessButtons[nextPositionButtonRow][nextPositionButtonColumn];
			ChessPiece nextPositionPiece = chessBoard.getGameBoard()[nextPositionRow][nextPositionColumn];

			if (nextPositionPiece.getAllegiance() != Allegiance.NONE
					|| chessBoard.getEnPassantPosition().equals(nextPosition)
					&& chessPiece instanceof Pawn) {
				nextPositionButton.setBackground(Color.RED);
			} else if (chessPiece instanceof Pawn &&
					(chessPiece.getAllegiance() == Allegiance.WHITE
							&& nextPositionRow == 0
							|| chessPiece.getAllegiance() == Allegiance.BLACK
							&& nextPositionRow == chessBoard.getNumOfRows() - 1)
			) {
				nextPositionButton.setBackground(Color.GREEN);
			} else if (nextPositionPiece instanceof EmptySquare) {
				nextPositionButton.setBackground(Color.BLUE);
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
			JButton startingButton = chessButtons[startingPositionRow][startingPositionColumn];

			startingButton.setBackground(getColorByRowCol(startingPositionRow, startingPositionColumn));
		}
		if (gameParameters.isShowNextMoves()) {
			for (String nextPosition : nextPositions) {
				int row = chessBoard.getRowFromPosition(nextPosition);
				int column = chessBoard.getColumnFromPosition(nextPosition);

				if (flipBoard) {
					row = chessBoard.getNumOfRows() - 1 - row;
					column = chessBoard.getNumOfColumns() - 1 - column;
				}
				JButton button = chessButtons[row][column];

				button.setBackground(getColorByRowCol(row, column));
			}
		}
	}

	// It inserts the given chessPiece to the given position on the board
	// (both the data structure and the GUI).
	@Override
	public void placePieceToPosition(String position, ChessPiece chessPiece) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		chessBoard.getGameBoard()[row][column] = chessPiece;

		String imagePath = GuiUtils.getImagePath(chessPiece);
		ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CHESS_PIECE_SQUARE_PIXEL_SIZE);

		if (flipBoard) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}
		chessButtons[row][column].setIcon(pieceImage);
	}

	// It removes the given chessPiece from the board (both the data structure and the GUI).
	@Override
	void removePieceFromPosition(String position) {
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);

		chessBoard.getGameBoard()[row][column] = new EmptySquare();

		if (flipBoard) {
			row = chessBoard.getNumOfRows() - 1 - row;
			column = chessBoard.getNumOfColumns() - 1 - column;
		}

		chessButtons[row][column].setIcon(null);
	}

	@Override
	void redrawChessBoard() {
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				String position = chessBoard.getPositionByRowCol(i, j);
				placePieceToPosition(position, chessPiece);
			}
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
				chessButtons[i][j].setIcon(null);

				chessButtons[i][j].setBackground(getColorByRowCol(i, j));
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
				if (flipBoard) {
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

	private void disableChessButtons() {
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

	@Override
	void disableChessPanelClicks() {
		disableChessButtons();
	}

	public static void main(String[] args) {
		ChessFrame buttonsFrame = new ButtonsFrame();
		buttonsFrame.startNewGame();
	}

}
