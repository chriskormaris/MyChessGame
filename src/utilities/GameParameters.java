package utilities;

import java.awt.Color;

import enums.Allegiance;

public class GameParameters {
	
	private GameParameters() { }  // Prevents instantiation.

	/* Default values */
	public static int guiStyle = Constants.CROSS_PLATFORM_STYLE;
	// public static int guiStyle = Constants.NIMBUS_STYLE;

	public static boolean enableSounds = true;
	
	public static Allegiance humanPlayerAllegiance = Allegiance.WHITE;
	
	public static int gameMode = Constants.HUMAN_VS_MINIMAX_AI;
	// public static int gameMode = Constants.HUMAN_VS_RANDOM_AI;
	// public static int gameMode = Constants.HUMAN_VS_HUMAN;
	// public static int gameMode = Constants.MINIMAX_AI_VS_MINIMAX_AI;
	
	public static int maxDepth1 = 1;
	public static int maxDepth2 = 1;

	public static Color blackTileColor = Color.BLACK;
	// public static Color blackTileColor = Color.GRAY;

	public static int numOfRows = 8;  // the number of rows

}
