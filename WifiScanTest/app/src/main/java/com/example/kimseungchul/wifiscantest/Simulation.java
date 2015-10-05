package com.example.kimseungchul.wifiscantest;



import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by kimseungchul on 15. 10. 2..
 */


//시뮬레이션 대비..
public class Simulation extends Activity {
    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    TextView textViewR_1, textViewR_2, textViewR_3, textViewR_4, textViewR_5;
    TextView textViewL_1, textViewL_2, textViewL_3, textViewL_4, textViewL_5;


    JoyStickClass js;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulation);

        textViewR_1 = (TextView)findViewById(R.id.textViewR_1);
        textViewR_2 = (TextView)findViewById(R.id.textViewR_2);
        textViewR_3 = (TextView)findViewById(R.id.textViewR_3);
        textViewR_4 = (TextView)findViewById(R.id.textViewR_4);
        textViewR_5 = (TextView)findViewById(R.id.textViewR_5);

        layout_joystick = (RelativeLayout)findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext()
                , layout_joystick, R.drawable.image_button);
        js.setStickSize(80, 80);
        js.setLayoutSize(400, 400);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);

        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textViewR_1.setText("X : " + String.valueOf(js.getX()));
                    textViewR_2.setText("Y : " + String.valueOf(js.getY()));
                    textViewR_3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textViewR_4.setText("Distance : " + String.valueOf(js.getDistance()));

                    int direction = js.get8Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        textViewR_5.setText("Direction : Up");
                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textViewR_5.setText("Direction : Up Right");
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textViewR_5.setText("Direction : Right");
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textViewR_5.setText("Direction : Down Right");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textViewR_5.setText("Direction : Down");
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textViewR_5.setText("Direction : Down Left");
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textViewR_5.setText("Direction : Left");
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textViewR_5.setText("Direction : Up Left");
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textViewR_5.setText("Direction : Center");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textViewR_1.setText("X :");
                    textViewR_2.setText("Y :");
                    textViewR_3.setText("Angle :");
                    textViewR_4.setText("Distance :");
                    textViewR_5.setText("Direction :");
                }
                return true;
            }
        });
    }
}
