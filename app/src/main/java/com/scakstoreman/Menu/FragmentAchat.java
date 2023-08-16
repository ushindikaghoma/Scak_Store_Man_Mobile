package com.scakstoreman.Menu;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.scakstoreman.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAchat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAchat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAchat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAchat.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAchat newInstance(String param1, String param2) {
        FragmentAchat fragment = new FragmentAchat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View root;
    Calendar calendar;
    String date_debut, date_fin, todayDate;

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
        root = inflater.inflate(R.layout.fragment_achat, container, false);

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());

        EditText date_debut = root.findViewById(R.id.achat_date_debut);
        EditText date_fin = root.findViewById(R.id.achat_date_fin);

        date_debut.setText(todayDate);
        date_fin.setText(todayDate);

        date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
//                                if(day>9 && month>9)date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                if(dayOfMonth>9 && month>9)date_debut.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(dayOfMonth>9 && !(month>9))date_debut.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(!(dayOfMonth>9) && month>9)date_debut.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                if(!(dayOfMonth>9) && !(month>9))date_debut.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                //date_debut.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if(dayOfMonth>9 && month>9)date_fin.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(dayOfMonth>9 && !(month>9))date_fin.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                                if(!(dayOfMonth>9) && month>9)date_fin.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                if(!(dayOfMonth>9) && !(month>9))date_fin.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                                //date_fin.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });



        return root;
    }
}