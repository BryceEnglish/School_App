package BE.school_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Term.class, Course.class, Coursementor.class, Assessment.class}, exportSchema = false, version = 2)
@TypeConverters({Converters.class})
public abstract class CompleteDatabase extends RoomDatabase {

    private static final String DB_NAME = "complete_db";
    private static CompleteDatabase instance;

    public static synchronized CompleteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CompleteDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract CoursementorDAO coursementorDAO();
    public abstract AssessmentDAO assessmentDAO();
}
