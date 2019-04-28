package br.com.zupfilms.server.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.zupfilms.model.Film;
import br.com.zupfilms.server.response.FilmResponse;

public class FilmMapper {

    public static List<Film> reponseForDomain(List<FilmResponse> listFilmResponse){
        List<Film> filmList = new ArrayList<>();

        for(FilmResponse filmsResponse : listFilmResponse){
            final Film film = new Film(filmsResponse.getOriginalTitle(), filmsResponse.getPosterPath(),
                    filmsResponse.getVoteAverage(),filmsResponse.getOverview(),filmsResponse.getReleaseDate(), filmsResponse.getGenreIds());
            filmList.add(film);
        }
        return filmList;
    }
}
