package interfacedao.maestros;

import modelo.maestros.Balanza;
import modelo.maestros.Transporte;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransporteDAO extends  JpaRepository<Transporte, Integer>{

}
