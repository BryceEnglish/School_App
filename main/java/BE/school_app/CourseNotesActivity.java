package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CourseNotesActivity extends AppCompatActivity {
    static final String LOG_TAG = "CourseNotesAct";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    EditText mlNotes;
    EditText txtPhone;
    Button btnSaveNotes;
    Button btnSendSMS;
    CompleteDatabase db;
    Intent intent;
    Course selectedCourse;
    int courseId;
    int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes);
        db = CompleteDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Course Notes");
        courseId = getIntent().getExtras().getInt("courseId", -1);
        termId = getIntent().getExtras().getInt("termId", -1);
        selectedCourse = db.courseDAO().getCourse(termId, courseId);
        //Attach Views
        mlNotes = findViewById(R.id.mlNotes);
        txtPhone = findViewById(R.id.txtPhone);
        //End Attach Views

        updateViews();

        // Save Course Notes Btn
        btnSaveNotes = findViewById(R.id.btnSaveNotes);
        btnSaveNotes.setOnClickListener(v -> {
            selectedCourse.setCourse_notes(mlNotes.getText().toString());
            db.courseDAO().updateCourse(selectedCourse);
            finish();
        });
        // End Save Course Notes Btn
        // Send SMS Btn
        btnSendSMS = findViewById(R.id.btnSendSMS);
        btnSendSMS.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mlNotes.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Send Message Title");
            sendIntent.putExtra("address", txtPhone.getText().toString());
            sendIntent.setType("text/plain");

            startActivity(sendIntent);
        });
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(CourseNotesActivity.LOG_TAG, "selected Course is Not null");
            mlNotes.setText(selectedCourse.getCourse_notes());
        } else {
            Log.d(TermDetailsActivity.LOG_TAG, "selected Term is Null");
            selectedCourse = new Course();
        }
        String newTitle = "Course Notes: " + selectedCourse.getCourse_name();
        setTitle(newTitle);
    }
}
