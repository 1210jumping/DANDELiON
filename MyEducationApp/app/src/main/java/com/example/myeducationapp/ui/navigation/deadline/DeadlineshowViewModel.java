package com.example.myeducationapp.ui.navigation.deadline;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class DeadlineshowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DeadlineshowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Calender");
    }

    public LiveData<String> getText() {

        return mText;
    }
}