package com.example.notepad;

import com.example.notepad.dao.NoteDAO;
import com.example.notepad.entity.NoteVO;
import com.example.notepad.http.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testHttpUtils(){
        String html = HttpUtils.getHtml(NoteDAO.SYNCHRONIZATION_URL);
        System.out.println(html);
    }

    @Test
    public void testConvertJsonToObject(){
        String json = "[{\"id\":1,\"title\":\"phj666\",\"content\":\"哈哈哈\",\"lastUpdate\":null,\"tag\":\"music\"},{\"id\":15,\"title\":\"test\",\"content\":\"hahahahah\",\"lastUpdate\":null,\"tag\":\"music\"},{\"id\":100,\"title\":\"test\",\"content\":\"hahahahah\",\"lastUpdate\":null,\"tag\":\"music\"},{\"id\":101,\"title\":\"test\",\"content\":\"hahahahah\",\"lastUpdate\":null,\"tag\":\"music\"}]";
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<NoteVO> list = mapper.readValue(json, List.class);
            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertJsonToMapTest() throws IOException {
        String json = "{\"code\":302,\"cookie\":\"ASP.NET_SessionId=3sz4ng45uimve2550e1is555; path=/\"}";
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(json, Map.class);
        System.out.println(map.get("cookie"));

    }
}