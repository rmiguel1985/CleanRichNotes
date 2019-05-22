package com.adictosalainformatica.cleanrichnotes.features.list.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.adictosalainformatica.cleanrichnotes.features.list.data.DataRepository;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;

public class NotesListViewModel extends ViewModel {
    public DataRepository mRepository;
    public static int PAGE_SIZE = 20;
    public static boolean PLACEHOLDERS = true;

    public NotesListViewModel(DataRepository repository) {
        mRepository = repository;
    }

    public LiveData<PagedList<Note>> getList() {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(PLACEHOLDERS)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .build();
        return new LivePagedListBuilder<>(mRepository.getNotes(), pagedListConfig).build();
    }

    public void save(Note note) {
        mRepository.setNote(note);
    }

    public void delete(Note note) {
        mRepository.delete(note);
    }

    public LiveData<PagedList<Note>> getFilteredList(String text) {
        PagedList.Config pagedListConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(PLACEHOLDERS)
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .build();

        text = "%" + text + "%";
        return new LivePagedListBuilder<>(mRepository.getFilteredNotes(text), pagedListConfig).build();
    }
}
