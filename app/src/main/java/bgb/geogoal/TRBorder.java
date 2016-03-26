package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TRBorder extends GameObject {
    private Bitmap image;

    public TRBorder(Bitmap res) {
        image = res;
        this.x = 1744;
        this.y = 0;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 1744, 0, null);
    }

}