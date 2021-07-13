package com.example.bthconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.bthconnect.CustomViewPager.CustomViewPager;
import com.example.bthconnect.FragmentAdapter.FragmentAdapter;
import com.example.bthconnect.MainActivityFragments.ChatFragment;
import com.example.bthconnect.MainActivityFragments.CreateEventFragment;
import com.example.bthconnect.MainActivityFragments.EventsFragment;
import com.example.bthconnect.MainActivityFragments.HomeFragment;
import com.example.bthconnect.MainActivityFragments.IndividualChatFragment;
import com.example.bthconnect.MainActivityFragments.LoginFragment;
import com.example.bthconnect.MainActivityFragments.MenuFragment;
import com.example.bthconnect.MainActivityFragments.SponsorFragment;
import com.example.bthconnect.MainActivityFragments.StudentListFragment;
import com.example.bthconnect.MainActivityFragments.PollVoteFragment;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FragmentAdapter fragAdapter;
    private CustomViewPager viewPager;
    public FirebaseUser localUser; // public for convenience
    final String channelID = "MyChannelID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fragAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = (CustomViewPager)findViewById(R.id.container);
        initViewPager();
        setViewPager(0);

        createNotificationChannel();

        Intent intent = new Intent(this, String.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                .setContentTitle("BTHConnect - beware!")
                .setContentText("Please do not enter any personal or sensitive information into the app.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(12902, builder.build());


    }

    private void initViewPager(){
        fragAdapter.addFragment(new LoginFragment()); // 0
        fragAdapter.addFragment(new MenuFragment()); // 1
        fragAdapter.addFragment(new ChatFragment()); // 2
        fragAdapter.addFragment(new HomeFragment()); // 3
        fragAdapter.addFragment(new EventsFragment()); // 4
        fragAdapter.addFragment(new CreateEventFragment()); // 5
        fragAdapter.addFragment(new StudentListFragment()); // 6
        fragAdapter.addFragment(new IndividualChatFragment()); // 7
        fragAdapter.addFragment(new SponsorFragment()); // 8
        fragAdapter.addFragment(new PollVoteFragment());//9

        viewPager.setAdapter((fragAdapter));
    }

    public void initEventsFragment()
    {
        EventsFragment ptr = (EventsFragment)fragAdapter.getItem(4);
        ptr.initEventsFragment();
        ptr.initChildListeners();
    }

    public void initPollVoteFragment(String id, String name, String time, String date, String loc){
        PollVoteFragment ptr = (PollVoteFragment)fragAdapter.getItem(9);
        ptr.initPollFragment(id, name, time, date, loc);
    }

    public void setIndividualChat(String person){
        IndividualChatFragment ptr = (IndividualChatFragment)fragAdapter.getItem(7);
        ptr.initializeIndividualChat(person);
    }

    public void initSponsorFragment(){
        SponsorFragment ptr = (SponsorFragment)fragAdapter.getItem(8);
        ptr.initializeSponsorFragment();
    }

    public void setViewPager(int fragment){
        viewPager.setCurrentItem(fragment, false);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Notification Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
