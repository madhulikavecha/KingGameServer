package tests.webserver;

import com.kinggameserver.backend.webserver.GameServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameServerTest {
    private String PORT="8080";

    @Before
    public void setUp() throws Exception {
        String[] args={};
        GameServer.main(args);
    }

    @After
    public void tearDown() throws Exception{

    }

    @Test
    public void testApplication() throws IOException {
        List<String> seesionKey = new ArrayList<>();
        for(int i=0;i<20;i++){
            seesionKey.add(curlLoginRequest(i));
        }
        for(int i=0;i<15;i++){
            String response= curlUpdateScore(seesionKey.get(i),10,i);
            Assert.assertNull("Score is invalid please try again",response);
        }
        String highScoreList = curlGetHighScoreList(10);
        Assert.assertEquals("HighScore Invalid",
                "14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2,1=1,0=0",highScoreList);
        curlUpdateScore(seesionKey.get(15),10,15);
        curlUpdateScore(seesionKey.get(16),10,16);
        String finalHighScoreList=curlGetHighScoreList(10);
        Assert.assertEquals("HighScore Invalid / Not updated to latest maximum values",
                "16=16,15=15,14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2",finalHighScoreList);

        Assert.assertNull("Data should be empty",curlGetHighScoreList(3333));
    }

    private String curlGetHighScoreList(int levelId) throws IOException {
        URL urlGet = new URL("http://localhost:"+PORT+"/"+levelId+"/highscorelist");
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String highScoreList;
        highScoreList=bufferedReader.readLine();
        conn.disconnect();
        return highScoreList;
    }

    private String curlUpdateScore(final String sessionKey, int levelId, int score) throws IOException {
        URL urlPost = new URL("http://localhost:"+PORT+"/"+levelId+"/score?sessionkey="+sessionKey);
        HttpURLConnection conn = (HttpURLConnection) urlPost.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
        dataOutputStream.writeBytes(Integer.toString(score));
        dataOutputStream.flush();
        dataOutputStream.close();
        String respose = null;
        if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            respose=br.readLine();
        }
        conn.disconnect();
        return respose;
    }

    private String curlLoginRequest(int userId) throws IOException {
        URL url = new URL("http://localhost:"+PORT+"/"+userId+"/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String sessionKey;
        sessionKey = bufferedReader.readLine() ;
        conn.disconnect();
        return sessionKey;
    }


}
