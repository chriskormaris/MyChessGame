package utilities;

import java.awt.Color;

public class GameParameters {
	
	public static int numOfRows = 8;  // the number of rows
	
	/* Default values */
	public static int guiStyle = Constants.CROSS_PLATFORM_STYLE;
	// public static int guiStyle = Constants.NIMBUS_STYLE;
	
	public static int gameMode = Constants.HUMAN_VS_MINIMAX_AI;
	// public static int gameMode = Constants.HUMAN_VS_RANDOM_AI;
	// public static int gameMode = Constants.HUMAN_VS_HUMAN;
	// public static int gameMode = Constants.MINIMAX_AI_VS_MINIMAX_AI;
	
	// public static Color blackTileColor = Color.GRAY;
	public static Color blackTileColor = Color.BLACK;
	
	public static boolean enableSounds = true;
	
	public static int maxDepth1 = 1;
	public static int maxDepth2 = 1;
	
}
