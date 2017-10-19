package com.droidcontalk.aliceyuan.droidcontalk.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;

import com.droidcontalk.aliceyuan.droidcontalk.MyProfileFragment;
import com.droidcontalk.aliceyuan.droidcontalk.PinnerFragment;
import com.droidcontalk.aliceyuan.droidcontalk.R;

public class HomeActivity extends AppCompatActivity {
    @Nullable SearchView _searchView;
    @Nullable private MenuItem _searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, myProfileFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        _searchMenuItem = menu.findItem(R.id.search);
        _searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        _searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        _searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(@NonNull View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        _searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideKeyboard(getCurrentFocus());
                PinnerFragment pinnerFragment = PinnerFragment.newInstance(query);
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, pinnerFragment)
                        .addToBackStack(query)
                        .commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void hideKeyboard(@NonNull final View view) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        MenuItemCompat.collapseActionView(_searchMenuItem);
    }
}
