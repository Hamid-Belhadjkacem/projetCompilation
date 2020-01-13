/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author pcstar
 */
public class Compiler extends javax.swing.JFrame {

    /**
     * Creates new form Compiler1
     */
 
    public Compiler() {
        initComponents();
    }
    
    static JFileChooser file_chooser = new JFileChooser("C:\\Users");
	static FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers text", "snl");
	static ArrayList<String> mots = new ArrayList<String>();
	static ArrayList<String> lignes = new ArrayList<String>();
	static ArrayList<String> sortie_lexic = new ArrayList<String>();
	static String[] mot;
        
        
    
        
    void charger() throws FileNotFoundException {
		file_chooser.addChoosableFileFilter(filter);
		if(file_chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			File file = file_chooser.getSelectedFile();
			Scanner sc_lignes = new Scanner(file);
			Scanner sc_mots = new Scanner(file);
			mots.clear();
			lignes.clear();
				while(sc_lignes.hasNextLine()){
					lignes.add(sc_lignes.nextLine());
				}
				while(sc_mots.hasNext()){
					mots.add(sc_mots.next());
					}

			sc_mots.close();
			sc_lignes.close();
			}
	}
    
     void lexicale(List<String> liste) {
         VerificatioMethods v = new VerificatioMethods();
		int i = 0;

		while (i < mots.size()) {
			if (v.UL_reserve(mots.get(i)) != null) {
				sortie_lexic.add(v.UL_reserve(mots.get(i)));
			} else if (v.id(mots.get(i)) != null) {
				sortie_lexic.add(v.id(mots.get(i)));
			} else if (v.num(mots.get(i)) != null) {
				sortie_lexic.add(v.num(mots.get(i)));
			}
			else sortie_lexic.add("Erreur");

			i++;
		}

	}
     String syntax(String chaine){
        VerificatioMethods v = new VerificatioMethods();
                v.num(chaine);
                v.isNum(chaine, WIDTH);
                String[] mot;
                
                
			if(chaine.equals("startprogramme")) return "Début du programme";
		else if(chaine.equals("Else")) return "SINON";
		else if(chaine.equals("Start")) return "Début d'un bloc";
		else if(chaine.equals("Finish")) return "Fin d'un bloc";
		else if(chaine.startsWith("//.")) return "un commentaire";
		else if(chaine.equals("endprogramme")) return "Fin du programme";
		else if(chaine.startsWith("showmes '") && chaine.endsWith("';;")) return "Affichage d'un message à l'ecran";
		else if(chaine.contains(" ")) {
			mot = chaine.split(" ");
			int i=0, k=1;

				if(mot[i].equals("int_number")){
					i++;
						if(v.id(mot[i]) != null){
							i++;
							while(mot[i].equals(",")){
								i++;
								k++;
								if(v.id(mot[i]) != null) i++;
							}
							if(mot[i].equals(";;")) return "Déclaration de "+k+" variables entiers";
						}					
				}
				else if(mot[i].equals("affect")){
					i++;
					if(v.id(mot[i]) != null){
						i++;
						if(v.num(mot[i]) == "Nombre entier") {
							i++;
							if(mot[i].equals(";;")) return "affectation dune valeur entiere à "+mot[i-2];
						}
						else if(v.num(mot[i]) == "Nombre reel"){
							i++;
							if(mot[i].equals(";;")) return "affectation dune valeur reel à "+mot[i-2];
						}
				}
				}				
				else if(mot[i].equals("real_number")){
					i++;
						if(v.id(mot[i]) != null)i++;
							if(mot[i].equals(";;")) return "Déclaration de  variable reel";
						}
				else if(mot[i].equals("If")){
					i++;
					if(mot[i].equals("--")){
						i++;
					if(v.id(mot[i]) != null){
						i++;
						if(mot[i].equals("<") || mot[i].equals(">") || mot[i].equals("==")){
						i++;
						if(v.id(mot[i]) != null){
							i++;
						if(mot[i].equals("--")) return "condition"; 
							}}}}
				}
				
				
				
				else if(mot[i].equals("Get")){
					i++;
					if(v.id(mot[i]) != null){
						i++;
					if(mot[i].equals("From")){
						i++;
						if(v.id(mot[i]) != null) {
							i++;
							if(mot[i].equals(";;")) return "affectation de "+mot[i-3]+" a "+mot[i-1];						}
					}
				}

				}

	else if(mot[i].equals("showmes")){
	i++;
        if(v.id(mot[i]) != null)i++;
	if(mot[i].equals(";;")) return "System.out.println("+mot[i-1]+");";
						}
		}
		return "erreur de syntaxe";
     
     }
     public String semantique(String chaine){
         
         VerificatioMethods v = new VerificatioMethods();
         
		if(chaine.equals("startprogramme")) return "public static void main(String[] args) {";
		else if(chaine.equals("Else")) return "else";
		else if(chaine.equals("Start")) return "{";
                else if(chaine.equals("--")) return "deubit de condition"; 
		else if(chaine.equals("Finish")) return "}";
		else if(chaine.startsWith("//.")) return "/*ceci est un commentaire*/";
		else if(chaine.equals("endprogramme")) return "}";
		else if(chaine.startsWith("showmes") && chaine.endsWith(";;")) return "System.out.println(\"Affichage d'un message à l'ecran\");";
		else if(chaine.contains(" ")) {
			mot = chaine.split(" ");
			int i=0;

				if(mot[i].equals("int_number")){
					i++;
						if(v.id(mot[i]) != null){
							i++;
							while(mot[i].equals(",")){
								i++;
								if(v.id(mot[i]) != null) i++;
							}
							if(mot[i].equals(";;"))return "int"+" "+mot[i-7]+","+mot[i-5]+","+mot[i-3]+","+mot[i-1]+";";
						}
					

				}
				
				else if(mot[i].equals("affect")){
					i++;
					if(v.id(mot[i]) != null){
					i++;
						if( v.num(mot[i]) == "Nombre entier") {
							i++;
							if(mot[i].equals(";;")) return mot[i-2]+"="+mot[i-1]+";";
						}
						else if(v.num(mot[i]) == "Nombre reel"){
							i++;
							if(mot[i].equals(";;")) return mot[i-3]+"="+mot[i-1]+";";
						}

					
				}

				}
				
				else if(mot[i].equals("real_number")){
					i++;
					if(mot[i].equals(" "))i++;
						if(v.id(mot[i]) != null)i++;
							if(mot[i].equals(";;")) return "float "+mot[i-1]+";";
						}


				
				
				else if(mot[i].equals("If")){
					i++;
					if(mot[i].equals("--")){
						i++;
					if(v.id(mot[i]) != null){
						i++;
						if(mot[i].equals("<") || mot[i].equals(">") || mot[i].equals("==")){
						i++;
						if(v.id(mot[i]) != null){
							i++;
						if(mot[i].equals("--")) return "if"+"("+mot[i-3]+mot[i-2]+mot[i-1]+")"; 
							}}}}
				}
				
				
				
				else if(mot[i].equals("Get")){
					i++;
					if(v.id(mot[i]) != null){
						i++;
					if(mot[i].equals("From")){
						i++;
						if(v.id(mot[i]) != null) {
							i++;
							if(mot[i].equals(";;")) return  mot[i-3]+"="+mot[i-1]+";";
						}

					}

				}

				}
				
				
				else if(mot[i].equals("showmes")){
					i++;
					if(mot[i].equals(" "))i++;
						if(v.id(mot[i]) != null)i++;
							if(mot[i].equals(";;")) return "System.out.println("+mot[i-1]+");";
						}

				

				
								}
		return "erreur symantique";
		
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(153, 0, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 340, 481));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton1.setText("Analyse Syntaxic");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 430, 70));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton2.setText("Charger");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 430, 70));

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jButton3.setText("Analyse Symantique");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 430, 70));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton4.setText("Analyse Lexical");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 430, 70));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
try {
jTextArea1.setText("");
charger();
int i = 0;
while (i < lignes.size()) {
	jTextArea1.setText(jTextArea1.getText()+lignes.get(i)+"\n");
i++;}
} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
e.printStackTrace();
}
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
 jTextArea1.setText("");
int i = 0;
while (i < lignes.size()) {
jTextArea1.setText(jTextArea1.getText()+lignes.get(i) + " --> " +syntax(lignes.get(i))+"\n");
i++;}



	
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
              jTextArea1.setText("");
int i = 0;
						
						
while (i < lignes.size()) {
jTextArea1.setText(jTextArea1.getText()+lignes.get(i) + " --> " +semantique(lignes.get(i))+"\n");
							
i++;
}						

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jTextArea1.setText("");
						lexicale(mots);
						int i = 0;
						while (i < mots.size()) {
							jTextArea1.setText(jTextArea1.getText()+mots.get(i) + " --> " + sortie_lexic.get(i)+"\n");
							i++;}
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compiler().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
