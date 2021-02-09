package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MentorsListActivity extends AppCompatActivity {
    static final String LOG_TAG = "MentorListAct";
    ListView lvMentors;
    CompleteDatabase db;
    Intent intent;
    int courseId;

    @Override
    protected void onResume() {
        super.onResume();
        updateMentorList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentors_list);
        db = CompleteDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        setTitle("Course Mentors");
        courseId = getIntent().getExtras().getInt("courseId", -1);
        lvMentors = findViewById(R.id.lvMentors);

        //List Click Listener
        lvMentors.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("Mentor Clicked: " + position);
            Intent intent = new Intent(getApplicationContext(), EditMentorActivity.class);
            int mentorId;
            List<Coursementor> coursementorList = db.coursementorDAO().getCoursementorList(courseId);
            mentorId = coursementorList.get(position).getCoursementor_id();
            intent.putExtra("mentorId", mentorId);
            intent.putExtra("courseId", courseId);
            System.out.println("mentorId selected = " + String.valueOf(mentorId));
            System.out.println("courseId selected = " + String.valueOf(courseId));

            startActivity(intent);
        });
        //End List Click Listener

        updateMentorList();

        //FAB Add Stuff
        FloatingActionButton addMentorFAB = findViewById(R.id.addMentorFAB);
        addMentorFAB.setOnClickListener((v) -> {
            int dbCount = db.coursementorDAO().getCoursementorList(courseId).size() + 1;
            Coursementor tempCoursementor = new Coursementor();
            tempCoursementor.setCoursementor_name("Mentor Added " + dbCount);
            tempCoursementor.setCoursementor_email("email");
            tempCoursementor.setCoursementor_phone("Phone");
            tempCoursementor.setCourse_id_fk(courseId);
            db.coursementorDAO().insertCoursementors(tempCoursementor);
            updateMentorList();
            System.out.println("addMentorFAB clicked");
        });
        //End FAB Add Stuff
    }

    private void updateMentorList() {
        List<Coursementor> allMentors = new ArrayList<>();
        try {
            allMentors = db.coursementorDAO().getCoursementorList(courseId);
            System.out.println("Number of Rows in Query: " + allMentors.size());
        } catch (Exception e) {System.out.println("could not pull query");}

        String[] items = new String[allMentors.size()];
        if(!allMentors.isEmpty()){
            for (int i = 0; i < allMentors.size(); i++) {
                items[i] = allMentors.get(i).getCoursementor_name();
                System.out.println("Inside updateList loop: " + i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvMentors.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

