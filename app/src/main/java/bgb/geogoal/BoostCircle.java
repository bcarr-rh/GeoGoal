package bgb.geogoal;

import android.content.Context;
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
    private CircleArea BoostFillerCircle;
    private Paint mCirclePaint;
    private static final int BOUNDRY_WIDTH = 200;
    private static final int CIRCLE_WIDTH = 60;

    /** Stores data about single circle */
    private class CircleArea {
        int radius;
        int centerX;
        int centerY;

        CircleArea(int centerX, int centerY, int radius) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

        @Override
        public String toString() {
            return "Circle[" + centerX + ", " + centerY + ", " + radius + "]";
        }
    }

    public BoostCircle(final Context ct,double percentX,double percentY) {
        Display display = ((WindowManager)ct.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height  = size.y;
        BoostFillerCircle = (new CircleArea((int)(width-(width)*percentX), (int)(height*percentY), CIRCLE_WIDTH));

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.YELLOW);
        mCirclePaint.setStyle(Paint.Style.FILL);

        x = BoostFillerCircle.centerX -BoostFillerCircle.radius;
        y = BoostFillerCircle.centerY - BoostFillerCircle.radius;
        width = BoostFillerCircle.radius*2;
        height = BoostFillerCircle.radius*2;
    }

    public void onDraw(final Canvas canv) {
        canv.drawCircle(BoostFillerCircle.centerX, BoostFillerCircle.centerY, BoostFillerCircle.radius, mCirclePaint);
    }


    }



