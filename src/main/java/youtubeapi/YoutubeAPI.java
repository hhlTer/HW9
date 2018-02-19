package youtubeapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import entiti.Item;
import entiti.Responce;
import entiti.SnippetVideo;

class YoutubeAPI {
    private static final String API_KEY = "AIzaSyDBVpCaXdSwREU9b5UeervX2eCUbqYLTcU";
    static Responce search(String query, int maxResult) throws UnirestException {

        new UnirestSerialization();
        HttpResponse<Responce> response = Unirest.get("https://www.googleapis.com/youtube/v3/search")
                .queryString("key", API_KEY)
                .queryString("part", "snippet")
                .queryString("maxResults", maxResult)
                .queryString("q", query)
                .asObject(Responce.class);

        return response.getBody();
    }
}
