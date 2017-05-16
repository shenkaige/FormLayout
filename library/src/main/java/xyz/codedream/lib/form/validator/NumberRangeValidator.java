package xyz.codedream.lib.form.validator;

import android.content.Context;
import android.text.TextUtils;

/**
 * 正则表达式验证
 * 
 * @author skg
 *
 */
public class NumberRangeValidator<Result> extends BaseValidator<Result> {
	private Double min;
	private Double max;
	private Double matchLimit;
	private boolean isMatchLimit;
	private String divider;

	public void setResultDivider(String divider) {
		this.divider = divider;
	}

	public void setRange(Double min, Double max) {
		this.min = min;
		this.max = max;
		isMatchLimit = false;
	}

	public void setRange(Double matchLimit) {
		this.matchLimit = matchLimit;
		isMatchLimit = true;
	}

	@Override
	public boolean validate(Result result) {
		Double dd = null;
		try {
			String str = (String) result;
			if (str != null && divider != null) {
				str = str.replace(divider, "");
			}
			dd = Double.parseDouble(str);
		} catch (Exception e) {
			return false;
		}
		if (isMatchLimit) {
			return matchLimit == dd;
		} else {
			if (min != null && dd < min) {
				return false;
			}
			if (max != null && dd > max) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getErrorMessage(Context context, String fieldTitle) {
		String msg = getErrorTemplate();
		if (TextUtils.isEmpty(msg)) {
			if (isMatchLimit) {
				if (matchLimit != null) {
					return String.format("%s必须是%s", fieldTitle, matchLimit);
				}
			} else {
				if (min != null && max != null) {
					return String.format("%s范围是[%s~%s]", fieldTitle, min, max);
				}
				if (min == null) {
					return String.format("%s最大值为%s", fieldTitle, max);
				}
				if (max == null) {
					return String.format("%s最小值为%s", fieldTitle, min);
				}
			}
			return "检查出错";
		} else {
			return msg;
		}

	}
}
