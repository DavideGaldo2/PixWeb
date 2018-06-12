package com.polito.gruppo4.pixweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by ilariaginepro on 21/08/17.
 * Mod by Davide Galdo on 15/09/17
 */

public class PreviewActivity extends AppCompatActivity implements OnClickListener {

    private ImageButton modificaBtn, tickBtn;
    private ImageView pixImagePreview;


    private boolean draw;

    private int delayTime=5;
    private com.polito.gruppo4.pixweb.Color ringColor1= new com.polito.gruppo4.pixweb.Color(255,0,0);
    private com.polito.gruppo4.pixweb.Color ringColor2= new com.polito.gruppo4.pixweb.Color(0,255,0);
    private com.polito.gruppo4.pixweb.Color ringColor3= new com.polito.gruppo4.pixweb.Color(0,0,255);


    public static JSONArray pixels_array;
    private JSONArray displayPixelsArray;
    private Handler mMainHandler;
    public Handler mNetworkHandler;
    private NetworkThread mNetworkThread = null;



    //Controllori della ragnatela
    private LedsWebController wire1Controller;
    private LedsWebController wire2Controller;
    private LedsWebController wire3Controller;
    private LedsWebController wire4Controller;
    private LedsWebController wire5Controller;
    private LedsWebController firstRingController;
    private LedsWebController secondRingController;
    private LedsWebController thirdRingController;

    Bitmap pixelizedBmp;
    Bitmap originalBmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        modificaBtn = (ImageButton)findViewById(R.id.modifica);
        tickBtn= (ImageButton)findViewById(R.id.tick);
        pixImagePreview = (ImageView)findViewById(R.id.imagePreview);
        modificaBtn.setOnClickListener(this);
        tickBtn.setOnClickListener(this);

        draw=getIntent().getBooleanExtra("draw",false);
        byte[] byteArray = getIntent().getByteArrayExtra("pixelPrew");
        originalBmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        pixelizedBmp = originalBmp;

        //I disegni vengono passati non pixelati quindi vanno pixelati, le foto
        //da galleria vengono passate pixelate
        if(draw){
            //Pixelize the image
            Pixelizer pixObj = new Pixelizer();
            pixelizedBmp = pixObj.getPixelizedBitmap(originalBmp);
        }

