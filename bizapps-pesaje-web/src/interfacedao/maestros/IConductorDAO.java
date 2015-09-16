package interfacedao.maestros;

import java.util.List;

import modelo.maestros.Ciudad;
import modelo.maestros.Conductor;
import modelo.maestros.Transporte;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IConductorDAO extends JpaRepository<Conductor, String> {

	List<Conductor> findByCiudad(Ciudad ciudad);

	List<Conductor> findByCiudadIn(List<Ciudad> eliminarLista);

	List<Conductor> findByTransporteIn(List<Transporte> eliminarLista);

	List<Conductor> findByTransporte(Transporte transporte);

}
