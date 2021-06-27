package Classes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static java.nio.file.Files.readAllBytes;
import org.xml.sax.SAXException;


public class ReadMp4Files{
/*

    //splits video into chunks
    public ArrayList<byte[]> splitToChunk(VideoFile videoFile) throws IOException {
        int chunkSize = 256288; //split every 0.5mb

        ArrayList<byte[]> ChunkList = new ArrayList<>();

        byte[] Mp4ByteArray = videoFile.videoFileChunks;


        int lastChunkSize = Mp4ByteArray.length%(chunkSize);
        int numOfChunks = Mp4ByteArray.length/(chunkSize);

        int index = 0;
        for (int i = 0 ; i <= numOfChunks ; i++) {
            if( i < numOfChunks) {
                byte[] chunk = new byte[chunkSize];
                for(int j = 0 ; j < chunkSize ; j++)
                {
                    chunk[j] = Mp4ByteArray[index];
                    index++;
                }
                ChunkList.add(chunk);
            }
            else {
                byte[] chunk = new byte[lastChunkSize];
                for (int j = 0; j < lastChunkSize; j++) {
                    chunk[j] = Mp4ByteArray[index];
                    index++;
                }
                ChunkList.add(chunk);
            }
        }
        return ChunkList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public VideoFile createVideo(String videoName, String channelName, ArrayList<String> hashtags) throws TikaException, IOException, SAXException {

        String pathOfVideo = "src/Videos/"+videoName+".mp4";
        Path path = Paths.get(pathOfVideo);
        HashMap<String,String> videoMetadata = getVideoAttributes(videoName);
        byte[] Mp4ByteArray = readAllBytes(path);

        return new VideoFile(videoName,hashtags,videoMetadata,Mp4ByteArray);
    }


    public ArrayList<VideoFile> getVideo(VideoFile video) throws TikaException, IOException, SAXException {
        HashMap<String,String> videoMetadata = getVideoAttributes(video.getName());
        ArrayList<VideoFile> videoFileArray = new ArrayList<>();
        ArrayList<byte[]> videoChunk= splitToChunk(video);
        int numberOfChunks = videoChunk.size();

        //loop through chunks and pass them to an array as VideoFile Objects
        for (int i = 0; i <numberOfChunks ; i++) {
            VideoFile videoFile = new VideoFile(video.getName()+"_"+i,video.getHashtags(),videoMetadata,videoChunk.get(i));
            videoFileArray.add(videoFile);
        }

        return  videoFileArray;
    }

    // returns a hashmap with video file attributes
    public HashMap<String,String> getVideoAttributes(String videoName) throws IOException,SAXException, TikaException{
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        String pathOfVideo = "src/Videos/"+ videoName+".mp4";
        FileInputStream inputstream = new FileInputStream(new File(pathOfVideo));
        ParseContext pcontext = new ParseContext();

        MP4Parser MP4Parser = new MP4Parser();
        MP4Parser.parse(inputstream, handler, metadata,pcontext);
        String[] metadataNames = metadata.names();

        HashMap<String,String> videoMetada = new HashMap<String,String>();

        for(String name : metadataNames){
            videoMetada.put(name,metadata.get(name));
        }
        return  videoMetada;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getVideoAllBytes(String videoName){
        String pathOfVideo= "src/Videos/"+videoName+".mp4";
        Path path = Paths.get(pathOfVideo);
    }

 */
}

