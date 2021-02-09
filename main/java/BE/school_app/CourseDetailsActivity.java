package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity {
    static final String LOG_TAG = "CourseDetAct";
    ListView lvAsmts;
    TextView tvCourseTitle;
    TextView tvCourseStart;
    TextView tvCourseEnd;
    TextView tvCourseStatus;
    Intent intent;
    CompleteDatabase db;
    Term selectedTerm;
    Course selectedCourse;
    Assessment selectedAsmt;
    SimpleDateFormat formatter;
    int courseId;
    int termId;
    int asmtId;

    @Override
    protected void onResume() {
        super.onResume();
        selectedTerm = db.termDAO().getTerm(termId);
        selectedCourse = db.courseDAO().getCourse(termId, courseId);
        updateAssessmentsList();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        db = CompleteDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Course Details");
        courseId = getIntent().getExtras().getInt("courseId");
        termId = getIntent().getExtras().getInt("termId");
        asmtId = getIntent().getExtras().getInt("asmtId");
        selectedCourse = db.courseDAO().getCourse(termId, courseId);
        selectedAsmt = db.assessmentDAO().getAssessment(asmtId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //Attach Views
        lvAsmts = findViewById(R.id.lvAsmts);
        tvCourseTitle = findViewById(R.id.tvCourseTitle);
        tvCourseStart = findViewById(R.id.tvCourseStart);
        tvCourseEnd = findViewById(R.id.tvCourseEnd);
        tvCourseStatus = findViewById(R.id.tvCourseStatus);
        //End Attach Views

        updateViews();

        //List Click Listener
        lvAsmts.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("Assessment Clicked: " + position);
            Intent intent = new Intent(getApplicationContext(), EditAssessmentActivity.class);
            int asmtId = db.assessmentDAO().getAssessmentList(courseId).get(position).getAssessment_id();
            int courseId = selectedCourse.getCourse_id();
            intent.putExtra("asmtId", asmtId);
            intent.putExtra("courseId", courseId);

            startActivity(intent);
        });
        //End List Click Listener

        updateAssessmentsList();

        //FAB Add Stuff
        FloatingActionButton addAssessmentFAB = findViewById(R.id.addAssessmentFAB);
        addAssessmentFAB.setOnClickListener((v) -> {
            Calendar calendar = Calendar.getInstance();
            int dbCount = db.assessmentDAO().getAssessmentList(courseId).size() + 1;
            Assessment tempAssessment = new Assessment();
            tempAssessment.setAssessment_type("Performance");
            tempAssessment.setAssessment_title("Assessment Added " + dbCount);
            tempAssessment.setAssessment_duedate(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tempAssessment.setAssessment_status("Pending");
            tempAssessment.setAssessment_notes("");
            tempAssessment.setAssessment_alarmdate(calendar.getTime());
            tempAssessment.setCourse_id_fk(courseId);
            db.assessmentDAO().insertAssessments(tempAssessment);
            updateAssessmentsList();
            System.out.println("addAssessmentFAB clicked");
        });
        //End FAB Add Stuff
        //FAB Edit Course
        FloatingActionButton editTermFAB = findViewById(R.id.fabEditTerm);
        editTermFAB.setOnClickListener((v) -> {
            System.out.println("FAB Edit stuff pressed. ");
            Term tempTerm = db.termDAO().getTerm(termId);
            System.out.println("Current Term Name: " + tempTerm.getTerm_name());
            Intent intent = new Intent(getApplicationContext(), EditCourseActivity.class);
            intent.putExtra("termId", termId);
            intent.putExtra("courseId", courseId);
            intent.putExtra("asmtId", asmtId);

            startActivity(intent);
        });
        //End FAB Edit Course
        //Course Mentors Btn Start
        Button btnCourseMentors = findViewById(R.id.btnCourseMentors);
        btnCourseMentors.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MentorsListActivity.class);
            int courseId = selectedCourse.getCourse_id();
            intent.putExtra("courseId", courseId);

            startActivity(intent);
        });
        //Course Mentors Btn End
        //Course Notes Btn Start
        Button btnCourseNotes = findViewById(R.id.btnCourseNotes);
        btnCourseNotes.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CourseNotesActivity.class);
            int termId = selectedTerm.getTerm_id();
            int courseId = selectedCourse.getCourse_id();
            intent.putExtra("termId", termId);
            intent.putExtra("courseId", courseId);
            System.out.println(String.valueOf(termId));
            System.out.println(String.valueOf(courseId));

            startActivity(intent);
        });
        //Course Notes Btn End
    }

    private void updateAssessmentsList() {
        List<Assessment> allAssessments = new ArrayList<>();
        try {
            allAssessments = db.assessmentDAO().getAssessmentList(courseId);
            System.out.println("Number of Rows in Course Query: " + allAssessments.size());
        } catch (Exception e) {System.out.println("could not pull query");}

        String[] items = new String[allAssessments.size()];
        if(!allAssessments.isEmpty()){
            for (int i = 0; i < allAssessments.size(); i++) {
                items[i] = allAssessments.get(i).getAssessment_title();
                System.out.println("Inside updateList loop: " + i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvAsmts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(CourseDetailsActivity.LOG_TAG, "selected Course is Not null");
            Date startDate = selectedCourse.getCourse_start();
            Date endDate = selectedCourse.getCourse_end();
            System.out.println("Millisecond Date: " + startDate.toString());
            String temp = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted Date: " + temp);
            tvCourseTitle.setText(selectedCourse.getCourse_name());
            tvCourseStart.setText(temp);
            tvCourseEnd.setText(tempEnd);
            tvCourseStatus.setText(selectedCourse.getCourse_status());
        } else {
            Log.d(CourseDetailsActivity.LOG_TAG, "selected Course is Null");
            selectedCourse = new Course();
        }
        String newTitle = "Course Details: " + selectedCourse.getCourse_name();
        setTitle(newTitle);
    }
}