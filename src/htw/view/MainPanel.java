package htw.view;


import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private final View view;

    public MainPanel(View view) {
        this.view = view;
        this.setBackground(Color.lightGray);
        this.setFocusable(true);
        this.setVisible(true);
    }
}

