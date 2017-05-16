package xyz.codedream.lib.form.field;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * 金钱输入字段
 * 
 * @author skg
 *
 */
public class FormFieldMoneyEditText extends FormFieldEditText {

	public FormFieldMoneyEditText(int id, String title, boolean titleInHint, PasswordStyle pwd) {
		super(id, title, titleInHint, pwd);
	}

	public FormFieldMoneyEditText(int id, String title, boolean titleInHint) {
		super(id, title, titleInHint);
	}

	@Override
	protected void onCreateView(Context context) {
		super.onCreateView(context);
		TextView tv = new TextView(context);
		tv.setText("元");
		tv.setTextColor(0xff888888);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		addEndView(tv);
		//
		if (mEditText != null) {
			mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEditText.setTextColor(0xFFFC5E5E);
		}
	}
}
