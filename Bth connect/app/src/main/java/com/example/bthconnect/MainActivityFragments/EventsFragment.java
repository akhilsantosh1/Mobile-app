package com.example.bthconnect.MainActivityFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bthconnect.MainActivity;
import com.example.bthconnect.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

public class EventsFragment extends Fragment{
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener childEventListener;

    private int index = 0;
    private Button[] eventButtons = new Button[8];
    private Button backBtn;
    Button createEventBtn;
    LinearLayout linearLayout;
    String[] idList = new String[8];
    ValueEventListener[] valueEventListeners = new ValueEventListener[8];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity1_update, container, false);
        final Context context = getContext();
        if(context == null)return view;

        linearLayout = (LinearLayout) view.findViewById(R.id.xmlActivity1Linear);

        backBtn = (Button) view.findViewById(R.id.activity1_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        createEventBtn = (Button) view.findViewById(R.id.activity1_create_event);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(5);
            }
        });

        initializeButtons(view);




        return view;
    }

    void initializeButtons(View view){
        eventButtons[0] = (Button)view.findViewById(R.id.act1button0);
        eventButtons[1] = (Button)view.findViewById(R.id.act1button1);
        eventButtons[2] = (Button)view.findViewById(R.id.act1button2);
        eventButtons[3] = (Button)view.findViewById(R.id.act1button3);
        eventButtons[4] = (Button)view.findViewById(R.id.act1button4);
        eventButtons[5] = (Button)view.findViewById(R.id.act1button5);
        eventButtons[6] = (Button)view.findViewById(R.id.act1button6);
        eventButtons[7] = (Button)view.findViewById(R.id.act1button7);
    }

    public void initEventsFragment(){
        index = 0;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("events");

        if(childEventListener != null)
        {
            myRef.removeEventListener(childEventListener);
        }

        childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String id = dataSnapshot.getKey();
                final String eventName = dataSnapshot.child("event_name").getValue(String.class);
                final String eventTime = dataSnapshot.child("event_time").getValue(String.class);
                final String eventDate = dataSnapshot.child("event_date").getValue(String.class);
                final String eventLocation = dataSnapshot.child("event_location").getValue(String.class);

                if(index > 7)index = 0;

                eventButtons[index].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)getActivity()).initPollVoteFragment(id, eventName, eventTime, eventDate, eventLocation);
                        ((MainActivity)getActivity()).setViewPager(9);
                    }
                });
                idList[index] = id;
                eventButtons[index++].setText(eventName);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void initChildListeners(){
        database = FirebaseDatabase.getInstance();

        for(int i = 0; i < 8; ++i)
        {
            final int indexCopy = i;

            if(idList[i] != null)
            {
                myRef = database.getReference(idList[i]);

                if(valueEventListeners[i] != null)
                {
                    myRef.removeEventListener(valueEventListeners[i]);
                }

                // Attach a listener to read the data at our posts reference
                valueEventListeners[i] = myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numChildren = (int)dataSnapshot.getChildrenCount();
                        eventButtons[indexCopy].setText(eventButtons[indexCopy].getText() + ", participants: " + Integer.toString(numChildren));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });

            }
        }


    }
}