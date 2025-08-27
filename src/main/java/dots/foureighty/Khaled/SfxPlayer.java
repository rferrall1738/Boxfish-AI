package dots.foureighty.Khaled;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class SfxPlayer {
    private final Map<Sfx, Clip> clips = new EnumMap<>(Sfx.class);
    private float desiredDb = 6.0f;
    private boolean muted = false;

    public SfxPlayer(Map<Sfx, URL> sources) throws Exception {
        for (Map.Entry<Sfx, URL> e : sources.entrySet()) {
            clips.put(e.getKey(), load(e.getValue()));
        }
    }

    private Clip load(URL url) throws Exception {
        if (url == null) throw new IllegalArgumentException("Missing SFX URL");
        AudioInputStream in = AudioSystem.getAudioInputStream(url);
        AudioFormat src = in.getFormat();
        AudioFormat dst = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                src.getSampleRate(), 16, src.getChannels(),
                src.getChannels() * 2, src.getSampleRate(), false);
        AudioInputStream din = AudioSystem.getAudioInputStream(dst, in);
        Clip clip = AudioSystem.getClip();
        clip.open(din);
        din.close();
        in.close();
        return clip;
    }

    public void setVolume(double volLinear) {
        double v = Math.max(0.0001, Math.min(1.0, volLinear));
        desiredDb = (float)(20.0 * Math.log10(v));
    }

    public void play(Sfx s) {
        if (muted) return;
        Clip c = clips.get(s);
        if (c == null) return;
        if (c.isRunning()) c.stop();
        c.setFramePosition(0);
        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl g = (FloatControl)c.getControl(FloatControl.Type.MASTER_GAIN);
            float db = Math.max(g.getMinimum(), Math.min(g.getMaximum(), desiredDb));
            g.setValue(db);
        }
        c.start();
    }
}