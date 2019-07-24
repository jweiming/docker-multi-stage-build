package net.midgard.medrec.service.impl;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.midgard.medrec.domain.Administrator;
import net.midgard.medrec.repository.AdminRepository;
import net.midgard.medrec.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author thor
 */
@Component
public class AdminServiceImpl implements AdminService {

  @Autowired
  private AdminRepository repo;

  @Override
  public Optional<Collection<Administrator>> findAll() {
    try {
      return Optional.ofNullable(repo.findAll());
    }
    finally {

    }

  }

  @Override
  public Optional<Administrator> findById(long id) {
    try{
      return Optional.ofNullable(repo.findById(id).get());
    }
    catch(Exception e){
      return Optional.empty();
    }
    finally{
      
    }
  }

  @Override
  public Optional<Administrator> findByUsername(String username) {
    try {
      return Optional.ofNullable(repo.findByUsername(username).iterator().next());
    }
    catch (NoSuchElementException e) {
      return Optional.empty();
    }
    finally {

    }
  }

  @Override
  public void addAdmin(Administrator admin) {
    try {
      repo.saveAndFlush(admin);
    }
    finally {
    }
  }

  @Override
  public void updateAdmin(Administrator admin) {
    try {
      repo.saveAndFlush(admin);
    }
    finally {
    }
  }

  @Override
  public void deleteAdmin(long id) {
    try {
      repo.deleteById(id);
    }
    finally {
    }
  }

  @Override
  public void deleteAll() {
    try {
      repo.deleteAllInBatch();
    }
    finally {

    }
  }

  @Override
  public boolean exists(long id) {
    try {
      return repo.existsById(id);
    }
    finally {
    }
  }

}
