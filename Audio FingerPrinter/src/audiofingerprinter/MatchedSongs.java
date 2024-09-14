package audiofingerprinter;

public class MatchedSongs {
    String songName;
    Integer maxNumOffset;

    public MatchedSongs(String songName, Integer maxNumOffset) {
        this.songName = songName;
        this.maxNumOffset = maxNumOffset;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setMaxNumOffset(Integer maxNumOffset) {
        this.maxNumOffset = maxNumOffset;
    }

    public String getSongName() {
        return songName;
    }

    public Integer getMaxNumOffset() {
        return maxNumOffset;
    }


}
