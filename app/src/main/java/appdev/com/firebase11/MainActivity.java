package appdev.com.firebase11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllTask(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("users", "Failed to read value.", error.toException());
            }
        });


        Button button =(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_txt=(EditText)findViewById(R.id.name_txt);
                String name=name_txt.getText().toString();

                EditText phone_txt=(EditText)findViewById(R.id.phone_txt);
                String phone=phone_txt.getText().toString();

                EditText email_txt=(EditText)findViewById(R.id.email_txt);
                String email=email_txt.getText().toString();

                String id= name.replaceAll("\\s","") + phone;

                writeNewUser(id, name, email, phone);

                name_txt.setText("");
                phone_txt.setText("");
                email_txt.setText("");
            }
        });

    }


    private void writeNewUser(String userId, String name, String email, String phone) {
        User user = new User(name, email, phone);
        mDatabase.child(userId).setValue(user);
        // sould use autoid and push
    }

    private void getAllTask(DataSnapshot dataSnapshot){
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> aUsers = new ArrayList<String>();

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            User user = singleSnapshot.getValue(User.class);
            aUsers.add(user.getName());

        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, aUsers);
        listView.setAdapter(arrayAdapter);
    }




}
