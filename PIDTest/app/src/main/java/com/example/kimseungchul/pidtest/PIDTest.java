package com.example.kimseungchul.pidtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by kimseungchul on 15. 10. 23..
 */
public class PIDTest extends Activity implements View.OnClickListener{

    TextView roll_text;
    TextView pitch_text;
    TextView yaw_text;
    TextView kp_text;
    TextView ki_text;
    TextView kd_text;
    TextView height_text;
    TextView throttle_text;
    Button up_btn;
    Button down_btn;
    Button up_btn2;
    Button down_btn2;
    Button up_btn3;
    Button down_btn3;
    String kp_data;
    String ki_data;


    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;
    EditText input;
    Button button;
    TextView output;
    String data;

    String[] arraytest = new String[8];

    int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pid_layout);
        roll_text = (TextView) findViewById(R.id.roll_text);
        pitch_text = (TextView) findViewById(R.id.pitch_text);
        yaw_text = (TextView) findViewById(R.id.yaw_text);
        kp_text = (TextView) findViewById(R.id.kp_text);
        ki_text = (TextView) findViewById(R.id.ki_text);
        kd_text = (TextView) findViewById(R.id.kd_text);
        height_text = (TextView) findViewById(R.id.height_text);
        throttle_text = (TextView) findViewById(R.id.throttle_text);
        up_btn = (Button) findViewById(R.id.up_btn);
        down_btn = (Button) findViewById(R.id.down_btn);


        up_btn.setOnClickListener(this);
        down_btn.setOnClickListener(this);
//        up_btn2.setOnClickListener(this);
//        down_btn2.setOnClickListener(this);
//        up_btn3.setOnClickListener(this);
//        down_btn3.setOnClickListener(this);


        //값 받아오기해서 텍스트뷰에 값표시.. 일단 kp만 해보자.
        kp_text.setText(String.valueOf(kp_data));


        Thread worker = new Thread() {
            public void run() {
                try {
                    socket = new Socket("192.168.1.7", 5555);
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    while (true) {

                        /*
                        if(count != 8) {
                            kp_data = socket_in.readLine();
                            Log.d("count", " "+kp_data);
                            kp_text.post(new Runnable() {
                                public void run() {

                                    arraytest[count] = kp_data;
                                    kp_text.setText(String.valueOf(kp_data));
                                    count++;
                                }
                            });
                           // kp_text.setText(String.valueOf(kp_data));

                        }
                        else{
                            count = 0;
                        }

                        */

                        kp_data = socket_in.readLine();

                        kp_text.post(new Runnable() {
                            public void run() {
                                kp_text.setText(String.valueOf(kp_data));
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        worker.start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (v == up_btn){
            if(kp_data != null) {

                int kp_int = Integer.valueOf(kp_data);
                ++kp_int;
                kp_data = String.valueOf(kp_int);
                kp_text.setText(kp_data);
                socket_out.println(kp_data);
            }
        }
        else if(v == down_btn){
            if(kp_data != null) {
                int kp_int = Integer.valueOf(kp_data);
                --kp_int;
                kp_data = String.valueOf(kp_int);
                kp_text.setText(kp_data);
                socket_out.println(kp_data);
            }
        }
        else if (v == up_btn2){
            if(ki_data != null) {

                int ki_int = Integer.valueOf(ki_data);
                ++ki_int;
                ki_data = String.valueOf(ki_int);
                ki_text.setText(ki_data);
                socket_out.println(ki_data);
            }
        }
        else if(v == down_btn2){
            if(ki_data != null) {
                int ki_int = Integer.valueOf(ki_data);
                --ki_int;
                ki_data = String.valueOf(ki_int);
                ki_text.setText(ki_data);
                socket_out.println(ki_data);
            }
        }

    }


}
