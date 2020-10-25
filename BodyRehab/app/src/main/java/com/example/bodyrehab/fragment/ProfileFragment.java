package com.example.bodyrehab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.PlaylistWithVideos;
import com.example.bodyrehab.UserDB.UserAndPlaylist;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.UserWithPlaylistsAndSongs;
import com.example.bodyrehab.UserDB.Video;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "Picasso";
    private EditText id, playlist_name;

    private UserDataBase userDataBase;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        userDataBase = UserDataBase.getINSTANCE(getActivity());
        id = rootView.findViewById(R.id.find_id);
        playlist_name = rootView.findViewById(R.id.search_name);
        Button btn_find = rootView.findViewById(R.id.btn_find);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long user_id = Integer.parseInt(id.getText().toString());
                String name = playlist_name.getText().toString();
                Playlist playlist = userDataBase.getUserDao().SearchPlaylistByName(user_id, name);
                Toast.makeText(getContext(), playlist.getPlaylist_name(), Toast.LENGTH_SHORT).show();
                /*
                UserWithPlaylistsAndSongs userWithPlaylistsAndSongs = userDataBase.getUserDao().SearchPlaylistsByUser(user_id);
                for (PlaylistWithVideos playlist : userWithPlaylistsAndSongs.playlists) {
                    Log.d(TAG, String.valueOf(playlist.getPlaylist().getPlaylist_name()));
                    for (Video video : playlist.videos){
                        Log.d(TAG, String.valueOf(video.getVideo_title()));
                    }
                }*/
            }
        });

        return rootView;
    }
}