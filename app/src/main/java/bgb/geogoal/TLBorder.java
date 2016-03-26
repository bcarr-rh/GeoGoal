package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TLBorder extends GameObject {
    private Bitmap image;

    public TLBorder(Bitmap res) {
        image = res;
        this.x = 0;
        this.y = 0;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0, 0, null);
    }

}