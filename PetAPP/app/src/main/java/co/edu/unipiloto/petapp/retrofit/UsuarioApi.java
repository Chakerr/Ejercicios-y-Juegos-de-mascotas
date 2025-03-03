package co.edu.unipiloto.petapp.retrofit;

import co.edu.unipiloto.petapp.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsuarioApi {

    @POST("/usuario/save")
    Call<Usuario> getSaveUsuario(@Body Usuario usuario);
    @GET("/usuario/verificarCorreo")
    Call<Boolean> verificarCorreo(@Query("correo") String correo);
}



