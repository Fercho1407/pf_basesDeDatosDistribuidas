package bdd.uam.mx;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import bdd.uam.mx.ui.AdminUsuarioUI;

import java.awt.EventQueue;

@SpringBootApplication
public class UamApplication {

	public static void main(String[] args) {
        // 1. Configuramos Spring para que permita interfaces gráficas (headless = false)
        ConfigurableApplicationContext context = new SpringApplicationBuilder(UamApplication.class)
                .headless(false)
                .run(args);

        // 2. Ejecutamos la ventana de Swing en el hilo correcto de Java AWT
        EventQueue.invokeLater(() -> {
            // Obtenemos nuestra ventana instanciada por Spring (con los repositorios inyectados)
            AdminUsuarioUI ex = context.getBean(AdminUsuarioUI.class);
            ex.setVisible(true);
        });
    }
}
