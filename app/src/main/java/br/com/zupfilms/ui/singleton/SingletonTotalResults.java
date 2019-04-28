package br.com.zupfilms.ui.singleton;

public enum SingletonTotalResults {

    INSTANCE;

    private Integer totalResults;

    private void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public static void setTotalResultsEntered(Integer totalResults){
        SingletonTotalResults singletonTotalResults = SingletonTotalResults.INSTANCE;
        singletonTotalResults.setTotalResults(totalResults);
    }

    public Integer getTotalResults(){
        if(totalResults != null ){
            return totalResults;
        }
        return null;
    }
}
