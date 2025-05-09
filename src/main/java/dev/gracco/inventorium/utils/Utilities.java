package dev.gracco.inventorium.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utilities {
    public static void sendError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void sendError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void sendFatalError(Exception e) {
        JOptionPane.showMessageDialog(null, "Encountered a fatal error! Check console for more. Closing program. \n(" + e.getClass().getName() + ")", "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println(e.getMessage() + e.getCause());
        System.exit(1);
    }
}
