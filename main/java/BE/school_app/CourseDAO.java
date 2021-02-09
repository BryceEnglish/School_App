package BE.school_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId ORDER BY course_id")
    List<Course> getCourseList(int termId);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId AND course_id = :courseId")
    Course getCourse(int termId, int courseId);

    @Query("INSERT INTO course_table (term_id_fk, course_name)\n" +
           "VALUES(:termId, \"Course Name\"); ")
    void addCourse(int termId);

    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();

    @Insert
    void insertCourse(Course course);

    @Insert
    void insertAll(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM course_table")
    public void deleteCourseTable();
}
