package com.example.arenaofpixels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arenaofpixels.homeAdapter.MyAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MyAdapter.MyClick{

    private DatabaseReference dbRef;

    private RecyclerView recyclerView;

    private EditText til;
    private FloatingActionButton faButton;

    private boolean nickClick = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler);

        dbRef = FirebaseDatabase.getInstance().getReference();

//        Task<DataSnapshot> task = dbRef.child("Urls").get();
//        for (DataSnapshot postSnapshot: task.getResult().getChildren()) {
//            String post = postSnapshot.getValue(String.class);
//            Log.e("Get Data", post);
//        }
        dbRef.child("Users").child(Resources.email.replace(".", "")).child("imgs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<ImageObj>> r = new GenericTypeIndicator<ArrayList<ImageObj>>() {};

                ArrayList<ImageObj> imgs = snapshot.getValue(r);


                if (imgs != null) {
                    ArrayList<ImageObj> arr = new ArrayList<>();
                    for (int i = 0; i < imgs.size(); i++)
                        if (!imgs.get(i).isDeleted) arr.add(imgs.get(i));
                    Resources.setCurrentNum(imgs.size());
                    Resources.imageMap = arr;
                }

                //System.out.println(Resources.currentNum);

                if (Resources.imageMap != null){
                    MyAdapter adapter = new MyAdapter(getContext(), Resources.imageMap, HomeFragment.this);

                    //System.out.println(Resources.imageMap);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        til = root.findViewById(R.id.field);
        til.setText(Resources.nickname);

        faButton = root.findViewById(R.id.buttonEdit);
        faButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickClick = !nickClick;
                if (nickClick) {
                    faButton.setImageResource(R.drawable.ic_ready);
                    til.setEnabled(true);
                    til.requestFocus();
                    //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(til, InputMethodManager.SHOW_IMPLICIT);
                    //((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(til, 0);
                }
                else{
                    if (til.getText() != null){
                        String text = til.getText().toString();
                        if (text.length() > 1){
                            Resources.setNickname(text);
                            dbRef.child("Userlist").child(Resources.email).child("nick").setValue(text);
                        }
                        else{
                            Toast.makeText(getContext(), "Слишком короткий ник!", Toast.LENGTH_SHORT).show();
                            til.setText(Resources.nickname);
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Слишком короткий ник!", Toast.LENGTH_SHORT).show();
                        til.setText(Resources.nickname);
                    }
                    faButton.setImageResource(R.drawable.ic_edit);
                    til.setEnabled(false);
                    til.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(til.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });

        Button button = root.findViewById(R.id.toCreateFragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_homeFragment_to_createFragment, null);
            }
        });

        root.findViewById(R.id.sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignIn.getClient(getActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut(); //лол это сработало

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);


            }
        });

        return root;
    }

    @Override
    public void onClick(int pos) {
        Bundle args = new Bundle();
        args.putSerializable("img", Resources.imageMap.get(pos));
        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_homeFragment_to_openImageFragment, args);
    }
}