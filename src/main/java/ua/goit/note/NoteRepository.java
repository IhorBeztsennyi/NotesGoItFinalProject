package ua.goit.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteDao, UUID> {


    Optional<NoteDao> findById(UUID id);

    @Query("SELECT n FROM note n where n.name IN (?1)")
    NoteDao findByName(String name);

    @Query("SELECT n FROM note n where n.user.id (?1)")
    List<NoteDao> findNotesByUserId(UUID userId);




}
