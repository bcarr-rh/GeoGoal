package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res) {
        image = res;

    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0, 0, null);

    }

    public void drawEnd(Canvas canvas, Bitmap screen) {
        canvas.drawBitmap(screen, 0, 0, null);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}