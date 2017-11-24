package de.essigt.light.mawebtomidi.business.boundary;

import java.util.ArrayList;
import java.util.List;

import de.essigt.light.mawebtomidi.business.entity.Executer;
import de.essigt.light.mawebtomidi.business.entity.ExecuterConfiguration;
import de.essigt.light.mawebtomidi.business.entity.ExecuterModel;

/**
 * Manages the state of all executers
 * @author essigt
 *
 */
public class ExecuterManager {
	
	private List<ExecuterConfiguration> execConfigs = new ArrayList<>();
	private ExecuterModel model = new ExecuterModel();

	private static ExecuterManager instance;
	
	
	private ExecuterManager() {
	}
	
	public static ExecuterManager getInstance() {
		if(instance == null) {
			instance = new ExecuterManager();
			
			//Add dummy configuration
			ExecuterConfiguration configuration = new ExecuterConfiguration();
			configuration.setId(127);
			configuration.setMidiOnNote(48);
			configuration.setMidiOnValue(3);
			configuration.setMidiOffNote(48);
			configuration.setMidiOffValue(1);
			instance.addExecuterConifguration( configuration );
		}
		
		return instance;
	}
	 

	/**
	 * 
	 * @param conf
	 */
	public void addExecuterConifguration(ExecuterConfiguration conf) {
		execConfigs.add(conf);
	}


	/**
	 * 
	 * @param id
	 * @return
	 */
	public boolean containsConfiguration(int id) {
		for(ExecuterConfiguration conf : execConfigs) {
			if(conf.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param exec
	 */
	public void updateExecuter(Executer exec) {
		ExecuterConfiguration executerConfiguration = getExecuterConfiguration(exec.getId());
		if(executerConfiguration == null) {
			return;
		}
		
		executerConfiguration.setName(exec.getName());
		executerConfiguration.setRunning(exec.isRunning());
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 */
	private ExecuterConfiguration getExecuterConfiguration(int id) {
		for(ExecuterConfiguration conf : execConfigs) {
			if(conf.getId() == id) {
				return conf;
			}
		}
		return null;
	}
}
