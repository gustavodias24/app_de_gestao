package benicio.soluces.appdegesto.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.Service;
import benicio.soluces.appdegesto.databinding.ActivityMainBinding;
import benicio.soluces.appdegesto.databinding.CadastroEmpresaLayoutBinding;
import benicio.soluces.appdegesto.model.EmpresaModel;
import benicio.soluces.appdegesto.model.ResponseModel;
import benicio.soluces.appdegesto.util.RetrofitUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ActivityMainBinding binding;
    private Retrofit retrofit;
    private Service service;
    private Dialog dialogCarregando, dialogCriarEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        preferences = getSharedPreferences("empresa_preferences", MODE_PRIVATE);
        editor = preferences.edit();

        if ( preferences.getBoolean("isLogado", false)){
            irParaContabilidade();
        }

        binding.entrarBtn.setOnClickListener( view->{
            String login, senha;

            login = binding.loginField.getEditText().getText().toString().trim();
            senha = binding.senhaField.getEditText().getText().toString().trim();


            binding.errorSenha.setOnClickListener( senhaView -> {
                binding.senhaField.setError(null);
            });

            binding.errorLogin.setOnClickListener( senhaView -> {
                binding.loginField.setError(null);
            });

            if ( !login.isEmpty() ){
                if( !senha.isEmpty()){
                    if ( login.equals("admin") && senha.equals("admin233")){
                        editor.putBoolean("isLogado", true);
                        editor.putString("login", login);
                        editor.apply();
                        startActivity(new Intent(
                                getApplicationContext(),
                                EmpresasCadastradasActivity.class
                        ));
                    }else{
                        logarEmpresa(new EmpresaModel(login, senha));
                    }
                }else{
                    binding.senhaField.setError("Campo senha obrigatório!");
                }
            }else{
                binding.loginField.setError("Campo login obrigatório!");
            }

        });

        binding.cadastrarEmpresaText.setOnClickListener( viewCadastrar -> {
            dialogCriarEmpresa.show();
        });

        dialogCarregando = RetrofitUtil.criarDialogCarregando(MainActivity.this);
        criarDialogEmpresa();
        retrofit = RetrofitUtil.criarRetrofit();
        service = RetrofitUtil.criarServie(retrofit);
    }

    public void criarDialogEmpresa(){
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        CadastroEmpresaLayoutBinding cadastroBinding = CadastroEmpresaLayoutBinding.inflate(getLayoutInflater());

        cadastroBinding.criarEmpresaBtn.setOnClickListener( criarEmpresaView -> {
            String login, senha;

            login = cadastroBinding.loginField.getEditText().getText().toString();
            senha = cadastroBinding.senhaField.getEditText().getText().toString();

            cadastroBinding.errorSenha.setOnClickListener( senhaView -> {
                cadastroBinding.senhaField.setError(null);
            });

            cadastroBinding.errorLogin.setOnClickListener( senhaView -> {
                cadastroBinding.loginField.setError(null);
            });



            if ( !login.isEmpty() ){
                if( !senha.isEmpty()){
                    criarEmpresa(new EmpresaModel(login, senha));
                }else{
                    cadastroBinding.senhaField.setError("Campo senha obrigatório!");
                }
            }else{
                cadastroBinding.loginField.setError("Campo login obrigatório!");
            }
        });

        b.setView(cadastroBinding.getRoot());

        dialogCriarEmpresa = b.create();
    }


    public void criarEmpresa(EmpresaModel empresaModel){
        dialogCarregando.show();
        service.criarEmpresa(empresaModel).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                dialogCarregando.dismiss();

                if ( responseModel.getSuccess()){
                    dialogCriarEmpresa.dismiss();
                    binding.loginField.getEditText().setText(empresaModel.getLogin());
                    binding.senhaField.getEditText().setText(empresaModel.getSenha());
                }

                Toast.makeText(MainActivity.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }

    public void irParaContabilidade(){
        finish();
        startActivity(new Intent(getApplicationContext(), ContabilidadeActivity.class));
    }

    public void logarEmpresa(EmpresaModel empresaModel){
        dialogCarregando.show();
        service.logarEmpresa(empresaModel).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                dialogCarregando.dismiss();
                ResponseModel responseModel =  response.body();

                if ( responseModel.getSuccess() ){
                    editor.putBoolean("isLogado", true);
                    editor.putString("login", empresaModel.getLogin());
                    editor.apply();
                    irParaContabilidade();
                }

                Toast.makeText(MainActivity.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialogCarregando.dismiss();
            }
        });
    }


}