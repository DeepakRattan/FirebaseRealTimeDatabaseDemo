package com.example.deepakrattan.firebaserealtimedatabasedemo;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edtArtistName;
    private Spinner spGeneres;
    private Button btnAddArtist, btnViewArtist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseArtists;
    private ListView lvArtist;

    //ArrayList to store all the artist from FirebaseDatabase
    private ArrayList<Artist> artistsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewByID
        edtArtistName = (EditText) findViewById(R.id.edtArtist);
        spGeneres = (Spinner) findViewById(R.id.spGeneres);
        btnAddArtist = (Button) findViewById(R.id.btnAddArtist);
        btnViewArtist = (Button) findViewById(R.id.btnViewArtist);
        lvArtist = (ListView) findViewById(R.id.lvArtist);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseArtists = firebaseDatabase.getReference("artists");

        artistsArrayList = new ArrayList<>();

        btnAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

   /*     btnViewArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseArtists.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //clear the previous artist list
                        artistsArrayList.clear();

                        //Iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //Getting artist
                            Artist artist = postSnapshot.getValue(Artist.class);
                            //adding artist to arraylist
                            artistsArrayList.add(artist);
                        }

                        //Creating adapter
                        ArtistAdapter adapter = new ArtistAdapter(MainActivity.this, artistsArrayList);
                        lvArtist.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });*/

        //On long clicking the ListView Item ,a dialog will be shown .Using dialog we can update and delete the data

        lvArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artistsArrayList.get(i);
                showUpdateDeleteDialog(artist.getId(), artist.getName());
                return true;
            }
        });

    }

    //Writing data to real Time FirebaseDatabase
    public void addArtist() {
        String name = edtArtistName.getText().toString().trim();
        String genere = spGeneres.getSelectedItem().toString();
        //If name is not empty write data to database
        if (!TextUtils.isEmpty(name)) {
            //Everytime a unique id is generated
            String id = databaseArtists.push().getKey();
            Artist artist = new Artist(id, name, genere);
            databaseArtists.child(id).setValue(artist);
            Toast.makeText(this, "Artist added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();

        }

    }


    //Reading data from real time FirebaseDatabase
    //Retrieve all the artists from realTime FirebaseDatabse.
    // For this we will attach a ValueEventListener to the database reference object,
    // inside onStart() method of MainActivity.
    @Override
    protected void onStart() {
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clear the previous artist list
                artistsArrayList.clear();

                //Iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to arraylist
                    artistsArrayList.add(artist);
                }

                //Creating adapter
                ArtistAdapter adapter = new ArtistAdapter(MainActivity.this, artistsArrayList);
                lvArtist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Update Artist
    private boolean updateArtist(String id, String name, String genre) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
        //updating artist
        Artist artist = new Artist(id, name, genre);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(), "Artist Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    //In this method we are building a Dialog and on update button click
    // we are calling the above method updateArtist() to update the artist.


    private void showUpdateDeleteDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.edtNam);
        final Spinner spinnerGenre = (Spinner) dialogView.findViewById(R.id.spGener);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btnUpdateArtist);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDelArtist);

        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String genre = spinnerGenre.getSelectedItem().toString();
                if (!TextUtils.isEmpty(name)) {
                    updateArtist(artistId, name, genre);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artistId);
                b.dismiss();
            }
        });
    }

    //Deleting Artist from Firebase Realtime Database

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
        //removing artist
        Task task = dR.removeValue();
       /* if (task.isSuccessful())
            Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Artist not Deleted", Toast.LENGTH_LONG).show();*/
        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


}
