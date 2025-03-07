package co.edu.unipiloto.petapp.retrofit;

import java.util.List;
import java.util.Map;

import co.edu.unipiloto.petapp.model.Usuario;
import co.edu.unipiloto.petapp.model.Mascota;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface PetApi {

    @POST("/usuario/save")
    Call<Usuario> getSaveUsuario(@Body Usuario usuario);
    @GET("/usuario/verificarCorreo")
    Call<Boolean> verificarCorreo(@Query("correo") String correo);
    @GET("/usuario/id")
    Call<Integer> obtenerIdUsuario(@Query("correo") String correo);
    @POST("/usuario/login")
    Call<Boolean> login(@Query("correo") String correo, @Query("password") String password);
    @POST("/mascota/agregar")
    Call<Map<String, Object>> agregarMascota(@Query("id_usuario") int idUsuario, @Body Map<String, Object> mascota);
    @GET("/mascota/getByUsuario")
    Call<List<Mascota>> getMascotasByUsuario(@Query("correo") String correo);


}



