package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateBookForm extends JFrame {

    public UpdateBookForm(
            Dashboard dashboard,
            int id,
            String oldTitle,
            String oldAuthor,
            String oldStatus
    ) {

        setTitle("Update Book");

        setSize(300, 300);

        setLocationRelativeTo(null);

        setLayout(new GridLayout(7,1));

        JLabel titleLabel = new JLabel("Book Title");

        JTextField titleField =
                new JTextField(oldTitle);

        JLabel authorLabel = new JLabel("Author");

        JTextField authorField =
                new JTextField(oldAuthor);

        JLabel statusLabel = new JLabel("Status");

        String[] options = {"Available", "Borrowed"};

        JComboBox<String> statusBox =
                new JComboBox<>(options);

        statusBox.setSelectedItem(oldStatus);

        JButton updateButton =
                new JButton("Update");

        add(titleLabel);

        add(titleField);

        add(authorLabel);

        add(authorField);

        add(statusLabel);

        add(statusBox);

        add(updateButton);

        updateButton.addActionListener(e -> {

            try {

                Connection conn =
                        DatabaseConnection.connect();

                String sql =
                        "UPDATE books SET title=?, author=?, status=? WHERE id=?";

                PreparedStatement pst =
                        conn.prepareStatement(sql);

                pst.setString(1, titleField.getText());

                pst.setString(2, authorField.getText());

                pst.setString(
                        3,
                        statusBox.getSelectedItem().toString()
                );

                pst.setInt(4, id);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        null,
                        "Book Updated Successfully"
                );

                dashboard.loadTable();

                conn.close();

                dispose();

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}