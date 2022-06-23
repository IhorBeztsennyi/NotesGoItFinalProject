package ua.goit.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteDao, UUID> {


    Optional<NoteDao> findById(UUID uuid);

    @Query("from note as n where n.name=:name")
    NoteDao findByName(String name);

    @Query("from note as n where n.user_id=:userId")
    List<NoteDao> findNotesByUserId(UUID userId);




}
