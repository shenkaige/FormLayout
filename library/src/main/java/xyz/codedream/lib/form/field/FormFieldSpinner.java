package xyz.codedream.lib.form.field;

import java.util.ArrayList;
import java.util.List;

import xyz.codedream.lib.form.BaseViewFormField;
import xyz.codedream.lib.form.FieldOptions;
import xyz.codedream.lib.form.R;
import xyz.codedream.lib.form.window.DialogSpinnerSelect;
import xyz.codedream.lib.form.window.DialogSpinnerSelect.SelectListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 选择器字段
 * 
 * @author skg
 * 
 */
public class FormFieldSpinner extends BaseViewFormField<Object> implements OnClickListener {
	private TextView mSpinnerTv;

	public FormFieldSpinner(int id, String title) {
		super(id);
		setTitle(title);
	}

	private List<Object> mData;

	public void setDataList(List<Object> data) {
		mData = data;
	}

	public void setData(Object... data) {
		if (mData == null) {
			mData = new ArrayList<Object>();
		} else {
			mData.clear();
		}
		if (data != null) {
			for (Object obj : data) {
				mData.add(obj);
			}
		}
	}

	private int mSelectedPosition = -1;

	@Override
	public Object getResult() {
		if (mSelectedPosition == -1) {
			return mDefaultValue;
		} else {
			return mData.get(mSelectedPosition);
		}
	}

	@Override
	public FormFieldSpinner resetResult() {
		changeSelectedResult(-1);
		return this;
	}

	@Override
	protected void onCreateView(Context context) {
		setContentView(R.layout.field_spinner);
		mSpinnerTv = findViewById(R.id.filed_spinner_text);
		mSpinnerTv.setOnClickListener(this);
		setDefaultValue(mDefaultValue);
	}

	private DialogSpinnerSelect mDialog;

	private SelectListener selectListener = new SelectListener() {

		@Override
		public void onSelected(int position) {
			changeSelectedResult(position);
		}
	};

	private void changeSelectedResult(int position) {
		mSelectedPosition = position;
		if (position < 0 || mData == null || mData.isEmpty() || position >= mData.size()) {
			mSpinnerTv.setText("");
		} else {
			mSpinnerTv.setText(mData.get(position).toString());
		}
	}

	@Override
	public void onClick(View v) {
		if (mDialog == null) {
			mDialog = new DialogSpinnerSelect(getForm().getContext());
			mDialog.setSelectListener(selectListener);
		}
		if (mData == null || mData.isEmpty()) {
			FieldOptions<?> opt = getFieldOptions();
			if (opt != null) {
				Object[] strArray = opt.getValues(getBindField().getType(), getForm().getContext());
				if (strArray != null) {
					if (mData == null) {
						mData = new ArrayList<Object>();
					}
					for (Object s : strArray) {
						mData.add(s);
					}
				}
			}
		}
		mDialog.setData(mData);
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	private Object mDefaultValue;

	@Override
	public void setDefaultValue(Object value) {
		mDefaultValue = value;
		if (mSpinnerTv != null) {
			if (mDefaultValue == null) {
				mSpinnerTv.setText(null);
			} else {
				mSpinnerTv.setText(value.toString());
			}
		}
	}
}
