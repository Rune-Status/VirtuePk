package org.virtue.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.virtue.Constants;

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class GroovyManager {

	private static GroovyScriptEngine engine;

	private static final Map<String, Class<?>> scripts = new HashMap<>();
	
	public static void init() {
		try {
			engine = new GroovyScriptEngine(new String("data/scripts"));
			ImportCustomizer imports = new ImportCustomizer();
			imports.addImport("Launcher", "org.virtue.Launcher");
			imports.addImport("Constants", "org.virtue.Constants");
			File importsFile = new File("data/imports.txt");
			if (Constants.DEVELOPER_MODE) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(importsFile));
				generateImports(new File("bin/" + "org/virtue/game/"), imports, writer);
				generateImports(new File("bin/" + "org/virtue/network/"), imports, writer);
				writer.close();
			} else {
				loadImports(importsFile, imports);
			}

			engine.getConfig().addCompilationCustomizers(imports);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadImports(File file, ImportCustomizer imports) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String name;
		while ((name = reader.readLine()) != null) {
			imports.addImport(name.substring(name.lastIndexOf('.') + 1, name.length()), name);
		}
		reader.close();
	}
	
	private static void generateImports(File dir, ImportCustomizer imports, BufferedWriter writer) throws IOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				generateImports(file, imports, writer);
				continue;
			}

			if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
				String alias = file.getName().replace(".class", "");
				String name = file.getPath().replace(File.separatorChar, '/').replace("bin/", "").replace(".class", "").replace('/', '.');
				imports.addImports(alias, name);

				writer.write(name);
				writer.newLine();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <S> Class<S> loadScript(String scriptName) {
		Class<S> clazz = (Class<S>) scripts.get(scriptName);
		if (clazz == null) {
			try {
				clazz = engine.loadScriptByName(scriptName.replace('.', '/') + ".groovy");
			} catch (ResourceException | ScriptException e) {
				System.out.println("Failed to load script : " + scriptName);
				e.printStackTrace();
				return null;
			}
			scripts.put(scriptName, clazz);
		}
		return clazz;
	}

	public static <S> S initScript(String scriptName, Object... args) {
		try {
			Class<S> clazz = loadScript(scriptName);

			Class<?> types[] = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				types[i] = args[i].getClass();
			}

			return clazz.getConstructor(types).newInstance(args);
		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
			System.out.println("Failed to init script : " + scriptName);
			e.printStackTrace();
			return null;
		}
	}

	public static void clearCache() {
		engine.getGroovyClassLoader().clearCache();
		scripts.clear();
	}

}
