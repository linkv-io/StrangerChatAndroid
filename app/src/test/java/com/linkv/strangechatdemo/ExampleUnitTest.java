package com.linkv.strangechatdemo;

import com.linkv.strangechatdemo.utils.TimeUtils;

import org.junit.Test;

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

        String timeFormat = TimeUtils.transferTimeFormat(66);
        System.out.println("time = " +timeFormat);

    }
}