package com.dane.dni;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dane on 12/7/2015.
 */
public class HolidayXmlParser {

    public ArrayList<DniHoliday> getHolidays(InputStream in)
            throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return processFeed(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<DniHoliday> processFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException{
        ArrayList<DniHoliday> holidays = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "Holidays");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Holiday")) {
                holidays.add(readHoliday(parser));
            } else {
                skip(parser);
            }
        }
        return holidays;
    }

    private DniHoliday readHoliday(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Holiday");
        String name = parser.getAttributeValue(null, "name");
        long vailee = DniDateTime.Vailee.valueOf(
                parser.getAttributeValue(null, "vailee").toUpperCase())
                .ordinal();
        long yahr = Long.parseLong(parser.getAttributeValue(null, "yahr"));
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, "Holiday");
        return new DniHoliday(name, vailee, yahr);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
