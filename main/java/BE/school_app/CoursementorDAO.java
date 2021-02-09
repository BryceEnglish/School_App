package BE.school_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CoursementorDAO {
    @Query("SELECT * FROM coursementor_table WHERE course_id_fk = :courseId ORDER BY coursementor_id")
    List<Coursementor> getCoursementorList(int courseId);

    @Query("SELECT * FROM coursementor_table WHERE course_id_fk = :courseId AND coursementor_id = :coursementorId")
    Coursementor getCoursementor(int coursementorId, int courseId);

    @Query("INSERT INTO coursementor_table (course_id_fk, coursementor_name)\n" +
           "VALUES(:courseId, \"Mentor Name\"); ")
    void addCoursementor(int courseId);

    @Query("SELECT * FROM coursementor_table")
    List<Coursementor> getAllCoursementors();

    @Insert
    void insertCoursementors(Coursementor coursementor);

    @Insert
    void insertAll(Coursementor coursementor);

    @Update
    void updateCoursementor(Coursementor coursementor);

    @Delete
    void deleteCoursementor(Coursementor coursementor);

    @Query("DELETE FROM coursementor_table")
    public void deleteCoursementorTable();
}
