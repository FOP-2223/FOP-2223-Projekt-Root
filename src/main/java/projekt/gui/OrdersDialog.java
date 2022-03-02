package projekt.gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

class OrdersDialog extends JDialog {

    JTextField textField1 = new JTextField();
    JTextField textField2 = new JTextField();
    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");

    OrdersDialog() {
        setMinimumSize(new Dimension(300, 300));
        setModal(true);
    }

    void showAddOrderDialog(OrdersPanel ordersPanel) {
        setTitle("Add order");
        setLayout(new GridLayout(3, 2));

        textField1.setText("");
        textField2.setText("");

        add(textField1);
        add(textField2);
        add(okButton);
        add(cancelButton);

        for (ActionListener listener : okButton.getActionListeners()) {
            okButton.removeActionListener(listener);
        }
        okButton.addActionListener(actionEvent -> {
            // ordersPanel.addOrder(textField1.getText()); // TODO add order in model?!
            setVisible(false);
        });
        for (ActionListener listener : cancelButton.getActionListeners()) {
            cancelButton.removeActionListener(listener);
        }
        cancelButton.addActionListener(actionEvent -> setVisible(false));

        pack();
        setModal(true);
        setVisible(true);
    }

    void showEditOrderDialog() {
        setTitle("Edit order");
        setLayout(new BorderLayout());

        add(okButton, BorderLayout.EAST);
        add(cancelButton, BorderLayout.WEST);

        okButton.addActionListener(actionEvent -> setVisible(false));
        cancelButton.addActionListener(actionEvent -> setVisible(false));

        pack();
        setModal(true);
        setVisible(true);
    }
}
