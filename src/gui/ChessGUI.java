package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
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
// import java.util.Timer;
// import java.util.TimerTask;
import java.util.HashMap;
import java.util.HashSet;

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
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.LineBorder;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import chess_board.ChessBoard;
import chess_board.Move;
import enumeration.AiType;
import enumeration.Allegiance;
import enumeration.GameMode;
import enumeration.GameResult;
import enumeration.GuiStyle;
import minimax_ai.MiniMaxAi;
import piece.Bishop;
import piece.ChessPiece;
import piece.EmptyTile;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;
import utility.Constants;
import utility.FenUtilities;
import utility.GameParameters;
import utility.InvalidFenFormatException;
import utility.ResourceLoader;
import utility.Utilities;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;


public class ChessGUI {
	
	public static GameParameters gameParameters = new GameParameters();
	public static GameParameters newGameParameters = new GameParameters(gameParameters);
	
	private static final String TITLE = "My Chess Game";
	
	private static final int NUM_OF_COLUMNS = Constants.DEFAULT_NUM_OF_COLUMNS;
	
	private static final int HEIGHT = Constants.DEFAULT_HEIGHT;
	private static final int WIDTH = Constants.DEFAULT_WIDTH;
	
	public static JFrame frame;
	public static JPanel gui = new JPanel();
	
	public static JToolBar tools = new JToolBar();

	public static JPanel chessBoardPanel;
	public static JPanel capturedPiecesPanel;
	
	public static String firstTurnText = "Turn: 1. White plays first.";
	public static JTextPane turnTextPane = new JTextPane();
	
	public static String zeroScoreText = "Score: 0";
	
	// The position (0, 0) of the chessBoardSquares,
	// corresponds to the position (NUM_OF_COLUMNS - 1, 0) of the ChessBoard's gameBoard.
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
	private static JMenuItem exitItem;
	
	private static JMenu helpMenu;
	private static JMenuItem howToPlayItem;
	private static JMenuItem aboutItem;
	
	// The position (0, 0) of the "chessBoard.getGameBoard()" is the lower left button 
	// of the JButton array "chessBoardSquares".
	// The position (chessBoard.getNumOfRows()-1, 0) of the "chessBoard.getGameBoard()" is the upper left button 
	// of the JButton array "chessBoardSquares".
	public static ChessBoard chessBoard = new ChessBoard();
	
	public static String startingPosition = "";
	public static String endingPosition = "";
	
	// These stacks of "ChessBoard" objects are used to handle the "undo" and "redo" functionality.
	public static Stack<ChessBoard> previousChessBoards = new Stack<>();
	public static Stack<ChessBoard> redoChessBoards = new Stack<>();

	// These stacks of "JLabel" arrays are used to handle the "undo" and "redo" functionality.
	public static Stack<JLabel[]> previousCapturedPiecesImages = new Stack<>();
	public static Stack<JLabel[]> redoCapturedPiecesImages = new Stack<>();
	
	public static boolean startingButtonIsClicked = false;
	public static Set<String> hintPositions = new HashSet<>();
	
	public static boolean buttonsEnabled = true;
	
	// This variable is used for the implementation of "Human Vs MiniMax AI".
	public static MiniMaxAi ai;
	
	// This variable is used for the implementation of "Human Vs Random AI".
	public static Allegiance randomAiAllegiance;
	
	// This variable is used for the implementation of MiniMax AI Vs MiniMax AI.
	public static boolean isGameOver;
	
	// This variable is true if the main is running from "ChessGui2" class.
	public static boolean isChessGui2;
	
	public static String savedFenPosition;

	// These stack of 2d "ChessPiece" arrays is used to check for a threefold repetition of a chess_board board position.
	public static Stack<ChessPiece[][]> halfMoveGameBoards = new Stack<>();
	public static Stack<ChessPiece[][]> redoHalfMoveGameBoards = new Stack<>();
	
	public static JLabel[] aiVsAiNewCapturedPiecesImages;
	
	public static GameResult gameResult;

	/*
	public static int whiteMinimaxAiMoveElapsedSecs;
	public static int blackMinimaxAiMoveElapsedSecs;

    public static double whiteMinimaxAiMoveAverageSecs;
    public static double blackMinimaxAiMoveAverageSecs;
	*/
	
