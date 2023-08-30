package benicio.soluces.appdegesto;

import java.util.List;

import benicio.soluces.appdegesto.model.EmpresaModel;
import benicio.soluces.appdegesto.model.ListaResumoTransacaoModel;
import benicio.soluces.appdegesto.model.ResponseModel;
import benicio.soluces.appdegesto.model.TransacaoModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    @POST("cadastrar_empresa")
    Call<ResponseModel> criarEmpresa(@Body EmpresaModel empresa);

    @POST("login_empresa")
    Call<ResponseModel> logarEmpresa(@Body EmpresaModel empresa);

    @POST("{login}/transacao")
    Call<ResponseModel> salvarTransacao(@Body TransacaoModel transacaoModel, @Path("login") String login);

    @POST("{login}/todas/transacoes")
    Call<ListaResumoTransacaoModel> getTodasTransacoes(@Path("login") String login);

    @POST("{login}/hoje/transacoes")
    Call<ListaResumoTransacaoModel> getHojeTransacoes(@Path("login") String login);
    @POST("{login}/mes/transacoes")
    Call<ListaResumoTransacaoModel> getMesTransacoes(@Path("login") String login);
    @POST("lista_empresa")
    Call<List<EmpresaModel>> listarEmpresas();
}
