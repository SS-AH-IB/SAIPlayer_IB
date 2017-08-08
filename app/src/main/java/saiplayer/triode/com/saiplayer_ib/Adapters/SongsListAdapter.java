package saiplayer.triode.com.saiplayer_ib.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import saiplayer.triode.com.saiplayer_ib.AsyncTasks.SongAsyncTask;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 08-Aug-17.
 */

public class SongsListAdapter extends ArrayAdapter<SongsDataProvider>{
    private ArrayList<SongsDataProvider> songsDataProviders;
    private Context context;
    private int listID;
    ContentResolver contentResolver;

    public SongsListAdapter(ContentResolver contentResolver,
                            Context context,
                            int listID,
                            ArrayList<SongsDataProvider> songsDataProviders){
        super(context,listID,songsDataProviders);
        this.contentResolver = contentResolver;
        this.songsDataProviders = songsDataProviders;
        this.context = context;
        this.listID = listID;
    }

    @Override
    public int getCount() {
        return songsDataProviders.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(listID, null);
            viewHolder=new ViewHolder();
            viewHolder.imageView = (CircleImageView) convertView.findViewById(R.id.album_art_of_songs);
            viewHolder.item_container_root = (LinearLayout) convertView.findViewById(R.id.item_container_root);
            viewHolder.songTitle = (TextView) convertView.findViewById(R.id.songs_title_on_songs);
            viewHolder.songDetalsText = (TextView) convertView.findViewById(R.id.songs_detail_on_songs);
            viewHolder.songTitle.setSelected(true);
            viewHolder.songDetalsText.setSelected(true);
            viewHolder.songAsyncTask = (SongAsyncTask) new SongAsyncTask(viewHolder.imageView,contentResolver)
                    .execute(songsDataProviders.get(position).getSongAlbum());
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
            if(viewHolder.songAsyncTask.getStatus()== AsyncTask.Status.RUNNING)
            {
                viewHolder.songAsyncTask.cancel(true);
            }
        }

        viewHolder.songTitle.setText(songsDataProviders.get(position).getSongTitle());
        viewHolder.songDetalsText.setText(songsDataProviders.get(position).getSongDetails());
        viewHolder.imageView.setVisibility(View.INVISIBLE);
        viewHolder.songAsyncTask = (SongAsyncTask) new SongAsyncTask(viewHolder.imageView,contentResolver)
                .execute(songsDataProviders.get(position).getSongAlbum());
        convertView.setTag(viewHolder);
        return convertView;
    }

    static class ViewHolder{
        CircleImageView imageView;
        LinearLayout item_container_root;
        TextView songTitle;
        TextView songDetalsText;
        SongAsyncTask songAsyncTask;
    }
}
