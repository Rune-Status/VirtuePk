package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;
import org.virtue.network.protocol.packet.RS3PacketReader;

public class GeneralMap {
	
	public final int mapID;
	
	private HashMap<Integer, Object> values;
	
	public GeneralMap(int id, byte[] data) {
		this.mapID = id;
		try {
			read(new RS3PacketReader(data));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void read(RS3PacketReader buffer) {
		for (;;) {
		    int opcode = buffer.getUnsignedByte();
		    if (0 == opcode) {
		    	break;
		    }
		    decode(buffer, opcode);
		}
    }
	
	private void decode(RS3PacketReader buffer, int opcode) {
		if (opcode == 249) {
		    int i_6_ = buffer.getUnsignedByte();
		    if (null == values) {
				//int i_7_ = Class480.method11001(i_6_, (byte) 29);
				values = new HashMap<Integer, Object>(i_6_);
		    }
		    for (int index = 0; index < i_6_; index++) {
				boolean bool = buffer.getUnsignedByte() == 1;
				int key = buffer.getTriByte();
				Object value;
				if (bool) {
					value = buffer.getString();
				} else {
					value = new Integer(buffer.getInt());
				}
				values.put(key, value);
		    }
		}
	}
	
	public void printFields(BufferedWriter writer) throws IllegalArgumentException, IllegalAccessException, IOException {
		if (values != null) {
			writer.write(mapID + "->" + values);
		} else {
			return;
		}
		writer.newLine();
		writer.flush();
	}
	
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		printAll(CacheLoader.getCache());
	}
	
	public static void printAll (Cache cache) throws IOException, IllegalArgumentException, IllegalAccessException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.GENERAL_MAPS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		//gfxDefinitions = new GraphicsDefinition[files * 256];
		File directory = new File("./dumps/");
		directory.mkdirs();
		File outputFile = new File(directory, "generalMaps.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.GENERAL_MAPS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 32 + member;
				GeneralMap map = new GeneralMap(id, archive.getEntry(nonSparseMember++).array());
				map.printFields(writer);
				//GraphicsDefinition definition = new GraphicsDefinition(id, archive.getEntry(nonSparseMember++).array());
				//gfxDefinitions[id] = definition;
				count++;
			}
		}
		writer.close();
		System.out.println("Loaded " + count + " general maps.");
	}
}
