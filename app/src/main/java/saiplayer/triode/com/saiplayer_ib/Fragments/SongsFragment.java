package saiplayer.triode.com.saiplayer_ib.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import saiplayer.triode.com.saiplayer_ib.Adapters.SongsListAdapter;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongSearch;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 05-Aug-17.
 */

public class SongsFragment extends Fragment implements SongsListAdapter.OnItemClickType{
    ArrayList<SongsDataProvider> songsDataProviderArrayList = new ArrayList<SongsDataProvider>();

    OnSongPressedListener onSongPressedListener;
    MaterialSearchView searchView;
    ListView songList;
    MenuItem search_menu_item = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         return inflater.inflate(R.layout.song_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.song_search_tool);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");

        songList = (ListView) view.findViewById(R.id.list_song_items);
        songsDataProviderArrayList = new SongSearch().getMusic(getActivity().getContentResolver());
        SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),
                getContext(),
                R.layout.single_song_layout,
                songsDataProviderArrayList);
        songList.setAdapter(songsListAdapter);
        songList.setFastScrollEnabled(true);
        songsListAdapter.setOnItemClickType(this);

        searchView = (MaterialSearchView) view.findViewById(R.id.song_search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                songList.setY(56*getActivity().getResources().getDisplayMetrics().density);
                search_menu_item.setVisible(false);
            }

            @Override
            public void onSearchViewClosed() {
                songList.setY(0);
                SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),
                        getContext(),
                        R.layout.single_song_layout,
                        songsDataProviderArrayList);
                songList.setAdapter(songsListAdapter);
                songList.setFastScrollEnabled(true);
                songsListAdapter.setOnItemClickType(SongsFragment.this);
                search_menu_item.setVisible(true);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<SongsDataProvider> queryArrayList = new ArrayList<SongsDataProvider>();
                if (newText != null && !newText.isEmpty()){
                    for (int i = 0;i < songsDataProviderArrayList.size();i++){
                        if (songsDataProviderArrayList.get(i).getSongTitle().contains(newText) ||
                                songsDataProviderArrayList.get(i).getSongTitle().toLowerCase().contains(newText)){
                            queryArrayList.add(songsDataProviderArrayList.get(i));
                        }
                    }
                    SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),
                            getContext(),
                            R.layout.single_song_layout,
                            queryArrayList);
                    songList.setAdapter(songsListAdapter);
                    songList.setFastScrollEnabled(true);
                    songsListAdapter.setOnItemClickType(SongsFragment.this);
                }
                else {
                    SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),
                            getContext(),
                            R.layout.single_song_layout,
                            songsDataProviderArrayList);
                    songList.setAdapter(songsListAdapter);
                    songList.setFastScrollEnabled(true);
                    songsListAdapter.setOnItemClickType(SongsFragment.this);
                }
                return true;
            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onSongPressedListener = (OnSongPressedListener) activity;
        }
        catch (Exception exception){
            exception.printStackTrace();
        };

    }

    @Override
    public void onSongItemClick(int itemPostition, ArrayList<SongsDataProvider> dataProviders) {
        Toast.makeText(getActivity(),dataProviders.get(itemPostition).getSongTitle(), Toast.LENGTH_SHORT).show();
        onSongPressedListener.onSongPressed(itemPostition,dataProviders);
    }

    public interface OnSongPressedListener {
        void onSongPressed(int songPosition,ArrayList<SongsDataProvider> arrayList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu_icon,menu);
        MenuItem menuItem = menu.findItem(R.id.song_search_symbol);
        search_menu_item = menuItem;
        searchView.setMenuItem(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
