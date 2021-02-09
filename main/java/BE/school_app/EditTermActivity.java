package BE.school_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTermActivity extends AppCompatActivity {
    static final String LOG_TAG = "EditTermAct";
    TextView txtTermTitle;
    TextView txtTermStart;
    TextView txtTermEnd;
    Button btnSaveEditTerm;
    Button btnCancelEditTerm;
    Button btnDelTerm;
    CompleteDatabase db;
    Intent intent;
    Term selectedTerm;
    SimpleDateFormat formatter;
    int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        db = CompleteDatabase.getInstance(getApplicationContext());
        setTitle("Edit Term Activity");
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        //Attach Views
        txtTermTitle = findViewById(R.id.txtTermTitle);
        txtTermStart = findViewById(R.id.txtTermStart);
        txtTermEnd = findViewById(R.id.txtTermEnd);
        //End Attach Views
        Intent intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        selectedTerm = db.termDAO().getTerm(termId);

        updateViews();

        //Save Edit Term Btn
        btnSaveEditTerm = findViewById(R.id.btnSaveEditTerm);
        btnSaveEditTerm.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            String tempStart = txtTermStart.getText().toString();
            try {
                Date startDate = formatter.parse(tempStart);
                selectedTerm.setTerm_start(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tempEnd = txtTermEnd.getText().toString();
            try {
                Date endDate = formatter.parse(tempEnd);
                selectedTerm.setTerm_end(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedTerm.setTerm_name(txtTermTitle.getText().toString());
            db.termDAO().updateTerm(selectedTerm);
            finish();
        });
        //End Save Edit Term Btn
        //Cancel Edit Term Btn
        btnCancelEditTerm = findViewById(R.id.btnCancelEditTerm);
        btnCancelEditTerm.setOnClickListener(v -> {
            finish();
        });
        //End Cancel Edit Term Btn
        //Delete Term Btn
        btnDelTerm = findViewById(R.id.btnDelTerm);
        btnDelTerm.setOnClickListener(v -> {
            List<Course> assocCourses = db.courseDAO().getCourseList(termId);
            if (assocCourses.size() == 0) {
                db.termDAO().deleteTerm(selectedTerm);
                System.out.println("No Assigned Courses, deletion successful.");
                Intent nextScreen  = new Intent(getApplicationContext(), TermListActivity.class);
                startActivity(nextScreen);
            } else {
                System.out.println("There are " + assocCourses.size() + " course(s) assigned to this term.");
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Error");
                alert.setMessage("Cannot delete term with assigned courses. This Term has " + assocCourses.size() + " courses assigned.");
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("OK button clicked");
                    }
                });
                alert.show();
            }
        });
        //End Delete Term Btn
    }

    private void updateViews() {
        if (selectedTerm != null) {
            Log.d(EditTermActivity.LOG_TAG, "selected Term is Not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();
            String temp = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted Date: " + temp);
            txtTermTitle.setText(selectedTerm.getTerm_name());
            txtTermStart.setText(temp);
            txtTermEnd.setText(tempEnd);
        } else {
            Log.d(TermDetailsActivity.LOG_TAG, "selected Term is Null");
            selectedTerm = new Term();
        }
        String newTitle = "Edit Term: " + selectedTerm.getTerm_name();
        setTitle(newTitle);
    }
}