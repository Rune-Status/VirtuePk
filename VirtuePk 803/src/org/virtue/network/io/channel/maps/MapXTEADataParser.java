package org.virtue.network.io.channel.maps;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.virtue.Launcher;
import org.virtue.network.io.IOHub;
import org.virtue.network.io.IOParser;

/**
 * @author Taylor
 * @version 1.0
 */
public class MapXTEADataParser implements IOParser<MapXTEAData, File> {
	
	/**
	 * Represents the size in bits of the XTEA data. (Per key).
	 */
	public static final int BIT_SIZE = 128;
	
	/**
	 * Represents the path.
	 */
	private static final String PACKED_PATH = "data/landscape/XTEA/packed.mcx";

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#load(java.nio.ByteBuffer)
	 */
	@Override
	public MapXTEAData load(File file) {
		try {
			init();
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
		return null;
	}

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#save()
	 */
	@Override
	public boolean save(Object... params) {
		try {
			throw new IllegalAccessException();
		} catch (IllegalAccessException e) {
			Launcher.getEngine().handleException(e);
		}
		return false;
	}
	
	/**
	 * Initializes the data parser.
	 */
	private void init() {
		if (new File(PACKED_PATH).exists()) {
			loadPackedKeys();
		} else {
			loadUnpackedKeys();
		}
	}

	/**
	 * Loads the packed data.
	 */
	private void loadPackedKeys() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int regionId = buffer.getShort() & 0xffff;
				int[] xteas = new int[4];
				for (int index = 0; index < 4; index++)
					xteas[index] = buffer.getInt();
				IOHub.getWorldMapIo().getMapXTEA().getData().put(regionId, xteas);
			}
			channel.close();
			in.close();
		} catch (Exception e) {
			Launcher.getEngine().handleException(e);
		}
	}

	/**
	 * Loads the unpacked data.
	 */
	private void loadUnpackedKeys() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(PACKED_PATH));
			File unpacked = new File("data/landscape/XTEA/unpacked/");
			File[] xteasFiles = unpacked.listFiles();
			for (File region : xteasFiles) {
				String name = region.getName();
				if (!name.contains(".txt")) {
					region.delete();
					continue;
				}
				int regionId = Short.parseShort(name.replace(".txt", ""));
				if (regionId <= 0) {
					region.delete();
					continue;
				}
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] xteas = new int[4];
				for (int index = 0; index < 4; index++) {
					xteas[index] = Integer.parseInt(in.readLine());
					out.writeInt(xteas[index]);
				}
				IOHub.getWorldMapIo().getMapXTEA().getData().put(regionId, xteas);
				in.close();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			Launcher.getEngine().handleException(e);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see org.virtue.network.io.IOParser#getPath()
	 */
	@Override
	public String getPath() {
		return "data/landscape/XTEA/";
	}

}
