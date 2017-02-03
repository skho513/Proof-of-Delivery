package com.example.podlibrary;

import com.lalamove.base.provider.module.AppModule;
import com.lalamove.base.provider.module.BusModule;
import com.lalamove.base.provider.module.ConfigModule;
import com.lalamove.base.provider.module.DataModule;
import com.lalamove.base.provider.module.LogModule;
import com.lalamove.base.provider.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component for binding pod dependencies
 *
 * @author milan
 */
@Singleton
@Component(modules = {AppModule.class, ConfigModule.class, NetworkModule.class, DataModule.class,
        BusModule.class, LogModule.class, PodModule.class})
public interface PodComponent {
    void inject(SignatureActivity activity);
}
