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
import org.virtue.game.config.Js5ConfigGroup;

/**
 *
 * @author Virtue Development Team 2014 (c).
 */
public class VarBitTypeLoader {
	
	private static VarBitType[] varBitTypes;
	
	public static void load (Cache cache) throws IOException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.CONFIG));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());
		ReferenceTable.Entry entry = table.getEntry(Js5ConfigGroup.VAR_BIT);
		
		varBitTypes = new VarBitType[entry.capacity()];
		
		int nonSparseMember = 0;		
		Archive archive = Archive.decode(cache.read(CacheIndex.CONFIG, Js5ConfigGroup.VAR_BIT).getData(), entry.size());
		for (int member = 0; member < entry.capacity(); member++) {
			ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
			if (childEntry == null) {
				continue;
			}
			VarBitType type = new VarBitType(member, archive.getEntry(nonSparseMember++).array());
			varBitTypes[member] = type;
			count++;
		}
		System.out.println("Loaded " + count + " VarBitType definitions.");
	}
	
	public static int count() {
		return varBitTypes.length;
	}
	
	public static VarBitType forID (int id) {
		if (varBitTypes == null) {
			try {
				load(CacheLoader.getCache());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		if (id < 0 || id > varBitTypes.length) {
			return null;//VarBit does not exist
		}
		return varBitTypes[id];
	}
	
	public static void dumpVarBits () throws IOException, IllegalArgumentException, IllegalAccessException {
		load(CacheLoader.getCache());
		File directory = new File("./dumps/");
		directory.mkdirs();
		File outputFile = new File(directory, "varbitdefs.txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			for (VarBitType type : varBitTypes) {
				if (type == null) {
					continue;
				}
				writer.write(type.toString());
				writer.newLine();
				writer.flush();
			}
		}
	}
}
