package com.example.kimseungchul.wifiscantest;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    int flag = 0;
    int flag_l = 0;


    RelativeLayout layout_joystickR, layout_joystickL;
    ImageView image_joystick, image_border;
    TextView textViewR_1, textViewR_2, textViewR_3, textViewR_4, textViewR_5;
    TextView textViewL_1, textViewL_2, textViewL_3, textViewL_4, textViewL_5;

    JoyStickClass js_r, js_l;

    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;
    String data;


    String js_rC ;
    String js_rS ;
    String js_rD ;
    String js_lC ;
    String js_lS ;
    String js_lD ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonmap);

        onHandler1();

        layout_joystickR = (RelativeLayout)findViewById(R.id.layout_joystickR);
        layout_joystickL = (RelativeLayout)findViewById(R.id.layout_joystickL);
        joystick_func();
        Thread worker = new Thread() {
            public void run() {
                try {
                    socket = new Socket("192.168.1.7", 5555);//집
                    //socket = new Socket("172.16.101.73", 5555);//somacenter
                    //socket = new Socket("192.168.4.1", 3002);//banana
                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    while (true) {
                        data = socket_in.readLine();
//                        output.post(new Runnable() {
//                            public void run() {
//                                output.setText(data);
//                            }
//                        });
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


       // load GPS( GPS 로 부터 현재의 내 위치값 가져오기 )
        setUpMapIfNeeded();

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


//                js_rA = Double.toString(js_r.getAngle());
//                js_rD = Double.toString(js_r.getDistance());

                int rd = (int)js_r.getDistance();
                int ld = (int)js_l.getDistance();


//                double cos = Math.cos(js_r.getAngle()) * 50 * rd;
//                double sin = Math.sin(js_r.getAngle()) * 50 * rd;

                double cos = Math.cos(Math.toRadians(js_r.getAngle())) * 50 * rd;
                double sin = Math.sin(Math.toRadians(js_r.getAngle())) * 50 * rd;

                double cos_l = Math.cos(Math.toRadians(js_l.getAngle())) * 50 * ld;
                double sin_l = Math.sin(Math.toRadians(js_l.getAngle())) * 50 * ld;

//                int rs = (int)sin;
//                int rc = (int)cos;

                int rs = (int)sin + 150;
                int rc = (int)cos + 150;

                int ls = (int)sin_l + 150;
                int lc = (int)cos_l + 150;


                js_rS = String.valueOf(rs);
                js_rC = String.valueOf(rc);
                js_rD = String.valueOf(rd);

                js_lS = String.valueOf(ls);
                js_lC = String.valueOf(lc);
                js_lD = String.valueOf(ld);

                if(flag == 1) {
                    socket_out.println("S" + js_rS + "*" + "C" + js_rC + "*");
                }
                if(flag_l == 1){
                    socket_out.println("H" + js_lS + "*" + "Y" + js_lC + "*");
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

    public void joystick_func(){


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
                    flag = 1;

//                    socket_out.println("Angle2 : " + String.valueOf(js_r.getAngle()));
//                    socket_out.println("Distance2 : " + String.valueOf(js_r.getDistance()));
                    float js_rA = js_r.getAngle();
                    float js_rD = js_r.getDistance();
//                    socket_out.println("A2:" + (int) js_rA);
//                    socket_out.println("D2:" + (int) js_rD);


                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {


                    flag = 0;
                    socket_out.println("S150" + "*" + "C150" + "*");


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
                    flag_l = 1;


                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {

//                    socket_out.println("A1:" + 0);
//                    socket_out.println("D1:" + 0);

                    flag_l = 0;
                    socket_out.println("H150" + "*" + "Y150" + "*");

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

                    //다이얼로그 띄우기
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MapsActivity.this);
                    alert_confirm.setMessage("최종지점으로 지정하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                    flag = 1;


                                    return;
                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'
                                    flag = 0;
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                   // alert.show();



                    MarkerOptions markerOptions = new MarkerOptions();
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.soma));
//                    if(flag == 0) {
//
//                        markerOptions.position(latLng); //마커위치설정
//                        //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));   // 마커생성위치로 이동
//                        mMap.addMarker(markerOptions); //마커 생성
//                        flag = 1;
//                        Log.i("tag", "클릭한 지점:" + latLng);
//                    }
                        //flag = 0;

                       // if(flag == 1) {
                            mMap.clear();
                            //현재위치
                    //mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).position(new LatLng(latLng_s.latitude, latLng_s.longitude)).title("현재위치입니다.."));


                    GpsInfo gps = new GpsInfo(MapsActivity.this);
                    // GPS 사용유무 가져오기
                    if (gps.isGetLocation()) {
                        Log.d("connect", "성공.....");

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // Creating a LatLng object for the current location
                        LatLng latLng_s = new LatLng(latitude, longitude);

                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).position(new LatLng(latLng_s.latitude, latLng_s.longitude)).title("현재위치입니다.."));


                    }

                    Toast.makeText(getApplication(), "도착 지점이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                            //랜스값 찍고.
                            markerOptions.position(latLng); //마커위치설정

                    mMap.addMarker(markerOptions); //마커 생성

                    int lati_hour = (int)latLng.latitude;
                    double lati_min = (latLng.latitude - lati_hour) * 60;
                    String lati_hour_str = String.valueOf(lati_hour);
                    String lati_min_str = String.valueOf(lati_min);


                    int long_hour = (int)latLng.longitude;
                    double long_min = (latLng.longitude - long_hour) * 60;
                    String long_hour_str = String.valueOf(long_hour);
                    String long_min_str = String.valueOf(long_min);


                    Log.d("위도경도 반환", "위도"+lati_hour_str + lati_min_str + "   경도" + long_hour_str + long_min_str);

                    //위도경도를 GPAGA로 변환한 값.
                    //socket_out.println("latitude" + lati_hour_str + lati_min_str + "*" + "longitude" + long_hour_str + long_min_str + "*");

                    //socket_out.println("LastLocation : " + latLng.latitude + " / " + latLng.longitude);
                            flag = 0;
                      //  }

//                        else{
//
//                        }

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
        GpsInfo gps = new GpsInfo(MapsActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            Log.d("connect", "성공.....");

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

// Creating a LatLng object for the current location
            LatLng latLng_s = new LatLng(latitude, longitude);

            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).position(new LatLng(latLng_s.latitude, latLng_s.longitude)).title("현재위치입니다.."));
            LatLng startingPoint = new LatLng(latLng_s.latitude, latLng_s.longitude);


             //   socket_out.println("CurrentLocation : " + latLng_s.latitude + " / " + latLng_s.longitude);
           // socket_out.println("CurrentLocation : " + latLng_s.latitude + "/" + latLng_s.longitude);

            Log.d("currentlocationtest", latLng_s.latitude + " / " + latLng_s.longitude + "result" + latLng_s);


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));




        }
    else {
            //Log.d("Connect", "sdnklasdnkldsa늬의의의느이ㅡ이능ㅇ!@@@@@@@@@@@"+location);

            //여긴 현재위치 찍어야해
//            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.soma)).position(new LatLng(location.getLatitude(), location.getLongitude())).title("현재위치입니다.."));
//            LatLng startingPoint = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).position(new LatLng(37.503946, 127.044800)).title("현재위치입니다.."));
            LatLng startingPoint = new LatLng(37.503946, 127.044800);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint, 16));

            Log.d("notConnect", "잘못걸렷네..");

            gps.showSettingsAlert();

        }



    }





}
