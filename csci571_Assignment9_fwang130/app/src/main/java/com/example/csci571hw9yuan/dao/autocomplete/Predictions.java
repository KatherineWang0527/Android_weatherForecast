package com.example.csci571hw9yuan.dao.autocomplete;

import java.util.List;

public class Predictions {
    String description;
    List<Terms> terms;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Terms> getTerms() {
        return terms;
    }

    public void setTerms(List<Terms> terms) {
        this.terms = terms;
    }
}
