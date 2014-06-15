package org.virtue.cache.def;

import org.virtue.game.logic.vars.VarDomainType;
import org.virtue.network.protocol.packet.RS3PacketReader;

public class VarBitType {
	
	private static int[] masklookup = new int[32];
	
	static {
		int i = 2;
		for (int bitIndex = 0; bitIndex < 32; bitIndex++) {
		    masklookup[bitIndex] = i - 1;
		    i += i;
		}
    }
	
	public int bitKey;
    private VarDomainType type;
    private int varKey;
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
	
	final void read(RS3PacketReader buffer) {
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
			type = VarDomainType.forID(buffer.getUnsignedByte());
			varKey = buffer.getBigSmart();
			break;
		case 2:
			startBit = buffer.getUnsignedByte();
			endBit = buffer.getUnsignedByte();
			break;
		}
	}
	
	public VarDomainType getDomainType () {
		return type;
	}
	
	public int getBaseVarKey () {
		return varKey;
	}
	
	public int setVarbitValue(int original, int bitVal) {
		int mask = masklookup[endBit - startBit];
		mask <<= startBit;
		return original & (mask ^ 0xffffffff) | bitVal << startBit & mask;
	}
	
	public int getVarbitValue(int value) {
		int mask = masklookup[endBit - startBit];
		return value >> startBit & mask;
	}
	
	@Override
	public String toString () {
		return new StringBuilder().append(bitKey).append("={start:").append(startBit).append(", end=").append(endBit)
				.append(", key=").append(varKey).append(", type=").append(type).append("(").append(type.ordinal()).append(")}").toString();
	}
}
