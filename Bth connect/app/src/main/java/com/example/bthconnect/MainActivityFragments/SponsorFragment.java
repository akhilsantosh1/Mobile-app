package com.example.bthconnect.MainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.Vector;

public class SponsorFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChildEventListener childEventListener;
    private boolean canBecomeSponsor;
    Button btn_become_sponsor;
    Button btn_get_sponsor;
    Button btn_back;
    Vector<String> sponsorList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sponsor_fragment, container, false);

        btn_become_sponsor = (Button)view.findViewById(R.id.xmlBecomeSponsor);
        btn_become_sponsor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(canBecomeSponsor)
                {
                    myRef = database.getReference("availableSponsors/" + ((MainActivity)getActivity()).localUser.getDisplayName());
                    myRef.setValue("Wants to be a sponsor");
                    myRef = database.getReference("availableSponsors");
                    canBecomeSponsor = false;
                    Toast.makeText(getActivity(), "You have now signed up for the sponsorship program", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "You have already signed up to be a sponsor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_get_sponsor = (Button)view.findViewById(R.id.xmlGetSponsor);
        btn_get_sponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sponsorList.size() > 0)
                {
                    String sponsor = sponsorList.get(0);
                    sponsorList.remove(0);

                    myRef = database.getReference("availableSponsors/" + sponsor);
                    myRef.removeValue();
                    myRef = database.getReference("availableSponsors");

                    //TODO MOVE THIS
                    int hash0 = sponsor.hashCode();
                    int hash1 = ((MainActivity)getActivity()).localUser.getDisplayName().hashCode();
                    int combinedHash = hash0 + hash1;
                    myRef = database.getReference("privateMessages/" + combinedHash);

                    myRef.push().setValue(sponsor + " has become a sponsor to " + ((MainActivity)getActivity()).localUser.getDisplayName() + ".");

                    if(childEventListener != null)
                    {
                        myRef.removeEventListener(childEventListener);
                    }

                    ((MainActivity)getActivity()).setIndividualChat(sponsor);
                    ((MainActivity)getActivity()).setViewPager(7);
                }
                else
                {
                    Toast.makeText(getActivity(), "No sponsors available :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back= (Button)view.findViewById(R.id.xmlSponsorBack);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });

        return view;
    }

    public void initializeSponsorFragment()
    {
        sponsorList = new Vector<String>();

        canBecomeSponsor = true;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("availableSponsors");

        if(childEventListener != null)
        {
            myRef.removeEventListener(childEventListener);
        }

        childEventListener = myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int hashedName = dataSnapshot.getKey().hashCode();
                int user = ((MainActivity)getActivity()).localUser.getDisplayName().toString().hashCode();
                if(hashedName == user) // Strings cannot be compared for some reason
                {
                    canBecomeSponsor = false;
                }
                else
                {
                    String temp = dataSnapshot.getKey();
                    sponsorList.add(temp);
                }
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
}
