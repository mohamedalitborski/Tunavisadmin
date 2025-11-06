package com.tunavis.establishment;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialiser les plugins Capacitor
        this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
            add(com.getcapacitor.plugin.PushNotifications.class);
            add(com.getcapacitor.plugin.App.class);
        }});
    }
}

