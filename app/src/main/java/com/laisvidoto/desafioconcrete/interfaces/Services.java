package com.laisvidoto.desafioconcrete.interfaces;

import com.laisvidoto.desafioconcrete.model.PullRequest;
import com.laisvidoto.desafioconcrete.model.RespostaRepositorio;
import com.laisvidoto.desafioconcrete.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Laís Vidoto on 30/01/2018.
 */

public interface Services
{
    @GET("search/repositories?q=language:Java&sort=stars")
    Call<RespostaRepositorio> getRepositories(@Query("page") Integer page);

    @GET("repos/{user}/{repo}/pulls")
    Call<List<PullRequest>>getPullRequests(@Path("user") String userName, @Path("repo") String repoName);

    @GET("users/{user}")
    Call<Usuario> getUser(@Path("user") String username);
}
