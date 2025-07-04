package pins_pong_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


class PongGame extends GameMode
{
    static final int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 480;
    private Ball gameBall, futureBall; /**TRON LEGACY TYPE SHIT, nah, in order to make pc paddle smarter
                                        * we can make an invisible ball that foresees normal ball action and have 
                                        * pc paddle responds to that ball instead
                                        */
    private Paddle userPaddle, pcPaddle;
    private int userMouseY, pcMouseY;

    //customizable attribute
    /*
     * little explanation here, getCx() cy and speed should be the same at first
     * getCx() and cy is how much a ball moves in one unit of time, or basically every one frame updated
     * which is about 33 miliseconds (check in main), the "speed" variable is only useful
     * in increaseSpeed() method, aside from that, it has no use, for now
     */
    
    /*private static int getCx()=1, cy=1, ballSpeed=1; //to make it harder, increase all THREE variables
    static int userPaddleSpeed=3;
    static int pcPaddleSpeed=1;
    private final int refreshRate = Main.DELAY; //want to change this? change main's delay
    private final Color pcPaddleColor=Color.RED, userPaddleColor=Color.BLUE, ballColor=Color.YELLOW;
    private boolean pcAccidentalMiss;
    private final int oscillationFrequency = 10;*/

    

    private int userScore, pcScore, bounceCount, intUserLoc2, intPcLoc2;
    private int detectedCollideY;
    private boolean pcGotToTarget;
    private int oscillateTowards;

    private Timer paddleKeyTimer;
    private boolean upKeyPressed=false, downKeyPressed=false;
    private boolean wPressed=false, sPressed=false;
    
    

    public PongGame()
    {
        super();
        
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

        paddleKeyTimer = new Timer(getRefreshRate(), e-> {

            if(!getMultiplayer())
            {
                if(upKeyPressed && userPaddle.getY() > 0)
                {
                    userMouseY-=getUserPaddleSpeed();
                    //System.out.println("key is supposed to be moving 01");
                }
                if(downKeyPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight() - 23)
                {
                    userMouseY+=getUserPaddleSpeed();
                    //System.out.println("key is supposed to be moving 02");
                }
            }
            else
            {
                if (upKeyPressed && pcPaddle.getY() > 0) {
                    pcMouseY -= getPcPaddleSpeed();
                    // System.out.println("key is supposed to be moving 01");
                }
                if (wPressed && userPaddle.getY() > 0) {
                    userMouseY -= getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 03");
                }

                // Handle DOWN or S key press
                if (downKeyPressed && pcPaddle.getY() < WINDOW_HEIGHT - pcPaddle.getHeight() - 23) {
                    pcMouseY += getPcPaddleSpeed();
                    // System.out.println("key is supposed to be moving 02");
                }
                if (sPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight() - 23) {
                    userMouseY += getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 04");
    }
            }

        });
    }
    
    public void intGame() //comes after constructor
    {
        gameBall = new Ball(300, 200 , getCx() ,getCy() , getBallSpeed() , getBallColor(), 10); //SPEED IS 3
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, getIntUserLoc(), getUserPaddleHeight(), getUserPaddleSpeed(), getUserPaddleColor()); //SPEED CAN CHANGE HERE, COLOR AS WELL
        pcPaddle = new Paddle(610, getIntPcLoc(), getPcPaddleHeight(), getPcPaddleSpeed(), getPcPaddleColor());

        userMouseY = getIntUserLoc()+getUserPaddleHeight()/2;
        pcMouseY=getIntPcLoc()+getPcPaddleHeight()/2;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        gameBall.paint(g);
        //futureBall.paint(g); //supposed to be invisible, added paint to make dev sees if mechanic works or not
        
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
        
