package bgb.geogoal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;
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
    private int boostValue;
    private static final int MAX_BOOST = 100;
    private Rect boostBarOuter;
    private Rect boostBarInner;
    private Paint boostOuterPaint;
    private Paint boostInnerPaint;


    public Player(Bitmap res, int w, int h, int numFrames, Context ct) {
        degrees = 0;
        x = 250;
        setY(h);
        boostValue = MAX_BOOST;

        Display display = ((WindowManager)ct.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height  = size.y;

        boostBarOuter = new Rect(110,230,10,10);
        boostBarInner = new Rect(100,220,20,220);
        boostInnerPaint = new Paint();
        boostInnerPaint.setColor(Color.YELLOW);
        boostInnerPaint.setStyle(Paint.Style.FILL);
        boostOuterPaint = new Paint();
        boostOuterPaint.setColor(Color.BLACK);
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


    public void resetBoost() {boostValue = MAX_BOOST;}


    public void update(Point movePoint, int boost)
    {
        if (boost > 1 && boostValue > 0) {
            boostValue--;
        } else {
            boost = 1;
        }
        if (movePoint.x != 0 && movePoint.y != 0) {
            degrees = (float) Math.toDegrees(Math.atan2(movePoint.y, movePoint.x));


            if (!collidingX) {
                dx = movePoint.x * .1 * boost ;
            }
            if (!collidingY) {
                dy = movePoint.y * .1 * boost;
            }

            this.x += dx;
            this.y += dy;

            speed = Math.sqrt(dx * dx + dy * dy);
        }
        animation.update();
    }

    public void draw(Canvas canvas)
    {
        //draw boost bar

        canvas.drawRect(boostBarOuter,boostOuterPaint);
        canvas.drawRect(boostBarInner.left,boostBarInner.top,boostBarInner.right,boostBarInner.bottom- (2*boostValue), boostInnerPaint);


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
        resetBoost();
    }

    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetScore(){score = 0;}
    public void increaseScore() {score++;}
}