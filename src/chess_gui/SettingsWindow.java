package chess_gui;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import enumerations.AiType;
import enumerations.Allegiance;
import enumerations.GameMode;
import enumerations.GuiStyle;
import utilities.Constants;
import utilities.GameParameters;


public class SettingsWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6651737783332653136L;
	
	private JLabel guiStyleLabel;
	private JLabel enableSoundsLabel;
	private JLabel humanPlayerAllegianceLabel;
	private JLabel gameModeLabel;
	private JLabel aiTypeLabel;
	private JLabel maxDepth1Label;
	private JLabel maxDepth2Label;
	private JLabel whiteTileColorLabel;
	private JLabel blackTileColorLabel;
	private JLabel numOfRowsLabel;

	private JComboBox<String> gui_style_drop_down;
	private JCheckBox enable_sounds_check_box;
	private JComboBox<String> human_player_allegiance_drop_down;
	private JComboBox<String> game_mode_drop_down;
	private JComboBox<String> ai_type_drop_down;
	private JComboBox<Integer> max_depth1_drop_down;
	private JComboBox<Integer> max_depth2_drop_down;
	private JComboBox<String> white_tile_color_drop_down;
	private JComboBox<String> black_tile_color_drop_down;
	SpinnerModel spinnerModel = new SpinnerNumberModel(8, 4, 8, 1);     
	private JSpinner num_of_rows_spinner = new JSpinner(spinnerModel);

	private JButton apply;
	private JButton cancel;
	
	private EventHandler handler;
	
	public static int width = 450;
	public static int height = 440;
	
	
	public SettingsWindow() {
		super("Settings");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(width, height);
		setLocationRelativeTo(null);
		setResizable(false);
		
		handler = new EventHandler();
		
		GuiStyle selectedGuiStyle = ChessGUI.gameParameters.guiStyle;
		boolean enableSounds = ChessGUI.gameParameters.enableSounds;
		Allegiance humanPlayerAllegiance = ChessGUI.gameParameters.humanPlayerAllegiance;
		GameMode selectedGameMode = ChessGUI.gameParameters.gameMode;
		AiType selectedAiMode = ChessGUI.gameParameters.aiType;
		int maxDepth1 = ChessGUI.gameParameters.maxDepth1 - 1;
		int maxDepth2 = ChessGUI.gameParameters.maxDepth2 - 1;
		Color selectedWhiteTileColor = ChessGUI.gameParameters.whiteTileColor;
		Color selectedBlackTileColor = ChessGUI.gameParameters.blackTileColor;
		int numOfRows = ChessGUI.gameParameters.numOfRows;

		
		guiStyleLabel = new JLabel("GUI style");
		enableSoundsLabel = new JLabel("Sounds");
		humanPlayerAllegianceLabel = new JLabel("Human Player Allegiance");
		gameModeLabel = new JLabel("Game mode");
		aiTypeLabel = new JLabel("AI type");
		maxDepth1Label = new JLabel("Minimax AI 1 depth");
		maxDepth2Label = new JLabel("Minimax AI 2 depth (AiVsAi)");
		whiteTileColorLabel = new JLabel("White tile color");
		blackTileColorLabel = new JLabel("Black tile color");
		numOfRowsLabel = new JLabel("Number of rows");

		
		add(guiStyleLabel);
		add(enableSoundsLabel);
		add(humanPlayerAllegianceLabel);
		add(gameModeLabel);
		add(aiTypeLabel);
		add(maxDepth1Label);
		add(maxDepth2Label);
		add(whiteTileColorLabel);
		add(blackTileColorLabel);
		add(numOfRowsLabel);

		
		gui_style_drop_down = new JComboBox<String>();
		gui_style_drop_down.addItem("Cross-platform style");
		gui_style_drop_down.addItem("Nimbus style");
		
		if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM_STYLE)
			gui_style_drop_down.setSelectedIndex(0);
		else if (selectedGuiStyle == GuiStyle.NIMBUS_STYLE)
			gui_style_drop_down.setSelectedIndex(1);
		
		
		enable_sounds_check_box = new JCheckBox();
		enable_sounds_check_box.setSelected(enableSounds);
		
		
		human_player_allegiance_drop_down = new JComboBox<String>();
		human_player_allegiance_drop_down.addItem("White");
		human_player_allegiance_drop_down.addItem("Black");
		
		if (humanPlayerAllegiance == Allegiance.WHITE)
			human_player_allegiance_drop_down.setSelectedIndex(0);
		else if (humanPlayerAllegiance == Allegiance.BLACK)
			human_player_allegiance_drop_down.setSelectedIndex(1);
		
		
		game_mode_drop_down = new JComboBox<String>();
		game_mode_drop_down.addItem("Human Vs AI");
		game_mode_drop_down.addItem("Human Vs Human");
		game_mode_drop_down.addItem("AI Vs AI");

		if (selectedGameMode == GameMode.HUMAN_VS_AI)
			game_mode_drop_down.setSelectedIndex(0);
		else if (selectedGameMode == GameMode.HUMAN_VS_HUMAN)
			game_mode_drop_down.setSelectedIndex(1);
		else if (selectedGameMode == GameMode.AI_VS_AI)
			game_mode_drop_down.setSelectedIndex(2);
		
		ai_type_drop_down = new JComboBox<String>();
		ai_type_drop_down.addItem("Minimax AI");
		ai_type_drop_down.addItem("Random AI");
		
		if (selectedAiMode == AiType.MINIMAX_AI)
			ai_type_drop_down.setSelectedIndex(0);
		else if (selectedAiMode == AiType.RANDOM_AI)
			ai_type_drop_down.setSelectedIndex(1);
		
		max_depth1_drop_down = new JComboBox<Integer>();
		max_depth1_drop_down.addItem(1);
		max_depth1_drop_down.addItem(2);
		max_depth1_drop_down.addItem(3);
		
		max_depth1_drop_down.setSelectedIndex(maxDepth1);

		max_depth2_drop_down = new JComboBox<Integer>();
		max_depth2_drop_down.addItem(1);
		max_depth2_drop_down.addItem(2);
		max_depth2_drop_down.addItem(3);
		
		max_depth2_drop_down.setSelectedIndex(maxDepth2);
		
		white_tile_color_drop_down = new JComboBox<String>();
		white_tile_color_drop_down.addItem("White");
		white_tile_color_drop_down.addItem("Pink");
		
		if (selectedWhiteTileColor == Color.WHITE)
			white_tile_color_drop_down.setSelectedIndex(0);
		else if (selectedWhiteTileColor == Constants.BRIGHT_PINK)
			white_tile_color_drop_down.setSelectedIndex(1);
		
		black_tile_color_drop_down = new JComboBox<String>();
		black_tile_color_drop_down.addItem("Dark Green");
		black_tile_color_drop_down.addItem("Black");
		black_tile_color_drop_down.addItem("Dark Gray");
		black_tile_color_drop_down.addItem("Gray");
		
		if (selectedBlackTileColor == Constants.DARK_GREEN)
			black_tile_color_drop_down.setSelectedIndex(0);
		else if (selectedBlackTileColor == Color.BLACK)
			black_tile_color_drop_down.setSelectedIndex(1);
		else if (selectedBlackTileColor == Color.DARK_GRAY)
			black_tile_color_drop_down.setSelectedIndex(2);
		else if (selectedBlackTileColor == Color.GRAY)
			black_tile_color_drop_down.setSelectedIndex(3);
		
		spinnerModel.setValue(numOfRows);

		
		add(gui_style_drop_down);
		add(enable_sounds_check_box);
		add(human_player_allegiance_drop_down);
		add(game_mode_drop_down);
		add(ai_type_drop_down);
		add(max_depth1_drop_down);
		add(max_depth2_drop_down);
		add(white_tile_color_drop_down);
		add(black_tile_color_drop_down);
		add(num_of_rows_spinner);

		guiStyleLabel.setBounds(25, 25, 205, 20);
		enableSoundsLabel.setBounds(25, 55, 205, 20);
		humanPlayerAllegianceLabel.setBounds(25, 85, 205, 20);
		gameModeLabel.setBounds(25, 115, 205, 20);
		aiTypeLabel.setBounds(25, 145, 205, 20);
		maxDepth1Label.setBounds(25, 175, 205, 20);
		maxDepth2Label.setBounds(25, 205, 205, 20);
		whiteTileColorLabel.setBounds(25, 235, 205, 20);
		blackTileColorLabel.setBounds(25, 265, 205, 20);
		numOfRowsLabel.setBounds(25, 295, 205, 20);
		
		gui_style_drop_down.setBounds(225, 25, 180, 20);
		enable_sounds_check_box.setBounds(225, 55, 180, 20);
		human_player_allegiance_drop_down.setBounds(225, 85, 180, 20);
		game_mode_drop_down.setBounds(225, 115, 180, 20);
		ai_type_drop_down.setBounds(225, 145, 180, 20);
		max_depth1_drop_down.setBounds(225, 175, 180, 20);
		max_depth2_drop_down.setBounds(225, 205, 180, 20);
		white_tile_color_drop_down.setBounds(225, 235, 180, 20);
		black_tile_color_drop_down.setBounds(225, 265, 180, 20);
		num_of_rows_spinner.setBounds(225, 295, 180, 30);

		apply = new JButton("Apply");
		cancel = new JButton("Cancel");
		add(apply);
		add(cancel);
		
		int distance = 10;
		apply.setBounds((int) (width / 2) - 110 - (int) (distance / 2), 340, 100, 30);
		apply.addActionListener(handler);
		cancel.setBounds((int) (width / 2) - 10 + (int) (distance / 2), 340, 100, 30);
		cancel.addActionListener(handler);
	}

	
	private class EventHandler implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent ev) {
			
			if (ev.getSource() == cancel) {
				dispose();
			}
			
			else if (ev.getSource() == apply) {
				try {
					
					GuiStyle guiStyle = 
						GuiStyle.valueOf(gui_style_drop_down.getSelectedItem().toString().toUpperCase().replace("-", "_").replace(" ", "_"));
					boolean enableSounds = enable_sounds_check_box.isSelected();
					Allegiance humanPlayerAllegiance = Allegiance.valueOf(human_player_allegiance_drop_down.getSelectedItem().toString().toUpperCase());
					GameMode gameMode = GameMode.valueOf(game_mode_drop_down.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
					AiType aiType = AiType.valueOf(ai_type_drop_down.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
					int maxDepth1 = (int) max_depth1_drop_down.getSelectedItem();
					int maxDepth2 = (int) max_depth2_drop_down.getSelectedItem();
					int whiteTileColorDropdownIndex = white_tile_color_drop_down.getSelectedIndex();
					int blackTileColorDropdownIndex = black_tile_color_drop_down.getSelectedIndex();
					int numOfRows = (int) num_of_rows_spinner.getValue();
					// numOfRows = (numOfRows >= 8) ? numOfRows : 8;
					
					Color whiteTileColor = null;
					if (whiteTileColorDropdownIndex == 0) {
						whiteTileColor = Color.WHITE;
					} else if (whiteTileColorDropdownIndex == 1) {
						whiteTileColor = Constants.BRIGHT_PINK;
					}
					
					Color blackTileColor = null;
					if (blackTileColorDropdownIndex == 0) {
						blackTileColor = Constants.DARK_GREEN;
					} else if (blackTileColorDropdownIndex == 1) {
						blackTileColor = Color.BLACK;
					} else if (blackTileColorDropdownIndex == 2) {
						blackTileColor = Color.DARK_GRAY;
					} else if (blackTileColorDropdownIndex == 3) {
						blackTileColor = Color.GRAY;
					}
					
					// Change game parameters based on the settings.
					ChessGUI.newGameParameters = new GameParameters(guiStyle, enableSounds, humanPlayerAllegiance,
							gameMode, aiType, maxDepth1, maxDepth2, whiteTileColor, blackTileColor, numOfRows); 
					
					JOptionPane.showMessageDialog(ChessGUI.frame,
							"Game settings have been changed.\nThe changes will be applied in the next new game.",
							"", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
				
				catch(Exception e) {
					System.err.println("ERROR : " + e.getMessage());
				}
				
			}  // else if.
			
		}  // action performed.
		
	}  // inner class.
	
}  // class end.

