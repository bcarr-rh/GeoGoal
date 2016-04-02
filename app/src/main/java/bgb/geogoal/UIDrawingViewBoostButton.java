
        package bgb.geogoal;

/**
 * Created by Ben on 3/4/2016.
 */

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.Point;
        import android.graphics.Rect;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.Canvas;
        import android.graphics.Paint;
        import android.graphics.Point;
        import android.graphics.Rect;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;

/**
 * Created by Ben on 3/4/2016.
 */
public class UIDrawingViewBoostButton extends View {
    private static final int BOUNDRY_WIDTH = 200;
    private static final int CIRCLE_WIDTH = 75;
    private static final int JOYSTICK_PULL = 100;
    private static final String TAG = "CirclesDrawingView";

    static Boolean touched = false;

    /** Main bitmap */
    private Bitmap mBitmap = null;

    private Rect mMeasuredRect;

    /** Stores data about single circle */
    private static class CircleArea {
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

    /** Paint to draw circles */
    private Paint mCirclePaint;


    /** All available circles */
    private CircleArea BoosteCircle;
    private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>();

    /**
     * Default constructor
     *
     * @param ct {@link android.content.Context}
     */
    public UIDrawingViewBoostButton(final Context ct) {
        super(ct);

        init(ct);
    }

    public UIDrawingViewBoostButton(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    public UIDrawingViewBoostButton(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    private void init(final Context ct) {
        Display display = ((WindowManager)ct.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height  = size.y;
        BoosteCircle = (new CircleArea(width-BOUNDRY_WIDTH, height/2, CIRCLE_WIDTH));

        mCirclePaint = new Paint();
        mCirclePaint.setColor(0x60FFFFFF);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(final Canvas canv) {
        canv.drawCircle(BoosteCircle.centerX, BoosteCircle.centerY, BoosteCircle.radius, mCirclePaint);
    }


    public int TouchEvent(final MotionEvent event) {
        CircleArea touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside the circle
                touchedCircle = getTouchedCircle(xTouch, yTouch);
                mCirclePointer.put(event.getPointerId(0), touchedCircle);
                if(touchedCircle != null) {
                    touched = true;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circle
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside the circle
                touchedCircle = getTouchedCircle(xTouch, yTouch);
                if (touchedCircle != null) {
                    touched = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                clearCirclePointer();
                touched = false;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // not general pointer was up
                touched = false;
                pointerId = event.getPointerId(actionIndex);

                mCirclePointer.remove(pointerId);
                break;

            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;

            default:
                // do nothing
                break;
        }

        return touched?10:0;
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");

        mCirclePointer.clear();
    }


    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link CircleArea} touched circle or null if no circle has been touched
     */
    private CircleArea getTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touched = null;
        if ((BoosteCircle.centerX - xTouch) * (BoosteCircle.centerX - xTouch) + (BoosteCircle.centerY - yTouch) * (BoosteCircle.centerY - yTouch) <= BoosteCircle.radius * BoosteCircle.radius) {
            touched = BoosteCircle;
        }
        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
