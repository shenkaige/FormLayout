package xyz.codedream.lib.form;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xyz.codedream.lib.form.FormField.JustShowField;
import xyz.codedream.lib.form.FormField.PasswordStyle;
import xyz.codedream.lib.form.annotion.FField;
import xyz.codedream.lib.form.annotion.FFieldInput;
import xyz.codedream.lib.form.annotion.FFieldLength;
import xyz.codedream.lib.form.annotion.FFieldNumberRange;
import xyz.codedream.lib.form.annotion.FFieldOptions;
import xyz.codedream.lib.form.annotion.FFieldValidator;
import xyz.codedream.lib.form.annotion.Form;
import xyz.codedream.lib.form.field.FormFieldDivider;
import xyz.codedream.lib.form.field.FormFieldEditText;
import xyz.codedream.lib.form.field.FormFieldMoneyEditText;
import xyz.codedream.lib.form.field.FormFieldSpinner;
import xyz.codedream.lib.form.field.FormFieldTitle;
import xyz.codedream.lib.form.validator.LengthValidator;
import xyz.codedream.lib.form.validator.NotNullValidator;
import xyz.codedream.lib.form.validator.NumberRangeValidator;

/**
 * Form controller
 * 
 * @author skg
 *
 */
public class FormLayout extends LinearLayout {
	private static final String TAG = FormLayout.class.getSimpleName();
	private SparseArray<FormField<?>> mFormFieldList = new SparseArray<FormField<?>>();

