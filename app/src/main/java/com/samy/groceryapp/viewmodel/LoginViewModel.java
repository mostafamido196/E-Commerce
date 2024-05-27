package com.samy.groceryapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samy.groceryapp.utils.Response;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Response> loginLivedata;
    private final CompositeDisposable disposables;
    private final LoginRepository loginRepo;

    public LoginViewModel() {
        loginRepo = new LoginRepository();
        loginLivedata = new MutableLiveData(Response.ideel());
        disposables = new CompositeDisposable();
    }

    public MutableLiveData<Response> getLoginLivedata() {
        return loginLivedata;
    }


    public void login(String email, String password) {
        loginLivedata.setValue(Response.loading());
        disposables.add((Disposable) loginRepo.signInWithEmailAndPassword(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> loginLivedata.setValue(Response.loading()))//when subscribe send data
                .subscribe(authResult -> {
                            loginLivedata.setValue(Response.success(authResult.getUser()));
                        }, throwable -> {
                            loginLivedata.setValue(Response.error(throwable.getMessage()));
                        }
                )
        );



    }


}

class LoginRepository {
    Single<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return Single.create(emitter -> {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(task.getResult());
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

}

