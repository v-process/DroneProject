package com.example.kimseungchul.wifiscantest;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by kimseungchul on 15. 10. 14..
 */
public class TCPsoketTest extends Activity {
    /** Called when the activity is first created. */
    private String return_msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcpsoket);


        //final EditText et = (EditText) findViewById(R.id.EditText01);
        Button btn = (Button) findViewById(R.id.Button01);
    }

    public void onClick(View v) {
        Toast toast = Toast.makeText(getApplicationContext(), "push", Toast.LENGTH_SHORT);
        toast.show();
        TCPclient tcpThread = new TCPclient("test");
        Thread thread = new Thread(tcpThread);


        thread.start();


    }

    private class TCPclient implements Runnable {
        //private static final String serverIP = "192.168.4.1"; // 서버 아이피
        private static final String serverIP = "192.168.1.11"; // 서버 아이피

        private static final int serverPort = 5555; // ex: 5555 // 접속 포트
        private Socket inetSocket = null;
        private String msg;

        // private String return_msg;
        public TCPclient(String _msg) {
            this.msg = _msg;
        }

        public void run() {
            // TODO Auto-generated method stub
            try {
                Toast toast = Toast.makeText(getApplicationContext(), "접속", 0);
                toast.show();



                Log.d("TCP", "C: Connecting...");

                inetSocket = new Socket(serverIP ,serverPort );
                //inetSocket.connect(socketAddr);

                try {
                    Log.d("TCP", "C: Sending: '" + msg + "'");
                    PrintWriter out = new PrintWriter(
                            new BufferedWriter(new OutputStreamWriter(
                                    inetSocket.getOutputStream())), true);

                    out.println(msg);
                    Log.d("TCP", "C: Sent.");
                    Log.d("TCP", "C: Done.");

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(inetSocket.getInputStream()));
                    return_msg = in.readLine();

                    Log.d("TCP", "C: Server send to me this message -->"
                            + return_msg);
                } catch (Exception e) {
                    Log.e("TCP", "C: Error1", e);
                } finally {
                    inetSocket.close();
                }
            } catch (Exception e) {
                Log.e("TCP", "C: Error2", e);
            }
        }// run
    }// TCPclient
}// class