package midi;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MidiWindow {
	
	JFrame jf;
	JPanel jp;
	Synthesizer synth;
	MidiChannel[] mcarray;
	boolean[] playing;
	boolean cmajor;
	JTextArea ta;
	JScrollPane sp;
	JTextField tf;
	JButton butt, increase, decrease, middleC;
	JLabel label, keys;
	Instrument[] inst;
	int basenote=60, octaveShift=0;
	int[] scale, keyorder;
	
	public MidiWindow(){
		
		jf = new JFrame("Music");
		jp = new JPanel();
		
		jp.setPreferredSize(new Dimension(510,410));
		jp.setFocusable(true);
		
		jf.setContentPane(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
	    
		playing = new boolean[8];
		setBooleanArray();
		
		scale = new int[32];
		makeScale();
		
		keyorder = new int[8];
		makeKeyOrder();
		
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			mcarray = synth.getChannels();
			synth.loadAllInstruments(synth.getDefaultSoundbank());
		} catch (MidiUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		inst = synth.getDefaultSoundbank().getInstruments();
		
		makeInstrumentTable();
		makeGUI();
		setBaseNote(60);
		makeScale();
		
		jf.setVisible(true);
		
		butt.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {				
				setInstrument(inst[Integer.parseInt(tf.getText())], mcarray[0]);
				jp.grabFocus();
			}
		});
		
		increase.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setBaseNote(basenote+1);
				makeScale();
			}
			
		});
		
		middleC.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setBaseNote(60);
				makeScale();
			}
			
		});
		
		decrease.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setBaseNote(basenote-1);
				makeScale();
			}
			
		});
		
		jp.addKeyListener(new KeyAdapter(){

			public void keyPressed(KeyEvent ev){
				if(ev.getKeyCode() == KeyEvent.VK_RIGHT){
					octaveShift++;
					makeKeyOrder();
				}
				if(ev.getKeyCode() == KeyEvent.VK_LEFT){
					octaveShift--;
					makeKeyOrder();
				}
				if(ev.getKeyCode() == KeyEvent.VK_A && !playing[0]){
					mcarray[0].noteOn(keyorder[0], 100);
					playing[0] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_S && !playing[1]){
					mcarray[0].noteOn(keyorder[1], 100);
					playing[1] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_D && !playing[2]){
					mcarray[0].noteOn(keyorder[2], 100);
					playing[2] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_F && !playing[3]){
					mcarray[0].noteOn(keyorder[3], 100);
					playing[3] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_G && !playing[4]){
					mcarray[0].noteOn(keyorder[4], 100);
					playing[4] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_H && !playing[5]){
					mcarray[0].noteOn(keyorder[5], 100);
					playing[5] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_J && !playing[6]){
					mcarray[0].noteOn(keyorder[6], 100);
					playing[6] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_K && !playing[7]){
					mcarray[0].noteOn(keyorder[7], 100);
					playing[7] = true;
				}
				if(ev.getKeyCode() == KeyEvent.VK_C && !cmajor){
					mcarray[0].noteOn(keyorder[0]-12, 100);
					mcarray[0].noteOn(keyorder[2]-12, 100);
					mcarray[0].noteOn(keyorder[4]-12, 100);
					cmajor = true;
					
				}
			}

			@Override
			public void keyReleased(KeyEvent ev) {
				if(ev.getKeyCode() == KeyEvent.VK_A){
					mcarray[0].noteOff(keyorder[0]);
					playing[0] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_S){
					mcarray[0].noteOff(keyorder[1]);
					playing[1] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_D){
					mcarray[0].noteOff(keyorder[2]);
					playing[2] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_F){
					mcarray[0].noteOff(keyorder[3]);
					playing[3] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_G){
					mcarray[0].noteOff(keyorder[4]);
					playing[4] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_H){
					mcarray[0].noteOff(keyorder[5]);
					playing[5] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_J){
					mcarray[0].noteOff(keyorder[6]);
					playing[6] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_K){
					mcarray[0].noteOff(keyorder[7]);
					playing[7] = false;
				}
				if(ev.getKeyCode() == KeyEvent.VK_C){
					mcarray[0].noteOff(keyorder[0]-12);
					mcarray[0].noteOff(keyorder[2]-12);
					mcarray[0].noteOff(keyorder[4]-12);
					cmajor = false;
				}
			}

		});
		
	}
	
	private void makeKeyOrder() {
		for(int c=0;c<keyorder.length;c++){
			keyorder[c] = scale[c+octaveShift];
		} 
	}

	private void setBaseNote(int i) {
		basenote = i;	
	}

	private void makeScale() {
		for(int i=0;i<(scale.length/8)-1;i++){
			scale[0+(i*12)] = basenote + (i * 12) + 0;
			scale[1+(i*12)] = basenote + (i * 12) + 2;
			scale[2+(i*12)] = basenote + (i * 12) + 4;
			scale[3+(i*12)] = basenote + (i * 12) + 5;
			scale[4+(i*12)] = basenote + (i * 12) + 7;
			scale[5+(i*12)] = basenote + (i * 12) + 9;
			scale[6+(i*12)] = basenote + (i * 12) + 11;
			scale[7+(i*12)] = basenote + (i * 12) + 12;
		}
	}

	private void makeInstrumentTable() {
		
		ta = new JTextArea();
		ta.setTabSize(12);
		ta.append("Instrument\tNumber\n");
		ta.setEditable(false);
		
		for(int i = 0;i<inst.length;i++){
			ta.append(inst[i].getName()+"\t"+i+"\n");
		}
		
		sp = new JScrollPane(ta);
		sp.setPreferredSize(new Dimension(260,350));
		
		jp.add(sp);
		
		sp.setFocusable(false);
		jf.setFocusable(false);
		ta.setFocusable(false);
		
	}
	
	private void makeGUI(){
		tf = new JTextField(5);
		
		label = new JLabel("Instrument Number:");
		label.setFocusable(false);
		
		butt = new JButton("OK");
		butt.setFocusable(false);
		
		increase = new JButton(">");
		increase.setFocusable(false);
		
		decrease = new JButton("<");
		decrease.setFocusable(false);
		
		middleC = new JButton("Middle C");
		middleC.setFocusable(false);
		
		jp.add(label);
		jp.add(tf);
		jp.add(butt);
		jp.add(decrease);
		jp.add(middleC);
		jp.add(increase);
	}

	private void setInstrument(Instrument in, MidiChannel mc){
		mc.programChange(in.getPatch().getProgram());
	}
	
	private void setBooleanArray(){
		for(int i = 0;i<playing.length;i++){
			playing[i] = false;
		}
	}
}
