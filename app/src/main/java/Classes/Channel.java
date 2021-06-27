package Classes;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.File;
public class Channel implements Serializable {

    protected String channelName;
    protected int key;

    protected ArrayList<String> hashtagsPublished = new ArrayList<String>();
    protected HashMap<String, ArrayList<VideoFile>> userVideosByHashtag = new HashMap<String, ArrayList<VideoFile>>();
    protected ArrayList<VideoFile> allVideos = new ArrayList<VideoFile>();

    public Channel(String channelName) throws NoSuchAlgorithmException {
        this.channelName = channelName;
        this.key = calculateChannelHash(this.channelName);
    }

    public int calculateChannelHash(String channelName) throws NoSuchAlgorithmException {
        HashM channelHash = new HashM ();
        int key = Integer.parseInt(channelHash.getMd5(channelName));

        return key;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public int getKey() {
        return this.key;
    }

    protected ArrayList<VideoFile> getAllVideos(){return allVideos;}

    public void addVideo(VideoFile newVideo){
        // Add New Video to All Videos
        allVideos.add(newVideo);
        ArrayList<String> newVideoHashtags = newVideo.getHashtags();
        // Update hashtags Published
        for(String hashtag : newVideoHashtags){
            if(!hashtagsPublished.contains(hashtag)){
                hashtagsPublished.add(hashtag);
            }
        }
        //add video to hashmap
        for(String newHashtag : newVideo.getHashtags()){
            boolean hashtagFound = false;
            
            if(userVideosByHashtag.containsKey(newHashtag)){
                userVideosByHashtag.get(newHashtag).add(newVideo);
            }else{
                ArrayList<VideoFile> firstVideo = new ArrayList<VideoFile>();
                firstVideo.add(newVideo);
                userVideosByHashtag.put(newHashtag,firstVideo);
            }
        }
       for(VideoFile video : allVideos){
            System.out.println(video.videoName);
        }
    }

    public void removeVideo(String videoNameToRemove){
        VideoFile videoToRemove =null;

        for(VideoFile video : allVideos){
            if(video.getName().equalsIgnoreCase(videoNameToRemove)){
                videoToRemove = video;
            }
        }
        // Remove from all videos
        int removeCounter=0;
        int indexToRemove=-1;
        for(VideoFile video : allVideos){
            if(video.getName().equalsIgnoreCase(videoNameToRemove)) {
                indexToRemove=removeCounter;
            }
            removeCounter++;
        }
        if(indexToRemove>=0){
            allVideos.remove(indexToRemove);
        }

       //Remove from Hashtags
        for  (String hashtag : videoToRemove.getHashtags()){
            if(userVideosByHashtag.containsKey(hashtag)){
                if(userVideosByHashtag.get(hashtag).size()<=1){
                    userVideosByHashtag.remove(hashtag);
                }else{
                  int removeCounter2=0;
                  int indexToRemove2=-1;
                  for(VideoFile video : userVideosByHashtag.get(hashtag)){
                    if(video.getName().equalsIgnoreCase(videoToRemove.getName())) {
                        indexToRemove2=removeCounter2;
                    }
                      removeCounter2++;
                    }
                   if(indexToRemove2>=0){
                       userVideosByHashtag.get(hashtag).remove(indexToRemove2);
                   }
                }

            }
        }
        for(VideoFile video : allVideos){
            System.out.println(video.getName());
        }
    }

    public ArrayList<String> getAllVideosFromFileSystem(){

        ArrayList<String> allVideosByName = new ArrayList<String>();
        File folder = new File("src/Videos");
        File[] listOfFiles = folder.listFiles();
        int fileIndex=0;
        if(listOfFiles.length>0){
            System.out.println("Επιλέξτε το βίντεο που θέλετε να ανεβάσετε");
            //Get All Videos from Videos Folder
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    int i = file.getName().lastIndexOf('.');
                    if (i > 0) {
                        String extension = file.getName().substring(i+1);
                        if(extension.equalsIgnoreCase("mp4")){ // get only mp4 files
                            System.out.println("["+fileIndex+"]  "+file.getName()); // Show Videos to User
                            allVideosByName.add(file.getName().substring(0,file.getName().indexOf('.'))); // Send All Options
                            fileIndex++;
                        }
                    }
                }
            }
        }
        return allVideosByName;
    }

    public ArrayList<String> selectVideosToRemove() {

        ArrayList<String> allVideosByName = new ArrayList<String>();
        System.out.println("Επιλέξτε το βίντεο που θέλετε να διαγράψετε");
        int fileIndex = 0;
        for (VideoFile video : allVideos) {
            System.out.println("[" + fileIndex + "]  " + video.getName());
            allVideosByName.add(video.getName());
            fileIndex++;
        }

        return allVideosByName;
    }

        public static void main(String[] args) throws NoSuchAlgorithmException{
        Channel n = new Channel("fef") ;
        n.getAllVideosFromFileSystem();

    }

}