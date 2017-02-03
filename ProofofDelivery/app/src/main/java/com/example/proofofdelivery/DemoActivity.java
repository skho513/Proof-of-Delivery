package com.example.proofofdelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.podlibrary.DaggerPodComponent;
import com.example.podlibrary.Order;
import com.example.podlibrary.PodComponent;
import com.example.podlibrary.PodInjector;
import com.example.podlibrary.PodModule;
import com.example.podlibrary.SignatureActivity;
import com.example.podlibrary.SignaturePresenter;
import com.google.gson.GsonBuilder;
import com.lalamove.base.config.Configuration;
import com.lalamove.base.provider.module.AppModule;
import com.lalamove.base.provider.module.ConfigModule;

public class DemoActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_signature_page);
    }

    public void requestPOD(View view) {
        initPodComponent();
        startActivity(new Intent(this, SignatureActivity.class)
                .putExtra(SignaturePresenter.KEY_ORDER, new Order("Bob")));
    }

    /**
     * Initializes a {@link PodComponent}
     */
    private void initPodComponent() {
        PodInjector.setComponent(DaggerPodComponent.builder().appModule(new AppModule(this))
                .podModule(getPODModule())
                .configModule(ConfigModule.getDebugConfiguration(Configuration.Flavor.USER))
                .build());
    }

    /**
     * Creates a {@link PodModule} with a custom dependencies
     *
     * @return a {@link PodModule}
     */
    private PodModule getPODModule() {
        return new PodModule(new MockAuthProvider(), new GsonBuilder().create());
    }
}
