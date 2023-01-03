package iss.workshop.android_ca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    EditText mURL; // where URL is placed
    RecyclerView recyclerView;
    TextView textView;
    Button mFetchBtn;
    Button mLeaderBoardBtn;
    File dir;
    String url; // input URL (stocksnap.io)
    File imageURLsFileDir; // not sure what this is for. seems like not used
    List <String> imgURLS = new ArrayList<>(); // list of 20 image Urls from website (eg stocksnap.io)

    List<Uri> uri; // list of 20 Uri referring to 20 jpg images in external storage
    HashMap<String, Integer> leaderBoard;

    // for placement of first 20 blank
    Uri dummy = Uri.parse("https://cdn-icons-png.flaticon.com/512/59/59836.png");
    List<Uri> dummies = new ArrayList<>();

    RecyclerAdapter adapter;
    Context context;
    ProgressBar progressBar;
    TextView downloadProgress;
    private  static  final  int Read_Permission = 101;
    int currentProgress = 0;

    // YT added
    List<Uri> selected_6Uri = new ArrayList<>();
    Button cfmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mURL = findViewById(R.id.urlTxt);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        downloadProgress = findViewById(R.id.downloadProgress);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        dir =cw.getDir("imageDir",Context.MODE_PRIVATE);

        uri = LoadImageUri();

//        // Set layout
//        for(int i=0; i<20;i++){
//            dummies.add(dummy);
//        }
        if(uri.size()==20){
            adapter = new RecyclerAdapter((ArrayList<Uri>) uri, this);        }


            // set 5 x 4
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        recyclerView.setAdapter(adapter);

        // an empty thread. not sure what this is for?
        HomePage hm = new HomePage();
        hm.execute();
        context = this;

        // give permission to access external storage
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
        }

        // listening for fetch button to start download
        mFetchBtn = findViewById(R.id.btnFetch);
        mFetchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                adapter = new RecyclerAdapter((ArrayList<Uri>) uri, MainActivity.this); // empty uri
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
                recyclerView.setAdapter(adapter);

