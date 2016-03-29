package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BottomBorder extends GameObject {
    private Bitmap image;

    public BottomBorder(Bitmap res) {
        image = res;
        this.x = 0;
        this.y = 1043;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0, 1043, null);
    }

}