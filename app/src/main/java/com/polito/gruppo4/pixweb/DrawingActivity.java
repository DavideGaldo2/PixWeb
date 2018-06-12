package com.polito.gruppo4.pixweb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * This is demo code to accompany the Mobiletuts+ tutorial series:
 * - Android SDK: Create a Drawing App
 *
 * Sue Smith
 * August 2013
 *
 */
public class DrawingActivity extends AppCompatActivity implements OnClickListener {

    //custom drawing view
    private DrawingView drawView;
    //buttons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, openGalleryBtn, previewBtn;
    //sizes
    private float smallBrush, mediumBrush, largeBrush;

    //Used for accessing the gallery
    public static final int PICK_IMAGE = 1;
    //Used to preview a draw
    public static final int PREVIEW_DRAW = 2;
    //Used to preview an Image from the gallery
    public static final int PREVIEW_IMAGE = 3;
    //Used to modify a draw
    public static final int MODIFY_DRAW = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        //get drawing view
        drawView = (DrawingView)findViewById(R.id.drawing);

        //get the palette and first color button
        //LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        //currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint = (ImageButton)findViewById(R.id.red_color);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(mediumBrush);

        //erase button
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //Gallery Button
        openGalleryBtn = (ImageButton)findViewById(R.id.open_gallery);
        openGalleryBtn.setOnClickListener(this);

        //preview button
        previewBtn = (ImageButton)findViewById(R.id.preview);
        previewBtn.setOnClickListener(this);

        //Se l'activity è tornata per effettuare modificare lo devo caricare nell'area di disegno
        if(getIntent().getBooleanExtra("modify",false)){
            byte[] byteArray = getIntent().getByteArrayExtra("pixelPrew");
            Bitmap bmpToModify = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            drawView.setDrawingCacheEnabled(true);

        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 */
    //user clicked paint
    public void paintClicked(View view){
        //use chosen color

        //set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }

        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }

        else if(view.getId()==R.id.open_gallery){
            //Open Gallery to chose existing image
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }

        else if (view.getId() == R.id.preview) {

            //save drawing
            drawView.setDrawingCacheEnabled(true);
            int w = drawView.getDrawingCache().getWidth();
            int h = drawView.getDrawingCache().getHeight();
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
            Bitmap originalBmp = Bitmap.createBitmap(w, h, conf);
            originalBmp = drawView.getDrawingCache();

            //Convert to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            originalBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            drawView.setDrawingCacheEnabled(false);

            //Passing the original draw to preview activity
            Intent i = new Intent(getApplicationContext(), PreviewActivity.class);
            i.putExtra("pixelPrew", byteArray);
            i.putExtra("draw",true); //Impostato a true se è stato inviato un disegno, a false se galleria
            startActivityForResult(i,PREVIEW_DRAW);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {

            if(resultCode==RESULT_OK){ //Se è stata selezionata una immagine
                Uri selectedImage = data.getData();
                Bitmap galleryBitmap = null;
                try {
                    galleryBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap pixelizedBmp = galleryBitmap;

                //Pixelize the image
                Pixelizer pixObj = new Pixelizer();
                pixelizedBmp = pixObj.getPixelizedBitmap(galleryBitmap);

                //Convert to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pixelizedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //Passing the preview to new activity
                Intent i = new Intent(getApplicationContext(), PreviewActivity.class);
                i.putExtra("pixelPrew", byteArray);
                i.putExtra("draw",false); //impostato a false per dire che è stata inviata una foto dalla galleria
                startActivity(i);
            }
        }
        else if(requestCode==PREVIEW_DRAW){
            if(resultCode==RESULT_OK){
                drawView.startNew();
            }
        }
    }

}
