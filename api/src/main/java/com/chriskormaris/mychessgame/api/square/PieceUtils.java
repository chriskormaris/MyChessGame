package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PieceUtils {

    public static byte getChessPiece(char pieceChar) {
        if (pieceChar == Constants.WHITE_PAWN_CHAR) {
            return Constants.WHITE_PAWN;
        } else if (pieceChar == Constants.WHITE_ROOK_CHAR) {
            return Constants.WHITE_ROOK;
        } else if (pieceChar == Constants.WHITE_KNIGHT_CHAR) {
            return Constants.WHITE_KNIGHT;
        } else if (pieceChar == Constants.WHITE_BISHOP_CHAR) {
            return Constants.WHITE_BISHOP;
        } else if (pieceChar == Constants.WHITE_QUEEN_CHAR) {
            return Constants.WHITE_QUEEN;
        } else if (pieceChar == Constants.WHITE_KING_CHAR) {
            return Constants.WHITE_KING;
        }

        if (pieceChar == Constants.BLACK_PAWN_CHAR) {
            return Constants.BLACK_PAWN;
        } else if (pieceChar == Constants.BLACK_ROOK_CHAR) {
            return Constants.BLACK_ROOK;
        } else if (pieceChar == Constants.BLACK_KNIGHT_CHAR) {
            return Constants.BLACK_KNIGHT;
        } else if (pieceChar == Constants.BLACK_BISHOP_CHAR) {
            return Constants.BLACK_BISHOP;
        } else if (pieceChar == Constants.BLACK_QUEEN_CHAR) {
            return Constants.BLACK_QUEEN;
        } else if (pieceChar == Constants.BLACK_KING_CHAR) {
            return Constants.BLACK_KING;
        }

        if (pieceChar != '-') {
            System.err.println("Invalid chessPiece character \"" + pieceChar + "\"!");
        }
        return Constants.EMPTY_SQUARE;
    }

    public static char getPieceChar(byte chessSquare) {
        if (chessSquare > 0) {
            if (Math.abs(chessSquare) == Constants.PAWN) {
                return Constants.WHITE_PAWN_CHAR;
            } else if (Math.abs(chessSquare) == Constants.ROOK) {
                return Constants.WHITE_ROOK_CHAR;
            } else if (Math.abs(chessSquare) == Constants.KNIGHT) {
                return Constants.WHITE_KNIGHT_CHAR;
            } else if (Math.abs(chessSquare) == Constants.BISHOP) {
                return Constants.WHITE_BISHOP_CHAR;
            } else if (Math.abs(chessSquare) == Constants.QUEEN) {
                return Constants.WHITE_QUEEN_CHAR;
            } else if (Math.abs(chessSquare) == Constants.KING) {
                return Constants.WHITE_KING_CHAR;
            }
        } else if (chessSquare < 0) {
            if (Math.abs(chessSquare) == Constants.PAWN) {
                return Constants.BLACK_PAWN_CHAR;
            } else if (Math.abs(chessSquare) == Constants.ROOK) {
                return Constants.BLACK_ROOK_CHAR;
            } else if (Math.abs(chessSquare) == Constants.KNIGHT) {
                return Constants.BLACK_KNIGHT_CHAR;
            } else if (Math.abs(chessSquare) == Constants.BISHOP) {
                return Constants.BLACK_BISHOP_CHAR;
            } else if (Math.abs(chessSquare) == Constants.QUEEN) {
                return Constants.BLACK_QUEEN_CHAR;
            } else if (Math.abs(chessSquare) == Constants.KING) {
                return Constants.BLACK_KING_CHAR;
            }
        }
        if (chessSquare != Constants.EMPTY_SQUARE) {
            System.err.println("Invalid chessPiece value \"" + chessSquare + "\"!");
        }
        return '-';
    }

    public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
        int row = chessBoard.getRowFromPosition(startingPosition);
        int column = chessBoard.getColumnFromPosition(startingPosition);
        byte piece = chessBoard.getGameBoard()[row][column];

        if (Math.abs(piece) == Constants.PAWN) {
            return Pawn.getNextPositions(startingPosition, chessBoard, returnThreats);
        } else if (Math.abs(piece) == Constants.KNIGHT) {
            return Knight.getNextPositions(startingPosition, chessBoard, returnThreats);
        } else if (Math.abs(piece) == Constants.BISHOP) {
            return Bishop.getNextPositions(startingPosition, chessBoard, returnThreats);
        } else if (Math.abs(piece) == Constants.ROOK) {
            return Rook.getNextPositions(startingPosition, chessBoard, returnThreats);
        } else if (Math.abs(piece) == Constants.QUEEN) {
            return Queen.getNextPositions(startingPosition, chessBoard, returnThreats);
        } else if (Math.abs(piece) == Constants.KING) {
            return King.getNextPositions(startingPosition, chessBoard, returnThreats);
        }

        return new HashSet<>();
    }

}
