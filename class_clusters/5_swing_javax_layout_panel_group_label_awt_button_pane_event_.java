package projetolp2.hotelriviera;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Toolkit;


public class TelaCheckin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaCheckin frame = new TelaCheckin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaCheckin() {
		setResizable(false);
		setTitle("Hotel Riviera - Sistema de Manuten\u00E7\u00E3o de Clientes e Servi\u00E7os - Check-in");
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Pedro Paulo\\workspace\\hotelriviera\\Media\\icone_janela.png"));
		setForeground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setBounds(100, 100, 1204, 766);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setToolTipText("Oi");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBackground(new Color(188, 143, 143));
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Pedro Paulo\\workspace\\hotelriviera\\Media\\botao_voltaraoinicio.png"));
		btnNewButton.setForeground(new Color(165, 42, 42));
		btnNewButton.setSelectedIcon(new ImageIcon("C:\\Users\\Pedro Paulo\\workspace\\hotelriviera\\Media\\botao_voltaraoinicio.png"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaInicial.main(null);
			}
		});
		btnNewButton.setBounds(45, 639, 168, 74);
		contentPane.add(btnNewButton);
		
		JLabel background = new JLabel("Hotel Riviera");
		background.setBounds(0, 0, 1241, 739);
		background.setIcon(new ImageIcon("C:\\Users\\Pedro Paulo\\workspace\\hotelriviera\\Media\\background_checkin.png"));
		background.setToolTipText("");
		background.setLabelFor(this);
		background.setBackground(Color.BLACK);
		contentPane.add(background);
	}

}


--------------------

package osmo.tester.gui.manualdrive;

import osmo.tester.model.data.SearchableInput;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Base GUI for variable values.
 *
 * @author Teemu Kanstren
 */
public abstract class ValueGUI extends JDialog {
  /** The input variable the this GUI is for. */
  protected final SearchableInput input;
  /** The value to provide next. */
  protected Object value = null;
  /** A hack to prevent calling SearchableInputObserver twice if automated (skip) option is chosen. */
  private boolean observed = false;

  public ValueGUI(SearchableInput input) throws HeadlessException {
    this.input = input;
    setTitle(input.getName());
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setModalityType(ModalityType.APPLICATION_MODAL);
    setLayout(new BorderLayout());
    Container pane = getContentPane();
    pane.add(new JLabel(createValueLabel()), BorderLayout.NORTH);
    pane.add(createValueComponent(), BorderLayout.CENTER);
    JButton ok = new JButton("OK");
    ok.addActionListener(e -> {
      value = value();
      if (value == null) {
        //invalid input observed
        return;
      }
      setVisible(false);
      synchronized (ValueGUI.this) {
        ValueGUI.this.notify();
      }
    });
    JButton skip = new JButton("Skip");
    skip.addActionListener(e -> {
      //setting value to null should cause the underlying object to generate the value
      value = null;
      setVisible(false);
      observed = true;
      synchronized (ValueGUI.this) {
        ValueGUI.this.notify();
      }
    });
    JButton auto = new JButton("Auto");
    auto.addActionListener(e -> {
      ValueGUI.this.input.disableGUI();
      //setting value to null should cause the underlying object to generate the value
      value = null;
      setVisible(false);
      observed = true;
      synchronized (ValueGUI.this) {
        ValueGUI.this.notify();
      }
    });
    JPanel panel = new JPanel(new FlowLayout());
    panel.add(ok);
    panel.add(skip);
    panel.add(auto);
    pane.add(panel, BorderLayout.SOUTH);
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Should create the label for the window to describe what value is requested.
   *
   * @return The label.
   */
  protected abstract String createValueLabel();

  /**
   * Should create the JComponent to request the value from the user.
   *
   * @return The JComponent to get the value.
   */
  protected abstract JComponent createValueComponent();

  /**
   * Gives the value for variable as parsed from user input. If this returns null, it is considered an
   * invalid value and the GUI is left open to request a new valid value.
   *
   * @return The defined value.
   */
  protected abstract Object value();

  /**
   * Provides the next value for the associated input.
   *
   * @return The next defined input value.
   */
  public Object next() {
    observed = false;
    setVisible(true);
    return value;
  }

  /** Enables the Nimbus look and feel. */
  public static void setNimbus() {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      // If Nimbus is not available, you can set the GUI to another look and feel.
    }
  }
}

--------------------

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;


public class AddDirector extends JFrame {

	private JPanel contentPane;
	private JTextField directorName;
	private JTextField directorGender;
	private JButton directorCancel;
	private JButton directorSubmit;

	public AddDirector() {
		setTitle("Add Director");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("Director Name");
		
		directorName = new JTextField();
		directorName.setColumns(10);
		
		JLabel lblGenderMf = new JLabel("Gender M/F");
		
		directorGender = new JTextField();
		directorGender.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(new Color(246, 221, 185));
		
		directorSubmit = new JButton("Submit");
		directorSubmit.setIcon(new ImageIcon(AddDirector.class.getResource("/res/OK.gif")));
		directorSubmit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (directorName.getText().isEmpty() || directorGender.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "All fields are necessary", "Error", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(AddDirector.class.getResource("/res/Alert.gif")));

				}
				else {
					MovieDatabase d3 = new MovieDatabase();
					d3.addDirector(directorName.getText(), directorGender.getText());
					JOptionPane.showMessageDialog(null, "Thank You!", "Add director", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(AddDirector.class.getResource("/res/OK.gif")));

				}
			}
			
		});
		
		directorCancel = new JButton("Cancel");
		directorCancel.setIcon(new ImageIcon(AddDirector.class.getResource("/res/Close.gif")));
		
		directorCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
			}
			
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 310, Short.MAX_VALUE)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addComponent(directorSubmit, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
					.addGap(35)
					.addComponent(directorCancel, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 71, Short.MAX_VALUE)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(14)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(directorCancel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(directorSubmit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(31)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblGenderMf))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(directorName, GroupLayout.PREFERRED_SIZE, 442, GroupLayout.PREFERRED_SIZE)
								.addComponent(directorGender, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(230)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(208, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(36)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(directorName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblGenderMf)
						.addComponent(directorGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 270, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
		setVisible(true);

	}

}

--------------------

