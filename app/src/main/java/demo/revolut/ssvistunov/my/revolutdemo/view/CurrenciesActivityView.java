package demo.revolut.ssvistunov.my.revolutdemo.view;

import android.support.v7.widget.RecyclerView;

import java.util.Map;

/**
 * Created by SSvistunov on 17.11.2017.
 */

public interface CurrenciesActivityView {

    void initList(RecyclerView.Adapter adapter);
    void showError(String errorString);
}
