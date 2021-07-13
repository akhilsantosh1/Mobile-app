package com.example.bthconnect.MainActivityFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bthconnect.MainActivity;
import com.example.bthconnect.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentListFragment extends Fragment {

    ScrollView scrollView;
    LinearLayout linearLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener childEventListener;
    Button back_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.student_list_fragment, container, false);
        back_button = (Button)view.findViewById(R.id.student_list_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });
        final Context context = getContext();
        if(context == null)return view;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        scrollView = view.findViewById(R.id.xmlStudentScrollView);
        linearLayout = view.findViewById(R.id.xmlStudentList);

        if(childEventListener != null)
        {
            myRef.removeEventListener(childEventListener);
        }

        childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final String value = dataSnapshot.getValue(String.class);
                Button button = new Button(context);
                button.setText(value);
                button.setBackgroundResource(R.drawable.becomesponser);
                button.setTextColor(Color.parseColor("#ffffff"));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity)getActivity()).setIndividualChat(value);
                        ((MainActivity)getActivity()).setViewPager(7);
                    }
                });
                linearLayout.addView(button);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
