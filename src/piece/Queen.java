package piece;

import java.util.HashSet;
import java.util.Set;

import chess_board.ChessBoard;
import enumeration.Allegiance;
import utility.Constants;
import utility.Utilities;


public class Queen extends ChessPiece {

    public Queen(Allegiance allegiance) {
        super(allegiance);
    }

    @Override
    public ChessPiece makeCopy() {
        return new Queen(super.getAllegiance());
    }

    @Override
    public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
        // System.out.println("current position: " + position);

        Set<String> nextQueenPositions = new HashSet<>();

        // First, find the row && the column
        // that corresponds to the given position String.
        int row = Utilities.getRowFromPosition(position);
        int column = Utilities.getColumnFromPosition(position);
        ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

        if (!(chessPiece instanceof Queen)) {
            return nextQueenPositions;
        }

        // Find all the up positions.
        for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
            if (row < chessBoard.getNumOfRows()) {

                int newRow = i;
                int newColumn = column;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
        }

        // Find all the down positions.
        for (int i = row - 1; i >= 0; i--) {
            if (row > 0) {

                int newRow = i;
                int newColumn = column;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
        }

        // Find all the right positions.
        for (int j = column + 1; j < Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
            if (column < Constants.DEFAULT_NUM_OF_COLUMNS) {

                int newRow = row;
                int newColumn = j;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
        }

        // Find all the left positions.
        for (int j = column - 1; j >= 0; j--) {
            if (column > 0) {

                int newRow = row;
                int newColumn = j;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
        }


        int counter;

        // Find all the upper right diagonal positions.
        counter = 1;
        for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
            if (row < chessBoard.getNumOfRows() && column + counter < Constants.DEFAULT_NUM_OF_COLUMNS) {

                int newRow = i;
                int newColumn = column + counter;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                // System.out.println("newPosition: " + newPosition);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
            counter++;
        }

        // Find all the lower left diagonal positions.
        counter = 1;
        for (int i = row - 1; i >= 0; i--) {
            if (row >= 0 && column - counter >= 0) {

                int newRow = i;
                int newColumn = column - counter;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                // System.out.println("newPosition: " + newPosition);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
            counter++;
        }

        // Find all the upper left diagonal positions.
        counter = 1;
        for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
            if (row < chessBoard.getNumOfRows() && column - counter >= 0) {

                int newRow = i;
                int newColumn = column - counter;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                // System.out.println("newPosition: " + newPosition);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
            counter++;
        }

        // Find all the lower right diagonal positions.
        counter = 1;
        for (int i = row - 1; i >= 0; i--) {
            if (row >= 0 && column + counter < Constants.DEFAULT_NUM_OF_COLUMNS) {

                int newRow = i;
                int newColumn = column + counter;
                String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
                // System.out.println("newPosition: " + newPosition);
                ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
                // System.out.println("endTile: " + endTile);
                if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
                        && !(endTile instanceof King) || returnThreats) {
                    nextQueenPositions.add(newPosition);
                }

                // Stop searching for other positions, if another chessPiece is reached.
                if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
                        || chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
                    break;
                }
            }
            counter++;
        }

        return nextQueenPositions;
    }


}
