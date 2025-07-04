package project3.gameMech;

import javax.swing.*;
// import java.awt.*;
import java.awt.event.*;


class Main extends JFrame
{
    static JFrame f = new JFrame("Pong");
    public static final int DELAY = 33; //this is the frequency of how much a frame would be updated, 
                                         //not just FPS but also the speed of a game

    public static void main(String[] args)
    {
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        f.setSize(650, 495);
        PongGame game = new PongGame();
        f.add(game);
        
       
        f.setVisible(true);
        
        //timer works as a frame updater, everytime it runs, in this case, every 33 milisecs
        //game will call repaint
        Timer timer = new Timer(DELAY, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        game.gameLogic();
                        game.repaint();
                    }
                });

           
           timer.start();

    }
}


