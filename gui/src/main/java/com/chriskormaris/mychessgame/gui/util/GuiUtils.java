package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.square.ChessSquare;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GuiUtils {

	public static ImageIcon preparePieceIcon(String imagePath, int size) {
		ImageIcon pieceIcon = new ImageIcon(ResourceLoader.load(imagePath));
		Image image = pieceIcon.getImage(); // transform it
		// scale it the smooth way
		Image newImg = image.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		pieceIcon = new ImageIcon(newImg);  // transform it back
		return pieceIcon;
	}

	public static String getImagePath(ChessSquare chessSquare) {
		String imagePath = "";

		if (chessSquare.isWhite()) {
			if (chessSquare.isPawn()) {
				imagePath = GuiConstants.WHITE_PAWN_IMG_PATH;
			} else if (chessSquare.isRook()) {
				imagePath = GuiConstants.WHITE_ROOK_IMG_PATH;
			} else if (chessSquare.isKnight()) {
				imagePath = GuiConstants.WHITE_KNIGHT_IMG_PATH;
			} else if (chessSquare.isBishop()) {
				imagePath = GuiConstants.WHITE_BISHOP_IMG_PATH;
			} else if (chessSquare.isQueen()) {
				imagePath = GuiConstants.WHITE_QUEEN_IMG_PATH;
			} else if (chessSquare.isKing()) {
				imagePath = GuiConstants.WHITE_KING_IMG_PATH;
			}
		} else if (chessSquare.isBlack()) {
			if (chessSquare.isPawn()) {
				imagePath = GuiConstants.BLACK_PAWN_IMG_PATH;
			} else if (chessSquare.isRook()) {
				imagePath = GuiConstants.BLACK_ROOK_IMG_PATH;
			} else if (chessSquare.isKnight()) {
				imagePath = GuiConstants.BLACK_KNIGHT_IMG_PATH;
			} else if (chessSquare.isBishop()) {
				imagePath = GuiConstants.BLACK_BISHOP_IMG_PATH;
			} else if (chessSquare.isQueen()) {
				imagePath = GuiConstants.BLACK_QUEEN_IMG_PATH;
			} else if (chessSquare.isKing()) {
				imagePath = GuiConstants.BLACK_KING_IMG_PATH;
			}
		}

		return imagePath;
	}

	public static void centerTextPaneAndMakeBold(JTextPane textPane) {
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

}
