package br.com.zupfilms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class DBCore extends SQLiteOpenHelper {
        private static final String BD_NAME = "movies_favorites";
        private static final int VERSION = 7;

        public DBCore(Context ctx){
            super(ctx, BD_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase bd) {
            bd.execSQL("create table favorites( " +
                    "movie_id integer primary key," +
                    " poster_path text," +
                    " backdrop_path text," +
                    " vote_average real," +
                    " title text not null," +
                    " release_date text," +
                    " genres text," +
                    " runtime integer," +
                    " overview text," +
                    " production_countries text," +
                    " tagline text," +
                    " vote_count integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase bd, int arg1, int arg2) {
            bd.execSQL("drop table favorites;");
            onCreate(bd);
        }
}
