package interfacedao.maestros;

import modelo.maestros.Balanza;


import org.springframework.data.jpa.repository.JpaRepository;

public interface IBalanzaDAO extends JpaRepository<Balanza, Integer>{

}
