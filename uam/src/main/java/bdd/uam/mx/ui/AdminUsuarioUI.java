package bdd.uam.mx.ui;


import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import bdd.uam.mx.model.Usuario;
import bdd.uam.mx.repository.UsuarioRepository;
import bdd.uam.mx.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class AdminUsuarioUI extends JFrame {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkActivo;
    private JButton btnGuardar;

    @PostConstruct
    private void initUI() {
        setTitle("Administrador de Usuarios - Censo");
        setSize(350, 250);
        setLocationRelativeTo(null); // Centrar en pantalla
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                setState(JFrame.ICONIFIED); // minimiza
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Componentes de la UI
        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Usuario Activo:"));
        chkActivo = new JCheckBox();
        chkActivo.setSelected(true); // Activo por defecto
        panel.add(chkActivo);

        btnGuardar = new JButton("Guardar Usuario");
        panel.add(new JLabel("")); // Espacio vacío
        panel.add(btnGuardar);

        btnGuardar.addActionListener(this::guardarUsuario);

        add(panel);
    }

    private void guardarUsuario(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        boolean activo = chkActivo.isSelected();

        // Validaciones básicas
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El usuario y la contraseña son obligatorios.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuarioRepository.findByUsername(username).isPresent()) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe.", 
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Crear la entidad
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            
            //Encriptar la contrasenia
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            
            nuevoUsuario.setActivo(activo);


            //Guardado en las tres bases de datos
            usuarioService.guardarEnTodasLasZonas(nuevoUsuario);

            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar los campos
            txtUsername.setText("");
            txtPassword.setText("");
            chkActivo.setSelected(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}