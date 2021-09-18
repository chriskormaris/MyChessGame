package piece;

import java.util.HashSet;
import java.util.Set;

import chess_board.ChessBoard;
import enumeration.Allegiance;
import utility.Constants;
import utility.Utilities;


public class Pawn extends ChessPiece {

    public Pawn(Allegiance allegiance) {
        super(allegiance);
    }

    @Override
    public ChessPiece makeCopy() {
        return new Pawn(super.getAllegiance());
    }

    @Override
    public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

        // System.out.println("current position: " + position);

        Set<String> nextPawnPositions = new HashSet<>();

        // First, find the row && the column
        // that corresponds to the given position String.
        int row = Utilities.getRowFromPosition(position);
        int column = Utilities.getColumnFromPosition(position);
        ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

        if (!(chessPiece instanceof Pawn)) {
            return nextPawnPositions;
        }

        int newRow = 0;

        if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 1
                || chessPiece.getAllegiance() == Allegiance.BLACK && row > 0) {

            if (!returnThreats) {
                // One step forward position.
                if (chessPiece.getAllegiance() == Allegiance.WHITE)
                    newRow = row + 1;
                else if (chessPiece.getAllegiance() == Allegiance.BLACK)
                    newRow = row - 1;
                int newColumn = column;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTileCode: " + endTileCode);
                if (endTile instanceof EmptyTile)
                    nextPawnPositions.add(newPosition);

                // Two steps forward position.
                if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 2
                        && chessBoard.getGameBoard()[row + 2][column] instanceof EmptyTile
                        && chessBoard.getGameBoard()[row + 1][column] instanceof EmptyTile
                        || chessPiece.getAllegiance() == Allegiance.BLACK && row > 1
                        && chessBoard.getGameBoard()[row - 2][column] instanceof EmptyTile
                        && chessBoard.getGameBoard()[row - 1][column] instanceof EmptyTile) {

                    if (chessPiece.getAllegiance() == Allegiance.WHITE && row == 1)
                        newRow = row + 2;
                    else if (chessPiece.getAllegiance() == Allegiance.BLACK && row == chessBoard.getNumOfRows() - 2)
                        newRow = row - 2;
                    newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                    // endTile = chessBoard.getGameBoard()[newRow][newColumn];
                    // System.out.println("endTile: " + endTile);
                    nextPawnPositions.add(newPosition);
                }

            }

            // One step diagonally forward left.
            if (column > 0) {
                if (chessPiece.getAllegiance() == Allegiance.WHITE)
                    newRow = row + 1;
                else if (chessPiece.getAllegiance() == Allegiance.BLACK)
                    newRow = row - 1;
                int newColumn = column - 1;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if ((!(endTile instanceof EmptyTile) && chessPiece.getAllegiance() != endTile.getAllegiance())
                        && !(endTile instanceof King) || returnThreats) {
                    nextPawnPositions.add(newPosition);
                }
            }

            // One step diagonally forward right.
            if (column < Constants.DEFAULT_NUM_OF_COLUMNS - 1) {
                if (chessPiece.getAllegiance() == Allegiance.WHITE)
                    newRow = row + 1;
                else if (chessPiece.getAllegiance() == Allegiance.BLACK)
                    newRow = row - 1;
                int newColumn = column + 1;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if ((!(endTile instanceof EmptyTile) && chessPiece.getAllegiance() != endTile.getAllegiance())
                        && !(endTile instanceof King) || returnThreats) {
                    nextPawnPositions.add(newPosition);
                }
            }

            Set<String> enPassantPositions = getEnPassantPositions(position, chessBoard, returnThreats);
            // System.out.println("enPassantPositions: " + enPassantPositions);
            nextPawnPositions.addAll(enPassantPositions);

        }

        return nextPawnPositions;
    }


    // Implementation of the "en passant" moves.
    public Set<String> getEnPassantPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
        // System.out.println("current position: " + position);

        Set<String> enPassantPositions = new HashSet<>();

        // First, find the row && the column
        // that corresponds to the given position String.
        int row = Utilities.getRowFromPosition(position);
        int column = Utilities.getColumnFromPosition(position);
        ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

        if (!(chessPiece instanceof Pawn))
            return enPassantPositions;

        int newRow = 0;

        if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 1
                || chessPiece.getAllegiance() == Allegiance.BLACK && row > 0) {

            // One step diagonally forward left.
            if (column > 0) {
                if (chessPiece.getAllegiance() == Allegiance.WHITE)
                    newRow = row + 1;
                if (chessPiece.getAllegiance() == Allegiance.BLACK)
                    newRow = row - 1;
                int newColumn = column - 1;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (!(endTile instanceof King)
                        && chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || endTile instanceof EmptyTile && newPosition.equals(chessBoard.getEnPassantPosition())
                        || returnThreats) {
                    enPassantPositions.add(newPosition);
                }
            }

            // One step diagonally forward right.
            if (column < Constants.DEFAULT_NUM_OF_COLUMNS - 1) {
                if (chessPiece.getAllegiance() == Allegiance.WHITE)
                    newRow = row + 1;
                if (chessPiece.getAllegiance() == Allegiance.BLACK)
                    newRow = row - 1;
                int newColumn = column + 1;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (!(endTile instanceof King)
                        && chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || endTile instanceof EmptyTile && newPosition.equals(chessBoard.getEnPassantPosition())
                        || returnThreats) {
                    enPassantPositions.add(newPosition);
                }
            }

        }

        return enPassantPositions;
    }


}
