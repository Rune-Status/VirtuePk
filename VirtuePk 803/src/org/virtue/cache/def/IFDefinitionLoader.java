package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;

public class IFDefinitionLoader {

	public static class InterfaceDefinition {
		private IFComponentDefinition[] components;
		private int interfaceID;
		
		public InterfaceDefinition(int id,  IFComponentDefinition[] components) {
			this.components = components;
			this.interfaceID = id;
		}
		
		public int getID () {
			return interfaceID;
		}
		
		public IFComponentDefinition getComponent (int id) {
			if (components.length < id || id < 0) {
				return null;
			}
			return components[id];
		}
		
		public int getComponentCount () {
			return components.length;
		}
		
		protected void printDefinition () throws IOException, IllegalArgumentException, IllegalAccessException {
			File directory = new File("./dumps/interfaces/");
			directory.mkdirs();
			File file = new File(directory, interfaceID+".txt");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("===== Interface "+interfaceID+" =====\n");
			writer.newLine();
			writer.flush();
			for (int i=0;i<components.length;i++) {
				IFComponentDefinition component = components[i];
				if (component == null) {
					continue;
				}
				writer.write("===== Component "+i+" =====\n");
				component.printFields(writer);
				writer.newLine();
				writer.flush();
			}
			writer.close();
		}
	}
	
	private static InterfaceDefinition[] interfaceDefinitions;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;
		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.INTERFACES));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		interfaceDefinitions = new InterfaceDefinition[files];
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}

			Archive archive = Archive.decode(cache.read(CacheIndex.INTERFACES, file).getData(), entry.size());
			int nonSparseMember = 0;
			IFComponentDefinition[] components = new IFComponentDefinition[entry.capacity()];
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int hash = file << 16 | member;
				IFComponentDefinition definition = new IFComponentDefinition(hash, archive.getEntry(nonSparseMember++).array());
				components[member] = definition;
			}
			interfaceDefinitions[file] = new InterfaceDefinition(file, components);
			count++;
		}
		
		System.out.println("Loaded " + count + " interface definitions.");
	}
	
	public static InterfaceDefinition forId(int id) {
		try {
			if (interfaceDefinitions == null) {
				load(CacheLoader.getCache());
			}
			if (id < 0 || id > interfaceDefinitions.length) {
				return null;//Item does not exist
			}
			return interfaceDefinitions[id];
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/*public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		dumpInterfaces();
	}*/

	public static void dumpInterfaces() throws IllegalArgumentException, IllegalAccessException, IOException {
		load(CacheLoader.getCache());
		for (InterfaceDefinition def : interfaceDefinitions) {
			//ItemDefinition def = forId(i);
			if (def == null) {
				continue;
			}
			//System.out.println("===== Name: " + def.getName() + " =====");
			def.printDefinition();
			//System.out.println("=======================================\n");
		}
	}
}
