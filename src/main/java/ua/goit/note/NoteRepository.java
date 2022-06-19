package ua.goit.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteDao, UUID> {

    @Override
    Optional<NoteDao> findById(UUID uuid);

    List<NoteDao> findByName(String name);
}
