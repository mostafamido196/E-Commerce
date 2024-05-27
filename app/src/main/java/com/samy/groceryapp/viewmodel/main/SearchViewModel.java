package com.samy.groceryapp.viewmodel.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.model.UserModel;
import com.samy.groceryapp.model.ViewAllModel;
import com.samy.groceryapp.utils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    //for viewModel
    private MutableLiveData<Response> searchListLivedata;
    private final CompositeDisposable disposables;
    private final SearchRepository searchRepository;

    //for ui
    public MutableLiveData<String> _searchKeyET = new MutableLiveData();
    public MutableLiveData<Boolean> _showSearchRV = new MutableLiveData(false);

    public MutableLiveData<String> getSearchKeyET() {
        return _searchKeyET;
    }

    public SearchViewModel() {
        searchListLivedata = new MutableLiveData(Response.ideel());
        disposables = new CompositeDisposable();
        searchRepository = new SearchRepository();
    }

    public MutableLiveData<Response> getSearchListLivedata() {
        return searchListLivedata;
    }

    public void search(String searchKey) {
        if (searchKey.isEmpty()){
            _showSearchRV.setValue(false);
            return;
        }
        searchListLivedata.setValue(Response.loading());
        disposables.add((Disposable) searchRepository.search(searchKey).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(__ -> searchListLivedata.setValue(Response.loading()))//when subscribe send data
                .subscribe(viewAllModel -> {
                    _showSearchRV.setValue(true);
                    searchListLivedata.setValue(Response.success(Objects.requireNonNull(viewAllModel)));
                }, throwable -> {
                    searchListLivedata.setValue(Response.error(Objects.requireNonNull(throwable.getMessage())));
                }));

    }

    public void onDeleteKeyPressed() {
        _showSearchRV.setValue(false);
    }
}

class SearchRepository {
    Single<List<ViewAllModel>> search(String searchKey) {
        return Single.create(emitter -> {
            if (!searchKey.isEmpty()) {
                FirebaseFirestore.getInstance().collection("AllProducts").whereEqualTo("type", searchKey).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<ViewAllModel> list = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ViewAllModel model = doc.toObject(ViewAllModel.class);
                                list.add(model);
                            }
                            emitter.onSuccess(list);
                        } else {
                            emitter.onError(task.getException());
                            ;
                        }
                    }

                });
            }
        });
    }
}
