/*
 * This file is part of RS3Emulator.
 *
 * RS3Emulator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RS3Emulator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RS3Emulator.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.virtue.utility;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * RS3Emulator
 * MachineData.java
 * 15/03/2014
 * @author Im Frizzy : Kyle Friz : <skype:kfriz1998>
 */
@SuppressWarnings("unused")
public class MachineData {
	
	private int os;
	private boolean x64Arch;
	private int osVersion;
	private int javaVendor;
	private int javaMainVersion;
	private int javaSubVersion;
	private int javaVersionBuild;
	private boolean hasApplet;
	private int heap;
	private int availableProcessors;
	private int ram;
	private int cpuClockFrequency;
	private int cpuInfo3;
	private int cpuInfo4;
	private int cpuInfo5;

	public MachineData (ChannelBuffer stream) {
		stream.readUnsignedByte();//Unknown (7)
		os = stream.readUnsignedByte();//Os type
		x64Arch = stream.readUnsignedByte() == 1;//Is 64 bit
		osVersion = stream.readUnsignedByte();//Os version type
		javaVendor = stream.readUnsignedByte();//Java vendor type (sun=1, microsoft=2, apple=3, other=4)
		javaMainVersion = stream.readUnsignedByte();//Main version number (eg 6 in 1.6.0_20)
		javaSubVersion = stream.readUnsignedByte();//Sub version number (eg 0 in 1.6.0_20)
		javaVersionBuild = stream.readUnsignedByte();//Build number (eg 20 in 1.6.0_20)
		hasApplet = stream.readUnsignedByte() == 1;//Whether the client has an applet
		heap = stream.readUnsignedShort();//The current max heap size
		availableProcessors = stream.readUnsignedByte();//The number of processors available
		ram = BufferUtils.read24bitInteger(stream);//Total computer ram
		cpuClockFrequency = stream.readUnsignedShort();
		cpuInfo3 = 0;//stream.readUnsignedByte();
		cpuInfo4 = 0;//stream.readUnsignedByte();
		cpuInfo5 = 0;//stream.readUnsignedByte();
		String empty1 = BufferUtils.readJagString(stream);//szDescription
		String empty2 = BufferUtils.readJagString(stream);//Always empty
		String dirextXVersion = BufferUtils.readJagString(stream);//dwDirectXVersion
		String empty4 = BufferUtils.readJagString(stream);//Always empty
		int unused1 = stream.readUnsignedByte();//szDriverDateEnglish
		int unused2 = stream.readUnsignedShort();//szDriverDateEnglish
		String empty5 = BufferUtils.readJagString(stream);//Some cpu info
		String empty6 = BufferUtils.readJagString(stream);//Some more cpu info
		int unused3 = stream.readUnsignedByte();//Cpu info 1
		int unused4 = stream.readUnsignedByte();//Cpu info 2
		for (int i=0;i<3;i++) {
			stream.readInt();//More stuff to do with Cpu
		}
		int unused5 = stream.readInt();//Cpu info 3
		String empty7 = BufferUtils.readJagString(stream);//Empty
	}
}
