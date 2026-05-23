package com.library;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard extends JFrame {

    JTable table;
    DefaultTableModel model;

    public Dashboard() {

        setTitle("Library Book Inventory");

        setSize(700, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ================= HEADER PANEL =================

        JPanel headerPanel = new JPanel();

        headerPanel.setBackground(new Color(41, 128, 185));

        headerPanel.setPreferredSize(new Dimension(100, 60));

        JLabel title = new JLabel(
                "Library Book Inventory System"
        );

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        headerPanel.add(title);

        // ================= SEARCH PANEL =================

        JPanel searchPanel = new JPanel();

        searchPanel.setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        10
                )
        );

        JLabel searchLabel = new JLabel("Search:");

        searchLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        JTextField searchField =
                new JTextField(25);

        searchField.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        searchField.setPreferredSize(
                new Dimension(250, 35)
        );

        JButton searchButton =
                new JButton("Search");

        searchPanel.add(searchLabel);

        searchPanel.add(searchField);

        searchPanel.add(searchButton);

        // ================= TOP PANEL =================

        JPanel topPanel = new JPanel();

        topPanel.setLayout(new BorderLayout());

        topPanel.add(headerPanel, BorderLayout.NORTH);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ================= TABLE =================

        model = new DefaultTableModel();

        model.setColumnIdentifiers(
                new String[]{"ID", "Title", "Author", "Status"}
        );

        table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        add(pane, BorderLayout.CENTER);

        // ================= SEARCH FILTER =================

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(model);

        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {

                    public void insertUpdate(DocumentEvent e) {

                        sorter.setRowFilter(
                                RowFilter.regexFilter(
                                        "(?i)" + searchField.getText()
                                )
                        );
                    }

                    public void removeUpdate(DocumentEvent e) {

                        sorter.setRowFilter(
                                RowFilter.regexFilter(
                                        "(?i)" + searchField.getText()
                                )
                        );
                    }

                    public void changedUpdate(DocumentEvent e) {

                    }
                }
        );

        // ================= BUTTON PANEL =================

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Book");

        JButton updateButton = new JButton("Update Book");

        JButton deleteButton = new JButton("Delete Book");

        JButton refreshButton = new JButton("Refresh");

        // Button Style

        addButton.setFocusPainted(false);

        updateButton.setFocusPainted(false);

        deleteButton.setFocusPainted(false);

        refreshButton.setFocusPainted(false);

        addButton.setBackground(
                new Color(46, 204, 113)
        );

        updateButton.setBackground(
                new Color(241, 196, 15)
        );

        deleteButton.setBackground(
                new Color(231, 76, 60)
        );

        refreshButton.setBackground(
                new Color(52, 152, 219)
        );

        addButton.setForeground(Color.WHITE);

        updateButton.setForeground(Color.WHITE);

        deleteButton.setForeground(Color.WHITE);

        refreshButton.setForeground(Color.WHITE);

        Font btnFont =
                new Font("Segoe UI", Font.BOLD, 13);

        addButton.setFont(btnFont);

        updateButton.setFont(btnFont);

        deleteButton.setFont(btnFont);

        refreshButton.setFont(btnFont);

        buttonPanel.add(addButton);

        buttonPanel.add(updateButton);

        buttonPanel.add(deleteButton);

        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ================= BUTTON ACTIONS =================

        addButton.addActionListener(
                e -> new AddBookForm(this)
        );

        updateButton.addActionListener(
                e -> updateBook()
        );

        deleteButton.addActionListener(
                e -> deleteBook()
        );

        refreshButton.addActionListener(
                e -> loadTable()
        );

        // ================= LOAD TABLE =================

        loadTable();

        // ================= TABLE DESIGN =================

        getContentPane().setBackground(
                new Color(240, 240, 240)
        );

        table.setRowHeight(30);

        table.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        table.getTableHeader().setPreferredSize(
                new Dimension(100, 35)
        );

        table.setShowGrid(false);

        table.setIntercellSpacing(
                new Dimension(0, 0)
        );

        table.setSelectionBackground(
                new Color(52, 152, 219)
        );

        table.setSelectionForeground(Color.WHITE);

        setVisible(true);
    }

    // ================= LOAD TABLE =================

    public void loadTable() {

        model.setRowCount(0);

        try {

            Connection conn = DatabaseConnection.connect();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM books"
            );

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("status")
                });
            }

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ================= DELETE BOOK =================

    public void deleteBook() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    null,
                    "Select a book first"
            );

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        try {

            Connection conn =
                    DatabaseConnection.connect();

            Statement stmt =
                    conn.createStatement();

            stmt.executeUpdate(
                    "DELETE FROM books WHERE id=" + id
            );

            JOptionPane.showMessageDialog(
                    null,
                    "Book Deleted Successfully"
            );

            loadTable();

            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // ================= UPDATE BOOK =================

    public void updateBook() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    null,
                    "Select a book first"
            );

            return;
        }

        int id = (int) model.getValueAt(row, 0);

        String title =
                model.getValueAt(row, 1).toString();

        String author =
                model.getValueAt(row, 2).toString();

        String status =
                model.getValueAt(row, 3).toString();

        new UpdateBookForm(
                this,
                id,
                title,
                author,
                status
        );
    }
}