package interfacedao.maestros;

import modelo.maestros.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDAO extends JpaRepository<Cliente, String>{

}
