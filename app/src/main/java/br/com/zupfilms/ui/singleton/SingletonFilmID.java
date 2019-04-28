package br.com.zupfilms.ui.singleton;

public enum SingletonFilmID {

    INSTANCE;

    private Integer ID;

    private void setID(Integer ID) {
        this.ID = ID;
    }

    public static void setIDEntered(Integer id){
        SingletonFilmID singletonID = SingletonFilmID.INSTANCE;
        singletonID.setID(id);
    }

    public Integer getID(){
        if(ID != null ){
            return ID;
        }
        return null;
    }
}
