package project3;
import project3.gameMech.*;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class gameRender extends JFrame{
    public static final int DELAY = 30;

    public static JPanel renderPlayable(int volumeLevel, int difficultyLevel, int winPoint, MainApplication mainFrame) {
        JPanel playArea = new JPanel(new BorderLayout());
        playArea.setSize(800, 600);
        mainFrame.setTitle("A random ball bouncing game");
        playArea.setFocusable(true);
        
        // For dubug and test, feel free to comment it
        // String message = "Input level: " + difficultyLevel + 
        //                  "\nVolume Level: " + volumeLevel;
        // JTextArea textArea = new JTextArea(message);
        // textArea.setEditable(false);
        // textArea.setBounds(250, 350, 300, 100); // Set position and size
        // playArea.add(textArea);

        PongGame game = new PongGame(difficultyLevel, winPoint);
        playArea.add(game);

        playArea.setVisible(true);
        // playArea.setSize(650, 495);      // 
        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.gameLogic();
                game.repaint();
            }
        });
        timer.start();

        return playArea;
    }
}
