package org.apache.myfaces.dateformat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.joda.time.DateTime;

import junit.framework.TestCase;

public class TestSimpleDateFormatter extends TestCase
{
    /**
     * Define a "callback" interface for a test that can be invoked with various dates.
     */
    public interface DateTest
    {
        public void test(int year, int month, int day) throws Exception;
    }

    // test just the very basics of date formatting
    public void testFormatSimple()
    {
        SimpleDateFormatter sdf = new SimpleDateFormatter("yyyy-MM-dd'T'hh:mm:ss", null);
        Date d = new Date();
        d.setYear(1987 - 1900);
        d.setMonth(2);
        d.setDate(12);
        d.setHours(13);
        d.setMinutes(23);
        d.setSeconds(59);
        
        String s = sdf.format(d);
        assertEquals("1987-03-12T01:23:59", s);
    }

    // test just the very basics of date parsing
    public void testParseSimple()
    {
        SimpleDateFormatter sdf = new SimpleDateFormatter("yyyy-MM-dd'T'HH:mm:ss", null);
        Date d = sdf.parse("1987-03-12T04:23:59");

        assertNotNull(d);
        assertEquals(1987, d.getYear() + 1900);
        assertEquals(2, d.getMonth());
        assertEquals(12, d.getDate());
        assertEquals(4, d.getHours());
        assertEquals(23, d.getMinutes());
        assertEquals(59, d.getSeconds());
    }
    
    // test every possible formatter in date formatting
    public void testFormatAll()
    {
        Date d = new Date();
        d.setYear(1987 - 1900);
        d.setMonth(2);
        d.setDate(12);
        d.setHours(4);
        d.setMinutes(23);
        d.setSeconds(59);
        
        String[] data =
        {
            "yyyy", "1987",
            "yyy", "87",
            "yy", "87",
            "y", "87",
            "MMMM", "March",
            "MMM", "Mar",
            "MM", "03",
            "M", "3",
            "dd", "12",
            "EEEE", "Thursday",
            "EE", "Thu",
            "HH", "04",
            "H", "4",
            "hh", "04",
            "h", "4",
            "mm", "23",
            "m", "23",
            "ss", "59",
            "s", "59",
            "a", "AM"
        };

        Locale locale = Locale.ENGLISH;
        for(int i=0; i<data.length; i+=2)
        {
            String pattern = data[i];
            String expected = data[i+1];

            SimpleDateFormatter sdf = new SimpleDateFormatter(pattern, null);
            String s = sdf.format(d);
            assertEquals("custom:" + pattern, expected, s);
            
            SimpleDateFormat sf = new SimpleDateFormat(pattern, locale);
            String s2 = sf.format(d);
            assertEquals("std:" + pattern, expected, s2);
        }
    }
    
    // test every possible formatter in date parsing
    public void testParseAll()
    {
        Date d = new Date();
        d.setYear(1987 - 1900);
        d.setMonth(2);
        d.setDate(12);
        d.setHours(4);
        d.setMinutes(23);
        d.setSeconds(59);
        
        String[] data =
        {
            "yyyy", "1987",
            "yyy", "87",
            "yy", "87",
            "y", "87",
            "MMMM", "March",
            "MMM", "Mar",
            "MM", "03",
            "M", "3",
            "dd", "12",

            // These are difficult to test in this way; disable them
            //"EEEE", "Monday",
            //"EE", "Mon",

            "HH", "04",
            "H", "4",

            "hh", "04",
            "h", "4",
            "mm", "23",
            "m", "23",
            "ss", "59",
            "s", "59",
            "a", "AM"
        };

        Locale locale = Locale.ENGLISH;
        for(int i=0; i<data.length; i+=2)
        {
            String pattern = data[i];
            String expected = data[i+1];

            // parse it with our code, then format it with the std code and
            // see if we get the same value back.
            SimpleDateFormatter sdf = new SimpleDateFormatter(pattern, null);
        
            Date d2 = sdf.parse(expected);
            int year = d2.getYear();
            SimpleDateFormat sf = new SimpleDateFormat(pattern, locale);
            String s2 = sf.format(d2);
            assertEquals(pattern, expected, s2);
        }
    }

