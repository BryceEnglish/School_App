package BE.school_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class TermListActivity extends AppCompatActivity {
    public static String LOG_TAG = "TermListActivityLog";
    CompleteDatabase db;
    ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        setTitle("Term List Activity");
        listView = findViewById(R.id.lvMentors);
        db = CompleteDatabase.getInstance(getApplicationContext());

        // On Click Listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("Position Clicked: " + position);
            Intent intent = new Intent(getApplicationContext(), TermDetailsActivity.class);
            int term_id;
            List<Term> termsList = db.termDAO().getTermList();
            term_id = termsList.get(position).getTerm_id();
            intent.putExtra("termId", term_id);
            System.out.println("termID selected = " + String.valueOf(term_id));
            startActivity(intent);
        });
        // End On Click Listener

        updateList();

        //FAB Things
        FloatingActionButton addTermFAB = findViewById(R.id.fabAddTerm);
        addTermFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Term tempTerm1 = new Term();
                tempTerm1.setTerm_name("Term Added");
                tempTerm1.setTerm_start(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
                tempTerm1.setTerm_end(calendar.getTime());
                db.termDAO().insertTerm(tempTerm1);
                updateList();
            }
        });
        //End FAB Things
    }

    private void updateList() {
        List<Term> allTerms = db.termDAO().getTermList();
        System.out.println("Number of Rows in Terms Table: " + allTerms.size());

        String[] items = new String[allTerms.size()];
        if(!allTerms.isEmpty()){
            for (int i = 0; i < allTerms.size(); i++) {
                items[i] = allTerms.get(i).getTerm_name();
                System.out.println("Term in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}