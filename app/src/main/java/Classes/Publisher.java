package Classes;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Publisher extends Thread {


    private static int N; //Num of brokers
    protected String[][] availableBrokers = new String[N][3]; //broker1: brokerIP, brokerPort -> Integer.parseInt(); , Integer.parseInt(broker keys);

    public Channel channel;

    private int BrokerPort;
    private String BrokerIp;

    private ServerSocket PublisherServer = null;
    private String pubIp;
    private int pubPort;

    public Publisher(){}

    public Publisher(String pubIp, int pubPort, String BrokerIp, int BrokerPort, int N){
        this.pubIp = pubIp;
        this.pubPort = pubPort;
        this.BrokerIp = BrokerIp;
        this.BrokerPort = BrokerPort;
        this.N = N;
        this.availableBrokers = new String[N][3]; //empty init
    }

    public void run(){
        // takes broker info and fills available brokers array
        initialization(BrokerIp, BrokerPort, availableBrokers);
        try {
            //get keys from brokers
			exchangeInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
            //shows menu and opens publisher server
			openServer();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public void initialization(String BrokerIp, int BrokerPort,String[][] availableBrokers) {
        Socket socket ;
        ObjectInputStream in;
        ObjectOutputStream out;
        try{
            socket = new Socket(BrokerIp, BrokerPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //System.out.println("Connected: " + socket);

            //take brokers' information
            out.writeObject("brokers");
            out.flush();

            availableBrokers = getBrokerInfo(in, availableBrokers );

            System.out.println("Brokers' information received");
            in.close();
            out.close();
            socket.close();
        }
        catch(UnknownHostException u) {
            System.out.println(u);
        }
        catch(IOException i) {
            System.out.println(i);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static String[][] getBrokerInfo(ObjectInputStream input, String[][] availableBrokers) throws IOException, ClassNotFoundException {
        System.out.println(availableBrokers.length);
        for (int i = 0; i < N; i++){
            for (int j = 0; j < 2; j++) {
                availableBrokers[i][j] = (String) input.readObject();
            }
        }
        return availableBrokers;
    }

    public void exchangeInfo() throws IOException {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;
        for (int i = 0; i < N; i++) { //για καθε broker
            try {
                socket = new Socket(availableBrokers[i][0], Integer.parseInt(availableBrokers[i][1]));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Publisher Connected: " + socket);

                //get keys from brokers
                out.writeObject("key");// ask for keys
                out.flush();
                String key = (String) in.readObject();//get keys
                availableBrokers[i][2] = key; //add key to availableBrokers

                //close connection
                in.close();
                out.close();
                socket.close();

            } catch (IOException u) {
                System.out.println(u);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

        //sort keys
        availableBrokers = sortKeys(availableBrokers);

        //υπολογισε ποιος broker είναι υπεύθυνος για σένα
        if(this.channel.key <= Integer.parseInt(availableBrokers[N][2])) {
        	for(int i = 0; i < N; i++) {
        		if(this.channel.key <= Integer.parseInt(availableBrokers[i][2])) {//αν το κλειδι του channel ειναι <= από το κλειδί του broker τοτε τον βρήκες
        			this.BrokerIp =  availableBrokers[i][0];
        			this.BrokerPort = Integer.parseInt(availableBrokers[i][1]);
        			break;
        		}
        	}
        }else {
        	int k = (this.channel.key) % N;
        	this.BrokerIp = availableBrokers[k+1][0];
        	this.BrokerPort = Integer.parseInt(availableBrokers[k+1][1]);
        }
        sendMyHashtags(BrokerIp, BrokerPort);

    }

    public void openServer() throws IOException, ClassNotFoundException {

        for(int i = 0; i < N; i++) { // foreach broker
            System.out.println((i+1) + " Broker's IP is: " + availableBrokers[i][0] + ".");
            System.out.println((i+1) + " Broker's Port is: " + availableBrokers[i][1] + ".");
            System.out.println((i+1) + " Broker's Key is: " + availableBrokers[i][2] + ".");
        }
    	 PublisherServer = new ServerSocket(pubPort);

         while (true) {

             System.out.println("Publisher > Waiting for requests...");
             Socket brokerRequest = PublisherServer.accept();
             ObjectInputStream in = new ObjectInputStream(brokerRequest.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(brokerRequest.getOutputStream());

             String data  = (String) in.readObject();
             if(data.equalsIgnoreCase("getVideos")){
                 //push
                 for(VideoFile video : channel.getAllVideos()){
                     //push(video,out);
                 }
             }
         }
    }

    public void printMenu() {
        System.out.println("---MENU---");
        System.out.println("Πατήστε 1 για να ορίσετε όνομα του καναλιού.");
        System.out.println("Πατήστε 2 για να φορτώσεται ή να τραβήξετε κάποιο βίντεο.");
        System.out.println("Πατήστε 3 για να διαγράψετε ένα βίντεο από το κανάλι σας.");
        System.out.println("Πατήστε 0 για έξοδο.");
    }

    public String getIp() {
        return this.pubIp;
    }


    //sort keys in availableBrokers array
    public static String[][] sortKeys(String[][] a) {

        String temp;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N - i - 1; j++) {
                if (Integer.parseInt(a[j][2]) > Integer.parseInt(a[j + 1][2])) {
                    for (int k = 0; k < 3; k++) {
                        temp = a[j][k];
                        a[j][k] = a[j + 1][k];
                        a[j + 1][k] = temp;
                    }
                }
            }
        }
        return a;
    }

    public void sendMyHashtags(String BrokerIp, int BrokerPort) {

        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            //συνδεόμαστε με τον υπεύθυνο broker
            socket = new Socket(BrokerIp, BrokerPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("My#s");
            out.flush();

            out.writeInt(this.channel.hashtagsPublished.size());//ενημερωνει τον broker για το μεγεθος του Array
            out.writeObject(this.channel);//ενημερωνει τον broker για το channel του Publisher
            for(String hashtag : channel.hashtagsPublished ) {
                out.writeObject(hashtag);//στελνει κάθε #.
            }

            socket.close();
            in.close();
            out.close();

        }catch(IOException e) {
            System.out.println(e);
        }

    }

    public void registration(String BrokerIp, int BrokerPort, Channel channel) {
        Socket socket;
        ObjectInputStream inBroker;
        ObjectOutputStream outBroker;

        try {
            //συνδεόμαστε με τον υπεύθυνο broker
            socket = new Socket(BrokerIp, BrokerPort);
            outBroker = new ObjectOutputStream(socket.getOutputStream());
            inBroker = new ObjectInputStream(socket.getInputStream());

            outBroker.writeObject("registerme");
            outBroker.flush();

            outBroker.writeObject(pubIp);
            outBroker.flush();

            outBroker.writeObject(pubPort);
            outBroker.flush();
            System.out.println("Channel  "+ channel);
            outBroker.writeObject(channel);
            outBroker.flush();

            socket.close();
            inBroker.close();
            outBroker.close();
            
        }catch(IOException e) {
            System.out.println(e);
        }
    }


    public void notifyBrokersDelete(String hashtag) {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        try {
            //συνδεόμαστε με τον υπεύθυνο broker
            socket = new Socket(BrokerIp, BrokerPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("delete#");
            out.flush();

            out.writeObject(hashtag);
            out.flush();
            
            out.writeObject(channel);
            out.flush();

            socket.close();
            in.close();
            out.close();

        }catch(IOException e) {
            System.out.println(e);
        }
    }

    public void loadVideos(){
        this.channel.getAllVideosFromFileSystem();
    }
/*
    public void push(VideoFile video,String BrokerIp,int BrokerPort) throws IOException {

        try {
            Socket connection2 = new Socket(BrokerIp,BrokerPort);
            ObjectOutputStream out = new ObjectOutputStream(connection2.getOutputStream());
            out.writeObject("sendVideo");
            out.flush();

            out.writeObject(this.channel);
            out.flush();

            out.writeObject("hello");
            out.flush();

            ReadMp4Files readMp4File = new ReadMp4Files();
            ArrayList<VideoFile> videoChunks = readMp4File.getVideo(video);
            out.writeInt(videoChunks.size());
            out.flush();

            for(VideoFile chunk : videoChunks){
                out.writeObject(chunk);
                out.flush();
                TimeUnit.SECONDS.sleep(1);
            }

            connection2.close();
            out.close();
        }catch(IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */
}
