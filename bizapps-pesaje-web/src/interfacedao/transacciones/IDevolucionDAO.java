package interfacedao.transacciones;

import modelo.transacciones.Devolucion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDevolucionDAO  extends JpaRepository<Devolucion, Long>{

}
