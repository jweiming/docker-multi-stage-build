package net.midgard.medrec.controller;

import io.micrometer.core.annotation.Timed;
import java.util.Collection;
import java.util.Optional;
import net.midgard.medrec.domain.Administrator;
import net.midgard.medrec.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author thor
 */
@RestController
@RequestMapping(value = "/admin")
@CrossOrigin(origins = "*")
@Timed
public class AdminController {

  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

  private final AdminService service;

  @Autowired
  public AdminController(AdminService service) {
    this.service = service;
  }

  @GetMapping(value="/all")
  public ResponseEntity<Collection<Administrator>> getAllAdmins() {
    try {
      Optional<Collection<Administrator>> admins = service.findAll();
      int admins_size = admins.get().size();
      if (admins_size > 0) {
        logger.info("Number of admins found={}", admins_size);
        admins.get().forEach((admin) -> {
          logger.info("Found:{}", admin);
        });
        return new ResponseEntity<>((Collection<Administrator>) admins.get(), HttpStatus.OK);
      }
      else {
        logger.warn("No Admin Found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.error("{}", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Administrator> getAdminById(@PathVariable int id) {
    try {
      Optional<Administrator> admin = service.findById(id);
      if (admin.isPresent()) {
        logger.info("Admin with id={} found:{}", id, admin.get());
        return new ResponseEntity<>(admin.get(), HttpStatus.OK);
      }
      else {
        logger.warn("Admin with id={} not found", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (NumberFormatException nfe) {
      logger.warn("{}", nfe);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    catch (Exception e) {
      logger.error("{}", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }

  @GetMapping(value = "/username/{username}")
  public ResponseEntity<Administrator> findAdminByUserName(
          @PathVariable(value = "username", required = true) String username) {
    try {
      Optional<Administrator> admin = service.findByUsername(username);
      if (admin.isPresent()) {
        logger.info("Admin with username={} found", username);
        return new ResponseEntity<>(admin.get(), HttpStatus.OK);
      }
      else {
        logger.warn("Admin with username={} not found", username);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }

  @PostMapping
  ResponseEntity<?> addAdmin(@RequestBody Administrator newAdmin) {
    try {
      Optional<Administrator> presentAdmin = service.findByUsername(newAdmin.getUsername());
      if (!presentAdmin.isPresent()) {
        logger.info("newAdmin with username={} not found. Saving {} to database.", newAdmin.getUsername(), newAdmin);
        service.addAdmin(newAdmin);
        return new ResponseEntity<>(HttpStatus.CREATED);
      }
      else {
        logger.warn("newAdmin with username={} was found. Not saving to database.", newAdmin.getUsername());
        return new ResponseEntity<>(HttpStatus.CONFLICT);
      }
    }
    catch (Exception e) {
      logger.error("{}", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Administrator> updateAdmin(@PathVariable(required = true, value = "id") int id, @RequestBody Administrator update) {
    try {
      if (service.exists(id)) {
        logger.info("Admin to update with id={} found. Updating database the database with {}.", update.getUsername(), update);
        update.setId(id);
        service.updateAdmin(update);
        return new ResponseEntity<>(HttpStatus.OK);
      }
      else {
        logger.warn("Admin to update with username={} not found. Not updating database.", update.getUsername());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.error("{}", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteAdmin(@PathVariable(required = true, value = "id") int id) {
    try {
      logger.info("Deleting admin with id={}", id);
      if (service.exists(id)) {
        service.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
      }
      else {
        logger.warn("Admin with id={} not found. Not deleting from database.", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      logger.error("{}", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    finally {
    }
  }
}
