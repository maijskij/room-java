package eu.aboutall.room;

import android.content.Context;
import android.support.annotation.NonNull;

import eu.aboutall.room.data.source.Repository;
import eu.aboutall.room.data.source.LocalRepository;
import eu.aboutall.room.utils.schedulers.BaseSchedulerProvider;
import eu.aboutall.room.utils.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {


    public static Repository provideItemsRepository(@NonNull Context context) {
        checkNotNull(context);
        return LocalRepository.getInstance(context).getRepository();
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
