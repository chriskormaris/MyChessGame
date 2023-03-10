package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class GameParameters {

	private GuiType guiType;
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

	private Color whiteSquareColor;

	private Color blackSquareColor;

	private int numOfRows;

	// Default constructor
	public GameParameters() {
		/* Default values */
		this.guiType = GuiType.BUTTONS;

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

		this.whiteSquareColor = Color.WHITE;
		// this.whiteSquareColor = GuiConstants.BRIGHT_PINK;

		this.blackSquareColor = GuiConstants.DARK_GREEN;
		// this.blackSquareColor = Color.BLACK;
		// this.blackSquareColor = Color.DARK_GRAY;
		// this.blackSquareColor = Color.GRAY;

		this.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;
	}

	// Copy constructor
	public GameParameters(GameParameters otherGameParameters) {
		this.guiType = otherGameParameters.getGuiType();
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
		this.whiteSquareColor = otherGameParameters.getWhiteSquareColor();
		this.blackSquareColor = otherGameParameters.getBlackSquareColor();
		this.numOfRows = otherGameParameters.getNumOfRows();
	}

}
