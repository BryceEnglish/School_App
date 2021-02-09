package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static String LOG_TAG = "HomePage";
    CompleteDatabase db;
    Button btnEnter;
    TextView tvPendingCourses;
    TextView tvCompletedCourses;
    TextView tvDroppedCourses;
    TextView tvPendingAsmts;
    TextView tvPassedAsmts;
    TextView tvFailedAsmts;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = CompleteDatabase.getInstance(getApplicationContext());
        //Attach Views
        tvPendingCourses = findViewById(R.id.tvPendingCourses);
        tvCompletedCourses = findViewById(R.id.tvCompletedCourses);
        tvDroppedCourses = findViewById(R.id.tvDroppedCourses);
        tvPendingAsmts = findViewById(R.id.tvPendingAsmts);
        tvPassedAsmts = findViewById(R.id.tvPassedAsmts);
        tvFailedAsmts = findViewById(R.id.tvFailedAsmts);
        //End Attach Views

        updateViews();

        //Enter Button Functionality
        btnEnter = findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermListActivity.class);
                startActivity(intent);
            }
        });
        //End Enter Button Functionality
        //Create Delete DB Button
        ConstraintLayout myLayout = findViewById(R.id.homeConstraintLayout);
        ConstraintSet set = new ConstraintSet();
        Button deleteDBbtn = new Button(getApplicationContext());
        deleteDBbtn.setText("Delete DB Tables");
        deleteDBbtn.setId(R.id.deleteDBbtn);

        set.constrainHeight(deleteDBbtn.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(deleteDBbtn.getId(), ConstraintSet.WRAP_CONTENT);
        set.connect(deleteDBbtn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
        set.connect(deleteDBbtn.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);

        myLayout.addView(deleteDBbtn);
        setContentView(myLayout);
        set.applyTo(myLayout);

        deleteDBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Delete DB Tables Button Pressed");
                db.clearAllTables();
                updateViews();
            }
        });
        //End Create Delete DB Button
        //Create Populate DB Button
        Button popDBbtn = new Button(getApplicationContext());
        popDBbtn.setText("Populate Database");
        popDBbtn.setId(R.id.popDBbtn);

        set.constrainHeight(popDBbtn.getId(), ConstraintSet.WRAP_CONTENT);
        set.constrainWidth(popDBbtn.getId(), ConstraintSet.WRAP_CONTENT);
        set.connect(popDBbtn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
        set.connect(popDBbtn.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);

        myLayout.addView(popDBbtn);
        setContentView(myLayout);
        set.applyTo(myLayout);

        popDBbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Populate Database Button Pressed");
                PopulateDatabase populateDatabase = new PopulateDatabase();
                populateDatabase.populate(getApplicationContext());
                updateViews();
            }
        });
        //End Create Populate DB Button
    }

    private void updateViews() {
        int coursesPending = 0;
        int coursesCompleted = 0;
        int coursesDropped = 0;
        int assessmentsPending = 0;
        int assessmentsPassed = 0;
        int assessmentsFailed = 0;
        try {
            List<Term> termList = db.termDAO().getAllTerms();
            List<Course> courseList = db.courseDAO().getAllCourses();
            List<Assessment> assessmentList = db.assessmentDAO().getAllAssessments();

            try {
                for(int i = 0; i < courseList.size(); i++) {
                    if(courseList.get(i).getCourse_status().contains("Pending")) coursesPending++;
                    if(courseList.get(i).getCourse_status().contains("In-Progress")) coursesPending++;
                    if(courseList.get(i).getCourse_status().contains("Completed")) coursesCompleted++;
                    if(courseList.get(i).getCourse_status().contains("Dropped")) coursesDropped++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < assessmentList.size(); i++) {
                if(assessmentList.get(i).getAssessment_status().contains("Pending")) assessmentsPending++;
                if(assessmentList.get(i).getAssessment_status().contains("Passed")) assessmentsPassed++;
                if(assessmentList.get(i).getAssessment_status().contains("Failed")) assessmentsFailed++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvPendingCourses.setText(String.valueOf(coursesPending));
        tvCompletedCourses.setText(String.valueOf(coursesCompleted));
        tvDroppedCourses.setText(String.valueOf(coursesDropped));
        tvPendingAsmts.setText(String.valueOf(assessmentsPending));
        tvPassedAsmts.setText(String.valueOf(assessmentsPassed));
        tvFailedAsmts.setText(String.valueOf(assessmentsFailed));
    }
}