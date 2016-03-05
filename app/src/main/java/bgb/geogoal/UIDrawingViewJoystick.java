package bgb.geogoal;

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
public class UIDrawingViewJoystick extends View {
    private static final int BOUNDRY_WIDTH = 200;
    private static final int CIRCLE_WIDTH = 75;
    private static final int JOYSTICK_PULL = 100;
    private static final String TAG = "CirclesDrawingView";

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
    private CircleArea JoyStickCircle;
    private Point JoyStickStartPoint = new Point();
    private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>();

    /**
     * Default constructor
     *
     * @param ct {@link android.content.Context}
     */
    public UIDrawingViewJoystick(final Context ct) {
        super(ct);

        init(ct);
    }

    public UIDrawingViewJoystick(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);

        init(ct);
    }

    public UIDrawingViewJoystick(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);

        init(ct);
    }

    private void init(final Context ct) {
        Display display = ((WindowManager)ct.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height  = size.y;
        JoyStickStartPoint.set(BOUNDRY_WIDTH,height/2);
        JoyStickCircle = (new CircleArea(JoyStickStartPoint.x, JoyStickStartPoint.y, CIRCLE_WIDTH));

        mCirclePaint = new Paint();
        mCirclePaint.setColor(0x60FFFFFF);
        mCirclePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(final Canvas canv) {
        canv.drawCircle(JoyStickCircle.centerX, JoyStickCircle.centerY, JoyStickCircle.radius, mCirclePaint);
    }


    public Point TouchEvent(final MotionEvent event) {
        Point deltaPoint = new Point();
        CircleArea touchedCircle;
        double xTouch;
        double yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearCirclePointer();

                xTouch = event.getX(0);
                yTouch = event.getY(0);

                // check if we've touched inside some circle
                touchedCircle = getTouchedCircle((int)xTouch, (int)yTouch);
                if (touchedCircle != null) {
                    touchedCircle.centerX = (int) xTouch;
                    touchedCircle.centerY = (int) yTouch;
                }
                mCirclePointer.put(event.getPointerId(0), touchedCircle);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                Log.w(TAG, "Pointer down");
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some circle
                touchedCircle = getTouchedCircle((int)xTouch, (int)yTouch);
                if (touchedCircle != null) {
                    mCirclePointer.put(pointerId, touchedCircle);
                    if (touchedCircle == JoyStickCircle) {
                        touchedCircle.centerX =(int) xTouch;
                        touchedCircle.centerY =(int) yTouch;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();

                Log.w(TAG, "Move");

                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);
                    touchedCircle = mCirclePointer.get(pointerId);
                    if (null != touchedCircle) {
                        if (touchedCircle == JoyStickCircle) {
                            if(Math.abs(xTouch - JoyStickStartPoint.x) < JOYSTICK_PULL && Math.abs(yTouch - JoyStickStartPoint.y) < JOYSTICK_PULL ) {
                                touchedCircle.centerX = (int)xTouch;
                                touchedCircle.centerY = (int)yTouch;
                                break;
                            }
                            xTouch = xTouch - JoyStickStartPoint.x;
                            yTouch = yTouch - JoyStickStartPoint.y;
                            touchedCircle.centerX = JoyStickStartPoint.x + (int)(JOYSTICK_PULL*(xTouch/(Math.sqrt(xTouch*xTouch+yTouch*yTouch))));
                            touchedCircle.centerY = JoyStickStartPoint.y + (int)(JOYSTICK_PULL*(yTouch/(Math.sqrt(xTouch*xTouch+yTouch*yTouch))));
                            break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                JoyStickCircle.centerX = JoyStickStartPoint.x;
                JoyStickCircle.centerY = JoyStickStartPoint.y;
                clearCirclePointer();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                mCirclePointer.remove(pointerId);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                // do nothing
                break;
        }

        return new Point(JoyStickCircle.centerX-JoyStickStartPoint.x,JoyStickCircle.centerY-JoyStickStartPoint.y);
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
        if ((JoyStickCircle.centerX - xTouch) * (JoyStickCircle.centerX - xTouch) + (JoyStickCircle.centerY - yTouch) * (JoyStickCircle.centerY - yTouch) <= JoyStickCircle.radius * JoyStickCircle.radius) {
            touched = JoyStickCircle;
        }
        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
