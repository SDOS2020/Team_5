package org.emberon.winscan.notifications;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.emberon.winscan.R;
import org.emberon.winscan.util.Utils;

import java.io.IOException;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
        WebView mywebview;
//    SurfaceView sf;
//    CameraSource cs;
//    BarcodeDetector bd;
//    TextView tv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        //TODO: Remove QR code scanner
//        sf = root.findViewById(R.id.surfaceView);
//        tv = root.findViewById(R.id.textView);
//        bd = new BarcodeDetector.Builder(root.getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
//        cs = new CameraSource.Builder(root.getContext(), bd).setRequestedPreviewSize(640, 480).build();
//        sf.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                if (ActivityCompat.checkSelfPermission(root.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                try {
//                    cs.start(holder);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cs.stop();
//            }
//        });
//
//        bd.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                SparseArray<Barcode> qrC = detections.getDetectedItems();
//
//                if (qrC.size() != 0) {
//                    tv.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Vibrator vib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//                            vib.vibrate(1000);
//                            tv.setText(qrC.valueAt(0).displayValue);
//                            Toast.makeText(root.getContext(), qrC.valueAt(0).displayValue, Toast.LENGTH_LONG).show();
//                            cs.stop();
//                        }
//                    });
//
//                }
//            }
//        });
        mywebview = (WebView) root.findViewById(R.id.webView);
        mywebview.setWebViewClient(new MyBrowser());
        mywebview.getSettings().setLoadsImagesAutomatically(true);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mywebview.loadUrl("https://www.zomato.com/");
        return root;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}