	public ChessGUI(String title) {
		
		// Change JDialog style.
		// JDialog.setDefaultLookAndFeelDecorated(true);
		
		configureGuiStyle();
		
		initializeGui();
		
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getAiType() == AiType.MINIMAX_AI) {
			if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
				ai = new MiniMaxAi(gameParameters.getAi1MaxDepth(), Constants.BLACK);
			} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
				ai = new MiniMaxAi(gameParameters.getAi1MaxDepth(), Constants.WHITE);
			}
		}

		frame = new JFrame(title);
		frame.add(getGui());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		// ensures the frame is the minimum size it needs to be
		// in order display the components within it
		frame.pack();

		frame.setSize(new Dimension(WIDTH, HEIGHT));
		
		// ensures the minimum size is enforced.
		frame.setMinimumSize(frame.getSize());
		
		frame.setLocation((int) (Constants.SCREEN_SIZE.getWidth() - frame.getWidth()) / 2, 5);
		
		frame.setResizable(false);
		// frame.setFocusable(true);
		
		BufferedImage icon;
		try {
			icon = ImageIO.read(ResourceLoader.load(Constants.ICON_PATH));
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
		settingsItem = new JMenuItem("Preferences");
		importFenPositionItem = new JMenuItem("Import FEN Position");
		exportFenPositionItem = new JMenuItem("Export FEN Position to file");
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
            SettingsWindow settings = new SettingsWindow();
            settings.setVisible(true);
        });
		
		importFenPositionItem.addActionListener(e -> {
            String fenPosition = (String) JOptionPane.showInputDialog(frame,
                    "Please insert the \"FEN\" position in the text field below:" +
							"                      ",
					"Import FEN Position",
					QUESTION_MESSAGE, null, null,
					Constants.DEFAULT_STARTING_FEN_POSITION);
            
            if (fenPosition != null) {
                // gameParameters.getNumOfRows() = Constants.DEFAULT_NUM_OF_ROWS;
                startNewGame();
                placePiecesToChessBoard(fenPosition);
            }
            
        });
		
		exportFenPositionItem.addActionListener(e -> {
            String exportedFenFilename = (String) JOptionPane.showInputDialog(frame,
                    "Please type the name of the export file:",
					"Export FEN Position", QUESTION_MESSAGE, null, null,
                    "exported_FEN_position.txt");
            
            String fenPosition = FenUtilities.getFenPositionFromChessBoard(chessBoard);

            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(exportedFenFilename));
                bw.write(fenPosition + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                	if (bw != null) {
						bw.flush();
						bw.close();
					}
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
		
		saveCheckpointItem.addActionListener(e -> {
            if (!chessBoard.isTerminalState()) {
                savedFenPosition = FenUtilities.getFenPositionFromChessBoard(chessBoard);
                loadCheckpointItem.setEnabled(true);
            }
        });
		
		loadCheckpointItem.addActionListener(e -> {				
            if (savedFenPosition != null) {
                startNewGame();
                placePiecesToChessBoard(savedFenPosition);
            }
        });

		exitItem.addActionListener(e -> System.exit(0));
		
		howToPlayItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, Constants.RULES, "How to Play",
										JOptionPane.INFORMATION_MESSAGE));
		
		aboutItem.addActionListener(e -> {
            JLabel label = new JLabel("<html>A traditional chess game implementation using Minimax AI,<br>"
                    + "with Alpha-Beta Pruning.<br>© Created by: Christos Kormaris, Athens 2020<br>"
                    + "Version " + Constants.VERSION + "</html>");
            
            BufferedImage img = null;
            try {
                img = ImageIO.read(ResourceLoader.load(Constants.ICON_PATH));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Image dImg = img.getScaledInstance(Constants.CHESS_SQUARE_PIXEL_SIZE, Constants.CHESS_SQUARE_PIXEL_SIZE, Image.SCALE_SMOOTH);
            ImageIcon icon1 = new ImageIcon(dImg);
            
            JOptionPane.showMessageDialog(frame, label, "About", JOptionPane.PLAIN_MESSAGE, icon1);
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
	
	
	public static void setTurnMessage() {
		if (chessBoard.getHalfMoveNumber() == 1) {
    		turnTextPane.setText(firstTurnText);
        } else {
            String turnMessage = "Turn: " + (int) Math.ceil((float) chessBoard.getHalfMoveNumber() / 2) + ". ";
            turnMessage += (chessBoard.whitePlays()) ? "White plays." : "Black plays.";

            if (chessBoard.whitePlays() && chessBoard.isWhiteKingInCheck())
                turnMessage += " White king is in check!";
            else if (chessBoard.blackPlays() && chessBoard.isBlackKingInCheck())
                turnMessage += " Black king is in check!";
            
    		turnTextPane.setText(turnMessage);
        }
	}
	

	/*
	public static void updateMinimaxAiMoveElapsedSecs() {
		setTurnMessage();

		String turnMessage = turnTextPane.getText();

		if (chessBoard.whitePlays() &&
				(gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
				|| gameParameters.getGameMode() == GameMode.AI_VS_AI && gameParameters.getAiType() == AiType.MINIMAX_AI)) {
	    	whiteMinimaxAiMoveElapsedSecs++;
			turnMessage += " Minimax AI has been thinking for: " + whiteMinimaxAiMoveElapsedSecs + " secs";
	    	// System.out.println("whiteMinimaxAiMoveElapsedSecs: " + whiteMinimaxAiMoveElapsedSecs);
		} else if (chessBoard.blackPlays() &&
				(gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
				|| gameParameters.getGameMode() == GameMode.AI_VS_AI && gameParameters.getAiType() == AiType.MINIMAX_AI)) {
			blackMinimaxAiMoveElapsedSecs++;
			turnMessage += " Minimax AI has been thinking for: " + blackMinimaxAiMoveElapsedSecs + " secs";
	    	// System.out.println("blackMinimaxAiMoveElapsedSecs: " + blackMinimaxAiMoveElapsedSecs);
		}
		
		turnTextPane.setText(turnMessage);
	}
	*/
	
	
	public static void setScoreMessage() {
		if (chessBoard.getScore() == 0) {
			capturedPiecesImages[15].setText(zeroScoreText);
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
		if (!previousChessBoards.isEmpty()) {
			System.out.println("Undo is pressed!");
			
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI || gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);
			
				int startingPositionRow = Utilities.getRowFromPosition(startingPosition);
				int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
				
				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares[startingPositionRow][NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, NUM_OF_COLUMNS - 1 - startingPositionColumn);
				} else {
					startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);				
				}
				
				changeTileColor(startingButton, startingButtonColor);
			}
			
			redoChessBoards.push(new ChessBoard(chessBoard));
			
			// Push to the redoCapturedPiecesImages Stack.
			JLabel[] newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			redoCapturedPiecesImages.push(newCapturedPiecesImages);
			
			chessBoard = previousChessBoards.pop();
			
			ChessPiece[][] halfMoveGameBoard = halfMoveGameBoards.pop();
			redoHalfMoveGameBoards.push(Utilities.copyGameBoard(halfMoveGameBoard));
			// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());
			
			// Display the "undo" captured chess_board pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = previousCapturedPiecesImages.pop();
			for (int i=0; i<31; i++) {
			    capturedPiecesPanel.add(capturedPiecesImages[i]);
			}
			
			// This is true if any terminal state has occurred.
			// The terminal states are: "draw", "stalemate draw" & "checkmate"
            if (!buttonsEnabled && gameParameters.getGameMode() != GameMode.AI_VS_AI) {
				enableChessBoardButtons();
			}
			
			// Display the "undo" chess_board board.
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					placePieceToPosition(Utilities.getPositionByRowCol(i, j), chessBoard.getGameBoard()[i][j]);		
				}
			}
			
			System.out.println();
			System.out.println(chessBoard);
			
			setTurnMessage();
			setScoreMessage();

			if (previousChessBoards.isEmpty()) {
				undoItem.setEnabled(false);
			}
			
			if (redoItem != null)
				redoItem.setEnabled(true);
		}
	}
	
	
	// NOTE: We are not able to perform a redo,
	// if we are in a terminal state, because the game has ended.
	private static void redoNextMove() {
		if (!redoChessBoards.isEmpty()) {
			System.out.println("Redo is pressed!");
			
			if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI || gameParameters.getGameMode() == GameMode.HUMAN_VS_HUMAN) {
				startingButtonIsClicked = false;
				hideHintPositions(hintPositions);
				
				int startingPositionRow = Utilities.getRowFromPosition(startingPosition);
				int startingPositionColumn = Utilities.getColumnFromPosition(startingPosition);
				
				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares[startingPositionRow][NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, NUM_OF_COLUMNS - 1 - startingPositionColumn);
				} else {
					startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);
				}
	
				changeTileColor(startingButton, startingButtonColor);
			}
			
			previousChessBoards.push(new ChessBoard(chessBoard));

			ChessPiece[][] halfMoveGameBoard = redoHalfMoveGameBoards.pop();
			halfMoveGameBoards.push(Utilities.copyGameBoard(halfMoveGameBoard));
			// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());
			
			// Push to the previousCapturedPiecesImages Stack.
			JLabel[] newCapturedPiecesImages = new JLabel[31];
			for (int i=0; i<=30; i++) {
				newCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
			}
			previousCapturedPiecesImages.push(newCapturedPiecesImages);
			
			ChessBoard redoChessBoard = redoChessBoards.pop();

			// Display the "redo" captured chess_board pieces icons.
			initializeCapturedPiecesPanel();
			capturedPiecesImages = redoCapturedPiecesImages.pop();
			for (int i=0; i<31; i++) {
			    capturedPiecesPanel.add(capturedPiecesImages[i]);
			}
			
			if (redoChessBoards.isEmpty()) {
				redoItem.setEnabled(false);
			}
			
			// Display the "redo" chess_board board.
			for (int i=0; i<redoChessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					placePieceToPosition(Utilities.getPositionByRowCol(i, j), redoChessBoard.getGameBoard()[i][j]);		
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
		String gifName = JOptionPane.showInputDialog("Please type the exported \".gif\" file name:", "chess_board.gif");
		
		BufferedImage bi = new BufferedImage(gui.getSize().width, gui.getSize().height, BufferedImage.TYPE_INT_ARGB); 
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
	
	
	private static void initializeTurnTextPaneBar() {
		if (tools != null)
			gui.remove(tools);
		
		tools = new JToolBar();
		tools.setFloatable(false);
		
		turnTextPane.setEditable(false);
		centerTextPaneAndMakeBold(turnTextPane);
		
		tools.add(turnTextPane);

		gui.add(tools, BorderLayout.NORTH);
	}
	
	private static void centerTextPaneAndMakeBold(JTextPane textPane) {
		// Center textPane
		StyledDocument style = textPane.getStyledDocument();
		SimpleAttributeSet align = new SimpleAttributeSet();
		StyleConstants.setAlignment(align, StyleConstants.ALIGN_CENTER);
		style.setParagraphAttributes(0, style.getLength(), align, false);
		
		// Make textPane bold
		MutableAttributeSet attrs = textPane.getInputAttributes();
	    StyleConstants.setBold(attrs, true); 
	    textPane.getStyledDocument().setCharacterAttributes(0, style.getLength(), attrs, false);
	}
	
	
	public static void initializeChessBoardPanel() {
		if (chessBoardPanel != null)
			gui.remove(chessBoardPanel);
		chessBoardPanel = new JPanel(new GridLayout(gameParameters.getNumOfRows()+2, NUM_OF_COLUMNS+2));
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

		chessBoardSquares = new JButton[gameParameters.getNumOfRows()][NUM_OF_COLUMNS];
		
		// Create the chess_board board square buttons.
		Insets buttonMargin = new Insets(0, 0, 0, 0);
		for (int i=0; i<gameParameters.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				JButton button = new JButton();
				button.setMargin(buttonMargin);
				
				// Our chess_board pieces are 64x64 px in size, so we'll
				// "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(
						Constants.CHESS_SQUARE_PIXEL_SIZE, Constants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				button.setIcon(icon);
				
				Color color = getColorByRowCol(i, j);
				
				button.setBackground(color);
				
				// button.setBorderPainted(false);
				button.setFocusPainted(false);
				// button.setRolloverEnabled(true);
				// button.setOpaque(false);
				
				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = i;
					column = Constants.DEFAULT_NUM_OF_COLUMNS - 1 - j;
				} else {
					row = chessBoard.getNumOfRows() - 1 - i;
					column = j;
				}
				
				button.addActionListener(e -> chessButtonClick(row, column, button));
				
				chessBoardSquares[i][j] = button;
			}
		}

		// fill the chess_board board panel
		chessBoardPanel.add(new JLabel(""));
		
		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j=NUM_OF_COLUMNS-1; j>=0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
			}
		} else {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
			}
		}
		
		chessBoardPanel.add(new JLabel(""));
		// fill the black non-pawn chessPiece row
		for (int i=0; i<gameParameters.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS+1; j++) {
				switch (j) {
					case 0:
						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							chessBoardPanel.add(new JLabel("" + (i + 1), SwingConstants.CENTER));
						} else {
							chessBoardPanel.add(new JLabel("" + (gameParameters.getNumOfRows() - i), SwingConstants.CENTER));
						}
						chessBoardPanel.add(chessBoardSquares[i][j]);
						break;
					case NUM_OF_COLUMNS:
						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							chessBoardPanel.add(new JLabel("" + (i + 1), SwingConstants.CENTER));
						} else {
							chessBoardPanel.add(new JLabel("" + (gameParameters.getNumOfRows() - i), SwingConstants.CENTER));
						}
						break;
					default:
						chessBoardPanel.add(chessBoardSquares[i][j]);
				}
			}
		}
		
		// fill the bottom row
		chessBoardPanel.add(new JLabel(""));
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			for (int j=NUM_OF_COLUMNS-1; j>=0; j--) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
			}
		} else {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				chessBoardPanel.add(new JLabel(String.valueOf((char) (65+j)), SwingConstants.CENTER));
			}
		}
	}

	public static void initializeCapturedPiecesImages() {

		capturedPiecesImages = new JLabel[31];
		
		// Create the captured chess_board pieces icons.
		for (int i=0; i<31; i++) {
			capturedPiecesImages[i] = new JLabel();
			
			if (i == 15) {
				capturedPiecesImages[i].setText(zeroScoreText);
			} else {
				// We'll "fill this in" using a transparent icon...
				ImageIcon icon = new ImageIcon(new BufferedImage(Constants.CAPTURED_PIECE_PIXEL_SIZE, Constants.CAPTURED_PIECE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				capturedPiecesImages[i].setIcon(icon);
				
				// This is for TESTING.
				// ImageIcon pieceImage = preparePieceIcon(Constants.WHITE_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
				// capturedPiecesImages[i].setIcon(pieceImage);
			}

		    capturedPiecesPanel.add(capturedPiecesImages[i]);
		}
		
	}
	
	public static void startNewGame() {
		System.out.println("Starting new game!");
		
		gameParameters = new GameParameters(newGameParameters);
		
		if (undoItem != null) {
			undoItem.setEnabled(false);
		}
		if (redoItem != null) {
			redoItem.setEnabled(false);
		}
		
		configureGuiStyle();
		
		/* If running "ChessGUI.java", you must use this! */
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
		
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI 
				&& gameParameters.getAiType() == AiType.MINIMAX_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			minimaxAiMove(ai);
		} else if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI 
				&& gameParameters.getAiType() == AiType.RANDOM_AI
				&& gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			randomAiMove(Allegiance.WHITE);
		} else if (gameParameters.getGameMode() == GameMode.AI_VS_AI) {
			playAiVsAi();
		}
		
	}

	
	// Restores all the default values.
	public static void restoreDefaultValues() {
		chessBoard = new ChessBoard();
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

		previousChessBoards.clear();
		previousCapturedPiecesImages.clear();
		redoChessBoards.clear();
		redoCapturedPiecesImages.clear();
		
		halfMoveGameBoards.clear();
		redoHalfMoveGameBoards.clear();
		
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
			if (gameParameters.getAiType() == AiType.MINIMAX_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					ai = new MiniMaxAi(gameParameters.getAi1MaxDepth(), Constants.BLACK);
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					ai = new MiniMaxAi(gameParameters.getAi1MaxDepth(), Constants.WHITE);
				}
			} else if (gameParameters.getAiType() == AiType.RANDOM_AI) {
				if (gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE) {
					randomAiAllegiance = Allegiance.BLACK;
				} else if (gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					randomAiAllegiance = Allegiance.WHITE;
				}
			}
		}
		
		isGameOver = false;
		
		gameResult = GameResult.NONE;

		setTurnMessage();
		
		// whiteMinimaxAiMoveAverageSecs = 0;
		// blackMinimaxAiMoveAverageSecs = 0;
	}
	
	
	// This method is only called from inside a chess_board button listener.
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
					
					// if (chessPiece instanceof King)
					// 	System.out.println("hint positions: " + hintPositions);
					
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
					}
					else if (chessBoard.blackPlays() 
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
						
						JButton hintPositionButton;
						
						if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
							hintPositionButton = 
								chessBoardSquares[hintPositionRow][NUM_OF_COLUMNS - 1 - hintPositionColumn];
						} else {
							hintPositionButton = 
								chessBoardSquares[chessBoard.getNumOfRows() - 1 - hintPositionRow][hintPositionColumn];	
						}
						
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
			
		} else if (startingButtonIsClicked && startingPiece != null &&
				(startingPiece.getAllegiance() == Allegiance.WHITE && chessBoard.whitePlays()
				 || startingPiece.getAllegiance() == Allegiance.BLACK && chessBoard.blackPlays())) {
			
			startingButtonIsClicked = false;
			
			endingPosition = position;
			// System.out.println("endingPosition: " + endingPosition);
			
			if (startingPosition.equals(endingPosition)
				|| !hintPositions.contains(endingPosition)) {
				
				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares[startingPositionRow][NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, NUM_OF_COLUMNS - 1 - startingPositionColumn);
				} else {
					startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() -1 - startingPositionRow, startingPositionColumn);
				}
				
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
					chessBoard.makeMove(move, true);
					
					// Store the chess_board board of the HalfMove that was just made.
					ChessPiece[][] halfMoveGameBoard = Utilities.copyGameBoard(chessBoard.getGameBoard());
					halfMoveGameBoards.push(halfMoveGameBoard);
					// System.out.println("size of halfMoveGameBoards: " + halfMoveGameBoards.size());
					
					hideHintPositions(hintPositions);
				}
				
				JButton startingButton;
				Color startingButtonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					startingButton = chessBoardSquares[startingPositionRow][NUM_OF_COLUMNS - 1 - startingPositionColumn];
					startingButtonColor = getColorByRowCol(startingPositionRow, NUM_OF_COLUMNS - 1 - startingPositionColumn);
				} else {
					startingButton = chessBoardSquares[chessBoard.getNumOfRows() - 1 - startingPositionRow][startingPositionColumn];
					startingButtonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - startingPositionRow, startingPositionColumn);
				}
				
				// System.out.println("startingButtonColor: " + startingButtonColor);
				changeTileColor(startingButton, startingButtonColor);
				
			}
			
			if (checkForGameOver()) return;

			if (gameParameters.isEnableSounds()) {
				Utilities.playSound("piece_move.wav");
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
				}
								
                if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {

                    /* MiniMax AI implementation here. */
                    if (gameParameters.getAiType() == AiType.MINIMAX_AI) {
                        // System.out.println("INSIDE MINIMAX AI");
                        minimaxAiMove(ai);
                    }

                    /* Random AI implementation here. */
                    else if (gameParameters.getAiType() == AiType.RANDOM_AI) {
                        // System.out.println("INSIDE RANDOM AI");
                        randomAiMove(randomAiAllegiance);
                    }
                    
                }
                
				setScoreMessage();
				
			}
		}
	}
	
	
	public static void addCapturedPieceImage(ChessPiece endTile) {
 		ImageIcon pieceImage = null;
 		
		if (chessBoard.getPromotedPieces().contains(endTile)) {
			if (endTile.getAllegiance() == Allegiance.WHITE) {
				pieceImage = preparePieceIcon(Constants.WHITE_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
			} else if (endTile.getAllegiance() == Allegiance.BLACK) {
				pieceImage = preparePieceIcon(Constants.BLACK_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
			}
		
		} else if (endTile.getAllegiance() == Allegiance.WHITE) {
		
	 			if (endTile instanceof Pawn) {
	 				pieceImage = preparePieceIcon(Constants.WHITE_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
	 			} else if (endTile instanceof Rook) {
	 				pieceImage = preparePieceIcon(Constants.WHITE_ROOK_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
	 			} else if (endTile instanceof Knight) {
	 				pieceImage = preparePieceIcon(Constants.WHITE_KNIGHT_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
	 			} else if (endTile instanceof Bishop) {
	 				pieceImage = preparePieceIcon(Constants.WHITE_BISHOP_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
	 			} else if (endTile instanceof Queen) {
	 				pieceImage = preparePieceIcon(Constants.WHITE_QUEEN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
	 			}
	 			
 		} else if (endTile.getAllegiance() == Allegiance.BLACK) {
 			if (endTile instanceof Pawn) {
 				pieceImage = preparePieceIcon(Constants.BLACK_PAWN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 			} else if (endTile instanceof Rook) {
 				pieceImage = preparePieceIcon(Constants.BLACK_ROOK_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 			} else if (endTile instanceof Knight) {
 				pieceImage = preparePieceIcon(Constants.BLACK_KNIGHT_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 			} else if (endTile instanceof Bishop) {
 				pieceImage = preparePieceIcon(Constants.BLACK_BISHOP_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 			} else if (endTile instanceof Queen) {
 				pieceImage = preparePieceIcon(Constants.BLACK_QUEEN_IMG_PATH, Constants.CAPTURED_PIECE_PIXEL_SIZE);
 			}
		}
		
		if (endTile.getAllegiance() == Allegiance.WHITE) {
			capturedPiecesImages[chessBoard.getWhiteCapturedPiecesCounter()].setIcon(pieceImage);
		} else if (endTile.getAllegiance() == Allegiance.BLACK) {
			capturedPiecesImages[31 - chessBoard.getBlackCapturedPiecesCounter() - 1].setIcon(pieceImage);
		}
		
		setScoreMessage();
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
					Utilities.playSound("checkmate.wav");
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
					Utilities.playSound("checkmate.wav");
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
			
			int dialogResult = JOptionPane.showConfirmDialog(gui, 
					"It is a draw due to insufficient mating material! Start a new game?", "Draw", JOptionPane.YES_NO_OPTION);
			
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
		// This situation occurs when we end up with the same chess_board board position 3 different times
		// at any time in the game, not necessarily successively.
		if (checkForThreefoldRepetitionDraw()) {
			int dialogResult = -1;

			if (!chessBoard.whitePlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE
					|| !chessBoard.blackPlays() && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI) {
				dialogResult = JOptionPane.showConfirmDialog(gui,
						"Threefold repetition of the same chess board position has occurred! "
								+ "Do you want to declare a draw?", "Draw", JOptionPane.YES_NO_OPTION);
			}

			// System.out.println("dialogResult:" + dialogResult);
			if (dialogResult == JOptionPane.YES_OPTION) {
				gameResult = GameResult.THREEFOLD_REPETITION_DRAW;
				showDeclareDrawDialog();
				return true;
			}

		}

		return false;
	}


	public static boolean checkForThreefoldRepetitionDraw() {
		
		if (!halfMoveGameBoards.isEmpty()) {
			int N = halfMoveGameBoards.size();
			for (int i=0; i<N - 1; i++) {
				int numOfRepeats = 0;
				for (int j=i; j<N; j++) {
					// Skip the iteration where i=j, 
					// and the last iteration, if the number of repeats found is less than 2.
					// The number of comparisons will be: (N 2) = N * (N-1) / 2
					if (i != j && !(numOfRepeats < 2 && j == N - 1)) {
						// System.out.println("i: " + i + ", j: " + j);
						ChessPiece[][] halfMoveGameBoard1 = halfMoveGameBoards.get(i);
						ChessPiece[][] halfMoveGameBoard2 = halfMoveGameBoards.get(j);
						if (Utilities.checkEqualGameBoards(halfMoveGameBoard1, halfMoveGameBoard2)) {
							// System.out.println("i: " + i + ", j: " + j);
							// ChessBoard.printChessBoard(halfMoveGameBoard1);
							numOfRepeats++;
						}
					}
				}
				// System.out.println("numOfRepeats: " + numOfRepeats);
				if (numOfRepeats >= 3) return true; 
			}
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
	

	/*
	private static void calculateAverageMinimaxAiMoveSecs() {
		if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI || gameParameters.getGameMode() == GameMode.AI_VS_AI)
				&& gameParameters.getAiType() == AiType.MINIMAX_AI) {
			
			whiteMinimaxAiMoveAverageSecs = whiteMinimaxAiMoveAverageSecs / Math.ceil((double) chessBoard.getHalfMoveNumber() / 2.0);
			blackMinimaxAiMoveAverageSecs = blackMinimaxAiMoveAverageSecs / Math.floor((double) chessBoard.getHalfMoveNumber() / 2.0);
			
			if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK 
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI))
				System.out.println("White Minimax AI move Average seconds: " + whiteMinimaxAiMoveAverageSecs);
			
			if ((gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.WHITE 
					|| gameParameters.getGameMode() == GameMode.AI_VS_AI))
				System.out.println("Black Minimax AI move Average seconds: " + blackMinimaxAiMoveAverageSecs);
		}
	}
	*/

	
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


	public static void randomAiMove(Allegiance aiAllegiance) {

		String randomAiStartingPosition = "";
		String randomAiEndingPosition = "";
		
		// This map is used for the Random AI implementation.
		Map<String, Set<String>> randomStartingEndingPositions = new HashMap<>();
		
		/* STEP 1. Random starting position. */
		if (chessBoard.whitePlays() && aiAllegiance == Allegiance.WHITE && !chessBoard.isWhiteKingInCheck() 
				|| 
			chessBoard.blackPlays() && aiAllegiance == Allegiance.BLACK && !chessBoard.isBlackKingInCheck()) {
			for (int i=0; i<chessBoard.getNumOfRows(); i++) {
				for (int j=0; j<NUM_OF_COLUMNS; j++) {
					if (aiAllegiance == Allegiance.WHITE 
						&& chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.WHITE
							||
						aiAllegiance == Allegiance.BLACK 
						&& chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.BLACK) {
						
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
			List<String> keys = new ArrayList<>(randomStartingEndingPositions.keySet());
			if (randomStartingEndingPositions.size() > 0) {
				int randomStartingPositionIndex = r.nextInt(randomStartingEndingPositions.size());
				// System.out.println("randomStartingPositionIndex: " + randomStartingPositionIndex);
				randomAiStartingPosition = keys.get(randomStartingPositionIndex);
			}
			
		}
		// If the AI King is in check, then get one of the following valid moves.
		else if (chessBoard.whitePlays() && aiAllegiance == Allegiance.WHITE && chessBoard.isWhiteKingInCheck() 
					|| 
				 chessBoard.blackPlays() && aiAllegiance == Allegiance.BLACK && chessBoard.isBlackKingInCheck()) {
			// System.out.println("chessBoard.blackKingInCheckValidPieceMoves: " + chessBoard.blackKingInCheckValidPieceMoves);
			Random r = new Random();
			List<String> keys = new ArrayList<>();
			int randomStartingPositionIndex = 0;
			
			if (chessBoard.whitePlays()) {
				keys = new ArrayList<>(chessBoard.getWhiteKingInCheckValidPieceMoves().keySet());
				randomStartingPositionIndex = r.nextInt(chessBoard.getWhiteKingInCheckValidPieceMoves().size());
			} else if (chessBoard.blackPlays()) {
				keys = new ArrayList<>(chessBoard.getBlackKingInCheckValidPieceMoves().keySet());
				randomStartingPositionIndex = r.nextInt(chessBoard.getBlackKingInCheckValidPieceMoves().size());
			}
			
			randomAiStartingPosition = keys.get(randomStartingPositionIndex);
		}
		// System.out.println("random starting position: " + randomAiStartingPosition);
		
		
		/* STEP 2. Random ending position. */
		Set<String> possibleEndingPositions = new HashSet<>();
		if (chessBoard.whitePlays() && !chessBoard.isWhiteKingInCheck()
				||
			chessBoard.blackPlays() && !chessBoard.isBlackKingInCheck()) {
			possibleEndingPositions = randomStartingEndingPositions.get(randomAiStartingPosition);
		} else {
			if (chessBoard.whitePlays()) {
				possibleEndingPositions = chessBoard.getWhiteKingInCheckValidPieceMoves().get(randomAiStartingPosition);
			} else if (chessBoard.blackPlays()) {
				possibleEndingPositions = chessBoard.getBlackKingInCheckValidPieceMoves().get(randomAiStartingPosition);
			}
		}
		
		// Get a random element from the set.
		Random r = new Random();
		int randomEndingPositionIndex = r.nextInt(possibleEndingPositions.size());
		// System.out.println("randomEndingPositionIndex: " + randomEndingPositionIndex);
		int i = 0;
		for (String possibleEndingPosition: possibleEndingPositions) {
		    if (i == randomEndingPositionIndex) {
		    	randomAiEndingPosition = possibleEndingPosition;
		    	break;
		    }
		    i++;
		}
		// System.out.println("random ending position: " + randomAiEndingPosition);
		
		// chessBoard.movePieceFromAPositionToAnother(randomAiStartingPosition, randomAiEndingPosition, true);
		// hideHintPositions(possibleEndingPositions);
		
		Move move = new Move(randomAiStartingPosition, randomAiEndingPosition, chessBoard.evaluate());
		
		chessBoard.makeMove(move, true);
		
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
		
		System.out.println();
		System.out.println(chessBoard);
	}
	
	
	// Gets called after the human player makes a move. It makes a Minimax AI move.
	public static void minimaxAiMove(MiniMaxAi ai) {
	    /*
		if (chessBoard.whitePlays()) {
			whiteMinimaxAiMoveElapsedSecs = -1;
		} else if (chessBoard.blackPlays()) {
			blackMinimaxAiMoveElapsedSecs = -1;
		}
		Timer timer = initializeMinimaxAiMoveTimer();
	    */

		setTurnMessage();

		chessBoardPanel.revalidate();
		chessBoardPanel.repaint();
		
		Move aiMove;
		if (chessBoard.whitePlays()) {
			aiMove = ai.miniMax(chessBoard);
		} else {
			aiMove = ai.miniMaxAlphaBeta(chessBoard);
		}
		System.out.println("aiMove: " + aiMove);
		// System.out.println("lastCapturedPieceValue: " + chessBoard.getLastCapturedPieceValue());
		
		chessBoard.makeMove(aiMove, true);
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
		
		System.out.println();
		System.out.println(chessBoard);
	}
	
	
	public static void playAiVsAi() {
		MiniMaxAi ai1 = new MiniMaxAi(gameParameters.getAi1MaxDepth(), Constants.WHITE);
		MiniMaxAi ai2 = new MiniMaxAi(gameParameters.getAi2MaxDepth(), Constants.BLACK);
				
		while (!isGameOver) {
			System.out.println(turnTextPane.getText());
			aiVsAiMove(ai1);
			
			if (!isGameOver) {
				System.out.println(turnTextPane.getText());
				
				try {
					if (gameParameters.getAiType() == AiType.MINIMAX_AI) {
						Thread.sleep(Constants.MINIMAX_AI_MOVE_MILLISECONDS);
					} else if (gameParameters.getAiType() == AiType.RANDOM_AI) {
						Thread.sleep(Constants.RANDOM_AI_MOVE_MILLISECONDS);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				aiVsAiMove(ai2);
			}
			
			if (!isGameOver) {
				try {
					if (gameParameters.getAiType() == AiType.MINIMAX_AI) {
						Thread.sleep(Constants.MINIMAX_AI_MOVE_MILLISECONDS);
					} else if (gameParameters.getAiType() == AiType.RANDOM_AI) {
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
	

	private static void aiVsAiMove(MiniMaxAi ai) {

		Allegiance allegiance = (ai.getAiPlayer() == Constants.WHITE) ? Allegiance.WHITE : Allegiance.BLACK;

		previousChessBoards.push(new ChessBoard(chessBoard));
		
		// Push to the previousCapturedPiecesImages Stack.
		aiVsAiNewCapturedPiecesImages = new JLabel[31];
		for (int i=0; i<=30; i++) {
			aiVsAiNewCapturedPiecesImages[i] = new JLabel(capturedPiecesImages[i].getIcon());
		}
		previousCapturedPiecesImages.push(aiVsAiNewCapturedPiecesImages);
		
		// System.out.println("white plays: " + chessBoard.whitePlays());
		if (gameParameters.getAiType() == AiType.MINIMAX_AI) {
			minimaxAiMove(ai);
		} else {
			randomAiMove(allegiance);
		}
		halfMoveGameBoards.push(Utilities.copyGameBoard(chessBoard.getGameBoard()));
		
		setTurnMessage();
		setScoreMessage();

		chessBoardPanel.revalidate();
		chessBoardPanel.repaint();
	}


	public static void hideHintPositions(Set<String> positionsToHide) {
		if (positionsToHide != null && positionsToHide.size() != 0) {
			for (String hintPosition: positionsToHide) {
				// System.out.println("hide hint position: " + hintPosition);
				int row = Utilities.getRowFromPosition(hintPosition);
				int column = Utilities.getColumnFromPosition(hintPosition);
				// System.out.println("hide hint row: " + row + ", hide hint column: " + column);
				
				JButton button;
				Color buttonColor;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					button = chessBoardSquares[row][NUM_OF_COLUMNS - 1 - column];
					buttonColor = getColorByRowCol(row, NUM_OF_COLUMNS - 1 - column);
				} else {
					button = chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column];
					buttonColor = getColorByRowCol(chessBoard.getNumOfRows() - 1 - row, column);	
				}
				
				changeTileColor(button, buttonColor);
			}
		}
	}


	private static void changeTileColor(JButton button, Color color) {
		button.setBackground(color);
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
	
	
	public static ImageIcon preparePieceIcon(String imagePath, int size) {
		ImageIcon pieceIcon = new ImageIcon(ResourceLoader.load(imagePath));
		Image image = pieceIcon.getImage(); // transform it 
		 // scale it the smooth way
		Image newImg = image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		pieceIcon = new ImageIcon(newImg);  // transform it back
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
		
		ImageIcon pieceImage = preparePieceIcon(imagePath, Constants.CHESS_SQUARE_PIXEL_SIZE);
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// System.out.println("chessBoardSquares.length: " + chessBoardSquares.length);
		// System.out.println("chessBoardSquares[0].length: " + chessBoardSquares[0].length);
		
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardSquares[row][NUM_OF_COLUMNS - 1 - column].setIcon(pieceImage);
		} else {
			chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(pieceImage);
		}
		
		chessBoard.getGameBoard()[row][column] = chessPiece;
	}
	
	
	// It removes the given chessPiece from the board (both the data structure and the GUI).
	public static void removePieceFromPosition(String position) {
		
		// int column = (int) Character.toUpperCase(position.charAt(0)) - 65;
		// int row = N - Character.getNumericValue(position.charAt(1));
		
		int column = Utilities.getColumnFromPosition(position);
		int row = Utilities.getRowFromPosition(position);
		
		// Our chess_board pieces are 64x64 px in size, so we'll
		// 'fill this in' using a transparent icon.
		ImageIcon icon = new ImageIcon(new BufferedImage(
				Constants.CHESS_SQUARE_PIXEL_SIZE, Constants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
		
		if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
			chessBoardSquares[row][NUM_OF_COLUMNS - 1 - column].setIcon(icon);
		} else {
			chessBoardSquares[chessBoard.getNumOfRows() - 1 - row][column].setIcon(icon);	
		}
		
		chessBoard.getGameBoard()[row][column] = new EmptyTile();
	}
	
	
	public static void placePiecesToChessBoard() {
		
		for (int j=0; j<Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
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
		
		for (int j=0; j<Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
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
		
		ChessPiece[][] halfMoveGameBoard = Utilities.copyGameBoard(chessBoard.getGameBoard());
		halfMoveGameBoards.push(halfMoveGameBoard);
	}
	
	
	public static void placePiecesToChessBoard(String fenPosition) {
		
		try {
			chessBoard = FenUtilities.getChessBoardFromFenPosition(fenPosition);
		} catch (InvalidFenFormatException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
				String piecePosition = Utilities.getPositionByRowCol(i, j);
				placePieceToPosition(piecePosition, chessBoard.getGameBoard()[i][j]);
			}
		}
		chessBoard.setThreats();
		
		setTurnMessage();
		
		ChessPiece[][] halfMoveGameBoard = Utilities.copyGameBoard(chessBoard.getGameBoard());
		halfMoveGameBoards.push(halfMoveGameBoard);
	}
	
	
	public static void makeChessBoardSquaresEmpty() {
		for (int i=0; i<gameParameters.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(true);
				
				// Our chess_board pieces are 64x64 px in size, so we'll
				// 'fill this in' using a transparent icon.
				ImageIcon icon = new ImageIcon(new BufferedImage(
						Constants.CHESS_SQUARE_PIXEL_SIZE, Constants.CHESS_SQUARE_PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB));
				chessBoardSquares[i][j].setIcon(icon);
				
				Color color = getColorByRowCol(i, j);
				chessBoardSquares[i][j].setBackground(color);
				chessBoardSquares[i][j].setOpaque(true);
				// chessBoardSquares[i][j].setBorderPainted(false);
				
				chessBoard.getGameBoard()[gameParameters.getNumOfRows() - 1 - i][j] = new EmptyTile();
				chessBoard.setThreats();
			}
		}
	}
	
	
	public static void enableChessBoardButtons() {
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				JButton button = chessBoardSquares[i][j];
				button.setEnabled(true);
				
				int row;
				int column;
				if (gameParameters.getGameMode() == GameMode.HUMAN_VS_AI && gameParameters.getHumanPlayerAllegiance() == Allegiance.BLACK) {
					row = i;
					column = NUM_OF_COLUMNS - 1 - j;
				} else {
					row = chessBoard.getNumOfRows() - 1 - i;
					column = j;
				}
				
				button.addActionListener(e -> chessButtonClick(row, column, button));
			}
		}
		buttonsEnabled = true;
	}
	
	
	public static void disableChessBoardSquares() {
		for (int i=0; i<chessBoard.getNumOfRows(); i++) {
			for (int j=0; j<NUM_OF_COLUMNS; j++) {
				// chessBoardSquares[i][j].setEnabled(false);
				
				chessBoardSquares[i][j].removeActionListener(chessBoardSquares[i][j].getActionListeners()[0]);
			}
		}
		buttonsEnabled = false;
	}
	
	
	public final JComponent getGui() {
		return gui;
	}
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(TITLE);
		placePiecesToChessBoard();
		
		System.out.println(chessBoard);
	}

}
