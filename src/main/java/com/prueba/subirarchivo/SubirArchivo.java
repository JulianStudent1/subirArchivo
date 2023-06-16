
package com.prueba.subirarchivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SubirArchivo {
    
        public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost/ayhconsultores?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrival=true"; 
        String username = "rootDos";
        String password = "admin";
        String csvFilePath = "C:\\Users\\jeave\\OneDrive\\Documentos\\prue.csv"; 

        String line = "";
        String csvSplitBy = ",";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             Statement statement = connection.createStatement();
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {

            String insertQuery = "INSERT INTO ejercicio_dos (nombre, correo, fecha_registro) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Ignorar la inserción del campo "id"
            preparedStatement.setString(1, "");
            preparedStatement.setString(2, "");
            preparedStatement.setString(3, "");

            // Omitir la primera línea (encabezados)
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);
                //System.out.println(data[0]);
                //System.out.println(data[1]);
                //System.out.println(data[2]);
                System.out.println(data.length);
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                preparedStatement.setString(1, data[0]); // Columna 1 (nombre)
                preparedStatement.setString(2, data[1]); // Columna 2 (correo)
                preparedStatement.setString(3,data[2].replaceAll(";", "")); // Columna 3 (fecha_registro)
                
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            System.out.println("Archivo CSV cargado exitosamente a MySQL.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
