package com.example.arenaofpixels;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArenaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArenaFragment extends Fragment {

    DatabaseReference firebaseDatabase;
    FirebaseAuth mAuth;

    Handler handler;
    Spinner spinner;
    ImageView imageView_1, imageView_2;

    private String user;
    private int num;
    private int wins;
    private int loses;

    private String user2;
    private int num2;
    private int wins2;
    private int loses2;

    private boolean click = false;
    private boolean isChange = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArenaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArenaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArenaFragment newInstance(String param1, String param2) {
        ArenaFragment fragment = new ArenaFragment();
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

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        handler = new Handler();

        View root = inflater.inflate(R.layout.fragment_arena, container, false);

        spinner = (Spinner) root.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isChange = true;
                firebaseDatabase.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Resources.numUsers > 2 && (click || isChange)) {
                            isChange = false;

                            String cat = spinner.getSelectedItem().toString();
                            //Random random = new Random();
                            System.out.println("here egethwrthryjety"); //отладка

                            Quad quad = curUser(snapshot, cat, "");
                            user = quad.getUser();
                            num = quad.getNum();
                            wins = quad.getWins();
                            loses = quad.getLoses();

                            quad = curUser(snapshot, cat, user);
                            user2 = quad.getUser();
                            num2 = quad.getNum();
                            wins2 = quad.getWins();
                            loses2 = quad.getLoses();

                            if (!user2.equals("error") && !user.equals("error")) {
                                ImageObj imageObj = (ImageObj) snapshot.child(user)
                                        .child("imgs")
                                        .child(String.valueOf(num))
                                        .getValue(ImageObj.class);
                                Glide.with(getContext()).load(Uri.parse(imageObj.uri)).into(imageView_1);
                                imageView_1.setClickable(true);

                                imageObj = (ImageObj) snapshot.child(user2)
                                        .child("imgs")
                                        .child(String.valueOf(num2))
                                        .getValue(ImageObj.class);
                                Glide.with(getContext()).load(Uri.parse(imageObj.uri)).into(imageView_2);
                                imageView_2.setClickable(true);

                                System.out.println(user + " " + user2); //отладка
                            } else{
                                setFakeImages();
                            }

                            if (click) animeAdd();
                            click = false;
                        }
                        else{
                            setFakeImages();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView_1 = root.findViewById(R.id.imageView_1);
        imageView_2 = root.findViewById(R.id.imageView_2);

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getParentFragment().getContext(), android.R.layout.simple_spinner_item, Resources.sections);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        imageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //я не знаю, как, но это работает
                animeDelete();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setWin(user, num, wins, user2, num2, loses2);
                    }
                }, 1000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView_1.setClickable(true);
                        imageView_2.setClickable(true);
                    }
                }, 2000);

            }
        });

        imageView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animeDelete();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //animeAdd();
                        setWin(user2, num2, wins2, user, num, loses);
                    }
                }, 1000);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView_1.setClickable(true);
                        imageView_2.setClickable(true);
                    }
                }, 2000);

            }
        });

        return root;
    }

    private void animeDelete(){
        if (imageView_2 != null && imageView_1 != null) {
            imageView_1.setClickable(false);
            imageView_2.setClickable(false);
            imageView_1.animate().translationYBy(100).alpha(0).setDuration(1000);
            imageView_2.animate().translationYBy(100).alpha(0).setDuration(1000);
        }
    }

    private void animeAdd(){
        if (imageView_2 != null && imageView_1 != null) {
            imageView_1.animate().translationYBy(-100).alpha(1).setDuration(1000);
            imageView_2.animate().translationYBy(-100).alpha(1).setDuration(1000);
        }
    }

    private Quad curUser(DataSnapshot snapshot, String cat, String userToCompare){
        Random random = new Random();

        HashMap<String, String> all = (HashMap<String, String>) snapshot.getValue();
        ArrayList<String> arr = new ArrayList<>();
//        for (int i = 0; i<Resources.numUsers; i++){
//            arr.add(new String(Resources.users.get(i)));
//        }

        for (Map.Entry<String, String> entry: all.entrySet())
            arr.add(entry.getKey());
        //arr.addAll(Resources.users);
        //System.out.println(Resources.users.toString());
        while (arr.size() > 0) {
            String user = arr.get(random.nextInt(arr.size())).replace(".", "");
            arr.remove(user);
            System.out.println(arr.size());
            if (user.equals(userToCompare) || user.equals(Resources.email.replace(".",""))) continue;
            GenericTypeIndicator<ArrayList<ImageObj>> r = new GenericTypeIndicator<ArrayList<ImageObj>>() {};
            ArrayList<ImageObj> list = (ArrayList<ImageObj>) snapshot.child(user).child("imgs").getValue(r);

            while(list.size() > 0) {
                ImageObj obj = list.get(random.nextInt(list.size()));
                if (!obj.isDeleted && obj.sections.contains(cat)) {
                    System.out.println(user + " " + obj.id + " " + obj.wins + " " + obj.loses);
                    return new Quad(user, obj.id, obj.wins, obj.loses);
                }
                list.remove(obj);
            }
        }
        return new Quad("error", -1, -1, -1);
    }

    public void setWin(String winner, int numImg, int countWins, final String loser, final int numImg2, final int countLoses){
        firebaseDatabase.child("Users").child(winner).child("imgs").child(String.valueOf(numImg)).child("wins").setValue(countWins + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                click = true;
                firebaseDatabase.child("Users").child(loser).child("imgs").child(String.valueOf(numImg2)).child("loses").setValue(countLoses + 1);
            }
        });
    }

    public void setFakeImages(){
        Toast.makeText(getContext(), "Не найдено изображений по тегу", Toast.LENGTH_SHORT).show();
        imageView_1.setImageResource(R.drawable.smorc);
        imageView_1.setClickable(false);
        imageView_2.setImageResource(R.drawable.smorc);
        imageView_2.setClickable(false);
    }

}

class Quad{
    private String user;
    private int num;
    private int wins;
    private int loses;

    public Quad(String user, int num, int wins, int loses){
        this.user = user;
        this.num = num;
        this.wins = wins;
        this.loses = loses;
    }

    public String getUser(){ return user; }
    public int getNum(){ return num; }
    public int getWins(){return wins;}
    public int getLoses(){return loses;}
}