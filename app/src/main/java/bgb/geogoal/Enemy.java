package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.Random;


public class Enemy extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private double dya;
    private double dxa;
    private boolean up;
    private boolean playing;
    private Animation animation = new Animation();
    public double speed = 0;
    float degrees = 0;


    public boolean collidingX = false;
    public boolean collidingY = false;

    public Enemy(Bitmap res, int w, int h, int numFrames) {


        setY(h);
        dy = 0;
        dx = 0;
        score = 0;
        height = h;
        width = w;
        x = GamePanel.WIDTH - (200 + this.width);
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i * width, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(10);

    }

    public void setUp(boolean b) {
        up = b;
    }

    public void update(Point ballLoc) {
        animation.update();

        Point desiredPos = findDestination(ballLoc, new Point(1795,540));
        double deltaX = desiredPos.x - this.x;
        double deltaY = desiredPos.y - this.y;
        degrees = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));


        //fill this in later
        if (!collidingX){}
            dx = deltaX * .2;
        if (!collidingY){}
            dy = deltaY * .2;

        this.x += dx;
        this.y += dy;

        speed = Math.sqrt(desiredPos.x * desiredPos.x + desiredPos.y * desiredPos.y);

    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(degrees, x + (this.width / 2), y + (this.height / 2)); //rotate the canvas' matrix
        canvas.drawBitmap(animation.getImage(), x, y, null); //draw the ball on the "rotated" canvas
        canvas.restore(); //rotate the canvas' matrix back
    }

    public void setY(int h) {
        Random rand = new Random();
        switch (rand.nextInt(3)) {
            case 0:
                y = GamePanel.HEIGHT / 2 - h / 2;
                break;
            case 1:
                y = GamePanel.HEIGHT / 3 - h / 2;
                break;
            case 2:
                y = (2 * (GamePanel.HEIGHT / 3)) - h / 2;
                break;
            default:
                break;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYA() {
        dya = 0;
    }

    public void resetScore() {
        score = 0;
    }

    public void increaseScore() {
        score++;
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
        this.setY(80);
        this.dx = 0;
        this.dy = 0;
        this.speed = 0;
        this.x = GamePanel.WIDTH - (200 + this.width);
    }

    public Point findDestination(Point ballPos, Point goalPos) {
        Point destination = new Point(0,0);

        destination.x = (ballPos.x + goalPos.x) / 2;
        destination.y = (ballPos.y + goalPos.y) / 2;

        return destination;
    }
}