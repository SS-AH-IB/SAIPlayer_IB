package saiplayer.triode.com.saiplayer_ib.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import saiplayer.triode.com.saiplayer_ib.Adapters.SongsListAdapter;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongSearch;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 05-Aug-17.
 */

public class SongsFragment extends Fragment {
    ArrayList<SongsDataProvider> songsDataProviderArrayList = new ArrayList<SongsDataProvider>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.song_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView songList = (ListView) view.findViewById(R.id.list_song_items);
        songsDataProviderArrayList = new SongSearch().getMusic(getActivity().getContentResolver());
        SongsListAdapter songsListAdapter = new SongsListAdapter(getActivity().getContentResolver(),getContext(),
                R.layout.single_song_layout,
                songsDataProviderArrayList);
        songList.setAdapter(songsListAdapter);

    }
}
