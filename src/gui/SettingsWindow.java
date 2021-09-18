package gui;


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

import enumeration.AiType;
import enumeration.Allegiance;
import enumeration.GameMode;
import enumeration.GuiStyle;
import utility.Constants;
import utility.GameParameters;


public class SettingsWindow extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 6651737783332653136L;
    public static int width = 450;
    public static int height = 490;
    private final JComboBox<String> gui_style_drop_down;
    private final JCheckBox enable_sounds_check_box;
    private final JComboBox<String> human_player_allegiance_drop_down;
    private final JComboBox<String> game_mode_drop_down;
    private final JComboBox<String> ai_type_drop_down;
    private final JComboBox<Integer> max_depth1_drop_down;
    private final JComboBox<Integer> max_depth2_drop_down;
    private final JComboBox<String> white_tile_color_drop_down;
    private final JComboBox<String> black_tile_color_drop_down;
    private final SpinnerModel spinnerModel = new SpinnerNumberModel(8, 4, 8, 1);
    private final JSpinner num_of_rows_spinner = new JSpinner(spinnerModel);
    private final JButton apply;
    private final JButton cancel;


    public SettingsWindow() {
        super("Settings");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(width, height);
        setLocationRelativeTo(null);
        setResizable(false);

        EventHandler handler = new EventHandler();

        GuiStyle selectedGuiStyle = ChessGUI.gameParameters.getGuiStyle();
        boolean enableSounds = ChessGUI.gameParameters.isEnableSounds();
        Allegiance humanPlayerAllegiance = ChessGUI.gameParameters.getHumanPlayerAllegiance();
        GameMode selectedGameMode = ChessGUI.gameParameters.getGameMode();
        AiType selectedAiMode = ChessGUI.gameParameters.getAiType();
        int maxDepth1 = ChessGUI.gameParameters.getAi1MaxDepth() - 1;
        int maxDepth2 = ChessGUI.gameParameters.getAi2MaxDepth() - 1;
        Color selectedWhiteTileColor = ChessGUI.gameParameters.getWhiteTileColor();
        Color selectedBlackTileColor = ChessGUI.gameParameters.getBlackTileColor();
        int numOfRows = ChessGUI.gameParameters.getNumOfRows();


        JLabel guiStyleLabel = new JLabel("GUI style");
        JLabel enableSoundsLabel = new JLabel("Sounds");
        JLabel humanPlayerAllegianceLabel = new JLabel("Human Player Allegiance");
        JLabel gameModeLabel = new JLabel("Game mode");
        JLabel aiTypeLabel = new JLabel("AI type");
        JLabel maxDepth1Label = new JLabel("Minimax AI 1 depth");
        JLabel maxDepth2Label = new JLabel("Minimax AI 2 depth (AiVsAi)");
        JLabel whiteTileColorLabel = new JLabel("White tile color");
        JLabel blackTileColorLabel = new JLabel("Black tile color");
        JLabel numOfRowsLabel = new JLabel("Number of rows");


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


        gui_style_drop_down = new JComboBox<>();
        gui_style_drop_down.addItem("Cross-platform style");
        gui_style_drop_down.addItem("Nimbus style");

        if (selectedGuiStyle == GuiStyle.CROSS_PLATFORM_STYLE)
            gui_style_drop_down.setSelectedIndex(0);
        else if (selectedGuiStyle == GuiStyle.NIMBUS_STYLE)
            gui_style_drop_down.setSelectedIndex(1);


        enable_sounds_check_box = new JCheckBox();
        enable_sounds_check_box.setSelected(enableSounds);


        human_player_allegiance_drop_down = new JComboBox<>();
        human_player_allegiance_drop_down.addItem("White");
        human_player_allegiance_drop_down.addItem("Black");

        if (humanPlayerAllegiance == Allegiance.WHITE)
            human_player_allegiance_drop_down.setSelectedIndex(0);
        else if (humanPlayerAllegiance == Allegiance.BLACK)
            human_player_allegiance_drop_down.setSelectedIndex(1);


        game_mode_drop_down = new JComboBox<>();
        game_mode_drop_down.addItem("Human Vs AI");
        game_mode_drop_down.addItem("Human Vs Human");
        game_mode_drop_down.addItem("AI Vs AI");

        if (selectedGameMode == GameMode.HUMAN_VS_AI)
            game_mode_drop_down.setSelectedIndex(0);
        else if (selectedGameMode == GameMode.HUMAN_VS_HUMAN)
            game_mode_drop_down.setSelectedIndex(1);
        else if (selectedGameMode == GameMode.AI_VS_AI)
            game_mode_drop_down.setSelectedIndex(2);

        ai_type_drop_down = new JComboBox<>();
        ai_type_drop_down.addItem("Minimax AI");
        ai_type_drop_down.addItem("Random AI");

        if (selectedAiMode == AiType.MINIMAX_AI)
            ai_type_drop_down.setSelectedIndex(0);
        else if (selectedAiMode == AiType.RANDOM_AI)
            ai_type_drop_down.setSelectedIndex(1);

        max_depth1_drop_down = new JComboBox<>();
        max_depth1_drop_down.addItem(1);
        max_depth1_drop_down.addItem(2);
        max_depth1_drop_down.addItem(3);

        max_depth1_drop_down.setSelectedIndex(maxDepth1);

        max_depth2_drop_down = new JComboBox<>();
        max_depth2_drop_down.addItem(1);
        max_depth2_drop_down.addItem(2);
        max_depth2_drop_down.addItem(3);

        max_depth2_drop_down.setSelectedIndex(maxDepth2);

        white_tile_color_drop_down = new JComboBox<>();
        white_tile_color_drop_down.addItem("White");
        white_tile_color_drop_down.addItem("Pink");

        if (selectedWhiteTileColor == Color.WHITE)
            white_tile_color_drop_down.setSelectedIndex(0);
        else if (selectedWhiteTileColor == Constants.BRIGHT_PINK)
            white_tile_color_drop_down.setSelectedIndex(1);

        black_tile_color_drop_down = new JComboBox<>();
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

        guiStyleLabel.setBounds(25, 25, 205, 25);
        enableSoundsLabel.setBounds(25, 60, 205, 25);
        humanPlayerAllegianceLabel.setBounds(25, 95, 205, 25);
        gameModeLabel.setBounds(25, 130, 205, 25);
        aiTypeLabel.setBounds(25, 165, 205, 25);
        maxDepth1Label.setBounds(25, 200, 205, 25);
        maxDepth2Label.setBounds(25, 235, 205, 25);
        whiteTileColorLabel.setBounds(25, 270, 205, 25);
        blackTileColorLabel.setBounds(25, 305, 205, 25);
        numOfRowsLabel.setBounds(25, 340, 205, 25);

        gui_style_drop_down.setBounds(225, 25, 180, 25);
        enable_sounds_check_box.setBounds(225, 60, 180, 25);
        human_player_allegiance_drop_down.setBounds(225, 95, 180, 25);
        game_mode_drop_down.setBounds(225, 130, 180, 25);
        ai_type_drop_down.setBounds(225, 165, 180, 25);
        max_depth1_drop_down.setBounds(225, 200, 180, 25);
        max_depth2_drop_down.setBounds(225, 235, 180, 25);
        white_tile_color_drop_down.setBounds(225, 270, 180, 25);
        black_tile_color_drop_down.setBounds(225, 305, 180, 25);
        num_of_rows_spinner.setBounds(225, 340, 180, 25);

        apply = new JButton("Apply");
        cancel = new JButton("Cancel");
        add(apply);
        add(cancel);

        int distance = 10;
        apply.setBounds((width / 2) - 110 - (distance / 2), 400, 100, 30);
        apply.addActionListener(handler);
        cancel.setBounds((width / 2) - 10 + (distance / 2), 400, 100, 30);
        cancel.addActionListener(handler);
    }


    private class EventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {

            if (ev.getSource() == cancel) {
                dispose();
            } else if (ev.getSource() == apply) {
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
                    // numOfRows = Math.max(numOfRows, 8);

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
                } catch (Exception e) {
                    System.err.println("ERROR: " + e.getMessage());
                }

            }  // else if.

        }  // action performed.

    }  // inner class.

}  // class end.
