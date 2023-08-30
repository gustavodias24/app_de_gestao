package benicio.soluces.appdegesto.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import benicio.soluces.appdegesto.Service;
import benicio.soluces.appdegesto.databinding.CarregandoLayoutBinding;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    public static Dialog criarDialogCarregando(Activity c){
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setView(CarregandoLayoutBinding.inflate(c.getLayoutInflater()).getRoot());
        b.setCancelable(false);
        return b.create();
    }

    public static Retrofit criarRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api-app-de-gestao.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Service criarService(Retrofit retrofit){
        return  retrofit.create(Service.class);
    }

    public static Service criarServie(Retrofit retrofit){
        return retrofit.create(Service.class);
    }
}
