package pins_pong_2;

import java.awt.*;

class Ball
{
    private int x, y, cx, cy, speed, size;
    private final Color color;
    static final int MAX_SPEED = 7;
    
    public Ball(int x, int y, int cx, int cy, int speed, Color color, int size) {
        this.x = x;
        this.y = y;
        this.cx = cx;
        this.cy = cy;
        this.speed = speed;
        this.color = color;
        this.size = size;
    }

    public Ball(Ball b) {
        this.x = b.x;
        this.y = b.y;
        this.cx = b.cx;
        this.cy = b.cy;
        this.speed = b.speed;
        this.color = b.color;
        this.size = b.size;
    }
    
    public void paint(Graphics g)
    {
        g.setColor(color);
        
        g.fillOval(x, y, size, size);
    }

    public void moveBall()
    {
        x += cx;
        y += cy;
    }
    
    public void bounceOffEdge(int top, int bottom)
    {
        /**
        * @param top - the y value of the top of the screen
        * @param bottom - the y value of the bottom of the screen
        */

        if(y > bottom-size - 30)
        {
            reverseY();
        }
        else if(y < top + 5)
        {
            reverseY();
        }

        /*if(x < 0)
        {
            reverseX();
        }
        else if(x > 640-size - 10)   //code to cage the ball. but since getting off x bound
        {                            //will result in opp's getting more score, ts is no important no more
            reverseX();
        }
        */ 
    }
    
    public void reverseX()
    {
        cx *= -1;
    }

    public void reverseY()
    {
        cy *= -1;
    }

    public int getY()               { return y; }
    public int getX()               { return x; }
    public int getSize()            { return size; }
    public void setX(int x)         { this.x = x; }
    public void setY(int y)         { this.y = y; }
    public void setCx(int cx)       { this.cx = cx; }
    public void setCy(int cy)       { this.cy = cy; }
    public void setSpeed(int speed) { this.speed = speed; }
    
    public void increaseSpeed()
    {
        if(speed<MAX_SPEED)
        {
            speed++;

            cx = (cx/Math.abs(cx) * speed);
            cy = (cy/Math.abs(cy) * speed);
        }
    }

}