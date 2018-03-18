package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class EditPostActivity extends AppCompatActivity {

    private ImageButton mEditSelectImage;
    private EditText mEditPostText;
    private Button mEditSubmitBtn;
    private ImageButton mEditRemoveImage;
    private static final int GALLARY_REQUEST = 1;
    private Uri mEditImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseComment;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    private String mPostKey = null;
    private String mText = null;
    private String mImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_edit_post);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("Posts");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users").child(mCurrentUser.getUid());
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Blog").child("Likes");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Blog").child("Comments");

        mPostKey = getIntent().getStringExtra("postid");
        mText = getIntent().getStringExtra("textid");
        mImage = getIntent().getStringExtra("imageid");

        mProgress = new ProgressDialog(this);

        mEditPostText = (EditText) findViewById(R.id.editTitleField);
        mEditSubmitBtn = (Button) findViewById(R.id.editSubmitButton);
        mEditSelectImage = (ImageButton) findViewById(R.id.editImageSelect);
        mEditRemoveImage = (ImageButton) findViewById(R.id.edit_post_remove_image);

        mEditPostText.setText(mText);

        if(mImage != null) {
            Picasso.with(this).load(mImage).into(mEditSelectImage);
            mEditImageUri = Uri.parse(mImage);
            mEditRemoveImage.setVisibility(View.VISIBLE);
        } else {
            mEditImageUri = Uri.parse("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded");
        }

        mEditRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditSelectImage.setImageResource(R.drawable.add_btn);
                mEditImageUri = Uri.parse("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded");
                mEditRemoveImage.setVisibility(View.INVISIBLE);
            }
        });

        mEditSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GALLARY_REQUEST);
            }
        });

        mEditSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditing();
            }
        });

    }

    private void startEditing() {

        mProgress.setMessage("Editing Blog...");

        final String textVal = mEditPostText.getText().toString().trim();

        if (!TextUtils.isEmpty(textVal)) {

            mProgress.show();

            String randomString = UUID.randomUUID().toString().replaceAll("-", "");
            StorageReference filepath = mStorage.child("Edited_Blog_Images").child(randomString);

            if(mEditImageUri.toString().equals(mImage)) {

                mDatabase.child(mPostKey).child("text").setValue(textVal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent mainIntent = new Intent(EditPostActivity.this, BlogMainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }
                });

                mProgress.dismiss();

            } else {

                filepath.putFile(mEditImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final Uri downloadUri = taskSnapshot.getDownloadUrl();

                        mDatabaseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mDatabase.child(mPostKey).child("text").setValue(textVal);
                                //newPost.child("text").setValue(textVal);

                                //if not equal to default set uri or else set Uri as No Image
                                if (!mEditImageUri.toString()
                                        .equals("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded")) {
                                    mDatabase.child(mPostKey).child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent mainIntent = new Intent(EditPostActivity.this, BlogMainActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(mainIntent);
                                            }
                                        }
                                    });
                                } else {
                                    mDatabase.child(mPostKey).child("image").removeValue();
                                    Intent mainIntent = new Intent(EditPostActivity.this, BlogMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        mProgress.dismiss();
                    }
                });

            }


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //.setAspectRatio(3, 2)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mEditImageUri = result.getUri();
                mEditSelectImage.setImageURI(mEditImageUri);
                mEditRemoveImage.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
