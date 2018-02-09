/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.arch.background.workmanager.impl.utils;

import android.arch.background.workmanager.State;
import android.arch.background.workmanager.impl.WorkDatabase;
import android.arch.background.workmanager.impl.WorkManagerImpl;
import android.arch.background.workmanager.impl.logger.Logger;
import android.arch.background.workmanager.impl.model.WorkSpecDao;
import android.support.annotation.RestrictTo;

/**
 * A {@link Runnable} that can stop work and set the {@link State} to {@link State#ENQUEUED} if it's
 * in {@link State#RUNNING}.
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class StopWorkRunnable implements Runnable {

    private static final String TAG = "StopWorkRunnable";

    private WorkManagerImpl mWorkManagerImpl;
    private String mWorkSpecId;

    public StopWorkRunnable(WorkManagerImpl workManagerImpl, String workSpecId) {
        mWorkManagerImpl = workManagerImpl;
        mWorkSpecId = workSpecId;
    }

    @Override
    public void run() {
        WorkDatabase workDatabase = mWorkManagerImpl.getWorkDatabase();
        WorkSpecDao workSpecDao = workDatabase.workSpecDao();
        workDatabase.beginTransaction();
        try {
            if (workSpecDao.getState(mWorkSpecId) == State.RUNNING) {
                workSpecDao.setState(State.ENQUEUED, mWorkSpecId);
            }
            boolean isStopped = mWorkManagerImpl.getProcessor().stopWork(mWorkSpecId, true);
            Logger.debug(TAG, "onStopJob for %s; Processor.stopWork = %s", mWorkSpecId, isStopped);
            workDatabase.setTransactionSuccessful();
        } finally {
            workDatabase.endTransaction();
        }
    }
}
