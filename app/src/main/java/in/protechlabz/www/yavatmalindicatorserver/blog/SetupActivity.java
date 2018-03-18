package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class SetupActivity extends AppCompatActivity {

    private ImageButton mSetupImageBtn;
    private EditText mNameField;
    private Button mSubmitBtn;
    private ImageButton mSetupRemoveProfilePic;
    private static final int GALLARY_REQUEST = 1;
    private ProgressDialog mProgress;
    private Uri mImageUri = null;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;
    private String user_id = null;
    private String originalProfilePicUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_setup_activity);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers =  FirebaseDatabase.getInstance().getReference().child("Blog").child("Users");
        user_id = mAuth.getCurrentUser().getUid();

        mProgress = new ProgressDialog(this);

        mImageUri = Uri.parse("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded");

        //mSetupImageBtn = (ImageButton) findViewById(R.id.setupImageBtn);
        mNameField = (EditText) findViewById(R.id.setupNameField);
        mSubmitBtn = (Button) findViewById(R.id.setupSubmitButton);
        //mSetupRemoveProfilePic = (ImageButton) findViewById(R.id.setup_remove_profile_pic);

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)) {
                    mNameField.setText(dataSnapshot.child(user_id).child("name").getValue().toString());

                    /*if(dataSnapshot.child(user_id).child("image").getValue().toString().equals("No Image")) {
                        mSetupImageBtn.setImageResource(R.drawable.default_image);
                    }else {
                        Picasso.with(SetupActivity.this)
                                .load(dataSnapshot.child(user_id).child("image").getValue().toString())
                                .into(mSetupImageBtn);
                        mSetupRemoveProfilePic.setVisibility(View.VISIBLE);
                    }

                    originalProfilePicUrl = dataSnapshot.child(user_id).child("image").getValue().toString();
                    mImageUri = Uri.parse(originalProfilePicUrl);*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image*//*");
                startActivityForResult(gallaryIntent, GALLARY_REQUEST);
            }
        });
*/
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });

        /*mSetupRemoveProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSetupImageBtn.setImageResource(R.drawable.default_image);
                mImageUri = Uri.parse("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded");
                mSetupRemoveProfilePic.setVisibility(View.INVISIBLE);

            }
        });*/
    }

    private void startSetupAccount() {

        final String name = mNameField.getText().toString().trim();

        if(!TextUtils.isEmpty(name)) {
            mProgress.setMessage("Saving Profile...");
            mProgress.show();
            mDatabaseUsers.child(user_id).child("name").setValue(name);
            mProgress.dismiss();
            Intent mainIntent = new Intent(SetupActivity.this, BlogMainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);

        }

        /*if(!TextUtils.isEmpty(name) && mImageUri != null) {

            mProgress.setMessage("Saving Profile...");
            mProgress.show();

            String randomString = UUID.randomUUID().toString().replaceAll("-", "");
            StorageReference filepath = mStorageImage.child(randomString);

            mDatabaseUsers.child(user_id).child("name").setValue(name);

            if(!(mImageUri.toString().equals(originalProfilePicUrl))) {
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String downloadUri = taskSnapshot.getDownloadUrl().toString();

                        if(mImageUri.toString()
                                .equals("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded")) {
                            mDatabaseUsers.child(user_id).child("image").setValue("No Image").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgress.dismiss();
                                    Intent mainIntent = new Intent(SetupActivity.this, BlogMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                }
                            });
                        }else {
                            mDatabaseUsers.child(user_id).child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mProgress.dismiss();
                                    Intent mainIntent = new Intent(SetupActivity.this, BlogMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                }
                            });
                        }
                    }
                });

            }
        }*/
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLARY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                mSetupImageBtn.setImageURI(mImageUri);
                mSetupRemoveProfilePic.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }*/
}
