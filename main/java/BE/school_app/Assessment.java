package BE.school_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = CASCADE
        )
)
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessment_id;
    @ColumnInfo(name = "course_id_fk")
    private int course_id_fk;
    @ColumnInfo(name = "assessment_type")
    private String assessment_type;
    @ColumnInfo(name = "assessment_title")
    private String assessment_title;
    @ColumnInfo(name = "assessment_status")
    private String assessment_status;
    @ColumnInfo(name = "assessment_notes")
    private String assessment_notes;
    @ColumnInfo(name = "assessment_duedate")
    private  Date assessment_duedate;
    @ColumnInfo(name = "assessment_alarmdate")
    private  Date assessment_alarmdate;

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }

    public String getAssessment_title() {
        return assessment_title;
    }

    public void setAssessment_title(String assessment_title) {
        this.assessment_title = assessment_title;
    }

    public String getAssessment_status() {
        return assessment_status;
    }

    public void setAssessment_status(String assessment_status) {
        this.assessment_status = assessment_status;
    }

    public String getAssessment_notes() {
        return assessment_notes;
    }

    public void setAssessment_notes(String assessment_notes) {
        this.assessment_notes = assessment_notes;
    }

    public Date getAssessment_duedate() { return assessment_duedate; }

    public void setAssessment_duedate(Date assessment_duedate) {
        this.assessment_duedate = assessment_duedate;
    }

    public Date getAssessment_alarmdate() { return assessment_alarmdate; }

    public void setAssessment_alarmdate(Date assessment_alarmdate) {
        this.assessment_alarmdate = assessment_alarmdate;
    }
}
