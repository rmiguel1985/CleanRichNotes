package com.adictosalainformatica.cleanrichnotes.features.list.presentation;

import android.os.Bundle;
import android.util.Log;
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
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.adictosalainformatica.cleanrichnotes.NotesApplication.getDaggerComponent;

public class ListNotesActivity extends AppCompatActivity {

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
            /*Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);*/
            Log.d("note", "add note clicked");
        });
    }

    private void initInjection() {
        ButterKnife.bind(this);
        getDaggerComponent().inject(this);
        notesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotesListViewModel.class);
    }

    private void initRecyclerView() {
        adapter = new NotesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

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

}
