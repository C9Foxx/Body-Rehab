package com.example.bodyrehab.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodyrehab.R;
import com.example.bodyrehab.adapter.AdapterHome;
import com.example.bodyrehab.models.ModelHome;
import com.example.bodyrehab.models.VideoYT;
import com.example.bodyrehab.network.YoutubeAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "Picasso";
    private AdapterHome adapter;
    private LinearLayoutManager manager;
    private List<VideoYT> videoYTList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rv = rootView.findViewById(R.id.recylerView);
        adapter = new AdapterHome(getContext(), videoYTList);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        
        if(videoYTList.size() == 0){
            getJson();
        }

        return rootView;
    }

    private void getJson() {
        String url = YoutubeAPI.BASE_URL + YoutubeAPI.SCH + YoutubeAPI.KEY + YoutubeAPI.CHID + YoutubeAPI.MX + YoutubeAPI.ORD + YoutubeAPI.PART;
        Call<ModelHome> data = YoutubeAPI.getHomeVideo().getYT(url);
        data.enqueue(new Callback<ModelHome>() {
            @Override
            public void onResponse(Call<ModelHome> call, Response<ModelHome> response) {
                if (response.errorBody() != null){
                    Log.w(TAG, "onResponse" + response.errorBody());
                }
                else {
                    ModelHome mh = response.body();
                    videoYTList.addAll(mh.getItems());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ModelHome> call, Throwable t) {
                    Log.e(TAG, "onFailure", t);
            }
        });
    }
}