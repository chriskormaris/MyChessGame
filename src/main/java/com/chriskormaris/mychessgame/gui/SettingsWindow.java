package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.api.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SettingsWindow extends JFrame {

	private final JComboBox<String> gui_style_drop_down;
	private final JCheckBox enable_sounds_check_box;
	private final JComboBox<String> human_player_allegiance_drop_down;
	private final JComboBox<String> game_mode_drop_down;
	private final JComboBox<String> ai1_type_drop_down;
	private final JComboBox<String> ai2_type_drop_down;
	private final JComboBox<Integer> ai1_max_depth_drop_down;
	private final JComboBox<Integer> ai2_max_depth_drop_down;
	private final JComboBox<String> evaluation_function1_drop_down;
	private final JComboBox<String> evaluation_function2_drop_down;
	private final JComboBox<String> white_square_color_drop_down;
	private final JComboBox<String> black_square_color_drop_down;

	private final SpinnerModel num_of_rows_spinner_model = new SpinnerNumberModel(8, 4, 8, 1);
	private final JSpinner num_of_rows_spinner = new JSpinner(num_of_rows_spinner_model);

	private final JButton apply;
	private final JButton cancel;

	private final Component parentComponent;
	private final GameParameters newGameParameters;

	public SettingsWindow(Component parentComponent, GameParameters gameParameters, GameParameters newGameParameters) {
		super("Settings");
		this.parentComponent = parentComponent;
		this.newGameParameters = newGameParameters;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		int width = 450;
		int height = 585;
		setSize(width, height);
		setLocationRelativeTo(parentComponent);
		setResizable(false);

		EventHandler handler = new EventHandler();

		newGameParameters.setGuiType(gameParameters.getGuiType());

		GuiStyle selectedGuiStyle = gameParameters.getGuiStyle();
		boolean enableSounds = gameParameters.isEnableSounds();
		Allegiance humanPlayerAllegiance = gameParameters.getHumanPlayerAllegiance();
		GameMode selectedGameMode = gameParameters.getGameMode();
		AiType selectedAi1Type = gameParameters.getAi1Type();
		AiType selectedAi2Type = gameParameters.getAi2Type();
		int maxDepth1 = gameParameters.getAi1MaxDepth() - 1;
		int maxDepth2 = gameParameters.getAi2MaxDepth() - 1;
		EvaluationFunction evaluationFunction1 = gameParameters.getEvaluationFunction1();
		EvaluationFunction evaluationFunction2 = gameParameters.getEvaluationFunction2();
		Color selectedWhiteSquareColor = gameParameters.getWhiteSquareColor();
		Color selectedBlackSquareColor = gameParameters.getBlackSquareColor();
		int numOfRows = gameParameters.getNumOfRows();


		JLabel guiStyleLabel = new JLabel("GUI style");
		JLabel enableSoundsLabel = new JLabel("Sounds");
		JLabel humanPlayerAllegianceLabel = new JLabel("Human Player Allegiance");
		JLabel gameModeLabel = new JLabel("Game mode");
		JLabel ai1TypeLabel = new JLabel("AI 1 type");
		JLabel ai2TypeLabel = new JLabel("AI 2 type (AIvsAI)");
		JLabel maxDepth1Label = new JLabel("Minimax AI 1 depth");
		JLabel maxDepth2Label = new JLabel("Minimax AI 2 depth (AIvsAI)");
		JLabel evaluationFunction1Label = new JLabel("AI 1 Evaluation Function");
		JLabel evaluationFunction2Label = new JLabel("AI 2 Evaluation Function (AIvsAI)");
		JLabel whiteSquareColorLabel = new JLabel("White square color");
		JLabel blackSquareColorLabel = new JLabel("Black square color");
		JLabel numOfRowsLabel = new JLabel("Number of rows");


		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(guiStyleLabel);
		}
		add(enableSoundsLabel);
		add(humanPlayerAllegianceLabel);
		add(gameModeLabel);
		add(ai1TypeLabel);
		add(ai2TypeLabel);
		add(maxDepth1Label);
		add(maxDepth2Label);
		add(evaluationFunction1Label);
		add(evaluationFunction2Label);
		add(whiteSquareColorLabel);
		add(blackSquareColorLabel);
		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(numOfRowsLabel);
		}

		gui_style_drop_down = new JComboBox<>();
		gui_style_drop_down.addItem("Cross-platform style");
		gui_style_drop_down.addItem("Nimbus style");

		if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM_STYLE) {
			gui_style_drop_down.setSelectedIndex(0);
		} else if (selectedGuiStyle == GuiStyle.NIMBUS_STYLE) {
			gui_style_drop_down.setSelectedIndex(1);
		}

		enable_sounds_check_box = new JCheckBox();
		enable_sounds_check_box.setSelected(enableSounds);

		human_player_allegiance_drop_down = new JComboBox<>();
		human_player_allegiance_drop_down.addItem("White");
		human_player_allegiance_drop_down.addItem("Black");

		if (humanPlayerAllegiance == Allegiance.WHITE) {
			human_player_allegiance_drop_down.setSelectedIndex(0);
		} else if (humanPlayerAllegiance == Allegiance.BLACK) {
			human_player_allegiance_drop_down.setSelectedIndex(1);
		}

		game_mode_drop_down = new JComboBox<>();
		game_mode_drop_down.addItem("Human Vs AI");
		game_mode_drop_down.addItem("Human Vs Human");
		game_mode_drop_down.addItem("AI Vs AI");

		if (selectedGameMode == GameMode.HUMAN_VS_AI) {
			game_mode_drop_down.setSelectedIndex(0);
		} else if (selectedGameMode == GameMode.HUMAN_VS_HUMAN) {
			game_mode_drop_down.setSelectedIndex(1);
		} else if (selectedGameMode == GameMode.AI_VS_AI) {
			game_mode_drop_down.setSelectedIndex(2);
		}

		ai1_type_drop_down = new JComboBox<>();
		ai1_type_drop_down.addItem("Minimax AI");
		ai1_type_drop_down.addItem("Random AI");

		if (selectedAi1Type == AiType.MINIMAX_AI) {
			ai1_type_drop_down.setSelectedIndex(0);
		} else if (selectedAi1Type == AiType.RANDOM_AI) {
			ai1_type_drop_down.setSelectedIndex(1);
		}

		ai2_type_drop_down = new JComboBox<>();
		ai2_type_drop_down.addItem("Minimax AI");
		ai2_type_drop_down.addItem("Random AI");

		if (selectedAi2Type == AiType.MINIMAX_AI) {
			ai2_type_drop_down.setSelectedIndex(0);
		} else if (selectedAi2Type == AiType.RANDOM_AI) {
			ai2_type_drop_down.setSelectedIndex(1);
		}

		ai1_max_depth_drop_down = new JComboBox<>();
		ai1_max_depth_drop_down.addItem(1);
		ai1_max_depth_drop_down.addItem(2);
		ai1_max_depth_drop_down.addItem(3);
		ai1_max_depth_drop_down.addItem(4);

		ai1_max_depth_drop_down.setSelectedIndex(maxDepth1);

		ai2_max_depth_drop_down = new JComboBox<>();
		ai2_max_depth_drop_down.addItem(1);
		ai2_max_depth_drop_down.addItem(2);
		ai2_max_depth_drop_down.addItem(3);
		ai2_max_depth_drop_down.addItem(4);

		ai2_max_depth_drop_down.setSelectedIndex(maxDepth2);

		evaluation_function1_drop_down = new JComboBox<>();
		evaluation_function1_drop_down.addItem("Simplified");
		evaluation_function1_drop_down.addItem("PeSTO");
		evaluation_function1_drop_down.addItem("Wukong");
		evaluation_function1_drop_down.addItem("Shannon");

		if (evaluationFunction1 == EvaluationFunction.SIMPLIFIED) {
			evaluation_function1_drop_down.setSelectedIndex(0);
		} else if (evaluationFunction1 == EvaluationFunction.PESTO) {
			evaluation_function1_drop_down.setSelectedIndex(1);
		} else if (evaluationFunction1 == EvaluationFunction.WUKONG) {
			evaluation_function1_drop_down.setSelectedIndex(2);
		} else if (evaluationFunction1 == EvaluationFunction.SHANNON) {
			evaluation_function1_drop_down.setSelectedIndex(3);
		}

		evaluation_function2_drop_down = new JComboBox<>();
		evaluation_function2_drop_down.addItem("Simplified");
		evaluation_function2_drop_down.addItem("PeSTO");
		evaluation_function2_drop_down.addItem("Wukong");
		evaluation_function2_drop_down.addItem("Shannon");

		if (evaluationFunction2 == EvaluationFunction.SIMPLIFIED) {
			evaluation_function2_drop_down.setSelectedIndex(0);
		} else if (evaluationFunction2 == EvaluationFunction.PESTO) {
			evaluation_function2_drop_down.setSelectedIndex(1);
		} else if (evaluationFunction2 == EvaluationFunction.WUKONG) {
			evaluation_function2_drop_down.setSelectedIndex(2);
		} else if (evaluationFunction2 == EvaluationFunction.SHANNON) {
			evaluation_function2_drop_down.setSelectedIndex(3);
		}

		white_square_color_drop_down = new JComboBox<>();
		white_square_color_drop_down.addItem("White");
		white_square_color_drop_down.addItem("Pink");

		if (selectedWhiteSquareColor == Color.WHITE) {
			white_square_color_drop_down.setSelectedIndex(0);
		} else if (selectedWhiteSquareColor == GuiConstants.BRIGHT_PINK) {
			white_square_color_drop_down.setSelectedIndex(1);
		}

		black_square_color_drop_down = new JComboBox<>();
		black_square_color_drop_down.addItem("Dark Green");
		black_square_color_drop_down.addItem("Black");
		black_square_color_drop_down.addItem("Dark Gray");
		black_square_color_drop_down.addItem("Gray");

		if (selectedBlackSquareColor == GuiConstants.DARK_GREEN) {
			black_square_color_drop_down.setSelectedIndex(0);
		} else if (selectedBlackSquareColor == Color.BLACK) {
			black_square_color_drop_down.setSelectedIndex(1);
		} else if (selectedBlackSquareColor == Color.DARK_GRAY) {
			black_square_color_drop_down.setSelectedIndex(2);
		} else if (selectedBlackSquareColor == Color.GRAY) {
			black_square_color_drop_down.setSelectedIndex(3);
		}

		num_of_rows_spinner_model.setValue(numOfRows);

		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(gui_style_drop_down);
		}
		add(enable_sounds_check_box);
		add(human_player_allegiance_drop_down);
		add(game_mode_drop_down);
		add(ai1_type_drop_down);
		add(ai2_type_drop_down);
		add(ai1_max_depth_drop_down);
		add(ai2_max_depth_drop_down);
		add(evaluation_function1_drop_down);
		add(evaluation_function2_drop_down);
		add(white_square_color_drop_down);
		add(black_square_color_drop_down);
		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(num_of_rows_spinner);
		}

		guiStyleLabel.setBounds(25, 25, 205, 25);
		enableSoundsLabel.setBounds(25, 60, 205, 25);
		humanPlayerAllegianceLabel.setBounds(25, 95, 205, 25);
		gameModeLabel.setBounds(25, 130, 205, 25);
		ai1TypeLabel.setBounds(25, 165, 205, 25);
		ai2TypeLabel.setBounds(25, 200, 205, 25);
		maxDepth1Label.setBounds(25, 235, 205, 25);
		maxDepth2Label.setBounds(25, 270, 205, 25);
		evaluationFunction1Label.setBounds(25, 305, 205, 25);
		evaluationFunction2Label.setBounds(25, 340, 205, 25);
		whiteSquareColorLabel.setBounds(25, 375, 205, 25);
		blackSquareColorLabel.setBounds(25, 405, 205, 25);
		numOfRowsLabel.setBounds(25, 440, 205, 25);

		gui_style_drop_down.setBounds(225, 25, 180, 25);
		enable_sounds_check_box.setBounds(225, 60, 180, 25);
		human_player_allegiance_drop_down.setBounds(225, 95, 180, 25);
		game_mode_drop_down.setBounds(225, 130, 180, 25);
		ai1_type_drop_down.setBounds(225, 165, 180, 25);
		ai2_type_drop_down.setBounds(225, 200, 180, 25);
		ai1_max_depth_drop_down.setBounds(225, 235, 180, 25);
		ai2_max_depth_drop_down.setBounds(225, 270, 180, 25);
		evaluation_function1_drop_down.setBounds(225, 305, 180, 25);
		evaluation_function2_drop_down.setBounds(225, 340, 180, 25);
		white_square_color_drop_down.setBounds(225, 375, 180, 25);
		black_square_color_drop_down.setBounds(225, 405, 180, 25);
		num_of_rows_spinner.setBounds(225, 440, 180, 25);

		apply = new JButton("Apply");
		cancel = new JButton("Cancel");
		add(apply);
		add(cancel);

		int distance = 10;
		apply.setBounds((width / 2) - 110 - (distance / 2), 485, 100, 30);
		apply.addActionListener(handler);
		cancel.setBounds((width / 2) - 10 + (distance / 2), 485, 100, 30);
		cancel.addActionListener(handler);
	}


	private class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ev) {

			if (ev.getSource() == cancel) {
				dispose();
			} else if (ev.getSource() == apply) {
				try {

					GuiStyle guiStyle = GuiStyle.valueOf(gui_style_drop_down.getSelectedItem().toString().toUpperCase()
							.replace("-", "_").replace(" ", "_"));
					boolean enableSounds = enable_sounds_check_box.isSelected();
					Allegiance humanPlayerAllegiance = Allegiance.valueOf(human_player_allegiance_drop_down
							.getSelectedItem().toString().toUpperCase());
					GameMode gameMode = GameMode.valueOf(game_mode_drop_down.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					AiType ai1Type = AiType.valueOf(ai1_type_drop_down.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					AiType ai2Type = AiType.valueOf(ai2_type_drop_down.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					int ai1MaxDepth = (int) ai1_max_depth_drop_down.getSelectedItem();
					int ai2MaxDepth = (int) ai2_max_depth_drop_down.getSelectedItem();
					EvaluationFunction evaluationFunction1 = EvaluationFunction.valueOf(evaluation_function1_drop_down
							.getSelectedItem().toString().toUpperCase());
					EvaluationFunction evaluationFunction2 = EvaluationFunction.valueOf(evaluation_function2_drop_down
							.getSelectedItem().toString().toUpperCase());
					int whiteSquareColorDropdownIndex = white_square_color_drop_down.getSelectedIndex();
					int blackSquareColorDropdownIndex = black_square_color_drop_down.getSelectedIndex();
					int numOfRows = (int) num_of_rows_spinner.getValue();
					// numOfRows = Math.max(numOfRows, 8);

					Color whiteSquareColor = null;
					if (whiteSquareColorDropdownIndex == 0) {
						whiteSquareColor = Color.WHITE;
					} else if (whiteSquareColorDropdownIndex == 1) {
						whiteSquareColor = GuiConstants.BRIGHT_PINK;
					}

					Color blackSquareColor = null;
					if (blackSquareColorDropdownIndex == 0) {
						blackSquareColor = GuiConstants.DARK_GREEN;
					} else if (blackSquareColorDropdownIndex == 1) {
						blackSquareColor = Color.BLACK;
					} else if (blackSquareColorDropdownIndex == 2) {
						blackSquareColor = Color.DARK_GRAY;
					} else if (blackSquareColorDropdownIndex == 3) {
						blackSquareColor = Color.GRAY;
					}

					newGameParameters.setGuiStyle(guiStyle);
					newGameParameters.setEnableSounds(enableSounds);
					newGameParameters.setHumanPlayerAllegiance(humanPlayerAllegiance);
					newGameParameters.setGameMode(gameMode);
					newGameParameters.setAi1Type(ai1Type);
					newGameParameters.setAi2Type(ai2Type);
					newGameParameters.setAi1MaxDepth(ai1MaxDepth);
					newGameParameters.setAi2MaxDepth(ai2MaxDepth);
					newGameParameters.setEvaluationFunction1(evaluationFunction1);
					newGameParameters.setEvaluationFunction2(evaluationFunction2);
					newGameParameters.setWhiteSquareColor(whiteSquareColor);
					newGameParameters.setBlackSquareColor(blackSquareColor);
					newGameParameters.setNumOfRows(numOfRows);

					JOptionPane.showMessageDialog(
							parentComponent,
							"Game settings have been changed.\n" +
									"The changes will be applied in the next new game.",
							"",
							JOptionPane.INFORMATION_MESSAGE
					);
					dispose();
				} catch (Exception e) {
					System.err.println("ERROR: " + e.getMessage());
				}

			}  // else if.

		}  // action performed.

	}  // inner class.

}  // class end.
