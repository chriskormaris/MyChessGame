package com.chriskormaris.mychessgame.gui.frame;

import javax.swing.*;
import java.awt.*;

public class ScrollableTextFrame extends JFrame {

    public ScrollableTextFrame(Component parentComponent, String title, String text) throws HeadlessException {
        super(title);

        super.setSize(900, 700);
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setLocationRelativeTo(parentComponent);
        super.setResizable(false);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setText(text);

        JScrollPane scrollBar = new JScrollPane(textArea);
        getContentPane().add(scrollBar);
    }

}
