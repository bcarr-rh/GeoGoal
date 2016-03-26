package bgb.geogoal;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int MOVESPEED = -5;
    private MainThread thread;
    private Background bg;
    private Player player;
    private Enemy enemy;
    private UIDrawingViewJoystick uiJoystick;
    private UIDrawingViewBoostButton uiBoost;
    private Ball ball;
    private TopBorder tBorder;
    private BottomBorder bBorder;
    private TLBorder tlBorder;
    private TRBorder trBorder;
    private BLBorder blBorder;
    private BRBorder brBorder;
    private boolean reset;
    private boolean newGameCreated;
    private boolean started;

    public GamePanel(Context context) {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
        uiJoystick = new UIDrawingViewJoystick(context);
        uiBoost = new UIDrawingViewBoostButton(context);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        ball = new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 95, 95, 2);

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.field));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 130, 80, 1);
        enemy = new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), 125, 80, 1);
        tBorder = new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.top));
        bBorder = new BottomBorder(BitmapFactory.decodeResource(getResources(), R.drawable.bot));
        tlBorder = new TLBorder(BitmapFactory.decodeResource(getResources(), R.drawable.topleft));
        trBorder = new TRBorder(BitmapFactory.decodeResource(getResources(), R.drawable.topright));
        blBorder = new BLBorder(BitmapFactory.decodeResource(getResources(), R.drawable.botleft));
        brBorder = new BRBorder(BitmapFactory.decodeResource(getResources(), R.drawable.botright));
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        uiBoost.TouchEvent(event);
        player.update(uiJoystick.TouchEvent(event));

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
                player.setUp(true);
            }
            if (player.getPlaying()) {
                if (!started) started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            enemy.update();
            ball.update();
            if (goal(ball)) {
                player.setPlaying(false);
            }
            if (collision(ball, tBorder) || collision(ball, bBorder)) {
                ball.changeDY();
            }
            if (collision(ball, trBorder) || collision(ball, tlBorder) ||
                    collision(ball, brBorder) || collision(ball, blBorder)) {
                ball.changeDX();
            }
        } else {
            player.resetDYA();
            enemy.resetDYA();
            ball.resetPosition(95, 95);
            if (!reset) {
                newGameCreated = false;
                reset = true;
            }

            if (!newGameCreated) {
                newGame();
            }


        }
    }

    public boolean goal(GameObject a) {
        if (a.getRectangle().exactCenterX() < 125) {
            if (a.getRectangle().exactCenterY() > 444 && a.getRectangle().exactCenterY() < 636) {
                enemy.increaseScore();
                return true;
            }
        }
        if (a.getRectangle().exactCenterX() > 1795) {
            if (a.getRectangle().exactCenterY() > 444 && a.getRectangle().exactCenterY() < 636) {
                player.increaseScore();
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            tBorder.draw(canvas);
            bBorder.draw(canvas);
            tlBorder.draw(canvas);
            trBorder.draw(canvas);
            blBorder.draw(canvas);
            brBorder.draw(canvas);
            player.draw(canvas);
            enemy.draw(canvas);
            ball.draw(canvas);
            canvas.restoreToCount(savedState);
            uiJoystick.draw(canvas);
            uiBoost.draw(canvas);
            drawText(canvas);
        }
    }

    public void newGame() {
        player.resetDYA();
        player.setY(80);
        enemy.resetDYA();
        enemy.setY(80);
        newGameCreated = true;
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Player Score: " + player.getScore(), 10, HEIGHT - 10, paint);
        canvas.drawText("Enemy Score: " + enemy.getScore(), WIDTH - 415, HEIGHT - 10, paint);

        if (!player.getPlaying() && newGameCreated && reset) {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("USE THE LEFT CIRCLE FOR MOVEMENT", WIDTH / 2 - 50, HEIGHT / 2 + 20, paint1);
            canvas.drawText("RIGHT CIRCLE FOR BOOST", WIDTH / 2 - 50, HEIGHT / 2 + 40, paint1);
        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }


}