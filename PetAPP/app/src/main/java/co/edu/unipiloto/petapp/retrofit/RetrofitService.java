package co.edu.unipiloto.petapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private final Retrofit retrofit;

    public RetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
