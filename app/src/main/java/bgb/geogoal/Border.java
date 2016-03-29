package bgb.geogoal;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Border extends GameObject{
    private Bitmap image;

    public Border(Bitmap res, int xPos, int yPos) {
        image = res;
        this.x = xPos;
        this.y = yPos;
        this.height = res.getHeight();
        this.width = res.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, this.x, this.y, null);
    }
}
