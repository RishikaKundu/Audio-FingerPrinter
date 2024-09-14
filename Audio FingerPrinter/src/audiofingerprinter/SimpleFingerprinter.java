package audiofingerprinter;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Simple fingerprinter to test your audio fingerprinter implementation. Performs everything
 * synchronously. Created by bjackson on 11/15/2015.
 */
public class SimpleFingerprinter {

    public static void main(String[] args) {

        URL musicDirectory = SimpleFingerprinter.class.getResource("/music");
        String path = musicDirectory.getPath();
        System.out.println("Loading db...");

        SongDatabase db = new SongDatabase();

        AudioFingerprinter rec = null;

        db.setFingerprinter(rec);
        db.loadDatabase(path);
        String song = SimpleFingerprinter.class.getResource("/music/CarolOfTheBells.mp3").getPath();
        System.out.println("Recognizing...");
        File fileIn = new File(song);
        List<String> results = rec.recognize(fileIn);
        int i = 1;
        System.out.println("Found " + results.size() + " results.");
        for (String s : results) {
            System.out.println(i + ": " + s);
            i++;
        }
    }
}
