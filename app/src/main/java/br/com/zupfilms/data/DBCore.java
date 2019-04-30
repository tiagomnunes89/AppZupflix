package br.com.zupfilms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    class DBCore extends SQLiteOpenHelper {
        private static final String BD_NAME = "movies_favorites";
        private static final int VERSION = 7;

        public DBCore(Context ctx){
            super(ctx, BD_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase bd) {
            bd.execSQL("CREATE TABLE favorites( " +
                    "movie_id integer PRIMARY KEY," +
                    " poster_path text," +
                    " backdrop_path text," +
                    " vote_average real," +
                    " title text NOT NULL," +
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
            bd.execSQL("DROP TABLE favorites;");
            onCreate(bd);
        }
}
