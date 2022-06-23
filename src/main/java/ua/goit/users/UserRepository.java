package ua.goit.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserDao, UUID> {

    Optional<UserDao> findByEmail(String email);

    Optional<UserDao> findByUsername(String email);

    @Query("from users as u where u.username=:name")
    UserDao findByName(String name);

}
