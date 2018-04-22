package saiplayer.triode.com.saiplayer_ib.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

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
    PopupWindow popUpWindow;
    String section_header = null;

    private OnItemClickType onItemClickType;

    public interface OnItemClickType {
        void onSongItemClick(int itemPostition,ArrayList<SongsDataProvider> dataProviders);
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
            viewHolder = new ViewHolder();

            section_header = songsDataProviders.get(position).getSongTitle().substring(0,1).toUpperCase();
            if (position>0 && section_header.equals(songsDataProviders.get(position-1).getSongTitle().substring(0,1).toUpperCase())){
                viewHolder.total_layout = (LinearLayout) convertView.findViewById(R.id.total_single_song_layout);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.total_layout.getLayoutParams();
                layoutParams.height = (int) (80*context.getResources().getDisplayMetrics().density);
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                viewHolder.total_layout.setLayoutParams(layoutParams);
                viewHolder.section_header_view = (TextView) convertView.findViewById(R.id.section_header);
                viewHolder.section_header_view.setVisibility(View.GONE);
            }
            else {
                viewHolder.total_layout = (LinearLayout) convertView.findViewById(R.id.total_single_song_layout);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.total_layout.getLayoutParams();
                layoutParams.height = (int) (100*context.getResources().getDisplayMetrics().density);
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                viewHolder.total_layout.setLayoutParams(layoutParams);
                viewHolder.section_header_view = (TextView) convertView.findViewById(R.id.section_header);
                viewHolder.section_header_view.setVisibility(View.VISIBLE);
                viewHolder.section_header_view.setText(section_header);
            }

            viewHolder.imageView = (CircleImageView) convertView.findViewById(R.id.album_art_of_songs);
            viewHolder.item_container_root = (LinearLayout) convertView.findViewById(R.id.item_container_root);
            viewHolder.songTitle = (TextView) convertView.findViewById(R.id.songs_title_on_songs);
            viewHolder.songDetalsText = (TextView) convertView.findViewById(R.id.songs_detail_on_songs);
            viewHolder.songPopUpMenu = (TextView) convertView.findViewById(R.id.songs_popup_menu);
            viewHolder.songTitle.setSelected(true);
            viewHolder.songDetalsText.setSelected(true);
            viewHolder.imageView.setImageResource(R.drawable.artist_card_shape);
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

        //SongsScrollAnimation.animateSongList(convertView);
        section_header = songsDataProviders.get(position).getSongTitle().substring(0,1).toUpperCase();
        if (position>0 && section_header.equals(songsDataProviders.get(position-1).getSongTitle().substring(0,1).toUpperCase())){
            viewHolder.total_layout = (LinearLayout) convertView.findViewById(R.id.total_single_song_layout);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.total_layout.getLayoutParams();
            layoutParams.height = (int) (80*context.getResources().getDisplayMetrics().density);
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewHolder.total_layout.setLayoutParams(layoutParams);
            viewHolder.section_header_view = (TextView) convertView.findViewById(R.id.section_header);
            viewHolder.section_header_view.setVisibility(View.GONE);
        }
        else {
            viewHolder.total_layout = (LinearLayout) convertView.findViewById(R.id.total_single_song_layout);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.total_layout.getLayoutParams();
            layoutParams.height = (int) (100*context.getResources().getDisplayMetrics().density);
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            viewHolder.total_layout.setLayoutParams(layoutParams);
            viewHolder.section_header_view = (TextView) convertView.findViewById(R.id.section_header);
            viewHolder.section_header_view.setVisibility(View.VISIBLE);
            viewHolder.section_header_view.setText(section_header);
        }
        viewHolder.imageView.setImageResource(R.drawable.artist_card_shape);
        viewHolder.songTitle.setText(songsDataProviders.get(position).getSongTitle());
        viewHolder.songDetalsText.setText(songsDataProviders.get(position).getSongDetails());
        viewHolder.songAsyncTask = (SongAsyncTask) new SongAsyncTask(viewHolder.imageView,contentResolver)
                .execute(songsDataProviders.get(position).getSongAlbum());
        convertView.setTag(viewHolder);
        viewHolder.item_container_root.setTag(position);
        viewHolder.item_container_root.setOnClickListener(this);
        viewHolder.songPopUpMenu.setOnClickListener(this);
        return convertView;
    }

    static class ViewHolder {
        LinearLayout total_layout;
        TextView section_header_view;
        CircleImageView imageView;
        LinearLayout item_container_root;
        TextView songTitle;
        TextView songDetalsText;
        TextView songPopUpMenu;
        SongAsyncTask songAsyncTask;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_container_root:
                if (popUpWindow != null){
                    popUpWindow.dismiss();
                    popUpWindow = null;
                }
                else {
                    onItemClickType.onSongItemClick((Integer) view.getTag(),songsDataProviders);
                }
                break;
            case R.id.songs_popup_menu:
                if (popUpWindow == null){
                    showPopup(view);
                }
                else {
                    popUpWindow.dismiss();
                    popUpWindow = null;
                }
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

    public void showPopup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.song_item_popup_menu_layout, null);

        popUpWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popUpWindow.showAsDropDown(view,Math.round(view.getX()),Math.round(view.getY()));
        popUpWindow.setFocusable(true);
    }

}
