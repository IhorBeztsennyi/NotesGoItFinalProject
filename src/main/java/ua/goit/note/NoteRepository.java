package ua.goit.note;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRepository extends JpaRepository<NoteDao, UUID> {

}
