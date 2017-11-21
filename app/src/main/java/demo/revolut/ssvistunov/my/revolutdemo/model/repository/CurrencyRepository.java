package demo.revolut.ssvistunov.my.revolutdemo.model.repository;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;

public interface CurrencyRepository {

    Map<String, Float> getDataFromCache();
    void getDataFromNetworkAndListen();
    void shutdownGettingDataFromNetwork();

    void setListener(RepositoryListener listener);
    void removeListener();
}
