package demo.revolut.ssvistunov.my.revolutdemo.model.repository;

import java.util.Map;

public interface RepositoryListener {
    void currenciesFromNetwork(Map<String, Float> data);
    void errorFromRepository(String errorString);
}
