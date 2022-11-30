package projekt.gui;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;

public class MyMenuBar extends MenuBar {

    private final MainFrame mf;
    private Menu editMenu;
    private Menu fileMenu;
    private Menu viewMenu;
    private MenuItem openFileMenuEntry;
    private MenuItem saveFileMenuEntry;
    private MenuItem centerViewMenuEntry;
    private MenuItem resetZoomMenuEntry;

    public MyMenuBar(MainFrame mf) {
        this.mf = mf;
        initComponents();
    }

    private void initComponents() {
        fileMenu = new Menu();
        editMenu = new Menu();
        viewMenu = new Menu();
        openFileMenuEntry = new MenuItem();
        saveFileMenuEntry = new MenuItem();
        centerViewMenuEntry = new MenuItem();
        resetZoomMenuEntry = new MenuItem();

        final Font dialog = new Font("Dialog", 16);

        fileMenu.setText("File");
        //fileMenu.setFont(dialog); // NOI18N

        //openFileMenuEntry.setFont(dialog); // NOI18N
        openFileMenuEntry.setText("Open");
        //saveFileMenuEntry.setFont(dialog); // NOI18N
        saveFileMenuEntry.setText("Save");

        fileMenu.getItems().addAll(openFileMenuEntry, saveFileMenuEntry);
        getMenus().add(fileMenu);

        editMenu.setText("Edit");
        //editMenu.setFont(dialog); // NOI18N
        getMenus().add(editMenu);

        viewMenu.setText("View");
        //viewMenu.setFont(dialog); // NOI18N

        //centerViewMenuEntry.setFont(dialog); // NOI18N
        centerViewMenuEntry.setText("Center View");
        centerViewMenuEntry.setOnAction(this::centerViewMenuEntry_actionPerformed);
        viewMenu.getItems().add(centerViewMenuEntry);

        //resetZoomMenuEntry.setFont(dialog); // NOI18N
        resetZoomMenuEntry.setText("Reset Zoom");
        resetZoomMenuEntry.setOnAction(this::resetZoomMenuEntry_actionPerformed);
        viewMenu.getItems().add(resetZoomMenuEntry);

        getMenus().add(viewMenu);
    }

    public void centerViewMenuEntry_actionPerformed(ActionEvent e) {
        //mf.getMapPanel().resetCenterLocation(); TODO
    }

    public void resetZoomMenuEntry_actionPerformed(ActionEvent e) {
        //mf.getMapPanel().resetScale(); TODO
    }
}
