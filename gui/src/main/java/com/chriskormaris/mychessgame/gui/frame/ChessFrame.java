package com.chriskormaris.mychessgame.gui.frame;

import com.chriskormaris.mychessgame.api.ai.AI;
import com.chriskormaris.mychessgame.api.ai.MinimaxAI;
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
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

public abstract class ChessFrame extends JFrame {

    public GameParameters gameParameters;
    public GameParameters newGameParameters;

    int width;
    int height;

    JPanel guiPanel;

    JToolBar tools;

    JPanel chessPanel;
    JPanel capturedPiecesPanel;

    JTextPane turnTextPane;

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

    Set<String> hintPositions;

    JMenuItem undoItem;
    JMenuItem redoItem;
    JMenuItem exportFenPositionItem;
    JMenuItem saveCheckpointItem;
    JMenuItem loadCheckpointItem;

    // This variable is used for the implementation of "Human vs AI".
    public AI ai;

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

    public ChessFrame(String title) throws HeadlessException {
        super(title);

        guiPanel = new JPanel();
        turnTextPane = new JTextPane();

        tools = new JToolBar();

        chessBoard = new ChessBoard();

        undoChessBoards = new Stack<>();
        redoChessBoards = new Stack<>();

        initializeCapturedPieces();

        undoCapturedPieces = new Stack<>();
        redoCapturedPieces = new Stack<>();

        startingPosition = "";
        endingPosition = "";

        hintPositions = new HashSet<>();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        undoItem = new JMenuItem("Undo    Ctrl+Z");
        redoItem = new JMenuItem("Redo    Ctrl+Y");
        JMenuItem exportToGifItem = new JMenuItem("Export to .gif");
        JMenuItem settingsItem = new JMenuItem("Settings");
        JMenuItem importStartingFenPositionItem = new JMenuItem("Import starting FEN position");
        exportFenPositionItem = new JMenuItem("Export FEN position to file");
        saveCheckpointItem = new JMenuItem("Save checkpoint");
        loadCheckpointItem = new JMenuItem("Load checkpoint");
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
                ex2.printStackTrace();
            }
        }
    }

    void setTurnMessage() {
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

    void setScoreMessage() {
        if (score > 0) {
            capturedPiecesImages[15].setText("White: +" + score);
        } else if (score < 0) {
            capturedPiecesImages[15].setText("Black: +" + (-score));
        } else {
            capturedPiecesImages[15].setText(GuiConstants.ZERO_SCORE_TEXT);
        }
    }

    void resetScore() {
        score = 0;
        for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
            for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
                String position = chessBoard.getPositionByRowCol(i, j);
                ChessPiece chessPiece = chessBoard.getChessPieceFromPosition(position);
                score += Utilities.getScoreValue(chessPiece);
            }
        }
    }

    void initializeTurnTextPaneBar() {
        if (tools != null) {
            guiPanel.remove(tools);
        }

        tools = new JToolBar();
        tools.setFloatable(false);

        turnTextPane.setEditable(false);
        turnTextPane.setFocusable(false);
        GuiUtils.centerTextPaneAndMakeBold(turnTextPane);

        tools.add(turnTextPane);

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

    void resetCapturedPiecesPanel() {
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

    void updateCapturedPieces(ChessPiece chessPiece) {
        if (chessPiece.getAllegiance() == Allegiance.WHITE) {
            int index = Math.min(whiteCapturedPiecesCounter, 14);
            capturedPieces[index] = chessPiece.getPieceChar();
        } else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
            int index = Math.min(blackCapturedPiecesCounter, 14);
            index = 30 - index - 1;
            capturedPieces[index] = chessPiece.getPieceChar();
        }
    }

    void addCapturedPieceImage(ChessPiece endSquare) {
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
                if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
                    ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.BLACK, evaluation1);
                } else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
                    ai = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
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

    public void aiMove(AI ai) {
        Move aiMove = ai.getNextMove(chessBoard);
        System.out.println("aiMove: " + aiMove);

        makeDisplayMove(aiMove, true);

        checkForGameOver();
    }

    void playAiVsAi() {
        AI ai1;
        if (gameParameters.getAi1Type() == AiType.MINIMAX_AI) {
            Evaluation evaluation1 = createEvaluation(gameParameters.getEvaluationFunction1());
            ai1 = new MinimaxAI(gameParameters.getAi1MaxDepth(), Constants.WHITE, evaluation1);
        } else {
            ai1 = new RandomChoiceAI(Constants.WHITE);
        }

        AI ai2;
        if (gameParameters.getAi2Type() == AiType.MINIMAX_AI) {
            Evaluation evaluation2 = createEvaluation(gameParameters.getEvaluationFunction2());
            ai2 = new MinimaxAI(gameParameters.getAi2MaxDepth(), Constants.BLACK, evaluation2);
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

            disableChessPanelClicks();
        }
    }

    abstract void initializeGUI();

    abstract void initializeChessPanel();

    abstract void undo();

    abstract void redo();

    abstract void startNewGame();

    abstract void startNewGame(String fenPosition);

    abstract void restoreDefaultValues(String fenPosition);

    abstract void makeDisplayMove(Move move, boolean isAiMove);

    abstract void showHintPositions(ChessPiece chessPiece);

    abstract void hideHintPositions();

    public abstract void placePieceToPosition(String position, ChessPiece chessPiece);

    abstract void removePieceFromPosition(String position);

    abstract void placePiecesToChessBoard(ChessBoard chessBoard);

    abstract void placePiecesToChessBoard(String fenPosition);

    abstract void disableChessPanelClicks();

}
