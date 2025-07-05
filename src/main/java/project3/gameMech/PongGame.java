package project3.gameMech;

import javax.swing.*;

// import project3.gameRender;

import java.awt.*;
import java.awt.event.*;

public class PongGame extends GameMode {
    // Designed dimension 640 x 480
    // Desired dimension 800 x 600
    static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    private Ball gameBall, futureBall;
    /**
     * TRON LEGACY TYPE SHIT, nah, in order to make pc paddle smarter
     * we can make an invisible ball that foresees normal ball action and have
     * pc paddle responds to that ball instead
     */
    private Paddle userPaddle, pcPaddle;
    private int userMouseY, pcMouseY;
    private int winpoint;

    // customizable attribute
    /*
     * little explanation here, cx cy and speed should be the same at first
     * cx and cy is how much a ball moves in one unit of time, or basically every
     * one frame updated
     * which is about 33 miliseconds (check in main), the "speed" variable is only
     * useful
     * in increaseSpeed() method, aside from that, it has no use, for now
     */

    GameMode globalConfig = new GameMode();
    
    // private int cx = 4, cy = 4, ballSpeed = 4; // to make it harder, increase all THREE variables
    // private int userPaddleSpeed = 3;
    // private int pcPaddleSpeed = 3;
    // private final int refreshRate = gameRender.DELAY; // want to change this? change main's delay
    // private final Color pcPaddleColor = Color.RED, userPaddleColor = Color.BLUE, ballColor = Color.YELLOW;
    // private boolean pcAccidentalMiss;

    // private final int userPaddleHeight = 80;
    // private final int pcPaddleHeight = 80;

    private int userScore, pcScore, bounceCount, intUserLoc2, intPcLoc2;
    private int detectedCollideY;
    private boolean pcGotToTarget;
    private int oscillateTowards;

    private Timer paddleKeyTimer;
    private boolean upKeyPressed=false, downKeyPressed=false;
    private boolean wPressed=false, sPressed=false;
    
    // I don't know but remove this, java won't listen key
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    public PongGame(int difficultyLevel, int winPoint) {
        this.winpoint = winPoint;

        userScore =0; pcScore =0;
        bounceCount=0;

        detectedCollideY=-1;
        pcGotToTarget=false;
        oscillateTowards=0;
        pcAccidentalMiss=false;
        
        requestFocusInWindow();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addMouseMotionListener(this);
        addKeyListener(this);

        intGame();

        paddleKeyTimer = new Timer(getRefreshRate(), e -> {
            // if (upKeyPressed && userPaddle.getY() > 0) {
            //         userMouseY -= globalConfig.getUserPaddleSpeed();
            //         // System.out.println("key is supposed to be moving 01");
            //     }
            //     if (downKeyPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight()) {
            //         userMouseY += globalConfig.getUserPaddleSpeed();
            //         // System.out.println("key is supposed to be moving 02");
            //     }

            // paddleKeyTimer = new Timer(globalConfig.getRefreshRate(), e -> {
            //     
            // });

            if (!getMultiplayer()) {
                if (upKeyPressed && userPaddle.getY() > 0) {
                    userMouseY -= globalConfig.getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 01");
                }
                if (downKeyPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight() - 23) {
                    userMouseY += globalConfig.getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 02");
                }
            } else {
                // Left Paddle
                if (upKeyPressed && pcPaddle.getY() > 0) {
                    pcMouseY -= globalConfig.getPcPaddleSpeed();
                    // System.out.println("key is supposed to be moving 01");
                }
                // Right Paddle
                if (wPressed && userPaddle.getY() > 0) {
                    userMouseY -= globalConfig.getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 03");
                }

                // Left Paddle
                if (downKeyPressed && pcPaddle.getY() < WINDOW_HEIGHT - pcPaddle.getHeight() - 23) {
                    pcMouseY += globalConfig.getPcPaddleSpeed();
                    // System.out.println("key is supposed to be moving 02");
                }
                // Right Paddle
                if (sPressed && userPaddle.getY() < WINDOW_HEIGHT - userPaddle.getHeight() - 23) {
                    userMouseY += globalConfig.getUserPaddleSpeed();
                    // System.out.println("key is supposed to be moving 04");
                }
            }

        });
    }

