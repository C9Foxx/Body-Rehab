package com.example.bodyrehab.adapter;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bodyrehab.R;

import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.PlaylistWithVideos;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.activity.AppActivity;
import com.example.bodyrehab.activity.MainActivity;
import com.example.bodyrehab.fragment.HomeFragment;
import com.example.bodyrehab.fragment.PlaylistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterPlaylist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Playlist> playlistList;
    private long user_id;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private UserDataBase database;
    private EditText newTitle;
    private Button btn_delete, btn_change_title;
    private CheckBox check_delete, check_title;
    private TextView textViewDel;
    private BottomNavigationView menu;
    private PlaylistFragment playlistFragment;
    int cont = 0;

    private HomeFragment homeFragment;


    public AdapterPlaylist(Context context, List<Playlist> playlistList, long user_id) {
        this.context = context;
        this.playlistList = playlistList;
        this.user_id = user_id;
    }

    class YoutubeHolder extends RecyclerView.ViewHolder {
        TextView title, vid_count2;
        ImageView thumbnail;
        Button btn_settings;

        public YoutubeHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.iv_playlist_thumb);
            title = itemView.findViewById(R.id.tv_playlist_title);
            vid_count2 = itemView.findViewById(R.id.tv_video_count2);
            btn_settings = itemView.findViewById(R.id.plalist_set);

            database = UserDataBase.getINSTANCE(context);
        }

        public void setData(Playlist data) {
            final String getTitle = data.getPlaylist_name();
            int getCount = data.getSize();
            String getThumb = data.getThumbnail();

            if(getThumb.equals("")){
                getThumb = "xxx";
            }

            title.setText(getTitle);
            vid_count2.setText(String.valueOf(getCount));

            Picasso.get()
                    .load(getThumb)
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, getTitle, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AppActivity.class);
                    intent.putExtra(MainActivity.EXTRA_PLAYLIST, getTitle);
                    intent.putExtra(MainActivity.EXTRA_STATE, 1);
                    intent.putExtra(MainActivity.EXTRA_ID, user_id);
                    ((Activity)context).finish();
                    context.startActivity(intent);
                }
            });

            btn_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    createNewPlaylistDialog(getTitle, position);
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

    public void createNewPlaylistDialog(final String old_name, final int position){
        dialogBuilder = new AlertDialog.Builder(context);
        final View contactPopupView = LayoutInflater.from(context).inflate(R.layout.popup_vid_set, null);

        newTitle = (EditText) contactPopupView.findViewById(R.id.new_title);
        newTitle.setHint("Change Name");
        check_title = (CheckBox) contactPopupView.findViewById(R.id.check_title);
        btn_change_title = (Button) contactPopupView.findViewById(R.id.btn_change_title);
        check_delete = (CheckBox) contactPopupView.findViewById(R.id.check_del);
        btn_delete = (Button) contactPopupView.findViewById(R.id.btn_del);
        textViewDel = (TextView) contactPopupView.findViewById(R.id.textViewDelete);
        textViewDel.setText("Delete Playlist");


        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_change_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_title.isChecked()){
                    String name = newTitle.getText().toString();
                    playlistList.get(position).setPlaylist_name(name);
                    database.getUserDao().UpdatePlaylistName(playlistList.get(position));
                    Toast.makeText(context, "Playlist Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_delete.isChecked()){
                    Playlist playlist = playlistList.get(position);
                    PlaylistWithVideos playlistWithVideos = database.getUserDao().LoadVideosOnPlaylist(user_id, playlist.getPlaylist_name());
                    List <Video> videoList = playlistWithVideos.videos;
                    playlistList.remove(position);
                    database.getUserDao().DeletePlaylist(playlist);
                    database.getUserDao().DeleteVideoList(videoList);
                    Toast.makeText(context, "Playlist Deleted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            }
        });


    }
}