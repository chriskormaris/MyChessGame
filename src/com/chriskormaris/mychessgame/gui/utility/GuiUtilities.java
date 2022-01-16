package com.chriskormaris.mychessgame.gui.utility;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;

import javax.swing.*;
import java.awt.*;

public class GuiUtilities {

	public static ImageIcon preparePieceIcon(String imagePath, int size) {
		ImageIcon pieceIcon = new ImageIcon(ResourceLoader.load(imagePath));
		Image image = pieceIcon.getImage(); // transform it
		// scale it the smooth way
		Image newImg = image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		pieceIcon = new ImageIcon(newImg);  // transform it back
		return pieceIcon;
	}

	public static String getImagePath(ChessPiece chessPiece) {
		String imagePath = "";

		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			if (chessPiece instanceof Pawn) {
				imagePath = GuiConstants.WHITE_PAWN_IMG_PATH;
			} else if (chessPiece instanceof Rook) {
				imagePath = GuiConstants.WHITE_ROOK_IMG_PATH;
			} else if (chessPiece instanceof Knight) {
				imagePath = GuiConstants.WHITE_KNIGHT_IMG_PATH;
			} else if (chessPiece instanceof Bishop) {
				imagePath = GuiConstants.WHITE_BISHOP_IMG_PATH;
			} else if (chessPiece instanceof Queen) {
				imagePath = GuiConstants.WHITE_QUEEN_IMG_PATH;
			} else if (chessPiece instanceof King) {
				imagePath = GuiConstants.WHITE_KING_IMG_PATH;
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			if (chessPiece instanceof Pawn) {
				imagePath = GuiConstants.BLACK_PAWN_IMG_PATH;
			} else if (chessPiece instanceof Rook) {
				imagePath = GuiConstants.BLACK_ROOK_IMG_PATH;
			} else if (chessPiece instanceof Knight) {
				imagePath = GuiConstants.BLACK_KNIGHT_IMG_PATH;
			} else if (chessPiece instanceof Bishop) {
				imagePath = GuiConstants.BLACK_BISHOP_IMG_PATH;
			} else if (chessPiece instanceof Queen) {
				imagePath = GuiConstants.BLACK_QUEEN_IMG_PATH;
			} else if (chessPiece instanceof King) {
				imagePath = GuiConstants.BLACK_KING_IMG_PATH;
			}
		}

		return imagePath;
	}

	public static void changeTileColor(JButton button, Color color) {
		button.setBackground(color);
	}

}
