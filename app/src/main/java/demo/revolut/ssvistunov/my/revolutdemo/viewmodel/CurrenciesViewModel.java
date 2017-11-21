package demo.revolut.ssvistunov.my.revolutdemo.viewmodel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import demo.revolut.ssvistunov.my.revolutdemo.Config;
import demo.revolut.ssvistunov.my.revolutdemo.model.repository.CurrencyRepository;
import demo.revolut.ssvistunov.my.revolutdemo.model.repository.RepositoryListener;
import demo.revolut.ssvistunov.my.revolutdemo.view.CurrenciesActivityView;
import demo.revolut.ssvistunov.my.revolutdemo.viewmodel.model.Currency;

public class CurrenciesViewModel implements RepositoryListener{

    private CurrencyRepository repository;
    private CurrenciesActivityView view;

    private CurrenciesAdapter currenciesAdapter;
    private @NonNull List<Currency> listModel = Collections.emptyList();
    private Float currentAmount = Config.BASE_AMOUNT;

    public CurrenciesViewModel(CurrencyRepository repository) {
        this.repository = repository;
    }

    public void attachView(CurrenciesActivityView view) {
        this.view = view;
        repository.setListener(this);
        repository.getDataFromNetworkAndListen();
        createListModelIfNeeded(repository.getDataFromCache());
    }

    private void createListModelIfNeeded(Map<String, Float> ratesFromCache) {
        if (listModel.size() == 0) {
            listModel = mapRepoDataToModel(ratesFromCache);
            view.initList(buildAdapter());
        }
    }

    public void detachView() {
        currenciesAdapter = null;
        repository.shutdownGettingDataFromNetwork();
        repository.removeListener();
        this.view = null;
    }

    private RecyclerView.Adapter buildAdapter() {
        if (currenciesAdapter == null)
            currenciesAdapter = CurrenciesAdapter.create(this);
        return currenciesAdapter;
    }

    private List<Currency> mapRepoDataToModel(Map<String, Float> repoData) {
        ArrayList<Currency> returnValue = new ArrayList<>();
        if (repoData != null) {
            for (String currencyName : repoData.keySet()) {
                Float value = repoData.get(currencyName);
                if (currencyName.equals(Config.BASE_CURRENCY))
                    returnValue.add(0, Currency.create(currencyName, value));
                else
                    returnValue.add(Currency.create(currencyName, value));
            }
        }
        return returnValue;
    }

    @Override
    public void currenciesFromNetwork(Map<String, Float> data) {
        if (listModel.size() == 0)
            createListModelIfNeeded(data);
        else {
            updateListModelWithNewData(data);
            if (currenciesAdapter != null)
                currenciesAdapter.updateAllCurrencyValues();
            else
                view.initList(buildAdapter());
        }
    }

    private void updateListModelWithNewData(Map<String, Float> data) {
        for (String currencyName : data.keySet()) {
            Float value = data.get(currencyName);

            for (Currency item : listModel) {
                if (item.getName().equals(currencyName)) {
                    item.setKoef(value);
                    break;
                }
            }
        }
    }

    @Override
    public void errorFromRepository(String errorString) {
        view.showError(errorString);
    }

    public final List<Currency> getModelList() {
        return listModel;
    }

    public Float getCurrentAmount() {
        return currentAmount;
    }

    public void modifyCurrentAmountWithItem(float newValue, float koef) {
        if (koef > 0.0f)
            this.currentAmount = newValue / koef;
    }

}
