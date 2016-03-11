package com.nethergrim.borg.data;

import com.nethergrim.borg.entities.Quote;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Andrey Drobyazko (c2q9450@gmail.com).
 *         All rights reserved.
 */
public class BashorgParser {

    public static final String INPUT = "input";
    public static final String CLASS = "class";
    public static final String PAGE = "page";
    public static final String VALUE = "value";
    public static final String MAX = "max";
    public static final String A = "a";
    public static final String ID = "id";
    public static final String DIV = "div";
    public static final String TEXT = "text";
    public static final String BR = "<br>";
    public static final String EMPTY_STRING = "";
    public static final String SPAN = "span";
    public static final String DATE = "date";
    public static final String RATING = "rating";
    public static final String THREE_DOTS = "...";
    public static final String QUESTION = "?";
    public static final String URI_QUOTE = "content://com.nethergrim.bashorg.web.uri.QUOTE";
    public static final String URL_BASHORG_PAGE = "http://bash.im/index/";
    public static final String URL_BASHORG_TOP = "http://bash.im/byrating/";
    public static final String URL_BASHORG_RANDOM_PAGE = "http://bash.im/random";
    private static final Random _random = new Random();
    private static final int DEFAULT_PAGE_SIZE = 50;

    public static List<Quote> getPage(int pageNumber) {
        if (pageNumber == 0) {
            return new ArrayList<Quote>();
        }
        return BashorgParser.parsePage(String.valueOf(pageNumber));
    }

    public static List<Quote> parsePage(final String pageNumber) {
        try {
            Document document = Jsoup.connect(URL_BASHORG_PAGE + pageNumber).get();
            return parseDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Quote>();
        }
    }

    public static List<Quote> parsePageFromTop(int byRatingPage) {
        try {
            Document document = Jsoup.connect(URL_BASHORG_TOP + String.valueOf(byRatingPage)).get();
            return parseDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Quote>();
        }
    }


    public static List<Quote> parseRandomPage() {
        try {
            Document document = Jsoup.connect(
                    URL_BASHORG_RANDOM_PAGE + QUESTION + _random.nextLong()).get();
            return parseDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Quote>();
        }
    }

    public static int getPageNumber(Document document) {
        Elements elements = document.select(INPUT);
        for (int i = 0, size = elements.size(); i < size; i++) {
            Element element = elements.get(i);
            if (element.attr(CLASS).equals(PAGE)) {
                String currentPage = element.attr(VALUE);
                String maxPage = element.attr(MAX);
                return Integer.parseInt(currentPage);
            }
        }
        return -1;
    }

    public static long[] getIds(Document document) {
        long[] array = new long[DEFAULT_PAGE_SIZE];
        int counter = 0;
        Elements numberElements = document.select(A);
        for (int i = 0, size = numberElements.size(); i < size; i++) {
            Element numberElement = numberElements.get(i);
            if (numberElement.attr(CLASS).equals(ID)) {
                String text = numberElement.html();
                text = text.substring(1, text.length());
                array[counter] = Long.parseLong(text);
                counter++;
            }
        }
        return array;
    }

    public static String[] getTexts(Document document) {
        String[] array = new String[DEFAULT_PAGE_SIZE];
        int counter = 0;
        Elements divs = document.select(DIV);

        for (int i = 0, size = divs.size(); i < size; i++) {
            Element element = divs.get(i);
            if (element.attr(CLASS).equals(TEXT)) {
                String text = element.html();
                text = text.replace(BR, EMPTY_STRING);
                text = StringEscapeUtils.unescapeHtml4(text);
                array[counter] = text;
                counter++;
            }
        }
        return array;
    }

    public static String[] getDates(Document document) {
        String[] array = new String[DEFAULT_PAGE_SIZE];
        int counter = 0;
        Elements divs = document.select(SPAN);

        for (int i = 0, size = divs.size(); i < size; i++) {
            Element element = divs.get(i);
            if (element.attr(CLASS).equals(DATE)) {
                String text = element.html();
                array[counter] = text;
                counter++;
            }
        }
        return array;
    }

    public static int[] getRatings(Document document) {
        int[] array = new int[DEFAULT_PAGE_SIZE];
        Elements spans = document.select(SPAN);
        int counter = 0;

        for (int i = 0, size = spans.size(); i < size; i++) {
            Element span = spans.get(i);
            if (span.attr(CLASS).equals(RATING)) {
                if (span.html().equals(THREE_DOTS)) {
                    array[counter] = 0;
                    continue;
                }
                try {
                    String rating = span.html();
                    array[counter] = Integer.parseInt(rating);
                } catch (NumberFormatException e) {
                    array[counter] = 0;
                    e.printStackTrace();
                }
                counter++;
            }
        }
        return array;
    }

    private static List<Quote> parseDocument(Document document) {
        List<Quote> result = new ArrayList<Quote>(100);
        try {
            long[] ids = getIds(document);
            int[] ratings = getRatings(document);
            String[] texts = getTexts(document);
            String[] dates = getDates(document);
            int size = ids.length;
            for (int i = 0; i < size; i++) {
                Quote quote = new Quote(ids[i], texts[i], ratings[i], dates[i]);
                result.add(quote);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}