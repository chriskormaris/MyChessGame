package com.chriskormaris.mychessgame.gui;


import com.chriskormaris.mychessgame.api.enumeration.AiType;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.enumeration.GameMode;
import com.chriskormaris.mychessgame.gui.enumeration.GuiType;
import com.chriskormaris.mychessgame.gui.enumeration.GuiStyle;
import com.chriskormaris.mychessgame.gui.util.GameParameters;
import com.chriskormaris.mychessgame.gui.util.GuiConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SettingsWindow extends JFrame {

	private final JComboBox<String> guiStyleDropDown;
	private final JCheckBox enableSoundsCheckBox;
	private final JComboBox<String> humanPlayerAllegianceDropDown;
	private final JComboBox<String> gameModeDropDown;
	private final JComboBox<String> ai1TypeDropDown;
	private final JComboBox<String> ai2TypeDropDown;
	private final JComboBox<Integer> ai1MaxDepthDropDown;
	private final JComboBox<Integer> ai2MaxDepthDropDown;
	private final JComboBox<String> evaluationFunction1DropDown;
	private final JComboBox<String> evaluationFunction2DropDown;
	private final JComboBox<String> whiteSquareColorDropDown;
	private final JComboBox<String> blackSquareColorDropDown;

	private final SpinnerModel numOfRowsSpinnerModel = new SpinnerNumberModel(8, 4, 8, 1);
	private final JSpinner numOfRowsSpinner = new JSpinner(numOfRowsSpinnerModel);

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
		int ai1MaxDepth = gameParameters.getAi1MaxDepth() - 1;
		int ai2MaxDepth = gameParameters.getAi2MaxDepth() - 1;
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
		JLabel ai1MaxDepthLabel = new JLabel("Minimax AI 1 depth");
		JLabel ai2MaxDepthLabel = new JLabel("Minimax AI 2 depth (AIvsAI)");
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
		add(ai1MaxDepthLabel);
		add(ai2MaxDepthLabel);
		add(evaluationFunction1Label);
		add(evaluationFunction2Label);
		add(whiteSquareColorLabel);
		add(blackSquareColorLabel);
		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(numOfRowsLabel);
		}

		guiStyleDropDown = new JComboBox<>();
		guiStyleDropDown.addItem("Cross-platform style");
		guiStyleDropDown.addItem("Nimbus style");

		if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM_STYLE) {
			guiStyleDropDown.setSelectedIndex(0);
		} else if (selectedGuiStyle == GuiStyle.NIMBUS_STYLE) {
			guiStyleDropDown.setSelectedIndex(1);
		}

		enableSoundsCheckBox = new JCheckBox();
		enableSoundsCheckBox.setSelected(enableSounds);

		humanPlayerAllegianceDropDown = new JComboBox<>();
		humanPlayerAllegianceDropDown.addItem("White");
		humanPlayerAllegianceDropDown.addItem("Black");

		if (humanPlayerAllegiance == Allegiance.WHITE) {
			humanPlayerAllegianceDropDown.setSelectedIndex(0);
		} else if (humanPlayerAllegiance == Allegiance.BLACK) {
			humanPlayerAllegianceDropDown.setSelectedIndex(1);
		}

		gameModeDropDown = new JComboBox<>();
		gameModeDropDown.addItem("Human Vs AI");
		gameModeDropDown.addItem("Human Vs Human");
		gameModeDropDown.addItem("AI Vs AI");

		if (selectedGameMode == GameMode.HUMAN_VS_AI) {
			gameModeDropDown.setSelectedIndex(0);
		} else if (selectedGameMode == GameMode.HUMAN_VS_HUMAN) {
			gameModeDropDown.setSelectedIndex(1);
		} else if (selectedGameMode == GameMode.AI_VS_AI) {
			gameModeDropDown.setSelectedIndex(2);
		}

		ai1TypeDropDown = new JComboBox<>();
		ai1TypeDropDown.addItem("Minimax AI");
		ai1TypeDropDown.addItem("Random AI");

		if (selectedAi1Type == AiType.MINIMAX_AI) {
			ai1TypeDropDown.setSelectedIndex(0);
		} else if (selectedAi1Type == AiType.RANDOM_AI) {
			ai1TypeDropDown.setSelectedIndex(1);
		}

		ai2TypeDropDown = new JComboBox<>();
		ai2TypeDropDown.addItem("Minimax AI");
		ai2TypeDropDown.addItem("Random AI");

		if (selectedAi2Type == AiType.MINIMAX_AI) {
			ai2TypeDropDown.setSelectedIndex(0);
		} else if (selectedAi2Type == AiType.RANDOM_AI) {
			ai2TypeDropDown.setSelectedIndex(1);
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

		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(guiStyleDropDown);
		}
		add(enableSoundsCheckBox);
		add(humanPlayerAllegianceDropDown);
		add(gameModeDropDown);
		add(ai1TypeDropDown);
		add(ai2TypeDropDown);
		add(ai1MaxDepthDropDown);
		add(ai2MaxDepthDropDown);
		add(evaluationFunction1DropDown);
		add(evaluationFunction2DropDown);
		add(whiteSquareColorDropDown);
		add(blackSquareColorDropDown);
		if (gameParameters.getGuiType() == GuiType.BUTTONS) {
			add(numOfRowsSpinner);
		}

		guiStyleLabel.setBounds(25, 25, 205, 25);
		enableSoundsLabel.setBounds(25, 60, 205, 25);
		humanPlayerAllegianceLabel.setBounds(25, 95, 205, 25);
		gameModeLabel.setBounds(25, 130, 205, 25);
		ai1TypeLabel.setBounds(25, 165, 205, 25);
		ai2TypeLabel.setBounds(25, 200, 205, 25);
		ai1MaxDepthLabel.setBounds(25, 235, 205, 25);
		ai2MaxDepthLabel.setBounds(25, 270, 205, 25);
		evaluationFunction1Label.setBounds(25, 305, 205, 25);
		evaluationFunction2Label.setBounds(25, 340, 205, 25);
		whiteSquareColorLabel.setBounds(25, 375, 205, 25);
		blackSquareColorLabel.setBounds(25, 405, 205, 25);
		numOfRowsLabel.setBounds(25, 440, 205, 25);

		guiStyleDropDown.setBounds(225, 25, 180, 25);
		enableSoundsCheckBox.setBounds(225, 60, 180, 25);
		humanPlayerAllegianceDropDown.setBounds(225, 95, 180, 25);
		gameModeDropDown.setBounds(225, 130, 180, 25);
		ai1TypeDropDown.setBounds(225, 165, 180, 25);
		ai2TypeDropDown.setBounds(225, 200, 180, 25);
		ai1MaxDepthDropDown.setBounds(225, 235, 180, 25);
		ai2MaxDepthDropDown.setBounds(225, 270, 180, 25);
		evaluationFunction1DropDown.setBounds(225, 305, 180, 25);
		evaluationFunction2DropDown.setBounds(225, 340, 180, 25);
		whiteSquareColorDropDown.setBounds(225, 375, 180, 25);
		blackSquareColorDropDown.setBounds(225, 405, 180, 25);
		numOfRowsSpinner.setBounds(225, 440, 180, 25);

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

					GuiStyle guiStyle = GuiStyle.valueOf(guiStyleDropDown.getSelectedItem().toString().toUpperCase()
							.replace("-", "_").replace(" ", "_"));
					boolean enableSounds = enableSoundsCheckBox.isSelected();
					Allegiance humanPlayerAllegiance = Allegiance.valueOf(humanPlayerAllegianceDropDown
							.getSelectedItem().toString().toUpperCase());
					GameMode gameMode = GameMode.valueOf(gameModeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					AiType ai1Type = AiType.valueOf(ai1TypeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
					AiType ai2Type = AiType.valueOf(ai2TypeDropDown.getSelectedItem().toString().toUpperCase()
							.replace(" ", "_"));
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
