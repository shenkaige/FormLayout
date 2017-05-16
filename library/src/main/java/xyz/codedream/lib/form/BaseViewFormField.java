package xyz.codedream.lib.form;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 表单View基础实现
 * 
 * @author skg
 *
 * @param <Result>
 */
public abstract class BaseViewFormField<Result> extends AbstractFormField<Result> {

	private final int mTitleViewId;

	public BaseViewFormField(int id) {
		this(id, R.id.field_title);
	}

	public BaseViewFormField(int id, int titleViewId) {
		super(id);
		mTitleViewId = titleViewId;
	}

	@Override
	public final void onFormAttached(FormLayout form) {
		super.onFormAttached(form);
		onCreateView(form.getContext());
	}

	protected abstract void onCreateView(Context context);

	private View mContentView;
	private TextView mTitleView;

	@Override
	public View getShowView() {
		return mContentView;
	}

	public void setContentView(int layoutId) {
		setContentView(LayoutInflater.from(getContent()).inflate(layoutId, null, false));
	}

	public void setContentView(View view) {
		mContentView = view;
		View titleView = mContentView.findViewById(mTitleViewId);
		if (titleView instanceof TextView) {
			mTitleView = (TextView) titleView;
		}
		refreshTitle();
		applyIconInner();
	}

	protected void refreshTitle() {
		if (mTitleView != null) {
			if (mTitleView instanceof EditText) {
				mTitleView.setHint(getTitle());
			} else {
				mTitleView.setText(getTitle());
			}
			checkSetTitleColor();
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T findViewById(int id) {
		return (T) mContentView.findViewById(id);
	}

	@Override
	protected void setTitle(String title) {
		super.setTitle(title);
		refreshTitle();
	}

	private Context getContent() {
		return getForm().getContext();
	}

	private Integer mTitleColor;

	@Override
	public BaseViewFormField<Result> setTitleColor(int color) {
		mTitleColor = color;
		checkSetTitleColor();
		return this;
	}

	private void checkSetTitleColor() {
		if (mTitleColor != null && mTitleView != null) {
			if (mTitleView instanceof EditText) {
				mTitleView.setHintTextColor(mTitleColor);
			} else {
				mTitleView.setTextColor(mTitleColor);
			}
		}
	}

	private Drawable mIconDrawable;
	private int mIconMargin;

	@Override
	public BaseViewFormField<Result> setIcon(Drawable icon, int margin) {
		mIconDrawable = icon;
		mIconMargin = margin;
		applyIconInner();
		return this;
	}

	private void applyIconInner() {
		if (mTitleView != null) {
			Drawable[] dd = mTitleView.getCompoundDrawables();
			mTitleView.setCompoundDrawablesWithIntrinsicBounds(//
					mIconDrawable, //
					dd[1], //
					dd[2], //
					dd[3]);
			mTitleView.setCompoundDrawablePadding(mIconMargin);
		}
	}
}
