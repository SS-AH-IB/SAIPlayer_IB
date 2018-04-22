package saiplayer.triode.com.saiplayer_ib.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import saiplayer.triode.com.saiplayer_ib.Adapters.ArtistsGridAdapter;
import saiplayer.triode.com.saiplayer_ib.DataHolder.ArtistSearch;
import saiplayer.triode.com.saiplayer_ib.DataHolder.ArtistsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 05-Aug-17.
 */

public class ArtistsFragment extends Fragment implements ArtistsGridAdapter.OnArtistClickType {
    ArrayList<ArtistsDataProvider> artistsDataProviders = new ArrayList<ArtistsDataProvider>();
    GridView artistGrid;

    OnArtistPressedListener onArtistPressedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.artist_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artistGrid = (GridView) view.findViewById(R.id.list_artist_items);
        artistsDataProviders = new ArtistSearch().getArtistDetails(getActivity().getContentResolver());
        ArtistsGridAdapter artistsGridAdapter = new ArtistsGridAdapter(getActivity().getContentResolver(),
                getContext(),
                R.layout.single_artist_layout,
                artistsDataProviders);
        artistGrid.setAdapter(artistsGridAdapter);
        artistGrid.setFastScrollEnabled(true);
        artistsGridAdapter.setOnArtistClickType(this);
        //OnClickDeclare
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            onArtistPressedListener = (OnArtistPressedListener) activity;
        }
        catch (Exception e){
          e.printStackTrace();
        };
    }

    @Override
    public void onArtistItemClick(String artistName, ArrayList<String> bitmapContainer) {
        onArtistPressedListener.onArtistPressed(artistName, bitmapContainer);
    }

    public interface OnArtistPressedListener{
        void onArtistPressed(String artistName, ArrayList<String> bitmapContainer);
    }

}
