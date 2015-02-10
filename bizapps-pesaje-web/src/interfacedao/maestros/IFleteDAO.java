package interfacedao.maestros;

import modelo.maestros.Flete;
import modelo.maestros.Transporte;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IFleteDAO  extends  JpaRepository<Flete, Integer>{

}
