package com.example.shallak.todo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.shallak.todo.api.GithubAPI;
import com.example.shallak.todo.model.GithubRepo;
import com.example.shallak.todo.model.GithubUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class GitHubFragment extends Fragment implements Callback<GithubUser>
{

    private String title;
    private int page;

    @BindView(R.id.loadRepositories)  Button btnLoadRepos;
    @BindView(R.id.loadUserData) Button btnLoadUserData;
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GithubAPI.ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
    GithubAPI githubUserAPI = retrofit.create(GithubAPI.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_git_hub, container, false);
        ButterKnife.bind(this,view);
        btnLoadRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<GithubUser> callUser = githubUserAPI.getUser("bigali");
                //asynchronous call
                callUser.enqueue(GitHubFragment.this);
            }
        });

        btnLoadUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<GithubRepo>> callRepos = githubUserAPI.getRepos("bigali");
                //asynchronous call
                callRepos.enqueue(repos);
            }
        });
        return view;

    }

    // newInstance constructor for creating fragment with arguments
    public static GitHubFragment newInstance(int page, String title) {
        GitHubFragment fragmentFirst = new GitHubFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    Callback repos = new Callback<List<GithubRepo>>(){

        @Override
        public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
            if (response.isSuccessful()) {
                List<GithubRepo> repos = response.body();
                StringBuilder builder = new StringBuilder();
                for (GithubRepo repo: repos) {
                    builder.append(repo.toString() + " " + repo.toString());
                }
                Toast.makeText(getActivity(), builder.toString(), Toast.LENGTH_SHORT).show();

            } else
            {
                Toast.makeText(getActivity(), "Error code " + response.code(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
            Toast.makeText(getActivity(), "Did not work " +  t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
        int code = response.code();
        if (code == 200) {
            GithubUser user = response.body();
            Toast.makeText(getActivity(), "Got the user: " + user.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Did not work: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<GithubUser> call, Throwable t) {
        Toast.makeText(getActivity(), "Nope", Toast.LENGTH_LONG).show();
    }


}
