package pins_pong;

import java.awt.*;

class Paddle
{
     private int height, x, y, speed;
    private Color color;

    //constant
    static final int PADDLE_WIDTH = 15;

    /**
     * A paddle is just a ping pong bat
     * @param x x cord of starting position of a paddle
     * @param y y cord of starting position of a paddle
     * @param height the paddle height
     * @param speed the amount the paddle may move per frame   CAN CHANGE LATER
     * @param color the paddle color
     */
    public Paddle(int x, int y, int height, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.speed = speed;
        this.color = color;
    }

    public int getHeight() { return height; }
    public int getY() { return y; }

    public void paint(Graphics g)
    {
        g.setColor(color);
        g.fillRect(x, y, PADDLE_WIDTH, height);
    }
    
    /**
    * Move the paddle towards this y position every frame (centered)
    * @param moveToY - position the paddle is centered on
    */

    public void moveToward(int moveToY)
    {
        /**
        * Move the paddle towards this y position every frame (centered)
        * @param moveToY - position the paddle is centered on
        */

        //find the location of the center of the paddle
        //@param centerY is also a "relative" center of the paddle, based on current y
        int centerY = y + height/2;

        if(Math.abs(centerY - moveToY) > speed) //check whether the difference between moveTo
        {                                       //and point we want to go is not more than the distance
            if(centerY >  moveToY)              //paddle usually travels
            {
                y -= speed;
            }

            if(centerY < moveToY )
            {
                y += speed;
            }

        }
    }

    /**
    *
    * @param b the ball we're checking for a collision with
    * @return true if collision is detected
    */
    public boolean checkCollision(Ball b)
    {
        int rightX = x + PADDLE_WIDTH;
        int bottomY = y + height;

        if( ( b.getX() + b.getSize() ) > x && b.getX() < rightX ) //check if ball is between the paddle (x cord)
        {
            if( ( b.getY() + b.getSize() ) > y && b.getY() < bottomY) //check if ball is between the paddle (y cord)
            {
                return true;
            }
        }
        
        return false; //nah, they aint be hitting bro (insert emoji cry)

    }

    

}
