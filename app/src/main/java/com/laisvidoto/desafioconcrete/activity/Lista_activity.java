package com.laisvidoto.desafioconcrete.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.laisvidoto.desafioconcrete.R;
import com.laisvidoto.desafioconcrete.adapter.PullAdapter;
import com.laisvidoto.desafioconcrete.interfaces.Services;
import com.laisvidoto.desafioconcrete.model.PullRequest;
import com.laisvidoto.desafioconcrete.util.Api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lista_activity extends AppCompatActivity
{
    private String nomeAutor;
    private String nomeRepositorio;
    private String data;
    private RecyclerView listPullRequest;
    private PullAdapter adapter;
    private Services gitService;
    private List<PullRequest> listaRequests;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nomeAutor = getIntent().getStringExtra("authorName");
        nomeRepositorio  = getIntent().getStringExtra("repoName");

        if (nomeAutor.isEmpty() || nomeRepositorio.isEmpty())
        {
            Toast.makeText(this, "carregamento inadequado! ", Toast.LENGTH_SHORT).show();
            finish();
        }

        getSupportActionBar().setTitle(nomeRepositorio);

        progressBar     = (ProgressBar) findViewById(R.id.pullProgressBar);
        gitService   = Api.getGitService();
        listPullRequest = (RecyclerView) findViewById(R.id.pullList);
        adapter = new PullAdapter(this, new ArrayList<PullRequest>(0), new PullAdapter.PullRequestListener()
        {
            @Override
            public void onPullRequestClick(String url)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listPullRequest.setLayoutManager(layoutManager);
        listPullRequest.setAdapter(adapter);
        listPullRequest.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        listPullRequest.addItemDecoration(itemDecoration);

        if (savedInstanceState != null)
        {
            progressBar.setVisibility(View.GONE);
            listaRequests = (List<PullRequest>) savedInstanceState.getSerializable("list");
            adapter.updatePullRequest(listaRequests);
        } else
        {
            loadPullRequests();
        }
    }

    public void loadPullRequests()
    {
        gitService.getPullRequests(nomeAutor, nomeRepositorio).enqueue(new Callback<List<PullRequest>>()
        {
            @Override
            public void onResponse(Call<List<PullRequest>> call, Response<List<PullRequest>> response)
            {
                if ( response.isSuccessful() )
                {
                    progressBar.setVisibility(View.GONE);
                    listaRequests = response.body();
                    adapter.updatePullRequest(listaRequests);
                    Log.i("PullRequestActivity", "Pull requests carregados.");
                }
            }

            @Override
            public void onFailure(Call<List<PullRequest>> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), "Carregamento inadequado!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.i("PullRequestActivity", "Erro no API!");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (Serializable) listaRequests);
    }
}