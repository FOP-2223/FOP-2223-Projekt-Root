package projekt.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {

    private final MainFrame mf;
    private JMenu editMenu;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenuItem openFileMenuEntry;
    private JMenuItem saveFileMenuEntry;
    private JMenuItem centerViewMenuEntry;
    private JMenuItem resetZoomMenuEntry;

    public MenuBar(MainFrame mf) {
        this.mf = mf;
        initComponents();
    }

    private void initComponents() {
        fileMenu = new JMenu();
        editMenu = new JMenu();
        viewMenu = new JMenu();
        openFileMenuEntry = new JMenuItem();
        saveFileMenuEntry = new JMenuItem();
        centerViewMenuEntry = new JMenuItem();
        resetZoomMenuEntry = new JMenuItem();

        setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N

        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        openFileMenuEntry.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        openFileMenuEntry.setText("Open");
        fileMenu.add(openFileMenuEntry);

        saveFileMenuEntry.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        saveFileMenuEntry.setText("Save");
        fileMenu.add(saveFileMenuEntry);

        add(fileMenu);

        editMenu.setText("Edit");
        editMenu.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        add(editMenu);

        viewMenu.setText("View");
        viewMenu.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        centerViewMenuEntry.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        centerViewMenuEntry.setText("Center View");
        centerViewMenuEntry.addActionListener(this::centerViewMenuEntry_actionPerformed);
        viewMenu.add(centerViewMenuEntry);

        resetZoomMenuEntry.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        resetZoomMenuEntry.setText("Reset Zoom");
        resetZoomMenuEntry.addActionListener(this::resetZoomMenuEntry_actionPerformed);
        viewMenu.add(resetZoomMenuEntry);

        add(viewMenu);
    }

    public void centerViewMenuEntry_actionPerformed(ActionEvent e) {
        //mf.getMapPanel().resetCenterLocation(); TODO
    }

    public void resetZoomMenuEntry_actionPerformed(ActionEvent e) {
        //mf.getMapPanel().resetScale(); TODO
    }
}
