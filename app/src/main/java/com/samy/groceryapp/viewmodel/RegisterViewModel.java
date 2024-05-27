package com.samy.groceryapp.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.samy.groceryapp.model.UserModel;
import com.samy.groceryapp.utils.Response;

import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {
    //for viewModel
    private MutableLiveData registerLivedata;
    private final CompositeDisposable disposables;
    private final RegisterRepository registerRepo;
    //for ui
    public MutableLiveData<String> username = new MutableLiveData();
    public MutableLiveData<String> password = new MutableLiveData();
    public MutableLiveData<String> email = new MutableLiveData();

    private MutableLiveData _signUpResult = new MutableLiveData<String>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData(false);


    private MutableLiveData _signInClick = new MutableLiveData(false);


    //toHandle Glide
//    private MutableLiveData<String> imageUrl = new MutableLiveData<>("regbg");

    public RegisterViewModel() {
        //for viewModel
        registerRepo = new RegisterRepository();
        registerLivedata = new MutableLiveData(Response.ideel());
        disposables = new CompositeDisposable();
    }

//    public MutableLiveData<String> getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl.setValue(imageUrl);
//    }
//
    public MutableLiveData get_signInClick() {
        return _signInClick;
    }
    public void signInClick(){
        _signInClick.setValue(true);
    }

    public MutableLiveData<Response> getRegisterLivedata() {
        return registerLivedata;
    }

    public MutableLiveData get_signUpResult() {
        return _signUpResult;
    }

    public void register(String userName, String email, String password) {
        registerLivedata.setValue(Response.loading());
        disposables.add((Disposable) registerRepo.createUser(userName, email, password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(__ -> registerLivedata.setValue(Response.loading()))//when subscribe send data
                .subscribe(authResult -> {
                    registerLivedata.setValue(Response.success(Objects.requireNonNull(authResult.getUser())));
                    isLoading.setValue(false);
                }, throwable -> {
                    registerLivedata.setValue(Response.error(Objects.requireNonNull(throwable.getMessage())));
                    isLoading.setValue(false);
                }));


    }

    public void onRegisterClicked() {
        isLoading.setValue(true);
        String userName = username.getValue().toString();
        String userEmail = email.getValue().toString();
        String userPassword = password.getValue().toString();
        if (TextUtils.isEmpty(userName)) {
            _signUpResult.setValue("Name is Empty");
            return;
        }
        if (TextUtils.isEmpty(userEmail)) {
            _signUpResult.setValue("Email is Empty");
            return;
        }
        if (TextUtils.isEmpty(userPassword)) {
            _signUpResult.setValue("Password is Empty");
            return;
        }
        if (userPassword.length() < 6) {
            _signUpResult.setValue("Password Length must be greater then 6 letter");
            return;
        }
        register(userName, userEmail, userPassword);
    }


}

class RegisterRepository {
    Single<AuthResult> createUser(String userName, String email, String pass) {
        return Single.create(emitter -> {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onSuccess(task.getResult());
                    realTimeFB(task.getResult().getUser().getUid(), new UserModel(userName, email, pass));
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }

    Single<AuthResult> realTimeFB(String id, UserModel userModel) {
        return Single.create(emitter -> {
            FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(userModel).addOnSuccessListener(unused -> Log.d("mos samy", "realtimeDB: addOnSuccessListener")).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("mos samy", "realtimeDB: onFailure: e: " + e.getMessage());
                }

            });
        });
    }

}