//                uri = new ArrayList<>();
                currentProgress = 0;
                url = mURL.getText().toString();
                if(url == null || !(url.contains("https"))){
                    Toast.makeText(MainActivity.this,"Please Enter Url."
                    ,Toast.LENGTH_SHORT).show();
                }
                else{

                    WebScrape ws = new WebScrape();
                    ws.execute();
                }
            }
        });

        leaderBoard = LeaderBoard.loadLeaderBoard();

        mLeaderBoardBtn = findViewById(R.id.leaderBoard_button);

        if(leaderBoard == null){
            mLeaderBoardBtn.setEnabled(false);
        }
        if(leaderBoard!=null){
            mLeaderBoardBtn.setEnabled(true);
        }

        mLeaderBoardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startleaderBoard(leaderBoard);
            }
        });

        // to set first download progress?
        if(mURL != null){
            SharedPreferences pref =
                    getPreferences(Context.MODE_PRIVATE);
            int download = pref.getInt("downloadProgress",0);
            downloadProgress.setText("Download " +download+ " of 20 images");

        }

        // confirm 6 images button
        cfmBtn = findViewById(R.id.cfm6_button);
        cfmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBtnClicked();
            }
        });
    }

    public class HomePage extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    // Main goal: put 20 image url into global attr imageURLs : List<String>
    public class WebScrape extends AsyncTask<Void,Void,Void>{

        // before download
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                downloadProgress.setVisibility(View.VISIBLE);
                downloadProgress.setText("Downloading");
        }

        // after 20 images have finished downloading
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation
                    (AnimationUtils.loadAnimation
                            (MainActivity.this,
                                    android.R.anim.fade_out));
            downloadProgress.setText("Download Finished");
            adapter.notifyDataSetChanged();

            //DIalog to go to another page
            /*
            AlertDialog.Builder alertBialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBialogBuilder
                    .setMessage(">>>>Ready to Play<<<<")
                    .setCancelable(false)
                    .setPositiveButton("Play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent  = new Intent(MainActivity.this, GamePlaying.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("CHOOSE AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertBialogBuilder.create();
            alertDialog.show();*/


        }

        // webscrape 20 image urls and add to imageURLs
        @Override
        protected Void doInBackground(Void... voids) {
            Document document = null;
            try {
                if(url == null){

                }
                else{
                document = Jsoup.connect(url).get();
                Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif|webp)]");
                int i = 0;

                for (Element image : images) {
//                    String imageUrl = image.attr("data-original");
                    String imageUrl = image.absUrl("src");


                    if(imageUrl.startsWith("https")){
                        // gets list of 20 image urls
                        imgURLS.add(imageUrl);

                        startDownloadImage(imageUrl, i+1);

                        // progress bar
                        currentProgress += 5;
                        progressBar.setProgress(currentProgress);
                        progressBar.setMax(100);
                        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("downloadProgress",i);
                        i++;
                    }

                    if(i>=20){
                        break;
                    }}
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(
                        MainActivity.this,
                        e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
            return null;
        }
    }

    // used in "WebScrape" class
    // Goal: for each image,
    // 1) download from URL -> byte[]
    // 2) store in external folder "app_imageDir -> jpg
    // 3) set image into recyclerView
    protected void startDownloadImage(String imgURL, int num){
        String destFilename =
                String.valueOf(num) + imgURL.lastIndexOf(".")+1;

//        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFilename);

        //create background thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                // download image from Url into byte[]. byte[] contained in destFile (eg. 10641)
                ImageDownloader imgDL = new ImageDownloader();
                if(imgDL.downloadImage(imgURL,destFile)){

                    // if download successful, save image from byte[] to jpg into the same dest folder
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());

                            // byte[] -> jpg
                            Uri imageUri = SaveImageJPG(num,bitmap);

                            // if image is not already set
                            if(!uri.contains(imageUri)){
                                uri.add(imageUri);
                            }
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        }).start();
    }

    protected ArrayList<Uri> LoadImageUri(){
        ArrayList<Uri> loadImageuris = new ArrayList<>();

        for(int i = 1; i<=20; i++){
            File imageFile = new File(dir, "Img" + String.valueOf(i)+".jpg");
            Uri uriImg= Uri.fromFile(imageFile);
            loadImageuris.add(uriImg);
        }
        return loadImageuris;
    }

    // byte[] --> jpg file. Returns Uri of the jpg image
    protected Uri SaveImageJPG(int num, Bitmap bitmap){
        File imageFile = new File(dir, "Img" + String.valueOf(num)+".jpg");
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(imageFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile);
    }

    // for selecting 6 images
    @Override
    public void onImageClick(int position){
        Uri selected_imgUri = uri.get(position);

        if (selected_6Uri.size() < 6 && !selected_6Uri.contains(selected_imgUri)){
            selected_6Uri.add(selected_imgUri);
        }
        else if (selected_6Uri.contains(selected_imgUri)){
            selected_6Uri.remove(selected_imgUri);
        }
        else{
            Toast.makeText(this,"Select 6 only",Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(adapter);

        // Enable or disable "confirm" button
        Button btnSelect = findViewById(R.id.cfm6_button);
        if (selected_6Uri.size() == 6){
            btnSelect.setEnabled(true);
        }
        else{
            btnSelect.setEnabled(false);
        }

        // Indicate how many images are selected
        TextView numSelected_tv = findViewById(R.id.numberOfSelected);
        String numSelected = Integer.valueOf(selected_6Uri.size()).toString();
        numSelected_tv.setText(numSelected + " of 6 selected");
    }

    // save the 6 images into internal storage and launch activity 2
    private void confirmBtnClicked(){
        List<String> files_of_selected6 = new ArrayList<>();

        if (selected_6Uri.size() != 6){
            Toast.makeText(this,"You need to select at least 6",Toast.LENGTH_SHORT).show();
            return;
        }


        // store
        String filePath = "selected_6";
        File mTargetFolder = new File(getFilesDir(), filePath);

        if (!mTargetFolder.exists()){
            mTargetFolder.mkdirs();
        }

        File[] files_inFolder = mTargetFolder.listFiles();

        // Delete previous 6 files in folder if exist
        if (files_inFolder != null || files_inFolder.length > 0 ){
            for (int j=0; j<files_inFolder.length; j++){
                new File(mTargetFolder, files_inFolder[j].getName()).delete();
            }
        }

        // Store selected 6 images into internal storage
        try{
            for (int k=0; k<6; k++){

                // 1. Make new file
                String targetFile_name = "selectedImg_"+Integer.valueOf(k+1).toString() + ".jpg";
                File newImgFile = new File (mTargetFolder, targetFile_name);

                // 2. write the file (byteArray/bitmap) to newImgFile
                FileOutputStream fos = new FileOutputStream(newImgFile);

                String imgFilePath = selected_6Uri.get(k).getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(imgFilePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Launch Activity 2
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
    }



    protected void startleaderBoard(HashMap<String,Integer> scoreList){

        ArrayList<String> playerNames = new ArrayList<>(scoreList.keySet());
        ArrayList<Integer> playerScores = new ArrayList<>(scoreList.values());

        Intent intent = new Intent(this, LeaderBoard.class);
        intent.putStringArrayListExtra("names", playerNames);
        intent.putIntegerArrayListExtra("scores", playerScores);
        startActivity(intent);
    }
}