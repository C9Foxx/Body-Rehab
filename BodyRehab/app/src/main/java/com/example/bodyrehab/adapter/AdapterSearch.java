package com.example.bodyrehab.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.activity.YTPlayerActivity;
import com.example.bodyrehab.models.VideoClass;
import com.example.bodyrehab.models.VideoYT;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VideoYT> videoYTList;
    List<Playlist> playlists = new ArrayList<>();
    long id_playlist;
    private static final String TAG = "Picasso";
    private long user_id;

    private UserDataBase database;


    public AdapterSearch(Context context, List<VideoYT> videoYTList, long user_id) {
        this.context = context;
        this.videoYTList = videoYTList;
        this.user_id = user_id;
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
                    List<Video> videoList = new ArrayList<>();
                    videoList.add(new Video(getUrl, getThumbnail, getTitle));
                    videoList.get(0).setContainer_id(4);
                    database.getUserDao().InsertVideo(videoList);
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

}
