package utilities;

import java.awt.Color;

import enumerations.AiType;
import enumerations.Allegiance;
import enumerations.GameMode;
import enumerations.GuiStyle;

public class GameParameters {
	
	
	// Default constructor
	public GameParameters() {
		
	}
	
	// Copy constructor
	public GameParameters(GameParameters otherGameParameters) {
		this.guiStyle = otherGameParameters.guiStyle;
		this.enableSounds = otherGameParameters.enableSounds;
		this.humanPlayerAllegiance = otherGameParameters.humanPlayerAllegiance;
		this.gameMode = otherGameParameters.gameMode;
		this.aiType = otherGameParameters.aiType;
		this.maxDepth1 = otherGameParameters.maxDepth1;
		this.maxDepth2 = otherGameParameters.maxDepth2;
		this.whiteTileColor = otherGameParameters.whiteTileColor;
		this.blackTileColor = otherGameParameters.blackTileColor;
		this.numOfRows = otherGameParameters.numOfRows;
	}
	
	
	public GameParameters(GuiStyle guiStyle, boolean enableSounds, Allegiance humanPlayerAllegiance, GameMode gameMode,
			AiType aiMode, int maxDepth1, int maxDepth2, Color whiteTileColor, Color blackTileColor, int numOfRows) {
		this.guiStyle = guiStyle;
		this.enableSounds = enableSounds;
		this.humanPlayerAllegiance = humanPlayerAllegiance;
		this.gameMode = gameMode;
		this.aiType = aiMode;
		this.maxDepth1 = maxDepth1;
		this.maxDepth2 = maxDepth2;
		this.whiteTileColor = whiteTileColor;
		this.blackTileColor = blackTileColor;
		this.numOfRows = numOfRows;
	}


	/* Default values */
	public GuiStyle guiStyle = GuiStyle.CROSS_PLATFORM_STYLE;
	// public static GuiStyle guiStyle = GuiStyle.NIMBUS_STYLE;
	
	public boolean enableSounds = true;
	
	public Allegiance humanPlayerAllegiance = Allegiance.WHITE;
	
	public GameMode gameMode = GameMode.HUMAN_VS_AI;
	// public static GameMode gameMode = GameMode.HUMAN_VS_RANDOM_AI;
	// public static GameMode gameMode = GameMode.HUMAN_VS_HUMAN;
	// public static GameMode gameMode = GameMode.MINIMAX_AI_VS_MINIMAX_AI;
	
	public AiType aiType = AiType.MINIMAX_AI;
	// public static AiType aiType = AiType.RANDOM_AI;
	
	public int maxDepth1 = Constants.DEFAULT_MAX_DEPTH;
	public int maxDepth2 = Constants.DEFAULT_MAX_DEPTH;
	
	public Color whiteTileColor = Color.WHITE;
	// public Color whiteTileColor = Color.PINK;

	public Color blackTileColor = Constants.DARK_GREEN;
	// public Color blackTileColor = Color.BLACK;
	// public Color blackTileColor = Color.DARK_GRAY;
	// public Color blackTileColor = Color.GRAY;

	public int numOfRows = Constants.DEFAULT_NUM_OF_ROWS;  // the number of rows

}
