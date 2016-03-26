package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BLBorder extends GameObject {
    private Bitmap image;

    public BLBorder(Bitmap res) {
        image = res;
        this.x = 0;
        this.y = 629;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0, 629, null);
    }

}