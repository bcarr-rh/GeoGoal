package bgb.geogoal;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;

import android.graphics.Bitmap;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private MainThread thread;
    private Background bg;
    public Player player;
    public Enemy enemy;
    private UIDrawingViewJoystick uiJoystick;
    private UIDrawingViewBoostButton uiBoost;
    private Ball ball;
    private BoostCircle bs1;
    private BoostCircle bs2;
    private BoostCircle bs3;
    private BoostCircle bs4;

    private Border tlBorder;
    private Border trBorder;
    private Border brBorder;
    private Border blBorder;
    private Border tBorder;
    private Border bBorder;

    private PauseButton pause;

    private boolean reset;
    private boolean newGameCreated;
    private boolean started;
    private Context ContextForScreenSize;

    private Bitmap victory;
    private Bitmap defeat;


    public GamePanel(Context context) {
        super(context);

        ContextForScreenSize = context;
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


        ball = new Ball(BitmapFactory.decodeResource(getResources(), R.drawable.ball), 60, 57, 1);
        pause =  new PauseButton(BitmapFactory.decodeResource(getResources(), R.drawable.pause),64,64,1);

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.field));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player), 60, 40, 1, ContextForScreenSize);
        enemy = new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), 60, 40, 1);

        tlBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.topleft), 0, 0);
        trBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.topright), 1744, 0);
        blBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.botleft), 0, 629);
        brBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.botright), 1744, 629);
        tBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.top), 0, 0);
        bBorder = new Border(BitmapFactory.decodeResource(getResources(), R.drawable.bot), 0, 1043);
        victory = BitmapFactory.decodeResource(getResources(), R.drawable.victory);
        defeat = BitmapFactory.decodeResource(getResources(), R.drawable.defeat);

        bs1 = new BoostCircle(BitmapFactory.decodeResource(getResources(), R.drawable.boostcircle), 50,50, 1, 480, 360);
        bs2 = new BoostCircle(BitmapFactory.decodeResource(getResources(), R.drawable.boostcircle), 50,50, 1, 480, 1080);
        bs3 = new BoostCircle(BitmapFactory.decodeResource(getResources(), R.drawable.boostcircle), 50,50, 1, 1440, 360);
        bs4 = new BoostCircle(BitmapFactory.decodeResource(getResources(), R.drawable.boostcircle), 50,50, 1, 1440, 1080);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (pause.TouchEvent(event)) {
            //TODO: add logic for pause
        }
        int boost = uiBoost.TouchEvent(event);
        int boost = uiBoost.TouchEvent(event);

        player.update(uiJoystick.TouchEvent(event), boost);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
            }
            if (player.getPlaying()) {
                if (!started) started = true;
                reset = false;
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public boolean update() {
        if (player.getPlaying()) {

            enemy.update(new Point(player.getX(), player.getY()), new Point(ball.x, ball.y));
            ball.update();


            if (goal(ball)) {
                player.setPlaying(false);
                if ((player.getScore() == 5 || enemy.getScore() == 5) && Math.abs(player.getScore() - enemy.getScore()) > 2) {
                    return false;
                }
            }

            checkCollisions();


        } else {

            if (!reset) {
                newGameCreated = false;
                resetAfterPoint();
                reset = true;
            }

            if (!newGameCreated) {
                newGame();
            }


        }
        return true;
    }

    public void resetAfterPoint() {
        ball.resetPosition(95, 95);
        ball.dx = 0;
        ball.dy = 0;
        player.reset();
        enemy.reset();
    }

    public boolean goal(GameObject a) {
        if (a.getRectangle().exactCenterX() < 175) {
            if (a.getRectangle().exactCenterY() > 400 && a.getRectangle().exactCenterY() < 675) {
                enemy.increaseScore();
                return true;
            }
        }
        if (a.getRectangle().exactCenterX() > 1750) {
            if (a.getRectangle().exactCenterY() > 400 && a.getRectangle().exactCenterY() < 675) {
                player.increaseScore();
                return true;
            }
        }
        return false;
    }

    private void checkCollisions() {


        if (collision(ball, tBorder)) {
            ball.changeDY(tBorder);
        }
        else if (collision(ball, bBorder)) {
            ball.changeDY(bBorder);
        }

        //ball with walls
        if (collision(ball, trBorder)) {
            ball.changeDX(trBorder);
        } else if (collision(ball, tlBorder)) {
            ball.changeDX(tlBorder);
        } else if (collision(ball, brBorder)) {
            ball.changeDX(brBorder);
        } else if (collision(ball, blBorder)) {
            ball.changeDX(blBorder);
        }

        //ball with cars
        if (collision(ball, player)) {
            ball.hit(player);
        }
        if (collision(ball, enemy)) {
            ball.hit(enemy);
        }

        //player with top / bottom walls
        if (collision(player, tBorder)) {
            player.changeDY(tBorder);
        } else if (collision(player, bBorder)) {
            player.changeDY(bBorder);
        } else
            player.collidingY = false;

        //player with left / right walls
        if (collision(player, trBorder)) {
            player.changeDX(trBorder);
        } else if (collision(player, tlBorder)) {
            player.changeDX(tlBorder);
        } else if (collision(player, brBorder)) {
            player.changeDX(brBorder);
        } else if (collision(player, blBorder)) {
            player.changeDX(blBorder);
        } else
            player.collidingX = false;


        //enemy with top / bottom walls
        if (collision(enemy, tBorder)) {
            enemy.changeDY(tBorder);
        } else if (collision(enemy, bBorder)) {
            enemy.changeDY(bBorder);
        } else
            enemy.collidingY = false;

        //enemy with left / right walls
        if (collision(enemy, trBorder)) {
            enemy.changeDX(trBorder);
        } else if (collision(enemy, tlBorder)) {
            enemy.changeDX(tlBorder);
        } else if (collision(enemy, brBorder)) {
            enemy.changeDX(brBorder);
        } else if (collision(enemy, blBorder)) {
            enemy.changeDX(blBorder);
        } else
            enemy.collidingX = false;

        if (collision(player, enemy)) {
            if (player.speed > enemy.speed)
                enemy.reset();
            else if (player.speed < enemy.speed)
                player.reset();
            else {
                player.reset();
                enemy.reset();
            }
        }

        if (collision(player, bs1)) {
            player.resetBoost();
        }
        else if (collision(player, bs2)) {
            player.resetBoost();
        }
        else if(collision(player, bs3)) {
            player.resetBoost();
        }
        else if (collision(player, bs4)) {
            player.resetBoost();
        }


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

            pause.draw(canvas);


            player.draw(canvas);
            player.drawBoost(canvas);
            enemy.draw(canvas);
            ball.draw(canvas);
            canvas.restoreToCount(savedState);
            uiJoystick.draw(canvas);
            uiBoost.draw(canvas);

            bs1.draw(canvas);
            bs2.draw(canvas);
            bs3.draw(canvas);
            bs4.draw(canvas);

            drawText(canvas);
        }
    }

    public void newGame() {
        player.setY(80);
        enemy.setY(80);
        newGameCreated = true;
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        //canvas.drawText("Player Score: " + player.getScore(), 10, HEIGHT - 10, paint);
        canvas.drawText("Enemy Score: " + enemy.getScore(), WIDTH - 415, HEIGHT - 10, paint);


        Paint paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(36);
        paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Player Score: " + player.getScore(), 10, HEIGHT - 10, paint1);
        //canvas.drawText("PRESS TO START", WIDTH / 2 - 50, HEIGHT / 2, paint1);

        paint1.setTextSize(36);
        //canvas.drawText("USE THE LEFT CIRCLE FOR MOVEMENT", WIDTH / 2 - 50, HEIGHT / 2 + 20, paint1);
        //canvas.drawText("RIGHT CIRCLE FOR BOOST", WIDTH / 2 - 50, HEIGHT / 2 + 40, paint1);

    }

    public boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    public void drawEnd(int which, Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            if (which == 0)
                bg.drawEnd(canvas, victory);
            else
                bg.drawEnd(canvas, victory);
        }
    }

}