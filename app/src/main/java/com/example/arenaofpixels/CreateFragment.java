package com.example.arenaofpixels;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arenaofpixels.sectionAdapter.SectionAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Button buttonAdd;
    private ImageButton pictureButton;
    private ImageView image;
    public static final int PICK_IMAGE = 1;
    private Bitmap picture;
    private Uri imageUri;
    private RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_create, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = root.findViewById(R.id.recyclerSections);
        buttonAdd = root.findViewById(R.id.button_add);
        pictureButton = root.findViewById(R.id.load_image);
        image = root.findViewById(R.id.imageView);

        // ????????????????
        final SectionAdapter adapter = new SectionAdapter(getContext(), Resources.sections);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*databaseReference.child("Users").child(Resources.email.replace(".", "")).child("imgs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String,String>> r = new GenericTypeIndicator<HashMap<String,String>>() {};
                HashMap<String,String> map = snapshot.getValue(r);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                   System.out.println(entry.getKey() + " " + entry.getValue());
                }
                //test
                //Uri uri = Uri.parse(map.get("-MYeR3hqQIueEcuR9SzB"));
                //Picasso.with(getContext()).load(uri).rotate(90).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){

                    final List<String> sections = new ArrayList<>();
                    int numOfChecked = 0;
                    for (int i = 0; i < adapter.getItemCount(); i++)
                        if (adapter.getChecked(i)){
                            System.out.println("checked" + i);
                            numOfChecked++;
                            sections.add(Resources.sections[i]);
                        }
                    if (numOfChecked > 0) {

                        UploadTask uploadTask = storageReference.child("images/" + Resources.email + "/" + (Resources.currentNum)).putFile(imageUri);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "??????????", Toast.LENGTH_SHORT).show();
                            }
                        });

                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                // Continue with the task to get the download URL
                                return storageReference.child("images/" + Resources.email + "/" + (Resources.currentNum)).getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("tut");
                                    //databaseReference.child("Urls").child(Resources.email.replace(".","")).push().setValue(task.getResult().toString());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put(String.valueOf(Resources.currentNum), new ImageObj(Resources.currentNum, task.getResult().toString(), sections, 0, 0, 0));
                                    databaseReference.child("Users").child(Resources.email.replace(".", "")).child("imgs").updateChildren(map);

                                } else {
                                    System.out.println("ne tut");
                                    System.out.println(task.getException());
                                }
                                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_createFragment_to_homeFragment, null);
                            }
                        });

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "??????????????", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else Toast.makeText(getContext(), "???????????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                } else {
                    Toast.makeText(getContext(), "?? ???????????????????? ?????? ?????????? ?????? ?????????????????? ????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage;

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedImage = data.getData();
                imageUri = selectedImage;
//                InputStream in;
//                try {
//                    in = getActivity().getContentResolver().openInputStream(selectedImage);
//                    final Bitmap selected = BitmapFactory.decodeStream(in);
//                    picture = selected;
//                    pictureButton.setImageBitmap(selected);
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                    Toast.makeText(getContext(), "???????? ???? ????????????", Toast.LENGTH_SHORT).show();
//                }
                try {
                    image.setImageURI(selectedImage);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "???? ??????????????", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}