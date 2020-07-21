package com.protienperdollar.redone;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherTest {
    @Test
    public void test_1() {
        int i = 0;
        Matcher match = Pattern.compile("x").matcher("aaaaxaaaxxaaa");
        while(match.find()) {
            i++;
        }

        assertEquals(3, i);
    }

    @Test
    public void test_2() {
        int i = 0;
        Matcher match = Pattern.compile("\n").matcher("aaaa\naaa\n\naaa");
        while(match.find()) {
            i++;
        }

        assertEquals(3, i);
    }

    @Test
    public void test_3() {
        int i = 0;
        Matcher match = Pattern.compile("\r\n|\r|\n").matcher("aaaa\naaa\n\naaa\r\naa\r\n\r\naaa\raaa");
        while(match.find()) {
            i++;
        }
        assertEquals(7, i);
    }
}
