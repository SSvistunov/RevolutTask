package demo.revolut.ssvistunov.my.revolutdemo.model.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import demo.revolut.ssvistunov.my.revolutdemo.model.entity.RevolutResponse;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by SSvistunov on 20.11.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class NetworkManagerTest {

    @Mock
    private NetworkManager networkManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        networkManager = null;
    }

    @Test
    public void tryToTestRetrofit() throws Exception {

    }
}
