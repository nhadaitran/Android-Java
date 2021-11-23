package vn.edu.stu.library.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import vn.edu.stu.library.dao.bookDAO;
import vn.edu.stu.library.dao.categoryDAO;
import vn.edu.stu.library.entity.bookEntity;
import vn.edu.stu.library.entity.categoryEntity;

@Database(entities = {bookEntity.class, categoryEntity.class}, version = 1, exportSchema = false)
@TypeConverters(BitmapUtil.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract bookDAO bookDAO();
    public abstract categoryDAO categoryDAO();



    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DBConfigUtil.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
