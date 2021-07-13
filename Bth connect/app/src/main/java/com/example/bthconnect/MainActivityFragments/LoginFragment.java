package com.example.bthconnect.MainActivityFragments;

import android.content.Intent;
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
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {
    private static final int MY_REQUEST_CODE = 15; // random number, needed for whatever reason
    List<AuthUI.IdpConfig> providers;
    Button btn_connect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        btn_connect = (Button)view.findViewById(R.id.xmlConnect);
        btn_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        showSignInOptions();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

       return view;
    }

    private void showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).setAvailableProviders(providers).build(), MY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                ((MainActivity)getActivity()).localUser = FirebaseAuth.getInstance().getCurrentUser();
                if(response.isNewUser()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    myRef.push().setValue(((MainActivity)getActivity()).localUser.getDisplayName());
                    Toast.makeText(getActivity(), "Welcome to BTH Connect: " + ((MainActivity)getActivity()).localUser.getDisplayName(), Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "Welcome Back: " + ((MainActivity)getActivity()).localUser.getDisplayName(), Toast.LENGTH_SHORT).show();

                ((MainActivity)getActivity()).setViewPager(3);
            }
            else
            {
                Toast.makeText(getActivity(), "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
