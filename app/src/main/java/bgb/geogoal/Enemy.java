package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;


public class Enemy extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private double dya;
    private double dxa;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();

    public Enemy(Bitmap res, int w, int h, int numFrames) {

        x = GamePanel.WIDTH - 200;
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
        dy = 0;
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

    public void update()
    {
        animation.update();

    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetScore(){score = 0;}
}