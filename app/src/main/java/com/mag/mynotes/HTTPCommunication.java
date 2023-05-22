package com.mag.mynotes;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

public class HTTPCommunication {
    private static final String apiUrl = "https://google-translator9.p.rapidapi.com/v2";
    private static final String apiKey = "place-your-api-key";
    private static final String apiHost = "google-translator9.p.rapidapi.com";

    public static String postTranslateText(
            String sourceLang,
            String targetLang,
            String content,
            HTTPResponses httpResponses) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\"q\": \"" + content + "\",\r\"source\": \"" + sourceLang + "\",\r\"target\": \"" + targetLang + "\",\r\"format\": \"text\"\r}");

        Request request = new Request.Builder()
            .url(apiUrl)
            .post(body)
            .addHeader("content-type", "application/json")
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build();

        Response response = client.newCall(request).execute();
        String responseStr = response.body().string();

        JSONObject returnedJO = (JSONObject) new JSONTokener(responseStr).nextValue();

        return returnedJO
                .getJSONObject("data")
                .getJSONArray("translations")
                .getJSONObject(0)
                .getString("translatedText");
    }

    public static String postGetSourceLanguage(String content)
            throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"q\": \"" + content + "\"\r\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/detect")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", apiKey)
                .addHeader("X-RapidAPI-Host", apiHost)
                .build();

        Response response = client.newCall(request).execute();

        String responseStr = response.body().string();

        JSONObject returnedJO = (JSONObject) new JSONTokener(responseStr).nextValue();

        return returnedJO
                .getJSONObject("data")
                .getJSONArray("detections")
                .getJSONArray(0)
                .getJSONObject(0)
                .getString("language");
    }
}
