package xyz.codedream.lib.form.window;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import xyz.codedream.lib.form.R;

/**
 * @author skg
 */
public class DialogSpinnerSelect extends Dialog {

    public DialogSpinnerSelect(Context context, int theme) {
        super(context, theme);
        init();
    }

    public DialogSpinnerSelect(Context context) {
        this(context, R.style.dialog_form_spinner);
    }

    @SuppressLint("InflateParams")
    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_form_spinner, null, false);
        setContentView(view);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        //
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(onItemClickListener);
        setCancelable(true);
    }

    private List<Object> dataList;
    private ListView mListView;

    public void setData(List<Object> data) {
        dataList = data;
        adapter.setData(dataList);
    }

    public Object getDataByPosition(int position) {
        if (dataList == null || position < 0 || position >= dataList.size()) {
            return null;
        }
        return dataList.get(position);
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapter, View item, int position, long id) {
            if (mSelectListener != null) {
                mSelectListener.onSelected(position);
                dismiss();
            }
        }
    };

    public interface SelectListener {
        public void onSelected(int position);
    }

    private SelectListener mSelectListener;

    public void setSelectListener(SelectListener l) {
        mSelectListener = l;
    }

    private final InnerAdapter adapter = new InnerAdapter();

    private class InnerAdapter extends BaseAdapter {
        private List<Object> mData;

        public void setData(List<Object> data) {
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            if (mData == null || mData.isEmpty()) {
                return null;
            }
            if (position < 0 || position >= mData.size()) {
                return null;
            }
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView titleView;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_spinner_item, parent,
                        false);
            }
            titleView = (TextView) convertView.findViewById(R.id.title);
            Object obj = getItem(position);
            titleView.setText(obj == null ? "" : obj.toString());
            return convertView;
        }
    }
}