    // try to parse various combinations, and see what we get
    public void testParseAssorted() throws Exception {
        Object[] data =
        {
            // test standard, with literals
            "yyyy-MM-dd", "1987-01-08", new Date(1987-1900, 0, 8),
            
            // test standard, with multichar literal sequences: any non-alpha
            // char must exactly match the input.
            "yyyy--MM-:()dd", "1987--01-:()08", new Date(1987-1900, 0, 8),
            
            // test standard, with quoted chars.
            "yyyy'T'MM'T'dd", "1987T01T08", new Date(1987-1900, 0, 8),
            
            // test standard, with non-pattern chars.
            // An alpha non-pattern char --> error
            "yyyyRMMRdd", "1987-01-08", null,
            
            // test quoted text
            "yyyy'year'MM'month'dd", "2003year04month06", new Date(2003-1900, 03, 06),
            
            // test mismatched quoted text
            "yyyy'year'MM'month'dd", "2003yexr04month06", null,
        };

        Locale locale = Locale.ENGLISH;
        for(int i=0; i<data.length; i+=3)
        {
            String pattern = (String) data[i];
            String input = (String) data[i+1];
            Date expected = (Date) data[i+2];

            // parse it with our code, and see if we get the expected result
            SimpleDateFormatter sdf = new SimpleDateFormatter(pattern, null);
            Date d = sdf.parse(input);
            assertEquals("custom:" + pattern, expected, d);
            
            // try with the standard parser too
            try
            {
                SimpleDateFormat sf = new SimpleDateFormat(pattern, locale);
                Date d2 = sf.parse(input);
                assertEquals("std:" + pattern, expected, d2);
            }
            catch(java.text.ParseException e)
            {
                // thrown when the input does not match the pattern
                assertEquals("std:" + pattern, null, expected);
            }
            catch(IllegalArgumentException e)
            {
                // thrown when the pattern is not value
                assertEquals("std:" + pattern, null, expected);
            }
        }
    }

    // try to format with various combinations, and see what we get
    public void testFormatAssorted() throws Exception
    {
        Date d = new Date();
        d.setYear(1987 - 1900);
        d.setMonth(2);
        d.setDate(12);
        d.setHours(4);
        d.setMinutes(23);
        d.setSeconds(59);
        
        String[] data =
        {
            // test standard, with literals
            "yyyy-MM-dd", "1987-03-12",
            
            // test standard, with multichar literal sequences: any non-alpha
            "yyyy--MM-:()dd", "1987--03-:()12",
            
            // test standard, with non-pattern chars.--> error
            "yyyyTMMTdd", null,
            
            // test standard, with non-pattern chars.
            "yyyy'T'MM'T'dd", "1987T03T12",
            
            // test quoted text
            "yyyy'year'MM'month'dd", "1987year03month12",
        };

        Locale locale = Locale.ENGLISH;
        for(int i=0; i<data.length; i+=2)
        {
            String pattern = data[i];
            String expected = data[i+1];

            // format it with our code, and check against expected.
            SimpleDateFormatter sdf = new SimpleDateFormatter(pattern, null);
            String s = sdf.format(d);
            assertEquals("custom:" + pattern, expected, s);

            // try with the standard parser too
            try
            {
                SimpleDateFormat sf = new SimpleDateFormat(pattern, locale);
                String s2 = sf.format(d);
                assertEquals("std:" + pattern, expected, s2);
            }
            catch(IllegalArgumentException e)
            {
                // thrown when the pattern is not value
                assertEquals("std:" + pattern, null, expected);
            }
        }
    }

    // for a wide range of dates, compare the output of
    //   SimpleDateFormatter.getIsoWeekNumber
    //   SimpleDateFormatter.getJavaWeekNumber (with firstOfWeek=monday)
    //   Joda DateTime class
    //
    // Of course this comparison can ONLY be done for firstDayOfWeek=monday.
    public void testWeekFormatAgainstJoda() throws Exception
    {
        applyTests(new DateTest()
        {
            public void test(int year, int month, int day)
            {
                checkWeekFormatAgainstJoda(year, month, day);
            }
        });
    }

    private void checkWeekFormatAgainstJoda(int year, int month, int day)
    {
        Date date = new Date(year-1900, month, day);
        DateTime jdt = new DateTime(date.getTime());
        int jodaWeekOfWeekyear = jdt.getWeekOfWeekyear();
        int jodaWeekyear = jdt.getWeekyear();

        SimpleDateFormatter.WeekDate iwd = SimpleDateFormatter.getIsoWeekNumber(date);

        // the java.util.Date convention is that 1 = monday
        int firstDayOfWeek = 1;
        SimpleDateFormatter.WeekDate jwd = SimpleDateFormatter.getJavaWeekNumber(date, firstDayOfWeek);

        String ds = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println(
            ds + ":"
            + "(" + jodaWeekyear + "-" + jodaWeekOfWeekyear + ")"
            + ",(" + iwd.year + "-" + iwd.week + ")"
            + ",(" + jwd.year + "-" + jwd.week + ")"
            );
        assertEquals(jodaWeekyear, iwd.year);
        assertEquals(jodaWeekOfWeekyear, iwd.week);
        assertEquals(jodaWeekyear, jwd.year);
        assertEquals(jodaWeekOfWeekyear, jwd.week);
    }

