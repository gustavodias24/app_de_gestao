package benicio.soluces.appdegesto.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.Service;
import benicio.soluces.appdegesto.adapter.AdapterEmpresa;
import benicio.soluces.appdegesto.databinding.ActivityEmpresasCadastradasBinding;
import benicio.soluces.appdegesto.model.EmpresaModel;
import benicio.soluces.appdegesto.util.RecyclerItemClickListener;
import benicio.soluces.appdegesto.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EmpresasCadastradasActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ActionBar bar;
    private Retrofit retrofit;
    private Service service;
    private RecyclerView r;
    private AdapterEmpresa adapter;
    private List<EmpresaModel> lista = new ArrayList<>();
    private ActivityEmpresasCadastradasBinding binding;
    private Dialog dialogCarregando;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmpresasCadastradasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("empresa_preferences", MODE_PRIVATE);
        editor = preferences.edit();

        bar = getSupportActionBar();
        bar.setTitle("Empresas cadastradas");
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);

        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarService(retrofit);
        dialogCarregando = RetrofitUtil.criarDialogCarregando(EmpresasCadastradasActivity.this);
        configuarRecycler();
        configurarListenerRecycler();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            editor.putBoolean("isLogado", false);
            editor.remove("login");
            editor.apply();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarEmpresas();
    }

    public void listarEmpresas(){
        lista.clear();
        dialogCarregando.show();
        service.listarEmpresas().enqueue(new Callback<List<EmpresaModel>>() {
            @Override
            public void onResponse(Call<List<EmpresaModel>> call, Response<List<EmpresaModel>> response) {
                dialogCarregando.dismiss();
                if ( response.isSuccessful() ){
                    lista.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EmpresaModel>> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    public void configuarRecycler(){
        r = binding.recycleEmpresas;
        r.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        r.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        adapter = new AdapterEmpresa(lista, getApplicationContext());
        r.setAdapter(adapter);
    }

    public void configurarListenerRecycler(){
        r.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                r,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(getApplicationContext(), ContabilidadeActivity.class);
                        i.putExtra("login", lista.get(position).getLogin());
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }
}