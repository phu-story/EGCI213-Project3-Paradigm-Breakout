package project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class gameRender extends JFrame{

    public static JPanel renderPlayable(int volumeLevel, int difficultyLevel, MainApplication mainFrame) {
        int curLevel = 0;
        JPanel playArea = new JPanel(new BorderLayout());
        playArea.setSize(800, 600);
        mainFrame.setTitle("Level: " + curLevel);
        playArea.setFocusable(true);

        // Press ESC to exit
        playArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int result = JOptionPane.showConfirmDialog(playArea, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        playArea.removeAll();
                        mainFrame.getMainMenu();
                        playArea.revalidate();
                        playArea.repaint();
                        playArea.removeKeyListener(this);
                    }
                }
            }
        });
        
        // For dubug and test, feel free to comment it
        String message = "Input level: " + difficultyLevel + 
                         "\nVolume Level: " + volumeLevel;
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setBounds(250, 350, 300, 100); // Set position and size
        playArea.add(textArea);

        playArea.add(textArea);

        return playArea;
    }
}
