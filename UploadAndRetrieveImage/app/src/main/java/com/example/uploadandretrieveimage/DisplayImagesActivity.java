package com.example.uploadandretrieveimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayImagesActivity extends AppCompatActivity {

    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    FirebaseRecyclerAdapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);

        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayImagesActivity.this));

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(DisplayImagesActivity.this);



        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All_Image_Uploads_Database");



    }




    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("All_Image_Uploads_Database");
        FirebaseRecyclerOptions<ImageUploadInfo> options = new FirebaseRecyclerOptions.Builder<ImageUploadInfo>()
                .setQuery(query, ImageUploadInfo.class)
                .build();
        FirebaseRecyclerAdapter<ImageUploadInfo ,FindFriendViewHolder > adapter =
                new FirebaseRecyclerAdapter<ImageUploadInfo, FindFriendViewHolder>(options ) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendViewHolder findFriendViewHolder, final int position, @NonNull final ImageUploadInfo imageUploadInfo) {


                        findFriendViewHolder.imageName.setText(imageUploadInfo.getImageName());
                        Glide.with(DisplayImagesActivity.this)
                                .load(imageUploadInfo.imageURL)
                                .into(findFriendViewHolder.imageView);

                        findFriendViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.i("DisplayImagesActivity", " get item number " + position);
                                Toast.makeText(DisplayImagesActivity.this ,   imageUploadInfo.getImageName() , Toast.LENGTH_SHORT).show();

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items,parent,false);
                        return new FindFriendViewHolder(view);
                    }
                };


          recyclerView.setAdapter(adapter);
          adapter.startListening();

    }

    public  static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView imageName ;
        ImageView imageView;
        View View;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);

            imageName = itemView.findViewById(R.id.ImageNameTextView);
            imageView = itemView.findViewById(R.id.imageView);
            this.View = itemView;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       // adapter.stopListening();
    }
}