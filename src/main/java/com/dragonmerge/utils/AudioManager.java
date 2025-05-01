package com.dragonmerge.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;
import javafx.util.Duration;

public class AudioManager {
    private static AudioManager instance;
    private MediaPlayer mediaPlayer;
    private boolean isMuted = false;

    private AudioManager() {
        initializeMediaPlayer();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    private void initializeMediaPlayer() {
        String soundPath = Paths.get("src/ressources/song/music1.mp3").toUri().toString();
        Media sound = new Media(soundPath);
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void playMusic() {
        if (mediaPlayer != null && !isMuted) {
            mediaPlayer.play();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    public boolean isMuted() {
        return isMuted;
    }
}
