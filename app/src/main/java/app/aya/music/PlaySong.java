package app.aya.music;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

public class PlaySong  extends AppCompatActivity {

    private View parent_view ;
    private SeekBar seek_song_progressbar ;

    private FloatingActionButton btn_play;

    private TextView tv_song_current_duration ,tv_song_total_duration ;
    private CircularImageView image ;

    private MediaPlayer mp ;
    private Handler mhandler = new Handler() ;

    private VideoUtils videoUtils;

    public String  SongName   ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        SongName =getIntent().getStringExtra("songname");

        setMusicPlayerComponents();
    }

    private void setMusicPlayerComponents() {

        parent_view=findViewById(R.id.parent);
        btn_play = findViewById(R.id.btn_play);

        seek_song_progressbar = findViewById(R.id.seek_song_progressbar);
        tv_song_current_duration = findViewById(R.id.tv_song_current_duration);
        tv_song_total_duration=findViewById(R.id.total_duration);
        image = findViewById(R.id.image);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlClick(view);
            }
        });

        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btn_play.setImageResource(R.drawable.ic_play_arrow);
            }
        });

        //----- play song -------------
        try{
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor afd = getAssets().openFd(SongName);// name music file
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            afd.close();
            mp.prepare();
        } catch (IOException e) {
            Snackbar.make(parent_view,"Could not load audio file.",Snackbar.LENGTH_LONG).show();
        }
        //------------------ seekbar ---------------
        videoUtils = new VideoUtils();

        seek_song_progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mhandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonPlayerAction();
        updateTimerAndSeekbar();
    }

    private void buttonPlayerAction(){

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.pause();
                    btn_play.setImageResource(R.drawable.ic_play_arrow);
                }else{
                    mp.start();
                    btn_play.setImageResource(R.drawable.ic_pause);
                    mhandler.post(mUpdateTimeTask);
                }
                rotateTheDisk();
            }
        });
    }

    public void controlClick(View v ){

        int id = v.getId();
        switch (id){
            case R.id.btn_next:{
                toggleButtonColor((ImageButton)v);
                Snackbar.make(parent_view,"text",Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_prev:{
                toggleButtonColor((ImageButton )v);
                Snackbar.make(parent_view,"Previous",Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_repeat:{
                toggleButtonColor((ImageButton )v);
                Snackbar.make(parent_view,"Repeat",Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_suffle:{
                toggleButtonColor((ImageButton )v);
                Snackbar.make(parent_view,"Shuffle",Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private Boolean  toggleButtonColor (ImageButton bt){

        String selected = (String) bt.getTag(bt.getId());
        if(selected != null){
            bt.setColorFilter(getResources().getColor(R.color.colorDarkOrange), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(),null);
            return false;
        }else{
            bt.setColorFilter(getResources().getColor(R.color.colorYellow), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(),"selected");
            return true;
        }
    }

    private void rotateTheDisk (){
        if(!mp.isPlaying()){
            return;
        }
        image.animate().setDuration(100).rotation(image.getRotation()+4f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                rotateTheDisk();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    PlaySong.super.onEnterAnimationComplete();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            updateTimerAndSeekbar();
            if(mp.isPlaying()){
                mhandler.postDelayed(this,100 );
            }
        }
    };

    private int getMaxSong (){
        long totalDuration = mp.getDuration();
        String limit =videoUtils.milliSecondesToTime(totalDuration) ;
        String[] keyValue = limit.split(":");
        String limit_hour = keyValue[0];
        String limit_min = keyValue[1];
        int hour = Integer.parseInt(limit_hour)*60*60;
        int min  =Integer.parseInt(limit_min)*60;

        return hour + min ;
    }

    private void updateTimerAndSeekbar (){
        Integer totalDuration = mp.getDuration();
        Integer CurrentDuration = mp.getCurrentPosition();

        tv_song_total_duration.setText(videoUtils.milliSecondesToTime(totalDuration));
        tv_song_current_duration.setText(videoUtils.milliSecondesToTime(CurrentDuration));

        seek_song_progressbar.setMax(totalDuration);
        int progress = (int) (videoUtils.getProgressSeekBar(CurrentDuration,totalDuration));
        seek_song_progressbar.setProgress(CurrentDuration);
       // Toast.makeText(PlaySong.this, "progress: "+progress+"...current:"+CurrentDuration+"... max:"+seek_song_progressbar.getMax(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        mhandler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }else{
            Snackbar.make(parent_view,item.getTitle(),Snackbar.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
