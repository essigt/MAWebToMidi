package de.essigt.light.mawebtomidi;

import static org.junit.Assert.fail;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.junit.Test;


public class MidiTest {

	@Test
	public void testMidiLaunchpadMini() throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
		Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
		
		MidiDevice device = null;
		for (Info i : midiDeviceInfo) {
			System.out.println("Name: " + i.getName() + " Data: " + i.toString());
			if(i.getName().contains("Launchpad")) {
				System.out.println("Found Launchpad");
				device = MidiSystem.getMidiDevice(i);
				
			}
		}
		if(device == null) {
			fail("No Launchpad found");
			return;
		}

		ShortMessage myMsg = new ShortMessage();
		Receiver rcvr = device.getReceiver();

		//Display color pallet
		for(int row = 0; row < 8 ; row++) {
			for(int i = 0; i < 8; i++) {
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, row*16 + i, row*8 + i);
				rcvr.send(myMsg, -1);
			}
		}

		
		//Let some lights blink
		while (true) {
			for (int i = 0; i < 4; i++) {
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, 48, i);
				rcvr.send(myMsg, -1);
				
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, 49, 16);
				rcvr.send(myMsg, -1);
				
				Thread.sleep(50L);
			}
			for (int i = 3; i >= 0; i--) {
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, 48, i);
				rcvr.send(myMsg, -1);
				
				myMsg.setMessage(ShortMessage.NOTE_ON, 0, 49, 0);
				rcvr.send(myMsg, -1);
				
				Thread.sleep(50L);
			}
		}
	}
}
