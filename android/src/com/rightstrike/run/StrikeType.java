package com.rightstrike.run;

public enum StrikeType {

	HEEL, FRONT;

	@Override
	public String toString() {
		if (this == HEEL) {
			return "H";
		} else if (this == FRONT) {
			return "F";
		} else {
			return "\0";
		}
	}
}
