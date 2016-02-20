package com.dane.dni;

import com.dane.dni.common.data.DniDateTime;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by Dane on 8/17/2015.
 */
public class DniDateTimeTest {

    @Test
    public void testDniDateTime() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.set(1991, 3, 21, 17, 54, 0);
        cal.set(Calendar.MILLISECOND, 0);

        DniDateTime dniDateTime = DniDateTime.now(cal.getTimeInMillis());
        assertEquals(0, dniDateTime.getProrahntee());
        assertEquals(0, dniDateTime.getGorahntee());
        assertEquals(0, dniDateTime.getTahvotee());
        assertEquals(0, dniDateTime.getPahrtahvotee());
        assertEquals(1, dniDateTime.getYahrtee());
        assertEquals("Leefo", dniDateTime.getVaileeName());
        assertEquals(9647, dniDateTime.getHahrtee());
    }
}
