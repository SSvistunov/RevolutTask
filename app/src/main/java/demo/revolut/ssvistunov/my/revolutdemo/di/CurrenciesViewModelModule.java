package demo.revolut.ssvistunov.my.revolutdemo.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import demo.revolut.ssvistunov.my.revolutdemo.model.repository.CurrencyRepository;
import demo.revolut.ssvistunov.my.revolutdemo.viewmodel.CurrenciesViewModel;

@Module
public class CurrenciesViewModelModule {

    @Provides
    @Singleton
    CurrenciesViewModel provideViewModel(CurrencyRepository repository) {
            return new CurrenciesViewModel(repository);
    }

}
