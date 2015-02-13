package interfacedao.maestros;

import modelo.maestros.Conductor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IConductorDAO extends JpaRepository<Conductor, String> {

}