        if(!getMultiplayer()){
            if(detectedCollideY == -1 && getBetterAi())
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
                        if(pcAccidentalMiss && getAcMissMode())
                        {
                            detectedCollideY+=getSwingState();
                        }

                        break;
                    }
                }
            }

            userPaddle.moveToward(userMouseY);
            //pcPaddle.moveToward(gameBall.getY()); //EASIEST IMPLEMENTATION, PCPADDLE ALWAYS MOVES TOWARD THE BALL
            //IMPLEMENTED ALREADY
            //We can make it harder though
            
            if( Math.abs( ( pcPaddle.getY() + pcPaddle.getHeight()/2 ) - detectedCollideY) < 3 && !pcGotToTarget)
            {
                pcGotToTarget = true;
                System.out.println("pc paddle got to designated target"); //for better ai movement

            }
            
            if(!pcGotToTarget){
                if(getBetterAi()){
                    pcPaddle.moveToward(detectedCollideY); //advance pc detection, for sees where the ball is going, HARDER GAME MODE
                }
                else
                {
                    if(getAcMissMode())
                    {
                        int missVar=0;
                        if(pcAccidentalMiss) 
                        {
                            missVar+=75; //this is actually wrong but it works, not as intended :shrug:
                        }
                        pcPaddle.moveToward(gameBall.getY() + missVar);
                    }
                    else
                    {
                        pcPaddle.moveToward(gameBall.getY());
                    }
                }
                
            }
            else if(getOscillation())
            {
                if(pcPaddle.getCenterY() > detectedCollideY + getOscillationFrequency())
                {
                    oscillateTowards = 0;
                }
                else if(pcPaddle.getCenterY() < detectedCollideY - getOscillationFrequency())
                {
                    oscillateTowards = WINDOW_HEIGHT;
                }

                pcPaddle.moveToward(oscillateTowards);
            }
            else
            {

            }

            if(userPaddle.checkCollision(gameBall))
            {
                gameBall.reverseX();
                gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
                if(getDynamicBallSpeed()){
                    bounceCount++;
                }
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

                if(getCryBabyChance())
                {
                    pcAccidentalMiss=true;
                    System.out.println("pc should miss next bounce");
                }

                if((int)(Math.random() * getPercentChance()) == 0 && getAcMissMode())
                {
                    pcAccidentalMiss = true; //PC ACCIDENTAL MISS
                    System.out.println("pc should miss next bounce");
                }
                
            }
            if(getDynamicBallSpeed())
            {
                if(bounceCount == 5) //DYNAMIC BALL SPEED CHANGE HERE
                {
                    bounceCount=0;
                    gameBall.increaseSpeed();
                }
            }
        }
        else
        {
            pcPaddle.moveToward(pcMouseY);
            userPaddle.moveToward(userMouseY);

            if(userPaddle.checkCollision(gameBall))
            {
                gameBall.reverseX();
                gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
                bounceCount++;
                
            }
            
            if(pcPaddle.checkCollision(gameBall))
            {
                gameBall.reverseX();
                gameBall.setX(pcPaddle.getX() - 10); // make it so that some part of the ball still stuck inside the paddle
                                                    //make it more.. collision realistic?
                                                //TO THIS
                bounceCount ++;
            }
            if(getDynamicBallSpeed())
            {
                if(bounceCount == 5) //DYNAMIC BALL SPEED CHANGE HERE
                {
                    bounceCount=0;
                    gameBall.increaseSpeed();
                }
            }
        }

        outXBound(gameBall); //belong to this class, if ball gets out of bound, then someone loses


    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(!getMultiplayer() && !getDisableMouse()){
            userMouseY = e.getY();
        }
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

        gameBall = new Ball(300, 200 , getCx() ,getCy() , getBallSpeed() , getBallColor(), 10); //SPEED IS 3
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, getIntUserLoc(), getUserPaddleHeight(), getUserPaddleSpeed(), getUserPaddleColor()); //SPEED CAN CHANGE HERE, COLOR AS WELL
        pcPaddle = new Paddle(610, getIntPcLoc(), getPcPaddleHeight(), getPcPaddleSpeed(), getPcPaddleColor());

        //reset futureBall
        bounceCount =0;
        detectedCollideY = -1;
        pcGotToTarget = false; //might be useful in future
        pcAccidentalMiss=false;
        userMouseY=getIntUserLoc()+getUserPaddleHeight()/2;
        pcMouseY=getIntPcLoc()+getPcPaddleHeight()/2;
        upKeyPressed = false;
        downKeyPressed = false;
        wPressed = false;
        sPressed = false;
        paddleKeyTimer.stop();
    
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

        if(!getMultiplayer())
        {
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
        else
        {
            if (keycode == KeyEvent.VK_UP) {
                if (!upKeyPressed) {
                    upKeyPressed = true;
                    //System.out.println("key is supposed to be moving 03");
                    if (!paddleKeyTimer.isRunning()) {
                        paddleKeyTimer.start(); // Start the timer only once
                        
                    }
                }
            }
            if (keycode == KeyEvent.VK_W) {
                if (!wPressed) {
                    wPressed = true;
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

            if (keycode == KeyEvent.VK_S) {
                if (!sPressed) {
                    sPressed = true;
                    //System.out.println("key is supposed to be moving 03");
                    if (!paddleKeyTimer.isRunning()) {
                        paddleKeyTimer.start(); // Start the timer only once
                        
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
         int keyCode = e.getKeyCode();
        
        if(!getMultiplayer())
        {
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
        else
        {
            if (keyCode == KeyEvent.VK_UP) {
                upKeyPressed = false;
            }

            if (keyCode == KeyEvent.VK_W) {
                wPressed = false;
            }

            if (keyCode == KeyEvent.VK_DOWN) {
                downKeyPressed = false;
            }

            if (keyCode == KeyEvent.VK_S) {
                sPressed = false;
            }

            if (!upKeyPressed && !downKeyPressed && !wPressed && !sPressed) {
                paddleKeyTimer.stop();
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Optionally handle key typed events if needed
    }

    


}