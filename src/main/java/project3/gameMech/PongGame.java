package project3.gameMech;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import project3.gameRender;


public class PongGame extends JPanel implements MouseMotionListener, KeyListener
{
    // Designed dimension   640 x 480
    // Desired dimension    800 x 600
    static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    private Ball gameBall, futureBall; /**TRON LEGACY TYPE SHIT, nah, in order to make pc paddle smarter
                                        * we can make an invisible ball that foresees normal ball action and have 
                                        * pc paddle responds to that ball instead
                                        */
    private Paddle userPaddle, pcPaddle;
    private int userMouseY;

    //customizable attribute
    /*
     * little explanation here, cx cy and speed should be the same at first
     * cx and cy is how much a ball moves in one unit of time, or basically every one frame updated
     * which is about 33 miliseconds (check in main), the "speed" variable is only useful
     * in increaseSpeed() method, aside from that, it has no use, for now
     */
    
    private int cx = 4, cy = 4, ballSpeed = 4; //to make it harder, increase all THREE variables
    private int userPaddleSpeed=3;
    private int pcPaddleSpeed=3;
    private final int refreshRate = gameRender.DELAY; //want to change this? change main's delay
    private final Color pcPaddleColor=Color.RED, userPaddleColor=Color.BLUE, ballColor=Color.YELLOW;
    private boolean pcAccidentalMiss;

    private final int userPaddleHeight=80;
    private final int pcPaddleHeight=80;

    private int userScore, pcScore, bounceCount;
    private int detectedCollideY;
    private boolean pcGotToTarget;
    private int oscillateTowards;

    private Timer paddleKeyTimer;
    private boolean upKeyPressed=false, downKeyPressed=false;

    static final String PATH = System.getProperty("user.dir") + "/src/main/java/project3/resources/";
    
    

    public PongGame(int difficultyLevel, int winPoint)
    {
        //add sound background
        SoundPlayer.playBackgroundSound(PATH + "backgroundsound.wav");
        // Adjust difficulty level by user's config
        if (difficultyLevel > 1) {
            System.out.println(difficultyLevel);
            cx = 4 + difficultyLevel;
            cy = 4 + difficultyLevel;
            ballSpeed = 4 + difficultyLevel;
            userPaddleSpeed = 3 + difficultyLevel;
            pcPaddleSpeed = 3 + difficultyLevel;
        }

        gameBall = new Ball(300, 200 , cx ,cy , ballSpeed , ballColor, 10); //SPEED IS 3
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, WINDOW_HEIGHT/2, userPaddleHeight, userPaddleSpeed, userPaddleColor); //SPEED CAN CHANGE HERE, COLOR AS WELL
        pcPaddle   = new Paddle(WINDOW_WIDTH - 40, WINDOW_HEIGHT/2, 
                                pcPaddleHeight, 
                                pcPaddleSpeed, 
                                pcPaddleColor
                                );

        userMouseY = 0;
        
        userScore =0; pcScore =0;
        bounceCount=0;

        detectedCollideY=-1;
        pcGotToTarget=false;
        oscillateTowards=0;
        pcAccidentalMiss=false;



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
    
    // To-do: Further Ui work
    @Override
    public void paintComponent(Graphics g)
    {
        
        super.paintComponent(g);
        // set background
        Image background = new ImageIcon(PATH + "background.png").getImage();
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
       
        
        // game objects
        gameBall.paint(g);
        userPaddle.paint(g);
        pcPaddle.paint(g);
        // label
        String user = String.valueOf(userScore); // score
        String pc = String.valueOf(pcScore);
        int boxSize = 100+80+(20*user.toCharArray().length) + 10 + 70 + (20*pc.toCharArray().length); //calc header width
        int x = (800-boxSize)/2, y = 20;

        Image scoreLabel = new ImageIcon(PATH + "score.png").getImage();
        g.drawImage(scoreLabel, x, y, 100, 60, this); 
        x+=100; 

        Image userLabel = new ImageIcon(PATH + "user.png").getImage();
        g.drawImage(userLabel, x, y, 80, 60, this); 
        x+=80; y+=5;

        for (char num : user.toCharArray()) {
            Image numImage = new ImageIcon(PATH + num + ".png").getImage();
            g.drawImage(numImage, x, y, 40, 40, this); 
            x+=20;
        } 
        x+=10; y-=5;

        Image pcLabel = new ImageIcon(PATH + "PC.png").getImage();
        g.drawImage(pcLabel, x, y, 80, 60, this); 
        x+=70; y+=5;

        // score
        for (char num : pc.toCharArray()) {
            Image numImage = new ImageIcon(PATH + num + ".png").getImage();
            g.drawImage(numImage, x, y, 40, 40, this);
            x+=20;
        }  
    }

