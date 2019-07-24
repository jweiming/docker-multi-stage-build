package net.midgard.medrec.repository;

import java.util.Collection;
import net.midgard.medrec.domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author thor
 */

@Repository
public interface AdminRepository extends JpaRepository<Administrator, Long> {

  Collection<Administrator> findByUsername(String username);

  Collection<Administrator> findByEmail(String email);

  Collection<Administrator> findByCountry(String country);
}
