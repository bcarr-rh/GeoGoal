package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BRBorder extends GameObject {
    private Bitmap image;

    public BRBorder(Bitmap res) {
        image = res;
        this.x = 1744;
        this.y = 629;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 1744, 629, null);
    }

}