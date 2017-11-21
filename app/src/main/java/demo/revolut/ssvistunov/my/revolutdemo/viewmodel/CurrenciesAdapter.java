package demo.revolut.ssvistunov.my.revolutdemo.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.revolut.ssvistunov.my.revolutdemo.R;
import demo.revolut.ssvistunov.my.revolutdemo.viewmodel.model.Currency;
import io.reactivex.disposables.Disposable;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.CurrencyItemiewHolder> {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    private CurrenciesViewModel viewModel;
    private Map<String, EditText> editTextCache = new HashMap<>();

    public static CurrenciesAdapter create(CurrenciesViewModel viewModel) {
        return new CurrenciesAdapter(viewModel);
    }

    private CurrenciesAdapter(CurrenciesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public CurrencyItemiewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false);
        return new CurrencyItemiewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(CurrencyItemiewHolder holder, int position) {
        final Currency item = viewModel.getModelList().get(position);
        bindToViewHolder(holder, item);
    }

    private void bindToViewHolder(CurrencyItemiewHolder holder, Currency item) {
        fillWithCurrentModel(holder, item);
        setActionsForClickAndFocus(holder, item);
        editTextCache.put(item.getName(), holder.editRate);
    }

    private void setActionsForClickAndFocus(CurrencyItemiewHolder holder, Currency item) {
        //Set action for click on whole view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapToZeroPositionAndAnimate(item, holder);
            }
        });
        //Set action for in focus for EditText
        holder.editRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isInFocus) {
                handleFocusForItem(isInFocus, item, holder);
            }
        });
    }

    private void fillWithCurrentModel(CurrencyItemiewHolder holder, Currency item) {
        //Fill view with data
        holder.textLabel.setText(item.getName());
        setValueToEditText(holder.editRate, item.getKoef());
    }

    private void handleFocusForItem(boolean isInFocus, Currency item, CurrencyItemiewHolder holder) {
        if (isInFocus) {
            swapToZeroPositionAndAnimate(item, holder);
            holder.setReactiveEditText(subscribeToTextEvents(item, holder));
            positionCursorBeforePoint(holder);
        } else {
            disposeAndRemoveKeyboard(holder);
        }
    }

    private void disposeAndRemoveKeyboard(CurrencyItemiewHolder holder) {
        //Dispose
        holder.disposeReactiveEditTextIfPossible();
        //Remove soft keyboardd
        InputMethodManager imm = (InputMethodManager)holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(holder.editRate.getWindowToken(), 0);
    }

    private void positionCursorBeforePoint(CurrencyItemiewHolder holder) {
        int posOfBeforePoint = holder.editRate.getText().toString().lastIndexOf(".") - 1;
        holder.editRate.setSelection(posOfBeforePoint);
    }

    @NonNull
    private Disposable subscribeToTextEvents(Currency item, CurrencyItemiewHolder holder) {
        return RxTextView.textChanges(holder.editRate)
                .skip(1)
                .map(text -> helperMapEmptyStringToZero(text.toString()))
                .map(Float::parseFloat)
                .doOnNext(value ->  {
                    viewModel.modifyCurrentAmountWithItem(value, item.getKoef());
                    updateAllCurrencyValues();
                })
                .subscribe();
    }

    private String helperMapEmptyStringToZero(String str) {
        if (str == null || str.isEmpty() || str.equals("."))
            return "0";
        else
            return str;
    }

    private void setValueToEditText(EditText editText, float koef) {
        float result = koef * viewModel.getCurrentAmount();
        editText.setText(String.format(Locale.US, "%.2f", result));
    }

    @Override
    public int getItemCount() {
        return viewModel.getModelList().size();
    }

    private void swapToZeroPositionAndAnimate(Currency item, CurrencyItemiewHolder holder) {
        //Old position
        int pos = viewModel.getModelList().indexOf(item);

        //swap if possible
        if (viewModel.getModelList().remove(item)) {
            viewModel.getModelList().add(0, item);

            //set focus to top element
            holder.editRate.requestFocus();

            // Show soft keyboard for the user to enter the value.
            InputMethodManager imm = (InputMethodManager)holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(holder.editRate, InputMethodManager.SHOW_IMPLICIT);

            //let recyclerView refresh and animate it
            notifyItemMoved(pos, 0);
        }
    }

    public void updateAllCurrencyValues() {
        for (String currencyName : editTextCache.keySet()) {
            Float koef = getKoefByName(currencyName);
            updateCurrencyValue(currencyName, koef);
        }
    }

    private void updateCurrencyValue(String currencyName, Float koef) {
        EditText editText = editTextCache.get(currencyName);

        boolean isTopItem = false;
        if (viewModel.getModelList().size() > 0) {
            Currency topItem = viewModel.getModelList().get(0);
            if (topItem.getName().equals(currencyName))
                isTopItem = true;
        }

        if (!isTopItem)
            setValueToEditText(editText, koef);
    }

    private Float getKoefByName(String name) {
        for (Currency item : viewModel.getModelList()) {
            if (item.getName().equals(name))
                return item.getKoef();
        }
        return Float.NaN;
    }


    class CurrencyItemiewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.currency_label)
        TextView textLabel;

        @BindView(R.id.currency_rateValue)
        EditText editRate;

        private Disposable reactiveEditText;

        CurrencyItemiewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void disposeReactiveEditTextIfPossible() {
            if (reactiveEditText != null)
                reactiveEditText.dispose();
        }

        void setReactiveEditText(Disposable reactiveEditText) {
            this.reactiveEditText = reactiveEditText;
        }
    }
}
