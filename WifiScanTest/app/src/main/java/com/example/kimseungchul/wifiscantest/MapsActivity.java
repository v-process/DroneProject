package com.example.kimseungchul.wifiscantest;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public int flag = 0;


    RelativeLayout layout_joystickR, layout_joystickL;
    ImageView image_joystick, image_border;
    TextView textViewR_1, textViewR_2, textViewR_3, textViewR_4, textViewR_5;
    TextView textViewL_1, textViewL_2, textViewL_3, textViewL_4, textViewL_5;

    JoyStickClass js_r, js_l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonmap);
        setUpMapIfNeeded();

        textViewR_1 = (TextView)findViewById(R.id.textViewR_1);
        textViewR_2 = (TextView)findViewById(R.id.textViewR_2);
        textViewR_3 = (TextView)findViewById(R.id.textViewR_3);
        textViewR_4 = (TextView)findViewById(R.id.textViewR_4);
        textViewR_5 = (TextView)findViewById(R.id.textViewR_5);
        textViewL_1 = (TextView)findViewById(R.id.textViewL_1);
        textViewL_2 = (TextView)findViewById(R.id.textViewL_2);
        textViewL_3 = (TextView)findViewById(R.id.textViewL_3);
        textViewL_4 = (TextView)findViewById(R.id.textViewL_4);
        textViewL_5 = (TextView)findViewById(R.id.textViewL_5);

        layout_joystickR = (RelativeLayout)findViewById(R.id.layout_joystickR);
        layout_joystickL = (RelativeLayout)findViewById(R.id.layout_joystickL);

        js_r = new JoyStickClass(getApplicationContext()
                , layout_joystickR, R.drawable.image_button);


        js_r.setStickSize(80, 80);
        js_r.setLayoutSize(400, 400);
        js_r.setLayoutAlpha(150);
        js_r.setStickAlpha(100);
        js_r.setOffset(90);
        js_r.setMinimumDistance(50);
        layout_joystickR.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_r.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textViewR_1.setText("X : " + String.valueOf(js_r.getX()));
                    textViewR_2.setText("Y : " + String.valueOf(js_r.getY()));
                    textViewR_3.setText("Angle : " + String.valueOf(js_r.getAngle()));
                    textViewR_4.setText("Distance : " + String.valueOf(js_r.getDistance()));

                    int direction = js_r.get8Direction();
                    if (direction == JoyStickClass.STICK_UP) {
                        textViewR_5.setText("Direction : Up");
                    } else if (direction == JoyStickClass.STICK_UPRIGHT) {
                        textViewR_5.setText("Direction : Up Right");
                    } else if (direction == JoyStickClass.STICK_RIGHT) {
                        textViewR_5.setText("Direction : Right");
                    } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textViewR_5.setText("Direction : Down Right");
                    } else if (direction == JoyStickClass.STICK_DOWN) {
                        textViewR_5.setText("Direction : Down");
                    } else if (direction == JoyStickClass.STICK_DOWNLEFT) {
                        textViewR_5.setText("Direction : Down Left");
                    } else if (direction == JoyStickClass.STICK_LEFT) {
                        textViewR_5.setText("Direction : Left");
                    } else if (direction == JoyStickClass.STICK_UPLEFT) {
                        textViewR_5.setText("Direction : Up Left");
                    } else if (direction == JoyStickClass.STICK_NONE) {
                        textViewR_5.setText("Direction : Center");
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    textViewR_1.setText("X :");
                    textViewR_2.setText("Y :");
                    textViewR_3.setText("Angle :");
                    textViewR_4.setText("Distance :");
                    textViewR_5.setText("Direction :");
                }
                return true;
            }
        });

        js_l = new JoyStickClass(getApplicationContext()
                , layout_joystickL, R.drawable.image_button);

        js_l.setStickSize(80, 80);
        js_l.setLayoutSize(400, 400);
        js_l.setLayoutAlpha(150);
        js_l.setStickAlpha(100);
        js_l.setOffset(90);
        js_l.setMinimumDistance(50);
        layout_joystickL.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js_l.drawStick(arg1);
                if (arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textViewL_1.setText("X : " + String.valueOf(js_l.getX()));
                    textViewL_2.setText("Y : " + String.valueOf(js_l.getY()));
                    textViewL_3.setText("Angle : " + String.valueOf(js_l.getAngle()));
                    textViewL_4.setText("Distance : " + String.valueOf(js_l.getDistance()));

                    int direction = js_l.get8Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        textViewL_5.setText("Direction : Up");
                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textViewL_5.setText("Direction : Up Right");
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textViewL_5.setText("Direction : Right");
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textViewL_5.setText("Direction : Down Right");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textViewL_5.setText("Direction : Down");
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textViewL_5.setText("Direction : Down Left");
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textViewL_5.setText("Direction : Left");
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textViewL_5.setText("Direction : Up Left");
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textViewL_5.setText("Direction : Center");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textViewL_1.setText("X :");
                    textViewL_2.setText("Y :");
                    textViewL_3.setText("Angle :");
                    textViewL_4.setText("Distance :");
                    textViewL_5.setText("Direction :");
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();


            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {


                    MarkerOptions markerOptions = new MarkerOptions();
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.soma));
                    //if(flag == 0) {

                        markerOptions.position(latLng); //마커위치설정
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));   // 마커생성위치로 이동
                        mMap.addMarker(markerOptions); //마커 생성
                        flag = 1;
                        Log.i("tag", "클릭한 지점:" + latLng);
//                    }
//                    else{
//                        mMap.clear();
//                        //현재위치 가져와서 찍고
//                        //랜스값 찍고.
//                        flag = 0;
//                    }
//
                }
            });

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();

            }
        }
    }



    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(37.503946, 127.044800)).title("SoMaCenter입니다."));
        LatLng startingPoint = new LatLng(37.503946, 127.044800);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));
    }
}
