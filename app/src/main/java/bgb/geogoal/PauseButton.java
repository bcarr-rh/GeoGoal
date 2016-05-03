package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by Ben on 5/3/2016.
 */
public class PauseButton extends GameObject{
    private Bitmap spritesheet;
    private double speed;
    private boolean playing;
    private Animation animation = new Animation();
    public boolean buttonOn;

    public PauseButton(Bitmap res, int w, int h, int numFrames) {
        buttonOn = false;
        setX((GamePanel.WIDTH * (5/7)) - (w / 2));
        setY((GamePanel.HEIGHT *(1/8)) - (h / 2));
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

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    public boolean TouchEvent(final MotionEvent event) {

        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                for (int i = 0; i < event.getPointerCount(); i++) {
                    xTouch = (int) event.getX(i);
                    yTouch = (int) event.getY(i);


                    if (Rect.intersects(this.getRectangle(), new Rect(xTouch,yTouch,xTouch,yTouch))) {
                        buttonOn = true;
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    xTouch = (int) event.getX(i);
                    yTouch = (int) event.getY(i);

                    // check if we've touched inside the button
                    if (Rect.intersects(this.getRectangle(), new Rect(xTouch,yTouch,xTouch,yTouch))) {
                        buttonOn = false;
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:

                break;

            case MotionEvent.ACTION_CANCEL:
                buttonOn = false;
                break;

            default:
                // do nothing
                break;
        }

        return buttonOn;
    }
}
