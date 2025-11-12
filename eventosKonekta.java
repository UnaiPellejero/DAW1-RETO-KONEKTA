import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class eventosKonekta {
    
    public static void main(String[] args) {
        try {
            // --- 1. CONEXIÓN  ---

            Class.forName("com.mysql.cj.jdbc.Driver");

            // La conexión con la base de datos
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/eventos", "root", ""
            );

            System.out.println("¡Conexión a la base de datos 'eventos' exitosa!");
            System.out.println("----------------------------------------------");

            // Crea un objeto Statement para poder ejecutar sentencias SQL
            Statement statement = connection.createStatement();

            // 1: Cantidad total de personas inscritas
            // Usamos 'AS total' para poder acceder a la columna por nombre
            ResultSet rs1 = statement.executeQuery("SELECT COUNT(*) AS total FROM Inscripciones");

            if (rs1.next()) {
                int total = rs1.getInt("total"); // Obtenemos el valor por nombre
                System.out.println("Total de personas inscritas: " + total);
            }

            
            // 2: Número de personas inscritas a cada evento
            System.out.println("\nNúmero de personas por evento");
            // Usamos 'GROUP BY' para que la BBDD agrupe y cuente por nosotros
            ResultSet rs2 = statement.executeQuery(
                "SELECT eventos, COUNT(*) AS cantidad FROM Inscripciones GROUP BY eventos"
            );

            // Recorremos cada fila del resultado (cada evento)
            while (rs2.next()) {
                String evento = rs2.getString("eventos");
                int cantidad = rs2.getInt("cantidad");
                System.out.println("  - " + evento + ": " + cantidad + " persona(s)");
            }


            // 3: Listado de personas por evento
            System.out.println("\nListado de personas por evento");
            // Pedimos los datos ordenados por evento para mostrarlos agrupados
            ResultSet rs3 = statement.executeQuery(
                "SELECT eventos, nombre, apellidos FROM Inscripciones ORDER BY eventos"
            );

            String eventoActual = ""; // Para saber cuándo cambia el grupo
            while (rs3.next()) {
                String evento = rs3.getString("eventos");
                String nombre = rs3.getString("nombre");
                String apellidos = rs3.getString("apellidos");

                // Si este 'evento' es diferente al de la fila anterior,
                // imprimimos un nuevo encabezado de evento.
                if (!evento.equals(eventoActual)) {
                    System.out.println("\n  Evento: " + evento);
                    eventoActual = evento; // Actualizamos el evento que estamos viendo
                }
                
                // Imprimimos a la persona
                System.out.println("    - " + nombre + " " + apellidos);
            }
            
            // Cierra la conexión con la base de datos
            connection.close();
            System.out.println("\n----------------------------------------------");
            System.out.println("Consultas finalizadas. Conexión cerrada.");

        } catch (Exception e) {
            // Captura de errores
            System.out.println("Ha ocurrido un error:");
            e.printStackTrace();
        }
    }
}