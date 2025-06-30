package project3;

import javax.swing.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class gameRender extends JFrame{

    public static JPanel renderPlayable(int volumeLevel, int difficultyLevel, MainApplication mainFrame) {
        int curLevel = 0;
        JPanel playArea = new JPanel();
        mainFrame.setTitle("Level: " + curLevel);
        playArea.setLayout(null);

        playArea.setFocusable(true);
        playArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int result = JOptionPane.showConfirmDialog(playArea, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        playArea.removeAll();
                        mainFrame.getMainMenu();
                        playArea.revalidate();
                        playArea.repaint();
                    }
                }
            }
        });

        return playArea;
    }
}
