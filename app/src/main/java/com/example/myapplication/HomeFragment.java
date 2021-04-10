package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents the Home or News page where users can read news related to Singapore Education
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    /**
     * API Key for news
     */
    private final String API_KEY = "4b33862d69d947bf8af52ca799c768bf";
    /**
     * RecyclerView to contain the news
     */
    private RecyclerView newsRecyclerView;
    /**
     * Layouytmanager to manage RecyclerView
     */
    private RecyclerView.LayoutManager layoutManager;
    /**
     * List of news articles
     */
    private List<Article> articles = new ArrayList<>();
    /**
     * Create an instance of the newsAdapter controller
     */
    private NewsAdapter newsAdapter;
    /**
     * Tag to store the simple name of the HomeFragment class
     */
    private String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    // Required empty public constructor
    public HomeFragment() {

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Initial creation of fragment from savedInstanceState
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /**
     * Graphical Initialisation of the fragment and inflates the layout of the fragment onto a container
     * @param inflater Inflate the layout of the fragment
     * @param container Container for the layout
     * @param savedInstanceState
     * @return The home or news page
     */
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

    /**
     * Load the List of articles from the News API
     */
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