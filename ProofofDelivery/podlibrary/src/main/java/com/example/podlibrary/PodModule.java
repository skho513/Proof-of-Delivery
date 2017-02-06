package com.example.podlibrary;

import com.google.gson.Gson;
import com.lalamove.base.auth.IAuthProvider;
import com.lalamove.base.provider.module.DataModule;
import com.lalamove.base.provider.module.NetworkModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Provides api dependencies.
 *
 * @author milan
 */
@Module
public class PodModule {
    private IAuthProvider authProvider;
    private Gson gson;

    public PodModule(IAuthProvider authProvider, Gson gson) {
        this.authProvider = authProvider;
        this.gson = gson;
    }

    @Singleton
    @Provides
    public PodApi providesPODAPI(@Named(NetworkModule.ADAPTER_API) Retrofit adapter) {
        return adapter.create(PodApi.class);
    }

    @Singleton
    @Provides
    public SignatureContract.ISignaturePresenter provideSignaturePresenter(PodApi podApi) {
        return new SignaturePresenter(podApi);
    }

    @Singleton
    @Provides
    public IAuthProvider provideAuthProvider() {
        return authProvider;
    }

    @Singleton
    @Provides
    @Named(DataModule.GSON_APP)
    public Gson provideGson() {
        return gson;
    }
}
