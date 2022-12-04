import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class ventanaCalculadora extends JFrame {
	public JTextField octets[] = new JTextField[4];
	public JTextField octetsBinari[] = new JTextField[4];
	public JTextField octetsMascara[] = new JTextField[4];
	public JTextField octetsXarxa[] = new JTextField[4];
	public JTextField mascara = new JTextField(2);
	public JTextField errors = new JTextField(20);

	public ventanaCalculadora() {
		super("Calculadora binaria");
		setSize(900, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridLayout gl = new GridLayout(4, 2);
		gl.setHgap(2);
		gl.setVgap(2);

		JPanel panelIP = new JPanel();
		panelIP.setLayout(new FlowLayout());
		JPanel panelBinaria = new JPanel();
		panelBinaria.setLayout(new FlowLayout());
		JPanel panelMascara = new JPanel();
		panelMascara.setLayout(new FlowLayout());
		JPanel panelXarxa = new JPanel();
		panelXarxa.setLayout(new FlowLayout());

		JLabel etiquetaIP = new JLabel("Ip decimal: ");
		JLabel etiquetaBinaria = new JLabel("Ip binara: ");
		JLabel etiquetaMascara = new JLabel("Màscara de sub-xarxa: ");
		JLabel etiquetaXarxa = new JLabel("Xarxa: ");

		JPanel panelCentral = new JPanel();
		panelCentral.setLayout(gl);

		panelCentral.add(etiquetaIP);
		omplirArrays(octets, panelIP);
		panelIP.add(new JLabel("/"));
		panelIP.add(mascara);
		panelCentral.add(panelIP);
		panelCentral.add(etiquetaBinaria);
		omplirArraysNoEditables(octetsBinari, panelBinaria);
		panelCentral.add(panelBinaria);
		panelCentral.add(etiquetaMascara);
		omplirArraysNoEditables(octetsMascara, panelMascara);
		panelCentral.add(panelMascara);
		panelCentral.add(etiquetaXarxa);
		omplirArraysNoEditables(octetsXarxa, panelXarxa);
		panelCentral.add(panelXarxa);

		JButton boto = new JButton("Calcular");
		boto.addActionListener(new EventCalcularBoto());
		JPanel panelBoto = new JPanel();
		panelBoto.setLayout(new FlowLayout());
		panelBoto.add(boto);
		
		
		errors.setEditable(false);
		JPanel panelErrors = new JPanel();
		panelErrors.add(errors);
		panelErrors.setLayout(new FlowLayout());
		

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(panelCentral, BorderLayout.NORTH);
		cp.add(panelBoto, BorderLayout.CENTER);
		cp.add(panelErrors, BorderLayout.SOUTH);

	}

	private void omplirArrays(JTextField[] array, JPanel panel) {

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				panel.add(new JLabel("."));
			}
			array[i] = new JTextField(4);
			panel.add(array[i]);
		}
	}

	private void omplirArraysNoEditables(JTextField[] array, JPanel panel) {

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				panel.add(new JLabel("."));
			}
			array[i] = new JTextField(8);
			panel.add(array[i]);
			array[i].setEditable(false);
		}
	}

	class EventCalcularBoto implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int octet = 0;
			int mascaraInt = 0;
			boolean error = false;
			Pattern pattern = Pattern.compile("[a-z]");
			for (int i = 0; i < 4; i++) {
				Matcher matcher = pattern.matcher(octets[i].getText());
				Matcher matcherMascara = pattern.matcher(mascara.getText());
				boolean matchFound = matcher.find();
				boolean matchFoundMascara = matcherMascara.find();
				if(octets[i].getText().isEmpty() || mascara.getText().isEmpty()) {
					errors.setText("Error: Hi ha un camp buit");
					error = true;
				}else if(matchFound || matchFoundMascara){
					errors.setText("Error: Hi ha text en algun dels camps");
					error = true;

				}else{
					octet = Integer.parseInt(octets[i].getText());
					mascaraInt = Integer.parseInt(mascara.getText());
				}
				if (octet > 255 || octet < 0) {
					errors.setText("Error: l'ip escrita es incorrecte");
					error = true;
				}else if(mascaraInt > 32 || mascaraInt < 0) {
					errors.setText("Error: la mascara es incorrecte");
					
					error = true;
				}
				}
			
	
			if (error == false) {
				calcularBinari();
				calcularMascara();
				calcularXarxa();
				errors.setText("");
			} else {
				for (int i = 0; i < 4; i++) {
					octetsBinari[i].setText("");
					octetsMascara[i].setText("");
					octetsXarxa[i].setText("");
				}
			}
			error = false;

		}

		
		public void calcularXarxa() {
			String[] array = new String[8];
			String total = "";
			int cont = 0;
			int valor1 = 0;
			int valor2 = 0;

			for (int i = 0; i < 4; i++) {
				cont = 0;
				for (int j = 0; j < 8; j++) {
					valor1 = Integer.parseInt(octetsBinari[i].getText().substring(cont, cont + 1));
					valor2 = Integer.parseInt(octetsMascara[i].getText().substring(cont, cont + 1));
					if (valor1 == 1 && valor2 == 1) {
						total = total + "1";
					} else {
						total = total + "0";
					}
					cont++;
				}
			}
			cont = 0;
			for (int j = 0; j < 4; j++) {
				array[j] = total.substring(cont, cont + 8);
				cont = cont + 8;
				octetsXarxa[j].setText(array[j]);
			}

		}

		public void calcularMascara() {

			int mascaraInt = Integer.parseInt(mascara.getText());
			int cont = 0;
			String total = "";
			String octet = "";
			String[] array = new String[8];

			while (total.length() != mascaraInt) {
				total = "1" + total;
			}

			while (total.length() < 32) {
				total = total + "0";
			}

			for (int j = 0; j < 4; j++) {
				array[j] = total.substring(cont, cont + 8);
				cont = cont + 8;
				octetsMascara[j].setText(array[j]);
			}

		}

		public void calcularBinari() {
			int total = 0;
			String resultat = "";
			for (int i = 0; i < octets.length; i++) {
				resultat = "";
				int num = Integer.parseInt(octets[i].getText());
				do {
					total = num % 2;
					resultat = total + resultat;
					num = num / 2;

				} while (num > 0);

				while (resultat.length() <= 7) {

					resultat = "0" + resultat;

				}

				octetsBinari[i].setText(resultat);

			}
		}

	}
}
