package com.example.fermi.fermi;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AssistantActivity extends AppCompatActivity {

    TextView topTextView, bottomTextView, bottomBarTextView;
    GridLayout buttonGrid;
    LinearLayout yesNoLayout;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        user = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.AssistantToolbar);
        toolbar.setTitle("XYZ");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(AssistantActivity.this);
            }
        });
        topTextView = (TextView) findViewById(R.id.assistantTextTop);
        bottomTextView = (TextView) findViewById(R.id.assistantTextBelow);
        buttonGrid = (GridLayout) findViewById(R.id.button_grid);
        bottomBarTextView = (TextView) findViewById(R.id.bottomBarTextView);
        yesNoLayout = (LinearLayout) findViewById(R.id.assistantLayoutYesNo);
        topTextView.setText("Hi " + user.getDisplayName().split(" ")[0] + "! I'm your XYZ assistant.");
        bottomTextView.setText("I can help you connect to the right person.\nCan you please tell me if your problem relates to any of the following?");
    }

    public void subjectChosen(View v) {
        bottomTextView.setVisibility(View.GONE);
        buttonGrid.setVisibility(View.GONE);
        bottomBarTextView.setVisibility(View.VISIBLE);
        bottomBarTextView.setText("Searching...");
        topTextView.setText("Okay Let me connect with you someone.\nSearching...\n\n");
        //find User to connect to
        User user2 = findUser(v.getTag().toString());
        //after user found
        topTextView.append(user.getDisplayName().split(" ")[0] + ", I found $USER for you. Do you want to start conversation with $USER?");
        yesNoLayout.setVisibility(View.VISIBLE);
    }

    public User findUser(String subject) {
        //TODO query the users for related subject from database, select random user from the list, return the user
        return new User();
    }

}
