package com.chriskormaris.mychessgame.gui.settings;


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


public class SettingsWindow extends JFrame {

	private final JComboBox<String> guiStyleDropDown;
	private final JComboBox<String> guiTypeDropDown;
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

		int width = 450;
		int height = 620;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(null);
		setSize(width, height);
		setLocationRelativeTo(parentComponent);
		setResizable(false);

		EventHandler handler = new EventHandler();

		newGameParameters.setGuiType(gameParameters.getGuiType());

		GuiStyle selectedGuiStyle = gameParameters.getGuiStyle();
		GuiType selectedGuiType = gameParameters.getGuiType();
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
		JLabel guiTypeLabel = new JLabel("GUI type");
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
		add(guiTypeLabel);
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

		guiTypeDropDown = new JComboBox<>();
		guiTypeDropDown.addItem("Drag and Drop");
		guiTypeDropDown.addItem("Buttons");

		if (selectedGuiType == GuiType.DRAG_AND_DROP) {
			guiTypeDropDown.setSelectedIndex(0);
		} else if (selectedGuiType == GuiType.BUTTONS) {
			guiTypeDropDown.setSelectedIndex(1);
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
		add(guiTypeDropDown);
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

		int x = 25;
		int y = 25;
		int distance = 35;
		int w = 205;
		int h = 25;
		guiStyleLabel.setBounds(x, y, w, h);
		guiTypeLabel.setBounds(x, y = y + distance, w, h);
		enableSoundsLabel.setBounds(x, y = y + distance, w, h);
		humanPlayerAllegianceLabel.setBounds(25, y = y + distance, w, h);
		gameModeLabel.setBounds(x, y = y + distance, w, h);
		ai1TypeLabel.setBounds(x, y = y + distance, w, h);
		ai2TypeLabel.setBounds(x, y = y + distance, w, h);
		ai1MaxDepthLabel.setBounds(x, y = y + distance, w, h);
		ai2MaxDepthLabel.setBounds(x, y = y + distance, w, h);
		evaluationFunction1Label.setBounds(x, y = y + distance, w, h);
		evaluationFunction2Label.setBounds(x, y = y + distance, w, h);
		whiteSquareColorLabel.setBounds(x, y = y + distance, w, h);
		blackSquareColorLabel.setBounds(x, y = y + distance, w, h);
		numOfRowsLabel.setBounds(x, y + distance, w, h);

		x = 225;
		y = 25;
		w = 180;
		guiStyleDropDown.setBounds(x, y, w, h);
		guiTypeDropDown.setBounds(x, y = y + distance, w, h);
		enableSoundsCheckBox.setBounds(x, y = y + distance, w, h);
		humanPlayerAllegianceDropDown.setBounds(x, y = y + distance, w, h);
		gameModeDropDown.setBounds(x, y = y + distance, w, h);
		ai1TypeDropDown.setBounds(x, y = y + distance, w, h);
		ai2TypeDropDown.setBounds(x, y = y + distance, w, h);
		ai1MaxDepthDropDown.setBounds(x, y = y + distance, w, h);
		ai2MaxDepthDropDown.setBounds(x, y = y + distance, w, h);
		evaluationFunction1DropDown.setBounds(x, y = y + distance, w, h);
		evaluationFunction2DropDown.setBounds(x, y = y + distance, w, h);
		whiteSquareColorDropDown.setBounds(x, y = y + distance, w, h);
		blackSquareColorDropDown.setBounds(x, y = y + distance, w, h);
		numOfRowsSpinner.setBounds(x, y + distance, w, h);

		apply = new JButton("Apply");
		cancel = new JButton("Cancel");
		add(apply);
		add(cancel);

		distance = 10;
		y = 520;
		w = 100;
		h = 30;
		apply.setBounds((width / 2) - 110 - (distance / 2), y, w, h);
		apply.addActionListener(handler);
		cancel.setBounds((width / 2) - 10 + (distance / 2), y, w, h);
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
					GuiType guiType = GuiType.valueOf(guiTypeDropDown.getSelectedItem().toString().toUpperCase()
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
					newGameParameters.setGuiType(guiType);
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
