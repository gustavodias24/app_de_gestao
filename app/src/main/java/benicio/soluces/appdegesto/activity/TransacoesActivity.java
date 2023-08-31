package benicio.soluces.appdegesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.Service;
import benicio.soluces.appdegesto.adapter.AdapterTransacao;
import benicio.soluces.appdegesto.databinding.ActivityTransacoesBinding;
import benicio.soluces.appdegesto.model.ListaResumoTransacaoModel;
import benicio.soluces.appdegesto.model.TransacaoModel;
import benicio.soluces.appdegesto.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransacoesActivity extends AppCompatActivity {
    Dialog dialogCarregando;
    private Retrofit retrofit;
    private Service service;
    private ActivityTransacoesBinding vbinding;
    private RecyclerView recyclerTransacoes;
    private AdapterTransacao adapter;
    private List<TransacaoModel> lista = new ArrayList<>();
    private SharedPreferences preferences;
    private String LOGIN_QUERY;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vbinding = ActivityTransacoesBinding.inflate(getLayoutInflater());
        setContentView(vbinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("empresa_preferences", MODE_PRIVATE);
        recyclerTransacoes = vbinding.transacoesRecycler;
        recyclerTransacoes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerTransacoes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerTransacoes.setHasFixedSize(true);
        adapter = new AdapterTransacao(lista, getApplicationContext());
        recyclerTransacoes.setAdapter(adapter);
        
        dialogCarregando = RetrofitUtil.criarDialogCarregando(TransacoesActivity.this);
        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarServie(retrofit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Todas as transações");

        b = getIntent().getExtras();
        if ( b != null){
            if ( b.getString("login", "") != null &&  !b.getString("login", "").isEmpty()){
                LOGIN_QUERY = b.getString("login", "");
            }
        }
        else{
            LOGIN_QUERY = preferences.getString("login", "");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){

            Intent i = new Intent(getApplicationContext(), ContabilidadeActivity.class);
            if ( b == null){
                finish();
            }else{
                i.putExtra("login", b.getString("login", ""));
            }

            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllTransicoes();
    }

    public void getAllTransicoes(){
        lista.clear();
        dialogCarregando.show();
        service.getTodasTransacoes(LOGIN_QUERY).enqueue(new Callback<ListaResumoTransacaoModel>() {
            @Override
            public void onResponse(Call<ListaResumoTransacaoModel> call, Response<ListaResumoTransacaoModel> response) {
                dialogCarregando.dismiss();

                if ( response.isSuccessful() ){
                    lista.addAll(response.body().getLista());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ListaResumoTransacaoModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }
    
    
}