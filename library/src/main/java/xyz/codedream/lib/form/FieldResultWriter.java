package xyz.codedream.lib.form;

import java.lang.reflect.Field;

public class FieldResultWriter {

	private FieldResultWriter() {
	}

	public static void write(Field field, Object target, Object formFieldvalue) {
		if (target != null && field != null) {
			field.setAccessible(true);
			Class<?> fieldType = field.getType();
			if (fieldType.isPrimitive()) {
				handlerPrimitive(field, fieldType, target, formFieldvalue);
			} else if (Number.class.isAssignableFrom(fieldType)) {
				handlerNumber(field, fieldType, target, formFieldvalue);
			} else {
				try {
					if (formFieldvalue == null || fieldType.isAssignableFrom(formFieldvalue.getClass())) {
						field.set(target, formFieldvalue);
					} else {
						// resolve real type data handler
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void handlerNumber(Field field, Class<?> fieldType, Object target, Object formFieldvalue) {
		try {
			if (formFieldvalue instanceof Number) {
				field.set(target, formFieldvalue);
			} else if (formFieldvalue == null) {
				return;
			} else {
				final String parseStr = formFieldvalue.toString();
				if (Integer.class == fieldType) {
					field.set(target, Integer.parseInt(parseStr));
				} else if (Float.class == fieldType) {
					field.set(target, Float.parseFloat(parseStr));
				} else if (Long.class == fieldType) {
					field.set(target, Long.parseLong(parseStr));
				} else if (Double.class == fieldType) {
					field.set(target, Double.parseDouble(parseStr));
				} else if (Byte.class == fieldType) {
					field.set(target, Byte.parseByte(parseStr));
				} else if (Short.class == fieldType) {
					field.set(target, Short.parseShort(parseStr));
				} else {
					throw new IllegalArgumentException(
							"unsupport argument number type:" + fieldType.getCanonicalName());
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static void handlerPrimitive(Field field, Class<?> fieldType, Object target, Object value) {
		if (value == null) {
			// ignore
			return;
		}
		try {
			final String parseStr = value.toString();
			if (int.class == fieldType) {
				field.setInt(target, Integer.parseInt(parseStr));
			} else if (float.class == fieldType) {
				field.setFloat(target, Float.parseFloat(parseStr));
			} else if (long.class == fieldType) {
				field.setLong(target, Long.parseLong(parseStr));
			} else if (double.class == fieldType) {
				field.setDouble(target, Double.parseDouble(parseStr));
			} else if (byte.class == fieldType) {
				field.setByte(target, Byte.parseByte(parseStr));
			} else if (short.class == fieldType) {
				field.setShort(target, Short.parseShort(parseStr));
			} else {
				throw new IllegalArgumentException("unsupport argument primitive type:" + fieldType.getCanonicalName());
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
