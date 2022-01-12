package jukebox;


import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class JukeBox {
    
    public static Clip clip;
    public static FloatControl control;
    public static boolean mute = false;
    private static int start, end;
    private static String name = "";
    
    public static void play(String path) {
        JukeBox.name = path;
        start = end = 0;
        AudioInputStream ais;
        try {
            ais = AudioSystem.getAudioInputStream(JukeBox.class.getClassLoader().getResource("music/"+path+".wav"));
            if(clip != null) {
                clip.stop();
                clip.close();
            }
            AudioFormat baseFormat = ais.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
            clip = AudioSystem.getClip();
            clip.open(dais);
            control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if(mute) {
                control.setValue(control.getMinimum());
            }
            clip.start();
            clip.addLineListener(new LineListener() {
                public void update(LineEvent le) {
                    
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println(ex);
        }
    }
    
    public static String getName() {
        return name;
    }
    
    public static void playAction(String path) {
        if(mute) return;
        Clip c;
        AudioInputStream ais;
        try {
            c = AudioSystem.getClip();
            ais = AudioSystem.getAudioInputStream(JukeBox.class.getClassLoader().getResource("sounds/"+path+".wav"));
            c.open(ais);
            c.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {}
    }
    
    public static void setMute(boolean mute) {
        JukeBox.mute = mute;
        if(mute) {
            if(clip != null) {
                clip.stop();
            }
        }
        else {
            if(clip != null) {
                clip.start();
            }
            if(control != null) {
                control.setValue(0);
            }
        }
    }
    
    public static void loop(int l) {
        if(clip != null) {
            clip.loop(l);
        }
    }
    
    public static void setLoopPoints(int start, int end) {
        if(clip == null) {
            return;
        }
        if(end == -1) {
            end = clip.getFrameLength() - 1;
        }
        clip.loop(-1);
        clip.setLoopPoints(start, end);
        JukeBox.start = start;
        JukeBox.end = end;
    }
    
    public static void stop() {
        if(control != null) {
            value = control.getValue();
            control.setValue(control.getMinimum());
        }
    }
    
    private static float value = 0;
}