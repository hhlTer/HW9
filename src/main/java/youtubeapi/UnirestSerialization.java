package youtubeapi;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import entiti.Responce;

import java.io.IOException;

class UnirestSerialization {
    static {
        Unirest.setObjectMapper(new ObjectMapper() {
            public <T> T readValue(String value, Class<T> valueType) {
                return JSON.parseObject(value, valueType);
            }
            public String writeValue(Object value) {
                return JSON.toJSONString(value);
            }
        });
    }
}
