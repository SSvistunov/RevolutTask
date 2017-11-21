package demo.revolut.ssvistunov.my.revolutdemo.model.network;

import demo.revolut.ssvistunov.my.revolutdemo.model.entity.RevolutResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RevolutRetrofitService {
    @GET("/latest")
    Observable<RevolutResponse> getCurrencies(@Query("base") String base);
}
