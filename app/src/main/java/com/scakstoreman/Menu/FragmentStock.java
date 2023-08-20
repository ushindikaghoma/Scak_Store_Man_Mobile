package com.scakstoreman.Menu;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.scakstoreman.Compte.data.CompteResponse;
import com.scakstoreman.R;
import com.scakstoreman.Stock.data.AdapterStock;
import com.scakstoreman.Stock.data.StockRepository;
import com.scakstoreman.Stock.data.StockResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentStock#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentStock extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentStock() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentStock.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentStock newInstance(String param1, String param2) {
        FragmentStock fragment = new FragmentStock();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    View root = null;
    StockRepository stockRepository;
    AdapterStock adapterStock;
    RecyclerView recyclerViewStock;
    ProgressBar progressBarLoadStock;

    SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    String pref_code_depot, pref_compte_user, pref_compte_stock_user,nom_user,
            todayDate, prefix_operation;
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
        root = inflater.inflate(R.layout.fragment_stock, container, false);

        getActivity().setTitle("Stock disponible");

        progressBarLoadStock = root.findViewById(R.id.stock_progress_load_stock);
        recyclerViewStock = root.findViewById(R.id.stock_recycle_stock);

        preferences = getActivity().getSharedPreferences("maPreference", MODE_PRIVATE);
        editor = preferences.edit();

        recyclerViewStock.setHasFixedSize(true);
        recyclerViewStock.setLayoutManager(new LinearLayoutManager(getContext()));

        stockRepository = StockRepository.getInstance();
        adapterStock = new AdapterStock(getContext());

        pref_code_depot = preferences.getString("pref_depot_user","");
        pref_compte_user = preferences.getString("pref_compte_user","");
        nom_user = preferences.getString("pref_nom_user","");
        pref_compte_stock_user = preferences.getString("pref_compte_stock_user","");

        LoadListeStock(progressBarLoadStock,
                recyclerViewStock, Integer.valueOf(pref_compte_stock_user),pref_code_depot);




        return root;
    }

    public void LoadListeStock(ProgressBar loadStock, RecyclerView listeStock,
                                      int numCompte, String codeDepot)
    {
        Call<List<StockResponse>> call_liste_stock = stockRepository.stockConnexion().getStockDepot(codeDepot, numCompte);
                loadStock.setVisibility(View.VISIBLE);
        call_liste_stock.enqueue(new Callback<List<StockResponse>>() {
            @Override
            public void onResponse(Call<List<StockResponse>> call, Response<List<StockResponse>> response) {
                if (response.isSuccessful())
                {
                    loadStock.setVisibility(View.GONE);
                    double _sortie_totale = 0;
                    double _achat_total = 0;
                    List<StockResponse> list_local_releve = new ArrayList<>();
                    for (int a = 0; a < response.body().size(); a++)
                    {
                        StockResponse liste_stock =
                                new StockResponse (
                                        response.body().get(a).getCodeArticle(),
                                        response.body().get(a).getDesignationArticle(),
                                        response.body().get(a).getDesignationDepot(),
                                        response.body().get(a).getEnStock(),
                                        response.body().get(a).getPrixMoyen(),
                                        response.body().get(a).getValeurMoyenne(),
                                        response.body().get(a).getPrixAchat()

                                );

//                        textView_solde_jour.setText(String.format("$%s", response.body().get(a).getSolde()));
//                        textView_solde_achat.setText(String.format("$%s", _achat_total));

                        list_local_releve.add(liste_stock);
                    }
                    adapterStock.setList(list_local_releve);
                    listeStock.setAdapter(adapterStock);

                }
            }

            @Override
            public void onFailure(Call<List<StockResponse>> call, Throwable t) {
                loadStock.setVisibility(View.GONE);
            }
        });
    }
}