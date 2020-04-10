package com.f0x1d.dogbin.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.f0x1d.dogbin.R;
import com.f0x1d.dogbin.network.model.DocumentLinkResponse;
import com.f0x1d.dogbin.network.okhttp.NetworkUtils;
import com.f0x1d.dogbin.network.retrofit.DogBinApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogBinWritingViewModel extends AndroidViewModel {

    private MutableLiveData<LoadingState> mLoadingStateData = new MutableLiveData<>();
    private MutableLiveData<DocumentLinkResponse> mDocumentResponseData = new MutableLiveData<>();

    public DogBinWritingViewModel(@NonNull Application application) {
        super(application);
    }

    public void publish(String text, String slug) {
        if (text == null || text.isEmpty())
            return;

        mLoadingStateData.setValue(LoadingState.LOADING);

        DogBinApi.getInstance().getService().createDocument(NetworkUtils.getDocumentBody(text, slug)).enqueue(new Callback<DocumentLinkResponse>() {
            @Override
            public void onResponse(Call<DocumentLinkResponse> call, Response<DocumentLinkResponse> response) {
                mLoadingStateData.setValue(LoadingState.LOADED);

                mDocumentResponseData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DocumentLinkResponse> call, Throwable t) {
                processError(t);
            }
        });
    }

    private void processError(Throwable t) {
        t.printStackTrace();

        mLoadingStateData.setValue(LoadingState.LOADED);
        Toast.makeText(getApplication(), getApplication().getString(R.string.error, t.getLocalizedMessage()), Toast.LENGTH_LONG).show();
    }

    public LiveData<LoadingState> getLoadingStateData() {
        return mLoadingStateData;
    }

    public LiveData<DocumentLinkResponse> getDocumentResponseData() {
        return mDocumentResponseData;
    }

    public enum LoadingState {
        LOADING, LOADED
    }
}