    public void intGame() { // comes after constructor
        gameBall = new Ball(300, 200, globalConfig.getCx(), globalConfig.getCy(), globalConfig.getBallSpeed(),
                globalConfig.getBallColor(), 10); // SPEED IS 3
        futureBall = new Ball(gameBall);
        userPaddle = new Paddle(10, WINDOW_HEIGHT / 2,
                globalConfig.getUserPaddleHeight(),
                globalConfig.getUserPaddleSpeed(),
                globalConfig.getUserPaddleColor());

        pcPaddle = new Paddle(WINDOW_WIDTH - 40, WINDOW_HEIGHT / 2, // x,y cord of starting position
                globalConfig.getPcPaddleHeight(), // Paddle's height
                globalConfig.getPcPaddleSpeed(), // Moveable unit per frame
                globalConfig.getPcPaddleColor() // paddle color
        );
    }
    // To-do: Further Ui work
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        gameBall.paint(g);
        // futureBall.paint(g); //supposed to be invisible, added paint to make dev sees
        // if mechanic works or not

        userPaddle.paint(g);
        pcPaddle.paint(g);

        g.setColor(Color.WHITE); // SCORE BOARD CAN ALSO BE CUSTOMIZED HERE
        String scoreBoard = "Score - user [ " + userScore + " ] PC [ " + pcScore + " ]";
        g.drawString(scoreBoard, 300, 20);

    }

    public void gameLogic() {
        gameBall.moveBall();
        gameBall.bounceOffEdge(0, WINDOW_HEIGHT);

        if (!globalConfig.getMultiplayer()) {
            if (detectedCollideY == -1 && globalConfig.getBetterAi()) {
                for (int i = 0; i < 10; i++) {
                    futureBall.moveBall();
                    // System.out.println("future moves ball"); //for debugging purpose
                    futureBall.bounceOffEdge(0, WINDOW_HEIGHT);

                    if (futureBall.getX() < userPaddle.getX() + Paddle.PADDLE_WIDTH) {
                        futureBall.reverseX();
                    }

                    if (futureBall.getX() > pcPaddle.getX()) {
                        detectedCollideY = futureBall.getY();
                        // check whether it actually works or not
                        System.out.println("Future collision at : " + detectedCollideY);
                        if (pcAccidentalMiss && globalConfig.getAcMissMode()) {
                            detectedCollideY += globalConfig.getSwingState();
                        }

                        break;
                    }
                }
            }

            userPaddle.moveToward(userMouseY);
            // pcPaddle.moveToward(gameBall.getY()); //EASIEST IMPLEMENTATION, PCPADDLE
            // ALWAYS MOVES TOWARD THE BALL
            // IMPLEMENTED ALREADY
            // We can make it harder though

            if (Math.abs((pcPaddle.getY() + pcPaddle.getHeight() / 2) - detectedCollideY) < 3 && !pcGotToTarget) {
                pcGotToTarget = true;
                System.out.println("pc paddle got to designated target"); // for better ai movement

            }

            if (!pcGotToTarget) {
                if (globalConfig.getBetterAi()) {
                    pcPaddle.moveToward(detectedCollideY); // advance pc detection, for sees where the ball is going,
                                                           // HARDER GAME MODE
                } else {
                    if (globalConfig.getAcMissMode()) {
                        int missVar = 0;
                        if (pcAccidentalMiss) {
                            missVar += 75; // this is actually wrong but it works, not as intended :shrug:
                        }
                        pcPaddle.moveToward(gameBall.getY() + missVar);
                    } else {
                        pcPaddle.moveToward(gameBall.getY());
                    }
                }

            } else if (globalConfig.getOscillation()) {
                if (pcPaddle.getCenterY() > detectedCollideY + globalConfig.getOscillationFrequency()) {
                    oscillateTowards = 0;
                } else if (pcPaddle.getCenterY() < detectedCollideY - globalConfig.getOscillationFrequency()) {
                    oscillateTowards = WINDOW_HEIGHT;
                }

                pcPaddle.moveToward(oscillateTowards);
            }
            // else { }

            if (userPaddle.checkCollision(gameBall)) {
                gameBall.reverseX();
                gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
                if (globalConfig.getDynamicBallSpeed()) {
                    bounceCount++;
                }
            }

            if (pcPaddle.checkCollision(gameBall)) {
                gameBall.reverseX();
                gameBall.setX(pcPaddle.getX() - 10); // make it so that some part of the ball still stuck inside the
                                                     // paddle
                                                     // make it more.. collision realistic?
                futureBall = new Ball(gameBall); // REMOVE TS PART FOR SIMPLER AI
                // reset the detected collision point
                detectedCollideY = -1;
                pcGotToTarget = false; // TO THIS
                bounceCount++;

                if (globalConfig.getCryBabyChance()) {
                    pcAccidentalMiss = true;
                    System.out.println("pc should miss next bounce");
                }

                if ((int) (Math.random() * globalConfig.getPercentChance()) == 0 && globalConfig.getAcMissMode()) {
                    pcAccidentalMiss = true; // PC ACCIDENTAL MISS
                    System.out.println("pc should miss next bounce");
                }

            }
            if (globalConfig.getDynamicBallSpeed()) {
                if (bounceCount == 5) // DYNAMIC BALL SPEED CHANGE HERE
                {
                    bounceCount = 0;
                    gameBall.increaseSpeed();
                }
            }
        } else {                                    // Multiplayer
            pcPaddle.moveToward(pcMouseY);
            userPaddle.moveToward(userMouseY);

            if (userPaddle.checkCollision(gameBall)) {
                gameBall.reverseX();
                gameBall.setX(userPaddle.getX() + Paddle.PADDLE_WIDTH + 1);
                bounceCount++;

            }

            if (pcPaddle.checkCollision(gameBall)) {
                gameBall.reverseX();
                gameBall.setX(pcPaddle.getX() - 10); // make it so that some part of the ball still stuck inside the
                                                     // paddle
                                                     // make it more.. collision realistic?
                // TO THIS
                bounceCount++;
            }
            if (globalConfig.getDynamicBallSpeed()) {
                if (bounceCount == 5) // DYNAMIC BALL SPEED CHANGE HERE
                {
                    bounceCount = 0;
                    gameBall.increaseSpeed();
                }
            }
        }

        outXBound(gameBall); // belong to this class, if ball gets out of bound, then someone loses

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // userMouseY = e.getY();

        // To fix
        if(!globalConfig.getMultiplayer()){
            userMouseY = e.getY();
        }
    }

    public void reset() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // gameBall = new Ball(300, 200, 
        //         globalConfig.getCx(), 
        //         globalConfig.getCy(), 
        //         globalConfig.getBallSpeed(), 
        //         globalConfig.getBallColor(), 10
        // );

        // futureBall = new Ball(gameBall);
        // userPaddle = new Paddle(10, 200, 
        //         globalConfig.getUserPaddleHeight(), 
        //         globalConfig.getUserPaddleSpeed(), 
        //         globalConfig.getUserPaddleColor()
        // );

        // pcPaddle = new Paddle(WINDOW_WIDTH - 40, WINDOW_HEIGHT / 2,
        //         globalConfig.getPcPaddleHeight(),
        //         globalConfig.getPcPaddleSpeed(),
        //         globalConfig.getPcPaddleColor());

        intGame();

        // reset futureBall
        bounceCount = 0;
        detectedCollideY = -1;
        pcGotToTarget = false; // might be useful in future
        pcAccidentalMiss = false;

        userMouseY = globalConfig.getIntUserLoc() + globalConfig.getUserPaddleHeight()/2;
        pcMouseY = globalConfig.getIntPcLoc() + globalConfig.getPcPaddleHeight()/2;
        upKeyPressed = false;
        downKeyPressed = false;
        wPressed = false;
        sPressed = false;
        paddleKeyTimer.stop();
    }

    // Check if ball hit wall Score counting
    public void outXBound(Ball gameBall) {
        if (gameBall.getX() < 0) // condition checking whether lose or not
        {
            pcScore++;
            if (winpoint > 0 && pcScore >= winpoint) {
                System.exit(0);
            }
            reset();
        } else if (gameBall.getX() > WINDOW_WIDTH) {
            userScore++;
            if (winpoint > 0 && userScore >= winpoint) {
                System.exit(0);
            }
            reset();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        if(!globalConfig.getMultiplayer())
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
        
        if(!globalConfig.getMultiplayer())
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