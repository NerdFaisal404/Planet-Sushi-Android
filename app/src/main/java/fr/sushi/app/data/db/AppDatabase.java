package fr.sushi.app.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public abstract class AppDatabase extends RoomDatabase {
    private String dpName = "palanet_db";
    private static AppDatabase appDatabase;

    public AppDatabase on(Context context) {
        if (appDatabase == null) {
            appDatabase = buildDatabase(context);
        }
        return appDatabase;
    }

    private AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dpName)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                }).build();
    }
}
