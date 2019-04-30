package br.com.zupfilms.ui.singleton;

import br.com.zupfilms.server.response.FilmGenres;

public enum SingletonFilmGenres {

    INSTANCE;

    private FilmGenres filmGenres;

    private void setGenreList(FilmGenres filmGenres) {
        this.filmGenres = filmGenres;
    }

    public static void createFilmGenres(FilmGenres filmGenres){
        SingletonFilmGenres.INSTANCE.setGenreList(filmGenres);
    }

    public FilmGenres getFilmGenres(){
        if(filmGenres != null){
            return filmGenres;
        }
        return null;
    }
}
