package app.aya.music;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;

import app.aya.music.media.MusicService;
import app.aya.music.media.PlaybackInfoListener;
import app.aya.music.media.PlayerAdapter;

public class PlaySong  extends AppCompatActivity {

    private View parent_view ;
    private SeekBar seek_song_progressbar ;
    private PlayerAdapter mPlayerAdapter;
    private MusicService mMusicService;
    private FloatingActionButton btn_play ;
    private boolean mUserIsSeeking = false;
    private TextView tv_song_current_duration ,tv_song_total_duration ,tv_playing;
    private CircularImageView image ;
    private ImageButton btn_prev , btn_next , btn_repeat , btn_suffle ;
    private MediaPlayer mp ;
    private Handler mhandler = new Handler() ;

    private VideoUtils videoUtils;
    private PlaybackListener mPlaybackListener;
    public String  SongName , playing , current_position;
    private Integer    totalDuration , CurrentDuration ;
    private ArrayList<String>  serial_music ;
    private ArrayList<String>  name_music ;
    private Boolean             repeat =false , suffle =false;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            mMusicService = ((MusicService.LocalBinder) iBinder).getInstance();
            mPlayerAdapter = mMusicService.getMediaPlayerHolder();

            if (mPlaybackListener == null) {
                mPlaybackListener = new PlaybackListener();
                mPlayerAdapter.setPlaybackInfoListener(mPlaybackListener);
            }
            if (mPlayerAdapter != null && mPlayerAdapter.isPlaying()) {

                restorePlayerStatus();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        serial_music = new ArrayList<String>();
        name_music = new ArrayList<String>();

        SongName =getIntent().getStringExtra("songname");
        playing =getIntent().getStringExtra("playing");
        current_position = getIntent().getStringExtra("position");
        serial_music = getIntent().getStringArrayListExtra("serial");
        name_music = getIntent().getStringArrayListExtra("name");
        setMusicPlayerComponents(SongName,playing);
    }

    private void setMusicPlayerComponents(String song , String title_song) {

        parent_view=findViewById(R.id.parent);
        btn_play = findViewById(R.id.btn_play);

        seek_song_progressbar = findViewById(R.id.seek_song_progressbar);
        tv_song_current_duration = findViewById(R.id.tv_song_current_duration);
        tv_song_total_duration=findViewById(R.id.total_duration);
        btn_next = findViewById(R.id.btn_next);
        btn_prev = findViewById(R.id.btn_prev);
        btn_repeat=findViewById(R.id.btn_repeat);
        btn_suffle = findViewById(R.id.btn_suffle);
        tv_playing = findViewById(R.id.playing);
        image = findViewById(R.id.image);

        tv_playing.setText(title_song);

        final int color_orange = ContextCompat.getColor(this, R.color.colorDarkOrange);
        final int color_white = ContextCompat.getColor(this, R.color.colorwhite);

        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                    if(repeat){
                        repeat =false;
                        btn_repeat.setImageTintList(ColorStateList.valueOf(color_white));
                    }else if(!repeat) {
                        repeat = true;
                       // btn_repeat.setBackgroundTintList(ColorStateList.valueOf(0xFF4CA00f));
                        btn_repeat.setImageTintList(ColorStateList.valueOf(color_orange));
                    }

            }
        });


        btn_suffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(suffle){
                    suffle =false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_suffle.setImageTintList(ColorStateList.valueOf(color_white));
                    }
                }else if(!suffle){
                    suffle=true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_suffle.setImageTintList(ColorStateList.valueOf(color_orange));
                    }
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMusic();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preMusic();
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
            AssetFileDescriptor afd = getAssets().openFd(song);// name music file
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            afd.close();
            mp.prepare();
        } catch (IOException e) {
            Snackbar.make(parent_view,"Could not load audio file.",Snackbar.LENGTH_LONG).show();
        }
        //------------------ seekbar ---------------
        videoUtils = new VideoUtils();

        seek_song_progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int userSelectedPosition = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    userSelectedPosition = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mhandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(userSelectedPosition);
                mhandler.post(mUpdateTimeTask);
            }
        });

        buttonPlayerAction(null);
        updateTimerAndSeekbar();

    }

    private void nextMusic (){
        int pos = Integer.parseInt(current_position)+1 ;
        if(pos < serial_music.size()) {
            String next = serial_music.get(pos);
            String name = name_music.get(pos);
            current_position = pos + "";
            setMusicPlayerComponents(next, name);
        }else {
            Toast.makeText(this, "لا يوجد اغنيه تالية", Toast.LENGTH_SHORT).show();
        }
    }

    private void preMusic (){

        int pos = Integer.parseInt(current_position)- 1 ;
        if (pos > -1) {
            String next = serial_music.get(pos);
            String name = name_music.get(pos);
            current_position = pos + "";
            setMusicPlayerComponents(next, name);
        }else {
            Toast.makeText(this, "لا يوجد اغنية سابقة", Toast.LENGTH_SHORT).show();
        }
    }

    private void restorePlayerStatus() {
        seek_song_progressbar.setEnabled(mPlayerAdapter.isMediaPlayer());

        //if we are playing and the activity was restarted
        //update the controls panel
        if (mPlayerAdapter != null && mPlayerAdapter.isMediaPlayer()) {

            mPlayerAdapter.onResumeActivity();
            updatePlayingInfo(true, false);
        }
    }

    private void updatePlayingInfo(boolean restore, boolean startPlay) {

        if (startPlay) {
            mPlayerAdapter.getMediaPlayer().start();
        }

        final int duration = mp.getDuration();
        seek_song_progressbar.setMax(duration);

        if (restore) {
            seek_song_progressbar.setProgress(mPlayerAdapter.getPlayerPosition());

        }
    }

    private void buttonPlayerAction(String action){

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.pause();
                    btn_play.setImageResource(R.drawable.ic_play_arrow);
                    seek_song_progressbar.setEnabled(false);
                }else{
                    mp.start();
                    btn_play.setImageResource(R.drawable.ic_pause);
                    seek_song_progressbar.setEnabled(true);

                    mhandler.post(mUpdateTimeTask);
                }
                rotateTheDisk();
            }
        });

        if (action == "stop"){
            mp.pause();
            btn_play.setImageResource(R.drawable.ic_play_arrow);
            seek_song_progressbar.setEnabled(false);
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
                mhandler.postDelayed(this,1000);
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

        totalDuration = mp.getDuration() ;
        CurrentDuration = mp.getCurrentPosition();

        tv_song_total_duration.setText(videoUtils.milliSecondesToTime(totalDuration));
        tv_song_current_duration.setText(videoUtils.milliSecondesToTime(CurrentDuration));
        seek_song_progressbar.setMax(totalDuration);
        int progress = (int) (videoUtils.getProgressSeekBar(CurrentDuration,totalDuration));
        seek_song_progressbar.setProgress(CurrentDuration);

    }

    @Override
    protected void onDestroy() {
        mhandler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        buttonPlayerAction("stop");
        super.onBackPressed();
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

    class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                seek_song_progressbar.setProgress(position);
            }
        }

        @Override
        public void onStateChanged(@State int state) {

            if (mPlayerAdapter.getState() != State.RESUMED && mPlayerAdapter.getState() != State.PAUSED) {
                updatePlayingInfo(false, true);
            }
        }

        @Override
        public void onPlaybackCompleted() {
            //After playback is complete
        }
    }
}
