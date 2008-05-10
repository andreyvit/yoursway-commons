package com.yoursway.utils;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractor {
    
    public final static Pattern WORD = compile("^\\w+");
    
    public final static Pattern WHITESPACE = compile("^\\s+");
    
    private String data;
    
    private final Pattern autoSkip;
    
    public StringExtractor(String data, Pattern autoSkip) {
        if (data == null)
            throw new NullPointerException("data is null");
        this.data = data;
        this.autoSkip = autoSkip;
    }
    
    public String requireAsString(Pattern pattern) {
        Matcher match = require(pattern);
        return (match == null ? null : match.group());
    }
    
    public String extractAsString(Pattern pattern) {
        Matcher match = extract(pattern);
        return (match == null ? null : match.group());
    }
    
    public Matcher require(Pattern pattern) {
        Matcher result = extract(pattern);
        if (result == null)
            throw new IllegalArgumentException("Pattern " + pattern + " was expected to match " + data);
        return result;
    }
    
    public Matcher extract(Pattern pattern) {
        Matcher matcher = pattern.matcher(data);
        if (!matcher.find() || matcher.start() > 0)
            return null;
        String result = matcher.group();
        data = data.substring(matcher.end());
        if (autoSkip != null)
            skip(autoSkip);
        return matcher;
    }
    
    public String requireWord() {
        return requireAsString(WORD);
    }
    
    public String extractWord() {
        return extractAsString(WORD);
    }
    
    public void skip(Pattern pattern) {
        Matcher matcher = pattern.matcher(data);
        if (matcher.find() && matcher.start() == 0)
            data = data.substring(matcher.end());
    }
    
    public void skipWhitespace() {
        skip(WHITESPACE);
    }
    
    public void mustBeEnd() {
        if (data.length() > 0)
            throw new IllegalArgumentException("Extra data in the string: " + data);
    }
    
}
