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

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.activity.YTPlayerActivity;
import com.example.bodyrehab.models.VideoYT;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Video> videoList;
    private static final String TAG = "Picasso";
    private long user_id;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newTitle;
    private Button btn_delete, btn_change_title, btn_exit;
    private CheckBox check_delete, check_title;

    private UserDataBase database;

    public AdapterHome(Context context, List<Video> videoList, long user_id) {
        this.context = context;
        this.videoList = videoList;
        this.user_id = user_id;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title;
        Button video_settings;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            video_settings = itemView.findViewById(R.id.video_set);

            database = UserDataBase.getINSTANCE(context);

        }

        public void setData(final Video data) {

            String getThumbnail =  data.getVideo_thumbnail();
            final String getTitle = data.getVideo_title();

            video_settings.setVisibility(View.VISIBLE);

            title.setText(getTitle);

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
                    i.putExtra("video_id", data.getVideo_url());
                    i.putExtra("video_title", getTitle);
                    context.startActivity(i);
                }
            });

            video_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewVideoSettingDialog();
                }
            });

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_item_home,parent,false);

        return new AdapterHome.YoutubeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Video video = videoList.get(position);
        AdapterHome.YoutubeHolder yth = (AdapterHome.YoutubeHolder) holder;
        yth.setData(video);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void createNewVideoSettingDialog(){
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactPopupView = LayoutInflater.from(context).inflate(R.layout.popup_vid_set, null);

        newTitle = (EditText) contactPopupView.findViewById(R.id.new_title);
        check_title = (CheckBox) contactPopupView.findViewById(R.id.check_title);
        btn_change_title = (Button) contactPopupView.findViewById(R.id.btn_change_title);
        check_delete = (CheckBox) contactPopupView.findViewById(R.id.check_del);
        btn_delete = (Button) contactPopupView.findViewById(R.id.btn_del);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

    }
}
