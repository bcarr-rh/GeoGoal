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
    private double dya;
    private double dxa;
    private boolean up;
    private boolean playing;
    private float degrees;
    private static final double DEGREE_TO_RADIAN = Math.PI/180;
    private static final double RADIAN_TO_DEGREE = 180/Math.PI;
    private Animation animation = new Animation();
    public Player(Bitmap res, int w, int h, int numFrames) {
        degrees = 0;
        x = 100;
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

        //y = GameP anel.HEIGHT / 2 - h / 2;
        dy = 0;
        dx = 1;
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

    public void setUp(boolean b){up = b;}

    public void update(Point movePoint)
    {


        animation.update();
    }

    public void draw(Canvas canvas)
    {
        canvas.save();
        canvas.rotate(degrees, x + (this.width / 2), y + (this.height / 2)); //rotate the canvas' matrix
        canvas.drawBitmap(animation.getImage(), x, y, null); //draw the ball on the "rotated" canvas
        canvas.restore(); //rotate the canvas' matrix back

    }

    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
}