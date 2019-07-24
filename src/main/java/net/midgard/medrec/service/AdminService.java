package net.midgard.medrec.service;

import java.util.Collection;
import java.util.Optional;
import net.midgard.medrec.domain.Administrator;
import org.springframework.stereotype.Service;

/**
 *
 * @author thor
 */

@Service
public interface AdminService {

  public Optional<Collection<Administrator>> findAll();

  public Optional<Administrator> findById(long id);

  public Optional<Administrator> findByUsername(String username);

  public void addAdmin(Administrator admin);

  public void updateAdmin(Administrator admin);

  public void deleteAdmin(long id);

  public void deleteAll();

  public boolean exists(long id);
}
