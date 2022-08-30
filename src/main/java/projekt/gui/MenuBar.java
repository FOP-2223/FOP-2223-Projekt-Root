package projekt.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {

    private final MainFrame mf;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem openFileMenuEntry;
    private javax.swing.JMenuItem saveFileMenuEntry;
    private javax.swing.JMenuItem centerViewMenuEntry;
    private javax.swing.JMenuItem resetZoomMenuEntry;

    public MenuBar(MainFrame mf) {
        this.mf = mf;
        initComponents();
    }

    private void initComponents() {
        fileMenu = new javax.swing.JMenu();
        editMenu = new javax.swing.JMenu();
        viewMenu = new javax.swing.JMenu();
        openFileMenuEntry = new javax.swing.JMenuItem();
        saveFileMenuEntry = new javax.swing.JMenuItem();
        centerViewMenuEntry = new javax.swing.JMenuItem();
        resetZoomMenuEntry = new javax.swing.JMenuItem();

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
