package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EditMentorActivity extends AppCompatActivity {
    static final String LOG_TAG = "EditMentorAct";
    TextView txtMentorName;
    TextView txtMentorPhone;
    TextView txtMentorEmail;
    Button btnDelMentor;
    Button btnSaveMentor;
    CompleteDatabase db;
    Intent intent;
    Coursementor selectedMentor;
    int mentorId;
    int courseId;

    @Override
    protected void onResume() {
        super.onResume();
        selectedMentor = db.coursementorDAO().getCoursementor(mentorId, courseId);
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mentor);
        db = CompleteDatabase.getInstance(getApplicationContext());
        setTitle("Edit Mentor Activity");
        //Attach Views
        txtMentorName = findViewById(R.id.txtMentorName);
        txtMentorPhone = findViewById(R.id.txtMentorPhone);
        txtMentorEmail = findViewById(R.id.txtMentorEmail);
        //End Attach Views

        Intent intent = getIntent();
        mentorId = intent.getIntExtra("mentorId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        System.out.println("MentorId = " + String.valueOf(mentorId));
        System.out.println("CourseId = " + String.valueOf(courseId));
        selectedMentor = db.coursementorDAO().getCoursementor(mentorId, courseId);

        updateViews();

       //Save Mentor Btn
        btnSaveMentor = findViewById(R.id.btnSaveMentor);
        btnSaveMentor.setOnClickListener(v -> {
            selectedMentor.setCoursementor_name(txtMentorName.getText().toString());
            selectedMentor.setCoursementor_phone(txtMentorPhone.getText().toString());
            selectedMentor.setCoursementor_email(txtMentorEmail.getText().toString());
            db.coursementorDAO().updateCoursementor(selectedMentor);
            finish();
        });
        //End Save Mentor Btn
        //Del Mentor Btn
        btnDelMentor = findViewById(R.id.btnDelMentor);
        btnDelMentor.setOnClickListener(v -> {
            db.coursementorDAO().deleteCoursementor(selectedMentor);
            finish();
        });
        //End Del Mentor Btn
    }

    private void updateViews() {
        if (selectedMentor != null) {
            Log.d(EditMentorActivity.LOG_TAG, "selected Mentor is Not null");
            txtMentorName.setText(selectedMentor.getCoursementor_name());
            txtMentorPhone.setText(selectedMentor.getCoursementor_phone());
            txtMentorEmail.setText(selectedMentor.getCoursementor_email());
        } else {
            Log.d(EditMentorActivity.LOG_TAG, "selected Mentor is Null");
            selectedMentor = new Coursementor();
        }
        String newTitle = "Edit Mentor: " + selectedMentor.getCoursementor_name();
        setTitle(newTitle);
    }
}