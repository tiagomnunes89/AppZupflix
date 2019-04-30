package br.com.zupfilms.server.response;

import java.util.List;

public class FilmsResults {

    private final Integer page;
    private final Integer total_results;
    private final Integer total_pages;

    private final List<FilmResponse> results;

    public FilmsResults(Integer page, Integer total_results, Integer total_pages, List<FilmResponse> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public List<FilmResponse> getResults() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }


}
