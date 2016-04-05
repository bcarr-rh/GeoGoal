package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import java.util.Random;


public class Player extends GameObject{

    private Bitmap spritesheet;
    private int score;
    private boolean playing;
    private float degrees;
    private static final double DEGREE_TO_RADIAN = Math.PI/180;
    private static final double RADIAN_TO_DEGREE = 180/Math.PI;
    private Animation animation = new Animation();
    public boolean collidingX = false;
    public boolean collidingY = false;
    public double speed = 0;

    public Player(Bitmap res, int w, int h, int numFrames) {
        degrees = 0;
        x = 250;
        setY(h);

        //y = Game Panel.HEIGHT / 2 - h / 2;
        dy = 0;
        dx = 0;
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);

    }

    public void update(Point movePoint, int boost)
    {
        if (movePoint.x != 0 && movePoint.y != 0) {
            degrees = (float) Math.toDegrees(Math.atan2(movePoint.y, movePoint.x));
            speed = Math.sqrt(movePoint.x * movePoint.x + movePoint.y * movePoint.y);

            if (!collidingX) {
                dx = movePoint.x * .1 + boost;
            }
            if (!collidingY) {
                dy = movePoint.y * .1 + boost;
            }

            this.x += dx;
            this.y += dy;


        }
        animation.update();
    }

    public void draw(Canvas canvas)
    {
        canvas.save();
        canvas.rotate(degrees, x + (this.width / 2), y + (this.height / 2)); //rotate the canvas' matrix
        canvas.drawBitmap(animation.getImage(), x, y, null); //draw the ball on the "rotated" canvas
        canvas.restore(); //rotate the canvas' matrix back

    }

    public void setY(int h)
    {
        Random rand = new Random();
        switch (rand.nextInt(3))
        {
            case 0: y = GamePanel.HEIGHT / 2 - h / 2;
                break;
            case 1: y = GamePanel.HEIGHT / 3 - h / 2;
                break;
            case 2: y = (2*(GamePanel.HEIGHT / 3)) - h / 2;
                break;
            default: break;
        }
    }

    public void changeDX(GameObject a) {
        this.dx *= -1;

        if (a.x < 1000)
            this.x = (int) (a.x + this.width * 1.5);
        if (a.x > 1000)
            this.x = (int) (a.x - this.width * 1.5);

        collidingX = true;
    }
    public void changeDY(GameObject a) {
        this.dy *= -1;

        if (a.y < 500)
            this.y = (int) (a.y + this.width);
        if (a.y > 500)
            this.y = (int) (a.y - this.width);

        collidingY = true;
    }

    public void reset() {
        this.x = 250;
        this.dx = 0;
        this.dy = 0;
        this.setY(80);
        this.speed = 0;
    }

    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
    public void increaseScore() {score++;}
}