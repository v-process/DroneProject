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
    int[] ctrldata = new int[8];


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

        up_btn.setOnClickListener(this);
        down_btn.setOnClickListener(this);
        up_btn2.setOnClickListener(this);
        down_btn2.setOnClickListener(this);
        up_btn3.setOnClickListener(this);
        down_btn3.setOnClickListener(this);



        Thread worker = new Thread() {
            public void run() {
                try {
                    socket = new Socket("192.168.1.7", 5555);
                    //socket = new Socket("172.16.101.111", 5555);
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    while (true) {

                        kp_data = socket_in.readLine();

                        kp_text.post(new Runnable() {
                            public void run() {
                                //kp_text.setText(String.valueOf(kp_data));
                                arraydata = kp_data.split("s");
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

    public  void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.format("array[%d] = %s%n", i, array[i]);
            Log.i("data123", " " + array[i]);
        }
        settingData(array);
    }

    public  void settingData(String[] array){
        for(int i = 0; i<array.length; i++){

            //적용
            setdata[i].setText(array[i]);

            ctrldata[i] = Integer.valueOf(array[i]);
        }

    }

    @Override
    public void onClick(View v) {
            if (v == up_btn) {
                int kp_int = ++ctrldata[3];
                String data = String.valueOf(kp_int);
                setdata[3].setText(data);
                socket_out.println(data);
            }
            else if (v == down_btn) {
                int kp_int = --ctrldata[3];
                String data = String.valueOf(kp_int);
                setdata[3].setText(data);
                socket_out.println(data);
            } else if (v == up_btn2) {
                if (ki_data != null) {

                    int ki_int = Integer.valueOf(ki_data);
                    ++ki_int;
                    ki_data = String.valueOf(ki_int);
                    ki_text.setText(ki_data);
                    socket_out.println(ki_data);
                }
            } else if (v == down_btn2) {
                if (ki_data != null) {
                    int ki_int = Integer.valueOf(ki_data);
                    --ki_int;
                    ki_data = String.valueOf(ki_int);
                    ki_text.setText(ki_data);
                    socket_out.println(ki_data);
                }
            } else if (v == up_btn3) {
                if (ki_data != null) {

                    int ki_int = Integer.valueOf(ki_data);
                    ++ki_int;
                    ki_data = String.valueOf(ki_int);
                    ki_text.setText(ki_data);
                    socket_out.println(ki_data);
                }
            } else if (v == down_btn3) {
                if (ki_data != null) {
                    int ki_int = Integer.valueOf(ki_data);
                    --ki_int;
                    ki_data = String.valueOf(ki_int);
                    ki_text.setText(ki_data);
                    socket_out.println(ki_data);
                }
            }
        }


}
