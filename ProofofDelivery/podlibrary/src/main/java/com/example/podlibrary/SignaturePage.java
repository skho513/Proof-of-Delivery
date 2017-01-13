package com.example.podlibrary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalamove.drawingview.DrawingView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public class SignaturePage extends AppCompatActivity implements MyButton.ButtonListener{
    public static final String TAG = SignaturePage.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String SER_KEY = "com.example.proofofdelivery.ser";
    private DrawingView drawingView;
    static boolean active = false;
    String myLog = "myLog";

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;

    private Button clearBtn;
    private Button saveBtn;
    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File file = new File(path, "podSignature.png");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.podlibrary.R.layout.signaturepage);

        setTitle("Collect Signature");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawingView = (DrawingView) findViewById(com.example.podlibrary.R.id.drawingView);
        drawingView.setDrawingCacheEnabled(true);

        saveBtn = (Button) findViewById(R.id.btnConfirm);
        clearBtn = (Button) findViewById(R.id.clearBtn);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        }

    /**
     * Defines actions after the back button on the action bar is pressed
     * @param item the item on the action bar that is pressed
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Saves a signature from {@link DrawingView} canvas to the file
     */
    private void saveSignature() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(SignaturePage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignaturePage.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(SignaturePage.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        //final Bitmap bitmap = drawingView.getDrawingCache();
        final Bitmap signatureBitmap = getBitmapFromView(drawingView);
        //final File file = new File("/DCIM/podSignature.png");
        FileOutputStream ostream = null;
       // File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File file = new File(path, "podSignature.png");

        try {
            if ((!file.exists() || file.delete()) /*&& file.createNewFile()*/) {
                ostream = new FileOutputStream(file);
                Bitmap signatureSize = Bitmap.createScaledBitmap(signatureBitmap, 600, 800, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                signatureSize.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                file.createNewFile();
                ostream.write(bytes.toByteArray());
            } else {
                Log.w(TAG, "Cannot create a file");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while writing a file", e);
        } finally {
            DataUtil.closeSilently(ostream);
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    public void onSave(View view) {
        try {
            saveSignature();
          //  new WaitingIcon().execute();
            onUpload();
            Log.d(TAG, saveBtn.getText() + " is successfully pressed and saved.");
        } catch (Exception e) {
            Log.e(TAG, saveBtn.getText() + " is failed to save.");
        }
    }

    @Override
    public void onClear(View view) {
        try {
            drawingView.clearView();
            Log.d(TAG, clearBtn.getText() + " is successfully pressed and cleared.");
        } catch (Exception e) {
            Log.e(TAG, clearBtn.getText() + " is failed to clear.");
        }
    }

    @Override
    public void onCancel(View view) {
        onStop();
    }

    public void onStart() {
        super.onStart();
        active = true;
        Log.d(TAG, "Signature Page has been successfully loaded.");
    }

    public void onStop() {
        super.onStop();
        active = false;
        Log.e(TAG, "Signature Page has stopped");
    }

    public void onUpload(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Service service = new Retrofit.Builder().baseUrl("http://10.10.8.143:8085").client(client).build().create(Service.class);

       // File file = new File(path, "podSignature.png");

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        Call<ResponseBody> req = service.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Do Something
                Log.d(TAG,"Signature Page has been successfully uploaded to server");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
//        JsonParser jsonParser = new JsonParser();
//        JsonObject jsonFile = (JsonObject) jsonParser.parse(loadJSONFromAsset());
//
//      //  File f = new File(path);
//        Ion.with(SignaturePage.this)
//                .load("http://10.10.8.143:8085/upload")
//                .setJsonObjectBody(jsonFile )
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        Log.d(TAG,"Signature Page has been successfully uploaded to server");
//                    }
//                });
    }

    interface Service {
        @Multipart
        @POST("/upload")
        Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);
    }

//    public String loadJSONFromAsset() {
//        String json = null;
//        InputStream is = null;
//        try {
//            is = getAssets().open("config.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        } finally {
//            DataUtil.closeSilently(is);
//        }
//        return json;
//    }

    private class WaitingIcon extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            saveBtn.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            saveBtn.setEnabled(true);
        }

        @Override
            protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 2; i++) {
                    Log.d(myLog, "Saving signature in png... " + i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}