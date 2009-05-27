package org.cipres.treebase.migration;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Insert the class description here.
 * 
 * @pattern Application
 * 
 * @generatedBy CodePro at 6/22/06 5:16 PM
 * 
 * @author Jin Ruan
 * 
 * @version $Revision$
 */
public class TBDataMigrationGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel ivjJFrameContentPane;
	private JPanel ivjApplicationPane;
	private JPanel ivjMesquitePane;
	private JPanel ivjPanelHelp;

	private JFileChooser mFileChooser;

	private TBDataMigrationController mController;

	/**
	 * Construct an instance of the TBDataMigrationGUI application.
	 */
	public TBDataMigrationGUI() {
		super();
		initialize();
	}

	/**
	 * Construct an instance of the TBDataMigrationGUI application.
	 * 
	 * @param title the title of this application
	 */
	public TBDataMigrationGUI(String title) {
		super(title);
		initialize();
	}

	/**
	 * Return the FileChooser field. Uses lazy initialization.
	 * 
	 * @return JFileChooser mFileChooser
	 */
	private JFileChooser getFileChooser() {
		if (mFileChooser == null) {
			mFileChooser = new JFileChooser();

			mFileChooser.setDialogTitle("Choose File");
			mFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			mFileChooser.setCurrentDirectory(new File("C:\\Awork\\treebase data\\"));
		}
		return mFileChooser;
	}

	/**
	 * Return the Controller field. Uses lazy initialization.
	 * 
	 * @return TBDataMigrationController mController
	 */
	private TBDataMigrationController getController() {
		if (mController == null) {
			mController = new TBDataMigrationController();

			// mController.setView(this);
		}
		return mController;
	}

	/**
	 * Return the JFrameContentPane property value.
	 * <p>
	 * WARNING: THIS METHOD WILL BE REGENERATED.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			try {
				ivjJFrameContentPane = new javax.swing.JPanel();
				ivjJFrameContentPane.setName("JFrameContentPane");
//				ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
//				getJFrameContentPane().add(getApplicationPane(), BorderLayout.CENTER);
//				getJFrameContentPane().add(getPanelHelp(), BorderLayout.SOUTH);
				ivjJFrameContentPane.setLayout(new BoxLayout(ivjJFrameContentPane, BoxLayout.PAGE_AXIS));
				getJFrameContentPane().add(getApplicationPane());
				ivjJFrameContentPane.add(new JLabel("Mesquite Nexus Parser Test"));
				ivjJFrameContentPane.add(getMesquitePane());
				getJFrameContentPane().add(getPanelHelp());

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Return the ivjPanelHelp field. Uses lazy initialization.
	 * 
	 * @return JPanel
	 */
	private JPanel getPanelHelp() {
		if (ivjPanelHelp == null) {
			ivjPanelHelp = new JPanel();
			ivjPanelHelp.setLayout(new FlowLayout());

			// help:
			JButton btnHelp = new JButton("Help");
			btnHelp.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleHelp();
				}

			});
			ivjPanelHelp.add(btnHelp);

		}
		return ivjPanelHelp;
	}

	/**
	 * Return the ivjApplicationPane property value.
	 * <p>
	 * WARNING: THIS METHOD WILL BE REGENERATED.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getApplicationPane() {
		if (ivjApplicationPane == null) {
			ivjApplicationPane = new javax.swing.JPanel();
			ivjApplicationPane.setName("ApplicationPane");
			ivjApplicationPane.setLayout(new FlowLayout());

			// load citation:
			JButton btnCitation = new JButton("Load Citation Endnote File");
			btnCitation.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadCitation();
				}

			});
			ivjApplicationPane.add(btnCitation);

			// test citation:
			JButton btnCitationTest = new JButton("Test Citations");
			btnCitationTest.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleTestCitation();
				}

			});
			ivjApplicationPane.add(btnCitationTest);

			// load study
			JButton btnStudy = new JButton("load study with id only");
			btnStudy.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadStudy();
				}

			});
			ivjApplicationPane.add(btnStudy);

			// load study
			JButton btnCitationAuthor = new JButton("Set citation authors");
			btnCitationAuthor.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleSetCitationAuthors();
				}

			});
			ivjApplicationPane.add(btnCitationAuthor);

			// test study
			JButton btnStudyTest = new JButton("Test studies");
			btnStudyTest.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleTestStudy();
				}

			});
			ivjApplicationPane.add(btnStudyTest);

			// load Analysis
			JButton btnAnalysis = new JButton("load analysis");
			btnAnalysis.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadAnalysis();
				}

			});
			ivjApplicationPane.add(btnAnalysis);

			// test Analysis
			JButton btnAnalysisTest = new JButton("Test loaded analysis");
			btnAnalysisTest.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleTestAnalysis();
				}

			});
			ivjApplicationPane.add(btnAnalysisTest);

			// load Matrix-Analysis
			JButton btnMatrixAnalysis = new JButton("load matrix-analysis");
			btnMatrixAnalysis.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadMatrixAnalysis();
				}

			});
			ivjApplicationPane.add(btnMatrixAnalysis);

			// Set Matrix names
			JButton btnMatrixName = new JButton("Set matrix names");
			btnMatrixName.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleSetMatrixName();
				}

			});
			ivjApplicationPane.add(btnMatrixName);

			// Load Nexus
			JButton btnLoadNexus = new JButton("Load Nexus Files in a directory");
			btnLoadNexus.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadNexus();
				}

			});
			ivjApplicationPane.add(btnLoadNexus);

		}
		return ivjApplicationPane;
	}

	/**
	 * Return the ivjMesquitePane property value.
	 * <p>
	 * WARNING: THIS METHOD WILL BE REGENERATED.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getMesquitePane() {
		if (ivjMesquitePane == null) {
			ivjMesquitePane = new javax.swing.JPanel();
			ivjMesquitePane.setName("MesquitePane");
			ivjMesquitePane.setLayout(new FlowLayout());

			// load Nexus file:
			JButton btnLoadNexus = new JButton("Load Nexus Files for Mesquite");
			btnLoadNexus.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent pE) {
					handleLoadNexusMesquite();
				}

			});
			ivjMesquitePane.add(btnLoadNexus);

		}
		return ivjMesquitePane;
	}

	/**
	 * 
	 */
	protected void handleSetCitationAuthors() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadCitationAuthor(f);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadNexus() {
		File directory = getSelectedDirectory();
		if (directory != null) {
			getController().processLoadNexus(directory);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadNexusMesquite() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadNexusMesquite(f);
		}
	}

	/**
	 * 
	 */
	protected void handleSetMatrixName() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processSetMatrixName(f);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadCitation() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadCitation(f);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadStudy() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadStudy(f);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadAnalysis() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadAnalysis(f);
		}
	}

	/**
	 * 
	 */
	protected void handleLoadMatrixAnalysis() {
		File f = getSelectedFile();
		if (f != null) {
			getController().processLoadMatrixAnalysis(f);
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private File getSelectedFile() {

		JFileChooser fileChooser = getFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fileChooser.showOpenDialog(getApplicationPane()) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	private File getSelectedDirectory() {

		JFileChooser fileChooser = getFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (fileChooser.showOpenDialog(getApplicationPane()) == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	/**
	 * 
	 */
	protected void handleTestCitation() {
		getController().processTestCitation();
	}

	/**
	 * 
	 */
	protected void handleTestStudy() {
		getController().processTestStudy();
	}

	/**
	 * 
	 */
	protected void handleTestAnalysis() {
		getController().processTestAnalysis();
	}

	/**
	 * 
	 */
	protected void handleHelp() {
	// TODO: handleHelp

	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
	}

	/**
	 * Initialize the class.
	 * <p>
	 * WARNING: THIS METHOD WILL BE REGENERATED.
	 */
	private void initialize() {
		setName("Application");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(660, 600);
		setTitle("Application Title");
		setContentPane(getJFrameContentPane());

		/* Add a windowListener for the windowClosedEvent */
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			};
		});

	}

	/**
	 * Starts the application.
	 * 
	 * @param args an array of command-line arguments
	 */
	public static void main(String[] args) {
		try {
			/* Set native look and feel */
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			/* Create the frame */
			TBDataMigrationGUI aTBDataMigrationGUI = new TBDataMigrationGUI(
				"TreeBASE I Data Migration");

			aTBDataMigrationGUI.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of TBDataMigrationGUI");
			exception.printStackTrace(System.out);
		}
	}
}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.applicationPattern strategyId =
 * com.instantiations.assist.eclipse.pattern.applicationPattern.application appletStyle = &Swing
 * based appletStyle.index = 0 applicationClass = TBDataMigrationGUI awtSuperclassType =
 * java.awt.Frame centerFrame = false comment = Insert the class description here. createMetaData =
 * false eventHandling = Use one &inner class for all events eventHandling.index = 1 package =
 * org.cipres.treebase.migration package.sourceFolder = treebase-core/src/test/java
 * swingSuperclassType = javax.swing.JFrame
 */