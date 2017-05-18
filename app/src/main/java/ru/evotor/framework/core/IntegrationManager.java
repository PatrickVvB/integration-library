package ru.evotor.framework.core;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by a.kuznetsov on 18/04/2017.
 */

public interface IntegrationManager {
    String KEY_INTENT_DATA = "intentData";
    String KEY_INTEGRATION_RESPONSE = "integrationResponse";
    String KEY_SOURCE_DATA = "sourceData";
    String KEY_INTENT = "intent";
    String KEY_SKIP = "skip";
    String KEY_DATA = "data";
    String KEY_ERROR_CODE = "errorCode";
    String KEY_ERROR_MESSAGE = "errorMessage";

    public IntegrationManagerFuture call(
            String action,
            ComponentName componentName,
            Bundle data,
            ICanStartActivity activity,
            IntegrationManagerCallback callback,
            Handler handler);

}