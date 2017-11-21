package demo.revolut.ssvistunov.my.revolutdemo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.revolut.ssvistunov.my.revolutdemo.App;
import demo.revolut.ssvistunov.my.revolutdemo.R;
import demo.revolut.ssvistunov.my.revolutdemo.viewmodel.CurrenciesViewModel;

public class CurrenciesActivity extends AppCompatActivity implements CurrenciesActivityView {

    @Inject
    public CurrenciesViewModel viewModel;

    @BindView(R.id.rates_list)
    RecyclerView ratesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencies);
        ButterKnife.bind(this);
        App.getComponent().inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.attachView(this);
    }

    @Override
    public void initList(RecyclerView.Adapter adapter) {
        ratesList.setLayoutManager(new LinearLayoutManager(this));
        ratesList.setHasFixedSize(true);
        ratesList.setItemViewCacheSize(adapter.getItemCount());
        ratesList.setAdapter(adapter);
    }

    @Override
    public void showError(String errorString) {
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        ratesList.setAdapter(null);
        viewModel.detachView();
        super.onStop();
    }
}
