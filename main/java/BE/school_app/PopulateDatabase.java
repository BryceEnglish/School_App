package BE.school_app;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class PopulateDatabase extends AppCompatActivity {
    public static String LOG_TAG = "PopData";
    Term tempTerm1 = new Term();
    Term tempTerm2 = new Term();
    Term tempTerm3 = new Term();
    Course tempCourse1 = new Course();
    Course tempCourse2 = new Course();
    Course tempCourse3 = new Course();
    Assessment tempAssessment1 = new Assessment();
    Assessment tempAssessment2 = new Assessment();
    Assessment tempAssessment3 = new Assessment();
    Coursementor tempCourseMentor1 = new Coursementor();
    Coursementor tempCourseMentor2 = new Coursementor();
    Coursementor tempCourseMentor3 = new Coursementor();
    CompleteDatabase db;

    public void populate(Context context) {
        db = CompleteDatabase.getInstance(context);
        try {
            insertTerms();
            insertCourses();
            insertAssessments();
            insertCoursementors();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Populate DB Failed");
        }
    }

    private void insertTerms() {
        Calendar start;
        Calendar end;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 1);
        tempTerm1.setTerm_name("Fall 2020");
        tempTerm1.setTerm_start(start.getTime());
        tempTerm1.setTerm_end(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH,2);
        end.add(Calendar.MONTH, 5);
        tempTerm2.setTerm_name("Spring 2021");
        tempTerm2.setTerm_start(start.getTime());
        tempTerm2.setTerm_end(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 6);
        end.add(Calendar.MONTH, 9);
        tempTerm3.setTerm_name("Fall 2021");
        tempTerm3.setTerm_start(start.getTime());
        tempTerm3.setTerm_end(end.getTime());

        db.termDAO().insertAll(tempTerm1, tempTerm2, tempTerm3);
    }

    private void insertCourses() {
        Calendar start;
        Calendar end;
        List<Term> termList = db.termDAO().getTermList();
        if (termList == null) return;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, -1);
        tempCourse1.setCourse_name("C482 - Software I");
        tempCourse1.setCourse_start(start.getTime());
        tempCourse1.setCourse_end(end.getTime());
        tempCourse1.setCourse_notes("PrePopulate Notes - data data data Notes Notes Notes");
        tempCourse1.setCourse_status("Completed");
        tempCourse1.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        tempCourse2.setCourse_name("C195 - Software II");
        tempCourse2.setCourse_start(start.getTime());
        tempCourse2.setCourse_end(end.getTime());
        tempCourse2.setCourse_notes("PrePopulate Notes - data data data Notes Notes Notes");
        tempCourse2.setCourse_status("Completed");
        tempCourse2.setTerm_id_fk(termList.get(1).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        end.add(Calendar.MONTH, 1);
        tempCourse3.setCourse_name("C196 - Mobile Application Development");
        tempCourse3.setCourse_start(start.getTime());
        tempCourse3.setCourse_end(end.getTime());
        tempCourse3.setCourse_notes("PrePopulate Notes - data data data Notes Notes Notes");
        tempCourse3.setCourse_status("In-Progress");
        tempCourse3.setTerm_id_fk(termList.get(2).getTerm_id());

        db.courseDAO().insertCourse(tempCourse1);
        db.courseDAO().insertCourse(tempCourse2);
        db.courseDAO().insertCourse(tempCourse3);
    }

    private void insertAssessments() {
        Calendar duedate;
        Calendar alarmdate;
        List<Course> courseList = db.courseDAO().getAllCourses();
        if (courseList == null) return;

        duedate = Calendar.getInstance();
        alarmdate = Calendar.getInstance();
        duedate.add(Calendar.MONTH, -2);
        alarmdate.add(Calendar.MONTH, -2);
        tempAssessment1.setAssessment_type("Performance");
        tempAssessment1.setAssessment_title("Software I Test");
        tempAssessment1.setAssessment_status("Pending");
        tempAssessment1.setAssessment_notes("Notes go here");
        tempAssessment1.setAssessment_duedate(duedate.getTime());
        tempAssessment1.setAssessment_alarmdate(alarmdate.getTime());
        tempAssessment1.setCourse_id_fk(courseList.get(0).getCourse_id());

        duedate = Calendar.getInstance();
        alarmdate = Calendar.getInstance();
        duedate.add(Calendar.MONTH, -1);
        alarmdate.add(Calendar.MONTH, -1);
        tempAssessment2.setAssessment_type("Objective");
        tempAssessment2.setAssessment_title("Software II Test");
        tempAssessment2.setAssessment_status("Passed");
        tempAssessment2.setAssessment_notes("Notes go here");
        tempAssessment2.setAssessment_duedate(duedate.getTime());
        tempAssessment2.setAssessment_alarmdate(alarmdate.getTime());
        tempAssessment2.setCourse_id_fk(courseList.get(1).getCourse_id());

        duedate = Calendar.getInstance();
        alarmdate = Calendar.getInstance();
        duedate.add(Calendar.MONTH, -3);
        alarmdate.add(Calendar.MONTH, -3);
        tempAssessment3.setAssessment_type("Objective");
        tempAssessment3.setAssessment_title("C196 Test");
        tempAssessment3.setAssessment_status("Failed");
        tempAssessment3.setAssessment_notes("Notes go here");
        tempAssessment3.setAssessment_duedate(duedate.getTime());
        tempAssessment3.setAssessment_alarmdate(alarmdate.getTime());
        tempAssessment3.setCourse_id_fk(courseList.get(2).getCourse_id());

        db.assessmentDAO().insertAll(tempAssessment1, tempAssessment2, tempAssessment3);
    }

    private void insertCoursementors() {
        List<Course> courseList = db.courseDAO().getAllCourses();
        if (courseList == null) return;

        tempCourseMentor1.setCoursementor_name("Malcolm Wabara");
        tempCourseMentor1.setCoursementor_phone("877-435-7948");
        tempCourseMentor1.setCoursementor_email("malcolm.wabara@wgu.edu");
        tempCourseMentor1.setCourse_id_fk(courseList.get(0).getCourse_id());

        tempCourseMentor2.setCoursementor_name("Bruce Johnson");
        tempCourseMentor2.setCoursementor_phone("877-435-7948");
        tempCourseMentor2.setCoursementor_email("b.johnson1@wgu.edu");
        tempCourseMentor2.setCourse_id_fk(courseList.get(1).getCourse_id());

        tempCourseMentor3.setCoursementor_name("Alvaro Escobar");
        tempCourseMentor3.setCoursementor_phone("877-435-7948");
        tempCourseMentor3.setCoursementor_email("alvaro.escobar@wgu.edu");
        tempCourseMentor3.setCourse_id_fk(courseList.get(2).getCourse_id());

        db.coursementorDAO().insertCoursementors(tempCourseMentor1);
        db.coursementorDAO().insertCoursementors(tempCourseMentor2);
        db.coursementorDAO().insertCoursementors(tempCourseMentor3);
    }
}
