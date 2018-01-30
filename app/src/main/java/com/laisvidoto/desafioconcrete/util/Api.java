package com.laisvidoto.desafioconcrete.util;

import com.laisvidoto.desafioconcrete.interfaces.Services;

/**
 * Created by Laís Vidoto on 30/01/2018.
 */

public class Api
{
    public static final String BASE_URL = "https://api.github.com";

    public static Services getGitService()
    {
        return RetrofitClient.getClient(BASE_URL).create(Services.class);
    }
}
