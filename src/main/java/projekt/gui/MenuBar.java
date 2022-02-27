package projekt.gui;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar{

    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem openFileMenuEntry;
    private javax.swing.JMenuItem saveFileMenuEntry;

    public MenuBar(){
        initComponents();
    }

    private void initComponents(){
        fileMenu = new javax.swing.JMenu();
        openFileMenuEntry = new javax.swing.JMenuItem();
        saveFileMenuEntry = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();

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
    }
}
