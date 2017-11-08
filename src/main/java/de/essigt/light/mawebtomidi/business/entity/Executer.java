package de.essigt.light.mawebtomidi.business.entity;


/**
 * Representing an executer
 * 
 * @author essigt
 *
 */
public class Executer {

	private int id;
	private String name;
	private boolean running;


	public Executer() {
	}


	public Executer(int id, String name, boolean running) {
		this.id = id;
		this.name = name;
		this.running = running;
	}


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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Executer other = (Executer) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Executer [id=" + id + ", name=" + name + ", running=" + running + "]";
	}


	

}
