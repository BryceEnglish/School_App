package BE.school_app;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AssessmentDAO {
    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseId ORDER BY assessment_id")
    List<Assessment> getAssessmentList(int courseId);

    @Query("SELECT * FROM assessment_table WHERE assessment_id = :assessmentId ORDER BY assessment_id")
    Assessment getAssessment(int assessmentId);

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();

    @Insert
    void insertAssessments(Assessment assessment);

    @Insert
    void insertAll(Assessment... assessment);

    @Update
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    public void deleteAssessmentTable();
}
