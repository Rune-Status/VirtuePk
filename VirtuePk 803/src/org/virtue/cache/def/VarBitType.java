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
import org.virtue.network.protocol.packet.RS3PacketReader;

public class VarBitType {

	public enum DomainType { PLAYER, NPC, CLIENT, WORLD, REGION, OBJECT, CLAN, CLAN_SETTINGS, UNKNOWN_1, UNKNOWN_2 };
	
	public int bitKey;
    public DomainType type;
    public int varKey;
    int startBit;
    int endBit;
    
    public VarBitType (int id, byte[] data) {
    	this.bitKey = id;
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
		switch (opcode) {
		case 1:
			type = DomainType.values()[buffer.getUnsignedByte()];
			varKey = buffer.getBigSmart();
			break;
		case 2:
			startBit = buffer.getUnsignedByte();
			endBit = buffer.getUnsignedByte();
			break;
		}
	}
	
	public void printFields(BufferedWriter writer) throws IllegalArgumentException, IllegalAccessException, IOException {
		writer.write(bitKey+"={start:"+startBit+", end="+endBit+", key="+varKey+", type="+type+"("+type.ordinal()+")}");
		writer.newLine();
		writer.flush();
	}
	
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		printAll(CacheLoader.getCache());
	}
	
	public static void printAll (Cache cache) throws IOException, IllegalArgumentException, IllegalAccessException {
		int count = 0;

		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.CONFIG));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());
		ReferenceTable.Entry entry = table.getEntry(CacheIndex.CONFIG_VARBIT);
		
		File directory = new File("./dumps/");
		directory.mkdirs();
		File outputFile = new File(directory, "varbitdefs.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		int nonSparseMember = 0;		
		Archive archive = Archive.decode(cache.read(CacheIndex.CONFIG, CacheIndex.CONFIG_VARBIT).getData(), entry.size());
		for (int member = 0; member < entry.capacity(); member++) {
			ReferenceTable.ChildEntry childEntry = entry.getEntry(member);
			if (childEntry == null) {
				continue;
			}
			VarBitType type = new VarBitType(member, archive.getEntry(nonSparseMember++).array());
			type.printFields(writer);
			count++;
		}
		writer.close();
		System.out.println("Loaded " + count + " VarBit type definitions.");
	}
}
