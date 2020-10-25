package com.example.bodyrehab.UserDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bodyrehab.models.VideoYT;

@Entity(tableName = Constants.TABLE_NAME_VIDEOS)
public class Video {

    @PrimaryKey(autoGenerate = true)
    private long video_id;

    private long container_id;
    private String video_url;
    private String video_thumbnail;
    private String video_title;

    public Video(String video_url, String video_thumbnail, String video_title) {
        this.video_url = video_url;
        this.video_thumbnail = video_thumbnail;
        this.video_title = video_title;
    }

    public long getVideo_id() {
        return video_id;
    }

    public void setVideo_id(long video_id) {
        this.video_id = video_id;
    }

    public long getContainer_id() {
        return container_id;
    }

    public void setContainer_id(long container_id) {
        this.container_id = container_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_thumbnail() {
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }
}