        pixImagePreview.setImageBitmap(pixelizedBmp);
        pixels_array=preparePixelsArray();


        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
            }
        };

        startHandlerThread();


        //Inizializzazione dei thread dei vettori
        if(wire1Controller==null)
            wire1Controller = new LedsWebController(pixels_array,mNetworkHandler,1);
        if(wire2Controller==null)
            wire2Controller = new LedsWebController(pixels_array,mNetworkHandler,2);
        if(wire3Controller==null)
            wire3Controller = new LedsWebController(pixels_array,mNetworkHandler,3);
        if(wire4Controller==null)
            wire4Controller = new LedsWebController(pixels_array,mNetworkHandler,4);
        if(wire5Controller==null)
            wire5Controller = new LedsWebController(pixels_array,mNetworkHandler,5);
        if(firstRingController ==null)
            firstRingController = new LedsWebController(pixels_array,mNetworkHandler,6);
        if(secondRingController==null)
            secondRingController = new LedsWebController(pixels_array,mNetworkHandler,7);
        if(thirdRingController ==null)
            thirdRingController = new LedsWebController(pixels_array,mNetworkHandler,8);


        if(!wire1Controller.isAlive())
            wire1Controller.start();
        if(!wire2Controller.isAlive())
            wire2Controller.start();
        if(!wire3Controller.isAlive())
            wire3Controller.start();
        if(!wire4Controller.isAlive())
            wire4Controller.start();
        if(!wire5Controller.isAlive())
            wire5Controller.start();
        if(!firstRingController.isAlive())
            firstRingController.start();
        if(!secondRingController.isAlive())
            secondRingController.start();
        if(!thirdRingController.isAlive())
            thirdRingController.start();

    }

    public void onClick(View view) {
        if (view.getId() == R.id.modifica) {
            //Ritorna alla draw activity e rispedisce il disegno da modificare
            setResult(RESULT_CANCELED);
            finish();


        } else if (view.getId() == R.id.tick) {

            //AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);

            //1 Avviare animazioni della ragnatela
            //2 Salvare il file sulla memoria interna del dispositivo
            //3 Inviare l'immagine alla ragnatela
            //4 Concludere attendendo e tornando alla scermata di disegno


            String imgSaved = MediaStore.Images.Media.insertImage(
                    getContentResolver(), originalBmp,
                    UUID.randomUUID().toString()+".bmp", "drawing");

            //feedback
            if(imgSaved!=null){
                Toast savedToast = Toast.makeText(getApplicationContext(),
                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else{
                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }


            /////////////////////////////////////////////
            int r,g,b;
            Pixelizer pixObj= new Pixelizer();

            Bitmap def= pixObj.getResizedBitmap(originalBmp,  32,  32);


            int x = (def.getWidth());
            int y = (def.getHeight());
            int[] intArray = new int[x * y];
            def.getPixels(intArray, 0, x, 0, 0, x, y);

            try {
                displayPixelsArray = prepareDisplayPixelsArray();
                for (int i = 0; i < intArray.length; i++) {
                    r = android.graphics.Color.red(intArray[i]);
                    g = android.graphics.Color.green(intArray[i]);
                    b = android.graphics.Color.blue(intArray[i]);
                    ((JSONObject) displayPixelsArray.get(i)).put("r", r);
                    ((JSONObject) displayPixelsArray.get(i)).put("g", g);
                    ((JSONObject) displayPixelsArray.get(i)).put("b", b);
                }

            } catch (JSONException e) {
                // There should be no Exception
            }
            handleNetworkRequest(NetworkThread.SET_DISPLAY_PIXELS, displayPixelsArray, 0 ,0);

            /////////////////////////////////////////////


            //Attendo qualche secondo
            for(int i=0;i<512;i++){
                this.WaitingWebAnimation();
                try {
                    Thread.sleep((long) (Math.random()*50));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.RingAnimation();

            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }






            //Una volta che il disegno o immagine è stato spedito restituisco il controllo alla
            //Activity di disegno
            setResult(RESULT_OK);
            finish();


        }
    }



    //David Editing of the PreparePixelArray
    JSONArray prepareDisplayPixelsArray(){
        JSONArray pixels_array = new JSONArray();
        JSONObject tmp;
        try {
            for (int i = 0; i < 1024; i++) {
                tmp = new JSONObject();
                tmp.put("a", 255);
                tmp.put("g", 0);
                tmp.put("b", 0);
                tmp.put("r", 0);
                pixels_array.put(tmp);
            }
        } catch (JSONException exception) {
            // No errors expected here
        }
        return pixels_array;

    }

    JSONArray preparePixelsArray() {
        JSONArray pixels_array = new JSONArray();
        JSONObject tmp;
        try {
            for (int i = 0; i < 1072; i++) {
                tmp = new JSONObject();
                tmp.put("a", 0);
                tmp.put("g", 0);
                tmp.put("b", 0);
                tmp.put("r", 0);
                pixels_array.put(tmp);
            }
        } catch (JSONException exception) {
            // No errors expected here
        }
        return pixels_array;
    }

    public void startHandlerThread() {
        mNetworkThread = new NetworkThread(mMainHandler);
        mNetworkThread.start();
        mNetworkHandler = mNetworkThread.getNetworkHandler();
    }

    private void handleNetworkRequest(int what, Object payload, int arg1, int arg2) {
        Message msg = mNetworkHandler.obtainMessage();
        msg.what = what;
        msg.obj = payload;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.sendToTarget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkThread != null && mNetworkHandler != null) {
            mNetworkHandler.removeMessages(mNetworkThread.SET_PIXELS);
            mNetworkHandler.removeMessages(mNetworkThread.SET_DISPLAY_PIXELS);
            mNetworkHandler.removeMessages(mNetworkThread.SET_SERVER_DATA);
            mNetworkThread.quit();
            try {
                mNetworkThread.join(100);
            } catch (InterruptedException ie) {
                throw new RuntimeException(ie);
            } finally {
                mNetworkThread = null;
                mNetworkHandler = null;
            }
        }
    }

    private void WaitingWebAnimation(){


        this.wire1Controller.insertInWire(255,0,0);
        this.wire2Controller.insertInWire(0,255,0);
        this.wire3Controller.insertInWire(0,0,255);
        this.wire4Controller.insertInWire(255,255,0);
        this.wire5Controller.insertInWire(0,255,255);
    }

    public void RingAnimation(){
        this.firstRingController.setRingColorAndPulse(ringColor1,5);
        this.secondRingController.setRingColorAndPulse(ringColor2,10);
        this.thirdRingController.setRingColorAndPulse(ringColor3,15);
    }




    private void drawOnDisplay(Bitmap bmp){

        int r,g,b;
        Pixelizer pixObj= new Pixelizer();

        Bitmap def= pixObj.getResizedBitmap(bmp,  32,  32);

        /*
        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        def.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        //Passo il dato dentro una bitmapfactory per impostare opzione
        //InScaled=false
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        def=BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
*/
        int x = (def.getWidth());
        int y = (def.getHeight());
        int[] intArray = new int[x * y];
        def.getPixels(intArray, 0, x, 0, 0, x, y);

        try {
            displayPixelsArray = prepareDisplayPixelsArray();
            for (int i = 0; i < intArray.length; i++) {
                r = android.graphics.Color.red(intArray[i]);
                g = android.graphics.Color.green(intArray[i]);
                b = android.graphics.Color.blue(intArray[i]);
                ((JSONObject) displayPixelsArray.get(i)).put("r", r);
                ((JSONObject) displayPixelsArray.get(i)).put("g", g);
                ((JSONObject) displayPixelsArray.get(i)).put("b", b);
            }
            handleNetworkRequest(NetworkThread.SET_DISPLAY_PIXELS, displayPixelsArray, 0 ,0);
        } catch (JSONException e) {
            // There should be no Exception
        }
    }
}







