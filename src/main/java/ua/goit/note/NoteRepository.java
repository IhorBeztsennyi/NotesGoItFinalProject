package ua.goit.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteDao, UUID> {

    @Override
    Optional<NoteDao> findById(UUID uuid);

    Optional<NoteDao> findByName(String name);

    @Query("SELECT n FROM NoteDao n WHERE n.user IN (?1)")
    List<NoteDao> findAllByUserId(UUID id);

    @Query("SELECT n FROM NoteDao n WHERE n.accessType IN (?1)")
    List<NoteDao> findAllPublicNotes(Access access);
}
