package com.chriskormaris.mychessgame.gui.frame;

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
import com.chriskormaris.mychessgame.api.enumeration.Variant;
import com.chriskormaris.mychessgame.api.evaluation.Evaluation;
import com.chriskormaris.mychessgame.api.evaluation.PeSTOEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.ShannonEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.SimplifiedEvaluation;
import com.chriskormaris.mychessgame.api.evaluation.WukongEvaluation;
import com.chriskormaris.mychessgame.api.square.ChessPiece;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
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
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public abstract class ChessFrame extends JFrame {

    public GameParameters gameParameters;
    public GameParameters newGameParameters;

    int width;
    int height;

    JPanel guiPanel;

    JPanel chessPanel;
    JPanel capturedPiecesPanel;

    JTextPane moveTextPane;
    JTextPane fenTextPane;

    JButton undoButton;
    JButton redoButton;

    // 30 captured pieces at maximum + 1 label for displaying the score = 31 labels size
    JLabel[] capturedPiecesImages;

    String startingPosition;
    String endingPosition;

    // The position (0, 0) of the "chessBoard.getGameBoard()" is the upper left button
    // of the JButton array "chessButtons".
    // The position (chessBoard.getNumOfRows() - 1, 0) of the "chessBoard.getGameBoard()" is the lower left button
    // of the JButton array "chessButtons".
    public ChessBoard chessBoard;

    int score;

    int whiteCapturedPiecesCounter;
    int blackCapturedPiecesCounter;

    // These stacks of "ChessBoard" objects are used to handle the "undo" and "redo" functionality.
    Stack<ChessBoard> undoChessBoards;
    Stack<ChessBoard> redoChessBoards;

    // The length of this array is 30 elements.
    // The first 15 elements represent White captured pieces and are capital chars.
    // The last 15 elements represent Black captured pieces and are lowercase chars.
    // The elements could also be '-', which is a placeholder for future captured pieces.
    char[] capturedPieces;

    // These stacks of "char" arrays are used to handle the "undo" and "redo" functionality.
    Stack<char[]> undoCapturedPieces;
    Stack<char[]> redoCapturedPieces;

    String savedFenPosition;

    // This variable is used for the implementation of "AI vs AI".
    boolean isGameOver;

    boolean flipBoard;

    Set<String> nextPositions;

    JMenuItem undoItem;
    JMenuItem redoItem;
    JMenuItem saveCheckpointItem;
    JMenuItem loadCheckpointItem;

    // This variable is used for the implementation of "Human vs AI".
    public AI ai;

    Timer whiteTimer;
    Timer blackTimer;
    int whiteElapsedSeconds;
    int blackElapsedSeconds;
    boolean timeUp;

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

    public ChessFrame() {
        this(GuiConstants.TITLE);
    }

    public ChessFrame(String title) {
        super(title);

        guiPanel = new JPanel();

        moveTextPane = new JTextPane();
        fenTextPane = new JTextPane();

        chessBoard = new ChessBoard();

        undoChessBoards = new Stack<>();
        redoChessBoards = new Stack<>();

        initializeCapturedPieces();

        undoCapturedPieces = new Stack<>();
        redoCapturedPieces = new Stack<>();

        startingPosition = "";
        endingPosition = "";

        isGameOver = false;

        flipBoard = false;

        nextPositions = new HashSet<>();

        addMenus();

        super.setVisible(true);
        super.addKeyListener(undoRedoKeyListener);
    }

    private void addMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        undoItem = new JMenuItem("Undo    Ctrl+Z");
        redoItem = new JMenuItem("Redo    Ctrl+Y");
        JMenuItem exportToGifItem = new JMenuItem("Export to .gif");
        JMenuItem settingsItem = new JMenuItem("Settings");
        saveCheckpointItem = new JMenuItem("Save Checkpoint");
        loadCheckpointItem = new JMenuItem("Load Checkpoint");
        JMenuItem flipBoardItem = new JMenuItem("Flip Board");
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
            saveCheckpointItem.setEnabled(true);
        });

        redoItem.addActionListener(e -> redo());

        exportToGifItem.addActionListener(e -> exportToGif());

        settingsItem.addActionListener(e -> {
            SettingsFrame settings = new SettingsFrame(this, newGameParameters);
            settings.setVisible(true);
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

        flipBoardItem.addActionListener(e -> {
            flipBoard = !flipBoard;

            guiPanel.removeAll();
            initializeGUI();

            guiPanel.revalidate();
            guiPanel.repaint();
            super.paint(getGraphics());
            super.repaint();

            redrawChessBoard();
            super.revalidate();
        });

        exitItem.addActionListener(e -> System.exit(0));

        howToPlayItem.addActionListener(
                e -> {
                    ScrollableTextFrame howToPlayFrame = new ScrollableTextFrame(this, "How to Play", GuiConstants.RULES);
                    howToPlayFrame.setVisible(true);
                }
        );

        aboutItem.addActionListener(e -> {
            JLabel label = new JLabel(
                    "<html>A traditional Chess game implementation using Minimax AI.<br>"
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
                throw new RuntimeException(ex);
            }
        });

        fileMenu.add(newGameItem);
        fileMenu.add(undoItem);
        fileMenu.add(redoItem);
        fileMenu.add(exportToGifItem);
        fileMenu.add(settingsItem);
        fileMenu.add(saveCheckpointItem);
        fileMenu.add(loadCheckpointItem);
        fileMenu.add(flipBoardItem);
        fileMenu.add(exitItem);

        helpMenu.add(howToPlayItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        super.setJMenuBar(menuBar);
    }

    void configureGuiStyle() {
        try {
            if (gameParameters.getGuiStyle() == GuiStyle.CROSS_PLATFORM) {
                // Option 1
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } else if (gameParameters.getGuiStyle() == GuiStyle.SYSTEM) {
                // Option 2
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else if (gameParameters.getGuiStyle() == GuiStyle.NIMBUS) {
                // Option 3
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
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
                throw new RuntimeException(ex2);
            }
        }
    }

    void setMoveText() {
        if (chessBoard.getHalfMoveNumber() == 1) {
            moveTextPane.setText(GuiConstants.FIRST_MOVE_TEXT);
        } else {
            String moveText = "Move: " + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". ";
            moveText += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";

            if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck()) {
                moveText += " White King is in check!";
            } else if (chessBoard.blackPlays() && chessBoard.isBlackKingInCheck()) {
                moveText += " Black King is in check!";
            }

            moveTextPane.setText(moveText);
        }

        setFenText();
    }

    void setFenText() {
        String fenPosition = FenUtils.getFenPositionFromChessBoard(chessBoard);
        fenTextPane.setText(fenPosition);
    }

    void setScoreAndTimeText() {
        String text;
        if (score > 0) {
            text = "White: +" + score;
            capturedPiecesImages[15].setText(text);
        } else if (score < 0) {
            text = "Black: +" + (-score);
            capturedPiecesImages[15].setText(text);
        } else {
            text = GuiConstants.ZERO_SCORE_TEXT;
            capturedPiecesImages[15].setText(text);
        }

        if (gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN && gameParameters.isEnableTimeLimit()) {
            int whiteRemainingSeconds = gameParameters.getTimeLimitSeconds() - whiteElapsedSeconds;
            text += ", White time: " + whiteRemainingSeconds + "''";
            int blackRemainingSeconds = gameParameters.getTimeLimitSeconds() - blackElapsedSeconds;
            text += ", Black time: " + blackRemainingSeconds + "''";
            capturedPiecesImages[15].setText(text);
        }
    }

    void resetScore() {
        score = 0;
        for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
            for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
                String position = chessBoard.getPositionByRowCol(i, j);
                ChessSquare chessSquare = chessBoard.getChessSquareFromPosition(position);
                score += Utilities.getScoreValue(chessSquare);
            }
        }
    }

    void initializeMoveTextPaneBar() {
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);

        undoButton = new JButton("Undo");
        undoButton.setFocusable(false);
        undoButton.setEnabled(false);
        undoButton.addActionListener(e -> {
            undo();
            saveCheckpointItem.setEnabled(true);
        });
        tools.add(undoButton);

        moveTextPane.setEditable(false);
        moveTextPane.setFocusable(false);
        GuiUtils.centerTextPaneAndMakeBold(moveTextPane);
        tools.add(moveTextPane);

        redoButton = new JButton("Redo");
        redoButton.setFocusable(false);
        redoButton.setEnabled(false);
        redoButton.addActionListener(e -> redo());
        tools.add(redoButton);

        guiPanel.add(tools, BorderLayout.NORTH);
    }

    void initializeFenTextPaneBar() {
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);

        JLabel fenTextLabel = new JLabel("FEN Position:");
        tools.add(fenTextLabel);

        fenTextPane.setEditable(false);
        fenTextPane.setFocusable(false);
        GuiUtils.centerTextPaneAndMakeBold(fenTextPane);
        tools.add(fenTextPane);

        JButton copyButton = new JButton("Copy");
        copyButton.setFocusable(false);
        copyButton.addActionListener(
                e -> GuiUtils.copyTextToClipboard(fenTextPane.getText())
        );
        tools.add(copyButton);

        JButton importButton = new JButton("Import");
        importButton.setFocusable(false);
        importButton.addActionListener(e -> {
            String fenPosition = (String) JOptionPane.showInputDialog(
                    this,
                    "Please insert the starting \"FEN\" position in the text field below:"
                            + "                      ",
                    "Import starting FEN position",
                    INFORMATION_MESSAGE,
                    null,
                    null,
                    fenTextPane.getText()
            );

            if (fenPosition != null) {
                startNewGame(fenPosition);
            }
        });
        tools.add(importButton);

        guiPanel.add(tools, BorderLayout.NORTH);
    }

    void exportToGif() {
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

    void initializeCapturedPiecesPanel() {
        if (capturedPiecesPanel != null) {
            guiPanel.remove(capturedPiecesPanel);
        }
        capturedPiecesPanel = new JPanel();
        guiPanel.add(capturedPiecesPanel, BorderLayout.SOUTH);
    }

    void initializeCapturedPiecesImages() {
        capturedPiecesImages = new JLabel[31];

        // Create the captured chess board pieces icons.
        for (int i = 0; i < 31; i++) {
            capturedPiecesImages[i] = new JLabel();

            if (i == 15) {
                capturedPiecesImages[i].setText(GuiConstants.ZERO_SCORE_TEXT);
            } else {
                capturedPiecesImages[i].setText(" ");
            }

            capturedPiecesPanel.add(capturedPiecesImages[i]);
        }
    }

    void resetCapturedPiecesPanel() {
        initializeCapturedPiecesPanel();
        initializeCapturedPiecesImages();
        whiteCapturedPiecesCounter = 0;
        blackCapturedPiecesCounter = 0;
        for (int i = 0; i < 30; i++) {
            char symbol = capturedPieces[i];
            if (symbol != '-') {
                ChessSquare chessSquare = Utilities.getChessPiece(symbol);
                addCapturedPieceImage(chessSquare);
            }
        }
    }

    void updateCapturedPieces(ChessSquare chessSquare) {
        if (chessSquare.isPiece()) {
            int index;
            if (chessSquare.isWhite()) {
                index = Math.min(whiteCapturedPiecesCounter, 14);
            } else {
                index = Math.min(blackCapturedPiecesCounter, 14);
                index = 30 - index - 1;
            }
            capturedPieces[index] = ((ChessPiece) chessSquare).getSymbol();
        }
    }

    void addCapturedPieceImage(ChessSquare endSquare) {
        String imagePath = GuiUtils.getImagePath(endSquare);

        ImageIcon pieceImage = GuiUtils.preparePieceIcon(imagePath, GuiConstants.CAPTURED_CHESS_PIECE_PIXEL_SIZE);

        if (endSquare.isWhite()) {
            int index = Math.min(whiteCapturedPiecesCounter, 14);
            capturedPiecesImages[index].setIcon(pieceImage);
        } else if (endSquare.isBlack()) {
            int index = Math.min(blackCapturedPiecesCounter, 14);
            index = 31 - index - 1;
            capturedPiecesImages[index].setIcon(pieceImage);
        }

        incrementCapturedPiecesCounter(endSquare.getAllegiance());

        setScoreAndTimeText();
    }

    void incrementCapturedPiecesCounter(Allegiance allegiance) {
        if (allegiance == Allegiance.WHITE) {
            whiteCapturedPiecesCounter++;
        } else if (allegiance == Allegiance.BLACK) {
            blackCapturedPiecesCounter++;
        }
    }

    void initializeCapturedPieces() {
        capturedPieces = new char[30];
        for (int i = 0; i < 30; i++) {
            capturedPieces[i] = '-';
        }
    }

    void initializeAI() {
        if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
            if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
                Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
                if (gameParameters.getHumanAllegiance() == Allegiance.WHITE) {
                    ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.BLACK, evaluation1);
                } else if (gameParameters.getHumanAllegiance() == Allegiance.BLACK) {
                    ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
                }
            } else if (gameParameters.getAi1Type() == AiType.MINIMAX_ALPHA_BETA_PRUNING_AI) {
                Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
                if (gameParameters.getHumanAllegiance() == Allegiance.WHITE) {
                    ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.BLACK, evaluation1);
                } else if (gameParameters.getHumanAllegiance() == Allegiance.BLACK) {
                    ai = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
                }
            } else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
                if (gameParameters.getHumanAllegiance() == Allegiance.WHITE) {
                    ai = new RandomChoiceAI(Constants.BLACK);
                } else if (gameParameters.getHumanAllegiance() == Allegiance.BLACK) {
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

    public void aiMove(AI ai) {
        Move aiMove = ai.getNextMove(chessBoard);
        System.out.println("aiMove: " + aiMove);

        makeDisplayMove(aiMove, true);

        if (gameParameters.isEnableSounds()) {
            if (chessBoard.isCapture()) {
                SoundUtils.playCaptureSound();
            } else {
                SoundUtils.playMoveSound();
            }
        }

        if (!checkForGameOver()) {
            setMoveText();
        }
    }

    void playAiVsAi() {
        AI ai1;
        if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
            Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
            ai1 = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
        } else if (gameParameters.getAi1Type() == AiType.MINIMAX_ALPHA_BETA_PRUNING_AI) {
            Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
            ai1 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
        } else {
            ai1 = new RandomChoiceAI(Constants.WHITE);
        }

        AI ai2;
        if (gameParameters.getAi2Type() == AiType.MINIMAX_AI) {
            Evaluation evaluation2 = createEvaluation(gameParameters.getEvaluationFunction2());
            ai2 = new MinimaxAI(gameParameters.getAi2MaxDepth(), Constants.BLACK, evaluation2);
        } else if (gameParameters.getAi2Type() == AiType.MINIMAX_ALPHA_BETA_PRUNING_AI) {
            Evaluation evaluation2 = createEvaluation(gameParameters.getEvaluationFunction2());
            ai2 = new MinimaxAlphaBetaPruningAI(gameParameters.getAi2MaxDepth(), Constants.BLACK, evaluation2);
        } else {
            ai2 = new RandomChoiceAI(Constants.BLACK);
        }

        while (!isGameOver) {
            System.out.println(moveTextPane.getText());
            aiVsAiMove(ai1);

            if (!isGameOver) {
                System.out.println(moveTextPane.getText());

                try {
                    if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
                        Thread.sleep(GuiConstants.MINIMAX_AI_MOVE_DELAY_MILLISECONDS);
                    } else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
                        Thread.sleep(GuiConstants.RANDOM_AI_MOVE_DELAY_MILLISECONDS);
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                aiVsAiMove(ai2);
            }

            if (!isGameOver) {
                try {
                    if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
                        Thread.sleep(GuiConstants.MINIMAX_AI_MOVE_DELAY_MILLISECONDS);
                    } else if (gameParameters.getAi1Type() == AiType.RANDOM_AI) {
                        Thread.sleep(GuiConstants.RANDOM_AI_MOVE_DELAY_MILLISECONDS);
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        if (undoItem != null) {
            undoItem.setEnabled(true);
            undoButton.setEnabled(true);
        }
    }

    void aiVsAiMove(AI ai) {
        aiMove(ai);

        super.revalidate();
        super.paint(super.getGraphics());
    }

    Color getColorByRowCol(int row, int column) {
        if ((row + column) % 2 == 0) {
            return gameParameters.getWhiteSquareColor();
        } else {
            return gameParameters.getBlackSquareColor();
        }
    }

    public boolean checkForGameOver() {
        BufferedImage checkmateImg;
        try {
            checkmateImg = ImageIO.read(ResourceLoader.load(GuiConstants.CHECKMATE_IMG_PATH));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Image checkmateDImg = checkmateImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        ImageIcon checkmateIcon = new ImageIcon(checkmateDImg);

        BufferedImage drawImg;
        try {
            drawImg = ImageIO.read(ResourceLoader.load(GuiConstants.DRAW_IMG_PATH));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Image drawDImg = drawImg.getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        ImageIcon drawIcon = new ImageIcon(drawDImg);

        chessBoard.checkForTerminalState();
        if (chessBoard.getGameResult() == GameResult.NONE) return false;

        /* Check for White checkmate. */
        if (chessBoard.blackPlays()) {
            if (chessBoard.getGameResult() == GameResult.WHITE_CHECKMATE) {
                String moveText = "Move: "
                        + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! White wins!";
                moveTextPane.setText(moveText);

                if (gameParameters.isEnableSounds()) {
                    SoundUtils.playCheckmateSound();
                }

                int dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "White wins! Start a new game?",
                        "Checkmate",
                        JOptionPane.YES_NO_OPTION,
                        QUESTION_MESSAGE,
                        checkmateIcon
                );

                startNewGameOrNot(dialogResult);

                return true;
            }
        }

        /* Check for Black checkmate. */
        else {
            if (chessBoard.getGameResult() == GameResult.BLACK_CHECKMATE) {
                String moveText = "Move: "
                        + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". Checkmate! Black wins!";
                moveTextPane.setText(moveText);

                if (gameParameters.isEnableSounds()) {
                    SoundUtils.playCheckmateSound();
                }

                int dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "Black wins! Start a new game?",
                        "Checkmate",
                        JOptionPane.YES_NO_OPTION,
                        QUESTION_MESSAGE,
                        checkmateIcon
                );

                startNewGameOrNot(dialogResult);

                return true;
            }
        }

        /* Stalemate draw implementation. */

        // Check for White stalemate.
        if (chessBoard.whitePlays()) {
            if (chessBoard.getGameResult() == GameResult.WHITE_STALEMATE_DRAW) {
                String moveText = "Move: "
                        + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                        + ". Stalemate! No legal moves for White exist.";
                moveTextPane.setText(moveText);

                int dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "Stalemate! No legal moves for White exist. Start a new game?",
                        "Draw",
                        JOptionPane.YES_NO_OPTION,
                        QUESTION_MESSAGE,
                        drawIcon
                );

                startNewGameOrNot(dialogResult);

                return true;
            }
        }

        // Check for Black stalemate.
        else {
            if (chessBoard.getGameResult() == GameResult.BLACK_STALEMATE_DRAW) {
                String moveText = "Move: "
                        + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                        + ". Stalemate! No legal moves for Black exist.";
                moveTextPane.setText(moveText);

                int dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "Stalemate! No legal moves for Black exist. Start a new game?",
                        "Draw",
                        JOptionPane.YES_NO_OPTION,
                        QUESTION_MESSAGE,
                        drawIcon
                );

                startNewGameOrNot(dialogResult);

                return true;
            }
        }

        /* Insufficient checkmate material draw implementation. */
        if (chessBoard.getGameResult() == GameResult.INSUFFICIENT_MATERIAL_DRAW) {
            String moveText = "Move: "
                    + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                    + ". It is a draw.";
            moveTextPane.setText(moveText);

            int dialogResult = JOptionPane.showConfirmDialog(
                    this,
                    "It is a draw due to insufficient mating material! Start a new game?",
                    "Draw",
                    JOptionPane.YES_NO_OPTION,
                    QUESTION_MESSAGE,
                    drawIcon
            );

            startNewGameOrNot(dialogResult);

            return true;
        }

        // 75 full-moves without a Chess piece capture Draw implementation.
        if (chessBoard.getGameResult() == GameResult.UNCONDITIONAL_NO_CAPTURE_DRAW) {
            String moveText = "Move: "
                    + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                    + ". It is a draw.";
            moveTextPane.setText(moveText);

            int dialogResult = JOptionPane.showConfirmDialog(
                    this,
                    "It is a draw! 75 moves have been played without a piece capture! Start a new game?",
                    "Draw",
                    JOptionPane.YES_NO_OPTION,
                    QUESTION_MESSAGE,
                    drawIcon
            );

            startNewGameOrNot(dialogResult);

            return true;
        }

        // 50 full-moves without a Chess piece capture Draw implementation.
        if (chessBoard.getGameResult() == GameResult.CONDITIONAL_NO_CAPTURE_DRAW) {
            int dialogResult = JOptionPane.CLOSED_OPTION;

            // In the HUMAN_VS_AI mode, show the draw dialog, only if the AI has just made a move.
            if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
                    || (chessBoard.blackPlays() && gameParameters.getHumanAllegiance() == Allegiance.WHITE
                    || chessBoard.whitePlays() && gameParameters.getHumanAllegiance() == Allegiance.BLACK)) {
                dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "50 moves have been played without a piece capture! Do you want to claim a draw?",
                        "Draw",
                        JOptionPane.YES_NO_OPTION
                );
            }

            if (dialogResult == JOptionPane.YES_OPTION) {
                showClaimDrawDialog(drawIcon);
                return true;
            } else {
                chessBoard.setGameResult(GameResult.NONE);
                return false;
            }
        }

        // Five-fold repetition draw rule implementation.
        // This situation occurs when we end up with the same chess board position 5 different times
        // at any time in the game, not necessarily successively.
        if (chessBoard.getGameResult() == GameResult.FIVEFOLD_REPETITION_DRAW) {
            String moveText = "Move: "
                    + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                    + ". It is a draw.";
            moveTextPane.setText(moveText);

            int dialogResult = JOptionPane.showConfirmDialog(
                    this,
                    "It is a draw! Fivefold repetition of the same Chess board position has occurred! " +
                            "Start a new game?",
                    "Draw",
                    JOptionPane.YES_NO_OPTION,
                    QUESTION_MESSAGE,
                    drawIcon
            );

            startNewGameOrNot(dialogResult);

            return true;
        }

        // Three-fold repetition draw rule implementation.
        // This situation occurs when we end up with the same chess board position 3 different times
        // at any time in the game, not necessarily successively.
        if (chessBoard.getGameResult() == GameResult.THREEFOLD_REPETITION_DRAW) {
            int dialogResult = JOptionPane.CLOSED_OPTION;

            // In the HUMAN_VS_AI mode, show the draw dialog, only if the AI has just made a move.
            if (gameParameters.getGameMode() != GameMode.HUMAN_VS_AI
                    || (chessBoard.blackPlays() && gameParameters.getHumanAllegiance() == Allegiance.WHITE
                    || chessBoard.whitePlays() && gameParameters.getHumanAllegiance() == Allegiance.BLACK)) {
                dialogResult = JOptionPane.showConfirmDialog(
                        this,
                        "Threefold repetition of the same Chess board position has occurred! "
                                + "Do you want to claim a draw?",
                        "Draw",
                        JOptionPane.YES_NO_OPTION
                );
            }

            if (JOptionPane.YES_OPTION == dialogResult) {
                showClaimDrawDialog(drawIcon);
                return true;
            } else {
                chessBoard.setGameResult(GameResult.NONE);
                return false;
            }
        }

        if (gameParameters.getVariant() == Variant.HORDE) {
            if (chessBoard.whitePlays()) {
                if (chessBoard.getGameResult() == GameResult.HORDE_NO_WHITE_PIECES_LEFT) {
                    int dialogResult = JOptionPane.showConfirmDialog(
                            this,
                            "Black wins! No White pieces are left. Start a new game?",
                            "No Pieces Left",
                            JOptionPane.YES_NO_OPTION,
                            QUESTION_MESSAGE,
                            checkmateIcon
                    );

                    startNewGameOrNot(dialogResult);

                    return true;
                } else if (chessBoard.getGameResult() == GameResult.HORDE_WHITE_STALEMATE_DRAW) {
                    int dialogResult = JOptionPane.showConfirmDialog(
                            this,
                            "Horde stalemate! No legal moves for White exist. Start a new game?",
                            "Draw",
                            JOptionPane.YES_NO_OPTION,
                            QUESTION_MESSAGE,
                            drawIcon
                    );

                    startNewGameOrNot(dialogResult);

                    return true;
                }
            }
        }

        return false;
    }

    private void showClaimDrawDialog(ImageIcon drawIcon) {
        String moveText = "Move: "
                + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2)
                + ". It is a draw.";
        moveTextPane.setText(moveText);

        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                "It is a draw! Start a new game?",
                "Draw",
                JOptionPane.YES_NO_OPTION,
                QUESTION_MESSAGE,
                drawIcon
        );

        startNewGameOrNot(dialogResult);
    }

    private void startNewGameOrNot(int dialogResult) {
        isGameOver = true;

        if (dialogResult == JOptionPane.YES_OPTION) {
            startNewGame();
        } else {
            if (!undoChessBoards.isEmpty() && undoItem != null) {
                undoItem.setEnabled(true);
                undoButton.setEnabled(true);
            }
            if (redoItem != null) {
                redoItem.setEnabled(false);
                redoButton.setEnabled(false);
            }
            if (saveCheckpointItem != null) {
                saveCheckpointItem.setEnabled(false);
            }

            disableChessPanelClicks();
        }
    }

    void initializeTimers() {
        if (whiteTimer == null) {
            whiteTimer = new Timer(1000, e -> {
                if (chessBoard.whitePlays() && !chessBoard.isTerminalState() && !timeUp) {
                    if (gameParameters.getTimeLimitSeconds() - whiteElapsedSeconds == 0) {
                        timeUp = true;
                        whiteTimer.stop();
                        int dialogResult = JOptionPane.showConfirmDialog(
                                this,
                                "Time is up! Black wins! Start a new game?",
                                "Out of Time",
                                JOptionPane.YES_NO_OPTION
                        );
                        startNewGameOrNot(dialogResult);
                    } else {
                        whiteElapsedSeconds++;
                        setScoreAndTimeText();
                    }
                }
            });
            whiteTimer.start();
        } else {
            whiteTimer.restart();
        }

        if (blackTimer == null) {
            blackTimer = new Timer(1000, e -> {
                if (chessBoard.blackPlays() && !chessBoard.isTerminalState() && !timeUp) {
                    if (gameParameters.getTimeLimitSeconds() - blackElapsedSeconds == 0) {
                        timeUp = true;
                        blackTimer.stop();
                        int dialogResult = JOptionPane.showConfirmDialog(
                                this,
                                "Time is up! White wins! Start a new game?",
                                "Out of Time",
                                JOptionPane.YES_NO_OPTION
                        );
                        startNewGameOrNot(dialogResult);
                    } else {
                        blackElapsedSeconds++;
                        setScoreAndTimeText();
                    }
                }
            });
            blackTimer.start();
        } else {
            blackTimer.restart();
        }
    }

    abstract void initializeGUI();

    public abstract void redrawChessBoard();

    abstract void initializeChessPanel();

    abstract void undo();

    abstract void redo();

    abstract void startNewGame();

    abstract void startNewGame(String fenPosition);

    void restoreDefaultValues() {
        restoreDefaultValues(null);
    }

    abstract void restoreDefaultValues(String fenPosition);

    abstract void makeDisplayMove(Move move, boolean isAiMove);

    abstract void showNextPositions(ChessSquare chessSquare);

    abstract void hideNextPositions();

    public abstract void placePieceToPosition(String position, ChessSquare chessSquare);

    abstract void removePieceFromPosition(String position);

    abstract void placePiecesToChessBoard(ChessBoard chessBoard);

    abstract void placePiecesToChessBoard(String fenPosition);

    abstract void disableChessPanelClicks();

}
