package utility;

import java.awt.Color;

import enumeration.AiType;
import enumeration.Allegiance;
import enumeration.GameMode;
import enumeration.GuiStyle;

public class GameParameters {

	/* Default values */
	private GuiStyle guiStyle = GuiStyle.CROSS_PLATFORM_STYLE;
	// private static GuiStyle guiStyle = GuiStyle.NIMBUS_STYLE;

	private boolean enableSounds = true;

	private Allegiance humanPlayerAllegiance = Allegiance.WHITE;

	private GameMode gameMode = GameMode.HUMAN_VS_AI;
	// private static GameMode gameMode = GameMode.HUMAN_VS_RANDOM_AI;
	// private static GameMode gameMode = GameMode.HUMAN_VS_HUMAN;
	// private static GameMode gameMode = GameMode.MINIMAX_AI_VS_MINIMAX_AI;

	private AiType aiType = AiType.MINIMAX_AI;
	// private static AiType aiType = AiType.RANDOM_AI;

	private int ai1MaxDepth = Constants.DEFAULT_MAX_DEPTH;
	private int ai2MaxDepth = Constants.DEFAULT_MAX_DEPTH;

	private Color whiteTileColor = Color.WHITE;
	// private Color whiteTileColor = Color.PINK;

	private Color blackTileColor = Constants.DARK_GREEN;
	// private Color blackTileColor = Color.BLACK;
	// private Color blackTileColor = Color.DARK_GRAY;
	// private Color blackTileColor = Color.GRAY;

	private int numOfRows = Constants.DEFAULT_NUM_OF_ROWS;  // the number of rows

	// Default constructor
	public GameParameters() {
		
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
	}
	
	
	public GameParameters(GuiStyle guiStyle, boolean enableSounds, Allegiance humanPlayerAllegiance, GameMode gameMode,
			AiType aiMode, int maxDepth1, int maxDepth2, Color whiteTileColor, Color blackTileColor, int numOfRows) {
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

}
