package project3;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import project3.gameMech.PongGame;
import project3.gameMech.SoundPlayer;

public class gameRender extends JFrame {

    public static final int DELAY = 30;
    private static PongGame game;
    private static Timer gameTimer;

    public static JPanel renderPlayable(int volumeLevel, int difficultyLevel, int winPoint, MainApplication mainFrame, int modeSelected) {
        // Stop any existing game first
        stopCurrentGame();

        JPanel playArea = new JPanel(new BorderLayout());
        playArea.setSize(800, 600);
        mainFrame.setTitle("A random ball bouncing game");
        playArea.setFocusable(true);

        //set volume before game start
        SoundPlayer.setVolume(volumeLevel);
        game = new PongGame(difficultyLevel, winPoint, modeSelected, mainFrame);
        playArea.add(game);

        gameTimer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                game.gameLogic();
                game.repaint();

            }
        });

        // playArea.addNotify();
        // playArea.requestFocusInWindow();

        // Press ESC to exit
        SwingUtilities.invokeLater(() -> {
            playArea.requestFocusInWindow();
            playArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        gameTimer.stop();
                        int result = JOptionPane.showConfirmDialog(playArea, "Do you want to return to main menu?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            stopCurrentGame();

                            Container contentPane = mainFrame.getContentPane();
                            contentPane.removeAll();
                            contentPane.add(mainFrame.getMainMenu());
                            contentPane.revalidate();
                            contentPane.repaint();
                        } else {
                            gameTimer.restart();
                        }
                    }
                }
            });
        });

        
        // For dubug and test, feel free to comment it
        // String message = "Input level: " + difficultyLevel + 
        //                  "\nVolume Level: " + volumeLevel;
        // JTextArea textArea = new JTextArea(message);
        // textArea.setEditable(false);
        // textArea.setBounds(250, 350, 300, 100); // Set position and size
        // playArea.add(textArea);
        gameTimer.start();
        return playArea;
    }

    // Static method to stop current game
    public static void stopCurrentGame() {
        // Stop the main game timer
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
            gameTimer = null;
        }

        // Stop the game instance
        if (game != null) {
            game.stop(); // Call the stop method
            game = null;
        }
        SoundPlayer.stop();
    }
}
