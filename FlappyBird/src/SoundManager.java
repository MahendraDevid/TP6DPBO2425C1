import javax.sound.sampled.*;

public class SoundManager {
    private Clip bgMusic;
    private Clip jumpSound;

    public SoundManager() {
        bgMusic = loadSound("/assets/sounds/tokyo_drift.wav");
        jumpSound = loadSound("/assets/sounds/car-engine.wav");
    }

    private Clip loadSound(String path) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void playBackground() {
        play(bgMusic, true);
    }

    public void stopBackground() {
        if (bgMusic != null) bgMusic.stop();
    }

    public void playJump() {
        play(jumpSound, false);
    }

    private void play(Clip clip, boolean loop) {
        if (clip == null) return;
        clip.stop();
        clip.setFramePosition(0);
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        else clip.start();
    }
}
