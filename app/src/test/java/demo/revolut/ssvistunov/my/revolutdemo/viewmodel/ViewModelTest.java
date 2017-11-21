package demo.revolut.ssvistunov.my.revolutdemo.viewmodel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import demo.revolut.ssvistunov.my.revolutdemo.model.repository.CurrencyRepository;
import demo.revolut.ssvistunov.my.revolutdemo.view.CurrenciesActivity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ViewModelTest {

    private CurrenciesViewModel viewModel;

    @Mock
    private CurrencyRepository repository;
    @Mock
    private CurrenciesActivity mockActivity;
    @Mock
    private CurrenciesAdapter adapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewModel = new CurrenciesViewModel(repository);
    }

    @After
    public void tearDown() {
        repository.shutdownGettingDataFromNetwork();
        repository = null;
        viewModel = null;
    }

    @Test
    public void tryWithoutAttachedView() throws Exception {
        //call detach in start
        viewModel.detachView();
        assertTrue(viewModel.getModelList().size() == 0);
    }

    @Test
    public void tryBehaviorAttachAndDetachView() throws Exception {
        viewModel.attachView(mockActivity);
        verify(repository).setListener(viewModel);
        verify(repository).getDataFromNetworkAndListen();
        viewModel.detachView();
        verify(repository).removeListener();
        verify(repository).shutdownGettingDataFromNetwork();
    }

}