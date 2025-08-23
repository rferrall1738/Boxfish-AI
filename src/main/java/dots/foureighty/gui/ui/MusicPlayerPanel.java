package dots.foureighty.gui.ui;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/***
 * JPanel component that displays music player, with mute/unmute button and
 * volume slider
 */
public class MusicPlayerPanel extends JPanel {
    private FloatControl gain;
    private BooleanControl muteControl;
    private final JButton muteBtn = new JButton("Mute");
    private final JSlider volume = new JSlider(0, 100, 50);
    private boolean softMuted = false;
    private float lastDbBeforeSoftMute = 0f;

    public MusicPlayerPanel(URL resourcePath) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        if (resourcePath == null) {
            throw new FileNotFoundException("Could not found the specified audio resource");
        }
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourcePath);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel south = new JPanel(new BorderLayout(10, 10));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        left.add(muteBtn);

        JPanel vol = new JPanel(new BorderLayout(6, 6));
        vol.add(new JLabel("Volume"), BorderLayout.WEST);
        vol.add(volume, BorderLayout.CENTER);

        south.add(left, BorderLayout.WEST);
        south.add(vol,  BorderLayout.CENTER);

        add(south, BorderLayout.SOUTH);

        muteBtn.addActionListener(e -> toggleMute());
        volume.addChangeListener(e -> applyVolume());
        play(audioInputStream);
    }

    private void play(AudioInputStream in) throws IOException, LineUnavailableException {
        AudioFormat src = in.getFormat();
        AudioFormat dst = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                src.getSampleRate(),
                16,
                src.getChannels(),
                src.getChannels() * 2,
                src.getSampleRate(),
                false
        );
        AudioInputStream din = AudioSystem.getAudioInputStream(dst, in);
        Clip clip = AudioSystem.getClip();
        clip.open(din);
        in.close();
        din.close();

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        if (clip.isControlSupported(BooleanControl.Type.MUTE)) {
            muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
        }

        applyVolume();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    private void applyVolume() {
        if (gain == null || softMuted) return;
        double lin = Math.max(0.0001, volume.getValue() / 100.0);
        float dB = (float) (20.0 * Math.log10(lin));
        dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
        gain.setValue(dB);
        lastDbBeforeSoftMute = dB;
    }

    private void toggleMute() {
        if (muteControl != null) {
            boolean newMute = !muteControl.getValue();
            muteControl.setValue(newMute);
            muteBtn.setText(newMute ? "Unmute" : "Mute");
            return;
        }
        if (gain == null) { muteBtn.setEnabled(false); return; }
        if (!softMuted) {
            lastDbBeforeSoftMute = gain.getValue();
            gain.setValue(gain.getMinimum());
            softMuted = true;
            muteBtn.setText("Unmute");
        } else {
            gain.setValue(Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), lastDbBeforeSoftMute)));
            softMuted = false;
            muteBtn.setText("Mute");
        }
    }

}
