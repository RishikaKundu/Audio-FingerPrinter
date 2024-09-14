package audiofingerprinter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;

public class AudioFingerprinterTest {

    private AudioFingerprinter audioFingerprinter;
    private SongDatabase songDB;

    @BeforeEach
    public void setup() {
        songDB = new SongDatabase();
        audioFingerprinter = new AudioFingerprinterSample(songDB);
        songDB.setFingerprinter(audioFingerprinter);

    }

    @Test
    public void testDetermineKeyPoints() {
        double[][] rawData = new double[2][800]; // two time steps with frequencies 0-400hz
        double[] realPartsTimeZero = IntStream.range(0, 400).asDoubleStream().toArray();
        double[] realPartsTimeOne = IntStream.range(0, 400).map(i -> 400 - i).asDoubleStream().toArray();

        for (int i = 0, j = 0; i < rawData[0].length; i += 2, j++) {
            rawData[0][i] = realPartsTimeZero[j];
            rawData[0][i + 1] = 0; // Keep the imaginary part zero

            rawData[1][i] = realPartsTimeOne[j];
            rawData[1][i + 1] = 0; // Keep the imaginary part zero
        }

        long[][] result = audioFingerprinter.determineKeyPoints(rawData);
        assertArrayEquals(new long[][] { { 39, 79, 119, 179, 299 }, { 30, 40, 80, 120, 180 } }, result);
    }

    @Test
    public void testHash() {
        long[] input = { 10, 20, 30, 40 };
        long hash = audioFingerprinter.hash(input);
        assertEquals(4003002010L, hash); // the L at the end makes it a long instead of an int literal
        long[] input2 = { 11, 21, 31, 41 };
        hash = audioFingerprinter.hash(input2);
        assertEquals(4003002010L, hash); // fuzz factor works.
        long[] input3 = { 12, 22, 32, 42 };
        hash = audioFingerprinter.hash(input3);
        assertEquals(4203202212L, hash);
    }

    @Test
    public void testRecognize() {

        URL musicDirectory = SimpleFingerprinter.class.getResource("/music");
        String path = musicDirectory.getPath();
        songDB.loadDatabase(path);

        String song = SimpleFingerprinter.class.getResource("/music/CarolOfTheBells.mp3").getPath();
        File fileIn = new File(song);
        List<String> results = audioFingerprinter.recognize(fileIn);

        assertTrue(results.get(0).startsWith("CarolOfTheBells")); // should have aboue 990 matches
        assertTrue(results.get(1).startsWith("JoyToTheWorld")); // should have about 5 matches
        assertTrue(results.get(2).startsWith("AdagioInC")); // should have about 3 matches
        assertTrue(results.get(3).startsWith("AllaWhatParody")); // should have about 2 matches
    }
}