	public FormLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FormLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOrientation(VERTICAL);
	}

	public void tipsError(String msg) {
		Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
	}

	/**
	 * Add money edit
	 */
	public FormFieldMoneyEditText money(int titleResId) {
		return money(titleResId, false);
	}

	/**
	 * Add money edit
	 */
	public FormFieldMoneyEditText money(int titleResId, boolean titleInHint) {
		FormFieldMoneyEditText field = new FormFieldMoneyEditText(titleResId, getString(titleResId), titleInHint);
		addField(field);
		return field;
	}

	/**
	 * 添加输入框
	 */
	public FormFieldEditText editPwd(int titleResId, boolean titleInHint) {
		return edit(titleResId, titleInHint, PasswordStyle.HIDE);
	}

	/**
	 * 添加输入框
	 */
	public FormFieldEditText edit(int titleResId) {
		return edit(titleResId, false);
	}

	/**
	 * 添加输入框
	 */
	public FormFieldEditText edit(int titleResId, boolean titleInHint) {
		return edit(titleResId, titleInHint, PasswordStyle.NONE);
	}

	/**
	 * 添加输入框
	 * 
	 * @param titleResId
	 * @param titleInHint
	 * @return
	 */
	public FormFieldEditText edit(int titleResId, boolean titleInHint, PasswordStyle pwdStyle) {
		FormFieldEditText field = new FormFieldEditText(titleResId, getString(titleResId), titleInHint, pwdStyle);
		addField(field);
		return field;
	}

	/**
	 * 添加选择器
	 * 
	 * @param titleResId
	 * @param data
	 * @return
	 */
	public FormFieldSpinner spinner(int titleResId, List<Object> data) {
		FormFieldSpinner field = new FormFieldSpinner(titleResId, getString(titleResId));
		field.setDataList(data);
		addField(field);
		return field;
	}

	/**
	 * 添加选择器
	 * 
	 * @param titleResId
	 * @param data
	 * @return
	 */
	public FormFieldSpinner spinner(int titleResId, Object... data) {
		FormFieldSpinner field = new FormFieldSpinner(titleResId, getString(titleResId));
		field.setData(data);
		addField(field);
		return field;
	}

	private int unuseId = -1;

	/**
	 * 添加title
	 * 
	 * @param titleResId
	 * @return
	 */
	public FormFieldTitle title(int titleResId) {
		unuseId--;
		FormFieldTitle field = new FormFieldTitle(unuseId, getString(titleResId));
		addField(field);
		return field;
	}

	/**
	 * 添加分割器
	 * 
	 * @return
	 */
	public FormFieldDivider divider() {
		unuseId--;
		FormFieldDivider field = new FormFieldDivider(unuseId);
		addField(field);
		return field;
	}

	public FormLayout addField(FormField<?> field) {
		mFormFieldList.put(field.getId(), field);
		field.onFormAttached(this);
		View view = field.getShowView();
		if (fieldTextColor != null) {
			field.setTitleColor(fieldTextColor);
		}
		view.setId(field.getId());
		addView(view);
		return this;
	}

	public FormField<?> getFormFieldById(int id) {
		return mFormFieldList.get(id);
	}

	public String getEditFieldValue(int id) {
		FormFieldEditText ed = (FormFieldEditText) getFormFieldById(id);
		return ed.getResult();
	}

	private Integer fieldTextColor;

	@SuppressWarnings("deprecation")
	public void applyFieldTitleColorRes(int colorRes) {
		applyFieldTitleColor(getResources().getColor(colorRes));
	}

	public void applyFieldTitleColor(int color) {
		fieldTextColor = color;
		int count = mFormFieldList.size();
		for (int i = 0; i < count; i++) {
			FormField<?> f = mFormFieldList.valueAt(i);
			if (f instanceof FormField.JustShowField) {
				continue;
			}
			f.setTitleColor(fieldTextColor);
		}
	}

	public boolean validateForm() {
		if (mFormFieldList.size() <= 0) {
			return true;
		}
		int size = mFormFieldList.size();
		for (int i = 0; i < size; i++) {
			FormField<?> ff = mFormFieldList.valueAt(i);
			if (ff instanceof JustShowField) {
				continue;
			}
			if (!ff.validateResult()) {
				return false;
			}
			FieldResultWriter.write(ff.getBindField(), mFormModel, ff.getResult());
			Log.d(TAG, "" + ff.getResult());
		}
		return true;
	}

	private String getString(int resId) {
		return getContext().getString(resId);
	}

	private Object mFormModel;
	private Class<?> mFormModelClz;

	public Object getFormBindModel() {
		return mFormModel;
	}

	public void parseAndBindField(Object obj) {
		parseAndBindField(obj, null);
	}

	public void parseAndBindField(Object obj, ParseFieldFilter filter) {
		mFormModel = obj;
		mFormModelClz = getFormClass(obj.getClass());
		if (mFormModelClz == null) {
			throw new IllegalArgumentException(
					"Can't find Form annotaton from class:" + obj.getClass().getCanonicalName());
		}
		ArrayList<FormField<?>> fields = FormLayout.parseField(getContext(), obj, mFormModelClz, filter);
		if (fields == null || fields.isEmpty()) {
			return;
		}
		for (FormField<?> f : fields) {
			addField(f);
		}
	}

	public interface ParseFieldFilter {
		public boolean isSkip(int id);

		public void onFieldCreated(int id, FormField<?> f);
	}

	public static Class<?> getFormClass(Class<?> clz) {
		if (clz == null || clz == Void.class) {
			return null;
		}
		if (clz.getAnnotation(Form.class) == null) {
			return getFormClass(clz.getSuperclass());
		} else {
			return clz;
		}
	}

	public static ArrayList<FormField<?>> parseField(Context context, Object obj, Class<?> referClz) {
		return parseField(context, obj, referClz, null);
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<FormField<?>> parseField(Context context, Object obj, Class<?> referClz,
			ParseFieldFilter filter) {
		Class<?> clz = referClz == null ? obj.getClass() : referClz;
		Field[] fields = clz.getDeclaredFields();
		ArrayList<FormField<?>> result = new ArrayList<FormField<?>>();
		for (Field f : fields) {
			FField fField = f.getAnnotation(FField.class);
			if (fField == null) {
				continue;
			}
			if (filter != null && filter.isSkip(fField.id())) {
				continue;
			}
			FormField<Object> formField = null;
			switch (fField.type()) {
			case MONEY:
				formField = (FormField<Object>) createMoneyField(context, fField, f);
				break;
			case SPINNER:
				formField = (FormField<Object>) createSpinnerField(context, fField, f);
				break;
			case EDIT:
				formField = (FormField<Object>) createEditField(context, fField, f);
				break;
			case DIVIDER:
				formField = (FormField<Object>) createDividerField(context, fField, f);
				break;
			case TITLE:
				formField = (FormField<Object>) createTitleField(context, fField, f);
				break;
			default:
				throw new IllegalArgumentException("unknow form field type" + fField.type());
			}
			formField.bindField(f);
			try {
				f.setAccessible(true);
				Object value = f.get(obj);
				if (value != null) {
					formField.setDefaultValue(value);
				}
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			FFieldOptions fo = f.getAnnotation(FFieldOptions.class);
			if (fo != null) {
				Class<? extends FieldOptions<?>> opClz = fo.value();
				if (opClz != null) {
					try {
						@SuppressWarnings("rawtypes")
						FieldOptions fopInstance = opClz.newInstance();
						formField.setFieldOptions(fopInstance);
					} catch (InstantiationException e) {
						e.printStackTrace();
						throw new IllegalArgumentException("create FieldOptions failed:" + e);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new IllegalArgumentException("create FieldOptions failed:" + e);
					}
				}
			}
			if (filter != null) {
				filter.onFieldCreated(fField.id(), formField);
			}
			result.add(formField);
		}
		Collections.sort(result, new Comparator<FormField<?>>() {
			@Override
			public int compare(FormField<?> lhs, FormField<?> rhs) {
				return lhs.getId() - rhs.getId();
			}
		});
		return result;
	}

	private static FormField<?> createTitleField(Context context, FField f, Field desc) {
		return new FormFieldTitle(f.id(), context.getString(f.titleId()));
	}

	private static FormField<?> createDividerField(Context context, FField f, Field desc) {
		return new FormFieldDivider(f.id());
	}

	private static FormField<?> createSpinnerField(Context context, FField f, Field desc) {
		FormFieldSpinner field = new FormFieldSpinner(f.id(), context.getString(f.titleId()));
		field.setValidator(findValidator(context, f, desc));
		return field;
	}

	private static FormField<?> createEditField(Context context, FField f, Field desc) {
		FFieldInput fInput = desc.getAnnotation(FFieldInput.class);
		FFieldLength fLength = desc.getAnnotation(FFieldLength.class);
		FormFieldEditText field = new FormFieldEditText(f.id(), context.getString(f.titleId()), f.hintStyle(),
				f.pwdStyle());
		setMaxLength(field, fLength);
		setInputType(field, desc, fInput);
		Validator<String> validator = findValidator(context, f, desc);
		field.setValidator(validator);
		field.setUnit(f.endUnit());
		return field;
	}

	private static FormField<?> createMoneyField(Context context, FField f, Field desc) {
		FormFieldMoneyEditText field = new FormFieldMoneyEditText(f.id(), context.getString(f.titleId()),
				f.hintStyle());
		FFieldInput fInput = desc.getAnnotation(FFieldInput.class);
		FFieldLength fLength = desc.getAnnotation(FFieldLength.class);
		// FFieldNumberRange fNumberRange =
		// desc.getAnnotation(FFieldNumberRange.class);
		// FFieldValidator fValidator =
		// desc.getAnnotation(FFieldValidator.class);
		// FFileNotNull fNotNull = desc.getAnnotation(FFileNotNull.class);
		setMaxLength(field, fLength);
		setInputType(field, desc, fInput);
		return field;
	}

	private static void setInputType(FormFieldEditText f, Field fileType, FFieldInput input) {
		if (input == null) {
			Class<?> tt = fileType.getType();
			if (tt.isPrimitive() || Number.class.isAssignableFrom(tt)) {
				f.setFieldInputType(FFieldInput.TYPE_CLASS_NUMBER);
				if (tt == float.class || tt == Float.class || tt == double.class || tt == Double.class) {
					f.setAccept("0123456789,");
				} else {
					f.setAccept("0123456789");
				}
			}
		} else {
			f.setFieldInputType(input.type());
			f.setAccept(input.accept());
		}
	}

	private static void setMaxLength(FormFieldEditText f, FFieldLength length) {
		if (length == null) {
			return;
		}
		int[] range = length.range();
		if (range == null) {
			return;
		}
		if (range.length > 1) {
			f.setMaxInputLength(range[1]);
		} else if (range.length > 0) {
			f.setMaxInputLength(range[0]);
		}
	}

	private static <T> Validator<T> findValidator(Context context, FField ff, Field desc) {
		Validator<T> validator = null;
		if (ff.notNull()) {
			String msg = ff.nullMsg();
			if (FField.NULL_MSG_EMPTY.equals(msg)) {
				int id = ff.nullMsgId();
				if (id != FField.NULL_MSG_ID_EMPTY) {
					msg = context.getString(id);
				}
			}
			NotNullValidator<T> v = new NotNullValidator<T>();
			v.setErrorTemplate(msg, false);
			v.setMessageOnTitle(msg == null || msg.length() <= 0);
			validator = v;
		}
		//
		FFieldNumberRange fNumberRange = desc.getAnnotation(FFieldNumberRange.class);
		if (fNumberRange != null) {
			String msg = fNumberRange.msg();
			if (TextUtils.isEmpty(msg)) {
				int id = fNumberRange.msgResId();
				if (id != View.NO_ID) {
					msg = context.getString(id);
				}
			}
			NumberRangeValidator<T> v = new NumberRangeValidator<T>();
			v.setErrorTemplate(msg, fNumberRange.msgFormat());
			double[] range = fNumberRange.range();
			if (range == null || range.length < 2) {
				throw new IllegalArgumentException(
						FFieldNumberRange.class.getCanonicalName() + " range must be set and lengt must be 2");
			}
			if (range.length > 1) {
				v.setRange(range[0], range[1]);
			} else {
				v.setRange(range[0]);
			}
			v.setRange(range[0], range[1]);
			if (validator == null) {
				validator = v;
			} else {
				validator.addSubsequent(v);
			}
		}
		//
		FFieldLength fLenght = desc.getAnnotation(FFieldLength.class);
		if (fLenght != null) {
			String msg = fLenght.msg();
			if (TextUtils.isEmpty(msg)) {
				int id = fLenght.msgResId();
				if (id != View.NO_ID) {
					msg = context.getString(id);
				}
			}
			LengthValidator<T> v = new LengthValidator<T>();
			int[] range = fLenght.range();
			if (range == null || range.length < 1) {
				throw new IllegalArgumentException(
						FFieldLength.class.getCanonicalName() + " range can't be null,and range length must >=1");
			}
			if (range.length > 1) {
				v.setRange(range[0], range[1]);
			} else {
				v.setRange(range[0]);
			}
			v.setErrorTemplate(msg, fLenght.msgFormat());
			if (validator == null) {
				validator = v;
			} else {
				validator.addSubsequent(v);
			}
		}
		return appendValidator(context, validator, desc);
	}

	private static <T> Validator<T> appendValidator(Context context, Validator<T> root, Field desc) {
		FFieldValidator v = desc.getAnnotation(FFieldValidator.class);
		if (v == null) {
			return root;
		} else {
			Class<? extends Validator<?>> clz = v.value();
			try {
				@SuppressWarnings("unchecked")
				Validator<T> newV = (Validator<T>) clz.newInstance();
				String errorT = v.errorTemplate();
				boolean format = v.format();
				if (!TextUtils.isEmpty(errorT)) {
					newV.setErrorTemplate(errorT, format);
				}
				if (root == null) {
					return newV;
				} else {
					root.addSubsequent(newV);
					return root;
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(e);
			}
		}
	}
}
