package com.android.angelo.popularmovies.Utils;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String MOVIEDB_BASE_URL =
            "https://api.themoviedb.org/3/";

    final static String  MOVIEDB_BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w500/";

    final static String API_KEY = "api_key";
    final static String LANGUAGE = "language";
    final static String DEFAUlT_LANGUAGE = "en-US";
    final static String PAGE_NUMBER = "page";

    public static URL buildUrl(String restCall, String apiKey, String language, int page_number) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + restCall).buildUpon()
                .appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(LANGUAGE, language)
                .appendQueryParameter(PAGE_NUMBER, String.valueOf(page_number))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrl(String restCall, String apiKey, int page_number) {
        return buildUrl(restCall,DEFAUlT_LANGUAGE,page_number);
    }

    public static Uri buildImageUrl(String posterCode) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL_IMAGE).buildUpon()
                .appendEncodedPath(posterCode)
                .build();
        return builtUri;
    }


    public static JSONObject getResponseFromHttpUrl(URL url) throws IOException, JSONException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return new JSONObject(scanner.next());
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
