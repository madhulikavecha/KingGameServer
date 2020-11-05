package tests.controller;

import com.kinggameserver.backend.controller.GameController;
import com.kinggameserver.backend.controller.SessionController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kinggameserver.backend.models.User;

public class GameControllerTest {

    GameController gameController;
    SessionController sessionController;

    @Before
    public void setUp() throws Exception {
        gameController = new GameController();
        sessionController=new SessionController();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUpdateScore() throws Exception{
        int levelId=3;
        int userId=1211;
        int score = 434;
        User user =new User(userId,score);
        String sessionkeyNew =gameController.login(userId);
        gameController.updateScore(levelId,sessionkeyNew,score);
        String highScoreList=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score invalid","1211=434",highScoreList);

    }

    @Test
    public void testLevelIdWithoutScore() throws Exception{
        int levelId = 661;
        String highScoreList =gameController.getHighScoreList(levelId);
        Assert.assertEquals("Level is invalid","",highScoreList);
    }

    @Test
    public void testUniqueScorePerUserPerLevel() throws Exception{
        int levelId =10;
        int userId=1122;
        int score =100;
        String sessionkeyNew1 =gameController.login(userId);
        gameController.updateScore(levelId,sessionkeyNew1,score);
        String highScoreList=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score invalid","1122=100",highScoreList);
        gameController.updateScore(levelId,sessionkeyNew1,155);
        String highScoreList1=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score invalid","1122=155",highScoreList1);
        gameController.updateScore(levelId,sessionkeyNew1,10);
        String highScoreList2=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score invalid","1122=155",highScoreList2);
    }

    @Test
    public void testServeralScorePerLevel() throws Exception{
        int levelId =5;
        String sessionkey =gameController.login(55);
        gameController.updateScore(levelId,sessionkey,500);
        String sessionkey1=gameController.login(44);
        gameController.updateScore(levelId,sessionkey1,4444);
        String highScoreList = gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score invalid","44=4444,55=500",highScoreList);
        String sessionkey2=gameController.login(88);
        gameController.updateScore(levelId,sessionkey2,800);
        String highScoreList2 = gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score is not in descending order","44=4444,88=800,55=500",highScoreList2);
        String sessionkey3=gameController.login(55);
        gameController.updateScore(levelId,sessionkey3,700);
        String highScoreList3 = gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score of a user is not updated to highest","44=4444,88=800,55=700",highScoreList3);
        String sessionkey4=gameController.login(44);
        gameController.updateScore(levelId,sessionkey4,33);
        String highScoreList4 = gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score of user is updated to low new value","44=4444,88=800,55=700",highScoreList4);
        String sessionkey5=gameController.login(66);
        gameController.updateScore(levelId,sessionkey5,6666);
        String highScoreList5 = gameController.getHighScoreList(levelId);
        Assert.assertEquals("High score is not in descending order","66=6666,44=4444,88=800,55=700",highScoreList5);
    }


    @Test
    public void testMaxUserScorePerLevel() throws Exception{
        int levelId=5;
        int limit=GameController.MAX_USERS_PER_LEVEL;
        for(int i=1;i<=limit;i++){
            User user = new User(i,i);
            String sessionkey6=gameController.login(i);
            gameController.updateScore(levelId,sessionkey6,i);
        }
        String higherList=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High Score is not descender order",
                "15=15,14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2,1=1",higherList);
        User user1 = new User(50,50);
        String sessionkey7=gameController.login(50);
        gameController.updateScore(levelId,sessionkey7,50);
        String higherList1=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High Score list invalid has crossed limit",
                "50=50,15=15,14=14,13=13,12=12,11=11,10=10,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2",higherList1);
        User user2 = new User(10,100);
        String sessionkey8=gameController.login(10);
        gameController.updateScore(levelId,sessionkey8,100);
        String higherList2=gameController.getHighScoreList(levelId);
        Assert.assertEquals("High Score is not updated after updated existing user value",
                "10=100,50=50,15=15,14=14,13=13,12=12,11=11,9=9,8=8,7=7,6=6,5=5,4=4,3=3,2=2",higherList2);

    }

}
