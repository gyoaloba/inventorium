package dev.gracco.inventorium.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Utilities {
    public static void sendError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void sendError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void sendSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void sendSuccess(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void sendFatalError(Exception e) {
        JOptionPane.showMessageDialog(null, "Encountered a fatal error! Check console for more. Closing program. \n(" + e.getClass().getName() + ")", "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println(e.getMessage() + e.getCause());
        System.exit(1);
    }

    public static void populateTable(JTable table, Object[][] data, String[] columnNames) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.setModel(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public static String splitIntoLines(String text, int minChar) {
        StringBuilder lineBuilder = new StringBuilder();
        StringBuilder finalText = new StringBuilder();
        String[] words = text.split(" ");
        int currentIteration = 0;

        for (String word : words) {
            currentIteration++;
            lineBuilder.append(word).append(" ");

            if (lineBuilder.length() > minChar - 1 || currentIteration == words.length) {
                finalText.append(lineBuilder.toString().trim()).append("\n");
                lineBuilder.setLength(0);
            }
        }

        return finalText.toString();
    }



}
