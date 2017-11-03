package de.essigt.light.mawebtomidi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.junit.Test;


public class MidiTest {

	@Test
	public void testMidi() throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
		Info[] midiDeviceInfo = MidiSystem.getMidiDeviceInfo();
		for(Info i : midiDeviceInfo) {
			System.out.println("Name: " + i.getName() + " Data: " + i.toString() );
		}
		
		ShortMessage myMsg = new ShortMessage();
		Receiver rcvr = MidiSystem.getReceiver();
		
		for(int i = 0; i< 128; i++) {
			myMsg.setMessage(ShortMessage.NOTE_ON, 0, 48, i);
			rcvr.send(myMsg, -1);
			Thread.sleep(500L);
		}
	}
}
