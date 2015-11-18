package com.example.kimseungchul.pidtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    Button up1_btn10;
    Button down1_btn10;
    Button up3_btn10;
    Button down3_btn10;
    //Button graph_btn;
    Button landing_btn;

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

    int Kp_value = 0;
    int Ki_value = 0;
    int Kd_value = 0;
    int Start_value = 100;
    int Height = 0;

    ArrayList<String> rollList;
    ArrayList<String> pitchList;
    ArrayList<String> yawList;

    RelativeLayout layout_joystickR, layout_joystickL;
    JoyStickClass js_r, js_l;
    TextView textViewR_3;
    TextView textViewR_4;


    int flag = 0;
    int flag_l = 0;
    String js_rC ;
    String js_rS ;
    String js_rD ;
    String js_lC ;
    String js_lS ;
    String js_lD ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pid_layout);

        textViewR_3 = (TextView) findViewById(R.id.textViewR_3);
        textViewR_4 = (TextView) findViewById(R.id.textViewR_4);

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
        up1_btn10 = (Button) findViewById(R.id.up1_10);
        down1_btn10 = (Button) findViewById(R.id.down1_10);
        up3_btn10 = (Button) findViewById(R.id.up3_10);
        down3_btn10 = (Button) findViewById(R.id.down3_10);
        //graph_btn = (Button) findViewById(R.id.graph);
        landing_btn = (Button) findViewById(R.id.landing_btn);

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
        up1_btn10.setOnClickListener(this);
        down1_btn10.setOnClickListener(this);
        up3_btn10.setOnClickListener(this);
        down3_btn10.setOnClickListener(this);
        //graph_btn.setOnClickListener(this);
        landing_btn.setOnClickListener(this);

        setdata[3].setText(String.valueOf(Kp_value));
        setdata[4].setText(String.valueOf(Ki_value));
        setdata[5].setText(String.valueOf(Kd_value));
        setdata[7].setText(String.valueOf(Start_value));

        rollList = new ArrayList<String>();
        pitchList = new ArrayList<String>();
        yawList = new ArrayList<String>();

        layout_joystickR = (RelativeLayout)findViewById(R.id.layout_joystickR);
        layout_joystickL = (RelativeLayout)findViewById(R.id.layout_joystickL);

        joystick_func();


        //flagsetting();


        onHandler1();

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
                        Log.i("totaldata", " " + input);
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
                                Log.i("secondtotaldata", " " + input);

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



    public void joystick_func() {


        js_r = new JoyStickClass(getApplicationContext()
                , layout_joystickR, R.drawable.image_button);


        js_r.setStickSize(80, 80);
        js_r.setLayoutSize(500, 500);
        js_r.setLayoutAlpha(150);
        js_r.setStickAlpha(100);
        js_r.setOffset(90);
        js_r.setMinimumDistance(50);


        layout_joystickR.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_r.drawStick(arg1);
                flag = 1;

                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textViewR_3.setText("Angle : " + String.valueOf(js_r.getAngle()));
                    textViewR_4.setText("Distance : " + String.valueOf(js_r.getDistance()));


                    float js_rA = js_r.getAngle();
                    float js_rD = js_r.getDistance();


                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {

                    textViewR_3.setText("Angle :");
                    textViewR_4.setText("Distance :");


                    flag = 0;
                    socket_out.println("S150" + "*" + "C150" + "*");

                }
                return true;
            }
        });



        js_l = new JoyStickClass(getApplicationContext()
                , layout_joystickL, R.drawable.image_button);

        js_l.setStickSize(80, 80);
        js_l.setLayoutSize(500, 500);
        js_l.setLayoutAlpha(150);
        js_l.setStickAlpha(100);
        js_l.setOffset(90);
        js_l.setMinimumDistance(50);
        layout_joystickL.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_l.drawStick(arg1);
                flag_l = 1;

                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {


                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {



                    flag_l = 0;

                }
                return true;
            }
        });

    }


    private Handler mHandler;
    private Runnable r;

    private void onHandler1() {
        mHandler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                Log.d("angle", " "+ js_r.getAngle());
                Log.d("distance", " "+ js_r.getDistance());
                int rd = (int)js_r.getDistance();
                int ld = (int)js_l.getDistance();

                double cos = Math.cos(Math.toRadians(js_r.getAngle())) * 50 * rd;
                double sin = Math.sin(Math.toRadians(js_r.getAngle())) * 50 * rd;
                double cos_l = Math.cos(Math.toRadians(js_l.getAngle())) * 50 * ld;
                double sin_l = Math.sin(Math.toRadians(js_l.getAngle())) * 50 * ld;

                int rs = (int)sin + 150;
                int rc = (int)cos + 150;
                int ls = (int)sin_l + 150;
                int lc = (int)cos_l + 150;

                js_rS = String.valueOf(rs);
                js_rC = String.valueOf(rc);
                js_lS = String.valueOf(ls);
                js_lC = String.valueOf(lc);


                if(flag == 1) {
                    socket_out.println("S" + js_rS + "*" + "C" + js_rC + "*");
                    Log.d("roll, pitch", " " + js_rS + "/" + js_rC);

                }
                if(flag_l == 1){
                    socket_out.println("H" + js_lS + "*" + "Y" + js_lC + "*");
                    Log.d("Height, yaw" , " " + js_lS + "/" +js_lC);

                }
                flag = 0;
                flag_l = 0;

                //socket_out.println("A" + js_rA + "D" + js_rD + "A" + js_rA + "D" + js_rD);
                //socket_out.println("D" + js_rD);

                mHandler.postDelayed(r, 200);

            }
        };
        mHandler.postDelayed(r, 200);
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
            Log.i("array", " " + array[i]);
        }
        settingData(array);
    }

    public void settingData(String[] array){
        for(int i = 0; i<array.length; i++){



            //적용
            //Log.i("test", " "+ array[i]);
            //setdata[i].setText(array[i]);
           //Log.i("test2", " " + array[i]);

            //ctrldata[i] = Float.valueOf(array[i]);


        }


        if(array[0].equals("Angle")){
            //이면 roll , pitch, yaw가 3, 4, 5에 들어가게


            setdata[0].setText("SET");
            socket_out.println("F");
        }
//        else if(array[2].equals("Value")){
//            setdata[3].setText(array[3]);
//            setdata[4].setText(array[4]);
//            setdata[5].setText(array[5]);
//            setdata[6].setText(array[6]);
//        }
    }

    @Override
    public void onClick(View v) {
            if (v == up_btn) {
                Kp_value++;
                double data = Kp_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[3].setText(String.valueOf(result));
                socket_out.println("P" + Kp_value + "*");

            }
            else if (v == down_btn) {
                Kp_value--;
                double data = Kp_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[3].setText(String.valueOf(result));
                socket_out.println("P" + Kp_value + "*");

            }
            else if (v == up_btn2) {
                Ki_value++;
                double data = Ki_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[4].setText(String.valueOf(result));
                socket_out.println("I"+Ki_value+"*");
            }
            else if (v == down_btn2) {
                Ki_value--;
                double data = Ki_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[4].setText(String.valueOf(result));
                socket_out.println("I"+Ki_value+"*");
            }
            else if (v == up_btn3) {
                Kd_value++;
                double data = Kd_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[5].setText(String.valueOf(result));
                socket_out.println("D"+Kd_value+"*");
            }
            else if (v == down_btn3) {
                Kd_value--;
                double data = Kd_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[5].setText(String.valueOf(result));
                socket_out.println("D"+Kd_value+"*");
            }
            else if (v == start_btn) {
                Start_value = 3570;
                int data = Start_value * 10;
                setdata[7].setText(String.valueOf(data));
                socket_out.println("T"+Start_value+"*");
            }
            else if (v == stop_btn) {
                Start_value = 100;
//                Kp_value = 0;
//                Ki_value = 0;
//                Kd_value = 0;

                int data = Start_value * 10;
//                setdata[3].setText(String.valueOf(Kp_value));
//                setdata[4].setText(String.valueOf(Ki_value));
//                setdata[5].setText(String.valueOf(Kd_value));
                setdata[7].setText(String.valueOf(data));
                socket_out.println("T" + Start_value + "*");

            }
            else if (v == up_btn4) {
                Start_value += 10;
                int data = Start_value * 10;
                setdata[7].setText(String.valueOf(data));
                socket_out.println("T" + Start_value + "*");
            }

            else if (v == down_btn4) {
                Start_value -= 10;
                int data = Start_value * 10;
                setdata[7].setText(String.valueOf(data));
                socket_out.println("T"+Start_value+"*");
            }
            else if(v == up1_btn10){
                Kp_value += 10;
                double data = Kp_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[3].setText(String.valueOf(result));
                socket_out.println("P" + Kp_value + "*");
            }
            else if(v == down1_btn10){
                Kp_value -= 10;
                double data = Kp_value * 0.01;
                double result = Math.round(data*100d) / 100d;
                setdata[3].setText(String.valueOf(result));
                socket_out.println("P" + Kp_value + "*");
            }
            else if(v == up3_btn10){
                Kd_value += 10;
                double data = Kd_value * 0.01;
                double result = Math.round(data*100d) / 100d;
    setdata[5].setText(String.valueOf(result));
    socket_out.println("D" + Kd_value + "*");
}
else if(v == down3_btn10){
        Kd_value -= 10;
        double data = Kd_value * 0.01;
        double result = Math.round(data*100d) / 100d;
        setdata[5].setText(String.valueOf(result));
        socket_out.println("D" + Kd_value + "*");
        }
        else if(v == landing_btn) {
        socket_out.println("L");
//                Intent intent = new Intent(this, GraphActivity.class);
//                intent.putStringArrayListExtra("roll", rollList);
//                intent.putStringArrayListExtra("pitch", pitchList);
//                intent.putStringArrayListExtra("yaw", yawList);

//                startActivity(intent);
        }

        }


}
