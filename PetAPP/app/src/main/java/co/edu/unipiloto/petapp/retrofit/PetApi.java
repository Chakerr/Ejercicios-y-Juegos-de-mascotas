package co.edu.unipiloto.petapp.retrofit;

import java.util.List;
import java.util.Map;

import co.edu.unipiloto.petapp.model.HistorialMedico;
import co.edu.unipiloto.petapp.model.Medicamento;
import co.edu.unipiloto.petapp.model.Usuario;
import co.edu.unipiloto.petapp.model.Mascota;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.PUT;


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
    @GET("/mascota/usuario/{idUsuario}")
    Call<List<Mascota>> getMascotasByUsuarioId(@Path("idUsuario") int idUsuario);
    @GET("/medicamentos/pendientes")
    Call<List<Medicamento>> obtenerMedicamentosPendientes();
    @POST("/medicamentos")
    Call<Medicamento> guardarMedicamento(@Body Medicamento medicamento);
    @GET("/medicamentos")
    Call<List<Medicamento>> getMedicamentos();
    @PUT("/medicamentos/{id}/administrado")
    Call<Medicamento> marcarMedicamentoComoAdministrado(@Path("id") Long id);
    @GET("medicamentos/mascota/{idMascota}")
    Call<List<Medicamento>> getMedicamentosPorMascota(@Path("idMascota") int idMascota);
    @POST("/historialMedico/agregar/{idMascota}")
    Call<HistorialMedico> guardarHistorialMedico(@Path("idMascota") int idMascota, @Body HistorialMedico historialMedico);
    @PUT("historialMedico/editar/{id}")
    Call<Void> editarHistorial(@Path("id") int id, @Body HistorialMedico historial);

    @GET("/historialMedico/porMascota/{idMascota}")
    Call<HistorialMedico> getHistorialMedicoPorMascota(@Path("idMascota") int idMascota);
    @DELETE("/historialMedico/eliminar/{idMascota}")
    Call<Void> eliminarHistorial(@Path("id") int id);

}