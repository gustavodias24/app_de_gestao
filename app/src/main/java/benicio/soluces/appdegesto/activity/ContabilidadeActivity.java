package benicio.soluces.appdegesto.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.Service;
import benicio.soluces.appdegesto.adapter.AdapterTransacao;
import benicio.soluces.appdegesto.databinding.ActivityContabilidadeBinding;
import benicio.soluces.appdegesto.databinding.AdicionarTransicaoLayoutBinding;
import benicio.soluces.appdegesto.model.ListaResumoTransacaoModel;
import benicio.soluces.appdegesto.model.ResponseModel;
import benicio.soluces.appdegesto.model.TransacaoModel;
import benicio.soluces.appdegesto.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContabilidadeActivity extends AppCompatActivity {

    private Dialog dialogReceita, dialogDespesa, dialogTranferi, dialogCarregando;
    private Retrofit retrofit;
    private Service service;
    int tempoTransicao = 0;
    private ActivityContabilidadeBinding activityBinding;
    private Button receitaBtn, despesaBtn, transfeBtn, transacoesBtn, verTodosBtn, verHojeBtn, verMesBtn;
    private TextView totalRecebidoText, totalPagoText, saldoText;
    private SharedPreferences preferences;
    private RecyclerView recyclerTransacoes;
    private AdapterTransacao adapter;
    private List<TransacaoModel> lista = new ArrayList<>();

    private String LOGIN_QUERY;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityContabilidadeBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        b = getIntent().getExtras();
        preferences = getSharedPreferences("empresa_preferences", MODE_PRIVATE);
        confirugarComponentes();
        selecaoDoTempoDaTransicao();

        criarAlertDialogTransicao(0, "Adicionar Receita");
        criarAlertDialogTransicao(1, "Adicionar Despesa");
        criarAlertDialogTransicao(2, "Adicionar Transferência");

        activityBinding.receitaBtn.setOnClickListener( addReceita -> {
            dialogReceita.show();
        });

        activityBinding.despesaBtn.setOnClickListener( addDespesa -> {
            dialogDespesa.show();
        });

        activityBinding.transferenciaBtn.setOnClickListener( addtransferencia -> {
            dialogTranferi.show();
        });

        activityBinding.trandacoesBtn.setOnClickListener( transaView -> {
            Intent i = new Intent(getApplicationContext(), TransacoesActivity.class);
            if ( b == null){
                finish();
            }else{
                i.putExtra("login", b.getString("login", ""));
            }
            startActivity(i);
        });

        dialogCarregando = RetrofitUtil.criarDialogCarregando(ContabilidadeActivity.this);
        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarServie(retrofit);

        if ( b != null){
            if ( b.getString("login", "") != null &&  !b.getString("login", "").isEmpty()){
                LOGIN_QUERY = b.getString("login", "");
            }
        }
        else{
            LOGIN_QUERY = preferences.getString("login", "");
        }
    }

    public void criarAlertDialogTransicao(int tipo, String title){
        AlertDialog.Builder b = new AlertDialog.Builder(ContabilidadeActivity.this);
        AdicionarTransicaoLayoutBinding dialogBinding = AdicionarTransicaoLayoutBinding.inflate(getLayoutInflater());
        dialogBinding.titleTransacaoText.setText(title);
        b.setView(dialogBinding.getRoot());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dialogBinding.dataField.getEditText().setText(dateFormat.format(calendar.getTime()));

        dialogBinding.salvarTransacaoBtn.setOnClickListener( saveReceita -> {
            String valor, categoria, metodo, data;

            valor = dialogBinding.valorField.getEditText().getText().toString();
            categoria = dialogBinding.categoriaField.getEditText().getText().toString();
            metodo = dialogBinding.metodoPagamentoField.getEditText().getText().toString();
            data = dialogBinding.dataField.getEditText().getText().toString();

            salvarTransicao(new TransacaoModel(data, categoria, metodo, tipo, Double.parseDouble(valor)));

            dialogBinding.valorField.getEditText().setText("");
            dialogBinding.categoriaField.getEditText().setText("");
            dialogBinding.metodoPagamentoField.getEditText().setText("");
        });

        switch (tipo){
            case 0:
                dialogReceita = b.create();
                break;
            case 1:
                dialogDespesa = b.create();
                break;
            default:
                dialogTranferi = b.create();
                break;
        }
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
            listarTodos();
        });

        verHojeBtn.setOnClickListener( verHojeView -> {
            getHojeTransicoes();
            tempoTransicao = 1;

            verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));
            verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));

            verTodosBtn.setTextColor(Color.parseColor("#673AB7"));
            verHojeBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
            verMesBtn.setTextColor(Color.parseColor("#673AB7"));
        });

        verMesBtn.setOnClickListener( vewMesView ->{
            getMesTransicoes();
            tempoTransicao = 2;

            verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
            verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));

            verTodosBtn.setTextColor(Color.parseColor("#673AB7"));
            verHojeBtn.setTextColor(Color.parseColor("#673AB7"));
            verMesBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
        });

    }

    public void salvarTransicao(TransacaoModel transacaoModel){
        dialogCarregando.show();
        service.salvarTransacao(transacaoModel, LOGIN_QUERY).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                dialogCarregando.dismiss();
                Toast.makeText(ContabilidadeActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
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
                    activityBinding.totalRecebidoText.setText(
                            String.format("Recebido: %.2f", response.body().getReceitas()));
                    activityBinding.totalPagoText.setText(
                            String.format("Pago: %.2f", response.body().getDespesas()));
                    activityBinding.totalSaldoText.setText(
                            String.format("Saldo: %.2f", response.body().getSaldo()));
                }
            }

            @Override
            public void onFailure(Call<ListaResumoTransacaoModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    public void getHojeTransicoes(){
        lista.clear();
        dialogCarregando.show();
        service.getHojeTransacoes(LOGIN_QUERY).enqueue(new Callback<ListaResumoTransacaoModel>() {
            @Override
            public void onResponse(Call<ListaResumoTransacaoModel> call, Response<ListaResumoTransacaoModel> response) {
                dialogCarregando.dismiss();

                if ( response.isSuccessful() ){
                    lista.addAll(response.body().getLista());
                    adapter.notifyDataSetChanged();
                    activityBinding.totalRecebidoText.setText(
                            String.format("Recebido: %.2f", response.body().getReceitas()));
                    activityBinding.totalPagoText.setText(
                            String.format("Pago: %.2f", response.body().getDespesas()));
                    activityBinding.totalSaldoText.setText(
                            String.format("Saldo: %.2f", response.body().getSaldo()));
                }
            }

            @Override
            public void onFailure(Call<ListaResumoTransacaoModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    public void getMesTransicoes(){
        lista.clear();
        dialogCarregando.show();
        service.getMesTransacoes(LOGIN_QUERY).enqueue(new Callback<ListaResumoTransacaoModel>() {
            @Override
            public void onResponse(Call<ListaResumoTransacaoModel> call, Response<ListaResumoTransacaoModel> response) {
                dialogCarregando.dismiss();

                if ( response.isSuccessful() ){
                    lista.addAll(response.body().getLista());
                    adapter.notifyDataSetChanged();
                    activityBinding.totalRecebidoText.setText(
                            String.format("Recebido: %.2f", response.body().getReceitas()));
                    activityBinding.totalPagoText.setText(
                            String.format("Pago: %.2f", response.body().getDespesas()));
                    activityBinding.totalSaldoText.setText(
                            String.format("Saldo: %.2f", response.body().getSaldo()));
                }
            }

            @Override
            public void onFailure(Call<ListaResumoTransacaoModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        listarTodos();
    }

    public void listarTodos(){
        getAllTransicoes();
        tempoTransicao = 0;

        verTodosBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.botao));
        verHojeBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));
        verMesBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.white));

        verTodosBtn.setTextColor(Color.parseColor("#FFFFFFFF"));
        verHojeBtn.setTextColor(Color.parseColor("#673AB7"));
        verMesBtn.setTextColor(Color.parseColor("#673AB7"));
    }



}