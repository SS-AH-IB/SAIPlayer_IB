package saiplayer.triode.com.saiplayer_ib.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


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
    ListView songList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.song_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songList = (ListView) view.findViewById(R.id.list_song_items);
        songsDataProviderArrayList = new SongSearch().getMusic(getActivity().getContentResolver());
        SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),getContext(),
                R.layout.single_song_layout,
                songsDataProviderArrayList);
        songList.setAdapter(songsListAdapter);
        songList.setFastScrollEnabled(true);
        songsListAdapter.setOnItemClickType(this);

    }

    @Override
    public void onSongItemClick(int itemPostition) {
        Toast.makeText(getActivity(),songsDataProviderArrayList.get(itemPostition).getSongTitle(), Toast.LENGTH_SHORT).show();
        onSongPressedListener.onSongPressed(itemPostition,songsDataProviderArrayList);
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

    public interface OnSongPressedListener {
        void onSongPressed(int songPosition,ArrayList<SongsDataProvider> arrayList);
    }

}
