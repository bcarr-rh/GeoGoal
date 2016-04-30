package bgb.geogoal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.media.MediaPlayer;


public class Game extends Activity {

    MediaPlayer backroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //turn title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GamePanel(this));
        backroundMusic =  MediaPlayer.create(Game.this, R.raw.backroundmusic);
        backroundMusic.setLooping(true);
        backroundMusic.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        backroundMusic.release();
        finish();
    }

}
