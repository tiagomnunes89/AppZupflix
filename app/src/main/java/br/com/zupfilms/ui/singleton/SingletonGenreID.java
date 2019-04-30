package br.com.zupfilms.ui.singleton;

public enum SingletonGenreID {

    INSTANCE;

    private String genreID;

    private void setGenreID(String genreID) {
        this.genreID = genreID;
    }

    public static void setGenreIDEntered(String genreID){
        SingletonGenreID.INSTANCE.setGenreID(genreID);
    }

    public String getGenreID(){
        if(genreID != null && !genreID.isEmpty()){
            return genreID;
        }
        return null;
    }
}
