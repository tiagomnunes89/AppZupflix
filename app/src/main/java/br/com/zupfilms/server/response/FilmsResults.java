package br.com.zupfilms.server.response;

import java.util.List;

public class FilmsResults {

    private Integer page;
    private Integer total_results;
    private Integer total_pages;

    private List<FilmResponse> results;

    public List<FilmResponse> getResults() {
        return results;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }


}
