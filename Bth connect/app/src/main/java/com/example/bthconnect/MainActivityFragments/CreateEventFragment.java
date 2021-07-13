package com.example.bthconnect.MainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bthconnect.MainActivity;
import com.example.bthconnect.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateEventFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener childEventListener;
    private Button button2;
    private Button backBtn;
    private Button btn_createPoll;
    private EditText event_name;
    private EditText event_time;
    private EditText event_date;
    private EditText event_location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity2_update, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("events");

        backBtn = (Button)view.findViewById(R.id.student_list_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).initEventsFragment();
                ((MainActivity)getActivity()).setViewPager(4);
            }
        });

        btn_createPoll = (Button)view.findViewById(R.id.activity2_create_poll);
        btn_createPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(event_name.getText().toString().equals("") && event_time.getText().toString().equals("")
                && event_date.getText().toString().equals("") && event_location.getText().toString().equals("")))
                {

                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put("event_name", event_name.getText().toString());
                    taskMap.put("event_time", event_time.getText().toString());
                    taskMap.put("event_date", event_date.getText().toString());
                    taskMap.put("event_location", event_location.getText().toString());
                    myRef.push().setValue(taskMap);

                    event_name.setText("");
                    event_time.setText("");
                    event_location.setText("");
                    event_date.setText("");

                    Toast.makeText(getActivity(), "Event uploaded!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        event_name = (EditText)view.findViewById(R.id.xmlEventName2);
        event_time = (EditText)view.findViewById(R.id.xmlEventTime2);
        event_date = (EditText)view.findViewById(R.id.xmlEventDate2);
        event_location = (EditText)view.findViewById(R.id.xmlEventLocation2);

        return view;
    }

}
