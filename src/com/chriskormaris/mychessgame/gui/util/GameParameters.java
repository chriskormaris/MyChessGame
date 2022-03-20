package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;

import java.awt.*;

public class GameParameters {

	private GuiStyle guiStyle;

	private boolean enableSounds;

	private Allegiance humanPlayerAllegiance;

	private GameMode gameMode;

	private AiType ai1Type;
	private AiType ai2Type;

	private int ai1MaxDepth;
	private int ai2MaxDepth;

	private EvaluationFunction evaluationFunction1;
	private EvaluationFunction evaluationFunction2;

	private Color whiteTileColor;

	private Color blackTileColor;

	private int numOfRows;

	// Default constructor
	public GameParameters() {
		/* Default values */
		this.guiStyle = GuiStyle.CROSS_PLATFORM_STYLE;
		// this.guiStyle = GuiStyle.NIMBUS_STYLE;

		this.enableSounds = true;

		this.humanPlayerAllegiance = Allegiance.WHITE;

		this.gameMode = GameMode.HUMAN_VS_AI;
		// this.gameMode = GameMode.HUMAN_VS_HUMAN;
		// this.gameMode = GameMode.AI_VS_AI;

		this.ai1Type = AiType.MINIMAX_AI;
		// this.aiType = AiType.RANDOM_AI;

		this.ai2Type = AiType.MINIMAX_AI;
		// this.ai2Type = AiType.RANDOM_AI;

		this.ai1MaxDepth = Constants.DEFAULT_MAX_DEPTH;
		this.ai2MaxDepth = Constants.DEFAULT_MAX_DEPTH;

		this.evaluationFunction1 = EvaluationFunction.SIMPLIFIED;
		this.evaluationFunction2 = EvaluationFunction.SIMPLIFIED;

		this.whiteTileColor = Color.WHITE;
		// this.whiteTileColor = GuiConstants.BRIGHT_PINK;

		this.blackTileColor = GuiConstants.DARK_GREEN;
		// this.blackTileColor = Color.BLACK;
		// this.blackTileColor = Color.DARK_GRAY;
		// this.blackTileColor = Color.GRAY;

		this.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;
	}

	// Copy constructor
	public GameParameters(GameParameters otherGameParameters) {
		this.guiStyle = otherGameParameters.getGuiStyle();
		this.enableSounds = otherGameParameters.isEnableSounds();
		this.humanPlayerAllegiance = otherGameParameters.getHumanPlayerAllegiance();
		this.gameMode = otherGameParameters.getGameMode();
		this.ai1Type = otherGameParameters.getAi1Type();
		this.ai2Type = otherGameParameters.getAi2Type();
		this.ai1MaxDepth = otherGameParameters.getAi1MaxDepth();
		this.ai2MaxDepth = otherGameParameters.getAi2MaxDepth();
		this.evaluationFunction1 = otherGameParameters.getEvaluationFunction1();
		this.evaluationFunction2 = otherGameParameters.getEvaluationFunction2();
		this.whiteTileColor = otherGameParameters.getWhiteTileColor();
		this.blackTileColor = otherGameParameters.getBlackTileColor();
		this.numOfRows = otherGameParameters.getNumOfRows();
	}


	public GameParameters(GuiStyle guiStyle, boolean enableSounds,
	                      Allegiance humanPlayerAllegiance, GameMode gameMode,
	                      AiType ai1Type, AiType ai2Type, int maxDepth1, int maxDepth2,
	                      EvaluationFunction evaluationFunction1, EvaluationFunction evaluationFunction2,
						  Color whiteTileColor, Color blackTileColor, int numOfRows) {
		this.guiStyle = guiStyle;
		this.enableSounds = enableSounds;
		this.humanPlayerAllegiance = humanPlayerAllegiance;
		this.gameMode = gameMode;
		this.ai1Type = ai1Type;
		this.ai2Type = ai2Type;
		this.ai1MaxDepth = maxDepth1;
		this.ai2MaxDepth = maxDepth2;
		this.evaluationFunction1 = evaluationFunction1;
		this.evaluationFunction2 = evaluationFunction2;
		this.whiteTileColor = whiteTileColor;
		this.blackTileColor = blackTileColor;
		this.numOfRows = numOfRows;
	}

	public GuiStyle getGuiStyle() {
		return guiStyle;
	}

	public void setGuiStyle(GuiStyle guiStyle) {
		this.guiStyle = guiStyle;
	}

	public boolean isEnableSounds() {
		return enableSounds;
	}

	public void setEnableSounds(boolean enableSounds) {
		this.enableSounds = enableSounds;
	}

	public Allegiance getHumanPlayerAllegiance() {
		return humanPlayerAllegiance;
	}

	public void setHumanPlayerAllegiance(Allegiance humanPlayerAllegiance) {
		this.humanPlayerAllegiance = humanPlayerAllegiance;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public AiType getAi1Type() {
		return ai1Type;
	}

	public void setAi1Type(AiType ai1Type) {
		this.ai1Type = ai1Type;
	}

	public AiType getAi2Type() {
		return ai2Type;
	}

	public void setAi2Type(AiType ai2Type) {
		this.ai2Type = ai2Type;
	}

	public int getAi1MaxDepth() {
		return ai1MaxDepth;
	}

	public void setAi1MaxDepth(int ai1MaxDepth) {
		this.ai1MaxDepth = ai1MaxDepth;
	}

	public int getAi2MaxDepth() {
		return ai2MaxDepth;
	}

	public void setAi2MaxDepth(int ai2MaxDepth) {
		this.ai2MaxDepth = ai2MaxDepth;
	}

	public EvaluationFunction getEvaluationFunction1() {
		return evaluationFunction1;
	}

	public void setEvaluationFunction1(EvaluationFunction evaluationFunction1) {
		this.evaluationFunction1 = evaluationFunction1;
	}

	public EvaluationFunction getEvaluationFunction2() {
		return evaluationFunction2;
	}

	public void setEvaluationFunction2(EvaluationFunction evaluationFunction2) {
		this.evaluationFunction2 = evaluationFunction2;
	}

	public Color getWhiteTileColor() {
		return whiteTileColor;
	}

	public void setWhiteTileColor(Color whiteTileColor) {
		this.whiteTileColor = whiteTileColor;
	}

	public Color getBlackTileColor() {
		return blackTileColor;
	}

	public void setBlackTileColor(Color blackTileColor) {
		this.blackTileColor = blackTileColor;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}

}
