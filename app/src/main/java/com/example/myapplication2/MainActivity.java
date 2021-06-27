package com.example.myapplication2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import Classes.Singleton;
import Classes.Publisher;

public class MainActivity extends AppCompatActivity {

    Button uploadButton;
    private Singleton singleton;
    Publisher publisher;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = (Button) findViewById(R.id.uploadButton);
        singleton = singleton.getInstance();
        publisher = singleton.getPublisher();
        Thread thread = new Thread(new PublisherServer());
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Socket brokerSocket = new Socket("10.0.2.2", 4090);
                            Log.i("ef", "fee");
                            ObjectOutputStream out = new ObjectOutputStream(brokerSocket.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(brokerSocket.getInputStream());


                            out.writeObject("kalispera");
                            out.flush();

                            String data = (String) in.readObject();

                            if (data.equalsIgnoreCase("kai kali bradia")) {
                                Log.i("Hel", "fefefe");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();


                //Intent intent = new Intent(getApplicationContext(),PublisherActivity.class);
                //startActivity(intent);

            }
        });
    }
}

class PublisherServer implements Runnable {
    private ServerSocket publisherServer = null;
    Socket socket;

    @RequiresApi(api = Build.VERSION_CODES.N)

    public void run(){
        try {
            publisherServer = new ServerSocket();
            System.out.println("Publisher > Waiting for requests...");
            Socket brokerRequest = publisherServer.accept();
            ObjectInputStream in = new ObjectInputStream(brokerRequest.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(brokerRequest.getOutputStream());

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}