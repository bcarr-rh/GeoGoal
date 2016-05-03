package bgb.geogoal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Ben on 4/20/2016.
 */
public class BoostCircle extends GameObject{

    private Bitmap spritesheet;
    private Animation animation = new Animation();

    public BoostCircle(Bitmap res, int w, int h, int numFrames, int x, int y) {
        this.x = x;
        this.y = y;

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

    public void draw(Canvas canv)  {
        canv.drawBitmap(animation.getImage(), x, y, null);
    }


    }



