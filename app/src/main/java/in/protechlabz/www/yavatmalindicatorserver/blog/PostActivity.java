package in.protechlabz.www.yavatmalindicatorserver.blog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import in.protechlabz.www.yavatmalindicatorserver.R;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageButton mSelectImage;
    private EditText mPostText;
    private Button mSubmitBtn;
    private static final int GALLARY_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseComment;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    private Spinner mCategorySpinner;
    private ArrayAdapter<CharSequence> categoriesAdapter;
    private String spinnerItemSelected = "General";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        /* Admob related code*/
        AdView mAdView = (AdView) findViewById(R.id.adView_post);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("Posts");
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Blog").child("Users").child(mCurrentUser.getUid());
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Blog").child("Likes");
        mDatabaseComment = FirebaseDatabase.getInstance().getReference().child("Blog").child("Comments");

        mProgress = new ProgressDialog(this);

        mImageUri = Uri.parse("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded");

        mPostText = (EditText) findViewById(R.id.titleField);
        mSubmitBtn = (Button) findViewById(R.id.submitButton);

        mCategorySpinner = (Spinner) findViewById(R.id.selectCategory);
        mCategorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(adapter);

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GALLARY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }


    private void startPosting() {

        mProgress.setMessage("Posting to Blog...");

        final String textVal = mPostText.getText().toString().trim();

        if (!TextUtils.isEmpty(textVal)) {

            mProgress.show();

            String randomString = UUID.randomUUID().toString().replaceAll("-", "");
            StorageReference filepath = mStorage.child("Blog_Images").child(randomString);

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    final Uri downloadUri = taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost = mDatabase.push();
                    final String post_key = newPost.getKey();

                    mDatabaseLike.child(post_key).child("totallikes").setValue("0");
                    mDatabaseComment.child(post_key).child("totalcomments").setValue("0");

                    newPost.child("text").setValue(textVal);
                    newPost.child("time").setValue(getFormatedDateTime());
                    newPost.child("uid").setValue(mCurrentUser.getUid());
                    newPost.child("category").setValue(spinnerItemSelected);

                    if (!mImageUri.toString()
                            .equals("android.resource://in.protechlabz.www.yavatmalindicatorserver/drawable/noimage_uploaded")) {
                        newPost.child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent mainIntent = new Intent(PostActivity.this, BlogMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                }
                            }
                        });
                    } else {
                        Intent mainIntent = new Intent(PostActivity.this, BlogMainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }

                    mProgress.dismiss();
                }
            });

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

                mImageUri = result.getUri();
                mSelectImage.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerItemSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private String getFormatedDateTime() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy 'at' hh:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String strDate = formatter.format(date);

        Log.d("Checking date time", strDate);
        return strDate;
    }

}
