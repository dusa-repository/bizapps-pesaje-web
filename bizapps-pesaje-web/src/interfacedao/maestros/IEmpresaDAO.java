package interfacedao.maestros;

import modelo.maestros.Empresa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpresaDAO extends JpaRepository<Empresa, String> {

}
