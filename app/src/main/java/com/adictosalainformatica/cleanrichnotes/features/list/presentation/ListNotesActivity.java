package com.adictosalainformatica.cleanrichnotes.features.list.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.base.presentation.ViewModelFactory;
import com.adictosalainformatica.cleanrichnotes.features.add.presentation.EditNote;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.perf.metrics.AddTrace;

import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.adictosalainformatica.cleanrichnotes.NotesApplication.getDaggerComponent;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.ADD_NOTE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.DELETE_NOTE_KEY;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.EDIT_NOTE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.NOTE_MODEL;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.RQ_ADD_NOTE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SCREEN_TYPE;
import static com.adictosalainformatica.cleanrichnotes.utils.Constants.SHOW_NOTE;

public class ListNotesActivity extends AppCompatActivity implements NotesAdapter.OnNoteListItemClickedListener, NotesAdapter.OnNoteListItemLongClickedListener {

    @Inject ViewModelFactory viewModelFactory;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    private NotesListViewModel notesListViewModel;
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInjection();
        initRecyclerView();
        initAction();

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditNote.class);
            intent.putExtra(SCREEN_TYPE, ADD_NOTE);
            startActivityForResult(intent, RQ_ADD_NOTE);
            Timber.d("add note clicked");
        });
    }

    private void initInjection() {
        ButterKnife.bind(this);
        getDaggerComponent().inject(this);
        notesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotesListViewModel.class);
    }

    private void initRecyclerView() {
        adapter = new NotesAdapter();
        adapter.setOnNoteListItemClickedListener(this);
        adapter.setOnNoteListItemLongClickedListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null || !newText.isEmpty()) {
                    getFilteredList(newText);
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 20) {
            Note note = Parcels.unwrap(data.getParcelableExtra(NOTE_MODEL));

            if(data.hasExtra(DELETE_NOTE_KEY))  {
                notesListViewModel.delete(note);
            } else {
                notesListViewModel.save(note);
            }
        }
    }

    @AddTrace(name = "getNotesList")
    public void getList(){
        notesListViewModel.getList().observe(this, adapter::submitList);
    }

    public void getFilteredList(String text) {
        notesListViewModel.getFilteredList(text).observe(this, adapter::submitList);
    }

    public void initAction() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                        @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = ((NotesAdapter.NotesViewHolder) viewHolder).getNote();
                notesListViewModel.delete(note);

                Snackbar.make(floatingActionButton, R.string.list_delete_note, Snackbar.LENGTH_LONG)
                        .setAction(R.string.list_undo, view -> notesListViewModel.save(note)).show();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNoteListItemClicked(Note note) {
        Intent intent = new Intent(this, EditNote.class);
        intent.putExtra(NOTE_MODEL, Parcels.wrap(note));
        intent.putExtra(SCREEN_TYPE, SHOW_NOTE);
        startActivityForResult(intent, RQ_ADD_NOTE);

    }

    @Override
    public void onNoteListItemLongClicked(Note note) {
        Intent intent = new Intent(this, EditNote.class);
        intent.putExtra(NOTE_MODEL, Parcels.wrap(note));
        intent.putExtra(SCREEN_TYPE, EDIT_NOTE);
        startActivityForResult(intent, RQ_ADD_NOTE);
    }
}
