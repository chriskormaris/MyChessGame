package utilities;

import java.awt.Color;

import enumerations.AiMode;
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
		this.aiMode = otherGameParameters.aiMode;
		this.maxDepth1 = otherGameParameters.maxDepth1;
		this.maxDepth2 = otherGameParameters.maxDepth2;
		this.blackTileColor = otherGameParameters.blackTileColor;
		this.numOfRows = otherGameParameters.numOfRows;
	}
	
	
	public GameParameters(GuiStyle guiStyle, boolean enableSounds, Allegiance humanPlayerAllegiance, 
			GameMode gameMode, AiMode aiMode, int maxDepth1, int maxDepth2, Color blackTileColor, int numOfRows) {
		this.guiStyle = guiStyle;
		this.enableSounds = enableSounds;
		this.humanPlayerAllegiance = humanPlayerAllegiance;
		this.gameMode = gameMode;
		this.aiMode = aiMode;
		this.maxDepth1 = maxDepth1;
		this.maxDepth2 = maxDepth2;
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
	
	public AiMode aiMode = AiMode.MINIMAX_AI;
	// public static AiMode aiMode = AiMode.RANDOM_AI;
	
	public int maxDepth1 = 1;
	public int maxDepth2 = 1;

	public Color blackTileColor = Color.BLACK;
	// public static Color blackTileColor = Color.GRAY;

	public int numOfRows = 8;  // the number of rows

}
