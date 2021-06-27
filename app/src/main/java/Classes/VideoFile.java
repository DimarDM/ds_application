package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoFile implements Serializable {
    public String videoName;
    public HashMap<String, String> metadata;
    public ArrayList<String> hashtags;
    public byte[] videoFileChunks;

    public VideoFile(String videoName,  ArrayList<String> hashtags, HashMap<String, String> metadata, byte[] videoFileChunks) {
        this.videoName = videoName;
        this.hashtags = hashtags;
        this.videoFileChunks = videoFileChunks;
        this.metadata = metadata;

    }

    public String getAttribute(String name) {
        for (String key : metadata.keySet()) {
            if (key == name) {
                return metadata.get(key);
            }
        }
        return "";
    }
    public String getName(){return videoName;}

    public ArrayList<String> getHashtags() {
        return hashtags;
    }
    public  byte[] getvideoFileChunk (){return videoFileChunks;}

    public void addHashtag(String hashtag) {
        hashtags.add(hashtag);
    }
}


