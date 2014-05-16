package org.virtue.game.logic.node.entity.player.identity;

import java.util.GregorianCalendar;

public class DateOfBirth {
	
	public GregorianCalendar dateofbirth;
	
	public DateOfBirth() {
		this.dateofbirth = new GregorianCalendar();
	}
	
	public DateOfBirth(GregorianCalendar birth) {
		this.dateofbirth = birth;
	}

	public GregorianCalendar getDateOfBirth() {
		return dateofbirth;
	}
	
	public void setDateOfBirth(GregorianCalendar dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	
}