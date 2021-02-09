package BE.school_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "coursementor_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = CASCADE
        )
)
public class Coursementor {
    @PrimaryKey(autoGenerate = true)
    private int coursementor_id;
    @ColumnInfo(name = "course_id_fk")
    private int course_id_fk;
    @ColumnInfo(name = "coursementor_name")
    private String coursementor_name;
    @ColumnInfo(name = "coursementor_email")
    private String coursementor_email;
    @ColumnInfo(name = "coursementor_phone")
    private String coursementor_phone;

    public int getCoursementor_id() {
        return coursementor_id;
    }

    public void setCoursementor_id(int coursementor_id) {
        this.coursementor_id = coursementor_id;
    }

    public int getCourse_id_fk() {
        return course_id_fk;
    }

    public void setCourse_id_fk(int course_id_fk) {
        this.course_id_fk = course_id_fk;
    }

    public String getCoursementor_name() {
        return coursementor_name;
    }

    public void setCoursementor_name(String coursementor_name) {
        this.coursementor_name = coursementor_name;
    }

    public String getCoursementor_email() {
        return coursementor_email;
    }

    public void setCoursementor_email(String coursementor_email) {
        this.coursementor_email = coursementor_email;
    }

    public String getCoursementor_phone() {
        return coursementor_phone;
    }

    public void setCoursementor_phone(String coursementor_phone) {
        this.coursementor_phone = coursementor_phone;
    }
}
