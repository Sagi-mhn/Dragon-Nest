package com.dragonmerge.tests;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.dragonmerge.utils.AudioManager;
import com.dragonmerge.utils.JavaFXInitializer;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class AudioManagerTest {

    private AudioManager audioManager;

    @BeforeAll
    public static void initToolkit() throws Exception {
        JavaFXInitializer.initialize();
    }

    @BeforeEach
    public void setUp() {
        audioManager = AudioManager.getInstance();
    }

    public void testSingletonInstance() {
        AudioManager anotherInstance = AudioManager.getInstance();
        assertSame(audioManager, anotherInstance, "AudioManager devrait respecter le modèle");
    }

    @Test
    public void testInitialMuteState() {
        assertFalse(audioManager.isMuted(), "AudioManager ne devrait pas être en mode muet au départ");
    }

    @Test
    public void testPlayMusic() {
        audioManager.playMusic();
        assertFalse(audioManager.isMuted(), "AudioManager ne doit pas être en mode muet lorsque la musique joue");
    }

    @Test
    public void testPauseMusic() {
        audioManager.pauseMusic();
        assertFalse(audioManager.isMuted(),
                "AudioManager doit rester en mode non muet lorsque la musique est en pause");
    }

    @Test
    public void testToggleMute() {
        audioManager.toggleMute();
        assertTrue(audioManager.isMuted(), "AudioManager doit être en mode muet après avoir basculé le mode muet");

        audioManager.toggleMute();
        assertFalse(audioManager.isMuted(),
                "AudioManager ne doit pas être en mode muet après un second basculement du mode muet");
    }
}
