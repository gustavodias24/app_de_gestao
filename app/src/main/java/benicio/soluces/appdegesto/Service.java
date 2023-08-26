package benicio.soluces.appdegesto;

import benicio.soluces.appdegesto.model.EmpresaModel;
import benicio.soluces.appdegesto.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Service {

    @POST("cadastrar_empresa")
    Call<ResponseModel> criarEmpresa(@Body EmpresaModel empresa);

    @POST("login_empresa")
    Call<ResponseModel> logarEmpresa(@Body EmpresaModel empresa);
}
