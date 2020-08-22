package com.github.ennoxhd.fontawesome.enumentries.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Icon implements Comparable<Icon> {

	public String javaName;
	public String cssClass;
	public String codePoint;

	public Icon() {}

	public Icon(String javaName, String cssClass, String codePoint) {
		this.javaName = javaName;
		this.cssClass = cssClass;
		this.codePoint = codePoint;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj == null || !(obj instanceof Icon)) return false;
		Icon other = (Icon) obj;
		return Objects.equals(javaName, other.javaName)
				&& Objects.equals(cssClass, other.cssClass)
				&& Objects.equals(codePoint, other.codePoint);
	}

	@Override
	public int hashCode() {
		return Objects.hash(javaName, cssClass, codePoint);
	}

	@Override
	public String toString() {
		return getClass().getName() + "("
				+ Arrays.asList(getClass().getFields()).stream()
					.sorted((a, b) -> a.getName().compareTo(b.getName()))
					.map(field -> {
						try {
							return field.getName() + "=" + field.get(this).toString();
						} catch (IllegalArgumentException | IllegalAccessException e) {
							return "???=???";
						}
					})
					.collect(Collectors.joining(","))
				+ ")";
	}

	@Override
	public int compareTo(Icon o) {
		if(o == this) return 0;
		if(o == null) return 1;
		return Objects.compare(javaName, o.javaName, (a, b) -> {
			if(a == b) return 0;
			if(a == null) return -1;
			if(b == null) return 1;
			return a.compareTo(b);
		});
	}
}
