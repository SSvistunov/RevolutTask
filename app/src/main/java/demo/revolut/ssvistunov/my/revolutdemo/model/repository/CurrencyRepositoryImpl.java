package demo.revolut.ssvistunov.my.revolutdemo.model.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import demo.revolut.ssvistunov.my.revolutdemo.model.entity.RevolutResponse;
import demo.revolut.ssvistunov.my.revolutdemo.model.network.NetworkManager;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class CurrencyRepositoryImpl implements CurrencyRepository {

    private RevolutResponse currencies;
    private SharedPrefManager prefManager;
    private NetworkManager networkManager;

    private RepositoryListener listener;

    public CurrencyRepositoryImpl(Context context, NetworkManager network) {
        this.prefManager = new SharedPrefManager(context);
        this.networkManager = network;
    }

    @Override
    public Map<String, Float> getDataFromCache() {
        String cachedData = prefManager.load();
        currencies = new Gson().fromJson(cachedData, RevolutResponse.class);
        if (currencies != null && currencies.getRates() != null) {
            addBaseCurrencyToBeginOfList();
            return currencies.getRates();
        }
        return null;
    }

    private void addBaseCurrencyToBeginOfList() {
        currencies.getRates().put(currencies.getBase(), 1.0f);
    }

    @Override
    public void getDataFromNetworkAndListen() {

        networkManager.requestDataAndListen(new NetworkManager.NetworkListener() {
            @Override
            public void onSuccess(RevolutResponse newCurrencies) {
                currencies = newCurrencies;
                if (listener != null)
                    listener.currenciesFromNetwork(proceedToListener(currencies));
                startSavingData(newCurrencies);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("failure", t.getLocalizedMessage());
                String errorString = (t != null)?t.getLocalizedMessage():"Unknown error";
                if (listener != null)
                    listener.errorFromRepository(errorString);
            }
        });
    }

    private void startSavingData(RevolutResponse newCurrencies) {
        String stringData  = new Gson().toJson(newCurrencies);
        Flowable.just(stringData)
                .subscribeOn(Schedulers.io())
                .doOnNext(str -> prefManager.save(str))
                .subscribe();
    }

    @Override
    public void shutdownGettingDataFromNetwork() {
        networkManager.shutdownRequestsToServer();
    }

    @Override
    public void setListener(RepositoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener() {
        this.listener = null;
    }

    private Map<String, Float> proceedToListener(RevolutResponse currencies) {
        this.currencies = currencies;
        addBaseCurrencyToBeginOfList();
        return currencies.getRates();
    }
}
