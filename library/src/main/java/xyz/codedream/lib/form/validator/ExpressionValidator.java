package xyz.codedream.lib.form.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

/**
 * 正则表达式验证
 * 
 * @author skg
 *
 */
public class ExpressionValidator<Result> extends BaseValidator<Result> {

	private String mExpression;

	public void setExpression(String expression) {
		mExpression = expression;
	}

	@Override
	public boolean validate(Result result) {
		if (result == null) {
			return false;
		}
		String content = result.toString();
		Pattern p = Pattern.compile(mExpression);
		Matcher m = p.matcher(content);
		return m.matches();
	}

	@Override
	public String getErrorMessage(Context context, String fieldTitle) {
		if (isTempalteNeedFormat()) {
			return getErrorTemplate();
		} else {
			return getErrorTemplate();
		}
	}

}
