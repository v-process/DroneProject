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

    TextView roll_text;// 각각의 이름
    TextView pitch_text;
    TextView yaw_text;
    TextView kp_text;
    TextView ki_text;
    TextView kd_text;
    TextView[] setdata = new TextView[8];

    Button up_btn; // kp, ki, kd버튼
    Button down_btn;
    Button up_btn2;
    Button down_btn2;
    Button up_btn3;
    Button down_btn3;
    Button up_btn4;
    Button down_btn4;

    Button start_btn;
    Button stop_btn;
    String kp_data;
    String ki_data;


    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;
    EditText input;
    Button button;
    TextView output;
    String data;
    String[] arraydata;
    float[] ctrldata = new float[8];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pid_layout);

        setdata[0] = (TextView) findViewById(R.id.roll);
        setdata[1] = (TextView) findViewById(R.id.pitch);
        setdata[2] = (TextView) findViewById(R.id.yaw);
        setdata[3] = (TextView) findViewById(R.id.kp);
        setdata[4] = (TextView) findViewById(R.id.ki);
        setdata[5] = (TextView) findViewById(R.id.kd);
        setdata[6] = (TextView) findViewById(R.id.height_text);
        setdata[7] = (TextView) findViewById(R.id.throttle_text);

        roll_text = (TextView) findViewById(R.id.roll_text);
        pitch_text = (TextView) findViewById(R.id.pitch_text);
        yaw_text = (TextView) findViewById(R.id.yaw_text);

        kp_text = (TextView) findViewById(R.id.kp_text);
        ki_text = (TextView) findViewById(R.id.ki_text);
        kd_text = (TextView) findViewById(R.id.kd_text);

        up_btn = (Button) findViewById(R.id.up_btn);
        down_btn = (Button) findViewById(R.id.down_btn);
        up_btn2 = (Button) findViewById(R.id.up_btn2);
        down_btn2 = (Button) findViewById(R.id.down_btn2);
        up_btn3 = (Button) findViewById(R.id.up_btn3);
        down_btn3 = (Button) findViewById(R.id.down_btn3);
        up_btn4 = (Button) findViewById(R.id.up_btn4);
        down_btn4 = (Button) findViewById(R.id.down_btn4);
        start_btn = (Button) findViewById(R.id.start_btn);
        stop_btn = (Button) findViewById(R.id.stop_btn);

        up_btn.setOnClickListener(this);
        down_btn.setOnClickListener(this);
        up_btn2.setOnClickListener(this);
        down_btn2.setOnClickListener(this);
        up_btn3.setOnClickListener(this);
        down_btn3.setOnClickListener(this);
        up_btn4.setOnClickListener(this);
        down_btn4.setOnClickListener(this);
        start_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);




        Thread worker = new Thread() {
            public void run() {
                try {
                    //socket = new Socket("192.168.1.7", 5555);//집.
                    //socket = new Socket("172.16.101.118", 5555);//해찬.
                    socket = new Socket("192.168.4.1", 3002);//바나나드론.
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String message;
                    while (true) {

                        char[] in = new char[50];
                        socket_in.read(in, 0, in.length);
                        final String input = new String(in,0,in.length);
                        Log.i("data12345", " " + input);
//                        arraydata = input.split(",");
//                        dumpArray(arraydata);


//                        while ((message = socket_in.readLine()) != null) {
//                            output = (TextView) findViewById(R.id.yaw_text);
//                            output.setText(message);
//                            Log.i("data12345", " " + message);
//
//                        }
//                        kp_data = socket_in.readLine();
//
//                        yaw_text.setText(kp_data);
//
//                        Log.i("data12345", " "+ kp_data);
//
//
                        kp_text.post(new Runnable() {
                            public void run() {
                                //yaw_text.setText(String.valueOf(kp_data));
                                arraydata = input.split(",");
                                dumpArray(arraydata);
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

    public void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.format("array[%d] = %s%n", i, array[i]);
            Log.i("data123", " " + array[i]);
        }
        settingData(array);
    }

    public void settingData(String[] array){
        for(int i = 0; i<array.length; i++){
            //적용
            Log.i("test", " "+ array[i]);
            setdata[i].setText(array[i]);
            Log.i("test2", " " + array[i]);

            //ctrldata[i] = Float.valueOf(array[i]);
        }
    }

    @Override
    public void onClick(View v) {
            if (v == up_btn) {
                String data = "q";
                socket_out.println(data);
            }
            else if (v == down_btn) {
                String data = "a";
                socket_out.println(data);
            }
            else if (v == up_btn2) {
                String data = "w";
                socket_out.println(data);
            }
            else if (v == down_btn2) {
                String data = "s";
                socket_out.println(data);
            }
            else if (v == up_btn3) {
                String data = "e";
                socket_out.println(data);
            }
            else if (v == down_btn3) {
                String data = "d";
                socket_out.println(data);
            }
            else if (v == start_btn) {
                String data = "b";
                socket_out.println(data);
            }
            else if (v == stop_btn) {
                String data = "x";
                socket_out.println(data);
            }
            else if (v == up_btn4) {
                String data = "t";
                socket_out.println(data);
            }

            else if (v == down_btn4) {
                String data = "g";
                socket_out.println(data);
            }

    }


}
