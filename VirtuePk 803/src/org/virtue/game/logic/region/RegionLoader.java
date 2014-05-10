package org.virtue.game.logic.region;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.virtue.Launcher;
import org.virtue.cache.def.CacheIndex;
import org.virtue.game.logic.node.object.RS3Object;
import org.virtue.network.protocol.packet.RS3PacketReader;

/**
 * @author Taylor
 * @version 1.0
 */
public class RegionLoader {

	/**
	 * Loads a world region map.
	 * @param region The region to load.
	 */
	public void loadRegion(final Region region) {
		if (!region.getLoadingStage().equals(RegionLoadingStage.NONE)) {
			return;
		}
		region.setLoadingStage(RegionLoadingStage.PREPARE_MAP);
		Launcher.getEngine().getAsycnhronousPool().execute(new Runnable() {

			@Override
			public void run() {
				byte[][][] mapSettings = new byte[4][64][64];
				ByteBuffer virtualMapData = null;
				ByteBuffer virtualNodeData = null;
				switch (region.getLoadingStage()) {
				case PREPARE_MAP:
					virtualMapData = getVirtualMapData(region.getArchiveKey());
					virtualNodeData = getVirtualNodeData(region.getArchiveKey());
					/*heightmapData = region.getHeightMapData();
					if (heightmapData == null) {
						heightmapData = new byte[4][64][64];
					}*/
					region.setLoadingStage(RegionLoadingStage.PARSE_HEIGH_MAP_MASKS);
				case PARSE_HEIGH_MAP_MASKS:
					if (virtualMapData != null) {
						RS3PacketReader mapStream = new RS3PacketReader(virtualMapData.array());
						/*for (int plane = 0; plane < 4; plane++) {
							for (int x = 0; x < 64; x++) {
								for (int y = 0; y < 64; y++) {
									int value = mapStream.getUnsignedByte();
									if ((value & 0x1) != 0) {
										mapStream.get();
										mapStream.getUnsignedSmart();
									}
									if ((value & 0x2) != 0) {
										//System.out.println("Clipped tile at x="+x+", y="+y+", z="+plane);
										mapSettings[plane][x][y] = (byte) mapStream.get();
									}
									if ((value & 0x4) != 0) {
										mapStream.getUnsignedSmart();
									}
									if ((value & 0x8) != 0) {
										mapStream.get();
									}
								}
							}
						}*/
						for (int plane = 0; plane < 4; plane++) {
							for (int deltaX = 0; deltaX < 64; deltaX++) {
								for (int deltaY = 0; deltaY < 64; deltaY++) {
									int bitMask = mapStream.getUnsignedByte();
									/*if ((bitMask & 0x1) != 0) {
										mapStream.get();
										mapStream.getUnsignedSmart();
									}*/
									if ((bitMask & 0x2) != 0) {
										//System.out.println("Clipped tile at x="+deltaX+", y="+deltaY+", z="+plane);
										mapSettings[plane][deltaX][deltaY] = (byte) mapStream.get();
									}
									if ((bitMask & 0x4) != 0) {
										mapStream.getUnsignedSmart();
									}
									if ((bitMask & 0x8) != 0) {
										mapStream.get();
									}
								}
							}
						}
					}
					region.setLoadingStage(RegionLoadingStage.APPLY_HEIGHT_MAP_DATA);
				case APPLY_HEIGHT_MAP_DATA:
					if (virtualMapData != null) {
						for (int plane = 0; plane < 4; plane++) {
							for (int x = 0; x < 64; x++) {
								for (int y = 0; y < 64; y++) {
									if ((mapSettings[plane][x][y] & 1) == 1) {
										int z = plane;
										if ((mapSettings[1][x][y] & 2) == 2) {
											z--;
										}
										if (z >= 0 && z <= 3) {
											region.getRegionMap().clipTile(z, x, y);
										}
									}
								}
							}
						}
						/*int clippingPlane = -1;
						for (int plane = 0; plane < 4; plane++) {
							for (int deltaX = 0; deltaX < 64; deltaX++) {
								for (int deltaY = 0; deltaY < 64; deltaY++) {
									if ((mapSettings[plane][deltaX][deltaY] & 1) == 1) {
										clippingPlane = plane;
									}
									if ((mapSettings[1][deltaX][deltaY] & 2) == 2) {
										clippingPlane--;
									}
									if (clippingPlane >= 0 && clippingPlane <= 3) {
										System.out.println("Clipped tile at x="+deltaX+", y="+deltaY+", z="+plane);
										region.getClippedRegionMap().clipTile(clippingPlane, deltaX, deltaY);
										//region.getRegionMap().clipTile(clippingPlane, deltaX, deltaY);
									}
								}
							}
						}*/
					}
					region.setLoadingStage(RegionLoadingStage.PARSE_NODE_DATA);
				case PARSE_NODE_DATA:
					if (virtualNodeData != null) {
						RS3PacketReader landStream = new RS3PacketReader(virtualNodeData.array());
						/*int objectId = -1;
						int incr;
						while (landStream.getRemaining() > 0 && (incr = landStream.getSmart2()) != 0) {
							objectId += incr;
							int location = 0;
							int incr2;
							while (landStream.getRemaining() > 0 && (incr2 = landStream.getUnsignedSmart()) != 0) {
								location += incr2 - 1;
								int localX = (location >> 6 & 0x3f);
								int localY = (location & 0x3f);
								int plane = location >> 12;
								int objectData = landStream.getUnsignedByte();
								int type = objectData >> 2;
								int rotation = objectData & 0x3;
								if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
									continue;
								}
								int objectPlane = plane;
								if (mapSettings != null && (mapSettings[1][localX][localY] & 2) == 2) {
									objectPlane--;
								}
								if (objectPlane < 0 || objectPlane >= 4 || plane < 0 || plane >= 4) {
									continue;
								}
								region.addObject(new RS3Object(objectId, type, rotation, new Tile(localX + region.getRegionX(), localY + region.getRegionY(), objectPlane)), objectPlane, localX, localY);
							}
						}*/
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
								int direction = objectData & 0x3;
								if (x < 0 || x >= 64 || y < 0 || y >= 64) {
									continue;
								}
								if ((mapSettings[1][x][y] & 2) == 2) {
									z--;
								}
								if (z >= 0 && z <= 3) {
									//System.out.println("Object "+objectID+" found at x="+x+", y="+y+", z="+z);
									region.addObject(new RS3Object(objectID, type, direction, new Tile(x, y, z, region.getId())), z, x, y);
								}
							}
						}
					}
					region.setLoadingStage(RegionLoadingStage.DONE_LOADING);
					//System.out.println("Region "+region.getId()+" loaded successfully!");
					//System.out.println(Arrays.deepToString(region.getClippedRegionMap().getMasks()));
				default:
					break;
				}
			}
		});
	}
	
	public final ByteBuffer getVirtualMapData (int key) {
		try {
			return Launcher.getCache().read(CacheIndex.LANDSCAPES, key, 3);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final ByteBuffer getVirtualNodeData (int key) {
		try {
			return Launcher.getCache().read(CacheIndex.LANDSCAPES, key, 0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
