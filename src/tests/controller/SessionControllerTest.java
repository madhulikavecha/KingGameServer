package tests.controller;

import com.kinggameserver.backend.controller.SessionController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.kinggameserver.backend.models.Session;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SessionControllerTest {

    SessionController sessionController;

    @Before
    public void setUp() throws Exception {
        sessionController = new SessionController();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateNewSession() throws Exception{
        Session session =sessionController.createNewSession(29);
        System.out.println("session.getUser_id()="+session.getUser_id());
        System.out.println("session.getSessionKey()"+session.getSessionKey());
        System.out.println("session.getSession_start_time()"+session.getSession_start_time());
        Assert.assertNotNull("Session is ninvalid",session.getSessionKey());
    }

    @Test
    public void testIsSessionValid() throws Exception{
        Session session= sessionController.createNewSession(199);
        String sessionKey = session.getSessionKey();
        boolean isValid = sessionController.isSessionValid(session);
        Assert.assertTrue("Sesssion Invalid",isValid);
    }

    @Test
    public  void testSessionValid() throws Exception{
        Session session = sessionController.createNewSession(27);
        boolean check =session.getSessionKey().matches("^[a-zA-Z0-9]*$");
        Assert.assertTrue("special characters are present in session"+session.getSessionKey(),check);
    }

    @Test
    public void testSessionTimeOut() throws Exception{
        Session session = sessionController.createNewSession(234);
        System.out.println("session ="+session);
        boolean valid =sessionController.isSessionValid(session);
        Assert.assertTrue(" session Invalid",valid);
        System.out.println("time now:"+new Date()+"session validity : "+valid);
        System.out.println("----------sleeping for 9 min 55 seconds-----------");
        TimeUnit.MILLISECONDS.sleep(sessionController.TIME_TO_LIVE-500);
        boolean validityCheck=sessionController.isSessionValid(session);
        System.out.println("time now: "+new Date()+" session validity : "+validityCheck);
        Assert.assertTrue("Session Invalid",validityCheck);
        System.out.println("---------------sleeping for 10 seconds-----------");
        TimeUnit.MILLISECONDS.sleep(1000);
        boolean inValid =sessionController.isSessionValid(session);
        System.out.println("Time now :"+new Date()+"session validity :"+inValid);
        Assert.assertFalse("session is invalid crossed Time to Live",inValid);


    }
}
