package com.winphyoethu.twoappstudiotest.features.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.SortedList;

import com.winphyoethu.twoappstudiotest.data.ApiService;
import com.winphyoethu.twoappstudiotest.data.model.UrlModel;
import com.winphyoethu.twoappstudiotest.features.home.homestate.HomeState;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowError;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowList;
import com.winphyoethu.twoappstudiotest.features.home.homestate.ShowLoading;
import com.winphyoethu.twoappstudiotest.features.home.homestate.UpdateList;
import com.winphyoethu.twoappstudiotest.util.UrlMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.HttpException;

public class HomeViewModel extends ViewModel {

    public HomeViewModel(ApiService apiService) {
        this.apiService = apiService;
    }

    private final ApiService apiService;
    private final UrlMapper urlMapper = new UrlMapper();

    PublishSubject<HomeState> urlStateSubject = PublishSubject.create();

    private final List<String> defaultUrlList = List.of("https://www.channelnewsasia.com", "https://sg.yahoo.com", "https://www.google.com", "https://www.youtube.com", "https://www.facebook.com", "https://www.espn.com", "https://www.twitch.tv", "https://www.sofascore.com", "https://www.flashscore.com", "https://www.channelnewsasia.com", "https://sg.yahoo.com", "https://www.google.com", "https://www.youtube.com", "https://www.facebook.com", "https://www.espn.com", "https://www.twitch.tv", "https://www.sofascore.com", "https://www.flashscore.com", "https://www.channelnewsasia.com", "https://sg.yahoo.com", "https://www.google.com", "https://www.youtube.com", "https://www.facebook.com", "https://www.espn.com", "https://www.twitch.tv", "https://www.sofascore.com", "https://www.flashscore.com");
    private List<UrlModel> urlList = new ArrayList();
    private boolean isAscend = true;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void initiateUrlList() {
        if (urlList.isEmpty()) {
            compositeDisposable.add(
                    Observable.fromIterable(defaultUrlList)
                            .flatMap(s -> apiService.findUrl(s))
                            .map(responseBodyResponse -> {
                                urlList.add(urlMapper.getWebsiteUrlFromString(responseBodyResponse));
                                Collections.sort(urlList, (o1, o2) -> o1.getUrl().compareTo(o2.getUrl()));
                                return urlList;
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(urlModels -> {
                                urlStateSubject.onNext(new ShowList(urlModels));
                            }, throwable -> {
                                String errorMessage = "Unable to access";
                                if (throwable instanceof HttpException) {
                                    switch (((HttpException) throwable).code()) {
                                        case 404:
                                            errorMessage = "Not Found";
                                        case 500:
                                            errorMessage = "Server Error";
                                        default:
                                            errorMessage = "Unable to access";
                                    }
                                }
                                urlStateSubject.onNext(new ShowError(errorMessage));
                                Log.i("error :: ", throwable.getMessage());
                            })
            );
        } else {
            compositeDisposable.add(
                    Single.just(urlList)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(urlModels -> {
                                urlStateSubject.onNext(new ShowList(urlModels));
                            }, throwable -> {
                                Log.i("error :: ", throwable.getMessage());
                            })
            );
        }
    }

    public void findUrl(String urlQuery) {
        urlStateSubject.onNext(new ShowLoading());

        compositeDisposable.add(
                apiService.findUrl(urlQuery)
                        .map(responseBodyResponse -> {
                            urlList.add(urlMapper.getWebsiteUrlFromString(responseBodyResponse));
                            Collections.sort(urlList, (o1, o2) -> o1.getUrl().compareTo(o2.getUrl()));
                            return urlList;
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(urlModels -> {
                            urlStateSubject.onNext(new ShowList(urlModels));
                        }, throwable -> {
                            String errorMessage = "Unable to access";
                            if (throwable instanceof HttpException) {
                                switch (((HttpException) throwable).code()) {
                                    case 404:
                                        errorMessage = "Not Found";
                                    case 500:
                                        errorMessage = "Server Error";
                                    default:
                                        errorMessage = "Unable to access";
                                }
                            }
                            urlStateSubject.onNext(new ShowError(errorMessage));
                            Log.i("error :: ", throwable.getMessage());
                        })
        );
    }

    public void selectUrl(UrlModel urlModel) {
        int index = urlList.indexOf(urlModel);
        UrlModel updatingUrl = new UrlModel(urlModel.getUrlName(), urlModel.getUrl(), urlModel.getUrlImage(), !urlModel.isSelected());
        urlList.set(index, updatingUrl);

        List<UrlModel> selectedList = new ArrayList<>();

        for (UrlModel urlModel1 : urlList) {
            Log.i("URLMODEL :: ", urlModel1.getUrlName() + ":: " + urlModel1.getUrl() + " :: " + urlModel1.getUrlImage() + " :: " + urlModel1.isSelected());
            if (urlModel1.isSelected()) {
                selectedList.add(urlModel1);
            }
        }

        urlStateSubject.onNext(new UpdateList(urlList, selectedList.size()));
    }

    public void sort() {
        compositeDisposable.add(
                Observable.just(isAscend)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .map(aBoolean -> {
                            if (aBoolean) {
                                Collections.sort(urlList, (o1, o2) -> o2.getUrl().compareTo(o1.getUrl()));
                            } else {
                                Collections.sort(urlList, (o1, o2) -> o1.getUrl().compareTo(o2.getUrl()));
                            }
                            return urlList;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(urlModels -> {
                            isAscend = !isAscend;

                            urlStateSubject.onNext(new ShowList(urlList));
                        }, throwable -> {
                            Log.i("FUCKSHIT :: ", throwable.getMessage() + " DICK");
                        })
        );
    }

    public void deleteUrl() {
        List<UrlModel> urlListCopy = new ArrayList<>(urlList);

        for (UrlModel urlModel : urlListCopy) {
            if (urlModel.isSelected()) {
                urlList.remove(urlModel);
            }
        }

        urlStateSubject.onNext(new ShowList(urlList));
    }

    public void clearSelected() {
        for (UrlModel urlModel : urlList) {
            if (urlModel.isSelected()) {
                int index = urlList.indexOf(urlModel);
                UrlModel newUrlModel = new UrlModel(urlModel.getUrlName(), urlModel.getUrl(), urlModel.getUrlImage(), !urlModel.isSelected());
                urlList.set(index, newUrlModel);
            }
        }

        urlStateSubject.onNext(new ShowList(urlList));
    }

}
