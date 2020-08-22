package com.github.ennoxhd.fontawesome.enumentries.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.github.ennoxhd.fontawesome.enumentries.util.Icon;
import com.github.ennoxhd.fontawesome.enumentries.util.IconDescription;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FontAwesome {

	public static void main(String[] args) throws Exception {

		final Path in = Path.of(System.getProperty("user.home") + "/Downloads/icons.json");
		final Path out = Path.of(System.getProperty("user.home") + "/Downloads/");

		final Map<String, Set<Icon>> iconsByStyle = new HashMap<>();
		Map<String, IconDescription> iconDescriptions = null;

		final Gson gson = new Gson();

		try(final BufferedReader reader = Files.newBufferedReader(in)) {
			final Type jsonMapType = new TypeToken<Map<String, IconDescription>>() {}.getType();
			iconDescriptions = gson.fromJson(reader, jsonMapType);
		}

		final Pattern beginsWithANumber = Pattern.compile("^[0-9].*$");
		for(Map.Entry<String, IconDescription> entry : iconDescriptions.entrySet()) {
			if("font-awesome-logo-full".equals(entry.getKey())) continue;
			String javaName = entry.getKey().toUpperCase().replace('-', '_');
			String cssClass = entry.getKey();
			String codePoint = entry.getValue().unicode;
			for(String style : entry.getValue().styles) {
				if(beginsWithANumber.matcher(javaName).find()) {
					javaName = "FA" + style.substring(0, 1).toUpperCase() + "_" + javaName;
				}
				if("regular".equals(style)) {
					cssClass = cssClass.substring(4, cssClass.length());
				}
				cssClass = "fa" + style.substring(0, 1).toLowerCase() + "-" + cssClass;
				Icon icon = new Icon(javaName, cssClass, codePoint);

				if(!iconsByStyle.containsKey(style)) {
					iconsByStyle.put(style, new TreeSet<>());
				}

				if(!iconsByStyle.get(style).contains(icon)) {
					iconsByStyle.get(style).add(icon);
				} else {
					throw new IllegalArgumentException(icon + " is already present!");
				}
			}
		}

		for(String style : iconsByStyle.keySet()) {
			String styleCapitalized = style.substring(0, 1).toUpperCase() + style.substring(1, style.length());
			final BufferedWriter writer = Files.newBufferedWriter(out.resolve("FontAwesome" + styleCapitalized + ".java"));
			for(Icon icon : iconsByStyle.get(style)) {
				writer.write(icon.javaName + "(\"" + icon.cssClass + "\", '\\u" + icon.codePoint + "'),\n");
			}
			writer.close();
		}
	}
}
