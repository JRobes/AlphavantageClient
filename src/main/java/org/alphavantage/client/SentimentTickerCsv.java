package org.alphavantage.client;

public class SentimentTickerCsv {
    private String ticker;
    private String date;
    private String ticker_sentiment_label;
    private double ticker_sentiment_score;
    private double relevance_score;
    private String summary;

    public SentimentTickerCsv(String ticker, String date, String ticker_sentiment_label, double ticker_sentiment_score, double relevance_score, String summary) {
        this.ticker = ticker;
        this.date = date;
        this.ticker_sentiment_label = ticker_sentiment_label;
        this.ticker_sentiment_score = ticker_sentiment_score;
        this.relevance_score = relevance_score;
        this.summary = summary;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicker_sentiment_label() {
        return ticker_sentiment_label;
    }

    public void setTicker_sentiment_label(String ticker_sentiment_label) {
        this.ticker_sentiment_label = ticker_sentiment_label;
    }

    public double getTicker_sentiment_score() {
        return ticker_sentiment_score;
    }

    public void setTicker_sentiment_score(double ticker_sentiment_score) {
        this.ticker_sentiment_score = ticker_sentiment_score;
    }

    public double getRelevance_score() {
        return relevance_score;
    }

    public void setRelevance_score(double relevance_score) {
        this.relevance_score = relevance_score;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
