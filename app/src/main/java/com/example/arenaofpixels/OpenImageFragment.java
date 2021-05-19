package com.example.arenaofpixels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenImageFragment extends Fragment {

    DatabaseReference databaseReference;

    ImageObj imageObj;
    ImageView imageViewOpen;
    TextView imageInfo;
    Button buttonDelete;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpenImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpenImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenImageFragment newInstance(String param1, String param2) {
        OpenImageFragment fragment = new OpenImageFragment();
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
        final View root = inflater.inflate(R.layout.fragment_open_image, container, false);

        imageObj = (ImageObj) getArguments().getSerializable("img");
        imageViewOpen = root.findViewById(R.id.imageViewOpen);
        imageInfo = root.findViewById(R.id.textView4);
        buttonDelete = root.findViewById(R.id.button_delete);

        imageInfo.setText("Победы: " + imageObj.wins + "\nПоражения: " + imageObj.loses + "\nРазделы: " +
                imageObj.sections.toString().replace("[", "").replace("]", ""));
        Glide.with(getContext()).load(Uri.parse(imageObj.uri)).into(imageViewOpen);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Вы уверены?");
                builder.setMessage("При удалении картинки её результаты также исчезнут!");
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() { // Кнопка Отмены
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //удаляем картиночку
                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        imageObj.isDeleted = true;
                        map.put(String.valueOf(imageObj.id), imageObj);
                        databaseReference.child("Users").child(Resources.email.replace(".", "")).child("imgs").updateChildren(map);
                        dialog.dismiss(); // Отпускает диалоговое окно
                        Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_openImageFragment_to_homeFragment);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return root;
    }
}