    /**
     * Execute the specified test against a range of dates selected to
     * find problems. The dates around the beginning and end of each year
     * are verified, plus a few in the middle.
     */
    private void applyTests(DateTest dt) throws Exception
    {
        // for every year from 2000-2010, test:
        //   1-8 jan jan
        //   29,30 may,
        //   1-8 june
        //   23-31 dec
         for(int year = 2000; year < 2020; ++year)
         {
             dt.test(year, 0, 1);
             dt.test(year, 0, 2);
             dt.test(year, 0, 3);
             dt.test(year, 0, 4);
             dt.test(year, 0, 5);
             dt.test(year, 0, 6);
             dt.test(year, 0, 7);
             dt.test(year, 0, 8);

             dt.test(year, 4, 29);
             dt.test(year, 4, 30);
             dt.test(year, 5, 1);
             dt.test(year, 5, 2);
             dt.test(year, 5, 3);
             dt.test(year, 5, 4);
             dt.test(year, 5, 5);
             dt.test(year, 5, 6);
             dt.test(year, 5, 7);
             dt.test(year, 5, 8);

             dt.test(year, 11, 23);
             dt.test(year, 11, 24);
             dt.test(year, 11, 25);
             dt.test(year, 11, 26);
             dt.test(year, 11, 27);
             dt.test(year, 11, 28);
             dt.test(year, 11, 29);
             dt.test(year, 11, 30);
             dt.test(year, 11, 31);
         }
    }

    public void xtestWeekFormatSimple()
    {
        // get the supported ids for UTC
         String[] ids = TimeZone.getAvailableIDs(0);
         // if no ids were returned, something is wrong. get out.
         if (ids.length == 0)
             System.exit(0);

         // create a Pacific Standard Time time zone
         SimpleTimeZone pdt = new SimpleTimeZone(0, ids[0]);

         GregorianCalendar calendar = new GregorianCalendar(pdt);
         calendar.setFirstDayOfWeek(2); // 1=sunday, 2=monday, 7=saturday
         calendar.setMinimalDaysInFirstWeek(4);
        
         for(int year = 2000; year < 2020; ++year)
         {
             tryDate(calendar, year, 0, 1);
             tryDate(calendar, year, 0, 2);
             tryDate(calendar, year, 0, 3);
             tryDate(calendar, year, 0, 4);
             tryDate(calendar, year, 0, 5);
             tryDate(calendar, year, 0, 6);
             tryDate(calendar, year, 0, 7);
             tryDate(calendar, year, 0, 8);

             tryDate(calendar, year, 5, 1);

             tryDate(calendar, year, 11, 23);
             tryDate(calendar, year, 11, 24);
             tryDate(calendar, year, 11, 25);
             tryDate(calendar, year, 11, 26);
             tryDate(calendar, year, 11, 27);
             tryDate(calendar, year, 11, 28);
             tryDate(calendar, year, 11, 29);
             tryDate(calendar, year, 11, 30);
             tryDate(calendar, year, 11, 31);
         }
    }
    
    private void tryDate(GregorianCalendar cal, int year, int month, int day)
    {
        cal.set(year, month, day);
        Date date = cal.getTime();

        int javaWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

        // aargh, JODA does not support firstDayOfWeek functionality; it only
        // supports the ISO standard which is first-day=monday
        DateTime jdt = new DateTime(date.getTime());
        int jodaWeekOfYear = jdt.getWeekOfWeekyear();

        // aargh, Date.getDay uses 0=sun,1=mon,6=sat
        // but Calendar.getFirstDayOfWeek uses 1=sun, 2=mon, 7=sat
        int firstDayOfWeek = cal.getFirstDayOfWeek() - 1;
        SimpleDateFormatter.WeekDate wd = SimpleDateFormatter.getJavaWeekNumber(date, firstDayOfWeek);

        String ds = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println(ds + ":" + javaWeekOfYear + ":" + jodaWeekOfYear + ":" + wd.week);
        // assertEquals(javaWeekOfYear, myWeekOfYear);
    }

    // test sanity of java.text.Calendar: convert a date to "ww/yyyy" format, then
    // parse it back again and ensure the year is correct.
    private void tryDate3(GregorianCalendar cal, int year, int month, int day) throws Exception
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-ww-E");

        Date d = new Date(year - 1900, month, day);
        String s1 = df.format(d);
        Date d2 = df.parse(s1);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
        String dstr = df2.format(d);
        String d2str =df2.format(d2);
        
        System.out.println(dstr + ":" + s1 + ":" + d2str + ":");
    }
}