package demo.revolut.ssvistunov.my.revolutdemo.model.repository;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Stubber;

import demo.revolut.ssvistunov.my.revolutdemo.model.entity.RevolutResponse;
import demo.revolut.ssvistunov.my.revolutdemo.model.network.NetworkManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrenciesRepositoryTest {

    @Mock
    private NetworkManager networkManager;

    @Mock
    private Context context;

    private CurrencyRepositoryImpl repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new CurrencyRepositoryImpl(context, networkManager);
    }

    @After
    public void tearDown() {
        repository = null;
    }

    @Test
    public void tryToSendCallbackIfNoListenerWasSet() throws Exception {
        repository.setListener(null);
        repository.getDataFromNetworkAndListen();
        repository.shutdownGettingDataFromNetwork();
        verify(networkManager).shutdownRequestsToServer();
    }

    private NetworkManager.NetworkListener listener = new NetworkManager.NetworkListener() {
        @Override
        public void onSuccess(RevolutResponse currencies) {

        }

        @Override
        public void onFailure(Throwable t) {

        }
    };
}
