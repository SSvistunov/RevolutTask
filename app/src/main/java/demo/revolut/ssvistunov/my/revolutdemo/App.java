package demo.revolut.ssvistunov.my.revolutdemo;

import android.app.Application;

import demo.revolut.ssvistunov.my.revolutdemo.di.AppComponent;
import demo.revolut.ssvistunov.my.revolutdemo.di.DaggerAppComponent;
import demo.revolut.ssvistunov.my.revolutdemo.di.RepositoryModule;
import demo.revolut.ssvistunov.my.revolutdemo.model.network.NetworkManager;


public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = buildComponent();

    }

    public static AppComponent getComponent() {
        return appComponent;
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(getApplicationContext(), new NetworkManager()))
                .build();
    }
}
