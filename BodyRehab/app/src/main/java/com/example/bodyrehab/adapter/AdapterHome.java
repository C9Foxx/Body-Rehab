package com.example.bodyrehab.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodyrehab.R;
import com.example.bodyrehab.activity.YTPlayerActivity;
import com.example.bodyrehab.models.VideoYT;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VideoYT> videoYTList;
    private static final String TAG = "Picasso";

    public AdapterHome(Context context, List<VideoYT> videoYTList) {
        this.context = context;
        this.videoYTList = videoYTList;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title, date;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            title = itemView.findViewById(R.id.video_title);
            date = itemView.findViewById(R.id.video_date);
        }

        public void setData(final VideoYT data) {
            String getThumbnail =  data.getSnippet().getThumbnails().getMedium().getUrl();
            final String getTitle = data.getSnippet().getTitle();
            String getDate = data.getSnippet().getPublishedAt();

            title.setText(getTitle);
            date.setText(getDate);
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
                    i.putExtra("video_id", data.getId().getVideoId());
                    i.putExtra("video_title", getTitle);
                    context.startActivity(i);
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
