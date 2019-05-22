package com.adictosalainformatica.cleanrichnotes.base.dagger;

import androidx.lifecycle.ViewModel;

import com.adictosalainformatica.cleanrichnotes.base.presentation.ViewModelFactory;
import com.adictosalainformatica.cleanrichnotes.features.list.data.DataRepository;
import com.adictosalainformatica.cleanrichnotes.features.list.presentation.NotesListViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;

import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class ViewModelModule {

    @MapKey
    @Documented
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }

    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
        return new ViewModelFactory(providerMap);
    }

    @Provides
    @IntoMap
    @ViewModelModule.ViewModelKey(NotesListViewModel.class)
    ViewModel notesViewModel(DataRepository dataRepository) {
        return new NotesListViewModel(dataRepository);
    }

}
