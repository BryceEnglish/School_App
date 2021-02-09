package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity {
    static final String LOG_TAG = "TermDetAct";
    ListView lvCourses;
    TextView tvTermStart;
    TextView tvTermEnd;
    TextView tvTermTitle;
    Intent intent;
    int termId;
    CompleteDatabase db;
    Term selectedTerm;
    SimpleDateFormat formatter;

    @Override
    protected void onResume() {
        super.onResume();
        selectedTerm = db.termDAO().getTerm(termId);
        updateCourseList();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        db = CompleteDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Term Details");
        termId = intent.getIntExtra("termId", -1);
        Log.d(TermDetailsActivity.LOG_TAG, "TermId passed In: " + termId);
        selectedTerm = db.termDAO().getTerm(termId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //Attach Views
        lvCourses = findViewById(R.id.lvCourses);
        tvTermStart = findViewById(R.id.tvTermStart);
        tvTermEnd = findViewById(R.id.tvTermEnd);
        tvTermTitle = findViewById(R.id.tvTermTitle);
        //End Attach Views

        updateViews();

        //List Click Listener
        lvCourses.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("Course Clicked: " + position);
            Intent intent = new Intent(getApplicationContext(), CourseDetailsActivity.class);
            int courseId = db.courseDAO().getCourseList(termId).get(position).getCourse_id();
            intent.putExtra("termId", termId);
            intent.putExtra("courseId", courseId);

            startActivity(intent);
        });
        //End List Click Listener

        updateCourseList();

        //FAB Add Stuff
        FloatingActionButton addCourseFAB = findViewById(R.id.addAssessmentFAB);
        addCourseFAB.setOnClickListener((v) -> {
            Calendar calendar = Calendar.getInstance();
            int dbCount = db.courseDAO().getCourseList(termId).size() + 1;
            Course tempCourse = new Course();
            tempCourse.setCourse_name("Course Added " + dbCount);
            tempCourse.setCourse_start(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tempCourse.setCourse_end(calendar.getTime());
            tempCourse.setCourse_status("Active Status");
            tempCourse.setTerm_id_fk(termId);
            db.courseDAO().insertCourse(tempCourse);
            updateCourseList();
            System.out.println("addCourseFAB clicked");
        });
        //End FAB Add Stuff
        //FAB Edit Term
        FloatingActionButton editTermFAB = findViewById(R.id.fabEditTerm);
        editTermFAB.setOnClickListener((v) -> {
            System.out.println("FAB Edit stuff pressed. ");
            Term tempTerm = db.termDAO().getTerm(termId);
            System.out.println("Current Term Name: " + tempTerm.getTerm_name());
            Intent intent = new Intent(getApplicationContext(), EditTermActivity.class);
            intent.putExtra("termId", termId);
            startActivity(intent);
        });
        //End FAB Edit Term
    }

    private void updateCourseList() {
        List<Course> allCourses = new ArrayList<>();
        try {
            allCourses = db.courseDAO().getCourseList(termId);
            System.out.println("Number of Rows in Query: " + allCourses.size());
        } catch (Exception e) {System.out.println("could not pull query");}

        String[] items = new String[allCourses.size()];
        if(!allCourses.isEmpty()){
            for (int i = 0; i < allCourses.size(); i++) {
                items[i] = allCourses.get(i).getCourse_name();
                System.out.println("Inside updateList loop: " + i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvCourses.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateViews() {
        if (selectedTerm != null) {
            Log.d(TermDetailsActivity.LOG_TAG, "selected Term is Not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();
            System.out.println("Millisecond Date: " + startDate.toString());
            String temp = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted Date: " + temp);
            tvTermStart.setText(temp);
            tvTermEnd.setText(tempEnd);
            tvTermTitle.setText(selectedTerm.getTerm_name());
        } else {
            Log.d(TermDetailsActivity.LOG_TAG, "selected Term is Null");
            selectedTerm = new Term();
        }
        String newTitle = "Term Details: " + selectedTerm.getTerm_name();
        setTitle(newTitle);
    }
}