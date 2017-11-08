package de.essigt.light.mawebtomidi.business.entity;


public class ExecuterConfiguration {

	private int id;
	private String name;
	private boolean running;

	private int midiOnNote;
	private int midiOnValue;

	private int midiOffNote;
	private int midiOffValue;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}


	public int getMidiOnNote() {
		return midiOnNote;
	}


	public void setMidiOnNote(int midiOnNote) {
		this.midiOnNote = midiOnNote;
	}


	public int getMidiOnValue() {
		return midiOnValue;
	}


	public void setMidiOnValue(int midiOnValue) {
		this.midiOnValue = midiOnValue;
	}


	public int getMidiOffNote() {
		return midiOffNote;
	}


	public void setMidiOffNote(int midiOffNote) {
		this.midiOffNote = midiOffNote;
	}


	public int getMidiOffValue() {
		return midiOffValue;
	}


	public void setMidiOffValue(int midiOffValue) {
		this.midiOffValue = midiOffValue;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecuterConfiguration other = (ExecuterConfiguration) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ExecuterConfiguration [id=" + id + ", name=" + name + ", midiOnNote=" + midiOnNote + ", midiOnValue=" + midiOnValue + ", midiOffNote=" + midiOffNote + ", midiOffValue=" + midiOffValue + "]";
	}


}
