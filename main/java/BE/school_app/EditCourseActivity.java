package BE.school_app;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditCourseActivity extends AppCompatActivity {
    static final String LOG_TAG = "EditCourseAct";
    static final String LOG_INFO = "AlertReminder Event";
    TextView txtCourseTitle;
    TextView txtCourseStart;
    TextView txtCourseEnd;
    TextView txtCourseStatus;
    Button btnStartAlarm;
    Button btnEndAlarm;
    Button btnDelCourse;
    Button btnSaveCourse;
    CompleteDatabase db;
    Intent intent;
    Term selectedTerm;
    Course selectedCourse;
    SimpleDateFormat formatter;
    int courseId;
    int termId;
    int NOTIFY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        db = CompleteDatabase.getInstance(getApplicationContext());
        setTitle("Edit Course Activity");
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //Attach Views
        txtCourseTitle = findViewById(R.id.txtCourseTitle);
        txtCourseStart = findViewById(R.id.txtCourseStart);
        txtCourseEnd = findViewById(R.id.txtCourseEnd);
        txtCourseStatus = findViewById(R.id.txtCourseStatus);
        //End Attach Views

        Intent intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        selectedTerm = db.termDAO().getTerm(termId);
        selectedCourse = db.courseDAO().getCourse(termId, courseId);

        updateViews();
        notifyChannel();

        //Save Edit Course Btn
        btnSaveCourse = findViewById(R.id.btnSaveCourse);
        btnSaveCourse.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            String tempStart = txtCourseStart.getText().toString();
            try {
                Date startDate = formatter.parse(tempStart);
                selectedCourse.setCourse_start(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tempEnd = txtCourseEnd.getText().toString();
            try {
                Date endDate = formatter.parse(tempEnd);
                selectedCourse.setCourse_end(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedCourse.setCourse_name(txtCourseTitle.getText().toString());
            selectedCourse.setCourse_status(txtCourseStatus.getText().toString());
            db.courseDAO().updateCourse(selectedCourse);
            finish();
        });
        //End Save Edit Course Btn
        //Delete Course Btn
        btnDelCourse = findViewById(R.id.btnDelCourse);
        btnDelCourse.setOnClickListener(v -> {
            db.courseDAO().deleteCourse(selectedCourse);
            Intent i = new Intent(getApplicationContext(), TermDetailsActivity.class);
            startActivity(i);
        });
        //End Delete Course Btn
        //Start Date Alarm Btn Listener
        btnStartAlarm = findViewById(R.id.btnStartAlarm);
        btnStartAlarm.setOnClickListener(view -> {
            Log.d(LOG_INFO, "Set Start Alert (click)");

            Calendar calDate = Calendar.getInstance();
            calDate.add(Calendar.DAY_OF_YEAR, -1);
            Date alertDate = calDate.getTime();
            try {
                alertDate = formatter.parse(txtCourseStart.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(LOG_INFO, "Set Alert Date Problem");
            }
            setAlert(alertDate);
            saveAlertInfo();
            //finish();
        });
        //End Start Date Alarm Btn Listener
        //Finsih Date Alarm Btn Listener
        btnEndAlarm = findViewById(R.id.btnEndAlarm);
        btnEndAlarm.setOnClickListener(view -> {
            Log.d(LOG_INFO, "Set End Alert (click)");

            Calendar calDate = Calendar.getInstance();
            calDate.add(Calendar.DAY_OF_YEAR, -1);
            Date alertDate = calDate.getTime();
            try {
                alertDate = formatter.parse(txtCourseEnd.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(LOG_INFO, "Set Alert Date Problem");
            }
            setAlert(alertDate);
            saveAlertInfo();
            //finish();
        });
        //End Finish Date Alarm Btn Listener
    }

    private void setAlert(Date calDateProvided) {
        Calendar calDateNow = Calendar.getInstance();
        if (calDateProvided.getTime() > calDateNow.getTime().getTime()) {
            Log.d(LOG_INFO, formatter.format(calDateProvided) + " Notify Date");
            Intent sendIntent = new Intent(getApplicationContext(), AlertReminder.class);
            sendIntent.putExtra("my_title", "Important Date");
            sendIntent.putExtra("my_message", "Course:  " + txtCourseTitle.getText().toString() + " is Due\n" + " Date and Time: " + formatter.format(calDateProvided));
            sendIntent.putExtra("notify_id", NOTIFY_ID);
            PendingIntent thisPendIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFY_ID, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alertManage = (AlarmManager) getSystemService(ALARM_SERVICE);
            alertManage.set(AlarmManager.RTC, calDateProvided.getTime(), thisPendIntent);
            Toast.makeText(getApplicationContext(), "Alert set for : " + txtCourseTitle.getText().toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), formatter.format(calDateProvided) + " is a PAST DATE or TIME", Toast.LENGTH_LONG).show();
        }
    }

    private  void saveAlertInfo() {
        try {
            selectedCourse.setCourse_start(formatter.parse(txtCourseStart.getText().toString()));
            selectedCourse.setCourse_end(formatter.parse(txtCourseEnd.getText().toString()));
            db.courseDAO().updateCourse(selectedCourse);
            Log.d(LOG_INFO, "Adjusted Goal Date");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.courseDAO().updateCourse(selectedCourse);
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(EditCourseActivity.LOG_TAG, "selected Course is Not null");
            Date startDate = selectedCourse.getCourse_start();
            Date endDate = selectedCourse.getCourse_end();
            String temp = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted Date: " + temp);
            txtCourseTitle.setText(selectedCourse.getCourse_name());
            txtCourseStart.setText(temp);
            txtCourseEnd.setText(tempEnd);
            txtCourseStatus.setText(selectedCourse.getCourse_status());
        } else {
            Log.d(TermDetailsActivity.LOG_TAG, "selected Course is Null");
            selectedCourse = new Course();
        }
        String newTitle = "Edit Course: " + selectedCourse.getCourse_name();
        setTitle(newTitle);
    }

    private void notifyChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence dgw_channel_name = "DGW Tracker NAME";
            String dgw_channel_description = "DGW Tracker DESC";
            int dgw_channel_importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notifyChannel = new NotificationChannel("dgwChanId", dgw_channel_name, dgw_channel_importance);
            notifyChannel.setDescription(dgw_channel_description);

            NotificationManager notifyManager = getSystemService(NotificationManager.class);
            notifyManager.createNotificationChannel(notifyChannel);
            Log.d(LOG_INFO, "Notification Channel Set");
        }
    }
}