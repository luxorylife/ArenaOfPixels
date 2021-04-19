package com.example.arenaofpixels;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArenaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArenaFragment extends Fragment {

    Handler handler;
    Spinner spinner;
    ImageView imageView_1, imageView_2;

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

        handler = new Handler();

        View root = inflater.inflate(R.layout.fragment_arena, container, false);

        spinner = (Spinner) root.findViewById(R.id.spinner);
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
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                        animeAdd();
                   }
               }, 1000);

               animeDelete();

            }
        });

        imageView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animeAdd();
                    }
                }, 1000);

                animeDelete();
            }
        });

        return root;
    }

    private void animeDelete(){
        if (imageView_2 != null && imageView_1 != null) {
            imageView_1.animate().translationYBy(100).alpha(0).setDuration(1000);
            imageView_2.animate().translationYBy(100).alpha(0).setDuration(1000);
        }
    }

    private void animeAdd(){
        if (imageView_2 != null && imageView_1 != null) {
            imageView_1.animate().translationYBy(-100).alpha(1).setDuration(1000).setStartDelay(1000);
            imageView_2.animate().translationYBy(-100).alpha(1).setDuration(1000).setStartDelay(1000);
        }
    }
}