package BE.school_app;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditAssessmentActivity extends AppCompatActivity {
    static final String LOG_TAG = "EditAsmtAct";
    static final String LOG_INFO = "AlertReminder Event";
    TextView txtAsmtType;
    TextView txtAsmtTitle;
    TextView txtAsmtDueDate;
    TextView txtAsmtInfo;
    TextView txtAlarmDate;
    Button btnApplyAlarm;
    Button btnDelAsmt;
    Button btnSaveAsmt;
    CompleteDatabase db;
    Assessment selectedAsmt;
    Term selectedTerm;
    Course selectedCourse;
    SimpleDateFormat formatter;
    int courseId;
    int termId;
    int asmtId;
    int NOTIFY_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        db = CompleteDatabase.getInstance(getApplicationContext());
        setTitle("Edit Assessment Activity");
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //Attach Views
        txtAsmtType = findViewById(R.id.txtAsmtType);
        txtAsmtTitle = findViewById(R.id.txtAsmtTitle);
        txtAsmtDueDate = findViewById(R.id.txtAsmtDueDate);
        txtAsmtInfo = findViewById(R.id.txtAsmtInfo);
        txtAlarmDate = findViewById(R.id.txtAlarmDate);
        //End Attach Views

        Intent intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        asmtId = intent.getIntExtra("asmtId", -1);
        selectedAsmt = db.assessmentDAO().getAssessment(asmtId);
        selectedTerm = db.termDAO().getTerm(termId);
        selectedCourse = db.courseDAO().getCourse(termId, courseId);

        updateViews();
        notifyChannel();

        //Save Edit Asmt Btn
        btnSaveAsmt = findViewById(R.id.btnSaveAsmt);
        btnSaveAsmt.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            String tempDue = txtAsmtDueDate.getText().toString();
            try {
                Date dueDate = formatter.parse(tempDue);
                selectedAsmt.setAssessment_duedate(dueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedAsmt.setAssessment_type(txtAsmtType.getText().toString());
            selectedAsmt.setAssessment_title(txtAsmtTitle.getText().toString());
            selectedAsmt.setAssessment_notes(txtAsmtInfo.getText().toString());
            db.assessmentDAO().updateAssessment(selectedAsmt);
            finish();
        });
        //End Save Edit Asmt Btn
        //Delete Edit Asmt Btn
        btnDelAsmt = findViewById(R.id.btnDelAsmt);
        btnDelAsmt.setOnClickListener(v -> {
            db.assessmentDAO().deleteAssessment(selectedAsmt);
            finish();
        });
        //End Delete Edit Asmt Btn
        //Alarm Btn Listener
        btnApplyAlarm = findViewById(R.id.btnApplyAlarm);
        btnApplyAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_INFO, "Set Alert (click");

                Calendar calDate = Calendar.getInstance();
                calDate.add(Calendar.DAY_OF_YEAR, -1);
                Date alertDate = calDate.getTime();
                try {
                    alertDate = formatter.parse(txtAlarmDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d(LOG_INFO, "Set Alert Date Problem");
                }
                setAlert(alertDate);
                saveAlertInfo();
                finish();
            }
        });
        //End Alarm Btn Listener
    }

    private void setAlert(Date calDateProvided) {
        Calendar calDateNow = Calendar.getInstance();
        if (calDateProvided.getTime() > calDateNow.getTime().getTime()) {
            Log.d(LOG_INFO, formatter.format(calDateProvided) + " Notify Date");
            Intent sendIntent = new Intent(getApplicationContext(), AlertReminder.class);
            sendIntent.putExtra("my_title", "Important Date");
            sendIntent.putExtra("my_message", "Assessment:  " + txtAsmtTitle.getText().toString() + " is Due\n" + " Date and Time: " + formatter.format(calDateProvided));
            NOTIFY_ID = (int) (Math.random() * 1000);
            sendIntent.putExtra("notify_id", NOTIFY_ID);
            PendingIntent thisPendIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFY_ID, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alertManage = (AlarmManager) getSystemService(ALARM_SERVICE);
            alertManage.set(AlarmManager.RTC, calDateProvided.getTime(), thisPendIntent);
            Toast.makeText(getApplicationContext(), "Alert set for : " + txtAsmtTitle.getText().toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), formatter.format(calDateProvided) + " is a PAST DATE or TIME", Toast.LENGTH_LONG).show();
        }
    }

    private  void saveAlertInfo() {
        try {
            selectedAsmt.setAssessment_alarmdate(formatter.parse(txtAlarmDate.getText().toString()));
            db.assessmentDAO().updateAssessment(selectedAsmt);
            Log.d(LOG_INFO, "Adjusted Goal Date");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.assessmentDAO().updateAssessment(selectedAsmt);
    }

    private void updateViews() {
        if (selectedAsmt != null) {
            Log.d(EditAssessmentActivity.LOG_TAG, "selected Assessment is Not null");
            Date duedate = selectedAsmt.getAssessment_duedate();
            Date alarmdate = selectedAsmt.getAssessment_alarmdate();
            String tempDue = formatter.format(duedate);
            String tempAlarm = formatter.format(alarmdate);
            txtAsmtType.setText(selectedAsmt.getAssessment_type());
            txtAsmtTitle.setText(selectedAsmt.getAssessment_title());
            txtAsmtInfo.setText(selectedAsmt.getAssessment_notes());
            txtAsmtDueDate.setText(tempDue);
            txtAlarmDate.setText(tempAlarm);
        } else {
            Log.d(EditAssessmentActivity.LOG_TAG, "selected Assessment is Null");
            selectedAsmt = new Assessment();
        }
        String newTitle = "Edit Assessment: " + selectedAsmt.getAssessment_title();
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