package youtubeapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import entiti.Item;
import entiti.Responce;
import entiti.SnippetVideo;

import java.time.Instant;

class YoutubeAPI {
    private static final String API_KEY = "AIzaSyDBVpCaXdSwREU9b5UeervX2eCUbqYLTcU";

    static Responce search(String query, String count, String days) throws UnirestException {
        int countInt;
        int daysInt;
        boolean countFlag = true;
        boolean daysFlag = true;

        try {
            countInt = Integer.parseInt(count);
            if (countInt < 1) throw new Exception();
        } catch (Exception e) {
            countInt = -1;
        }
        try {
            daysInt = Integer.parseInt(days);
            if (daysInt < 0) throw new Exception();
        } catch (Exception e) {
            daysInt = 0;
        }

        return search(query, countInt, daysInt);
    }

    static Responce search(String query, int maxResult, int days) throws UnirestException {
        new UnirestSerialization();
        final int DEFAULT = 5;
        final int MAXIMUM = 50;
        if (maxResult < 1) maxResult = DEFAULT;
        if (maxResult > 49) maxResult = MAXIMUM;

        String published = (days < 1) ? "publishedBefore" : "publishedAfter";
        final int DAY_IN_SEC = 60*60*24;
        Instant instant = Instant.now();
        instant = instant.minusSeconds(DAY_IN_SEC * days);

        HttpResponse<Responce> response = Unirest.get("https://www.googleapis.com/youtube/v3/search")
                .queryString("key", API_KEY)
                .queryString("part", "snippet")
                .queryString("maxResults", maxResult)
                .queryString(published, instant)
                .queryString("q", query)
                .asObject(Responce.class);

        return response.getBody();
    }


//    static Responce search(String query, int maxResult) throws UnirestException {
//        new UnirestSerialization();
//        HttpResponse<Responce> response = Unirest.get("https://www.googleapis.com/youtube/v3/search")
//                .queryString("key", API_KEY)
//                .queryString("part", "snippet")
//                .queryString("maxResults", maxResult)
//                .queryString("q", query)
//                .asObject(Responce.class);
//
//        return response.getBody();
//    }


//        try {
//            countInt = Integer.parseInt(count);
//            if (countInt < 1) throw new Exception();
//        }catch (Exception e){
//            countFlag = false;
//        }
//        try {
//            daysInt = Integer.parseInt(count);
//            if (daysInt < 0) throw new Exception();
//        }catch (Exception e){
//            daysFlag = false;
//        }

//        if (countFlag & daysFlag) return search(query, daysInt, countInt);
//        else if (countFlag) return search(query, countInt);
//        else return search(daysInt, query);

    }



