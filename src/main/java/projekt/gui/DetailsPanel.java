package projekt.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class DetailsPanel extends JPanel {
    private JScrollPane scrollPane;
    private JTextArea detailsArea;

    public DetailsPanel() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Location Details"));
        detailsArea = new JTextArea(5, 20);
        scrollPane = new JScrollPane(detailsArea);
        add(scrollPane, BorderLayout.CENTER);
    }
}
