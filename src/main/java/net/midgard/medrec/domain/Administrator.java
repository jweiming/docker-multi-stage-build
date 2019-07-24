package net.midgard.medrec.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional;
import lombok.Data;

/**
 *
 * @author thor
 */
@Entity
@Table(name = "Administrators")
@Transactional
@Data
public class Administrator implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "username")
  private String username;

  @JsonIgnore
  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "country")
  private String country;

  public Administrator() {

  }

  @JsonCreator
  public Administrator(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("email") String email, @JsonProperty("country") String country
  ) {
    this.username = username;
    this.email = email;
    this.country = country;
    this.password = "welcome1";
  }
}
