package xyz.codedream.lib.form.validator;

import android.content.Context;

/**
 * 正则表达式验证
 * 
 * @author skg
 *
 */
public class NotNullValidator<Result> extends BaseValidator<Result> {

	private boolean msgOnTitle = true;

	public void setMessageOnTitle(boolean onTitle) {
		msgOnTitle = onTitle;
	}

	@Override
	public boolean validate(Result result) {
		if (result == null) {
			return false;
		}
		if (result instanceof String) {
			String str = (String) result;
			return str.length() > 0;
		} else {
			return result != null;
		}
	}

	@Override
	public String getErrorMessage(Context context, String fieldTitle) {
		if (msgOnTitle) {
			return fieldTitle + "不可为空";
		} else {
			return getErrorTemplate();
		}
	}

}