    public void gameLogic()
    {
        gameBall.moveBall();
        gameBall.bounceOffEdge(0, WINDOW_HEIGHT);

        if(detectedCollideY == -1)
        {
            for(int i=0; i<10; i++)
            {
                futureBall.moveBall();
                //System.out.println("future moves ball"); //for debugging purpose
                futureBall.bounceOffEdge(0, WINDOW_HEIGHT);

                if(futureBall.getX() < userPaddle.getX() + Paddle.PADDLE_WIDTH)
                {
                    futureBall.reverseX();
                }

                if(futureBall.getX() > pcPaddle.getX())
                {
                    detectedCollideY = futureBall.getY();
                    //check whether it actually works or not
                    System.out.println("Future collision at : " + detectedCollideY);
                    if(pcAccidentalMiss)
                    {
                        detectedCollideY+=75;
                    }

                    break;
                }
            }
        }

        userPaddle.moveToward(userMouseY);
        //pcPaddle.moveToward(gameBall.getY()); //EASIEST IMPLEMENTATION, PCPADDLE ALWAYS MOVES TOWARD THE BALL
        //We can make it harder though
        
        if( Math.abs( ( pcPaddle.getY() + pcPaddle.getHeight()/2 ) - detectedCollideY) < 3 && !pcGotToTarget)
        {
            pcGotToTarget = true;
            System.out.println("pc paddle got to designated target"); //for better ai movement

        }
        
        if(!pcGotToTarget){
            pcPaddle.moveToward(detectedCollideY); //advance pc detection, for sees where the ball is going, HARDER GAME MODE
        }
        else
        {
            if(pcPaddle.getCenterY() > detectedCollideY + 10)
            {
                oscillateTowards = 0;
            }
            else if(pcPaddle.getCenterY() < detectedCollideY - 10)
            {
                oscillateTowards = WINDOW_HEIGHT;
            }

            pcPaddle.moveToward(oscillateTowards);
        }

        if(userPaddle.checkCollision(gameBall))
        {
            gameBall.reverseX();
            gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
            bounceCount++;

            SoundPlayer.playsound(PATH + "hitsound.wav");
        }
        
        if(pcPaddle.checkCollision(gameBall))
        {
            gameBall.reverseX();
            gameBall.setX(pcPaddle.getX() - 10); // make it so that some part of the ball still stuck inside the paddle
                                                   //make it more.. collision realistic?
            futureBall = new Ball(gameBall);                         //REMOVE TS PART FOR SIMPLER AI
            //reset the detected collision point
            detectedCollideY = -1; 
            pcGotToTarget = false;                                  //TO THIS
            bounceCount ++;

            SoundPlayer.playsound(PATH + "hitsound.wav");

            if((int)(Math.random() * 3) == 0)
            {
                pcAccidentalMiss = true; //PC ACCIDENTAL MISS
                System.out.println("pc should miss next bounce");
            }
        }

        if(bounceCount == 5) //DYNAMIC BALL SPEED CHANGE HERE
        {
            bounceCount=0;
            gameBall.increaseSpeed();
        }

        outXBound(gameBall); //belong to this class, if ball gets out of bound, then someone loses


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

        gameBall = new Ball(300, 200 , cx ,cy , ballSpeed , ballColor, 10); //SPEED IS 3
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, 200, userPaddleHeight, userPaddleSpeed, userPaddleColor); //SPEED CAN CHANGE HERE, COLOR AS WELL
        pcPaddle = new Paddle(WINDOW_WIDTH - 40, WINDOW_HEIGHT/2, 
                                pcPaddleHeight, 
                                pcPaddleSpeed, 
                                pcPaddleColor
                                );

        //reset futureBall
        bounceCount =0;
        detectedCollideY = -1;
        pcGotToTarget = false; //might be useful in future
        pcAccidentalMiss=false;
    
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

        if (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W) {
            if (!upKeyPressed) {
                upKeyPressed = true;
                //System.out.println("key is supposed to be moving 03");
                if (!paddleKeyTimer.isRunning()) {
                    paddleKeyTimer.start(); // Start the timer only once
                    
                }
            }
        }

        if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
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

        if (keyCode == KeyEvent.VK_UP  || keyCode == KeyEvent.VK_W ) {
            upKeyPressed = false;  // Stop printing when UP key is released
            //System.out.println("key is supposed to be moving 05");
            //classical debug technique
        }

        if (keyCode == KeyEvent.VK_DOWN  || keyCode == KeyEvent.VK_S) {
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