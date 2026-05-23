package com.library;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddBookForm extends JFrame {

    public AddBookForm(Dashboard dashboard) {

        setTitle("Add Book");

        setSize(300, 300);

        setLocationRelativeTo(null);

        setLayout(new GridLayout(7, 1));

        JLabel titleLabel = new JLabel("Book Title");

        JTextField titleField = new JTextField();

        JLabel authorLabel = new JLabel("Author");

        JTextField authorField = new JTextField();

        JLabel statusLabel = new JLabel("Status");

        String[] options = {"Available", "Borrowed"};

        JComboBox<String> statusBox =
                new JComboBox<>(options);

        JButton saveButton = new JButton("Save");

        add(titleLabel);

        add(titleField);

        add(authorLabel);

        add(authorField);

        add(statusLabel);

        add(statusBox);

        add(saveButton);

        saveButton.addActionListener(e -> {

            try {

                Connection conn =
                        DatabaseConnection.connect();

                String sql =
                        "INSERT INTO books(title, author, status) VALUES(?, ?, ?)";

                PreparedStatement pst =
                        conn.prepareStatement(sql);

                pst.setString(1, titleField.getText());

                pst.setString(2, authorField.getText());

                pst.setString(
                        3,
                        statusBox.getSelectedItem().toString()
                );

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                        null,
                        "Book Added Successfully"
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