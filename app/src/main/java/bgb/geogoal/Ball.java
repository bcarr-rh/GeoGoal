package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Ball extends GameObject {
    private Bitmap spritesheet;
    private double speed;
    private boolean playing;
    private Animation animation = new Animation();

    public Ball(Bitmap res, int w, int h, int numFrames) {

        resetPosition(w, h);
        //dx = -20;
        //dy = -10;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(500);


    }

    public void update() {
        animation.update();

        //ball needs to move based on collision physics

        //hitDetection()

        if (dx < 20 && dx > -20) {
            x += dx;
        }
        else {
            dx = dx / Math.abs(dx);
            x += (int)dx * 20;
        }
        if (dy < 20 && dy > -20) {
            y += dy;
        }
        else {
            dy = (dy / Math.abs(dy));
            y += (int)dy*20;
        }


    }

    public void hitDetection(int sourceSpeed, int sourceX, int sourceY) {
        //this method will receive the parameters from the player's x and y coordinate
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public void resetPosition(int w, int h) {
        setX((GamePanel.WIDTH / 2) - (w / 2));
        setY((GamePanel.HEIGHT / 2) - (h / 2));
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDY() {
        dy = 0;
    }

    public void resetDX() {
        dx = 0;
    }

    public void changeDX(GameObject a) {
        this.dx *= -.9;


        if (a.x < 1000) {
            double overlap = Math.abs(a.x + a.width - this.x);
            this.x = (int) (a.x + a.width + overlap + 1);
        }
        if (a.x > 1000) {
            double overlap = Math.abs(a.x - (this.x + this.width));
            this.x = (int) (a.x - (overlap + 1 + this.width));
        }

    }

    public void changeDY(GameObject a) {
        this.dy *= -.9;

        if (a.y < 500) {
            double overlap = Math.abs(a.y + a.height - this.y);
            this.y = (int) (a.y + a.height + overlap + 1);
        }
        if (a.y > 500) {
            double overlap = Math.abs(a.y - (this.y + this.height));
            this.y = (int) (a.y  - (overlap + 1 + this.height));
        }

    }

    public void hit(GameObject a) {

        this.dx = a.dx + this.dx;
        this.dy = a.dy + this.dy;
    }
}