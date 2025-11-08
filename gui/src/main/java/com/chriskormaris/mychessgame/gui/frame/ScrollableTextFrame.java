package com.chriskormaris.mychessgame.gui.frame;

import javax.swing.*;
import java.awt.*;

public class ScrollableTextFrame extends JFrame {

    public ScrollableTextFrame(Component parentComponent, String title, String text) throws HeadlessException {
        super(title);

        super.setSize(900, 700);
        super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        super.setLocationRelativeTo(parentComponent);
        super.setResizable(false);

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFocusable(false);
        textPane.setContentType("text/html");
        textPane.setText(text);
	    textPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textPane);
        getContentPane().add(scrollPane);
    }

}
