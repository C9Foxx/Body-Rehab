package com.example.bodyrehab.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bodyrehab.R;
import com.example.bodyrehab.adapter.AdapterSearch;
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
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = "Picasso";
    private EditText input_query;
    private Button btn_search;
    private AdapterSearch adapter;
    private LinearLayoutManager manager;
    private List<VideoYT> videoYTList = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private long mParam1;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(long param1) {
        SearchFragment fragment = new SearchFragment();
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
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

        input_query = rootView.findViewById(R.id.input_query);
        btn_search = rootView.findViewById(R.id.btn_search);

        RecyclerView rv = rootView.findViewById(R.id.recycler_search);

        adapter = new AdapterSearch(getContext(), videoYTList, mParam1);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoYTList.clear();
                String query = input_query.getText().toString();
                if (!TextUtils.isEmpty(query)){
                    getJson(query);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
                else {
                    Toast.makeText(getContext(), "Enter a search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void getJson(String query) {
        String url = YoutubeAPI.BASE_URL + YoutubeAPI.SCH + YoutubeAPI.KEY + YoutubeAPI.MX + YoutubeAPI.ORD + YoutubeAPI.PART + YoutubeAPI.QUERY + query + YoutubeAPI.TYPE;
        Call<ModelHome> data = YoutubeAPI.getHomeVideo().getYT(url);
        data.enqueue(new Callback<ModelHome>() {
            @Override
            public void onResponse(Call<ModelHome> call, Response<ModelHome> response) {
                if (response.errorBody() != null){
                    Log.w(TAG, "onResponse SEARCH" + response.errorBody());
                }
                else {
                    ModelHome mh = response.body();
                    if (mh.getItems().size() != 0){
                        videoYTList.addAll(mh.getItems());
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getContext(), "No video found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelHome> call, Throwable t) {
                Log.e(TAG, "onFailure SEARCH", t);
            }
        });
    }
}