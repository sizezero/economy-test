package org.kleemann.economy_test;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.kleemann.economy_test.swing.MapComponent;

public class Main {

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Economy Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent comp = new MapComponent();
		Dimension d = new Dimension(600, 400);
		comp.setMinimumSize(d);
		comp.setPreferredSize(d);
		frame.getContentPane().add(comp);

		// Display the window.
		frame.setSize(new Dimension(600, 400));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
}