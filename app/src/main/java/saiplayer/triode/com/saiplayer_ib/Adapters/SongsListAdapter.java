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
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import saiplayer.triode.com.saiplayer_ib.AdapterAnimation.SongsScrollAnimation;
import saiplayer.triode.com.saiplayer_ib.AsyncTasks.SongAsyncTask;
import saiplayer.triode.com.saiplayer_ib.DataHolder.SongsDataProvider;
import saiplayer.triode.com.saiplayer_ib.R;

/**
 * Created by dell on 08-Aug-17.
 */

public class SongsListAdapter extends ArrayAdapter<SongsDataProvider> implements View.OnClickListener,SectionIndexer{
    ArrayList<SongsDataProvider> songsDataProviders;
    Context context;
    int listID;
    ContentResolver contentResolver;
    HashMap<String,Integer> alphaIndexer;
    String[] sections;

    private OnItemClickType onItemClickType;

    public interface OnItemClickType {
        void onSongItemClick(int itemPostition);
    }

    public void setOnItemClickType(final OnItemClickType onItemClickType){
        this.onItemClickType = onItemClickType;
    }

    public SongsListAdapter(ContentResolver contentResolver,
                            Context context,
                            int listID,
                            ArrayList<SongsDataProvider> songsDataProviders){
        super(context,listID,songsDataProviders);
        this.contentResolver = contentResolver;
        this.songsDataProviders = songsDataProviders;
        this.context = context;
        this.listID = listID;
        alphaIndexer = new HashMap<String, Integer>();
        int size = songsDataProviders.size();
        for (int i = 0;i<size;i++){
            String title = songsDataProviders.get(i).getSongTitle();
            String initials = title.substring(0,1);
            initials = initials.toUpperCase();
            if (!alphaIndexer.containsKey(initials)){
                alphaIndexer.put(initials,i);
            }
            Set<String> sectionLetters = alphaIndexer.keySet();
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);
        }
    }

    @Override
    public int getCount() {
        return songsDataProviders.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
            viewHolder.imageView.setImageResource(R.drawable.blank_song_holder);
            viewHolder.songAsyncTask = (SongAsyncTask) new SongAsyncTask(viewHolder.imageView,contentResolver)
                    .execute(songsDataProviders.get(position).getSongAlbum());
            convertView.setTag(viewHolder);
            viewHolder.item_container_root.setTag(position);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            if(viewHolder.songAsyncTask.getStatus()== AsyncTask.Status.RUNNING)
            {
                viewHolder.songAsyncTask.cancel(true);
            }
        }
        SongsScrollAnimation.animateSongList(convertView);
        viewHolder.imageView.setImageResource(R.drawable.blank_song_holder);
        viewHolder.songTitle.setText(songsDataProviders.get(position).getSongTitle());
        viewHolder.songDetalsText.setText(songsDataProviders.get(position).getSongDetails());
        viewHolder.songAsyncTask = (SongAsyncTask) new SongAsyncTask(viewHolder.imageView,contentResolver)
                .execute(songsDataProviders.get(position).getSongAlbum());
        convertView.setTag(viewHolder);
        viewHolder.item_container_root.setTag(position);
        viewHolder.item_container_root.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder {
        CircleImageView imageView;
        LinearLayout item_container_root;
        TextView songTitle;
        TextView songDetalsText;
        SongAsyncTask songAsyncTask;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_container_root:
                onItemClickType.onSongItemClick((Integer) view.getTag());
                break;
        }
    }

    @Override
    public Object[] getSections() {
        return sections;
    }

    @Override
    public int getPositionForSection(int i) {
        return alphaIndexer.get(sections[i]);
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

}
