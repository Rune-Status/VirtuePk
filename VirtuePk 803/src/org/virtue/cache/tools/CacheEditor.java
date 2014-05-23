package org.virtue.cache.tools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import org.virtue.Launcher;
import org.virtue.cache.Archive;
import org.virtue.cache.Cache;
import org.virtue.cache.Container;
import org.virtue.cache.ReferenceTable;

import org.virtue.cache.def.AnimationDefinitionLoader;
import org.virtue.cache.def.CacheIndex;
import org.virtue.cache.def.GraphicsDefinitionLoader;
import org.virtue.cache.def.ItemDefinitionLoader;
import org.virtue.cache.def.NPCDefinitionLoader;
import org.virtue.cache.def.ObjectDefinitionLoader;
import org.virtue.game.logic.node.object.ObjectTransformer;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.game.logic.region.Tile;
import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class CacheEditor {

	private static JFrame frame = null;
	
	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		frame = new JFrame("CacheEditor");
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*int i = 0;
		while (i < 14484) {
			ItemDefinition item = new ItemDefinition(i);
	        System.out.println("Item: name="+item.getName()+", equiptID="+item.equipID+", equiptSlot="+item.equipSlotID+", maleModel="+item.maleEquip1);
			i++;
		}*/
		//ItemDefinitionLoader.dumpItems();
		//NPCDefinitionLoader.dumpNpc();
		//ObjectDefinitionLoader.dumpObjects();
		//AnimationDefinitionLoader.dumpAnimations();
		//GraphicsDefinitionLoader.dumpGfx();
		findObjectCoords(30548, CacheLoader.getCache());
		System.out.println("done");
	}
	
	public static void findObjectCoords (int expectedID, Cache cache) throws IOException {
		Container tableContainer = Container.decode(cache.getStore().read(255, CacheIndex.LANDSCAPES));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		int files = table.capacity();
		System.out.println(files+" total...");
		for (int file = 0; file < files; file++) {
			ReferenceTable.Entry entry = table.getEntry(file);
			if (entry == null) {
				continue;
			}
			Archive archive = Archive.decode(cache.read(CacheIndex.LANDSCAPES, file).getData(), entry.size());
			RS3PacketReader landStream = new RS3PacketReader(archive.getEntry(0).array());
			int objectID = -1;
			int modifier;
			while (landStream.getRemaining() > 0 && (modifier = landStream.getUnsignedSmart()) != 0) {
				objectID += modifier;
				int location = 0;
				int locationModifier;
				while (landStream.getRemaining() > 0 && (locationModifier = landStream.getUnsignedSmart()) != 0) {
					location += locationModifier - 1;
					int x = (location >> 6 & 0x3f);
					int y = (location & 0x3f);
					int z = location >> 12;
					int objectData = landStream.getUnsignedByte();
					int type = objectData >> 2;
					int rotation = objectData & 0x3;
					if (objectID == expectedID) {
						System.out.println("Object found at position: "+getCoords(file, x, y, z));//+file+", x="+x+", y="+y+", z="+z);
					}
				}
			}
		}
	}
		
	public static String getCoords (int cacheRegionID, int localX, int localY, int z) {
		int yComponent = (cacheRegionID >> 7);
		int xComponent = cacheRegionID & 0xff;
		
		int x = ((xComponent*8) << 3)+localX;
		int y = ((yComponent*8) << 3)+localY;
		return "x="+x+", y="+y+", z="+z;
	}
	
}
