package utils;

import org.monte.media.Format;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import java.awt.*;
import java.io.File;
import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

public class VideoRecorderUtil {
    private SpecializedScreenRecorder screenRecorder;

    public void startRecording(String testName) throws Exception {
        File file = new File("./target/videos");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        screenRecorder = new SpecializedScreenRecorder(gc, captureSize,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null, file, testName);
        screenRecorder.start();
    }

    public void stopRecording() throws Exception {
        screenRecorder.stop();
    }

    private class SpecializedScreenRecorder extends ScreenRecorder {
        private String name;

        public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea,
                                         Format fileFormat, Format screenFormat, Format mouseFormat,
                                         Format audioFormat, File movieFolder, String name) throws Exception {
            super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
            this.name = name;
        }

        @Override
        protected File createMovieFile(Format fileFormat) {
            if (!movieFolder.exists()) {
                movieFolder.mkdirs();
            }
            return new File(movieFolder, name + ".avi");
        }
    }
}
