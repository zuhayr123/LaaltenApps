package com.laaltentech.abou.laalten;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ListViewItemCheckbox extends BaseAdapter {

    private List<ListViewItem> listViewItemDtoList = null;

    private Context ctx = null;

    public ListViewItemCheckbox(Context ctx, List<ListViewItem> listViewItemDtoList) {
        this.ctx = ctx;
        this.listViewItemDtoList = listViewItemDtoList;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(listViewItemDtoList!=null)
        {
            ret = listViewItemDtoList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int itemIndex) {
        Object ret = null;
        if(listViewItemDtoList!=null) {
            ret = listViewItemDtoList.get(itemIndex);
        }
        return ret;
    }

    @Override
    public long getItemId(int itemIndex) {
        return itemIndex;
    }

    @Override
    public View getView(int itemIndex, View convertView, ViewGroup viewGroup) {

        ItemViewHolder viewHolder = null;

        if(convertView!=null)
        {
            viewHolder = (ItemViewHolder) convertView.getTag();
        }else
        {
            convertView = View.inflate(ctx, R.layout.adapterlayout, null);
            //Nothing
            CheckBox listItemCheckbox = convertView.findViewById(R.id.HC_05_MACs);

            TextView listItemText = convertView.findViewById(R.id.text_for_list);

            viewHolder = new ItemViewHolder(convertView);

            viewHolder.setItemCheckbox(listItemCheckbox);

            viewHolder.setItemTextView(listItemText);

            convertView.setTag(viewHolder);
        }

        ListViewItem listViewItemDto = listViewItemDtoList.get(itemIndex);
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.getItemTextView().setText(listViewItemDto.getItemText());

        return convertView;
    }
}
