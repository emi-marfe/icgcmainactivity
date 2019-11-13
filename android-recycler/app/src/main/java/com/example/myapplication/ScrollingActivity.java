package com.example.myapplication;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.myapplication.PictureContent.deleteSavedImages;
import static com.example.myapplication.PictureContent.downloadRandomImage;
import static com.example.myapplication.PictureContent.loadSavedImages;

public class ScrollingActivity extends AppCompatActivity
        implements ItemFragment.OnListFragmentInteractionListener{
    private ScrollingActivity context;
    private DownloadManager downloadManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private BroadcastReceiver onComplete;
    private View progressBar;
    Boolean isRotate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap myImage = getBitmapFromURL("https://www.pexels.com/photo/gray-concrete-tower-364096/");
        CoordinatorLayout rLayout=(CoordinatorLayout)findViewById(R.id.CoordinatorLayout);
        Drawable dr = new BitmapDrawable(myImage);
        rLayout.setBackgroundDrawable(dr);


       // return true;


        if (recyclerViewAdapter == null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            recyclerView = (RecyclerView) currentFragment.getView();
            recyclerViewAdapter = ((RecyclerView) currentFragment.getView()).getAdapter();
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        progressBar = findViewById(R.id.indeterminateBar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        final FloatingActionButton fabMic = findViewById(R.id.fabMic);
        final FloatingActionButton fabCall = findViewById(R.id.fabCall);
        final FloatingActionButton fabb = findViewById(R.id.fabb);
        final FloatingActionButton fabweb = findViewById(R.id.fabweb);
        ViewAnimation.init(fabMic);
        ViewAnimation.init(fabCall);
        ViewAnimation.init(fab);
        ViewAnimation.init(fabweb);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                        downloadRandomImage(downloadManager, context);
                    }
                });
            }
        });
        fabb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if(isRotate){
                    ViewAnimation.showIn(fabCall);
                    ViewAnimation.showIn(fabMic);
                    ViewAnimation.showIn(fab);
                    ViewAnimation.showIn(fabweb);
                }else{
                    ViewAnimation.showOut(fabCall);
                    ViewAnimation.showOut(fabMic);
                    ViewAnimation.showOut(fab);
                    ViewAnimation.showOut(fabweb);
                }

            }
        });
        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ScrollingActivity.this,ContactActivity.class));
            }
        });

        fabweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ScrollingActivity.this,Web.class));
            }
        });

        fabMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(ScrollingActivity.this,RegisterActivity.class));
            }
        });

        onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String filePath="";
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
                Cursor c = downloadManager.query(q);

                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        filePath = Uri.parse(downloadFileLocalUri).getPath();
                    }
                }
                c.close();
                PictureContent.loadImage(new File(filePath));
                recyclerViewAdapter.notifyItemInserted(0);
                progressBar.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        };

        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteSavedImages(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            recyclerViewAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(onComplete);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onListFragmentInteraction(PictureItem item) {
        // This is where you'd handle clicking an item in the list
    }
}
