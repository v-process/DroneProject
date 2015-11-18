package com.example.kimseungchul.wifiscantest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by kimseungchul on 15. 9. 16..
 */
public class APConnect extends Activity{

    String ssid;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apconnect);



    }


    public void flight_func(View view) {

//        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(APConnect.this);
//        alert_confirm.setMessage("GPS 설정창으로 이동하시겠습니까?").setCancelable(false).setPositiveButton("확인",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 'YES'
//                    }
//                }).setNegativeButton("취소",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 'No'
//                        return;
//                    }
//                });
//        AlertDialog alert = alert_confirm.create();
//        alert.show();


        try{

            WifiConfiguration wfc = new WifiConfiguration();

            wfc.SSID = "\"".concat("SoMa Center").concat("\"");
            wfc.status = WifiConfiguration.Status.DISABLED;
            wfc.priority = 40;

            //WPA/WPA2
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wfc.preSharedKey = "\"".concat("").concat("\"");



            WifiManager wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wfMgr.getConnectionInfo();
            Log.i("tag", "와이파이 이름은?? " + wInfo.getSSID());

            List<WifiConfiguration> configList = wfMgr.getConfiguredNetworks();

            boolean isConfigured = false;

            int networkId = -1;

//            //설정된 config 불러와서 있으면 network id를 가져옵니다.
//            for(WifiConfiguration wifiConfig : configList){
//                Log.d("tag", "wifiConfig.SSID:" + wifiConfig.SSID);
//                if(wifiConfig.SSID.equals(wfc.SSID)){
//                    networkId = wifiConfig.networkId;
//                    ssid = wifiConfig.SSID;
//                    Log.i("tag", "found network id:"+ssid);
//                    isConfigured = true;
//                    break;
//                }
//            }
//
//            // 설정되지 않았다면 wfc 값으로 설정하여 추가합니다.
//
//            if (!isConfigured) {
//                networkId = wfMgr.addNetwork(wfc);
//
//            // 설정한 값을 저장합니다.
//                wfMgr.saveConfiguration();
//            }



 //           Log.i("tag", "networkId:"+networkId);

            // 연결하면 true로 리턴되는데 실제로는 연결이 안되어 있습니다.
            if (wInfo.getSSID().equals(wfc.SSID)) {
                Log.i("tag", "is connect:"+wfMgr.enableNetwork(networkId, false));
                Toast.makeText(this, "성공입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(APConnect.this, MapsActivity.class);
                startActivity(intent);
                finish();

            }
            else{
                Toast.makeText(this, "드론 AP에 접속해주세요.", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Log.e("tag", e.getMessage(), e);
        }
    }
}