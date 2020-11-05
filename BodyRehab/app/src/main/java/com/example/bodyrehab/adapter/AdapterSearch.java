package com.example.bodyrehab.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.Timer;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.activity.YTPlayerActivity;
import com.example.bodyrehab.models.VideoClass;
import com.example.bodyrehab.models.VideoYT;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterView.OnItemClickListener {

    private Context context;
    private List<VideoYT> videoYTList;
    List<Playlist> playlists = new ArrayList<>();
    long id_playlist;
    private static final String TAG = "Picasso";
    private long user_id;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ListView playlist_menu;



    private UserDataBase database;


    public AdapterSearch(Context context, List<VideoYT> videoYTList, long user_id) {
        this.context = context;
        this.videoYTList = videoYTList;
        this.user_id = user_id;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title;
        Button add;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            add = itemView.findViewById(R.id.btn_add);

            database = UserDataBase.getINSTANCE(context);



            //playlists.add(new Playlist("Music"));
            //playlists.get(0).setCreator_id(user_id);
            //database.getUserDao().InsertPlaylist(playlists);

        }

        public void setData(final VideoYT data) {
            final String getThumbnail =  data.getSnippet().getThumbnails().getMedium().getUrl();
            final String getTitle = data.getSnippet().getTitle();
            final String getUrl = data.getId().getVideoId();

            add.setVisibility(View.VISIBLE);

            title.setText(getTitle);
            //date.setText(getDate);
            Picasso.get()
                    .load(getThumbnail)
                    .placeholder(R.drawable.ic_baseline_cloud_24)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Success");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "Error", e);
                        }
                    });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, YTPlayerActivity.class);
                    i.putExtra("video_id", getUrl);
                    i.putExtra("video_title", getTitle);
                    context.startActivity(i);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewSelectPlaylistDialog(getThumbnail, getTitle, getUrl);
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_home,parent,false);

        return new YoutubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoYT videoYT = videoYTList.get(position);
        YoutubeHolder yth = (YoutubeHolder) holder;
        yth.setData(videoYT);
    }

    @Override
    public int getItemCount() {
        return videoYTList.size();
    }

    public void createNewSelectPlaylistDialog(final String thumb, final String title, final String url){
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactPopupView = LayoutInflater.from(context).inflate(R.layout.popup_add_video, null);
        playlist_menu = (ListView) contactPopupView.findViewById(R.id.list);

        final List<Playlist> playlists = database.getUserDao().loadUserPlaylits(user_id);
        final List<String> playlist_names = new ArrayList<>();

        for (Playlist playlist : playlists){
            String name = playlist.getPlaylist_name();
            Log.d(TAG, name);
            playlist_names.add(name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, playlist_names);
        playlist_menu.setAdapter(arrayAdapter);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        playlist_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Video added to playlist", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                List<Video> videoList = new ArrayList<>();
                Timer timer = new Timer(0,0,0,false);
                videoList.add(new Video(url, thumb, title, timer));
                Playlist playlist = playlists.get(position);
                videoList.get(0).setContainer_id(playlist.getPlaylist_id());
                int size = playlist.getSize();
                playlist.setSize(size+1);
                if(size == 0){
                    playlist.setThumbnail(thumb);
                }
                database.getUserDao().UpdatePlaylistName(playlist);
                database.getUserDao().InsertVideo(videoList);
            }
        });



    }

}
