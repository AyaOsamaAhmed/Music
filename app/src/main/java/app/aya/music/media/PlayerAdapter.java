package app.aya.music.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import java.util.List;

import app.aya.music.play.PlaySong;

public interface PlayerAdapter {
    void initMediaPlayer();

    void release();

    boolean isMediaPlayer();

    boolean isPlaying();

    void resumeOrPause();

    void reset();

    boolean isReset();

    void instantReset();

    void skip(final boolean isNext);

    void seekTo(final int position);

    void setPlaybackInfoListener(final PlaybackInfoListener playbackInfoListener);

    PlaySong getCurrentSong();

    @PlaybackInfoListener.State
    int getState();

    int getPlayerPosition();

    void registerNotificationActionsReceiver(final boolean isRegister);


    void setCurrentSong(@NonNull final PlaySong song, @NonNull final List<PlaySong> songs);

    MediaPlayer getMediaPlayer();

    void onPauseActivity();

    void onResumeActivity();
}
