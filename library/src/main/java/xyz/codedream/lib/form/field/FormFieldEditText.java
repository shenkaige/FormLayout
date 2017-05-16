package xyz.codedream.lib.form.field;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import xyz.codedream.lib.form.AbstractFormField;
import xyz.codedream.lib.form.BaseViewFormField;
import xyz.codedream.lib.form.R;

/**
 * 文本输入字段
 * 
 * @author skg
 *
 */
public class FormFieldEditText extends BaseViewFormField<String> implements TextWatcher {

	private boolean titleInHint;
	private PasswordStyle passwordStyle;

	public FormFieldEditText(int id, String title, boolean titleInHint) {
		this(id, title, titleInHint, PasswordStyle.HIDE);
	}

	public FormFieldEditText(int id, String title, boolean titleInHint, PasswordStyle pwd) {
		super(id, titleInHint ? R.id.filed_edittext : R.id.field_title);
		this.titleInHint = titleInHint;
		this.passwordStyle = pwd;
		setTitle(title);
	}

	protected EditText mEditText;
	private ToggleButton mPwdButton;
	private TextView mFieldDesc;

	private final static int INPUT_TYPE_HIDE_PWD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
	private final static int INPUT_TYPE_VISIBLE_PWD = InputType.TYPE_CLASS_TEXT
			| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

	@Override
	@SuppressLint("InflateParams")
	protected void onCreateView(Context context) {
		setContentView(titleInHint ? R.layout.field_edittext_hint_title : R.layout.field_edittext);
		mEditText = findViewById(R.id.filed_edittext);
		mEditText.addTextChangedListener(this);
		mFieldDesc = findViewById(R.id.filed_desc);
		ViewGroup rootView = (ViewGroup) getShowView();
		checkAddEndView();
		if (passwordStyle == null) {
			passwordStyle = PasswordStyle.NONE;
		}
		switch (passwordStyle) {
		case VISIBLE:
			mEditText.setInputType(INPUT_TYPE_HIDE_PWD);
			mPwdButton = (ToggleButton) LayoutInflater.from(context).inflate(R.layout.field_btn_pwd, null, false);
			rootView.addView(mPwdButton);
			mPwdButton.setChecked(false);
			mPwdButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						mEditText.setInputType(INPUT_TYPE_VISIBLE_PWD);
					} else {
						mEditText.setInputType(INPUT_TYPE_HIDE_PWD);
					}
				}
			});
			rootView.invalidate();
			break;
		case HIDE:
			mEditText.setInputType(INPUT_TYPE_HIDE_PWD);
			break;
		case NONE:
		default:
			break;
		}
		checkNullDesc();
		setDefaultValue(mDefaultValue);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		checkNullDesc();
	}

	@Override
	public String getResult() {
		String result = mEditText.getText().toString();
		if (result == null || result.length() <= 0) {
			return mDefaultValue;
		} else {
			return result;
		}
	}

	@Override
	public FormFieldEditText resetResult() {
		mEditText.setText("");
		return this;
	}

	private View endView;

	public void addEndView(View view) {
		endView = view;
		checkAddEndView();
	}

	private boolean isPassword() {
		return passwordStyle != null && passwordStyle != PasswordStyle.NONE;
	}

	private TextView unitView;

	private void checkAddEndView() {
		checkNullDesc();
		if (endView != null) {
			View rootView = getShowView();
			if (rootView == null) {
				return;
			}
			ViewGroup.LayoutParams lp = endView.getLayoutParams();
			if (lp == null) {
				lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			} else {
				lp = new MarginLayoutParams(lp.width, lp.height);
			}
			((MarginLayoutParams) lp).leftMargin = rootView.getResources()
					.getDimensionPixelSize(R.dimen.form_feild_content_interval_size);
			((ViewGroup) rootView).addView(endView, lp);
		}
		if (mEditText != null) {
			if (inputMaxLength > -1) {
				mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(inputMaxLength) });
			}
			if (fieldInputType != InputType.TYPE_NULL && !isPassword()) {
				mEditText.setInputType(fieldInputType);
			}
			mEditText.setEnabled(mEditable);
		}
		if (mEditText != null) {
			// TODO dos't support accept digits now
			// throw new
			// UnsupportedOperationException(getClass().getCanonicalName() +
			// "dos't support accept digits now");
		}
		View rootView = getShowView();
		if (rootView != null) {
			if (!TextUtils.isEmpty(unitString)) {
				if (unitView == null) {
					unitView = new TextView(getForm().getContext());
					addEndView(unitView);
				}
				unitView.setText(unitString);
			}
			if (unitView != null && mEditText != null) {
				unitView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEditText.getTextSize());
				unitView.setTextColor(mEditText.getTextColors());
			}
		}
	}

	@Override
	public AbstractFormField<String> setNullable(boolean nullable) {
		super.setNullable(nullable);
		checkNullDesc();
		return this;
	}

	private void checkNullDesc() {
		if (mFieldDesc != null) {
			if (isNullable()) {
				String str = mEditText == null ? "" : mEditText.getText().toString();
				str = str.trim();
				if (str.length() <= 0) {
					mFieldDesc.setText("选填");
					mFieldDesc.setVisibility(View.VISIBLE);
				} else {
					mFieldDesc.setVisibility(View.GONE);
				}
			} else {
				mFieldDesc.setVisibility(View.GONE);
			}
		}
	}

	private int inputMaxLength = -1;

	private int fieldInputType = InputType.TYPE_NULL;

	/**
	 * set max input length
	 * 
	 * @param length
	 */
	public void setMaxInputLength(int length) {
		inputMaxLength = length;
		checkAddEndView();// invoke for set input length
	}

	/**
	 * set FieldInputType
	 * 
	 * @param length
	 */
	public void setFieldInputType(int type) {
		fieldInputType = type;
		checkAddEndView();// invoke for set input length
	}

	private String mAcceptChars;

	public void setAccept(String accept) {
		mAcceptChars = accept;
		checkAddEndView();// invoke for set accept chars
	}

	private String unitString;

	public void setUnit(String unit) {
		unitString = unit;
		checkAddEndView();// invoke for set unit view
	}

	private String mDefaultValue;

	@Override
	public void setDefaultValue(String value) {
		mDefaultValue = value;
		if (mEditText != null) {
			mEditText.setText(value);
		}
	}

	private boolean mEditable = true;

	public void setEditable(boolean editable) {
		this.mEditable = editable;
		checkAddEndView();// invoke for set unit view
	}
}
