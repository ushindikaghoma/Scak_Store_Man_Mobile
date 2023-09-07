package com.scakstoreman.Menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.scakstoreman.Achat.AdapterAchat;
import com.scakstoreman.Article.data.ArticleResponse;
import com.scakstoreman.OfflineModels.Comptabilite.AchatJourModel;
import com.scakstoreman.OfflineModels.Comptabilite.tAchatAdapter;
import com.scakstoreman.OfflineModels.Comptabilite.tComptabilite;
import com.scakstoreman.OfflineModels.Panier.tPanier;
import com.scakstoreman.OfflineModels.Panier.tPanierAdapter;
import com.scakstoreman.Operation.OperationRepository;
import com.scakstoreman.Operation.OperationResponse;
import com.scakstoreman.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    RecyclerView recyclerViewAchatJourList;
    ProgressBar progressBarLoadAchat;
    OperationRepository operationRepository;
    AdapterAchat adapterAchat;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user, pref_mode_type;
    SwipeRefreshLayout refreshListeAchat;
    List<AchatJourModel> dataListe;
    tAchatAdapter _tAchatAdapter;

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

        preferences = getActivity().getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");
        pref_mode_type = preferences.getString("pref_mode_type","");

        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        todayDate = format.format(calendar.getTime());

        EditText date_debut = root.findViewById(R.id.achat_date_debut);
        EditText date_fin = root.findViewById(R.id.achat_date_fin);
        recyclerViewAchatJourList = root.findViewById(R.id.achat_journalier_recycle);
        progressBarLoadAchat = root.findViewById(R.id.achatProgressLoadAchat);
        refreshListeAchat = root.findViewById(R.id.achat_swipe_torefresh);

        recyclerViewAchatJourList.setHasFixedSize(true);
        recyclerViewAchatJourList.setLayoutManager(new LinearLayoutManager(getContext()));

        operationRepository = OperationRepository.getInstance();
        adapterAchat = new AdapterAchat(getContext());



        date_debut.setText(todayDate);
        date_fin.setText(todayDate);

        Toast.makeText(getContext(), "Usher"+pref_mode_type, Toast.LENGTH_SHORT).show();

        if (pref_mode_type.equals("online"))
        {
            LoadListeAchatJounalier(nom_user, todayDate, progressBarLoadAchat);

        } else if (pref_mode_type.equals("offline"))
        {
            dataListe = new ArrayList<>();
            dataListe = tComptabilite.GetAchatDuJour(getContext(), dataListe, todayDate, nom_user);
            _tAchatAdapter =  new tAchatAdapter(getContext(),dataListe);
            recyclerViewAchatJourList.setAdapter(_tAchatAdapter);

            progressBarLoadAchat.setVisibility(View.GONE);

            _tAchatAdapter.notifyDataSetChanged();
        }else
        {

        }

        refreshListeAchat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (pref_mode_type.equals("online"))
                {
                    LoadListeAchatJounalier(nom_user, todayDate, progressBarLoadAchat);

                } else if (pref_mode_type.equals("offline"))
                {
                    dataListe = new ArrayList<>();
                    dataListe = tComptabilite.GetAchatDuJour(getContext(), dataListe, todayDate, nom_user);
                    _tAchatAdapter =  new tAchatAdapter(getContext(),dataListe);
                    recyclerViewAchatJourList.setAdapter(_tAchatAdapter);

                    progressBarLoadAchat.setVisibility(View.GONE);

                    _tAchatAdapter.notifyDataSetChanged();
                }else
                {

                }

                //LoadListeAchatJounalier(nom_user, todayDate, progressBarLoadAchat);
                refreshListeAchat.setRefreshing(false);
            }
        });

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



                if (pref_mode_type.equals("online"))
                {
                    LoadListeAchatJounalier(nom_user, date_debut.getText().toString(), progressBarLoadAchat);
                } else if (pref_mode_type.equals("offline"))
                {
                    dataListe = new ArrayList<>();
                    dataListe = tComptabilite.GetAchatDuJour(getContext(), dataListe, date_debut.getText().toString(), nom_user);
                    _tAchatAdapter =  new tAchatAdapter(getContext(),dataListe);
                    recyclerViewAchatJourList.setAdapter(_tAchatAdapter);

                    progressBarLoadAchat.setVisibility(View.GONE);

                    _tAchatAdapter.notifyDataSetChanged();
                }else
                {}

                datePickerDialog.show();
            }
        });



        return root;
    }

    public void LoadListeAchatJounalier(String userName, String date, ProgressBar loadAchat)
    {
        Call<List<OperationResponse>> call_liste_achat = operationRepository.operationConnexion().getListAchatJournalier(userName, date);
        loadAchat.setVisibility(View.VISIBLE);
        call_liste_achat.enqueue(new Callback<List<OperationResponse>>() {
            @Override
            public void onResponse(Call<List<OperationResponse>> call, Response<List<OperationResponse>> response) {
                if (response.isSuccessful())
                {
                    loadAchat.setVisibility(View.GONE);
                    List<OperationResponse> list_local = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        OperationResponse liste_achat =
                                new OperationResponse (
                                        response.body().get(a).getLibelle(),
                                        response.body().get(a).getDateOperation(),
                                        response.body().get(a).getMontant()
                                );


                        list_local.add(liste_achat);
                    }
                    adapterAchat.setList(list_local);
                    recyclerViewAchatJourList.setAdapter(adapterAchat);

                }
            }

            @Override
            public void onFailure(Call<List<OperationResponse>> call, Throwable t) {
                loadAchat.setVisibility(View.GONE);
            }
        });
    }
}