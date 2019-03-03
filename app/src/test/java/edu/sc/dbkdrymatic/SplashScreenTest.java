package edu.sc.dbkdrymatic;

import org.junit.Test;

import static org.junit.Assert.*;

public class SplashScreenTest {

    @Test
    public void time_is_correct() {
        //used to test sleep value in splash screen
        //value should equal 3000 ms
        SplashScreen splash_test=new SplashScreen();
        int sleep_value= splash_test.wait_time;
        assertEquals(3000, sleep_value);
    }
}
