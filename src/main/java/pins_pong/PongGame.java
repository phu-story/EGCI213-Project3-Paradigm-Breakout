package pins_pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class PongGame extends JPanel implements MouseMotionListener, KeyListener
{
    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    private Ball gameBall;
    private Paddle userPaddle, pcPaddle;
    private int userMouseY;

    //customizable attribute
    /*
     * little explanation here, cx cy and speed should be the same at first
     * cx and cy is how much a ball moves in one unit of time, or basically every one frame updated
     * which is about 33 miliseconds (check in main), the "speed" variable is only useful
     * in increaseSpeed() method, aside from that, it has no use, for now
     */
    
    private final int cx=3, cy=3, ballSpeed=3; //to make it harder, increase all THREE variables
    private final int userPaddleSpeed=3;
    private final int pcPaddleSpeed=3;
    private final int refreshRate = Main.DELAY; //want to change this? change main's delay

    private int userScore, pcScore, bounceCount;

    private Timer paddleKeyTimer;
    private boolean upKeyPressed=false, downKeyPressed=false;
    
    

    public PongGame()
    {
        gameBall = new Ball(300, 200 , cx ,cy , ballSpeed , Color.YELLOW, 10); //SPEED IS 3
        userPaddle = new Paddle(10, 200, 75, userPaddleSpeed, Color.BLUE); //SPEED CAN CHANGE HERE, COLOR AS WELL
        pcPaddle = new Paddle(610, 200, 75, pcPaddleSpeed, Color.RED);

        userMouseY = 0;
        
        userScore =0; pcScore =0;
        bounceCount=0;

        setFocusable(true);
        requestFocusInWindow();

        addMouseMotionListener(this);
        addKeyListener(this);

        paddleKeyTimer = new Timer(refreshRate, e-> {
            if(upKeyPressed && userPaddle.getY() > 0)
            {
                userMouseY-=userPaddleSpeed;
                //System.out.println("key is supposed to be moving 01");
            }
            if(downKeyPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight() )
            {
                userMouseY+=userPaddleSpeed;
                //System.out.println("key is supposed to be moving 02");
            }
        });
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        gameBall.paint(g);
        
        userPaddle.paint(g);
        pcPaddle.paint(g);

        g.setColor(Color.WHITE); //SCORE BOARD CAN ALSO BE CUSTOMIZED HERE
        String scoreBoard = "Score - user [ " + userScore + " ] PC [ " + pcScore + " ]"; 
        g.drawString(scoreBoard, 250, 20);
             
    }

    public void gameLogic()
    {
        gameBall.moveBall();
        gameBall.bounceOffEdge(0, WINDOW_HEIGHT);
        userPaddle.moveToward(userMouseY);
        pcPaddle.moveToward(gameBall.getY()); //EASIEST IMPLEMENTATION, PCPADDLE ALWAYS MOVES TOWARD THE BALL
                                              //We can make it harder though
        if(pcPaddle.checkCollision(gameBall) || userPaddle.checkCollision(gameBall))
        {
            gameBall.reverseX();
            bounceCount++;
        }

        if(bounceCount == 5)
        {
            bounceCount=0;
            gameBall.increaseSpeed();
        }

        outXBound(gameBall);


    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        userMouseY = e.getY();

    }

    public void reset()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {   
            e.printStackTrace();
        }

        gameBall.setX(300);
        gameBall.setY(200);
        gameBall.setCx(3);
        gameBall.setCy(3);
        gameBall.setSpeed(3);
        bounceCount = 0;
    }

    public void outXBound(Ball gameBall)
    {
        if(gameBall.getX() < 0) //condition checking whether lose or not
        {
            pcScore++;
            reset();
        }
        else if(gameBall.getX() > WINDOW_WIDTH)
        {
            userScore++;
            reset();
        }
    }

     @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        if (keycode == KeyEvent.VK_UP) {
            if (!upKeyPressed) {
                upKeyPressed = true;
                //System.out.println("key is supposed to be moving 03");
                if (!paddleKeyTimer.isRunning()) {
                    paddleKeyTimer.start(); // Start the timer only once
                    
                }
            }
        }

        if (keycode == KeyEvent.VK_DOWN) {
            if (!downKeyPressed) {
                downKeyPressed = true;
                //System.out.println("key is supposed to be moving 04");
                if (!paddleKeyTimer.isRunning()) {
                    paddleKeyTimer.start(); // Start the timer only once
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            upKeyPressed = false;  // Stop printing when UP key is released
            //System.out.println("key is supposed to be moving 05");
            //classical debug technique
        }

        if (keyCode == KeyEvent.VK_DOWN) {
            downKeyPressed = false;  // Stop printing when DOWN key is released
            //System.out.println("key is supposed to be moving 06");
        }

        // Stop the timer when no keys are pressed
        if (!upKeyPressed && !downKeyPressed) {
            paddleKeyTimer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Optionally handle key typed events if needed
    }

    


}