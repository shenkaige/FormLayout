package xyz.codedream.lib.form;

import android.content.Context;

public interface FieldOptions<T> {

	public int getCount(Class<?> fieldType);

	public String[] getShowArray(Class<?> fieldType, Context context);

	public T[] getValues(Class<?> fieldType, Context context);

	public T getValue(Class<?> fieldType, int position);

	public static class EnumOptions implements FieldOptions<Enum<?>> {

		@Override
		public int getCount(Class<?> fieldType) {
			if (fieldType.isEnum()) {
				return fieldType.getEnumConstants().length;
			} else {
				return 0;
			}
		}

		@Override
		public String[] getShowArray(Class<?> fieldType, Context context) {
			Object[] enumValues = getValues(fieldType, context);
			if (enumValues == null || enumValues.length <= 0) {
				return null;
			} else {
				int size = enumValues.length;
				String[] stringValues = new String[size];
				for (int i = 0; i < size; i++) {
					stringValues[i] = enumValues[i].toString();
				}
				return stringValues;
			}
		}

		@Override
		public Enum<?> getValue(Class<?> fieldType, int position) {
			if (fieldType.isEnum()) {
				return (Enum<?>) fieldType.getEnumConstants()[position];
			} else {
				return null;
			}
		}

		@Override
		public Enum<?>[] getValues(Class<?> fieldType, Context context) {
			if (fieldType.isEnum()) {
				return (Enum<?>[]) fieldType.getEnumConstants();
			} else {
				return null;
			}
		}

	}
}
