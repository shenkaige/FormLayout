package xyz.codedream.lib.form.validator;

import android.content.Context;
import android.text.TextUtils;

/**
 * 验证长度
 * 
 * @author skg
 *
 */
public class LengthValidator<Result> extends BaseValidator<Result> {
	private int min;
	private int max;
	private int matchLimit;
	private boolean isMatchLimit;

	public void setRange(int min, int max) {
		this.min = min;
		this.max = max;
		isMatchLimit = false;
	}

	public void setRange(int matchLimit) {
		this.matchLimit = matchLimit;
		isMatchLimit = true;
	}

	@Override
	public boolean validate(Result result) {
		if (result instanceof String) {
			int len = ((String) result).length();
			if (isMatchLimit) {
				return len == matchLimit;
			} else {
				if (min > 0 && len < min) {
					return false;
				}
				if (max > 0 && len > max) {
					return false;
				}
				return true;
			}
		} else {
			return true;
		}
	}

	private static final String def_format = "%s%s~%s位";
	private static final String def_format_one_limit = "%s%s位";
	private static final String def_format_too_small = "%s至少%s位";
	private static final String def_format_too_big = "%s最多%s位";

	@Override
	public String getErrorMessage(Context context, String fieldTitle) {
		String msg = getErrorTemplate();
		if (TextUtils.isEmpty(msg)) {
			if (isMatchLimit) {
				return String.format(def_format_one_limit, fieldTitle, matchLimit);
			} else {
				if (min > 0 && max > 0) {
					return String.format(def_format, fieldTitle, min, max);
				} else if (min <= 0 && max <= 0) {
					return null;
				} else {
					if (min < 0) {
						return String.format(def_format_too_big, fieldTitle, max);
					} else {
						return String.format(def_format_too_small, fieldTitle, min);
					}
				}
			}
		} else {
			return msg;
		}
	}

}
