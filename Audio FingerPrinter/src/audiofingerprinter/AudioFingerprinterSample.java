package audiofingerprinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;


/**
 * Implements the AudioFingerprinter interface. Recognises the match for the input song.
 */
public class AudioFingerprinterSample implements AudioFingerprinter {

    SongDatabase songDatabase;

    public final int[] RANGE = new int[] { 40, 80, 120, 180, 300 };
    private static final int FUZ_FACTOR = 2;


    public AudioFingerprinterSample(SongDatabase songDatabase) {
        this.songDatabase = songDatabase;
    }

    /**
     * Returns the database of songs that this fingerprinter uses to recognize.
     * 
     * @return
     */

    public SongDatabase getSongDB() {
        return songDatabase;
    }


    /**
     * Given a 2D array of frequency information over time, returns the keypoints.
     * 
     * @param results, an array of frequency data.
     * @return a 2D array where the first index represents the time slice, and the second index contains
     *         the highest frequencies for the following ranges with that time slice
     */


    public long[][] determineKeyPoints(double[][] results) {
        long[][] keypoints = new long[results.length][5];
        for (int time = 0; time < results.length; time++) {
            double[] highScores = new double[] { 0, 0, 0, 0, 0 };

            for (int freq = 30; freq < 300; freq++) {
                double re = results[time][2 * freq];
                double im = results[time][2 * freq + 1];
                double mag = Math.log(Math.sqrt(re * re + im * im) + 1);

                int index = getIndex(freq);

                if (mag > highScores[index]) {
                    highScores[index] = mag;
                    keypoints[time][index] = freq;

                }
            }
        }
        return keypoints;
    }


    public int getIndex(int freq) {
        int i = 0;
        while (RANGE[i] <= freq)
            i++;
        return i;
    }

    /**
     * Overloaded method that takes a file object to recognize and call the other overloaded recognize
     * method.
     * 
     * @param fileIn
     * @return
     */
    public List<String> recognize(File fileIn) {
        byte[] audioData = songDatabase.getRawData(fileIn);
        return recognize(audioData);

    }

    /**
     * Takes an array of bytes representing a song and returns a list of song names with matching
     * fingerprints.
     * 
     * @param audioData array of bytes representing a song
     * @return A list of song names with the number of matching fingerprints, sorted in order from most
     *         likely match to least likely match.
     */

    public List<String> recognize(byte[] audioData) {

        double[][] freqArray = songDatabase.convertToFrequencyDomain(audioData);
        long[][] keypoints = determineKeyPoints(freqArray);

        HashMap<Integer, HashMap<Long, Integer>> matches = new HashMap<>();

        for (int time = 0; time < keypoints.length; time++) {
            long hash = hash(keypoints[time]);
            List<DataPoint> matchingPoints = songDatabase.getMatchingPoints(hash);
            if (matchingPoints != null) {
                for (DataPoint point : matchingPoints) {
                    long timeSong = point.getTime();
                    int songID = point.getSongId();
                    long offsetTime = timeSong - time;
                    HashMap<Long, Integer> offSets = matches.get(songID);
                    if (offSets == null) {
                        HashMap<Long, Integer> offSet = new HashMap<>();
                        offSet.put(offsetTime, 1);
                        matches.put(songID, offSet);
                    } else {
                        Integer count = offSets.get(offsetTime);
                        if (count == null) {
                            offSets.put(offsetTime, 1);
                        } else {
                            offSets.put(offsetTime, count + 1);
                        }
                    }
                }

            }
        }

        HashMap<String, Integer> matchOffset = new HashMap<>();
        for (Map.Entry<Integer, HashMap<Long, Integer>> entry : matches.entrySet()) {
            HashMap<Long, Integer> offSets = entry.getValue();
            int maxOffset = Collections.max(offSets.values());
            matchOffset.put(songDatabase.getSongName(entry.getKey()), maxOffset);
        }
        List<MatchedSongs> offSetNums = new ArrayList<>();
        for (java.util.Map.Entry<String, Integer> entry : matchOffset.entrySet()) {
            MatchedSongs matchedSongs = new MatchedSongs(entry.getKey(), entry.getValue());
            offSetNums.add(matchedSongs);
        }
        Collections.sort(offSetNums, Comparator.comparing(MatchedSongs::getMaxNumOffset));
        Collections.reverse(offSetNums);
        List<String> songNames = new ArrayList<>();
        for (MatchedSongs matched : offSetNums) {
            songNames.add(matched.getSongName() + " " + matched.getMaxNumOffset());
        }
        return songNames;
    }

    /**
     * Returns a hash combining information of several keypoints.
     * 
     * @param points array of key points for a particular slice of time. Must be at least length 4.
     * @return
     */
    public long hash(long[] points) {
        long h = 0;
        h = hash(points[0], points[1], points[2], points[3]);
        return h;
    }

    /**
     * Calculates the hash of 4 points.
     * 
     * @param point takes 4 long key points as input.
     * @return
     */
    private long hash(long p1, long p2, long p3, long p4) {
        return (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR))
            * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100
            + (p1 - (p1 % FUZ_FACTOR));
    }


}
