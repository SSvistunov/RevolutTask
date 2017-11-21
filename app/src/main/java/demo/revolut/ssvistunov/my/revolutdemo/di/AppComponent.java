package demo.revolut.ssvistunov.my.revolutdemo.di;

import javax.inject.Singleton;

import dagger.Component;
import demo.revolut.ssvistunov.my.revolutdemo.view.CurrenciesActivity;

@Singleton
@Component(modules = {RepositoryModule.class, CurrenciesViewModelModule.class})
public interface AppComponent {
    void inject(CurrenciesActivity activity);
}
