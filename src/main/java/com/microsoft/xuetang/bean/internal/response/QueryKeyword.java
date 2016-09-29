package com.microsoft.xuetang.bean.internal.response;

/**
 * Created by jiash on 8/30/2016.
 */
public class QueryKeyword {
    private String term;
    private int startOffset;
    private int endOffset;

    public QueryKeyword() {}

    public QueryKeyword(String term, int startOffset, int endOffset) {
        this.term = term;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public String toString() {
        return String.format("Term: %s. StartOffset: %d. EndOffset: %d", term == null ? "" : term, startOffset, endOffset);
    }
}
