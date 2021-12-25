package com.example.bmianalayzer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class addfood extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText etNameFood, etClary;
    Spinner spinner;
    ImageView photoFood;
    Button botUploadPhoto, botSave;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    StorageReference references;
    Uri imgUri, uri;
    nfood listFoods;
    String uIds, categorty;
    ProgressDialog dialog;
    int selspinner = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
        etNameFood = findViewById(R.id.et_nameFood);
        photoFood = findViewById(R.id.photoFood);
        botSave = findViewById(R.id.botSave);
        etClary = findViewById(R.id.etClary);
        spinner = findViewById(R.id.spinner);
        botUploadPhoto = findViewById(R.id.botUploadPhoto);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        uIds = firebaseAuth.getUid();
        references = FirebaseStorage.getInstance().getReference();
        botUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 20);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        categorty = spinner.getSelectedItem().toString();

        botSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etNameFood.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "add meal name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(etClary.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "add calorie ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uri == null) {
                    Toast.makeText(getApplicationContext(), "add photo", Toast.LENGTH_LONG).show();
                    return;
                }
                listFoods = new nfood();
                listFoods.setuId(uIds);

                listFoods.setClaryFoods(etClary.getText().toString());
                listFoods.setNameFoods(etNameFood.getText().toString());
                listFoods.setCategoryFoodsName(selspinner + "");
                listFoods.setFBUri(uri + "");
                listFoods.setuId(uIds);

                firebaseFirestore.collection("food").add(listFoods)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    etClary.setText("");
                                    spinner.setSelection(0);
                                    etNameFood.setText("");
                                    Glide.with(getApplicationContext()).load(R.drawable.ic_launcher_background).into(photoFood);
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                imgUri = data.getData();
                Glide.with(getApplicationContext()).load(imgUri).into(photoFood);
                upload(imgUri);
            }
        }
    }

    private void upload(Uri image) {
        dialog = ProgressDialog.show(this, "wait ", "wait", true);
        final StorageReference reference = this.references.child(uIds);
        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        addfood.this.uri = uri;
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        categorty = adapterView.getItemAtPosition(i).toString();
        selspinner = adapterView.getSelectedItemPosition();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

}