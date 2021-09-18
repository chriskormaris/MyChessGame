package gui2;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

/*  www.java2s.com*/
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ai.RandomChoiceAI;
import enumeration.AiType;
import gui.ChessGUI;
import enumeration.GameMode;
import ai.MiniMaxAI;
import utility.Constants;


public class ChessGui2 extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -5684370471793136759L;

    private final static String title = "My Chess Game GUI 2";

    private static final int numOfRows = ChessGUI.gameParameters.getNumOfRows();
    private final static int numOfColumns = Constants.DEFAULT_NUM_OF_COLUMNS;

    private static final int HEIGHT = Constants.DEFAULT_HEIGHT;
    private static final int WIDTH = Constants.DEFAULT_WIDTH;

    // static int SIZE = 75;

    public static JLabel labelMessage = new JLabel("Turn 1, White plays first.");


    public ChessGui2() {
        super(new GridLayout(numOfRows + 2, numOfColumns + 2));
        // ChessGUI.gameParameters.getGameMode() = Constants.HumanVsRandomAi;

        if (ChessGUI.gameParameters.getGameMode() == GameMode.HUMAN_VS_AI) {
            if (ChessGUI.gameParameters.getAiType() == AiType.MINIMAX_AI) {
                ChessGUI.miniMaxAI = new MiniMaxAI(ChessGUI.gameParameters.getAi1MaxDepth(), Constants.BLACK, true);
            } else if (ChessGUI.gameParameters.getAiType() == AiType.RANDOM_AI) {
                ChessGUI.randomChoiceAI = new RandomChoiceAI(Constants.BLACK);
            }
        }

        ChessGUI.configureGuiStyle();

        // this.setPreferredSize(new Dimension(N * SIZE, N * SIZE));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // this.initializeChessBoardPanel();

        ChessGUI.isChessGui2 = true;
        ChessGUI.chessBoardPanel = this;
        ChessGUI.gui.add(ChessGUI.chessBoardPanel);
        ChessGUI.initializeChessBoardSquareButtons();

        ChessGUI.initializeCapturedPiecesPanel();
        ChessGUI.initializeCapturedPiecesImages();
    }
	
	
	/*
	private void initializeChessBoardPanel() {

		for (int i=0; i<N; i++) {
			for (int j=0; j<N; j++) {
				// chessBoardSquares[i][j] = new ChessButton(i*N + j);
				ChessGUI.chessBoardSquares[i][j] = new ChessButton(i, j);
				JButton button = ChessGUI.chessBoardSquares[i][j];
				
				int row = 7 - i;
				int column = j;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ChessGUI.chessButtonClick(row, column, button);
					}
				});
				
				// this.add(chessBoardSquares[i][j]);
			}
		}
		
		// fill the chess_board board panel
		add(new JLabel(""));
		
		// fill the top row
		// Remember: ASCII decimal character code for the character 'A' is 65 
		for (int i=0; i<N; i++) {
			add(new JLabel(String.valueOf((char) (65+i)), SwingConstants.CENTER));
		}
		
		add(new JLabel(""));
		
		// fill the black non-pawn piece row
		for (int i=0; i<N; i++) {
			for (int j=0; j<N+1; j++) {
				switch (j) {
					case 0:
						add(new JLabel("" + (N - i), SwingConstants.CENTER));
						add(ChessGUI.chessBoardSquares[i][j]);
						break;
					case N:
						add(new JLabel("" + (N - i), SwingConstants.CENTER));
						break;
					default:
						add(ChessGUI.chessBoardSquares[i][j]);
				}
			}
		}
		
		// fill the bottom row
		add(new JLabel(""));
		for (int i=0; i<N; i++) {
			add(new JLabel(String.valueOf((char) (65+i)), SwingConstants.CENTER));
		}
		
	}
	*/


    public static void main(String[] args) {

        ChessGui2 chessGui2 = new ChessGui2();

        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(chessGui2);
        f.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int) (screenSize.getWidth() - f.getWidth()) / 2,
                (int) (screenSize.getHeight() - f.getHeight()) / 2);

        f.setResizable(false);
        f.setVisible(true);

        ChessGUI.placePiecesToChessBoard();
        System.out.println(ChessGUI.chessBoard);
    }

}
