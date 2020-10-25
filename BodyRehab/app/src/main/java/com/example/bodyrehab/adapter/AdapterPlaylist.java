package com.example.bodyrehab.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bodyrehab.R;

import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterPlaylist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Playlist> playlistList;
    private long user_id;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText playlist_name;
    private Button btn_done, btn_settings;
    int cont = 0;


    public AdapterPlaylist(Context context, List<Playlist> playlistList, long user_id) {
        this.context = context;
        this.playlistList = playlistList;
        this.user_id = user_id;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title, vid_count2;


        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.iv_playlist_thumb);
            title = itemView.findViewById(R.id.tv_playlist_title);
            vid_count2 = itemView.findViewById(R.id.tv_video_count2);
            btn_settings = itemView.findViewById(R.id.plalist_set);
        }

        public void setData(Playlist data) {
            final String getTitle = data.getPlaylist_name();
            int getCount = 4;
            String getThumb = "";

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, getTitle, Toast.LENGTH_SHORT).show();
                }
            });

            title.setText(getTitle);
            vid_count2.setText(String.valueOf(getCount));
            Picasso.get()
                    .load(R.drawable.ic_baseline_add_circle_outline_24)
                    .placeholder(R.drawable.background_image_one_password)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Thumbnail berhasil ditampilkan");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Thumbnail error: ", e);
                        }
                    });

            btn_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewPlaylistDialog();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_item_playlist, parent, false);
        return new AdapterPlaylist.YoutubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        AdapterPlaylist.YoutubeHolder yth = (AdapterPlaylist.YoutubeHolder) holder;
        yth.setData(playlist);
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public void createNewPlaylistDialog(){
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactPopupView = LayoutInflater.from(context).inflate(R.layout.popup_add_playlist, null);

        playlist_name = (EditText) contactPopupView.findViewById(R.id.playlist_name);
        playlist_name.setHint("Change name");
        btn_done = (Button) contactPopupView.findViewById(R.id.btn_done);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}