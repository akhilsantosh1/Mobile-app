package com.example.bthconnect.MainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class ChatFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener childEventListener;
    final int NUM_TEXT_VIEWS = 10;
    TextView textViews[] = new TextView[NUM_TEXT_VIEWS];
    int textViewOffset = 0;
    Button btn_send;
    Button btn_go_back;
    EditText textInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.chat_fragment, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("globalChat");

        if(childEventListener != null) // Stupid solution but works. Program will end up having multiple childEventListenrs to the same reference otherwise and onChildAdded call will be called multiple times.
        {
            myRef.removeEventListener(childEventListener);
        }

        childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                updateTextViews();
                textViews[NUM_TEXT_VIEWS - 1].setText(value);
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

        initializeTextViews(view);

        textInput = (EditText)view.findViewById(R.id.xmlChatInput);

        btn_send = (Button)view.findViewById(R.id.xmlChatSend);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post(textInput.getText().toString());
                textInput.setText("");
                ((MainActivity)getActivity()).hideKeyboardFrom(getContext(), view);
            }
        });

        btn_go_back = (Button)view.findViewById(R.id.xmlChatGoBack);
        btn_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(childEventListener != null)
                {
                    myRef.removeEventListener(childEventListener);
                }
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        return view;
    }
    void post(String input){
        myRef.push().setValue(((MainActivity)getActivity()).localUser.getDisplayName() + ": " + input);
    }

    void updateTextViews(){
        for(int i = 0; i < NUM_TEXT_VIEWS - 1; ++i){
            textViews[i].setText(textViews[i + 1].getText());
        }
    }

    void initializeTextViews(View view){
        textViews[0] = (TextView)view.findViewById(R.id.xmlChat0);
        textViews[1] = (TextView)view.findViewById(R.id.xmlChat1);
        textViews[2] = (TextView)view.findViewById(R.id.xmlChat2);
        textViews[3] = (TextView)view.findViewById(R.id.xmlChat3);
        textViews[4] = (TextView)view.findViewById(R.id.xmlChat4);
        textViews[5] = (TextView)view.findViewById(R.id.xmlChat5);
        textViews[6] = (TextView)view.findViewById(R.id.xmlChat6);
        textViews[7] = (TextView)view.findViewById(R.id.xmlChat7);
        textViews[8] = (TextView)view.findViewById(R.id.xmlChat8);
        textViews[9] = (TextView)view.findViewById(R.id.xmlChat9);
    }
}
