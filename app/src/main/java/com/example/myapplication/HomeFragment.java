package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {



    private final String API_KEY = "4b33862d69d947bf8af52ca799c768bf";
    private RecyclerView newsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");


        newsRecyclerView = view.findViewById(R.id.newsrecyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerView.setNestedScrollingEnabled(false);
        LoadJson();


        return view;
    }

    public void LoadJson(){

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = Utils.getCountry();

        Call<News> call;
        call = apiInterface.getNews();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null)
                {
                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    newsAdapter = new NewsAdapter(articles, getActivity());
                    newsRecyclerView.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();


                }
                
                else
                {
                    Toast.makeText(getActivity(), "No result", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });


    }

}