package com.chriskormaris.mychessgame.gui.frame;


import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class SettingsFrame extends JFrame {

	private final JComboBox<String> guiTypeDropDown;
	private final JComboBox<String> guiStyleDropDown;
	private final JCheckBox enableSoundsCheckBox;
	private final JComboBox<String> humanAllegianceDropDown;
	private final JComboBox<String> gameModeDropDown;
	private final JComboBox<String> ai1TypeDropDown;
	private final JComboBox<String> ai2TypeDropDown;
	private final JComboBox<Integer> ai1MaxDepthDropDown;
	private final JComboBox<Integer> ai2MaxDepthDropDown;
	private final JComboBox<String> evaluationFunction1DropDown;
	private final JComboBox<String> evaluationFunction2DropDown;
	private final JComboBox<String> whiteSquareColorDropDown;
	private final JComboBox<String> blackSquareColorDropDown;
	private final JCheckBox enableTimeLimitCheckBox;

	private final SpinnerModel numOfRowsSpinnerModel = new SpinnerNumberModel(8, 4, 8, 1);
	private final JSpinner numOfRowsSpinner = new JSpinner(numOfRowsSpinnerModel);
	private final SpinnerModel timeLimitSecondsSpinnerModel = new SpinnerNumberModel(600, 1, 1200, 1);
	private final JSpinner timeLimitSecondsSpinner = new JSpinner(timeLimitSecondsSpinnerModel);

	private final JCheckBox showNextMovesCheckBox;

	private final JButton apply;
	private final JButton cancel;

	private final Component parentComponent;
	private final GameParameters newGameParameters;

	public SettingsFrame(Component parentComponent, GameParameters newGameParameters) {
		super("Settings");

		this.parentComponent = parentComponent;
		this.newGameParameters = newGameParameters;

		int width = 525;
		int height = 700;

		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLayout(null);
		super.setSize(width, height);
		super.setLocationRelativeTo(parentComponent);
		super.setResizable(false);

		GuiType selectedGuiType = newGameParameters.getGuiType();
		GuiStyle selectedGuiStyle = newGameParameters.getGuiStyle();
		boolean enableSounds = newGameParameters.isEnableSounds();
		Allegiance humanAllegiance = newGameParameters.getHumanAllegiance();
		GameMode selectedGameMode = newGameParameters.getGameMode();
		AiType selectedAi1Type = newGameParameters.getAi1Type();
		AiType selectedAi2Type = newGameParameters.getAi2Type();
		int ai1MaxDepth = newGameParameters.getAi1MaxDepth() - 1;
		int ai2MaxDepth = newGameParameters.getAi2MaxDepth() - 1;
		EvaluationFunction evaluationFunction1 = newGameParameters.getEvaluationFunction1();
		EvaluationFunction evaluationFunction2 = newGameParameters.getEvaluationFunction2();
		Color selectedWhiteSquareColor = newGameParameters.getWhiteSquareColor();
		Color selectedBlackSquareColor = newGameParameters.getBlackSquareColor();
		int numOfRows = newGameParameters.getNumOfRows();
		boolean showNextMoves = newGameParameters.isShowNextMoves();
		boolean enableTimeLimit = newGameParameters.isEnableTimeLimit();
		int timeLimitSeconds = newGameParameters.getTimeLimitSeconds();

		List<JLabel> labels = new ArrayList<>();
		List<JComponent> components = new ArrayList<>();

		JLabel guiTypeLabel = new JLabel("GUI type");
		JLabel guiStyleLabel = new JLabel("GUI style");
		JLabel enableSoundsLabel = new JLabel("Sounds");
		JLabel humanAllegianceLabel = new JLabel("Human allegiance (Human vs AI)");
		JLabel gameModeLabel = new JLabel("Game mode");
		JLabel ai1TypeLabel = new JLabel("AI 1 type");
		JLabel ai2TypeLabel = new JLabel("AI 2 type (AI vs AI)");
		JLabel ai1MaxDepthLabel = new JLabel("Minimax AI 1 depth");
		JLabel ai2MaxDepthLabel = new JLabel("Minimax AI 2 depth (AI vs AI)");
		JLabel evaluationFunction1Label = new JLabel("AI 1 evaluation function");
		JLabel evaluationFunction2Label = new JLabel("AI 2 evaluation function (AI vs AI)");
		JLabel whiteSquareColorLabel = new JLabel("White square color");
		JLabel blackSquareColorLabel = new JLabel("Black square color");
		JLabel numOfRowsLabel = new JLabel("Number of rows");
		JLabel showNextMovesLabel = new JLabel("Show next moves");
		JLabel enableTimeLimitLabel = new JLabel("Time limit (Human vs Human)");
		JLabel timeLimitSecondsLabel = new JLabel("Time limit seconds");

		labels.add(guiTypeLabel);
		labels.add(guiStyleLabel);
		labels.add(enableSoundsLabel);
		labels.add(humanAllegianceLabel);
		labels.add(gameModeLabel);
		labels.add(ai1TypeLabel);
		labels.add(ai2TypeLabel);
		labels.add(ai1MaxDepthLabel);
		labels.add(ai2MaxDepthLabel);
		labels.add(evaluationFunction1Label);
		labels.add(evaluationFunction2Label);
		labels.add(whiteSquareColorLabel);
		labels.add(blackSquareColorLabel);
		if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
			labels.add(numOfRowsLabel);
		}
		labels.add(showNextMovesLabel);
		labels.add(enableTimeLimitLabel);
		labels.add(timeLimitSecondsLabel);

		guiTypeDropDown = new JComboBox<>();
		guiTypeDropDown.addItem("Drag and Drop");
		guiTypeDropDown.addItem("Buttons");

		if (selectedGuiType == GuiType.DRAG_AND_DROP) {
			guiTypeDropDown.setSelectedIndex(0);
		} else if (selectedGuiType == GuiType.BUTTONS) {
			guiTypeDropDown.setSelectedIndex(1);
		}

		guiStyleDropDown = new JComboBox<>();
		guiStyleDropDown.addItem("Cross-platform");
		guiStyleDropDown.addItem("System");
		guiStyleDropDown.addItem("Nimbus");

		if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM) {
			guiStyleDropDown.setSelectedIndex(0);
		} else if (selectedGuiStyle == GuiStyle.SYSTEM) {
			guiStyleDropDown.setSelectedIndex(1);
		} else if (selectedGuiStyle == GuiStyle.NIMBUS) {
			guiStyleDropDown.setSelectedIndex(2);
		}

		enableSoundsCheckBox = new JCheckBox();
		enableSoundsCheckBox.setSelected(enableSounds);

		humanAllegianceDropDown = new JComboBox<>();
		humanAllegianceDropDown.addItem("White");
		humanAllegianceDropDown.addItem("Black");

		if (humanAllegiance == Allegiance.WHITE) {
			humanAllegianceDropDown.setSelectedIndex(0);
		} else if (humanAllegiance == Allegiance.BLACK) {
			humanAllegianceDropDown.setSelectedIndex(1);
		}

		gameModeDropDown = new JComboBox<>();
		gameModeDropDown.addItem("Human vs AI");
		gameModeDropDown.addItem("Human vs Human");
		gameModeDropDown.addItem("AI vs AI");

		if (selectedGameMode == GameMode.HUMAN_VS_AI) {
			gameModeDropDown.setSelectedIndex(0);
		} else if (selectedGameMode == GameMode.HUMAN_VS_HUMAN) {
			gameModeDropDown.setSelectedIndex(1);
		} else if (selectedGameMode == GameMode.AI_VS_AI) {
			gameModeDropDown.setSelectedIndex(2);
		}

		ai1TypeDropDown = new JComboBox<>();
		ai1TypeDropDown.addItem("Minimax AI");
		ai1TypeDropDown.addItem("Minimax Alpha-Beta Pruning AI");
		ai1TypeDropDown.addItem("Random AI");

		if (selectedAi1Type == AiType.MINIMAX_AI) {
			ai1TypeDropDown.setSelectedIndex(0);
		} else if (selectedAi1Type == AiType.MINIMAX_ALPHA_BETA_PRUNING_AI) {
			ai1TypeDropDown.setSelectedIndex(1);
		} else if (selectedAi1Type == AiType.RANDOM_AI) {
			ai1TypeDropDown.setSelectedIndex(2);
		}

		ai2TypeDropDown = new JComboBox<>();
		ai2TypeDropDown.addItem("Minimax AI");
		ai2TypeDropDown.addItem("Minimax Alpha-Beta Pruning AI");
		ai2TypeDropDown.addItem("Random AI");

		if (selectedAi2Type == AiType.MINIMAX_AI) {
			ai2TypeDropDown.setSelectedIndex(0);
		} else if (selectedAi2Type == AiType.MINIMAX_ALPHA_BETA_PRUNING_AI) {
			ai2TypeDropDown.setSelectedIndex(1);
		} else if (selectedAi2Type == AiType.RANDOM_AI) {
			ai2TypeDropDown.setSelectedIndex(2);
		}

		ai1MaxDepthDropDown = new JComboBox<>();
		ai1MaxDepthDropDown.addItem(1);
		ai1MaxDepthDropDown.addItem(2);
		ai1MaxDepthDropDown.addItem(3);
		ai1MaxDepthDropDown.addItem(4);

		ai1MaxDepthDropDown.setSelectedIndex(ai1MaxDepth);

		ai2MaxDepthDropDown = new JComboBox<>();
		ai2MaxDepthDropDown.addItem(1);
		ai2MaxDepthDropDown.addItem(2);
		ai2MaxDepthDropDown.addItem(3);
		ai2MaxDepthDropDown.addItem(4);

		ai2MaxDepthDropDown.setSelectedIndex(ai2MaxDepth);

		evaluationFunction1DropDown = new JComboBox<>();
		evaluationFunction1DropDown.addItem("Simplified");
		evaluationFunction1DropDown.addItem("PeSTO");
		evaluationFunction1DropDown.addItem("Wukong");
		evaluationFunction1DropDown.addItem("Shannon");

		if (evaluationFunction1 == EvaluationFunction.SIMPLIFIED) {
			evaluationFunction1DropDown.setSelectedIndex(0);
		} else if (evaluationFunction1 == EvaluationFunction.PESTO) {
			evaluationFunction1DropDown.setSelectedIndex(1);
		} else if (evaluationFunction1 == EvaluationFunction.WUKONG) {
			evaluationFunction1DropDown.setSelectedIndex(2);
		} else if (evaluationFunction1 == EvaluationFunction.SHANNON) {
			evaluationFunction1DropDown.setSelectedIndex(3);
		}

		evaluationFunction2DropDown = new JComboBox<>();
		evaluationFunction2DropDown.addItem("Simplified");
		evaluationFunction2DropDown.addItem("PeSTO");
		evaluationFunction2DropDown.addItem("Wukong");
		evaluationFunction2DropDown.addItem("Shannon");

		if (evaluationFunction2 == EvaluationFunction.SIMPLIFIED) {
			evaluationFunction2DropDown.setSelectedIndex(0);
		} else if (evaluationFunction2 == EvaluationFunction.PESTO) {
			evaluationFunction2DropDown.setSelectedIndex(1);
		} else if (evaluationFunction2 == EvaluationFunction.WUKONG) {
			evaluationFunction2DropDown.setSelectedIndex(2);
		} else if (evaluationFunction2 == EvaluationFunction.SHANNON) {
			evaluationFunction2DropDown.setSelectedIndex(3);
		}

		whiteSquareColorDropDown = new JComboBox<>();
		whiteSquareColorDropDown.addItem("White");
		whiteSquareColorDropDown.addItem("Pink");

		if (selectedWhiteSquareColor == Color.WHITE) {
			whiteSquareColorDropDown.setSelectedIndex(0);
		} else if (selectedWhiteSquareColor == GuiConstants.BRIGHT_PINK) {
			whiteSquareColorDropDown.setSelectedIndex(1);
		}

		blackSquareColorDropDown = new JComboBox<>();
		blackSquareColorDropDown.addItem("Dark Green");
		blackSquareColorDropDown.addItem("Black");
		blackSquareColorDropDown.addItem("Dark Gray");
		blackSquareColorDropDown.addItem("Gray");

		if (selectedBlackSquareColor == GuiConstants.DARK_GREEN) {
			blackSquareColorDropDown.setSelectedIndex(0);
		} else if (selectedBlackSquareColor == Color.BLACK) {
			blackSquareColorDropDown.setSelectedIndex(1);
		} else if (selectedBlackSquareColor == Color.DARK_GRAY) {
			blackSquareColorDropDown.setSelectedIndex(2);
		} else if (selectedBlackSquareColor == Color.GRAY) {
			blackSquareColorDropDown.setSelectedIndex(3);
		}

		numOfRowsSpinnerModel.setValue(numOfRows);

		showNextMovesCheckBox = new JCheckBox();
		showNextMovesCheckBox.setSelected(showNextMoves);

		enableTimeLimitCheckBox = new JCheckBox();
		enableTimeLimitCheckBox.setSelected(enableTimeLimit);

		timeLimitSecondsSpinnerModel.setValue(timeLimitSeconds);

		components.add(guiTypeDropDown);
		components.add(guiStyleDropDown);
		components.add(enableSoundsCheckBox);
		components.add(humanAllegianceDropDown);
		components.add(gameModeDropDown);
		components.add(ai1TypeDropDown);
		components.add(ai2TypeDropDown);
		components.add(ai1MaxDepthDropDown);
		components.add(ai2MaxDepthDropDown);
		components.add(evaluationFunction1DropDown);
		components.add(evaluationFunction2DropDown);
		components.add(whiteSquareColorDropDown);
		components.add(blackSquareColorDropDown);
		if (newGameParameters.getGuiType() == GuiType.BUTTONS) {
			components.add(numOfRowsSpinner);
		}
		components.add(showNextMovesCheckBox);
		components.add(enableTimeLimitCheckBox);
		components.add(timeLimitSecondsSpinner);

		int x = 25;
		int y = 25;
		int distance = 35;
		int w = 240;
		int h = 25;
		for (JLabel label : labels) {
			label.setBounds(x, y, w, h);
			y = y + distance;
			super.add(label);
		}

		x = 265;
		y = 25;
		w = 220;
		for (JComponent component : components) {
			component.setBounds(x, y, w, h);
			y = y + distance;
			super.add(component);
		}

		apply = new JButton("Apply");
		cancel = new JButton("Cancel");

		distance = 10;
		y = 600;
		w = 100;
		h = 30;
		EventHandler handler = new EventHandler();
		apply.setBounds((width / 2) - 110 - (distance / 2), y, w, h);
		apply.addActionListener(handler);
		cancel.setBounds((width / 2) - 10 + (distance / 2), y, w, h);
		cancel.addActionListener(handler);

		super.add(apply);
		super.add(cancel);
	}


	private class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == cancel) {
				dispose();
			} else if (event.getSource() == apply) {
				try {
					GuiType guiType = GuiType.valueOf(guiTypeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					GuiStyle guiStyle = GuiStyle.valueOf(guiStyleDropDown.getSelectedItem().toString().toUpperCase()
							.replace("-", "_"));
					boolean enableSounds = enableSoundsCheckBox.isSelected();
					Allegiance humanAllegiance = Allegiance
							.valueOf(humanAllegianceDropDown.getSelectedItem().toString().toUpperCase());
					GameMode gameMode = GameMode.valueOf(gameModeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					AiType ai1Type = AiType.valueOf(ai1TypeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_").replace("-", "_"));
					AiType ai2Type = AiType.valueOf(ai2TypeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_").replace("-", "_"));
					int ai1MaxDepth = (int) ai1MaxDepthDropDown.getSelectedItem();
					int ai2MaxDepth = (int) ai2MaxDepthDropDown.getSelectedItem();
					EvaluationFunction evaluationFunction1 = EvaluationFunction.valueOf(evaluationFunction1DropDown
							.getSelectedItem().toString().toUpperCase());
					EvaluationFunction evaluationFunction2 = EvaluationFunction.valueOf(evaluationFunction2DropDown
							.getSelectedItem().toString().toUpperCase());
					int whiteSquareColorDropdownIndex = whiteSquareColorDropDown.getSelectedIndex();
					int blackSquareColorDropdownIndex = blackSquareColorDropDown.getSelectedIndex();
					int numOfRows = (int) numOfRowsSpinner.getValue();
					// numOfRows = Math.max(numOfRows, 8);
					boolean showNextMoves = showNextMovesCheckBox.isSelected();
					boolean enableTimeLimit = enableTimeLimitCheckBox.isSelected();
					int timeLimitSeconds = (int) timeLimitSecondsSpinner.getValue();

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

					newGameParameters.setGuiType(guiType);
					newGameParameters.setGuiStyle(guiStyle);
					newGameParameters.setEnableSounds(enableSounds);
					newGameParameters.setHumanAllegiance(humanAllegiance);
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
					newGameParameters.setShowNextMoves(showNextMoves);
					newGameParameters.setEnableTimeLimit(enableTimeLimit);
					newGameParameters.setTimeLimitSeconds(timeLimitSeconds);

					JOptionPane.showMessageDialog(
							parentComponent,
							"Game settings have been changed.\n" +
									"The changes will be applied in the next new game.",
							"",
							JOptionPane.INFORMATION_MESSAGE
					);
					dispose();
				} catch (Exception ex) {
					System.err.println("ERROR: " + ex.getMessage());
				}

			}  // else if.

		}  // action performed.

	}  // inner class.

}  // class end.
