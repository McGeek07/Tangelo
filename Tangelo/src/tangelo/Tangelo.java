package tangelo;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Tangelo {
	public static final Version
		VERSION = new Version("Tangelo", 1, 0, 1);
	public static final Font
		FONT = new Font("Monospaced", Font.PLAIN, 20);
	public static final int
		WINDOW_W = 640,
		WINDOW_H = 480;
	public static final String
		ENDL = System.lineSeparator();
	
	private static final char[]
		ABC = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '.'
		},
		KEY = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ', '.'
		};
	private static final int
		SIZE = 28;
	
	private static final JFrame
		window = new JFrame(VERSION.toString());
	private static final MenuBar
		mb = new MenuBar();
	private static final Menu
		m1 = new Menu("File"),
		m2 = new Menu("Edit");
	private static final MenuItem
		m11 = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O)),
		m12 = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S)),
		m21 = new MenuItem("Seed Serial Key", new MenuShortcut(KeyEvent.VK_S, true)),
		m22 = new MenuItem("Seed Random Key", new MenuShortcut(KeyEvent.VK_R, true)),
		m23 = new MenuItem("Encode/Decode", new MenuShortcut(KeyEvent.VK_ENTER));
	private static final JPanel
		panel = new JPanel(),
		keyPanel = new JPanel(),
		txtPanel = new JPanel();
	private static final Label
		keyLabel = new Label(" KEY:");
	private static final TextField
		keyInput = new TextField(key());
	private static final TextArea
		txtInput = new TextArea();
	private static final JFileChooser
		jfc = new JFileChooser();
	private static final Random
		random = new Random();
		
	
	public static void main(String[] args) {		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		mb.setFont(FONT);
		mb.add(m1);
			m1.add(m11);
			m1.add(m12);
		mb.add(m2);
			m2.add(m21);
			m2.add(m22);
			m2.addSeparator();
			m2.add(m23);
		window.setMenuBar(mb);
		
		m11.addActionListener((ae) -> {
			open();
		});
		m12.addActionListener((ae) -> {
			save();
		});
		m21.addActionListener((ae) -> {
			seedSerialKey();
		});
		m22.addActionListener((ae) -> {
			seedRandomKey();
		});
		m23.addActionListener((ae) -> {
			toggle();
		});
		
		panel.setLayout(new BorderLayout());
		keyPanel.setLayout(new BorderLayout());
		txtPanel.setLayout(new BorderLayout());
		
		keyLabel.setFont(FONT);
		keyInput.setFont(FONT);
		keyInput.setEditable(false);
		keyPanel.add(keyLabel, BorderLayout.WEST );
		keyPanel.add(keyInput, BorderLayout.CENTER);

		txtInput.setFont(FONT);
		txtPanel.add(txtInput, BorderLayout.CENTER);
		
		panel.add(keyPanel, BorderLayout.SOUTH );
		panel.add(txtPanel, BorderLayout.CENTER);
		window.add(panel);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(WINDOW_W, WINDOW_H);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	public static void open() {
		File file = null;
		if(jfc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
			file = jfc.getSelectedFile();
		if(file != null)
			open(file);
	}
	
	public static void open(File file) {
		LinkedList<String> list = new LinkedList<>();
		StringBuilder txt = new StringBuilder();
		parseFromFile(file, list);
		
		for(String line: list)
			txt.append(line + ENDL);
		txtInput.setText(txt.toString());
	}
	
	private static void save() {
		File file = null;
		if(jfc.showSaveDialog(window) == JFileChooser.APPROVE_OPTION)
			file = jfc.getSelectedFile();
		if(file != null)
			save(file);
	}
	
	public static void save(File file) {
		LinkedList<String> list = new LinkedList<>();
		list.add(txtInput.getText());
		printToFile(file, false, list);
	}
	
	public static void seedSerialKey() {
		String seed = JOptionPane.showInputDialog(window, "Seed", "Seed Serial Key", JOptionPane.QUESTION_MESSAGE);
		if(seed != null)
			seedSerialKey(seed);
	}
	
	public static void seedSerialKey(String seed) {
		seed = seed.toUpperCase();
		
		List<Character> abc = new ArrayList<>();
		for(int i = 0; i < SIZE; i ++)
			abc.add(ABC[i]);
		int
			i = 0;
		for(int j = 0; j < seed.length() && i < SIZE; j ++) {
			char c = seed.charAt(j);
			int  k = -1;
			for(int l = i; l < SIZE; l ++)
				if(abc.get(l) == c) {
					k = l;
					break;
				}
			if(k >= 0) {
				char d = abc.get(k);
				KEY[i ++] = d;
				abc.remove(k);
				abc.add(0, d);
			}			
		}		
		for(int l = i; l < SIZE; l ++)
			KEY[l] = abc.get(l);
		keyInput.setText(key());
	}
	
	public static void seedRandomKey() {
		String seed = JOptionPane.showInputDialog(window, "Seed", "Seed Random Key", JOptionPane.QUESTION_MESSAGE);
		if(seed != null)
			seedRandomKey(seed);
	}
	
	public static void seedRandomKey(String seed) {
		seed = seed.toUpperCase();
		
		List<Character> abc = new ArrayList<>();
		for(int i = 0; i < SIZE; i ++)
			abc.add(ABC[i]);
		int
			i = 0;
		for(int j = 0; j < seed.length() && i < SIZE; j ++) {
			char c = seed.charAt(j);
			int  k = -1;
			for(int l = i; l < SIZE; l ++)
				if(abc.get(l) == c) {
					k = l;
					break;
				}
			if(k >= 0) {
				char d = abc.get(k);
				KEY[i ++] = d;
				abc.remove(k);
				abc.add(0, d);
			}			
		}		
		for(int l = i; l < SIZE; l ++) {
			int k = l + random.nextInt(SIZE - l);
			char d = abc.get(k);
			KEY[i ++] = d;
			abc.remove(k);
			abc.add(0, d);
		}
		keyInput.setText(key());
	}
	
	public static void toggle() {
		String txt = txtInput.getText().toUpperCase();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < txt.length(); i ++)
			sb.append(toggle(txt.charAt(i)));
		txtInput.setText(sb.toString());
	}
	
	public static char toggle(char c) {
		int i = -1;
		for(int j = 0; j < SIZE; j ++)
			if(KEY[j] == c) {
				i = j;
				break;
			}
		if(i >= 0)
			return KEY[(i + SIZE / 2) % SIZE];
		return c;
	}
	
	public static String key() {
		StringBuilder key = new StringBuilder();
		for(int i = 0; i < SIZE; i ++)
			key.append(KEY[i]);
		return key.toString();
	}
	
	public static File validate(File file) {
		if(!file.exists())
			try {
				if(file.getParentFile() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
			} catch(IOException ioe) {
				System.err.println("Unable to validate file \"" + file + "\"");
				ioe.printStackTrace();
			}
		return file;
	}
	
	public static ObjectOutputStream createObjectOutputStream(String path, boolean append) {
		return createObjectOutputStream(new File(path), append);
	}
	
	public static ObjectOutputStream createObjectOutputStream(File file  , boolean append) {
		try {
			return new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(validate(file), append)));
		} catch (IOException ioe) {
			System.err.println("Unable to open file \"" + file + "\"");
		}
		return null;
	}
	
	public static ObjectInputStream createObjectInputStream(String path) {
		return createObjectInputStream(new File(path));
	}
	
	public static ObjectInputStream createObjectInputStream(File file  ) {
		try {
			return new ObjectInputStream(new BufferedInputStream(new FileInputStream(validate(file))));
		} catch(IOException ioe) {
			System.err.println("Unable to open file \"" + file + "\"");
		}
		return null;
	}
	
	public static BufferedWriter createBufferedWriter(String path, boolean append) {
		return createBufferedWriter(new File(path), append);
	}
	
	public static BufferedWriter createBufferedWriter(File file  , boolean append) {
		try {
			return new BufferedWriter(new FileWriter(validate(file), append));
		} catch(IOException ioe) {
			System.err.println("Unable to open file \"" + file + "\"");
		}
		return null;
	}
	
	public static PrintWriter createPrintWriter(String path, boolean append) {
		return createPrintWriter(new File(path), append);
	}
	
	public static PrintWriter createPrintWriter(File file  , boolean append) {
		try {
			return new PrintWriter(new BufferedWriter(new FileWriter(validate(file), append)));
		} catch(IOException ioe) {
			System.err.println("Unable to open file \"" + file + "\"");
		}
		return null;
	}
	
	public static BufferedReader createBufferedReader(String path) {
		return createBufferedReader(new File(path));
	}
	
	public static BufferedReader createBufferedReader(File file  ) {
		try {
			return new BufferedReader(new FileReader(validate(file)));
		} catch(IOException ioe) {
			System.err.println("Unable to open file \"" + file + "\"");
		}
		return null;
	}
	
	public static <T> void writeToFile(String path, boolean append, Iterable<T> list) {
		writeToFile(new File(path), append, list);
	}
	
	public static <T> void writeToFile(File file  , boolean append, Iterable<T> list) {
		try(ObjectOutputStream out = createObjectOutputStream(file, append)) {
			out.writeObject(list);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static <T> List<T> readFromFile(String path, List<T> list) {
		return readFromFile(new File(path), list);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> readFromFile(File file  , List<T> list) {
		try(ObjectInputStream in = createObjectInputStream(file)) {
			Object o = in.readObject();
			if(o instanceof Iterable)
				for(T t: (Iterable<T>)o)
					list.add(t);
			else
				for(T t: (T[])o)
					list.add(t);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public static <T> void printToFile(String path, boolean append, Iterable<T> list) {
		printToFile(new File(path), append, list);
	}
	
	public static <T> void printToFile(File file  , boolean append, Iterable<T> list) {
		try(BufferedWriter out = createBufferedWriter(file, append)) {
			for(T t: list)
				out.write(String.format(t + "%n"));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static List<String> parseFromFile(String path, List<String> list) {
		return parseFromFile(new File(path), list);
	}
	
	public static List<String> parseFromFile(File file  , List<String> list) {
		try(BufferedReader in = createBufferedReader(file)) {
			while(in.ready()) list.add(in.readLine());
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return list;
	}
	
	public static void clearFile(String path) {
		clearFile(new File(path));
	}
	
	public static void clearFile(File file  ) {
		try(BufferedWriter out = createBufferedWriter(file, false)) {
			out.write("");
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}	

	public static class Version {
		public final String
			VERSION_NAME;
		public final int
			VERSION_ID,
			RELEASE_ID,
			PATCH_ID;
		
		public Version(
				String version_name,
				int version_id,
				int release_id,
				int patch_id
				) {
			this.VERSION_NAME = version_name;
			this.VERSION_ID = version_id;
			this.RELEASE_ID = release_id;
			this.PATCH_ID = patch_id;
		}
		
		@Override
		public String toString() {
			return 
					this.VERSION_NAME + " " + 
					this.VERSION_ID + "." + 
					this.RELEASE_ID + "." + 
					this.PATCH_ID;
		}
	}
}
