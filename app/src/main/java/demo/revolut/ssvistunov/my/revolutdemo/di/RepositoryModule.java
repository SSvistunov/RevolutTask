package demo.revolut.ssvistunov.my.revolutdemo.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import demo.revolut.ssvistunov.my.revolutdemo.model.network.NetworkManager;
import demo.revolut.ssvistunov.my.revolutdemo.model.repository.CurrencyRepositoryImpl;
import demo.revolut.ssvistunov.my.revolutdemo.model.repository.CurrencyRepository;

@Module
public class RepositoryModule {
    private CurrencyRepository repository;

    public RepositoryModule(Context context, NetworkManager networkManager) {
        repository = new CurrencyRepositoryImpl(context, networkManager);
    }

    @Provides
    @Singleton
    CurrencyRepository provideRepository() {
        return repository;
    }
}
