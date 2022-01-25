package com.chriskormaris.mychessgame.gui.utility;

import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.utility.Constants;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;

import java.awt.*;

public class GameParameters {

	private GuiStyle guiStyle;

	private boolean enableSounds;

	private Allegiance humanPlayerAllegiance;

	private GameMode gameMode;

	private AiType aiType;

	private int ai1MaxDepth;
	private int ai2MaxDepth;

	private Color whiteTileColor;

	private Color blackTileColor;

	private int numOfRows;

	private EvaluationFunction evaluationFunction;

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

		this.aiType = AiType.MINIMAX_AI;
		// this.aiType = AiType.RANDOM_AI;

		this.ai1MaxDepth = Constants.DEFAULT_MAX_DEPTH;
		this.ai2MaxDepth = Constants.DEFAULT_MAX_DEPTH;

		this.whiteTileColor = Color.WHITE;
		// this.whiteTileColor = Color.PINK;

		this.blackTileColor = GuiConstants.DARK_GREEN;
		// this.blackTileColor = Color.BLACK;
		// this.blackTileColor = Color.DARK_GRAY;
		// this.blackTileColor = Color.GRAY;

		this.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;

		this.evaluationFunction = EvaluationFunction.SIMPLIFIED;
	}

	// Copy constructor
	public GameParameters(GameParameters otherGameParameters) {
		this.guiStyle = otherGameParameters.getGuiStyle();
		this.enableSounds = otherGameParameters.isEnableSounds();
		this.humanPlayerAllegiance = otherGameParameters.getHumanPlayerAllegiance();
		this.gameMode = otherGameParameters.getGameMode();
		this.aiType = otherGameParameters.getAiType();
		this.ai1MaxDepth = otherGameParameters.getAi1MaxDepth();
		this.ai2MaxDepth = otherGameParameters.getAi2MaxDepth();
		this.whiteTileColor = otherGameParameters.getWhiteTileColor();
		this.blackTileColor = otherGameParameters.getBlackTileColor();
		this.numOfRows = otherGameParameters.getNumOfRows();
		this.evaluationFunction = otherGameParameters.getEvaluationFunction();
	}


	public GameParameters(GuiStyle guiStyle, boolean enableSounds,
	                      Allegiance humanPlayerAllegiance, GameMode gameMode,
	                      AiType aiMode, int maxDepth1, int maxDepth2,
	                      Color whiteTileColor, Color blackTileColor,
	                      int numOfRows, EvaluationFunction evaluationFunction) {
		this.guiStyle = guiStyle;
		this.enableSounds = enableSounds;
		this.humanPlayerAllegiance = humanPlayerAllegiance;
		this.gameMode = gameMode;
		this.aiType = aiMode;
		this.ai1MaxDepth = maxDepth1;
		this.ai2MaxDepth = maxDepth2;
		this.whiteTileColor = whiteTileColor;
		this.blackTileColor = blackTileColor;
		this.numOfRows = numOfRows;
		this.evaluationFunction = evaluationFunction;
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

	public AiType getAiType() {
		return aiType;
	}

	public void setAiType(AiType aiType) {
		this.aiType = aiType;
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

	public EvaluationFunction getEvaluationFunction() {
		return evaluationFunction;
	}

	public void setEvaluationFunction(EvaluationFunction evaluationFunction) {
		this.evaluationFunction = evaluationFunction;
	}

}
