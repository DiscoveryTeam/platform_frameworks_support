package foo;

import static com.android.support.lifecycle.Lifecycle.ON_STOP;

import com.android.support.lifecycle.LifecycleOwner;
import com.android.support.lifecycle.OnLifecycleEvent;

public class TooManyArgs {
    @OnLifecycleEvent(ON_STOP)
    public void onStop(LifecycleOwner provider, int lastEvent, int x) {
    }
}