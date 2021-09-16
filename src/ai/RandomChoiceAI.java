package ai;

import chess_board.ChessBoard;
import chess_board.Move;
import enumeration.Allegiance;
import utility.Constants;
import utility.Utilities;

import java.util.*;

public class RandomChoiceAI extends AI {

    public RandomChoiceAI(boolean aiPlayer) {
        super(aiPlayer);
    }

    // Initiates the random move.
    @Override
    public Move getNextMove(ChessBoard chessBoard) {

        String randomAiStartingPosition = "";
        String randomAiEndingPosition = "";

        // This map is used for the Random AI implementation.
        Map<String, Set<String>> randomStartingEndingPositions = new HashMap<>();

        /* STEP 1. Random starting position. */
        if (chessBoard.whitePlays() && getAiPlayer() == Constants.WHITE && !chessBoard.isWhiteKingInCheck()
                ||
                chessBoard.blackPlays() && getAiPlayer() == Constants.BLACK && !chessBoard.isBlackKingInCheck()) {
            for (int i=0; i<chessBoard.getNumOfRows(); i++) {
                for (int j=0; j<ChessBoard.NUM_OF_COLUMNS; j++) {
                    if (getAiPlayer() == Constants.WHITE
                            && chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.WHITE
                            ||
                        getAiPlayer() == Constants.BLACK
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
        else if (chessBoard.whitePlays() && getAiPlayer() == Constants.WHITE && chessBoard.isWhiteKingInCheck()
                ||
                chessBoard.blackPlays() && getAiPlayer() == Constants.BLACK && chessBoard.isBlackKingInCheck()) {
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

        return new Move(randomAiStartingPosition, randomAiEndingPosition, chessBoard.evaluate());
    }

}