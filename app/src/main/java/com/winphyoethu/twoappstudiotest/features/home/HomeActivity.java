package com.winphyoethu.twoappstudiotest.features.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.winphyoethu.twoappstudiotest.R;
import com.winphyoethu.twoappstudiotest.data.ApiService;
import com.winphyoethu.twoappstudiotest.data.ProvideRetrofit;
import com.winphyoethu.twoappstudiotest.data.model.UrlModel;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowError;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowList;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowLoading;
import com.winphyoethu.twoappstudiotest.features.home.homestate.UpdateList;
import com.winphyoethu.twoappstudiotest.features.viewmodelfactory.ViewModelFactory;
import com.winphyoethu.twoappstudiotest.util.ui.ButtonHelper;
import com.winphyoethu.twoappstudiotest.util.ui.EditTextHelper;
import com.winphyoethu.twoappstudiotest.util.UrlRegex;
import com.winphyoethu.twoappstudiotest.util.ui.LinearLayoutManagerWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements ClickListener, ActionMode.Callback {

    private ProgressBar pbLoading;
    private RecyclerView rvUrl;
    private FloatingActionButton fabAddNewUrl;
    private TextView tvErrorMessage;
    private Button btnRetry;

    private UrlAdapter urlAdapter;

    private ActionMode actionMode;

    private HomeViewModel homeViewModel;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    // Helpers
    private final UrlRegex urlRegex = new UrlRegex();
    private final EditTextHelper editTextHelper = new EditTextHelper();
    private final ButtonHelper buttonHelper = new ButtonHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initiate
        ProvideRetrofit provideRetrofit = new ProvideRetrofit();
        ApiService apiService = provideRetrofit.provideRetrofit().create(ApiService.class);
        ViewModelFactory viewModelFactory = new ViewModelFactory(apiService);
        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);

        urlAdapter = new UrlAdapter(Glide.with(this), this);

        pbLoading = findViewById(R.id.pb_loading);
        rvUrl = findViewById(R.id.rv_url);
        fabAddNewUrl = findViewById(R.id.fab_add_new_url);
        tvErrorMessage = findViewById(R.id.tv_error_message);
        btnRetry = findViewById(R.id.btn_retry);

        // Set Recyclerview
        rvUrl.setLayoutManager(new LinearLayoutManagerWrapper(this));
        rvUrl.setAdapter(urlAdapter);

        homeViewModel.initiateUrlList();

        compositeDisposable.add(
                buttonHelper.bindFabButton(fabAddNewUrl)
                        .throttleFirst(300, TimeUnit.MILLISECONDS)
                        .subscribe(unit -> {
                            View view = LayoutInflater.from(this).inflate(R.layout.dialog_find_url, null);
                            EditText etNewUrl = view.findViewById(R.id.et_new_url);
                            TextView tvNewUrlError = view.findViewById(R.id.tv_new_url_error);
                            Button btnAddNewUrl = view.findViewById(R.id.btn_add_new_url);

                            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                                    .setTitle("Add New Link")
                                    .setView(view);

                            AlertDialog dialog = builder.create();
                            if (!isFinishing()) {
                                dialog.show();
                            }

                            compositeDisposable.add(
                                    editTextHelper.bindEditText(etNewUrl)
                                            .map(urlRegex::checkUrl)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(s -> {
                                                if (s.isValid()) {
                                                    tvNewUrlError.setVisibility(View.GONE);
                                                    btnAddNewUrl.setEnabled(true);
                                                } else {
                                                    tvNewUrlError.setVisibility(View.VISIBLE);
                                                    tvNewUrlError.setText(s.getResult());
                                                    btnAddNewUrl.setEnabled(false);
                                                }
                                            }, throwable -> {

                                            })
                            );

                            compositeDisposable.add(
                                    buttonHelper.bindButton(btnAddNewUrl)
                                            .throttleLast(300, TimeUnit.MILLISECONDS)
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(unit1 -> {
                                                dialog.dismiss();
                                                homeViewModel.findUrl(etNewUrl.getText().toString());
                                            }, throwable -> {

                                            })
                            );
                        }, throwable -> {

                        })
        );

        // Observe Retry Click
        compositeDisposable.add(
                buttonHelper.bindButton(btnRetry)
                        .throttleLast(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(unit -> {
                            tvErrorMessage.setVisibility(View.GONE);
                            btnRetry.setVisibility(View.GONE);
                            pbLoading.setVisibility(View.VISIBLE);

                            homeViewModel.initiateUrlList();
                        }, throwable -> {
                            Log.i("fuck :: ", throwable.getMessage());
                        })
        );

        // Observe Home State
        compositeDisposable.add(
                homeViewModel.urlStateSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(homeState -> {
                            if (homeState instanceof ShowList) {
                                pbLoading.setVisibility(View.GONE);

                                List<UrlModel> newList = new ArrayList<>(((ShowList) homeState).getUrlList());
                                urlAdapter.submitList(newList);
                            } else if (homeState instanceof ShowLoading) {
                                pbLoading.setVisibility(View.VISIBLE);

                                tvErrorMessage.setVisibility(View.GONE);
                                btnRetry.setVisibility(View.GONE);
                            } else if (homeState instanceof UpdateList) {
                                pbLoading.setVisibility(View.GONE);

                                List<UrlModel> newList = new ArrayList<>(((UpdateList) homeState).getUrlList());
                                urlAdapter.submitList(newList);

                                actionMode.setTitle(((UpdateList) homeState).getUrlCount() + "");
                                actionMode.invalidate();
                            } else if (homeState instanceof ShowError) {
                                pbLoading.setVisibility(View.GONE);

                                if (urlAdapter.getItemCount() > 0) {
                                    Toast.makeText(this, ((ShowError) homeState).getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    tvErrorMessage.setVisibility(View.VISIBLE);
                                    btnRetry.setVisibility(View.VISIBLE);
                                    tvErrorMessage.setText(((ShowError) homeState).getErrorMessage());
                                }
                            }
                        }, throwable -> {
                            Log.i("HOMERROR :: ", throwable.getMessage());
                        })
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_default, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            homeViewModel.sort();
        }
        return true;
    }

    @Override
    public void onUrlLongClick(UrlModel urlModel) {
        homeViewModel.selectUrl(urlModel);
        if (actionMode == null) {
            actionMode = startSupportActionMode(this);
        }
    }

    @Override
    public void onUrlClick(UrlModel urlModel) {
        if (actionMode != null) {
            homeViewModel.selectUrl(urlModel);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_home_delete, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            homeViewModel.deleteUrl();
            mode.finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        homeViewModel.clearSelected();
        actionMode = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}