package com.example.bodyrehab.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodyrehab.R;
import com.example.bodyrehab.UserDB.Playlist;
import com.example.bodyrehab.UserDB.PlaylistWithVideos;
import com.example.bodyrehab.UserDB.UserAndPlaylist;
import com.example.bodyrehab.UserDB.UserAndVideo;
import com.example.bodyrehab.UserDB.UserDataBase;
import com.example.bodyrehab.UserDB.UserWithPlaylistsAndSongs;
import com.example.bodyrehab.UserDB.Video;
import com.example.bodyrehab.adapter.AdapterHome;
import com.example.bodyrehab.models.ModelHome;
import com.example.bodyrehab.network.YoutubeAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "Picasso";
    private AdapterHome adapter;
    private LinearLayoutManager manager;
    private List<Video> videoList = new ArrayList<>();
    private TextView routine;
    private String playlist_name;

    private UserDataBase userDataBase;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private long mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(long param1, String playlist_name) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, playlist_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getLong(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        userDataBase = UserDataBase.getINSTANCE(getActivity());

        routine = rootView.findViewById(R.id.routine_name);


        RecyclerView rv = rootView.findViewById(R.id.recylerView);
        adapter = new AdapterHome(getContext(), videoList, mParam1);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        getVideoList();

        return rootView;
    }

    private void getVideoList() {

        videoList.clear();
        long user_id = mParam1;
        String name = mParam2;

        if(mParam2.equals("")){
            UserAndPlaylist userAndPlaylist = userDataBase.getUserDao().SearchByUserAndPlaylist(user_id);
            //Log.w(TAG, String.valueOf(userAndPlaylist.playlistList.size()));
            if (userAndPlaylist.playlistList.size() > 0) {
                name = userAndPlaylist.playlistList.get(0).getPlaylist_name();
            }
        }


        PlaylistWithVideos playlistWithVideos = userDataBase.getUserDao().LoadVideosOnPlaylist(user_id, name);
        if (playlistWithVideos != null) {
            for (Video video : playlistWithVideos.videos) {
                Log.d(TAG, String.valueOf(video.getVideo_title()));
            }
            videoList.addAll(playlistWithVideos.videos);
        }

        routine.setText("Routine: " + name);
        adapter.notifyDataSetChanged();
    }

}