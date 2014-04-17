package org.virtue.cache.tools;

import java.io.IOException;

import javax.swing.JFrame;

import org.virtue.cache.def.ItemDefinition;

/**
 * @author Virtue Development Team 2014 (c).
 * @since Apr 17, 2014
 */
public class CacheEditor {

	private static JFrame frame = null;
	
	public static void main(String[] args) throws IOException {
		frame = new JFrame("CacheEditor");
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int i = 0;
		while (i < 14484) {
			ItemDefinition item = new ItemDefinition(i);
	        System.out.println("Item: name="+item.getName()+", equiptID="+item.equipID+", equiptSlot="+item.equipSlotID+", maleModel="+item.maleEquip1);
			i++;
		}
	}
	
}
