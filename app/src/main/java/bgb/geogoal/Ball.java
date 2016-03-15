package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Ball extends GameObject{
    private Bitmap spritesheet;
    private double dya;
    private double dxa;
    private double speed;
    private boolean playing;
    private Animation animation = new Animation();

    public Ball(Bitmap res, int w, int h, int numFrames) {

        resetPosition(w, h);
        dy = 0;
        dx = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(500);

    }

    public void update()
    {
        animation.update();

        //ball needs to move based on collision physics

        //hitDetection()
        //x += 20;


    }

    public void hitDetection(int sourceSpeed, int sourceX, int sourceY)
    {
        //this method will receive the parameters from the player's x and y coordinate
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public void resetPosition(int w, int h)
    {
        x = (GamePanel.WIDTH / 2) - (w / 2);
        y = (GamePanel.HEIGHT / 2) - (h / 2);
    }

    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dya = 0;}
    public void resetDXA(){dxa = 0;}
}