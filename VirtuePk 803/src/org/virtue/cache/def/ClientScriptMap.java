package org.virtue.cache.def;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;
import org.virtue.cache.tools.CacheLoader;
import org.virtue.network.protocol.packet.RS3PacketReader;

public class ClientScriptMap {
	
	public final int mapID;	
	public char aChar5898;
	public char type;
	String defaultstr = "null";
	int defaultInt;
	int count = 0;
	Map<Integer, Serializable> valueMap;
	Object[] valueArray;
	
	public ClientScriptMap (int id, byte[] data) {
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
		if (opcode == 1) {
		    type = (char) buffer.get();
		} else if (opcode == 2) {
		    aChar5898 = (char) buffer.get();
		} else if (3 == opcode) {
		    defaultstr = buffer.getString();
		} else if (opcode == 4) {
			defaultInt = buffer.getInt();
		} else if (opcode == 5 || opcode == 6) {
		    count = buffer.getUnsignedShort();
		    valueMap = new HashMap<Integer, Serializable>(count);
		    for (int index = 0; index < count; index++) {
				int key = buffer.getInt();
				java.io.Serializable serializable;
				if (opcode == 5) {
				    serializable = buffer.getString();
				} else {
				    serializable = new Integer(buffer.getInt());
				}
				valueMap.put(new Integer(key), serializable);
		    }
		} else if (opcode == 7 || opcode == 8) {
		    int maxValue = buffer.getUnsignedShort();
		    count = buffer.getUnsignedShort();
		    valueArray = new Object[maxValue];
		    for (int index = 0; index < count; index++) {
				int key = buffer.getUnsignedShort();
				if (opcode == 7) {
				    valueArray[key] = buffer.getString();
				} else {
				    valueArray[key] = new Integer(buffer.getInt());
				}
		    }
		}
	}
	
	public void printFields(BufferedWriter writer) throws IllegalArgumentException, IllegalAccessException, IOException {
		if (valueMap != null) {
			writer.write(mapID + "->" + valueMap);
		} else if (valueArray != null) {
			writer.write(mapID + "->" + Arrays.toString(valueArray));
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

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.CLIENT_SCRIPT_MAPS));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		//gfxDefinitions = new GraphicsDefinition[files * 256];
		File directory = new File("./dumps/");
		directory.mkdirs();
		File outputFile = new File(directory, "cs2maps.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.CLIENT_SCRIPT_MAPS, file).getData(), entry.size());
			int nonSparseMember = 0;
			for (int member = 0; member < entry.capacity(); member++) {
				ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
				if (childEntry == null) {
					continue;
				}
				int id = file * 256 + member;
				ClientScriptMap map = new ClientScriptMap(id, archive.getEntry(nonSparseMember++).array());
				map.printFields(writer);
				//GraphicsDefinition definition = new GraphicsDefinition(id, archive.getEntry(nonSparseMember++).array());
				//gfxDefinitions[id] = definition;
				count++;
			}
		}
		writer.close();
		System.out.println("Loaded " + count + " client script maps.");
	}
}
