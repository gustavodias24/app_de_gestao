package benicio.soluces.appdegesto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.adapter.AdapterTransacao;
import benicio.soluces.appdegesto.databinding.ActivityContabilidadeBinding;
import benicio.soluces.appdegesto.model.TransacaoModel;

public class ContabilidadeActivity extends AppCompatActivity {

    int tempoTransicao = 0;
    private ActivityContabilidadeBinding activityBinding;
    private Button receitaBtn, despesaBtn, transfeBtn, transacoesBtn, verTodosBtn, verHojeBtn, verMesBtn;
    private TextView totalRecebidoText, totalPagoText, saldoText;
    private RecyclerView recyclerTransacoes;
    private AdapterTransacao adapter;
    private List<TransacaoModel> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityContabilidadeBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        confirugarComponentes();
        selecaoDoTempoDaTransicao();

    }

    public void confirugarComponentes(){
        receitaBtn = activityBinding.receitaBtn;
        despesaBtn = activityBinding.despesaBtn;
        transacoesBtn = activityBinding.trandacoesBtn;
        transfeBtn = activityBinding.transferenciaBtn;
        verTodosBtn = activityBinding.verTodosBtn;
        verHojeBtn = activityBinding.verHojeBtn;
        verMesBtn = activityBinding.verMesBtn;
        totalPagoText = activityBinding.totalPagoText;
        totalRecebidoText = activityBinding.totalRecebidoText;
        saldoText = activityBinding.totalSaldoText;
        // configuracoes do recycler
        recyclerTransacoes = activityBinding.transacoesRecycler;
        recyclerTransacoes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerTransacoes.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerTransacoes.setHasFixedSize(true);
        adapter = new AdapterTransacao(lista, getApplicationContext());
        recyclerTransacoes.setAdapter(adapter);
    }

    public void selecaoDoTempoDaTransicao(){

        verTodosBtn.setOnClickListener( verTodosView -> {
            tempoTransicao = 0;

            verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));
            verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));

            verTodosBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
            verHojeBtn.setTextColor(Color.parseColor("#673AB7"));
            verMesBtn.setTextColor(Color.parseColor("#673AB7"));
        });

        verHojeBtn.setOnClickListener( verHojeView -> {
            tempoTransicao = 1;

            verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));
            verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));

            verTodosBtn.setTextColor(Color.parseColor("#673AB7"));
            verHojeBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
            verMesBtn.setTextColor(Color.parseColor("#673AB7"));
        });

        verMesBtn.setOnClickListener( vewMesView ->{
            tempoTransicao = 2;

            verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));

            verTodosBtn.setTextColor(Color.parseColor("#673AB7"));
            verHojeBtn.setTextColor(Color.parseColor("#673AB7"));
            verMesBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}