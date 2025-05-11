package dev.gracco.inventorium.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.util.Map;

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

    public static <K, V> void populateTable(JTable table, Map<K, V> map, String[] columnNames) {
        Object[][] data = new Object[map.size()][2];

        int i = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

}
