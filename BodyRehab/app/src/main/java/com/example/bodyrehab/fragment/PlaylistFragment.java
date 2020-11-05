package com.example.bodyrehab.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.PlaylistWithVideos;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.UserWithPlaylistsAndSongs;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.adapter.AdapterPlaylist;

import java.util.ArrayList;
import java.util.List;


import static androidx.constraintlayout.widget.Constraints.TAG;


public class PlaylistFragment extends Fragment {

    private AdapterPlaylist adapter;
    private LinearLayoutManager manager;
    private Button add_playlist;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText playlist_name;
    private Button btn_done;

    private List<Playlist> playlistList = new ArrayList<>();

    private UserDataBase userDataBase;

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private long mParam1;



    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance(long param1) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        userDataBase = UserDataBase.getINSTANCE(getActivity());

        RecyclerView rv = view.findViewById(R.id.recycler_playlist);
        adapter = new AdapterPlaylist(getContext(), playlistList, mParam1);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        add_playlist = view.findViewById(R.id.btn_add_playlist);

        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlaylistDialog();
            }
        });

        getPlaylistList();

        return view;
    }

    private void getPlaylistList() {

        playlistList.clear();
        long user_id = mParam1;

        UserWithPlaylistsAndSongs userWithPlaylistsAndSongs = userDataBase.getUserDao().SearchPlaylistsByUser(user_id);
        for (PlaylistWithVideos playlist : userWithPlaylistsAndSongs.playlists) {
            Log.d(TAG, String.valueOf(playlist.getPlaylist().getPlaylist_name()));
            playlistList.add(playlist.getPlaylist());
            for (Video video : playlist.videos){
                Log.d(TAG, String.valueOf(video.getVideo_title()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void createNewPlaylistDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View contactPopupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_add_playlist, null);

        playlist_name = (EditText) contactPopupView.findViewById(R.id.playlist_name);
        btn_done = (Button) contactPopupView.findViewById(R.id.btn_select_playlist);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = playlist_name.getText().toString();
                Playlist playlist = new Playlist(name);
                playlist.setCreator_id(mParam1);
                userDataBase.getUserDao().InsertOnePlaylist(playlist);
                getPlaylistList();
                dialog.dismiss();
            }
        });
    }

}