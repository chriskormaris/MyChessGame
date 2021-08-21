package chess_gui_tests;


// import chess_board.ChessBoard;
import chess_gui.ChessGUI;
import enumeration.GameMode;
import org.junit.jupiter.api.Test;


class AiVsAiTest {

	@Test
	public void testAiVsAi() {
		String title = "AI Vs AI Test";

		@SuppressWarnings("unused")
		ChessGUI cbg = new ChessGUI(title);
		
		ChessGUI.gameParameters.setGameMode(GameMode.AI_VS_AI);
		// ChessGUI.gameParameters.setAiType(AiType.RANDOM_AI);

		ChessGUI.restoreDefaultValues();
		
		// String fenPosition = "5Knk/1P6/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1qpKnk/8/4Q3/8/8/8/8/8 w - - 0 1";
		// String fenPosition = "1R1rpKnk/8/4R3/8/8/8/8/8 w - - 0 1";

		// If White King is in check, White does not plays first, 
		// because the "whiteKingInCheckValidPieceMoves" Map<String, Set<String>> is empty. 
		// String fenPosition = "1R1q1Knk/8/4Q3/8/8/8/8/8 w - - 0 1";

		// ChessGUI.placePiecesToChessBoard(fenPosition);
		// System.out.println("White king position: " + ChessGUI.chessBoard.getWhiteKingPosition());
		// System.out.println("Black king position: " + ChessGUI.chessBoard.getBlackKingPosition());
		
		// System.out.println("player: " + ChessGUI.chessBoard.getPlayer());
		
		// ChessBoard.printChessBoard(ChessGUI.chessBoard.getGameBoard());

		// Find the "whiteKingInCheckValidPieceMoves" and "blackKingInCheckValidPieceMoves",
		// if the White or Black King respectively is in check.
		// boolean storeKingInCheckMoves = true;
		// ChessGUI.chessBoard.checkForBlackCheckmate(storeKingInCheckMoves);
		// ChessGUI.chessBoard.checkForWhiteCheckmate(storeKingInCheckMoves);
		// System.out.println(chessBoard.getWhiteKingInCheckValidPieceMoves());

		ChessGUI.placePiecesToChessBoard();
		ChessGUI.playAiVsAi();
		
		System.out.println();

		System.out.println("*****************************");
		System.out.println();
		
		while (true);

	}

}
