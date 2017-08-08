package saiplayer.triode.com.saiplayer_ib.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 05-Aug-17.
 */

public class PlaylistsFragment extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist_fragment,container,false);
        return view;
    }
}
