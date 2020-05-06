package com.example.cvtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    /*declaração de variáveis*/
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Mat img1,submat;
    String coordTexto = "";
    Point finalLinha = new Point();
    boolean flag_ok_linhas = false;
    List<Point> coordLinhas = new ArrayList<Point>();


    /*Definindo o comportamento ao toque*/
    View.OnTouchListener handleTouch = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    finalLinha.x = x;
                    finalLinha.y = y - 180;

                    break;
                case MotionEvent.ACTION_UP:
                    coordLinhas.add(new Point(x,y - 180)); /*Ajuste da coordeanda vertical, de forma a ficar na janela da imagem*/
                    break;
            }
            return true;
        }
    };
    /*Fim da definição do comportamento ao toque*/

    /*Fim declaração de variaveis*/

    void imprimirLinha(Point ponto_inicial,Point ponto_final, Mat imagem){
        Imgproc.line(imagem,ponto_inicial,ponto_final,new Scalar(255,255,0));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        /*Solicitando a permissao da camera*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        //Habilitando a camera
        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.myCameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        /*Habilitando a obseravação de toques na superficie da câmera*/
        cameraBridgeViewBase.setOnTouchListener(handleTouch);

        /*botôes para confirmar as linhas*/
        final Button Bt_confirm_coord = (Button) findViewById(R.id.Confirm_coord);
        final Button Bt_remove_coord = (Button) findViewById(R.id.remove_coord);

        Bt_confirm_coord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bt_confirm_coord.setVisibility(View.INVISIBLE);
                Bt_remove_coord.setVisibility(View.INVISIBLE);
                flag_ok_linhas = true;
            }
        });
        Bt_remove_coord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordLinhas.clear();
            }
        });
        /*fim funções bototes*/

        if(OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Deu certo!!!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Deu errado!!!",Toast.LENGTH_SHORT).show();
        }

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status){
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;

                }


            }
        };
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        img1 = new Mat();
        submat = new Mat();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        img1 = inputFrame.gray();
        int nPontos;
        Core.flip(img1.t(),img1,1);
        if(!coordLinhas.isEmpty()){
            if(!flag_ok_linhas){
                for (nPontos=0;nPontos<coordLinhas.size();nPontos++) {
                    Imgproc.circle(img1, coordLinhas.get(nPontos), 1, new Scalar(255, 152, 22));
                }
                if (coordLinhas.size() % 2 == 0) {
                    imprimirLinha(coordLinhas.get(nPontos - 1), coordLinhas.get(nPontos - 2), img1);
                } else {
                    imprimirLinha(coordLinhas.get(nPontos - 1), finalLinha, img1);
                }
            }
            else{
                for(int i=0;i<coordLinhas.size();i++){
                    
                }
            }

        }
        //Imgproc.putText(img1,coordTexto,new Point(100,100),Core.FONT_ITALIC,2.0,new Scalar(255,255,0));

        return img1;
    }

    @Override
    public void onCameraViewStopped() {
        img1.release();
        submat.release();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Erro ao abrir o OpenCV", Toast.LENGTH_SHORT).show();
        }
        else{
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cameraBridgeViewBase!=null) {
            cameraBridgeViewBase.disableView();
            img1.release();
            submat.release();
        }
    }
}
