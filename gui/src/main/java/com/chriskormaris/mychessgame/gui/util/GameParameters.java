package com.chriskormaris.mychessgame.gui.util;

import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
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

	private Allegiance humanAllegiance;

	private GameMode gameMode;

	private AiType ai1Type;
	private int ai1MaxDepth;
	private EvaluationFunction evaluationFunction1;

	private AiType ai2Type;
	private int ai2MaxDepth;
	private EvaluationFunction evaluationFunction2;

	private Color whiteSquareColor;

	private Color blackSquareColor;

	private int numOfRows;

	private boolean showNextMoves;

	private boolean enableTimeLimit;
	private int timeLimitSeconds;

	// Default constructor
	public GameParameters() {
		/* Default values */

		// Other values: GuiType.BUTTONS
		this.guiType = GuiType.DRAG_AND_DROP;

		// Other values: GuiStyle.SYSTEM, GuiStyle.NIMBUS
		this.guiStyle = GuiStyle.CROSS_PLATFORM;

		this.enableSounds = true;

		this.humanAllegiance = Allegiance.WHITE;

		// Other values: GameMode.HUMAN_VS_HUMAN, GameMode.AI_VS_AI
		this.gameMode = GameMode.HUMAN_VS_AI;

		// Other values: AiType.RANDOM_AI
		this.ai1Type = AiType.MINIMAX_AI;
		this.ai1MaxDepth = Constants.DEFAULT_MAX_DEPTH;
		this.evaluationFunction1 = EvaluationFunction.SIMPLIFIED;

		this.ai2Type = AiType.MINIMAX_AI;
		this.ai2MaxDepth = Constants.DEFAULT_MAX_DEPTH;
		this.evaluationFunction2 = EvaluationFunction.SIMPLIFIED;

		// Other values: GuiConstants.BRIGHT_PINK
		this.whiteSquareColor = Color.WHITE;

		/* Other values:
		 * Color.BLACK
		 * Color.DARK_GRAY
		 * Color.GRAY */
		this.blackSquareColor = GuiConstants.DARK_GREEN;

		this.numOfRows = Constants.DEFAULT_NUM_OF_ROWS;

		this.showNextMoves = true;

		this.enableTimeLimit = true;
		this.timeLimitSeconds = Constants.DEFAULT_TIME_LIMIT_SECONDS;
	}

	// Copy constructor
	public GameParameters(GameParameters otherGameParameters) {
		this.guiType = otherGameParameters.getGuiType();
		this.guiStyle = otherGameParameters.getGuiStyle();
		this.enableSounds = otherGameParameters.isEnableSounds();
		this.humanAllegiance = otherGameParameters.getHumanAllegiance();
		this.gameMode = otherGameParameters.getGameMode();
		this.ai1Type = otherGameParameters.getAi1Type();
		this.ai1MaxDepth = otherGameParameters.getAi1MaxDepth();
		this.evaluationFunction1 = otherGameParameters.getEvaluationFunction1();
		this.ai2Type = otherGameParameters.getAi2Type();
		this.ai2MaxDepth = otherGameParameters.getAi2MaxDepth();
		this.evaluationFunction2 = otherGameParameters.getEvaluationFunction2();
		this.whiteSquareColor = otherGameParameters.getWhiteSquareColor();
		this.blackSquareColor = otherGameParameters.getBlackSquareColor();
		this.numOfRows = otherGameParameters.getNumOfRows();
		this.showNextMoves = otherGameParameters.isShowNextMoves();
		this.enableTimeLimit = otherGameParameters.isEnableTimeLimit();
		this.timeLimitSeconds = otherGameParameters.getTimeLimitSeconds();
	}

}
